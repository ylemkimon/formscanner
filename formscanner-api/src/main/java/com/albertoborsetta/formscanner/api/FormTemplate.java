package com.albertoborsetta.formscanner.api;

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

import org.apache.commons.io.FilenameUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.albertoborsetta.formscanner.api.FormField;
import com.albertoborsetta.formscanner.api.FormPoint;
import com.albertoborsetta.formscanner.api.commons.Constants.Corners;
import com.albertoborsetta.formscanner.api.commons.Constants.FieldType;

/**
 * The <code>FormTemplate</code> class represents the scanned form.<p>
 * A field object has this attributes:
 * <ul>
 * <li>A name
 * <li>A rotation
 * <li>A height
 * <li>A width
 * <li>A diagonal
 * <li>The corners
 * <li>A set of fields
 * <li>Can have a parent template
 * </ul>
 * <p>
 * How to use:
 * <pre>
 * {@code
 * // Create a template with the points of correct responses from the xml file
 * File template = new File("C:\templates\temlpate.xml");
 * formTemplate = new FormTemplate(template);>
 * 
 * // Analyze image for search correct answers using the formTemplate
 * BufferedImage image = ImageIO.read(imageFile);
 * filledForm = new FormTemplate(imageFile.getName(), formTemplate);
 * filledForm.findCorners(image, threshold, density);
 * filledForm.findPoints(image, threshold, density,
 * 	shapeSize);
 * }
 * </pre>
 * Example of an XML representation for a template:
 * <pre>
 * {@code
 * <?xml version="1.0" encoding="UTF-8" standalone="no"?>
 * <template>
 * 	<rotation angle="0.0"/>
 * 	<corners>
 * 		<corner position="BOTTOM_RIGHT" x="2324.627551020408" y="3353.5"/>
 * 		<corner position="TOP_RIGHT" x="2324.627551020408" y="153.5"/>
 * 		<corner position="TOP_LEFT" x="153.7844387755102" y="153.5"/>
 * 		<corner position="BOTTOM_LEFT" x="153.7844387755102" y="3353.5"/>
 * 	</corners>
 * 	<fields>
 * 		<field multiple="false" orientation="QUESTIONS_BY_ROWS">
 * 			<name>Question 01</name>
 * 			<values>
 * 				<value x="840.0" y="1287.0">Response 02</value>
 * 				<value x="715.0" y="1287.0">Response 01</value>
 * 				<value x="1091.0" y="1287.0">Response 04</value>
 * 				<value x="965.0" y="1287.0">Response 03</value>
 * 			</values>
 * 		</field>
 * 		<field multiple="false" orientation="QUESTIONS_BY_ROWS">
 * 			<name>Question 05</name>
 * 			<values>
 * 				<value x="840.0" y="1620.0">Response 02</value>
 * 				<value x="715.0" y="1620.0">Response 01</value>
 * 				<value x="1091.0" y="1620.0">Response 04</value>
 * 				<value x="965.0" y="1620.0">Response 03</value>
 * 			</values>
 * 		</field>
 * 	</fields>
 * </template>
 * }
 * </pre>
 * 
 * @author Alberto Borsetta
 * @version 0.9-SNAPSHOT
 * @see FormPoint
 * @see FormField
 * @see FieldType
 * @see Corners
 */
public class FormTemplate {

	private String name;
	private HashMap<String, FormField> fields;
	private HashMap<Corners, FormPoint> corners;
	private ArrayList<FormPoint> pointList;
	private double rotation;
	private FormTemplate template;
	private int height;
	private int width;
	private double diagonal;
	
	private static class FormTemplateWrapper {

