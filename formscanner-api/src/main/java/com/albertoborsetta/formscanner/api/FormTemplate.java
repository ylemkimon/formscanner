package com.albertoborsetta.formscanner.api;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.codec.binary.Base64;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.naming.InitialContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.albertoborsetta.formscanner.api.commons.Constants;
import com.albertoborsetta.formscanner.api.commons.Constants.CornerType;
import com.albertoborsetta.formscanner.api.commons.Constants.Corners;
import com.albertoborsetta.formscanner.api.commons.Constants.FieldType;
import com.albertoborsetta.formscanner.api.commons.Constants.ShapeType;
import com.albertoborsetta.formscanner.api.exceptions.FormScannerException;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

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
 * filledForm.findPoints(image, threshold, density, shapeSize);
 * }
 * </pre>
 *
 * Example of an XML representation for a template:
 *
 * <pre>
 * {@code
 * <?xml version="1.0" encoding="UTF-8" standalone="no"?>
 * <template density="40" threshold="127" version="3.0">
 * <image name="image.jpg"/>BINARY IMAGE DATA</image>
 * 	<rotation angle="0.0"/>
 * <crop top="0" left="0" right="0" bottom="0"/>
 * 	<corners type="ANGULAR">
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
 * 	<fields groups="true" shape="SQUARE" size="20">
 * 		<group name="first">
 * 			<question multiple="false" type="QUESTIONS_BY_ROWS" question="Question 01">
 * 				<values>
 * 					<value response="Response 01">
 * 						<point x="800.0" y="1300.0" />
 * 					</value>
 * 					<value response="Response 02">
 * 						<point x="925.0" y="1300.0" />
 * 					</value>
 * 					<value response="Response 03">
 * 						<point x="1050.0" y="1300.0" />
 * 					</value>
 * 					<value response="Response 04">
 * 						<point x="1175.0" y="1300.0" />
 * 					</value>
 * 				</values>
 * 			</question>
 * 			<question multiple="false" type="QUESTIONS_BY_COLS" question="Question 02">
 * 				<values>
 * 					<value response="Response 01">
 * 						<point x="800.0" y="1400.0" />
 * 					</value>
 * 					<value response="Response 02">
 * 						<point x="800.0" y="1525.0" />
 * 					</value>
 * 					<value response="Response 03">
 * 						<point x="800.0" y="1650.0" />
 * 					</value>
 * 					<value response="Response 04">
 * 						<point x="800.0" y="1725.0" />
 * 					</value>
 * 				</values>
 * 			</question>
 * 		</group>
 * 		<group name="second">
 * 			<question multiple="false" type="RESPONSES_BY_GRID" question="Question 03">
 * 				<values>
 * 					<value response="Response A">
 * 						<point x="800.0" y="1850.0" />
 * 					</value>
 * 					<value response="Response B">
 * 						<point x="925.0" y="1850.0" />
 * 					</value>
 * 					<value response="Response C">
 * 						<point x="1050.0" y="1850.0" />
 * 					</value>
 * 					<value response="Response D">
 * 						<point x="800.0" y="1975.0" />
 * 					</value>
 * 					<value response="Response E">
 * 						<point x="925.0" y="1975.0" />
 * 					</value>
 * 					<value response="Response F">
 * 						<point x="1050.0" y="1975.0" />
 * 					</value>
 * 				</values>
 * 			</question>
 * 		</group>
 * 		<group>
 * 			<area type="BARCODE" name="Barcode">
 * 				<corners>
 * 					<corner position="TOP_LEFT">
 * 						<point x="800.0" y="2100.0" />
 * 					</corner>
 * 					<corner position="TOP_RIGHT">
 * 						<point x="1200.0" y="2100.0" />
 * 					</corner>
 * 					<corner position="BOTTOM_LEFT">
 * 						<point x="800.0" y="2300.0" />
 * 					</corner>
 * 					<corner position="BOTTOM_RIGHT">
 * 						<point x="1200.0" y="2300.0" />
 * 					</corner>
 * 				</corners>
 * 			</area>
 * 		</group>
 * 	</fields>
 * </template>
 * }
 * </pre>
 *
 * @author Alberto Borsetta
 * @version 1.2-SNAPSHOT
 * @see FormPoint
 * @see FormQuestion
 * @see FieldType
 * @see Corners
 * @see CornerType
 */
public final class FormTemplate {

	private HashMap<String, FormGroup> groups;
	private HashMap<Corners, FormPoint> corners;

