package org.albertoborsetta.formscanner.api;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.albertoborsetta.formscanner.api.FormField;
import org.albertoborsetta.formscanner.api.FormPoint;
import org.albertoborsetta.formscanner.api.FormTemplateWrapper;
import org.albertoborsetta.formscanner.api.commons.Constants.Corners;
import org.apache.commons.io.FilenameUtils;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

// TODO: Auto-generated Javadoc
/**
 * The Class FormTemplate.
 */
public class FormTemplate {

	/** The name. */
	private String name;
	
	/** The fields. */
	private HashMap<String, FormField> fields;
	
	/** The corners. */
	private HashMap<Corners, FormPoint> corners;
	
	/** The point list. */
	private ArrayList<FormPoint> pointList;
	
	/** The rotation. */
	private double rotation;
	
	/** The template. */
	private FormTemplate template;
	
	/** The height. */
	private int height;
	
	/** The width. */
	private int width;
	
	/** The diagonal. */
	private double diagonal;

	/**
	 * Instantiates a new form template.
	 *
	 * @author Alberto Borsetta
	 * @param name the name
	 */
	public FormTemplate(String name) {
		this(name, null);
	}
	
	/**
	 * Instantiates a new form template.
	 *
	 * @author Alberto Borsetta
	 * @param file the file
	 * @throws ParserConfigurationException the parser configuration exception
	 * @throws SAXException the SAX exception
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public FormTemplate(File file) throws ParserConfigurationException, SAXException, IOException {
		this(FilenameUtils.removeExtension(file.getName()), null);
		FormTemplateWrapper.presetFromTemplate(file, this);
	}
	
	/**
	 * Instantiates a new form template.
	 *
	 * @author Alberto Borsetta
	 * @param name the name
	 * @param template the template
	 */
	public FormTemplate(String name, FormTemplate template) {
		this.template = template;
		this.name = name;

		corners = new HashMap<Corners, FormPoint>();
		setDefaultCornerPosition();
		calculateDiagonal();
		rotation = 0;
		fields = new HashMap<String, FormField>();
		pointList = new ArrayList<FormPoint>();
	}

	/**
	 * Calculate diagonal.
	 * 
	 * @author Alberto Borsetta
	 */
	public void calculateDiagonal() {
		diagonal = (corners.get(Corners.TOP_LEFT).dist2(
				corners.get(Corners.BOTTOM_RIGHT)) + corners.get(
				Corners.TOP_RIGHT).dist2(corners.get(Corners.BOTTOM_LEFT))) / 2;
	}

	/**
	 * Sets the default corner position.
	 * 
	 * @author Alberto Borsetta
	 */
	private void setDefaultCornerPosition() {
		int x;
		int y;

		for (Corners position : Corners.values()) {
			x = 0;
			y = 0;

			switch (position) {
			case TOP_RIGHT:
				x = width;
				break;
			case BOTTOM_LEFT:
				y = height;
				break;
			case BOTTOM_RIGHT:
				x = width;
				y = height;
				break;
			default:
				break;
			}

			FormPoint corner = new FormPoint(x, y);
			corners.put(position, corner);
		}
	}

	/**
	 * Gets the field.
	 *
	 * @author Alberto Borsetta
	 * @param name the name
	 * @return the field
	 */
	public FormField getField(String name) {
		return fields.get(name);
	}

	/**
	 * Gets the fields.
	 *
	 * @author Alberto Borsetta
	 * @return the fields
	 */
	public HashMap<String, FormField> getFields() {
		return fields;
	}

	/**
	 * Sets the field.
	 *
	 * @author Alberto Borsetta
	 * @param name the name
	 * @param field the field
	 */
	public void setField(String name, FormField field) {
		fields.put(name, field);
		for (Entry<String, FormPoint> point : field.getPoints().entrySet()) {
			if (point.getValue() != null) {
				pointList.add(point.getValue());
			}
		}
	}

	/**
	 * Sets the fields.
	 *
	 * @author Alberto Borsetta
	 * @param fields the fields
	 */
	public void setFields(HashMap<String, FormField> fields) {
		for (Entry<String, FormField> field : fields.entrySet()) {
			setField(field.getKey(), field.getValue());
		}
	}

	/**
	 * Sets the corners.
	 *
	 * @author Alberto Borsetta
	 * @param corners the corners
	 */
	public void setCorners(HashMap<Corners, FormPoint> corners) {
		this.corners = corners;
		calculateRotation();
	}

