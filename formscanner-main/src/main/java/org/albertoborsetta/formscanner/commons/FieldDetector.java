package org.albertoborsetta.formscanner.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.Callable;

import org.albertoborsetta.formscanner.commons.FormScannerConstants.Corners;

public class FieldDetector implements Callable<HashMap<String, FormField>> {

	private FormTemplate template;
	private int threshold;
	private int density;
	private FormField templateField;
	private int size;
	private FormTemplate parent;
	private HashMap<String, FormField> fields;

	FieldDetector(int threshold, int density, int size, FormTemplate template,
			FormField templateField) {
		this.template = template;
		parent = template.getParentTemplate();
		this.threshold = threshold;
		this.density = density;
		this.templateField = templateField;
		this.size = size;
		fields = new HashMap<String, FormField>();
	}

	@Override
	public HashMap<String, FormField> call() throws Exception {
		boolean found = false;

		HashMap<String, FormPoint> templatePoints = templateField.getPoints();
		String fieldName = templateField.getName();

		ArrayList<String> pointNames = new ArrayList<String>(
				templatePoints.keySet());
		Collections.sort(pointNames);

		for (String pointName : pointNames) {
			FormPoint responsePoint = calcResponsePoint(templatePoints
					.get(pointName));

			if (found = isFilled(responsePoint)) {
				FormField filledField = getField(templateField, fieldName);
				filledField.setPoint(pointName, responsePoint);
				fields.put(fieldName, filledField);
				if (!templateField.isMultiple()) {
					break;
				}
			}
		}

		if (!found) {
			FormField filledField = getField(templateField, fieldName);
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

		point.relativePositionTo(templateOrigin, templateRotation);
		point.scale(scale);
		point.originalPositionFrom(template.getCorners().get(Corners.TOP_LEFT),
				template.getRotation());
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
			template.getImage().getRGB(xCoord - halfSize, yCoord - halfSize,
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

	private FormField getField(FormField field, String fieldName) {
		FormField filledField = fields.get(fieldName);

		if (filledField == null) {
			filledField = new FormField(fieldName);
			filledField.setMultiple(field.isMultiple());
		}

		return filledField;
	}
}
