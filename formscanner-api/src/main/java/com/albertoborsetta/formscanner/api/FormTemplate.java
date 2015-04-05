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

import com.albertoborsetta.formscanner.api.FormQuestion;
import com.albertoborsetta.formscanner.api.FormPoint;
import com.albertoborsetta.formscanner.api.commons.Constants;
import com.albertoborsetta.formscanner.api.commons.Constants.Corners;
import com.albertoborsetta.formscanner.api.commons.Constants.FieldType;

/**
 * The <code>FormTemplate</code> class represents the scanned form.
 * <p>
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
 * 
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
 * 
 * Example of an XML representation for a template:
 * 
 * <pre>
 * {@code
 * <?xml version="1.0" encoding="UTF-8" standalone="no"?>
 * <template version="1.0">
 * 	<rotation angle="0.0"/>
 * 	<corners>
 * 		<corner position="TOP_RIGHT">
 * 			<point x="2324.627551020408" y="153.5"/>
 * 		</corner>
 * 		<corner position="TOP_LEFT">
 * 			<point x="153.7844387755102" y="153.5"/>
 * 		</corner>
 * 		<corner position="BOTTOM_RIGHT">
 * 			<point x="2324.627551020408" y="3353.5"/>
 * 		</corner>
 * 		<corner position="BOTTOM_LEFT">
 * 			<point x="153.7844387755102" y="3353.5"/>
 * 		</corner>
 * 	</corners>
 * 	<fields>
 * 		<question multiple="false" type="QUESTIONS_BY_ROWS" question="Question 01">
 * 			<values>
 * 				<value response="Response 01">
 * 					<point x="800.0" y="1300.0" />
 * 				</value>
 * 				<value response="Response 02">
 * 					<point x="925.0" y="1300.0" />
 * 				</value>
 * 				<value response="Response 03">
 * 					<point x="1050.0" y="1300.0" />
 * 				</value>
 * 				<value response="Response 04">
 * 					<point x="1175.0" y="1300.0" />
 * 				</value>
 * 			</values>
 * 		</question>
 * 		<question multiple="false" type="QUESTIONS_BY_COLS" question="Question 02">
 * 			<values>
 * 				<value response="Response 01">
 * 					<point x="800.0" y="1400.0" />
 * 				</value>
 * 				<value response="Response 02">
 * 					<point x="800.0" y="1525.0" />
 * 				</value>
 * 				<value response="Response 03">
 * 					<point x="800.0" y="1650.0" />
 * 				</value>
 * 				<value response="Response 04">
 * 					<point x="800.0" y="1725.0" />
 * 				</value>
 * 			</values>
 * 		</question>
 * 		<question multiple="false" type="RESPONSES_BY_GRID" question="Question 03">
 * 			<values>
 * 				<value response="Response A">
 * 					<point x="800.0" y="1850.0" />
 * 				</value>
 * 				<value response="Response B">
 * 					<point x="925.0" y="1850.0" />
 * 				</value>
 * 				<value response="Response C">
 * 					<point x="1050.0" y="1850.0" />
 * 				</value>
 * 				<value response="Response D">
 * 					<point x="800.0" y="1975.0" />
 * 				</value>
 * 				<value response="Response E">
 * 					<point x="925.0" y="1975.0" />
 * 				</value>
 * 				<value response="Response F">
 * 					<point x="1050.0" y="1975.0" />
 * 				</value>
 * 			</values>
 * 		</question>
 * 		<area type="BARCODE" name="Barcode">
 * 			<corners>
 * 				<corner position="TOP_LEFT">
 * 					<point x="800.0" y="2100.0" />
 * 				</corner>
 * 				<corner position="TOP_RIGHT">
 * 					<point x="1200.0" y="2100.0" />
 * 				</corner>
 * 				<corner position="BOTTOM_LEFT">
 * 					<point x="800.0" y="2300.0" />
 * 				</corner>
 * 				<corner position="BOTTOM_RIGHT">
 * 					<point x="1200.0" y="2300.0" />
 * 				</corner>
 * 			</corners>
 * 		</area>
 * 	</fields>
 * </template>
 * }
 * </pre>
 * 
 * @author Alberto Borsetta
 * @version 0.11-SNAPSHOT
 * @see FormPoint
 * @see FormQuestion
 * @see FieldType
 * @see Corners
 */