	/**
	 * Sets the corner.
	 *
	 * @author Alberto Borsetta
	 * @param corner the corner
	 * @param point the point
	 */
	public void setCorner(Corners corner, FormPoint point) {
		corners.put(corner, point);
		calculateRotation();
		calculateDiagonal();
	}

	/**
	 * Gets the corners.
	 *
	 * @author Alberto Borsetta
	 * @return the corners
	 */
	public HashMap<Corners, FormPoint> getCorners() {
		return corners;
	}

	/**
	 * Gets the rotation.
	 *
	 * @author Alberto Borsetta
	 * @return the rotation
	 */
	public double getRotation() {
		return rotation;
	}

	/**
	 * Sets the rotation.
	 *
	 * @author Alberto Borsetta
	 * @param rotation the new rotation
	 */
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	/**
	 * Gets the field points.
	 *
	 * @author Alberto Borsetta
	 * @return the field points
	 */
	public ArrayList<FormPoint> getFieldPoints() {
		return pointList;
	}

	/**
	 * Removes the field by name.
	 *
	 * @author Alberto Borsetta
	 * @param fieldName the field name
	 */
	public void removeFieldByName(String fieldName) {

		FormField field = fields.get(fieldName);
		for (Entry<String, FormPoint> point : field.getPoints().entrySet()) {
			pointList.remove(point.getValue());
		}

		fields.remove(fieldName);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return FormTemplateWrapper.getString(this);
	}

	/**
	 * Gets the name.
	 *
	 * @author Alberto Borsetta
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the corner.
	 *
	 * @author Alberto Borsetta
	 * @param corner the corner
	 * @return the corner
	 */
	public FormPoint getCorner(Corners corner) {
		return corners.get(corner);
	}

	/**
	 * Gets the diagonal.
	 *
	 * @author Alberto Borsetta
	 * @return the diagonal
	 */
	public double getDiagonal() {
		return diagonal;
	}
	
	/**
	 * Sets the diagonal.
	 *
	 * @author Alberto Borsetta
	 * @param diag the new diagonal
	 */
	public void setDiagonal(double diag) {
		diagonal=diag;
	}
	