		public static Document getXml(FormTemplate template)
				throws ParserConfigurationException {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root element
			Document doc = docBuilder.newDocument();
			Element templateElement = doc.createElement("template");

			// rotation element
			doc.appendChild(templateElement);
			Element rotationElement = doc.createElement("rotation");
			rotationElement.setAttribute("angle",
					String.valueOf(template.getRotation()));
			templateElement.appendChild(rotationElement);

			// corners element
			Element cornersElement = doc.createElement("corners");
			templateElement.appendChild(cornersElement);

			// corner elements
			for (Entry<Corners, FormPoint> corner : template.getCorners()
					.entrySet()) {
				Element cornerElement = doc.createElement("corner");
				cornerElement.setAttribute("position", corner.getKey().getName());
				cornerElement.appendChild(corner.getValue().getXml(doc));
				cornersElement.appendChild(cornerElement);
			}

			// fields element
			Element fieldsElement = doc.createElement("fields");
			templateElement.appendChild(fieldsElement);

			// field elements
			for (Entry<String, FormField> field : template.getFields().entrySet()) {
				fieldsElement.appendChild(field.getValue().getXml(doc));
			}
			return doc;
		}

		public static void presetFromTemplate(File file, FormTemplate template)
				throws ParserConfigurationException, SAXException, IOException {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();

			Element templateElement = (Element) doc.getDocumentElement();
			Element rotationElement = (Element) templateElement
					.getElementsByTagName("rotation").item(0);
			template.setRotation(Double.parseDouble(rotationElement
					.getAttribute("angle")));

			Element cornersElement = (Element) templateElement
					.getElementsByTagName("corners").item(0);
			NodeList cornerList = cornersElement.getElementsByTagName("corner");
			for (int i = 0; i < cornerList.getLength(); i++) {
				Element cornerElement = (Element) cornerList.item(i);
				String postion = cornerElement.getAttribute("position");
				cornerElement.getElementsByTagName("point");
				Element pointElement = (Element) cornerElement
						.getElementsByTagName("point").item(0);
				String xCoord = pointElement.getAttribute("x");
				String yCoord = pointElement.getAttribute("y");

				FormPoint corner = new FormPoint(Double.parseDouble(xCoord),
						Double.parseDouble(yCoord));
				template.setCorner(Corners.valueOf(postion), corner);
			}

			template.calculateDiagonal();

			Element fieldsElement = (Element) templateElement.getElementsByTagName(
					"fields").item(0);
			NodeList fieldList = fieldsElement.getElementsByTagName("field");
			for (int i = 0; i < fieldList.getLength(); i++) {
				Element fieldElement = (Element) fieldList.item(i);
				String fieldName = fieldElement.getAttribute("question");

				FormField field = new FormField(fieldName);
				field.setMultiple(Boolean.parseBoolean(fieldElement
						.getAttribute("multiple")));
				field.setRejectMultiple(Boolean.parseBoolean(fieldElement
						.getAttribute("rejectMultiple")));
				field.setType(FieldType.valueOf(fieldElement
						.getAttribute("orientation")));

				Element valuesElement = (Element) fieldElement
						.getElementsByTagName("values").item(0);
				NodeList valueList = valuesElement.getElementsByTagName("value");
				for (int j = 0; j < valueList.getLength(); j++) {
					Element valueElement = (Element) valueList.item(j);
					Element pointElement = (Element) valueElement.getElementsByTagName("point").item(0);
					String xCoord = pointElement.getAttribute("x");
					String yCoord = pointElement.getAttribute("y");

					FormPoint point = new FormPoint(Double.parseDouble(xCoord),
							Double.parseDouble(yCoord));
					field.setPoint(valueElement.getAttribute("response"), point);
				}
				template.setField(fieldName, field);
			}
		}

		public static String toString(FormTemplate template) {
			try {
				return getXml(template).toString();
			} catch (ParserConfigurationException e) {
				e.printStackTrace();
				return null;
			}
		}
	}
	
	/**
	 * Instantiates a new empty FormTemplate object.
	 *
	 * @author Alberto Borsetta
	 * @param name the name of the FormTemplate object
	 */
	public FormTemplate(String name) {
		this(name, null);
	}
	
	/**
	 * Instantiates a new form template from an xml representation.
	 *
	 * @author Alberto Borsetta
	 * @param file the file with the xml representation of the FormTemplate
	 * @throws ParserConfigurationException Signals that a parser configuration exception has occurred.
	 * @throws SAXException Signals that a SAX exception has occurred.
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public FormTemplate(File file) throws ParserConfigurationException, SAXException, IOException {
		this(FilenameUtils.removeExtension(file.getName()), null);
		FormTemplateWrapper.presetFromTemplate(file, this);
	}
	
	/**
	 * Instantiates a new FormTemplate object with the given name and parent template.
	 *
	 * @author Alberto Borsetta
	 * @param name the name of the FormTemplate object
	 * @param template the parent template
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
	 * Calculates the diagonal of the FormTemplate.
	 * 
	 * @author Alberto Borsetta
	 */
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

