package org.albertoborsetta.formscanner.commons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import org.albertoborsetta.formscanner.commons.FormScannerConstants.FieldType;
import org.albertoborsetta.formscanner.commons.FormPoint;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FormField {
	private String name;
	private FieldType type;
	private boolean multiple;
	private HashMap<String, FormPoint> points;

	public FormField(String name, HashMap<String, FormPoint> points) {
		this.name = name;
		this.points = points;
	}

	public FormField(String name) {
		this.name = name;
		points = new HashMap<String, FormPoint>();
	}

	public void setPoint(String value, FormPoint point) {
		points.put(value, point);
	}

	public FormPoint getPoint(String value) {
		return points.get(value);
	}

	public HashMap<String, FormPoint> getPoints() {
		return points;
	}

	public void setType(FieldType type) {
		this.type = type;
	}

	public FieldType getType() {
		return type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isMultiple() {
		return multiple;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	public String getValues() {
		ArrayList<String> results = new ArrayList<String>(points.keySet());
		Collections.sort(results);
		String ret = "";
		for (String result : results) {
			if (StringUtils.isEmpty(ret)) {
				ret += result;
			} else {
				ret += "|" + result;
			}
		}
		return ret;
	}

	public Element getXml(Document doc) {
		Element fieldElement = doc.createElement("field");
		fieldElement.setAttribute("orientation", type.name());
		fieldElement.setAttribute("multiple", String.valueOf(multiple));
		fieldElement.setAttribute("question", name);
		
		// values element
		Element valuesElement = doc.createElement("values");

		// value elements
		for (Entry<String, FormPoint> point : points.entrySet()) {
			Element valueElement = doc.createElement("value");
			valueElement.setAttribute("response", point.getKey());
			valueElement.appendChild(point.getValue().getXml(doc));
			valuesElement.appendChild(valueElement);
		}

		fieldElement.appendChild(valuesElement);
		return fieldElement;
	}
}