	/**
	 * Find corners.
	 *
	 * @author Alberto Borsetta
	 * @param image the image
	 * @param threshold the threshold
	 * @param density the density
	 */
	public void findCorners(BufferedImage image, int threshold, int density) {
		height = image.getHeight();
		width = image.getWidth();
		
		ExecutorService threadPool = Executors.newFixedThreadPool(4);
		HashMap<Corners, Future<FormPoint>> cornerDetectorThreads = new HashMap<Corners, Future<FormPoint>>();

		for (Corners position : Corners.values()) {
			Future<FormPoint> future = threadPool.submit(new CornerDetector(
					threshold, density, position, image));
			cornerDetectorThreads.put(position, future);
		}

		for (Corners position : Corners.values()) {
			try {
				FormPoint corner = cornerDetectorThreads.get(position).get();
				if (corner != null) {
					corners.put(position, corner);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		calculateDiagonal();
		calculateRotation();
		threadPool.shutdown();

	}
	
	/**
	 * Find points.
	 *
	 * @author Alberto Borsetta
	 * @param image the image
	 * @param threshold the threshold
	 * @param density the density
	 * @param size the size
	 */
	public void findPoints(BufferedImage image, int threshold, int density, int size) {
		height = image.getHeight();
		width = image.getWidth();
		
		ExecutorService threadPool = Executors.newFixedThreadPool(8);
		HashSet<Future<HashMap<String, FormField>>> fieldDetectorThreads = new HashSet<Future<HashMap<String, FormField>>>();
		
		HashMap<String, FormField> templateFields = template.getFields();
		ArrayList<String> fieldNames = new ArrayList<String>(templateFields.keySet());
		Collections.sort(fieldNames);

		for (String fieldName : fieldNames) {
			Future<HashMap<String, FormField>> future = threadPool.submit(new FieldDetector(
					threshold, density, size, this, templateFields.get(fieldName), image));
			fieldDetectorThreads.add(future);
		}
		
		for (Future<HashMap<String, FormField>> thread: fieldDetectorThreads) {
			try {
				HashMap<String, FormField> threadFields = thread.get();
				for (String fieldName: threadFields.keySet()) {
					FormField field = threadFields.get(fieldName);
					fields.put(fieldName, field);
					for (Entry<String, FormPoint> point: field.getPoints().entrySet()) {
						if (point.getValue() != null) {
							pointList.add(point.getValue());
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Calculate rotation.
	 * 
	 * @author Alberto Borsetta
	 */
	public void calculateRotation() {
		FormPoint topLeftPoint = corners.get(Corners.TOP_LEFT);
		FormPoint topRightPoint = corners.get(Corners.TOP_RIGHT);

		double dx = (double) (topRightPoint.getX() - topLeftPoint.getX());
		double dy = (double) (topLeftPoint.getY() - topRightPoint.getY());

		rotation = Math.atan(dy / dx);
	}

	/**
	 * Removes the point.
	 *
	 * @author Alberto Borsetta
	 * @param cursorPoint the cursor point
	 */
	public void removePoint(FormPoint cursorPoint) {
		if (!pointList.isEmpty()) {
			FormPoint nearestPoint = pointList.get(0);
			double firstDistance = cursorPoint.dist2(nearestPoint);
			for (FormPoint point : pointList) {
				double lastDistance = cursorPoint.dist2(point);
				if (lastDistance < firstDistance) {
					nearestPoint = point;
					firstDistance = lastDistance;
				}
			}
			pointList.remove(nearestPoint);

			for (Entry<String, FormField> field : fields.entrySet()) {
				FormField fieldValue = field.getValue();
				for (Entry<String, FormPoint> point : fieldValue.getPoints()
						.entrySet()) {
					if (nearestPoint.equals(point.getValue())) {
						fieldValue.getPoints().remove(point.getKey());
						return;
					}
				}
			}
		}

	}

	/**
	 * Adds the point.
	 *
	 * @author Alberto Borsetta
	 * @param cursorPoint the cursor point
	 */
	public void addPoint(FormPoint cursorPoint) {
		pointList.add(cursorPoint);
		FormPoint templateOrigin = template.getCorner(Corners.TOP_LEFT);
		double templateRotation = template.getRotation();
		double scale = Math.sqrt(diagonal / template.getDiagonal());
		ArrayList<FormPoint> templatePoints = template.getFieldPoints();
		FormPoint point = new FormPoint();
		if (!templatePoints.isEmpty()) {

			FormPoint nearestTemplatePoint = templatePoints.get(0);

			point = nearestTemplatePoint.clone();
			point.relativePositionTo(templateOrigin, templateRotation);
			point.scale(scale);
			point.originalPositionFrom(corners.get(Corners.TOP_LEFT), rotation);

			double firstDistance = cursorPoint.dist2(nearestTemplatePoint);
			for (FormPoint templatePoint : templatePoints) {
				point = templatePoint.clone();
				point.relativePositionTo(templateOrigin, templateRotation);
				point.scale(scale);
				point.originalPositionFrom(corners.get(Corners.TOP_LEFT),
						rotation);

				double lastDistance = cursorPoint.dist2(point);
				if (lastDistance < firstDistance) {
					nearestTemplatePoint = templatePoint;
					firstDistance = lastDistance;
				}
			}

			HashMap<String, FormField> templateFields = template.getFields();
			for (Entry<String, FormField> templateField : templateFields
					.entrySet()) {
				FormField fieldValue = templateField.getValue();
				for (Entry<String, FormPoint> templatePoint : fieldValue
						.getPoints().entrySet()) {
					if (nearestTemplatePoint.equals(templatePoint.getValue())) {
						FormField currentField = fields.get(templateField
								.getKey());
						currentField.setPoint(templatePoint.getKey(),
								cursorPoint);
						fields.put(templateField.getKey(), currentField);
						return;
					}
				}

			}

		}
	}

	/**
	 * Clear points.
	 * 
	 * @author Alberto Borsetta
	 */
	public void clearPoints() {
		pointList.clear();
		fields.clear();
	}

	/**
	 * Gets the point.
	 *
	 * @author Alberto Borsetta
	 * @param i the i
	 * @return the point
	 */
	public FormPoint getPoint(int i) {
		return pointList.get(i);
	}
	
	/**
	 * Gets the parent template.
	 *
	 * @author Alberto Borsetta
	 * @return the parent template
	 */
	public FormTemplate getParentTemplate() {
		return template;
	}
}