	/**
	 * Returns the field identified by the given name.
	 *
	 * @author Alberto Borsetta
	 * @param name the name of the field
	 * @return the FormField object
	 * @see FormField
	 */
	public FormField getField(String name) {
		return fields.get(name);
	}

	/**
	 * Returns all the fields in the FormTemplate object.
	 *
	 * @author Alberto Borsetta
	 * @return the fields of the FormTemplate object
	 * @see FormField
	 */
	public HashMap<String, FormField> getFields() {
		return fields;
	}

	/**
	 * Sets the field with the given name.
	 *
	 * @author Alberto Borsetta
	 * @param name the name of the field
	 * @param field the field to set
	 * @see FormField
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
	 * Sets all the fields in the FormTemplate object. 
	 *
	 * @author Alberto Borsetta
	 * @param fields the fields to set
	 */
	public void setFields(HashMap<String, FormField> fields) {
		for (Entry<String, FormField> field : fields.entrySet()) {
			setField(field.getKey(), field.getValue());
		}
	}

	/**
	 * Sets the corners of the FormTemplate object.
	 *
	 * @author Alberto Borsetta
	 * @param corners the corners to set
	 * @see Corners
	 * @see FormPoint
	 */
	public void setCorners(HashMap<Corners, FormPoint> corners) {
		this.corners = corners;
		calculateRotation();
	}

	/**
	 * Sets the given corner into the FormTemplate object.
	 *
	 * @author Alberto Borsetta
	 * @param corner the corner to set
	 * @param point the point of the corner
	 * @see Corners
	 * @see FormPoint
	 */
	public void setCorner(Corners corner, FormPoint point) {
		corners.put(corner, point);
		calculateRotation();
		calculateDiagonal();
	}

	/**
	 * Returns the corners of the FormTemplate object.
	 *
	 * @author Alberto Borsetta
	 * @return the corners of the FormTemplate object
	 * @see Corners
	 * @see FormPoint
	 */
	public HashMap<Corners, FormPoint> getCorners() {
		return corners;
	}

	/**
	 * Returns the rotation of the FormTemplate object.
	 *
	 * @author Alberto Borsetta
	 * @return the rotation of the FormTemplate object
	 */
	public double getRotation() {
		return rotation;
	}

	/**
	 * Sets the rotation of the FormTemplate object.
	 *
	 * @author Alberto Borsetta
	 * @param rotation the new rotation of the FormTemplate object
	 */
	public void setRotation(double rotation) {
		this.rotation = rotation;
	}

	/**
	 * Returns the points for the fields of the FormTemplate object.
	 *
	 * @author Alberto Borsetta
	 * @return the points for the fields of the FormTemplate object
	 */
	public ArrayList<FormPoint> getFieldPoints() {
		return pointList;
	}

	/**
	 * Removes the field identified by the given name.
	 *
	 * @author Alberto Borsetta
	 * @param fieldName the name of the field to remove
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
		return FormTemplateWrapper.toString(this);
	}
	
	/**
	 * Returns the xml representation of the FormTemplate object.
	 *
	 * @author Alberto Borsetta
	 * @return the xml representation of the FormTemplate object
	 * @throws ParserConfigurationException the parser configuration exception
	 */
	public Document getXml() throws ParserConfigurationException {
		return FormTemplateWrapper.getXml(this);
	}

	/**
	 * Returns the name of the FormTemplate object.
	 *
	 * @author Alberto Borsetta
	 * @return the name of the FormTemplate object
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the point of the given corner of the FormTemplate object.
	 *
	 * @author Alberto Borsetta
	 * @param corner the corner 
	 * @return the point of the corner
	 */
	public FormPoint getCorner(Corners corner) {
		return corners.get(corner);
	}