	private ArrayList<FormPoint> pointList;
	private ArrayList<FormArea> areaList;

	private CornerType cornerType;
	private ShapeType shape;
	private FormTemplate template;
	private String version = null;
	private double rotation;
	private double diagonal;
	private int height;
	private int width;
	private Integer threshold;
	private Integer density;
	private Integer size;
	private boolean isGroupsEnabled = false;
	private HashMap<String, Integer> crop = new HashMap<>();
	private ArrayList<String> usedGroupNames = new ArrayList<>();
	private String imageName;
	private BufferedImage image;

	private static class FormTemplateWrapper {

		public static Document getXml(FormTemplate template) throws ParserConfigurationException, IOException {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root element
			Document doc = docBuilder.newDocument();
			Element templateElement = doc.createElement("template");
			templateElement.setAttribute("version", Constants.CURRENT_TEMPLATE_VERSION);
			if (template.getThreshold() != null) {
				templateElement.setAttribute("threshold", String.valueOf(template.getThreshold()));
			}
			if (template.getDensity() != null) {
				templateElement.setAttribute("density", String.valueOf(template.getDensity()));
			}

			// imageElement
			Element imageElement = doc.createElement("image");
			imageElement.setAttribute("name", template.getImageName());
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(template.getImage(), FilenameUtils.getExtension(template.getImageName()), baos);
			baos.flush();
			Base64 encoder = new Base64();
			String encodedImage = encoder.encodeToString(baos.toByteArray());
			baos.close();
			imageElement.setTextContent(encodedImage);
			templateElement.appendChild(imageElement);

			// crop element
			Element cropElement = doc.createElement("crop");
			HashMap<String, Integer> crop = template.getCrop();
			cropElement.setAttribute("top", crop.isEmpty() ? "0" : String.valueOf(crop.get(Constants.TOP)));
			cropElement.setAttribute("left", crop.isEmpty() ? "0" : String.valueOf(crop.get(Constants.LEFT)));
			cropElement.setAttribute("right", crop.isEmpty() ? "0" : String.valueOf(crop.get(Constants.RIGHT)));
			cropElement.setAttribute("bottom", crop.isEmpty() ? "0" : String.valueOf(crop.get(Constants.BOTTOM)));
			templateElement.appendChild(cropElement);

			// rotation element
			Element rotationElement = doc.createElement("rotation");
			rotationElement.setAttribute("angle", String.valueOf(template.getRotation()));
			templateElement.appendChild(rotationElement);

			// corners element
			Element cornersElement = doc.createElement("corners");
			if (template.getCornerType() != null) {
				cornersElement.setAttribute("type", template.getCornerType().getName());
			}
			templateElement.appendChild(cornersElement);

			// corner elements
			for (Entry<Corners, FormPoint> corner : template.getCorners().entrySet()) {
				Element cornerElement = doc.createElement("corner");
				cornerElement.setAttribute("position", corner.getKey().getName());
				cornerElement.appendChild(corner.getValue().getXml(doc));
				cornersElement.appendChild(cornerElement);
			}

			// fields element
			Element fieldsElement = doc.createElement("fields");
			fieldsElement.setAttribute("groups", String.valueOf(template.isGroupsEnabled()));
			if (template.getSize() != null) {
				fieldsElement.setAttribute("size", String.valueOf(template.getSize()));
			}
			if (template.getShapeType() != null) {
				fieldsElement.setAttribute("shape", template.getShapeType().getName());
			}
			templateElement.appendChild(fieldsElement);

			for (Entry<String, FormGroup> group : template.getGroups().entrySet()) {
				// group element
				Element groupElement = doc.createElement("group");
				groupElement.setAttribute("name", group.getKey());

				// question elements
				for (Entry<String, FormQuestion> field : group.getValue().getFields().entrySet()) {
					groupElement.appendChild(field.getValue().getXml(doc));
				}

				// area elements
				for (Entry<String, FormArea> area : group.getValue().getAreas().entrySet()) {
					groupElement.appendChild(area.getValue().getXml(doc));
				}

				fieldsElement.appendChild(groupElement);
			}

			doc.appendChild(templateElement);
			return doc;
		}

