package org.albertoborsetta.formscanner.commons;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import org.albertoborsetta.formscanner.commons.FormScannerConstants.Corners;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.FieldType;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.imageparser.ScanImage;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class FormTemplate {
	
	private String name;
	private HashMap<String, FormField> fields;
	private HashMap<Corners, FormPoint> corners;
	private double rotation;
	private ArrayList<FormPoint> pointList;
	
	public FormTemplate(String name) {
		this.name = name;
		corners = new HashMap<Corners, FormPoint>();
		rotation = 0; 
		fields = new HashMap<String, FormField>();        
		pointList = new ArrayList<FormPoint>();
	}
	
	public void findCorners(File image) {
		ScanImage imageScan = new ScanImage(image);
        corners = imageScan.locateCorners();
        rotation = calculateRotation();
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
	
	private double calculateRotation() {
		FormPoint topLeftPoint = corners.get(Corners.TOP_LEFT);
		FormPoint topRightPoint = corners.get(Corners.TOP_RIGHT);
		
		double dx = (double) (topRightPoint.getX() - topLeftPoint.getX());
		double dy = (double) (topLeftPoint.getY() - topRightPoint.getY());
		
		return Math.atan(dy/dx);
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
				cornerElement.setAttribute("x", String.valueOf(cornerValue.getX()));
				cornerElement.setAttribute("y", String.valueOf(cornerValue.getY()));
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
					valueElement
							.setAttribute("x", String.valueOf(pointValue.getX()));
					valueElement
							.setAttribute("y", String.valueOf(pointValue.getY()));
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

	public void presetFromTemplate(File template) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(template);
			doc.getDocumentElement().normalize();
			 
			Element templateElement = (Element) doc.getDocumentElement(); 
			Element rotationElement = (Element) templateElement.getElementsByTagName("rotation").item(0);
			rotation = Double.parseDouble(rotationElement.getAttribute("angle"));
			
			Element cornersElement = (Element) templateElement.getElementsByTagName("corners").item(0);
			NodeList cornerList = cornersElement.getElementsByTagName("corner");
			for (int i=0; i<cornerList.getLength(); i++) {
				Element cornerElement = (Element) cornerList.item(i);
				String postion = cornerElement.getAttribute("position");
				String xCoord = cornerElement.getAttribute("x");
				String yCoord = cornerElement.getAttribute("y");
				
				FormPoint cornerPoint = new FormPoint(Double.parseDouble(xCoord), Double.parseDouble(yCoord));
				corners.put(Corners.valueOf(postion), cornerPoint);
			}
			
			Element fieldsElement = (Element) templateElement.getElementsByTagName("fields").item(0);
			NodeList fieldList = fieldsElement.getElementsByTagName("field");
			for (int i=0; i<fieldList.getLength(); i++) {
				Element fieldElement = (Element) fieldList.item(i);
				Element nameElement = (Element) fieldElement.getElementsByTagName("name").item(0);
				String fieldName = nameElement.getTextContent();
				FormField field = new FormField(fieldName);
				field.setMultiple(Boolean.parseBoolean(fieldElement.getAttribute("multiple")));
				field.setType(FieldType.valueOf(fieldElement.getAttribute("orientation")));
				
				Element valuesElement = (Element) fieldElement.getElementsByTagName("values").item(0);
				NodeList valueList = valuesElement.getElementsByTagName("value");
				for (int j=0; j<valueList.getLength(); j++) {
					Element valueElement = (Element) valueList.item(j);
					String xCoord = valueElement.getAttribute("x");
					String yCoord = valueElement.getAttribute("y");
					
					FormPoint point = new FormPoint(Double.parseDouble(xCoord), Double.parseDouble(yCoord));
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
		
		builder = builder.append("[[Rotation:")
			.append(rotation)
			.append("]")
			.append(" [Corners: ");
						 
			// corner elements
			for (Entry<Corners, FormPoint> corner : corners.entrySet()) {
				Corners cornerPosition = corner.getKey();
				FormPoint cornerValue = corner.getValue();
						
				builder = builder.append(" [position:")
					.append(cornerPosition.name())
					.append(" x coord:")
					.append(cornerValue.getX())
					.append(" y coord:")
					.append(cornerValue.getY())
					.append("]");
	        }
			
			builder = builder.append("]")
					.append(" [Fields: ");

			// field elements
			for (Entry<String, FormField> field : fields.entrySet()) {
				FormField fieldValue = field.getValue();
				
				builder = builder.append(" [name:")
						.append(fieldValue.getName())
						.append(" is multiple:")
						.append(fieldValue.isMultiple())
						.append(" orientation:")
						.append(fieldValue.getType().name())
						.append(" values: ");
				
				// value elements
				for (Entry<String, FormPoint> point : fieldValue.getPoints().entrySet()) {
					FormPoint pointValue = point.getValue();
					
					builder = builder.append(" [response:")
							.append(point.getKey())
							.append(" x coord:")
							.append(pointValue.getX())
							.append(" y coord:")
							.append(pointValue.getY())
							.append("]");
				}
				
				builder = builder.append("]");
	        }

			return builder.append("]").append("]").toString();
	}
	
	public void findPoints(File imageFile, FormTemplate formTemplate) {
		BufferedImage image;
		boolean found;
		
		try {		
			image = ImageIO.read(imageFile);
		} catch (IOException ex) {
			image = null;
		}
		
		HashMap<String, FormField> templateFields = formTemplate.getFields();
		ArrayList<String> fieldNames = new ArrayList<String>(templateFields.keySet()); 
		Collections.sort(fieldNames);
		
		for (String fieldName: fieldNames) { 
			FormField templateField = templateFields.get(fieldName);
			HashMap<String, FormPoint> templatePoints = templateField.getPoints();
			
			ArrayList<String> pointNames = new ArrayList<String>(templatePoints.keySet()); 
			Collections.sort(pointNames);
			found = false;
			
			for (String pointName: pointNames) {
				FormPoint responsePoint = templatePoints.get(pointName).clone();
				calcResponsePoint(formTemplate, responsePoint);
				
				double density = calcDensity(image, responsePoint);
				
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

	private void calcResponsePoint(FormTemplate formTemplate, FormPoint responsePoint) {
		FormPoint templateOrigin = formTemplate.getCorners().get(Corners.TOP_LEFT);
		double templateRotation = formTemplate.getRotation();
				
		responsePoint.relativePositionTo(templateOrigin, templateRotation);
		responsePoint.originalPositionFrom(corners.get(Corners.TOP_LEFT), rotation);
	}

	private FormField getField(FormField field, String fieldName) {
		FormField filledField = fields.get(fieldName);
		
		if (filledField == null) {
			 filledField = new FormField(fieldName);
			 filledField.setMultiple(field.isMultiple());
		}
		
		return filledField;		
	}

	private double calcDensity(BufferedImage image, FormPoint responsePoint) {
		int IThreshold = 127;
		int offset = 0;
		int delta = 5;
		int width = 2*delta;
		int height =2*delta;
		int total = width * height;
		int[] rgbArray = new int[total];
		int count = 0;
		
		int xCoord = (int) responsePoint.getX();
		int yCoord = (int) responsePoint.getY();

		image.getRGB(xCoord-delta, yCoord-delta, width, height, rgbArray, offset, width);
		for (int i=0; i<width*height; i++) {
			if ((rgbArray[i] & (0xFF)) < IThreshold) {
				count++;
			}
		}
		return count / (double) total;
	}

	public String getName() {
		return name;
	}

	public String[] getHeader() {
		String[] header = new String[fields.size() + 1];
		int i = 0;
		header[i++] = FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.FIRST_CSV_COLUMN);
		
		ArrayList<String> keys = new ArrayList<String>(fields.keySet());
		Collections.sort(keys);
		for (String key: keys) {
			header[i++] = key;
		}
		
		return header;
	}	
}
