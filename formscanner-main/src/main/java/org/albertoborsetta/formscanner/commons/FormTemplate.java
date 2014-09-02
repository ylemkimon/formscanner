package org.albertoborsetta.formscanner.commons;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import org.albertoborsetta.formscanner.commons.FormScannerConstants.Corners;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.FieldType;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class FormTemplate {

	private static final int WHITE = 1;
	private static final int BLACK = 0;
	private static final int HALF_WINDOW_SIZE = 5;
	private static final int WINDOW_SIZE = (HALF_WINDOW_SIZE * 2) + 1;

	private BufferedImage image;
	private String name;
	private HashMap<String, FormField> fields;
	private HashMap<Corners, FormPoint> corners;
	private ArrayList<FormPoint> pointList;
	private double rotation;
	private FormTemplate template;
	private File file;
	private int height;
	private int width;
	private int subImageWidth;
	private int subImageHeight;
	private double diagonal;

	public FormTemplate(File file) {
		this(file, null);
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

		subImageWidth = (int) (width / 2);
		subImageHeight = (int) (height / 2);

		this.file = file;
		this.template = template;
		this.name = FilenameUtils.removeExtension(this.file.getName());

		corners = calculateDefaultCornerPosition();
		diagonal = calculateDiagonal();
		new HashMap<Corners, FormPoint>();
		rotation = 0;
		fields = new HashMap<String, FormField>();
		pointList = new ArrayList<FormPoint>();
	}

	private double calculateDiagonal() {
		return (corners.get(Corners.TOP_LEFT).dist2(corners.get(Corners.BOTTOM_RIGHT)) +
				corners.get(Corners.TOP_RIGHT).dist2(corners.get(Corners.BOTTOM_LEFT))) / 2;
	}

	private HashMap<Corners, FormPoint> calculateDefaultCornerPosition() {
		int x;
		int y;
		HashMap<Corners, FormPoint> corners = new HashMap<Corners, FormPoint>();

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
		return corners;
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
		diagonal = calculateDiagonal();
		rotation = calculateRotation();
	}

	public void setCorner(Corners corner, FormPoint point) {
		corners.put(corner, point);
		rotation = calculateRotation();
		diagonal = calculateDiagonal();
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

			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root element
			Document doc = docBuilder.newDocument();
			Element templateElement = doc.createElement("template");

			// rotation element
			doc.appendChild(templateElement);
			Element rotationElement = doc.createElement("rotation");
			rotationElement.setAttribute("angle", String.valueOf(rotation));
			templateElement.appendChild(rotationElement);

			// corners element
			Element cornersElement = doc.createElement("corners");
			templateElement.appendChild(cornersElement);

			// corner elements
			for (Entry<Corners, FormPoint> corner : corners.entrySet()) {
				Element cornerElement = doc.createElement("corner");
				Corners cornerPosition = corner.getKey();
				FormPoint cornerValue = corner.getValue();
				cornerElement.setAttribute("position", cornerPosition.name());
				cornerElement.setAttribute("x",
						String.valueOf(cornerValue.getX()));
				cornerElement.setAttribute("y",
						String.valueOf(cornerValue.getY()));
				cornersElement.appendChild(cornerElement);
			}

			// fields element
			Element fieldsElement = doc.createElement("fields");
			templateElement.appendChild(fieldsElement);

			// field elements
			for (Entry<String, FormField> field : fields.entrySet()) {
				Element fieldElement = doc.createElement("field");
				FormField fieldValue = field.getValue();
				fieldElement.setAttribute("orientation", fieldValue.getType()
						.name());
				fieldElement.setAttribute("multiple",
						String.valueOf(fieldValue.isMultiple()));

				// name element
				Element fieldNameElement = doc.createElement("name");
				fieldNameElement.appendChild(doc.createTextNode(fieldValue
						.getName()));
				fieldElement.appendChild(fieldNameElement);

				// values element
				Element valuesElement = doc.createElement("values");

				// value elements
				for (Entry<String, FormPoint> point : fieldValue.getPoints()
						.entrySet()) {
					Element valueElement = doc.createElement("value");
					FormPoint pointValue = point.getValue();
					valueElement.setAttribute("x",
							String.valueOf(pointValue.getX()));
					valueElement.setAttribute("y",
							String.valueOf(pointValue.getY()));
					valueElement
							.appendChild(doc.createTextNode(point.getKey()));
					valuesElement.appendChild(valueElement);
				}

				fieldElement.appendChild(valuesElement);
				fieldsElement.appendChild(fieldElement);
			}

			FormFileUtils.getInstance().saveTemplateAs(outputFile, doc);

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		}
		return outputFile;
	}

	public boolean presetFromTemplate() {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();

			Element templateElement = (Element) doc.getDocumentElement();
			Element rotationElement = (Element) templateElement
					.getElementsByTagName("rotation").item(0);
			rotation = Double
					.parseDouble(rotationElement.getAttribute("angle"));

			Element cornersElement = (Element) templateElement
					.getElementsByTagName("corners").item(0);
			NodeList cornerList = cornersElement.getElementsByTagName("corner");
			for (int i = 0; i < cornerList.getLength(); i++) {
				Element cornerElement = (Element) cornerList.item(i);
				String postion = cornerElement.getAttribute("position");
				String xCoord = cornerElement.getAttribute("x");
				String yCoord = cornerElement.getAttribute("y");

				FormPoint cornerPoint = new FormPoint(
						Double.parseDouble(xCoord), Double.parseDouble(yCoord));
				corners.put(Corners.valueOf(postion), cornerPoint);
			}
			
			diagonal = calculateDiagonal();

			Element fieldsElement = (Element) templateElement
					.getElementsByTagName("fields").item(0);
			NodeList fieldList = fieldsElement.getElementsByTagName("field");
			for (int i = 0; i < fieldList.getLength(); i++) {
				Element fieldElement = (Element) fieldList.item(i);
				Element nameElement = (Element) fieldElement
						.getElementsByTagName("name").item(0);
				String fieldName = nameElement.getTextContent();
				FormField field = new FormField(fieldName);
				field.setMultiple(Boolean.parseBoolean(fieldElement
						.getAttribute("multiple")));
				field.setType(FieldType.valueOf(fieldElement
						.getAttribute("orientation")));

				Element valuesElement = (Element) fieldElement
						.getElementsByTagName("values").item(0);
				NodeList valueList = valuesElement
						.getElementsByTagName("value");
				for (int j = 0; j < valueList.getLength(); j++) {
					Element valueElement = (Element) valueList.item(j);
					String xCoord = valueElement.getAttribute("x");
					String yCoord = valueElement.getAttribute("y");

					FormPoint point = new FormPoint(Double.parseDouble(xCoord),
							Double.parseDouble(yCoord));
					field.setPoint(valueElement.getTextContent(), point);
					pointList.add(point);
				}
				fields.put(fieldName, field);
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder = builder.append("[\r\n[Rotation:").append(rotation)
				.append("]").append("\r\n[Corners: ");

		// corner elements
		for (Entry<Corners, FormPoint> corner : corners.entrySet()) {
			Corners cornerPosition = corner.getKey();
			FormPoint cornerValue = corner.getValue();

			builder = builder.append("\r\n[position:")
					.append(cornerPosition.name()).append(" x coord:")
					.append(cornerValue.getX()).append(" y coord:")
					.append(cornerValue.getY()).append("]");
		}

		builder = builder.append("]").append("\r\n[Fields: ");

		// field elements
		for (Entry<String, FormField> field : fields.entrySet()) {
			FormField fieldValue = field.getValue();

			builder = builder.append("\r\n[name:").append(fieldValue.getName())
					.append(" is multiple:").append(fieldValue.isMultiple())
					.append(" orientation:")
					.append(fieldValue.getType().name()).append(" values: ");

			// value elements
			for (Entry<String, FormPoint> point : fieldValue.getPoints()
					.entrySet()) {
				FormPoint pointValue = point.getValue();

				builder = builder.append("\r\n[response:")
						.append(point.getKey()).append(" x coord:")
						.append(pointValue.getX()).append(" y coord:")
						.append(pointValue.getY()).append("]");
			}

			builder = builder.append("]");
		}

		return builder.append("]").append("]").toString();
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

	public void findCorners(int threshold, int density) {

		for (Corners position : Corners.values()) {

			FormPoint corner = getCircleCenter(threshold, density, position);
			if (corner != null) {
				corners.put(position, corner);
			}
		}
		
		diagonal = calculateDiagonal();
		rotation = calculateRotation();
	}

	private FormPoint getCircleCenter(int threshold, int density, Corners position) {
		boolean found = false;
		boolean passed = false;
		double Xc = 0;
		double Yc = 0;
		int centralPoints = 0;
		int dx = 1;
		int dy = 1;
		int x = HALF_WINDOW_SIZE;
		int y = HALF_WINDOW_SIZE;
		int x1 = 0;
		int y1 = 0;
		int stato;
		int pixel;
		int old_pixel;
//		int whites;
//		int currentPixelIndex;
		int[] rgbArray = new int[subImageWidth * subImageHeight];
		FormPoint[] points = new FormPoint[4];
		
		switch (position) {
		case TOP_RIGHT:
			x = subImageWidth - (HALF_WINDOW_SIZE + 1);
			x1 = width - (subImageWidth + 1);
			dx = -1;
			break;
		case BOTTOM_LEFT:
			y = subImageHeight - (HALF_WINDOW_SIZE + 1);
			y1 = height - (subImageHeight + 1);
			dy = -1;
			break;
		case BOTTOM_RIGHT:
			x = subImageWidth - (HALF_WINDOW_SIZE + 1);
			y = subImageHeight - (HALF_WINDOW_SIZE + 1);
			x1 = width - (subImageWidth + 1);
			y1 = height - (subImageHeight + 1);
			dx = -1;
			dy = -1;
			break;
		default:
			break;
		}

		image.getRGB(x1, y1, subImageWidth, subImageHeight, rgbArray, 0,
				subImageWidth);

		for (int yi = y; (yi < (subImageHeight - HALF_WINDOW_SIZE)) && (yi >= HALF_WINDOW_SIZE); yi += dy) {
			stato = 0;
			pixel = WHITE;
			old_pixel = pixel;
			// whites = WINDOW_SIZE * WINDOW_SIZE;

			for (int xi = x; (xi < (subImageWidth - HALF_WINDOW_SIZE)) && (xi >= HALF_WINDOW_SIZE); xi += dx) {

//				currentPixelIndex = ((yi * subImageWidth) + xi);
//				if ((xi > WINDOW_SIZE) && (Math.abs(x - xi) > WINDOW_SIZE)) {
//					if ((rgbArray[currentPixelIndex - (dx * WINDOW_SIZE)] & (0xFF)) > threshold) {
//						whites--;
//					}
//					if ((rgbArray[currentPixelIndex] & (0xFF)) > threshold) {
//						whites++;
//					}
//				}

//				pixel = (whites > HALF_WINDOW_SIZE) ? WHITE : BLACK;
				pixel = isWhite(xi, yi, rgbArray, threshold, density);

				if (pixel != old_pixel) {
					stato++;
					old_pixel = pixel;
					switch (stato) {
					case 1:
//						points[0] = new FormPoint(x1 + xi - dx * HALF_WINDOW_SIZE, y1 + yi);
						points[0] = new FormPoint(x1 + xi, y1 + yi);
					case 3:
//						points[2] = new FormPoint(x1 + xi - dx * HALF_WINDOW_SIZE, y1 + yi);
						points[2] = new FormPoint(x1 + xi, y1 + yi);
						break;
					case 2:
//						points[1] = new FormPoint(x1 + xi - dx * (HALF_WINDOW_SIZE + 1), y1 + yi);
						points[1] = new FormPoint(x1 + xi, y1 + yi);
					case 4:
//						points[3] = new FormPoint(x1 + xi - dx * (HALF_WINDOW_SIZE + 1), y1 + yi);
						points[3] = new FormPoint(x1 + xi, y1 + yi);
						found = found || (stato==4);
						break;
					default:
						break;
					}
				}
				
				if ((found && (stato==4)) || (passed && (stato==2))) {
					break;
				}
			}

			switch (stato) {
			case 2:
				passed = passed || (found && (stato==2));
			case 4:
				double Xc1 = (points[0].getX() + points[3].getX()) / 2;
				double Xc2 = (points[1].getX() + points[2].getX()) / 2;
				centralPoints++;
				Xc += (Xc1 + Xc2) / 2;
				Yc += points[0].getY();
				break;
			case 0:
			case 1:
			case 3:
			default:
				break;
			}

			if (passed && found && (stato==0)) {
				break;
			}
		}

		if (centralPoints == 0) {
			return null;
		}
		Xc = Xc / centralPoints;
		Yc = Yc / centralPoints;
		
		FormPoint p = new FormPoint(Xc, Yc);
		return p;
	}

	private int isWhite(int xi, int yi, int[] rgbArray, int threshold, int density) {
		int blacks = 0;
		int total = WINDOW_SIZE * WINDOW_SIZE;
		for (int i=0; i<WINDOW_SIZE; i++) {
			for (int j=0; j<WINDOW_SIZE; j++) {
				int xji = xi - HALF_WINDOW_SIZE + j;
				int yji = yi - HALF_WINDOW_SIZE + i;
				int index = (yji * subImageWidth) + xji;
				if ((rgbArray[index] & (0xFF)) < threshold) {
					blacks++;
				}
			}
		}
		if ((blacks/ (double) total) >= (density / 100.0))
			return BLACK;
		return WHITE;
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
		double scale = Math.sqrt(diagonal/template.getDiagonal());

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

		image.getRGB(xCoord - halfSize, yCoord - halfSize, size, size, rgbArray, 0,
				size);
		for (int i = 0; i < total; i++) {
			if ((rgbArray[i] & (0xFF)) < threshold) {
				count++;
			}
		}
		return (count / (double) total) >= (density / 100.0);
	}

	public double calculateRotation() {
		FormPoint topLeftPoint = corners.get(Corners.TOP_LEFT);
		FormPoint topRightPoint = corners.get(Corners.TOP_RIGHT);

		double dx = (double) (topRightPoint.getX() - topLeftPoint.getX());
		double dy = (double) (topLeftPoint.getY() - topRightPoint.getY());

		return Math.atan(dy / dx);
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
		double scale = Math.sqrt(diagonal/template.getDiagonal());
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
				point.originalPositionFrom(corners.get(Corners.TOP_LEFT), rotation);
				
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
