package com.albertoborsetta.formscanner.api;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.Callable;

import com.albertoborsetta.formscanner.api.commons.Constants.Corners;

public class FieldDetector implements Callable<HashMap<String, FormQuestion>> {

    private final FormTemplate template;
    private final int threshold;
    private final int density;
    private final FormQuestion templateField;
    private final int size;
    private final FormTemplate parent;
    private final HashMap<String, FormQuestion> fields;
    private final BufferedImage image;

    FieldDetector(int threshold, int density, int size, FormTemplate template,
            FormQuestion templateField, BufferedImage image) {
        this.template = template;
        parent = template.getParentTemplate();
        this.threshold = threshold;
        this.density = density;
        this.templateField = templateField;
        this.size = size;
        fields = new HashMap<>();
        this.image = image;
    }

    @Override
    public HashMap<String, FormQuestion> call() throws Exception {
        boolean found = false;
        int count = 0;

        HashMap<String, FormPoint> templatePoints = templateField.getPoints();
        String fieldName = templateField.getName();

        ArrayList<String> pointNames = new ArrayList<>(
                templatePoints.keySet());
        Collections.sort(pointNames);

        for (String pointName : pointNames) {
            FormPoint responsePoint = calcResponsePoint(templatePoints
                    .get(pointName));

            if (found = isFilled(responsePoint)) {
                count++;
                FormQuestion filledField = getField(templateField, fieldName);

                filledField.setPoint(pointName, responsePoint);
                fields.put(fieldName, filledField);

                if (!templateField.isMultiple()) {
                    if (templateField.rejectMultiple() && count > 1) {
                        filledField.clearPoints();
                        filledField.setPoint("", null);
                        fields.clear();
                        fields.put(fieldName, filledField);
                        break;
                    }
                    if (!templateField.rejectMultiple()) {
                        break;
                    }
                }
            }
        }

        if (!found) {
            FormQuestion filledField = getField(templateField, fieldName);
            filledField.setPoint("", null);
            fields.put(fieldName, filledField);
        }

        return fields;
    }

    private FormPoint calcResponsePoint(FormPoint responsePoint) {
        FormPoint point = responsePoint.clone();

        FormPoint templateOrigin = parent.getCorner(Corners.TOP_LEFT);
        double templateRotation = parent.getRotation();
        double scale = Math.sqrt(template.getDiagonal() / parent.getDiagonal());

        point.rotoTranslate(templateOrigin, templateRotation, true);
        point.scale(scale);
        point.rotoTranslate(template.getCorners().get(Corners.TOP_LEFT), template.getRotation(), false);
        return point;
    }

    private boolean isFilled(FormPoint responsePoint) {
        int total = size * size;
        int halfSize = (int) size / 2;
        int[] rgbArray = new int[total];
        int count = 0;

        int xCoord = (int) responsePoint.getX();
        int yCoord = (int) responsePoint.getY();

        try {
            image.getRGB(xCoord - halfSize, yCoord - halfSize,
                    size, size, rgbArray, 0, size);
            for (int i = 0; i < total; i++) {
                if ((rgbArray[i] & (0xFF)) < threshold) {
                    count++;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return (count / (double) total) >= (density / 100.0);
    }

    private FormQuestion getField(FormQuestion field, String fieldName) {
        FormQuestion filledField = fields.get(fieldName);

        if (filledField == null) {
            filledField = new FormQuestion(fieldName);
            filledField.setMultiple(field.isMultiple());
        }

        return filledField;
    }
}