public class FormTemplate {

	private String name;
	private HashMap<String, FormQuestion> fields;
	private HashMap<String, FormArea> areas;
	private HashMap<Corners, FormPoint> corners;
	private ArrayList<FormPoint> pointList;
	private double rotation;
	private FormTemplate template;
	private int height;
	private int width;
	private double diagonal;
	private ArrayList<FormArea> areaList;
	private String version;

	private static class FormTemplateWrapper {

		public static Document getXml(FormTemplate template)
				throws ParserConfigurationException {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root element
			Document doc = docBuilder.newDocument();
			Element templateElement = doc.createElement("template");
			templateElement.setAttribute("version",
					Constants.CURRENT_TEMPLATE_VERSION);

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
				cornerElement.setAttribute("position", corner.getKey()
						.getName());
				cornerElement.appendChild(corner.getValue().getXml(doc));
				cornersElement.appendChild(cornerElement);
			}

			// fields element
			Element fieldsElement = doc.createElement("fields");
			templateElement.appendChild(fieldsElement);

			// question elements
			for (Entry<String, FormQuestion> field : template.getFields()
					.entrySet()) {
				fieldsElement.appendChild(field.getValue().getXml(doc));
			}

			// area elements
			for (Entry<String, FormArea> area : template.getAreas().entrySet()) {
				fieldsElement.appendChild(area.getValue().getXml(doc));
			}

			return doc;
		}

		public static void presetFromTemplate(File file, FormTemplate template)
				throws ParserConfigurationException, SAXException, IOException {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();

			Element templateElement = (Element) doc.getDocumentElement();
			template.setVersion(templateElement.getAttribute("version"));
			Element rotationElement = (Element) templateElement
					.getElementsByTagName("rotation").item(0);
			template.setRotation(Double.parseDouble(rotationElement
					.getAttribute("angle")));

			Element cornersElement = (Element) templateElement
					.getElementsByTagName("corners").item(0);
			addCorners(template, cornersElement);

			template.calculateDiagonal();

			Element fieldsElement = (Element) templateElement
					.getElementsByTagName("fields").item(0);

			addQuestions(template, fieldsElement);
			addAreas(template, fieldsElement);
		}

		private static void addAreas(FormTemplate template, Element element) {
			NodeList fieldList = element.getElementsByTagName("area");
			for (int i = 0; i < fieldList.getLength(); i++) {
				Element fieldElement = (Element) fieldList.item(i);
				String fieldName = fieldElement.getAttribute("name");

				FormArea field = getArea(fieldElement, fieldName);
				template.setArea(fieldName, field);
			}

		}

		private static FormArea getArea(Element element, String name) {
			FormArea area = new FormArea(name);

			area.setType(FieldType.valueOf(element.getAttribute("type")));

			Element cornersElement = (Element) element.getElementsByTagName(
					"corners").item(0);
			NodeList cornerList = cornersElement.getElementsByTagName("corner");
			for (int i = 0; i < cornerList.getLength(); i++) {
				Element cornerElement = (Element) cornerList.item(i);
				String position = cornerElement.getAttribute("position");
				cornerElement.getElementsByTagName("point");
				FormPoint point = getPoint(cornerElement);
				area.setCorner(Corners.valueOf(position), point);
			}
			return area;
		}

		private static void addCorners(FormTemplate template, Element element) {
			NodeList cornerList = element.getElementsByTagName("corner");
			for (int i = 0; i < cornerList.getLength(); i++) {
				Element cornerElement = (Element) cornerList.item(i);
				String position = cornerElement.getAttribute("position");
				cornerElement.getElementsByTagName("point");
				FormPoint corner = getPoint(cornerElement);
				template.setCorner(Corners.valueOf(position), corner);
			}
		}

