package org.albertoborsetta.formscanner.commons;

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

import org.albertoborsetta.formscanner.commons.FormScannerConstants.Corners;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class FormTemplate {

//	private BufferedImage image;
	private String name;
	private HashMap<String, FormField> fields;
	private HashMap<Corners, FormPoint> corners;
	private ArrayList<FormPoint> pointList;
	private double rotation;
	private FormTemplate template;
	private int height;
	private int width;
	private double diagonal;

	public FormTemplate(String name) {
		this(name, null);
	}
	
	public FormTemplate(File file) throws ParserConfigurationException, SAXException, IOException {
		this(FilenameUtils.removeExtension(file.getName()), null);
		FormTemplateWrapper.presetFromTemplate(file, this);
	}
	
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

	public void calculateDiagonal() {
		diagonal = (corners.get(Corners.TOP_LEFT).dist2(
				corners.get(Corners.BOTTOM_RIGHT)) + corners.get(
				Corners.TOP_RIGHT).dist2(corners.get(Corners.BOTTOM_LEFT))) / 2;
	}

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

	public FormField getField(String name) {
		return fields.get(name);
	}

	public HashMap<String, FormField> getFields() {
		return fields;
	}

	public void setField(String name, FormField field) {
		fields.put(name, field);
		for (Entry<String, FormPoint> point : field.getPoints().entrySet()) {
			if (point.getValue() != null) {
				pointList.add(point.getValue());
			}
		}
	}

	public void setFields(HashMap<String, FormField> fields) {
		for (Entry<String, FormField> field : fields.entrySet()) {
			setField(field.getKey(), field.getValue());
		}
	}

	public void setCorners(HashMap<Corners, FormPoint> corners) {
		this.corners = corners;
		calculateRotation();
	}

	public void setCorner(Corners corner, FormPoint point) {
		corners.put(corner, point);
		calculateRotation();
		calculateDiagonal();
	}

	public HashMap<Corners, FormPoint> getCorners() {
		return corners;
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	public ArrayList<FormPoint> getFieldPoints() {
		return pointList;
	}

	public void removeFieldByName(String fieldName) {

		FormField field = fields.get(fieldName);
		for (Entry<String, FormPoint> point : field.getPoints().entrySet()) {
			pointList.remove(point.getValue());
		}

		fields.remove(fieldName);
	}

	public File saveToFile(String path) {
		File outputFile = null;
		
		try {
			outputFile = new File(path + "/template/" + name + ".xtmpl");
			Document xml = FormTemplateWrapper.getXml(this);
			outputFile = FormFileUtils.getInstance().saveTemplateAs(outputFile, xml);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		return outputFile;
	}

	public String toString() {
		return FormTemplateWrapper.getString(this);
	}

	public String getName() {
		return name;
	}

	public String[] getHeader() {
		String[] header = new String[fields.size() + 1];
		int i = 0;
		header[i++] = FormScannerTranslation
				.getTranslationFor(FormScannerTranslationKeys.FIRST_CSV_COLUMN);

		ArrayList<String> keys = new ArrayList<String>(fields.keySet());
		Collections.sort(keys);
		for (String key : keys) {
			header[i++] = key;
		}

		return header;
	}

	public FormPoint getCorner(Corners corner) {
		return corners.get(corner);
	}

	public double getDiagonal() {
		return diagonal;
	}
	
	public void setDiagonal(double diag) {
		diagonal=diag;
	}
	
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

	public void calculateRotation() {
		FormPoint topLeftPoint = corners.get(Corners.TOP_LEFT);
		FormPoint topRightPoint = corners.get(Corners.TOP_RIGHT);

		double dx = (double) (topRightPoint.getX() - topLeftPoint.getX());
		double dy = (double) (topLeftPoint.getY() - topRightPoint.getY());

		rotation = Math.atan(dy / dx);
	}

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

	public void clearPoints() {
		pointList.clear();
		fields.clear();
	}

	public FormPoint getPoint(int i) {
		return pointList.get(i);
	}
	
	public FormTemplate getParentTemplate() {
		return template;
	}
}