		public static void presetFromTemplate(File file, FormTemplate template)
				throws ParserConfigurationException, SAXException, IOException {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(file);
			doc.getDocumentElement().normalize();

			Element templateElement = doc.getDocumentElement();
			template.setVersion(templateElement.getAttribute("version"));
			template.setThreshold(
					Integer.parseInt(StringUtils.defaultIfBlank(templateElement.getAttribute("threshold"), "-1")));
			template.setDensity(
					Integer.parseInt(StringUtils.defaultIfBlank(templateElement.getAttribute("density"), "-1")));

			// Compatibility with version 1.0
			String shapeType = templateElement.getAttribute("shape");
			String size = templateElement.getAttribute("size");

			Element imageElement = (Element) templateElement.getElementsByTagName("image").item(0);
			template.setImageName(imageElement.getAttribute("name"));
			String encodedImage = imageElement.getTextContent();
			Base64 decoder = new Base64();
			byte[] bytes = decoder.decode(encodedImage);
			template.setImage(ImageIO.read(new ByteArrayInputStream(bytes)));

			Element rotationElement = (Element) templateElement.getElementsByTagName("rotation").item(0);
			template.setRotation(Double.parseDouble(rotationElement.getAttribute("angle")));

			Element cropElement = (Element) templateElement.getElementsByTagName("crop").item(0);
			template.setCrop(Integer.parseInt((cropElement != null) ? cropElement.getAttribute("top") : "0"),
					Integer.parseInt((cropElement != null) ? cropElement.getAttribute("left") : "0"),
					Integer.parseInt((cropElement != null) ? cropElement.getAttribute("right") : "0"),
					Integer.parseInt((cropElement != null) ? cropElement.getAttribute("bottom") : "0"));

			Element cornersElement = (Element) templateElement.getElementsByTagName("corners").item(0);
			template.setCornerType(StringUtils.isNotBlank(cornersElement.getAttribute("type"))
					? CornerType.valueOf(cornersElement.getAttribute("type")) : CornerType.ROUND);
			addCorners(template, cornersElement);

			template.calculateDiagonal();

			Element fieldsElement = (Element) templateElement.getElementsByTagName("fields").item(0);

			template.setSize(Integer.parseInt(StringUtils.defaultIfBlank(size, fieldsElement.getAttribute("size"))));
			template.setShapeType(StringUtils.isNotBlank(shapeType) ? ShapeType.valueOf(shapeType)
					: ShapeType.valueOf(fieldsElement.getAttribute("shape")));

			template.setGroupsEnabled(Boolean.parseBoolean(fieldsElement.getAttribute("groups")));

			NodeList groupsList = fieldsElement.getElementsByTagName("group");

			if (groupsList.getLength() > 0) {
				ArrayList<String> groupNamesList = new ArrayList<>();
				for (int i = 0; i < groupsList.getLength(); i++) {
					Element groupElement = (Element) groupsList.item(i);
					groupNamesList.add(groupElement.getAttribute("name"));

					addQuestions(template, groupElement);
					addAreas(template, groupElement);
				}
				template.setUsedGroupNames(groupNamesList);
			} else { // Version < 2.x
				addQuestions(template, templateElement);
				addAreas(template, templateElement);
			}
		}

		private static FormArea getArea(Element element) {
			FormArea area = new FormArea(element.getAttribute("name"));

			area.setType(FieldType.valueOf(element.getAttribute("type")));

			Element cornersElement = (Element) element.getElementsByTagName("corners").item(0);
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
			Element pointElement = (Element) element.getElementsByTagName("point").item(0);
			String xCoord = pointElement.getAttribute("x");
			String yCoord = pointElement.getAttribute("y");

			FormPoint point = new FormPoint(Double.parseDouble(xCoord), Double.parseDouble(yCoord));
			return point;
		}

		private static void addAreas(FormTemplate template, Element element) {

			NodeList areasList = element.getElementsByTagName("area");
			for (int i = 0; i < areasList.getLength(); i++) {
				Element areaElement = (Element) areasList.item(i);

				FormArea area = getArea(areaElement);

				String groupName = "group".equals(element.getNodeName()) ? element.getAttribute("name")
						: areaElement.getAttribute("group");
				groupName = StringUtils.defaultIfBlank(groupName, Constants.EMPTY_GROUP_NAME);

				template.addArea(groupName, area);
			}

		}

