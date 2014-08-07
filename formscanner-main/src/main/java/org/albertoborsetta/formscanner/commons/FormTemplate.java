package org.albertoborsetta.formscanner.commons;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

	private BufferedImage image;
	private String name;
	private HashMap<String, FormField> fields;
	private HashMap<Corners, FormPoint> corners;
	private ArrayList<FormPoint> pointList;
	private double rotation;
	private FormTemplate template;
	private int height;
	private int width;
	private double diagonal;

	public FormTemplate(File file) throws ParserConfigurationException, SAXException, IOException {
		this(file, null);
		FormTemplateWrapper.presetFromTemplate(file, this);
	}

	public FormTemplate(File file, FormTemplate template) {
		try {
			image = ImageIO.read(file);

			height = image.getHeight();
			width = image.getWidth();
		} catch (Exception e) {
			image = null;

			height = 0;
			width = 0;
		}

		this.template = template;
		this.name = FilenameUtils.removeExtension(file.getName());

		setDefaultCornerPosition();
		calculateDiagonal();
		corners = new HashMap<Corners, FormPoint>();
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
			pointList.add(point.getValue());
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
			FormFileUtils.getInstance().saveTemplateAs(outputFile, xml);
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

	public BufferedImage getImage() {
		return image;
	}

	public double getDiagonal() {
		return diagonal;
	}
	
	public void setDiagonal(double diag) {
		diagonal=diag;
	}

	public void findCorners(int threshold, int density) {
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
		threadPool.shutdown();

	}

	public void findPoints(int threshold, int density, int size) {
		boolean found;
		HashMap<String, FormField> templateFields = template.getFields();
		ArrayList<String> fieldNames = new ArrayList<String>(
				templateFields.keySet());
		Collections.sort(fieldNames);

		for (String fieldName : fieldNames) {
			FormField templateField = templateFields.get(fieldName);
			HashMap<String, FormPoint> templatePoints = templateField
					.getPoints();

			ArrayList<String> pointNames = new ArrayList<String>(
					templatePoints.keySet());
			Collections.sort(pointNames);
			found = false;

			for (String pointName : pointNames) {
				FormPoint responsePoint = calcResponsePoint(template,
						templatePoints.get(pointName));

				if (found = isFilled(responsePoint, threshold, density, size)) {
					FormField filledField = getField(templateField, fieldName);
					filledField.setPoint(pointName, responsePoint);
					fields.put(fieldName, filledField);
					pointList.add(responsePoint);
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
		}
	}

	private FormPoint calcResponsePoint(FormTemplate template,
			FormPoint responsePoint) {
		FormPoint point = responsePoint.clone();
		FormPoint templateOrigin = template.getCorner(Corners.TOP_LEFT);
		double templateRotation = template.getRotation();
		double scale = Math.sqrt(diagonal / template.getDiagonal());

		point.relativePositionTo(templateOrigin, templateRotation);
		point.scale(scale);
		point.originalPositionFrom(corners.get(Corners.TOP_LEFT), rotation);
		return point;
	}

	private FormField getField(FormField field, String fieldName) {
		FormField filledField = fields.get(fieldName);

		if (filledField == null) {
			filledField = new FormField(fieldName);
			filledField.setMultiple(field.isMultiple());
		}

		return filledField;
	}

	private boolean isFilled(FormPoint responsePoint, int threshold,
			int density, int size) {
		int total = size * size;
		int halfSize = (int) size / 2;
		int[] rgbArray = new int[total];
		int count = 0;

		int xCoord = (int) responsePoint.getX();
		int yCoord = (int) responsePoint.getY();

		image.getRGB(xCoord - halfSize, yCoord - halfSize, size, size,
				rgbArray, 0, size);
		for (int i = 0; i < total; i++) {
			if ((rgbArray[i] & (0xFF)) < threshold) {
				count++;
			}
		}
		return (count / (double) total) >= (density / 100.0);
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
}
