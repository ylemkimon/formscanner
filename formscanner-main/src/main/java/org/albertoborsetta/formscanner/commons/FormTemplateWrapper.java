package org.albertoborsetta.formscanner.commons;

import java.io.File;
import java.io.IOException;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.albertoborsetta.formscanner.commons.FormScannerConstants.Corners;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.FieldType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class FormTemplateWrapper {

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

	public static String getString(FormTemplate template) {
		try {
			return getXml(template).toString();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		}
	}
}