		private static void addQuestions(FormTemplate template, Element element) {

			NodeList questionsList = element.getElementsByTagName("question");
			for (int i = 0; i < questionsList.getLength(); i++) {
				Element questionElement = (Element) questionsList.item(i);
				String questionName = questionElement.getAttribute("question");

				FormQuestion field = getQuestion(questionElement, questionName);

				String groupName = "group".equals(element.getNodeName()) ? element.getAttribute("name")
						: questionElement.getAttribute("group");
				groupName = StringUtils.defaultIfBlank(groupName, Constants.EMPTY_GROUP_NAME);

				template.addField(groupName, questionName, field);
			}

			// TODO: Tag "field" deprecated. To be removed
			questionsList = element.getElementsByTagName("field");
			for (int i = 0; i < questionsList.getLength(); i++) {
				Element fieldElement = (Element) questionsList.item(i);
				String fieldName = fieldElement.getAttribute("question");

				FormQuestion field = getQuestion(fieldElement, fieldName);

				template.addField(Constants.EMPTY_GROUP_NAME, fieldName, field);
			}
		}

		private static FormQuestion getQuestion(Element element, String name) {
			FormQuestion field = new FormQuestion(name);
			field.setMultiple(Boolean.parseBoolean(element.getAttribute("multiple")));
			field.setRejectMultiple(Boolean.parseBoolean(element.getAttribute("rejectMultiple")));

			// TODO: Attribute "orientation" deprecated. To be removed
			field.setType(FieldType.valueOf((element.getAttribute("orientation").isEmpty())
					? element.getAttribute("type") : element.getAttribute("orientation")));

			Element valuesElement = (Element) element.getElementsByTagName("values").item(0);
			NodeList valueList = valuesElement.getElementsByTagName("value");
			for (int j = 0; j < valueList.getLength(); j++) {
				Element valueElement = (Element) valueList.item(j);
				FormPoint point = getPoint(valueElement);
				field.setPoint(valueElement.getAttribute("response"), point);
			}
			return field;
		}

		public static String toString(FormTemplate template)
				throws ParserConfigurationException, TransformerException, IOException {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			// initialize StreamResult with File object to save to file
			StreamResult result = new StreamResult(new StringWriter());
			DOMSource source = new DOMSource(getXml(template));
			transformer.transform(source, result);
			String xmlString = result.getWriter().toString();
			return xmlString;
		}
	}

	/**
	 * Returns the Image of the FormTemplate object field.
	 *
	 * @author Alberto Borsetta
	 * @return the image
	 */
	public BufferedImage getImage() {
		return image;
	}

	/**
	 * Sets the image of the FormTemplate object.
	 *
	 * @author Alberto Borsetta
	 * @param image
	 *            the image of the FormTemplate object
	 */
	public void setImage(BufferedImage image) {
		this.image = image;
	}

	/**
	 * Sets the name of the template image.
	 *
	 * @author Alberto Borsetta
	 * @param name
	 *            the name of the image
	 */
	public void setImageName(String name) {
		this.imageName = name;
	}

	public void setUsedGroupNames(ArrayList<String> groupNamesList) {
		usedGroupNames = groupNamesList;
	}

	/**
	 * Returns the GroupsEnabled field.
	 *
	 * @author Alberto Borsetta
	 * @return isGroupsEnabled
	 */
	public boolean isGroupsEnabled() {
		return isGroupsEnabled;
	}

	/**
	 * Sets the GroupsEnabled field.
	 *
	 * @author Alberto Borsetta
	 * @param enabled
	 *            is enabled
	 */
	public void setGroupsEnabled(boolean enabled) {
		isGroupsEnabled = enabled;
	}

	/**
	 * Adds a group to the FormTemplate object
	 * 
	 * @author Alberto Borsetta
	 * @param groupName
	 *            the name of the group
	 * @param group
	 *            the group to add
	 * @see FormGroup
	 */
	public void addGroup(String groupName, FormGroup group) {
		groups.put(groupName, group);
	}

	/**
	 * Sets the corner type of the template.
	 *
	 * @author Alberto Borsetta
	 * @param cornerType
	 *            the corner type
	 * @see CornerType
	 */
	public void setCornerType(CornerType cornerType) {
		this.cornerType = cornerType;
	}

	/**
	 * Returns the corner type of the template.
	 *
	 * @author Alberto Borsetta
	 * @return the corner type
	 */
	public CornerType getCornerType() {
		return cornerType;
	}