	/**
	 * Returns the diagonal of the FormTemplate object.
	 *
	 * @author Alberto Borsetta
	 * @return the diagonal of the FormTemplate object.
	 */
	public double getDiagonal() {
		return diagonal;
	}
	
	/**
	 * Sets the diagonal to the FormTemplate object.
	 *
	 * @author Alberto Borsetta
	 * @param diag the new diagonal
	 */
	public void setDiagonal(double diag) {
		diagonal=diag;
	}
	
	/**
	 * Find the corners of the FormTemplate object.
	 * <ul>
	 * <li>threshold: is the value of the RGB components beyond which the pixels are considered "blacks", 
	 * for example, if Threshold = 127 the software considers as blacks all pixels whose value is less than 127, 
	 * if the pixel value is greater than or equal to 127 then it is considered as white.
	 * <li>density: is the amount (in percentage) of black pixels (determined as described above) 
	 * that must be present in the bubble in order to identify the selected responses, 
	 * for example, if Density = 50 (%) is sufficient that at least half of the pixels in the area of the bubble 
	 * are blacks to select the response.
	 * </ul>
	 *
	 * @author Alberto Borsetta
	 * @param image the image on which to find the corners
	 * @param threshold the value of threshold parameter
	 * @param density the value of density parameter
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
	 * Find the filled points of the FormTemplate object.
	 * <ul>
	 * <li>threshold: is the value of the RGB components beyond which the pixels are considered "blacks", 
	 * for example, if Threshold = 127 the software considers as blacks all pixels whose value is less than 127, 
	 * if the pixel value is greater than or equal to 127 then it is considered as white.
	 * <li>density: is the amount (in percentage) of black pixels (determined as described above) 
	 * that must be present in the bubble in order to identify the selected responses, 
	 * for example, if Density = 50 (%) is sufficient that at least half of the pixels in the area of the bubble 
	 * are blacks to select the response.
	 * </ul>Find points.
	 *
	 * @author Alberto Borsetta
	 * @param image the image on which to find the corners
	 * * @param threshold the value of threshold parameter
	 * @param density the value of density parameter
	 * @param size the size of the area of a single point
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

	private void calculateRotation() {
		FormPoint topLeftPoint = corners.get(Corners.TOP_LEFT);
		FormPoint topRightPoint = corners.get(Corners.TOP_RIGHT);

		double dx = (double) (topRightPoint.getX() - topLeftPoint.getX());
		double dy = (double) (topLeftPoint.getY() - topRightPoint.getY());

		rotation = Math.atan(dy / dx);
	}

	/**
	 * Removes the nearest point to the given one from the FromTemplate object.
	 *
	 * @author Alberto Borsetta
	 * @param cursorPoint the point to remove
	 * @see FormPoint
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
	 * Adds the given point to the FormTemplateobject.
	 *
	 * @author Alberto Borsetta
	 * @param cursorPoint the point to add
	 * @see FormPoint
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
			point.rotoTranslate(templateOrigin, templateRotation, true);
			point.scale(scale);
			point.rotoTranslate(corners.get(Corners.TOP_LEFT), rotation, false);

			double firstDistance = cursorPoint.dist2(nearestTemplatePoint);
			for (FormPoint templatePoint : templatePoints) {
				point = templatePoint.clone();
				point.rotoTranslate(templateOrigin, templateRotation, true);
				point.scale(scale);
				point.rotoTranslate(corners.get(Corners.TOP_LEFT), rotation, false);

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
	 * Clear all the points form the FormTemplate object.
	 * 
	 * @author Alberto Borsetta
	 */
	public void clearPoints() {
		pointList.clear();
		fields.clear();
	}

	/**
	 * Returns the point at the given index from the FormTemplate object.
	 *
	 * @author Alberto Borsetta
	 * @param i the index
	 * @return the point at the given index
	 */
	public FormPoint getPoint(int i) {
		return pointList.get(i);
	}
	
	/**
	 * Returns the parent template.
	 *
	 * @author Alberto Borsetta
	 * @return the parent template
	 */
	public FormTemplate getParentTemplate() {
		return template;
	}
}
