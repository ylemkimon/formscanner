package org.albertoborsetta.formscanner.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import org.albertoborsetta.formscanner.api.FormPoint;
import org.albertoborsetta.formscanner.api.commons.Constants.FieldType;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The Class FormField.
 */
public class FormField {
	
	/** The name. */
	private String name;
	
	/** The type. */
	private FieldType type;
	
	/** The multiple. */
	private boolean multiple;
	
	/** The points. */
	private HashMap<String, FormPoint> points;
	
	/** The reject multiple. */
	private boolean rejectMultiple = false;

	/**
	 * Instantiates a new form field.
	 *
	 * @author Alberto Borsetta
	 * @param name the name
	 * @param points the points
	 */
	public FormField(String name, HashMap<String, FormPoint> points) {
		this.name = name;
		this.points = points;
	}

	/**
	 * Instantiates a new form field.
	 *
	 * @author Alberto Borsetta
	 * @param name the name
	 */
	public FormField(String name) {
		this.name = name;
		points = new HashMap<String, FormPoint>();
	}

	/**
	 * Sets the point.
	 *
	 * @author Alberto Borsetta
	 * @param value the value
	 * @param point the point
	 */
	public void setPoint(String value, FormPoint point) {
		points.put(value, point);
	}

	/**
	 * Gets the point.
	 *
	 * @author Alberto Borsetta
	 * @param value the value
	 * @return the point
	 */
	public FormPoint getPoint(String value) {
		return points.get(value);
	}

	/**
	 * Gets the points.
	 *
	 * @author Alberto Borsetta
	 * @return the points
	 */
	public HashMap<String, FormPoint> getPoints() {
		return points;
	}

	/**
	 * Sets the type.
	 *
	 * @author Alberto Borsetta
	 * @param type the new type
	 */
	public void setType(FieldType type) {
		this.type = type;
	}

	/**
	 * Gets the type.
	 *
	 * @author Alberto Borsetta
	 * @return the type
	 */
	public FieldType getType() {
		return type;
	}

	/**
	 * Sets the name.
	 *
	 * @author Alberto Borsetta
	 * @param name the new name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Gets the name.
	 *
	 * @author Alberto Borsetta
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Checks if is multiple.
	 *
	 * @author Alberto Borsetta
	 * @return true, if is multiple
	 */
	public boolean isMultiple() {
		return multiple;
	}

	/**
	 * Sets the multiple.
	 *
	 * @author Alberto Borsetta
	 * @param multiple the new multiple
	 */
	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	/**
	 * Gets the values.
	 *
	 * @author Alberto Borsetta
	 * @return the values
	 */
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

	/**
	 * Gets the xml.
	 *
	 * @author Alberto Borsetta
	 * @param doc the doc
	 * @return the xml
	 */
	public Element getXml(Document doc) {
		Element fieldElement = doc.createElement("field");
		fieldElement.setAttribute("orientation", type.name());
		fieldElement.setAttribute("multiple", String.valueOf(multiple));
		fieldElement.setAttribute("rejectMultiple", String.valueOf(rejectMultiple));
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

	/**
	 * Sets the reject multiple.
	 *
	 * @author Alberto Borsetta
	 * @param rejectMultiple the new reject multiple
	 */
	public void setRejectMultiple(boolean rejectMultiple) {
		this.rejectMultiple = rejectMultiple;
	}
	
	/**
	 * Reject multiple.
	 *
	 * @author Alberto Borsetta
	 * @return true, if successful
	 */
	public boolean rejectMultiple() {
		return rejectMultiple;
	}
}