	/**
	 * Sets the version of the template.
	 *
	 * @author Alberto Borsetta
	 * @param version
	 *            the version
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Instantiates a new empty form template.
	 *
	 * @author Alberto Borsetta
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public FormTemplate() throws IOException {
		this(null, null);
	}

	/**
	 * Presets an empty form template from an xml representation.
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
	public void presetFormTemplate(File xmlFile) throws ParserConfigurationException, SAXException, IOException {
		FormTemplateWrapper.presetFromTemplate(xmlFile, this);
	}

	/**
	 * Instantiates a new FormTemplate object with the given image file and
	 * parent template.
	 *
	 * @author Alberto Borsetta
	 * @param image
	 *            the image of the FormTemplate object
	 * @param template
	 *            the parent template
	 * @throws IOException
	 */
	public FormTemplate(File imageFile, FormTemplate template) throws IOException {
		this.template = template;
		if (imageFile != null) {
			image = ImageIO.read(imageFile);
			imageName = imageFile.getName();
		}

		corners = new HashMap<>();
		setDefaultCornerPosition();
		calculateDiagonal();
		rotation = 0;
		pointList = new ArrayList<>();
		areaList = new ArrayList<>();
		groups = new HashMap<>();
		if (template != null) {
			presetFromTemplate();
		}

	}

	/**
	 * Instantiates a new empty FormTemplate object.
	 *
	 * @author Alberto Borsetta
	 * @param name
	 *            the name of the FormTemplate object
	 * @throws IOException
	 */
	public FormTemplate(File imageFile) throws IOException {
		this(imageFile, null);
	}

	private void presetFromTemplate() {
		cornerType = template.getCornerType();
		crop = template.getCrop();
		density = template.getDensity();
		shape = template.getShapeType();
		size = template.getSize();
		threshold = template.getThreshold();
		version = template.getVersion();
	}

