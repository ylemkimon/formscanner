package org.albertoborsetta.formscanner.commons;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.albertoborsetta.formscanner.commons.FormScannerConstants.Corners;
import org.albertoborsetta.formscanner.imageparser.ScanImage;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
 
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
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

	public void saveToFile(String path) {
		try {
			 
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
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
				cornerElement.setAttribute("x", String.valueOf(cornerValue.x));
				cornerElement.setAttribute("y", String.valueOf(cornerValue.y));
				cornersElement.appendChild(cornerElement);
	        }
			
			// fields element
			Element fieldsElement = doc.createElement("fields");
			templateElement.appendChild(fieldsElement);
				
			// field elements
			for (Entry<String, FormField> field : fields.entrySet()) {
				Element fieldElement = doc.createElement("field");
				FormField fieldValue = field.getValue();
				fieldElement.setAttribute("orientation", fieldValue.getType().name());
				fieldElement.setAttribute("multiple", String.valueOf(fieldValue.isMultiple()));
				
				// name element
				Element fieldNameElement = doc.createElement("name");
				fieldNameElement.appendChild(doc.createTextNode(fieldValue.getName()));
				fieldElement.appendChild(fieldNameElement);
				
				// values element
				Element valuesElement = doc.createElement("values");
				
				// value elements
				for (Entry<String, FormPoint> point : fieldValue.getPoints().entrySet()) {
					Element valueElement = doc.createElement("value");
					FormPoint pointValue = point.getValue();
					valueElement.setAttribute("x", String.valueOf(pointValue.x));
					valueElement.setAttribute("y", String.valueOf(pointValue.y));
					valueElement.appendChild(doc.createTextNode(point.getKey()));
					valuesElement.appendChild(valueElement);
				}
				
				fieldElement.appendChild(valuesElement);				
				fieldsElement.appendChild(fieldElement);
	        }
			File outputFile = new File(path + "/template/" + name + ".xtmpl");
			FormFileUtils.getInstance().saveTemplateAs(outputFile, doc);
	 
		  } catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		  } 	
	}

	public void presetFromTemplate(File template) {
		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(template);
			doc.getDocumentElement().normalize();
			 
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
		 
			NodeList nList = doc.getElementsByTagName("template");
		 
			System.out.println("----------------------------");
		 
			for (int temp = 0; temp < nList.getLength(); temp++) {
		 
				Node nNode = nList.item(temp);
		 
				System.out.println("\nCurrent Element :" + nNode.getNodeName());
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element eElement = (Element) nNode;
					
					System.out.println(nNode.getLocalName());
		 
//					System.out.println("Staff id : " + eElement.getAttribute("id"));
//					System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
//					System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
//					System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
//					System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());
		 
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
