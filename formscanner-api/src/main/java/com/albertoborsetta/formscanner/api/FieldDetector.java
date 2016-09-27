package com.albertoborsetta.formscanner.api;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.Callable;

import com.albertoborsetta.formscanner.api.commons.Constants;

/**
 *
 * @author Alberto Borsetta
 * @version 1.1.3-SNAPSHOT
 */
public class FieldDetector extends FormScannerDetector implements Callable<HashMap<String, FormQuestion>> {

	private final FormQuestion templateField;
	private final int size;
	private final HashMap<String, FormQuestion> fields;

	FieldDetector(int threshold, int density, int size, FormTemplate template, FormQuestion templateField,
			BufferedImage image) {
		super(threshold, density, image, template);
		this.templateField = templateField;
		this.size = size;
		fields = new HashMap<>();
	}

	@Override
	public HashMap<String, FormQuestion> call() throws Exception {
		boolean found = false;
		int count = 0;

		double bestFilled = 0d;
		double lastFilled;

		HashMap<String, FormPoint> templatePoints = templateField.getPoints();
		String fieldName = templateField.getName();

		ArrayList<String> pointNames = new ArrayList<>(templatePoints.keySet());
		Collections.sort(pointNames);

		for (String pointName : pointNames) {
			FormPoint responsePoint = calcResponsePoint(templatePoints.get(pointName));

			lastFilled = isFilled(responsePoint);
			if (lastFilled > bestFilled) {
				found = true;
				count++;
				FormQuestion filledField = getField(fieldName);

				if (!templateField.isMultiple()) {
					if (count > 1) {						
						filledField.clearPoints();
						fields.clear();					
						
						if (templateField.rejectMultiple()) {
							filledField.setPoint(Constants.NO_RESPONSE, Constants.EMPTY_POINT);
							fields.put(fieldName, filledField);
							break;
						}
					}
					
					bestFilled = lastFilled;
				}
				
				filledField.setPoint(pointName, responsePoint);
				fields.put(fieldName, filledField);
			}
		}

		if (!found) {
			FormQuestion filledField = getField(fieldName);
			filledField.setPoint(Constants.NO_RESPONSE, Constants.EMPTY_POINT);
			fields.put(fieldName, filledField);
		}

		return fields;
	}

	private double isFilled(FormPoint responsePoint) {
		int total = size * size;
		int halfSize = size / 2;
		int[] rgbArray = new int[total];
		int count = 0;

		int xCoord = (int) responsePoint.getX();
		int yCoord = (int) responsePoint.getY();

		image.getRGB(xCoord - halfSize, yCoord - halfSize, size, size, rgbArray, 0, size);
		for (int i = 0; i < total; i++) {
			if ((rgbArray[i] & (0xFF)) < threshold) {
				count++;
			}
		}
		double filled = -1d;
		if ((count / (double) total) >= (density / 100.0)) {
			filled = count / (double) total;
		}
		return filled;
		// return (count / (double) total) >= (density / 100.0);
	}

	private FormQuestion getField(String fieldName) {
		FormQuestion filledField = fields.get(fieldName);

		if (filledField == null) {
			filledField = new FormQuestion(fieldName);
			filledField.setMultiple(templateField.isMultiple());
			filledField.setType(templateField.getType());
			filledField.setRejectMultiple(templateField.rejectMultiple());
		}

		return filledField;
	}
}