	/**
	 * Calculates the diagonal of the FormTemplate.
	 *
	 * @author Alberto Borsetta
	 */
	public void calculateDiagonal() {
		diagonal = (corners.get(Corners.TOP_LEFT).dist2(corners.get(Corners.BOTTOM_RIGHT))
				+ corners.get(Corners.TOP_RIGHT).dist2(corners.get(Corners.BOTTOM_LEFT))) / 2;
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
	 * Returns the group identified by the given name.
	 *
	 * @author Alberto Borsetta
	 * @param name
	 *            the name of the group
	 * @return the FormGroup object
	 * @see FormGroup
	 */
	public FormGroup getGroup(String name) {
		return groups.get(name);
	}

	/**
	 * Returns all the groups in the FormTemplate object.
	 *
	 * @author Alberto Borsetta
	 * @return the fields of the FormTemplate object
	 * @see FormGroup
	 */
	public HashMap<String, FormGroup> getGroups() {
		return groups;
	}

	/**
	 * Add a field to the group with the given name.
	 *
	 * @author Alberto Borsetta
	 * @param groupName
	 *            the name of the group
	 * @param fieldName
	 *            tha name of the field
	 * @param field
	 *            the field to set
	 * @see FormQuestion
	 * @see FormGroup
	 */
	public void addField(String groupName, String fieldName, FormQuestion field) {
		FormGroup group = groups.get(groupName);
		if (group == null) {
			group = new FormGroup();
		}
		group.setField(fieldName, field);
		groups.put(groupName, group);

		for (Entry<String, FormPoint> point : field.getPoints().entrySet()) {
			if (point.getValue() != null) {
				pointList.add(point.getValue());
			}
		}
	}

	/**
	 * Add an Area to the group with the given name.
	 *
	 * @author Alberto Borsetta
	 * @param groupName
	 *            the name of the group
	 * @param areaName
	 *            the name of the Area
	 * @param area
	 *            the Area to set
	 * @see FormQuestion
	 * @see FormGroup
	 */
	public void addArea(String groupName, String areaName, FormArea area) {
		FormGroup group = groups.get(groupName);
		if (group == null) {
			group = new FormGroup();
		}
		group.setArea(areaName, area);
		groups.put(groupName, group);

		areaList.add(area);
	}

	/**
	 * Sets all the fields in the FormTemplate object.
	 *
	 * @author Alberto Borsetta
	 * @param groupName
	 *            the group name
	 * @param fields
	 *            the fields to set
	 */
	public void addFields(String groupName, HashMap<String, FormQuestion> fields) {

		for (Entry<String, FormQuestion> field : fields.entrySet()) {
			addField(groupName, field.getKey(), field.getValue());
		}
	}

	/**
	 * Sets an area in the FormTemplate object.
	 *
	 * @author Alberto Borsetta
	 * @param groupName
	 *            the group name
	 * @param area
	 *            the area to set
	 */
	public void addArea(String groupName, FormArea area) {
		addArea(groupName, area.getName(), area);
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
	 * @param groupName
	 *            the group name
	 * @param fieldName
	 *            the name of the field to remove
	 */
	public void removeFieldByName(String groupName, String fieldName) {

		FormGroup group = groups.get(groupName);
		FormQuestion field = group.getFields().get(fieldName);
		for (Entry<String, FormPoint> point : field.getPoints().entrySet()) {
			pointList.remove(point.getValue());
		}

		group.removeField(fieldName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		try {
			return FormTemplateWrapper.toString(this);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Returns the xml representation of the FormTemplate object.
	 *
	 * @author Alberto Borsetta
	 * @return the xml representation of the FormTemplate object
	 * @throws ParserConfigurationException
	 *             the parser configuration exception
	 * @throws IOException
	 */
	public Document getXml() throws ParserConfigurationException, IOException {
		return FormTemplateWrapper.getXml(this);
	}

	/**
	 * Returns the name of the image of the FormTemplate object.
	 *
	 * @author Alberto Borsetta
	 * @return the name of the image of the FormTemplate object
	 */
	public String getImageName() {
		return imageName;
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
	 * @param cornerType
	 *            the corner type
	 * @param crop
	 *            the crop values
	 * @throws FormScannerException
	 *             throws FormScannerException
	 * @see CornerType
	 */
	public void findCorners(int threshold, int density, CornerType cornerType, HashMap<String, Integer> crop)
			throws FormScannerException {
		Integer top = crop.get(Constants.TOP);
		Integer bottom = crop.get(Constants.BOTTOM);
		Integer left = crop.get(Constants.LEFT);
		Integer right = crop.get(Constants.RIGHT);

		width = image.getWidth() - (left + right);
		height = image.getHeight() - (top + bottom);

		BufferedImage croppedImage = image.getSubimage(left, top, width, height);

		int cores = Runtime.getRuntime().availableProcessors();

		//		Only for debug
		//		cores = 1;

		ExecutorService threadPool = Executors.newFixedThreadPool(--cores <= 0 ? 1 : cores);
		HashMap<Corners, Future<FormPoint>> cornerDetectorThreads = new HashMap<>();

		for (Corners position : Corners.values()) {
			Future<FormPoint> future = threadPool
					.submit(new CornerDetector(threshold, density, position, croppedImage, cornerType));
			cornerDetectorThreads.put(position, future);
		}

		for (Corners position : Corners.values()) {
			try {
				FormPoint corner = cornerDetectorThreads.get(position).get();
				corner.setX(corner.getX() + left);
				corner.setY(corner.getY() + top);
				if (corner != null) {
					corners.put(position, corner);
				}
			} catch (InterruptedException | ExecutionException e) {
				throw new FormScannerException(e.getCause());
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
	 * considers as blacks all pixels whose value is less than 127, if the pi
	 * xel value is greater than or equal to 127 then it is considered as white.
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
	 * @throws FormScannerException
	 *             throws FormScannerException
	 */
	public void findPoints(int threshold, int density, int size) throws FormScannerException {
		height = image.getHeight();
		width = image.getWidth();
		int cores = Runtime.getRuntime().availableProcessors();
//		Only for debug
//		cores = 1;

		HashMap<String, FormGroup> templateGroups = template.getGroups();
		for (Entry<String, FormGroup> templateGroup : templateGroups.entrySet()) {
			ExecutorService threadPool = Executors.newFixedThreadPool(--cores <= 0 ? 1 : cores);
			HashSet<Future<HashMap<String, FormQuestion>>> fieldDetectorThreads = new HashSet<>();

			HashMap<String, FormQuestion> templateFields = templateGroup.getValue().getFields();
			ArrayList<String> fieldNames = new ArrayList<>(templateFields.keySet());
			Collections.sort(fieldNames);

			for (String fieldName : fieldNames) {
				Future<HashMap<String, FormQuestion>> future = threadPool.submit(
						new FieldDetector(threshold, density, size, this, templateFields.get(fieldName), image));
				fieldDetectorThreads.add(future);
			}

			for (Future<HashMap<String, FormQuestion>> thread : fieldDetectorThreads) {
				try {
					HashMap<String, FormQuestion> threadFields = thread.get();
					for (String fieldName : threadFields.keySet()) {
						FormQuestion field = threadFields.get(fieldName);
						addField(templateGroup.getKey(), fieldName, field);
					}
				} catch (InterruptedException | ExecutionException e) {
					throw new FormScannerException(e.getCause());
				}
			}
			threadPool.shutdown();
		}
	}

	private void calculateRotation() {
		FormPoint topLeftPoint = corners.get(Corners.TOP_LEFT);
		FormPoint topRightPoint = corners.get(Corners.TOP_RIGHT);

		double dx = topRightPoint.getX() - topLeftPoint.getX();
		double dy = topLeftPoint.getY() - topRightPoint.getY();

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

			for (Entry<String, FormGroup> group : groups.entrySet()) {
				HashMap<String, FormQuestion> fields = group.getValue().getFields();
				for (Entry<String, FormQuestion> field : fields.entrySet()) {
					FormQuestion fieldValue = field.getValue();
					for (Entry<String, FormPoint> point : fieldValue.getPoints().entrySet()) {
						if (nearestPoint.equals(point.getValue())) {
							fieldValue.getPoints().remove(point.getKey());
							return;
						}
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
		FormPoint templateOrigin = template.getCorner(Corners.TOP_LEFT);
		double templateRotation = template.getRotation();
		double scale = Math.sqrt(diagonal / template.getDiagonal());
		ArrayList<FormPoint> templatePoints = template.getFieldPoints();
		FormPoint point;
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

			HashMap<String, FormGroup> templateGroups = template.getGroups();
			for (Entry<String, FormGroup> templateGroup : templateGroups.entrySet()) {
				HashMap<String, FormQuestion> templateFields = templateGroup.getValue().getFields();
				for (Entry<String, FormQuestion> templateField : templateFields.entrySet()) {
					FormQuestion fieldValue = templateField.getValue();
					for (Entry<String, FormPoint> templatePoint : fieldValue.getPoints().entrySet()) {
						if (nearestTemplatePoint.equals(templatePoint.getValue())) {
							FormQuestion currentField = groups.get(templateGroup.getKey()).getFields()
									.get(templateField.getKey());
							currentField.setPoint(templatePoint.getKey(), cursorPoint);
							addField(templateGroup.getKey(), templateField.getKey(), currentField);
							return;
						}
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
		for (Entry<String, FormGroup> group : groups.entrySet()) {
			group.getValue().clearFields();
		}
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

	/**
	 * Returns the version of the template.
	 *
	 * @author Alberto Borsetta
	 * @return the version
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Find the areas with barcodes from a FormTemplate object.
	 *
	 * @author Alberto Borsetta
	 * @param image
	 *            the image on which to find the barcode
	 * @throws FormScannerException
	 *             throws FormScannerException
	 */
	public void findAreas() throws FormScannerException {
		height = image.getHeight();
		width = image.getWidth();
		int cores = Runtime.getRuntime().availableProcessors();
//		Only for debug
//		cores = 1;

		HashMap<String, FormGroup> templateGroups = template.getGroups();
		for (Entry<String, FormGroup> templateGroup : templateGroups.entrySet()) {
			ExecutorService threadPool = Executors.newFixedThreadPool(--cores <= 0 ? 1 : cores);
			HashSet<Future<HashMap<String, FormArea>>> barcodeDetectorThreads = new HashSet<>();

			HashMap<String, FormArea> barcodeFields = templateGroup.getValue().getAreas();
			ArrayList<String> barcodeNames = new ArrayList<>(barcodeFields.keySet());
			Collections.sort(barcodeNames);

			for (String barcodeName : barcodeNames) {
				FormArea area = barcodeFields.get(barcodeName);
				BufferedImage subImage = getAreaImage(image, area);
				Future<HashMap<String, FormArea>> future = threadPool.submit(new BarcodeDetector(this, area, subImage));
				barcodeDetectorThreads.add(future);
			}

			for (Future<HashMap<String, FormArea>> thread : barcodeDetectorThreads) {
				try {
					HashMap<String, FormArea> threadFields = thread.get();
					for (String barcodeName : threadFields.keySet()) {
						FormArea barcode = threadFields.get(barcodeName);
						addArea(templateGroup.getKey(), barcodeName, barcode);
					}
				} catch (InterruptedException | ExecutionException e) {
					throw new FormScannerException(e.getCause());
				}
			}
			threadPool.shutdown();
		}
	}

	private static BufferedImage getAreaImage(BufferedImage image, FormArea area) {
		int minX = (int) Math.min(area.getCorner(Corners.TOP_LEFT).getX(), area.getCorner(Corners.BOTTOM_LEFT).getX());
		int minY = (int) Math.min(area.getCorner(Corners.TOP_LEFT).getY(), area.getCorner(Corners.TOP_RIGHT).getY());
		int maxX = (int) Math.max(area.getCorner(Corners.TOP_RIGHT).getX(),
				area.getCorner(Corners.BOTTOM_RIGHT).getX());
		int maxY = (int) Math.max(area.getCorner(Corners.BOTTOM_LEFT).getY(),
				area.getCorner(Corners.BOTTOM_RIGHT).getY());
		int subImageWidth = maxX - minX;
		int hsubImageHeight = maxY - minY;
		BufferedImage subImage = image.getSubimage(minX, minY, subImageWidth, hsubImageHeight);
		return subImage;
	}

	/**
	 * Returns the threshold of the template.
	 *
	 * @author Alberto Borsetta
	 * @return the threshold
	 */
	public Integer getThreshold() {
		return threshold;
	}

	/**
	 * Sets the threshold of the template.
	 *
	 * @author Alberto Borsetta
	 * @param threshold
	 *            the threshold
	 */
	public void setThreshold(Integer threshold) {
		this.threshold = threshold;
	}

	/**
	 * Returns the density of the template.
	 *
	 * @author Alberto Borsetta
	 * @return the density
	 */
	public Integer getDensity() {
		return density;
	}

	/**
	 * Sets the density of the template.
	 *
	 * @author Alberto Borsetta
	 * @param density
	 *            the density
	 */
	public void setDensity(Integer density) {
		this.density = density;
	}

	/**
	 * Returns the size of the marker of the template.
	 *
	 * @author Alberto Borsetta
	 * @return the size of the marker
	 */
	public Integer getSize() {
		return size;
	}

	/**
	 * Sets the size of the marker of the template.
	 *
	 * @author Alberto Borsetta
	 * @param size
	 *            the size of the marker
	 */
	public void setSize(Integer size) {
		this.size = size;
	}

	/**
	 * Returns the shape of the marker of the template.
	 *
	 * @author Alberto Borsetta
	 * @return the shape of the marker
	 */
	public ShapeType getShapeType() {
		return shape;
	}

	/**
	 * Sets the shape of the marker of the template.
	 *
	 * @author Alberto Borsetta
	 * @param shape
	 *            the shape of the marker
	 * @see ShapeType
	 */
	public void setShapeType(ShapeType shape) {
		this.shape = shape;
	}

	/**
	 * Returns the last index for the named group.
	 *
	 * @author Alberto Borsetta
	 * @param groupName
	 *            the name of the group
	 * @return the index for the group
	 */
	public int lastIndexOfGroup(String groupName) {
		FormGroup group = groups.get(groupName);
		if (group != null) {
			return group.getLastFieldIndex();
		}
		return 1;
	}

	/**
	 * Returns the crop values for images scanned using current template.
	 *
	 * @author Alberto Borsetta
	 * @return the crop values
	 */
	public HashMap<String, Integer> getCrop() {
		return crop;
	}

	/**
	 * Sets crop values for images scanned using current template
	 * 
	 * @author Alberto Borsetta
	 * @param cropFromTop
	 *            the padding from top margin
	 * @param cropFromLeft
	 *            the padding from left margin
	 * @param cropFromRight
	 *            the padding from right margin
	 * @param cropFromBottom
	 *            the padding from bottom margin
	 */
	public void setCrop(Integer cropFromTop, Integer cropFromLeft, Integer cropFromRight, Integer cropFromBottom) {
		crop.put(Constants.TOP, cropFromTop);
		crop.put(Constants.LEFT, cropFromLeft);
		crop.put(Constants.RIGHT, cropFromRight);
		crop.put(Constants.BOTTOM, cropFromBottom);
	}

	/**
	 * Sets crop values for images scanned using current template
	 * 
	 * @author Alberto Borsetta
	 * @param crop
	 *            the padding from margin
	 */
	public void setCrop(HashMap<String, Integer> crop) {
		this.crop = crop;
	}

	/**
	 * Returns the name of the image file of the FormTemplate object.
	 *
	 * @author Alberto Borsetta
	 * @return the name of the image without extension
	 */
	public String getName() {
		return FilenameUtils.removeExtension(imageName);
	}

	/**
	 * Remove all fields from the FormTemplate object
	 * 
	 * @author Alberto Borsetta
	 */
	public void removeAllFields() {
		for (FormGroup group : groups.values()) {
			group.clearFields();
		}
		groups.clear();
		pointList.clear();
	}
}