		private static FormPoint getPoint(Element element) {
			Element pointElement = (Element) element.getElementsByTagName(
					"point").item(0);
			String xCoord = pointElement.getAttribute("x");
			String yCoord = pointElement.getAttribute("y");

			FormPoint point = new FormPoint(Double.parseDouble(xCoord),
					Double.parseDouble(yCoord));
			return point;
		}

		private static void addQuestions(FormTemplate template, Element element) {
			NodeList fieldList = element.getElementsByTagName("question");
			for (int i = 0; i < fieldList.getLength(); i++) {
				Element fieldElement = (Element) fieldList.item(i);
				String fieldName = fieldElement.getAttribute("question");

				FormQuestion field = getQuestion(fieldElement, fieldName);
				template.setField(fieldName, field);
			}

			// Tag "field" deprecated. To be removed
			fieldList = element.getElementsByTagName("field");
			for (int i = 0; i < fieldList.getLength(); i++) {
				Element fieldElement = (Element) fieldList.item(i);
				String fieldName = fieldElement.getAttribute("question");

				FormQuestion field = getQuestion(fieldElement, fieldName);
				template.setField(fieldName, field);
			}
		}

		private static FormQuestion getQuestion(Element element, String name) {
			FormQuestion field = new FormQuestion(name);
			field.setMultiple(Boolean.parseBoolean(element
					.getAttribute("multiple")));
			field.setRejectMultiple(Boolean.parseBoolean(element
					.getAttribute("rejectMultiple")));

			// Attribute "orientation" deprecated. To be removed
			field.setType(FieldType.valueOf((element
					.getAttribute("orientation").isEmpty()) ? element
					.getAttribute("type") : element.getAttribute("orientation")));

			Element valuesElement = (Element) element.getElementsByTagName(
					"values").item(0);
			NodeList valueList = valuesElement.getElementsByTagName("value");
			for (int j = 0; j < valueList.getLength(); j++) {
				Element valueElement = (Element) valueList.item(j);
				FormPoint point = getPoint(valueElement);
				field.setPoint(valueElement.getAttribute("response"), point);
			}
			return field;
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
	 * @param name
	 *            the name of the FormTemplate object
	 */
	public FormTemplate(String name) {
		this(name, null);
	}

	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Instantiates a new form template from an xml representation.
	 *
	 * @author Alberto Borsetta
	 * @param file
	 *            the file with the xml representation of the FormTemplate
	 * @throws ParserConfigurationException
	 *             Signals that a parser configuration exception has occurred.
	 * @throws SAXException
	 *             Signals that a SAX exception has occurred.
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public FormTemplate(File file) throws ParserConfigurationException,
			SAXException, IOException {
		this(FilenameUtils.removeExtension(file.getName()), null);
		FormTemplateWrapper.presetFromTemplate(file, this);
	}

	/**
	 * Instantiates a new FormTemplate object with the given name and parent
	 * template.
	 *
	 * @author Alberto Borsetta
	 * @param name
	 *            the name of the FormTemplate object
	 * @param template
	 *            the parent template
	 */
	public FormTemplate(String name, FormTemplate template) {
		this.template = template;
		this.name = name;

		corners = new HashMap<Corners, FormPoint>();
		setDefaultCornerPosition();
		calculateDiagonal();
		rotation = 0;
		fields = new HashMap<String, FormQuestion>();
		pointList = new ArrayList<FormPoint>();
		areas = new HashMap<String, FormArea>();
		areaList = new ArrayList<FormArea>();
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
	 * @param name
	 *            the name of the field
	 * @return the FormField object
	 * @see FormQuestion
	 */
	public FormQuestion getField(String name) {
		return fields.get(name);
	}

	/**
	 * Returns all the fields in the FormTemplate object.
	 *
	 * @author Alberto Borsetta
	 * @return the fields of the FormTemplate object
	 * @see FormQuestion
	 */
	public HashMap<String, FormQuestion> getFields() {
		return fields;
	}

	/**
	 * Sets the field with the given name.
	 *
	 * @author Alberto Borsetta
	 * @param name
	 *            the name of the field
	 * @param field
	 *            the field to set
	 * @see FormQuestion
	 */
	public void setField(String name, FormQuestion field) {
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
	 * @param fields
	 *            the fields to set
	 */
	public void setFields(HashMap<String, FormQuestion> fields) {
		for (Entry<String, FormQuestion> field : fields.entrySet()) {
			setField(field.getKey(), field.getValue());
		}
	}

	// TODO: Javadoc
	public FormArea getArea(String name) {
		return areas.get(name);
	}

	// TODO: Javadoc
	public HashMap<String, FormArea> getAreas() {
		return areas;
	}

	// TODO: Javadoc
	public void setArea(String fieldName, FormArea area) {
		areas.put(fieldName, area);
		areaList.add(area);
	}

	// TODO: Javadoc
	public void setAreas(HashMap<String, FormArea> areas) {
		this.areas = areas;
		areaList.addAll(areas.values());
	}

	/**
	 * Returns the list of the areas of the FormTemplate object.
	 *
	 * @author Alberto Borsetta
	 * @return the areas of the FormTemplate object
	 */
	public ArrayList<FormArea> getFieldAreas() {
		return areaList;
	}

	/**
	 * Sets the corners of the FormTemplate object.
	 *
	 * @author Alberto Borsetta
	 * @param corners
	 *            the corners to set
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
	 * @param corner
	 *            the corner to set
	 * @param point
	 *            the point of the corner
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
	 * @param rotation
	 *            the new rotation of the FormTemplate object
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
	 * @param fieldName
	 *            the name of the field to remove
	 */
	public void removeFieldByName(String fieldName) {

		FormQuestion field = fields.get(fieldName);
		for (Entry<String, FormPoint> point : field.getPoints().entrySet()) {
			pointList.remove(point.getValue());
		}

		fields.remove(fieldName);
	}

	/*
	 * (non-Javadoc)
	 * 
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
	 * @throws ParserConfigurationException
	 *             the parser configuration exception
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
	 * @param corner
	 *            the corner
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
	 * @param diag
	 *            the new diagonal
	 */
	public void setDiagonal(double diag) {
		diagonal = diag;
	}

	/**
	 * Find the corners of the FormTemplate object.
	 * <ul>
	 * <li>threshold: is the value of the RGB components beyond which the pixels
	 * are considered "blacks", for example, if Threshold = 127 the software
	 * considers as blacks all pixels whose value is less than 127, if the pixel
	 * value is greater than or equal to 127 then it is considered as white.
	 * <li>density: is the amount (in percentage) of black pixels (determined as
	 * described above) that must be present in the bubble in order to identify
	 * the selected responses, for example, if Density = 50 (%) is sufficient
	 * that at least half of the pixels in the area of the bubble are blacks to
	 * select the response.
	 * </ul>
	 *
	 * @author Alberto Borsetta
	 * @param image
	 *            the image on which to find the corners
	 * @param threshold
	 *            the value of threshold parameter
	 * @param density
	 *            the value of density parameter
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
	 * <li>threshold: is the value of the RGB components beyond which the pixels
	 * are considered "blacks", for example, if Threshold = 127 the software
	 * considers as blacks all pixels whose value is less than 127, if the pixel
	 * value is greater than or equal to 127 then it is considered as white.
	 * <li>density: is the amount (in percentage) of black pixels (determined as
	 * described above) that must be present in the bubble in order to identify
	 * the selected responses, for example, if Density = 50 (%) is sufficient
	 * that at least half of the pixels in the area of the bubble are blacks to
	 * select the response.
	 * </ul>
	 * Find points.
	 *
	 * @author Alberto Borsetta
	 * @param image
	 *            the image on which to find the corners
	 * @param threshold
	 *            the value of threshold parameter
	 * @param density
	 *            the value of density parameter
	 * @param size
	 *            the size of the area of a single point
	 */
	public void findPoints(BufferedImage image, int threshold, int density,
			int size) {
		height = image.getHeight();
		width = image.getWidth();

		ExecutorService threadPool = Executors.newFixedThreadPool(8);
		HashSet<Future<HashMap<String, FormQuestion>>> fieldDetectorThreads = new HashSet<Future<HashMap<String, FormQuestion>>>();

		HashMap<String, FormQuestion> templateFields = template.getFields();
		ArrayList<String> fieldNames = new ArrayList<String>(
				templateFields.keySet());
		Collections.sort(fieldNames);

		for (String fieldName : fieldNames) {
			Future<HashMap<String, FormQuestion>> future = threadPool
					.submit(new FieldDetector(threshold, density, size, this,
							templateFields.get(fieldName), image));
			fieldDetectorThreads.add(future);
		}

		for (Future<HashMap<String, FormQuestion>> thread : fieldDetectorThreads) {
			try {
				HashMap<String, FormQuestion> threadFields = thread.get();
				for (String fieldName : threadFields.keySet()) {
					FormQuestion field = threadFields.get(fieldName);
					fields.put(fieldName, field);
					for (Entry<String, FormPoint> point : field.getPoints()
							.entrySet()) {
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
	 * @param cursorPoint
	 *            the point to remove
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

			for (Entry<String, FormQuestion> field : fields.entrySet()) {
				FormQuestion fieldValue = field.getValue();
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
	 * @param cursorPoint
	 *            the point to add
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
				point.rotoTranslate(corners.get(Corners.TOP_LEFT), rotation,
						false);

				double lastDistance = cursorPoint.dist2(point);
				if (lastDistance < firstDistance) {
					nearestTemplatePoint = templatePoint;
					firstDistance = lastDistance;
				}
			}

			HashMap<String, FormQuestion> templateFields = template.getFields();
			for (Entry<String, FormQuestion> templateField : templateFields
					.entrySet()) {
				FormQuestion fieldValue = templateField.getValue();
				for (Entry<String, FormPoint> templatePoint : fieldValue
						.getPoints().entrySet()) {
					if (nearestTemplatePoint.equals(templatePoint.getValue())) {
						FormQuestion currentField = fields.get(templateField
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
	 * @param i
	 *            the index
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

	public String getVersion() {
		return version;
	}

	// TODO: javadoc
	public void findAreas(BufferedImage image) {
		height = image.getHeight();
		width = image.getWidth();
		
		ExecutorService threadPool = Executors.newFixedThreadPool(8);
		HashSet<Future<HashMap<String, FormArea>>> barcodeDetectorThreads = new HashSet<Future<HashMap<String, FormArea>>>();
		
		HashMap<String, FormArea> barcodeFields = template.getAreas();
		ArrayList<String> barcodeNames = new ArrayList<String>(barcodeFields.keySet());
		Collections.sort(barcodeNames);

		for (String barcodeName: barcodeNames) {
			FormArea area = barcodeFields.get(barcodeName);
			BufferedImage subImage = getAreaImage(image, area);
			Future<HashMap<String, FormArea>> future = threadPool.submit(new BarcodeDetector(
					this, area, subImage));
			barcodeDetectorThreads.add(future);
		}
		
		for (Future<HashMap<String, FormArea>> thread: barcodeDetectorThreads) {
			try {
				HashMap<String, FormArea> threadFields = thread.get();
				for (String barcodeName: threadFields.keySet()) {
					FormArea barcode = threadFields.get(barcodeName);
					areas.put(barcodeName, barcode);
					areaList.add(barcode);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private BufferedImage getAreaImage(BufferedImage image, FormArea area) {
		int minX = (int) Math.min(area.getCorner(Corners.TOP_LEFT).getX(), area.getCorner(Corners.BOTTOM_LEFT).getX());
		int minY = (int) Math.min(area.getCorner(Corners.TOP_LEFT).getY(), area.getCorner(Corners.TOP_RIGHT).getY());
		int maxX = (int) Math.max(area.getCorner(Corners.TOP_RIGHT).getX(), area.getCorner(Corners.BOTTOM_RIGHT).getX());
		int maxY = (int) Math.max(area.getCorner(Corners.BOTTOM_LEFT).getY(), area.getCorner(Corners.BOTTOM_RIGHT).getY());
		int subImageWidth = maxX - minX;
		int hsubImageHeight = maxY - minY;
		BufferedImage subImage = image.getSubimage(minX, minY, subImageWidth, hsubImageHeight);
		return subImage;
	}
}