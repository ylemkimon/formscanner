package org.albertoborsetta.formscanner.commons;

import ij.ImagePlus;
import ij.process.ImageProcessor;

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
import org.albertoborsetta.formscanner.imageparser.CornerDetector;
import org.apache.commons.io.FilenameUtils;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class FormTemplate {

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
	
	private int WHITE = 1;
	private int BLACK = 0;

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

		this.file = file;
		this.template = template;
		this.name = FilenameUtils.removeExtension(this.file.getName());

		corners = new HashMap<Corners, FormPoint>();
		rotation = 0;
		fields = new HashMap<String, FormField>();
		pointList = new ArrayList<FormPoint>();
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
		rotation = calculateRotation();
	}

	public void setCorner(Corners corner, FormPoint point) {
		corners.put(corner, point);
		rotation = calculateRotation();
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
		File outputFile = new File(path + "/template/" + name + ".xtmpl");

		try {

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

	public void presetFromTemplate() {
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
			e.printStackTrace();
		}
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder = builder.append("[[Rotation:").append(rotation).append("]")
				.append(" [Corners: ");

		// corner elements
		for (Entry<Corners, FormPoint> corner : corners.entrySet()) {
			Corners cornerPosition = corner.getKey();
			FormPoint cornerValue = corner.getValue();

			builder = builder.append(" [position:")
					.append(cornerPosition.name()).append(" x coord:")
					.append(cornerValue.getX()).append(" y coord:")
					.append(cornerValue.getY()).append("]");
		}

		builder = builder.append("]").append(" [Fields: ");

		// field elements
		for (Entry<String, FormField> field : fields.entrySet()) {
			FormField fieldValue = field.getValue();

			builder = builder.append(" [name:").append(fieldValue.getName())
					.append(" is multiple:").append(fieldValue.isMultiple())
					.append(" orientation:")
					.append(fieldValue.getType().name()).append(" values: ");

			// value elements
			for (Entry<String, FormPoint> point : fieldValue.getPoints()
					.entrySet()) {
				FormPoint pointValue = point.getValue();

				builder = builder.append(" [response:").append(point.getKey())
						.append(" x coord:").append(pointValue.getX())
						.append(" y coord:").append(pointValue.getY())
						.append("]");
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

	public void findCircleCorners() {
		int x;
		int y;
		int subImageWidth = width / 6;
		int subImageHeight = height / 8;
		int x1 = (width - (subImageWidth + 1));
		int y1 = (height - (subImageHeight + 1));
		int stato = 0;
		
		int WINDOW_SIZE = 5;
		int IThreshold = 127;
		int total = subImageWidth * subImageHeight;
		int[] rgbArray = new int[total];
		FormPoint[] points = new FormPoint[4];
		ArrayList<FormPoint> centralPoints = new ArrayList<FormPoint>(); 
		
		for (Corners position : Corners.values()) {
			x = 0;
			y = 0;

			switch (position) {
			case TOP_RIGHT:
				x = x1;
				break;
			case BOTTOM_LEFT:
				y = y1;
				break;
			case BOTTOM_RIGHT:
				x = x1;
				y = y1;
				break;
			default:
				break;
			}

			image.getRGB(x, y, subImageWidth, subImageHeight, rgbArray, 0, subImageWidth);
			
			for (int yi = 0; yi < subImageHeight; yi++) {				
				stato = 0;
				int pixel = WHITE;
				int old_pixel = pixel;
				
				int whites = WINDOW_SIZE;
				for (int xi = 0; xi < subImageWidth; xi++) {

					if (xi < WINDOW_SIZE) {
						if ((rgbArray[(yi*subImageWidth)+xi] & (0xFF)) < IThreshold) {
							whites--;
						}
					} else {
						if ((rgbArray[(yi*subImageWidth)+xi-WINDOW_SIZE] & (0xFF)) > IThreshold) {
							whites--;
						}
						if ((rgbArray[(yi*subImageWidth)+xi] & (0xFF)) > IThreshold) {
							whites++;
						}
					}
					
					if (whites > 2) {
						pixel = WHITE;
					} else {
						pixel = BLACK;
					}
					
					if (pixel != old_pixel) {
						stato++;
						old_pixel = pixel;
						switch (stato) {
						case 1:
							points[0] = new FormPoint(xi-2, yi);							
						case 3:
							points[1] = new FormPoint(xi-2, yi);
							break;
						case 2:
							points[2] = new FormPoint(xi-3, yi);
						case 4:
							points[3] = new FormPoint(xi-3, yi);
							break;
						default:
							break;
						}							
					}					
				}
				
				switch (stato) {
				case 2:
				case 4:
					int Xc1 = (int) ((points[0].getX() + points[3].getX()) / 2);
					int Xc2 = (int) ((points[1].getX() + points[2].getX()) / 2);
					int Xc = (Xc1 + Xc2)/2;
					int Yc = (int) points[1].getY();
					centralPoints.add(new FormPoint(Xc, Yc));
					break;
				case 0:
				case 1:
				case 3:
					break;
				default:
					break;
				}
			}
			
			int Xc = 0;
			int Yc = 0;
			for (FormPoint p: centralPoints) {
				Xc += p.getX();
				Yc += p.getY();
			}
			
			Xc = Xc / centralPoints.size();
			Yc = Yc / centralPoints.size();
			
			corners.put(position, new FormPoint((x + Xc), (y + Yc)));
		}
	}

	public void findCorners() {
		ImagePlus imagePlus = new ImagePlus();
		CornerDetector cornerDetector;
		ImageProcessor imageProcessor;
		FormPoint corner;

		int x;
		int y;
		int subImageWidth = width / 8;
		int subImageHeight = height / 8;
		int x1 = (width - (subImageWidth + 1));
		int y1 = (height - (subImageHeight + 1));

		for (Corners position : Corners.values()) {
			x = 0;
			y = 0;

			switch (position) {
			case TOP_RIGHT:
				x = x1;
				break;
			case BOTTOM_LEFT:
				y = y1;
				break;
			case BOTTOM_RIGHT:
				x = x1;
				y = y1;
				break;
			default:
				break;
			}

			BufferedImage cornerImage = image.getSubimage(x, y, subImageWidth,
					subImageHeight);
			imagePlus.setImage(cornerImage);
			imageProcessor = imagePlus.getProcessor();
			cornerDetector = new CornerDetector(imageProcessor, position);
			corner = cornerDetector.findCorners();
			corner.setLocation((x + corner.getX()), (y + corner.getY()));
			corners.put(position, corner);
		}
		rotation = calculateRotation();
	}

	public void findPoints() {
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

				double density = calcDensity(responsePoint);

				if (density > 0.6) {
					found = true;
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
		FormPoint point = responsePoint;
		FormPoint templateOrigin = template.getCorner(Corners.TOP_LEFT);
		double templateRotation = template.getRotation();

		point.relativePositionTo(templateOrigin, templateRotation);
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

	private double calcDensity(FormPoint responsePoint) {
		int IThreshold = 127;
		int offset = 0;
		int delta = 150;
		int width = 2 * delta;
		int height = 2 * delta;
		int total = width * height;
		int[] rgbArray = new int[total];
		int count = 0;

		int xCoord = (int) responsePoint.getX();
		int yCoord = (int) responsePoint.getY();

		image.getRGB(xCoord - delta, yCoord - delta, width, height, rgbArray,
				offset, width);
		for (int i = 0; i < total; i++) {
			if ((rgbArray[i] & (0xFF)) < IThreshold) {
				count++;
			}
		}
		return count / (double) total;
	}

	public double calculateRotation() {
		FormPoint topLeftPoint = corners.get(Corners.TOP_LEFT);
		FormPoint topRightPoint = corners.get(Corners.TOP_RIGHT);

		double dx = (double) (topRightPoint.getX() - topLeftPoint.getX());
		double dy = (double) (topLeftPoint.getY() - topRightPoint.getY());

		return Math.atan(dy / dx);
	}
}
