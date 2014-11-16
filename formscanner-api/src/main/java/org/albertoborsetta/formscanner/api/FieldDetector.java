package org.albertoborsetta.formscanner.api;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.concurrent.Callable;

import org.albertoborsetta.formscanner.api.FormField;
import org.albertoborsetta.formscanner.api.FormPoint;
import org.albertoborsetta.formscanner.api.FormTemplate;
import org.albertoborsetta.formscanner.api.commons.Constants.Corners;;

/**
 * The Class FieldDetector.
 */
public class FieldDetector implements Callable<HashMap<String, FormField>> {

	/** The template. */
	private FormTemplate template;
	
	/** The threshold. */
	private int threshold;
	
	/** The density. */
	private int density;
	
	/** The template field. */
	private FormField templateField;
	
	/** The size. */
	private int size;
	
	/** The parent. */
	private FormTemplate parent;
	
	/** The fields. */
	private HashMap<String, FormField> fields;
	
	/** The image. */
	BufferedImage image;

	/**
	 * Instantiates a new field detector.
	 *
	 * @author Alberto Borsetta
	 * @param threshold the threshold
	 * @param density the density
	 * @param size the size
	 * @param template the template
	 * @param templateField the template field
	 * @param image the image
	 */
	FieldDetector(int threshold, int density, int size, FormTemplate template,
			FormField templateField, BufferedImage image) {
		this.template = template;
		parent = template.getParentTemplate();
		this.threshold = threshold;
		this.density = density;
		this.templateField = templateField;
		this.size = size;
		fields = new HashMap<String, FormField>();
		this.image = image;
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	public HashMap<String, FormField> call() throws Exception {
		boolean found = false;
		int count = 0;

		HashMap<String, FormPoint> templatePoints = templateField.getPoints();
		String fieldName = templateField.getName();

		ArrayList<String> pointNames = new ArrayList<String>(
				templatePoints.keySet());
		Collections.sort(pointNames);

		for (String pointName : pointNames) {
			FormPoint responsePoint = calcResponsePoint(templatePoints
					.get(pointName));

			if (found = isFilled(responsePoint)) {
				count++;
				FormField filledField = getField(templateField, fieldName);
				filledField.setPoint(pointName, responsePoint);
				fields.put(fieldName, filledField);
				if (!templateField.isMultiple()) {
					if (!templateField.rejectMultiple()) {
						break;
					} if (templateField.rejectMultiple() && count > 1) {
						fields.clear();
						break;
					}
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

	/**
	 * Calc response point.
	 *
	 * @author Alberto Borsetta
	 * @param responsePoint the response point
	 * @return the form point
	 */
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

	/**
	 * Checks if is filled.
	 *
	 * @author Alberto Borsetta
	 * @param responsePoint the response point
	 * @return true, if is filled
	 */
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

	/**
	 * Gets the field.
	 *
	 * @author Alberto Borsetta
	 * @param field the field
	 * @param fieldName the field name
	 * @return the field
	 */
	private FormField getField(FormField field, String fieldName) {
		FormField filledField = fields.get(fieldName);

		if (filledField == null) {
			filledField = new FormField(fieldName);
			filledField.setMultiple(field.isMultiple());
		}

		return filledField;
	}
}
