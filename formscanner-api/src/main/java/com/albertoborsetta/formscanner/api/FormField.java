package com.albertoborsetta.formscanner.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.albertoborsetta.formscanner.api.FormPoint;
import com.albertoborsetta.formscanner.api.commons.Constants.FieldType;

/**
 * The <code>FormField</code> class represents a field into the scanned form.<p>
 * A FormField object has this attributes:
 * <ul>
 * <li>A name
 * <li>A specific type
 * <li>A set of points which indicates the position of the responses
 * <li>Can be a multiple choice field
 * <li>Can be rejected if the response is multiple (default <code>false</code>
 * </ul>
 * 
 * @author Alberto Borsetta
 * @version 0.10.1-SNAPSHOT
 * @see FormPoint
 * @see FieldType
 */
public class FormField {
	
	private String name;
	private FieldType type;
	private boolean multiple;
	private HashMap<String, FormPoint> points;
	private boolean rejectMultiple = false;

	/**
	 * Instantiates a new FormField with the responses points.
	 *
	 * @author Alberto Borsetta
	 * @param name the name of the field
	 * @param points the points which indicates the position of the responses
	 * @see FormPoint
	 */
	public FormField(String name, HashMap<String, FormPoint> points) {
		this.name = name;
		this.points = points;
	}

	/**
	 * Instantiates a new empty FormField. Initialize an empty set of response points.
	 *
	 * @author Alberto Borsetta
	 * @param name the name of the field
	 * @see FormPoint
	 */
	public FormField(String name) {
		this.name = name;
		points = new HashMap<String, FormPoint>();
	}

	/**
	 * Sets a point of a single response.
	 *
	 * @author Alberto Borsetta
	 * @param value the value of the response
	 * @param point the point of the response
	 * @see FormPoint
	 */
	public void setPoint(String value, FormPoint point) {
		points.put(value, point);
	}

	/**
	 * Returns a point of a single response.
	 *
	 * @author Alberto Borsetta
	 * @param value the value of the response
	 * @return the point of the response
	 */
	public FormPoint getPoint(String value) {
		return points.get(value);
	}
	
	/**
	 * Clear all points of a single response.
	 *
	 * @author Alberto Borsetta
	 */
	public void clearPoints() {
		points.clear();
	}

	/**
	 * Returns the response points of a FormField object.
	 *
	 * @author Alberto Borsetta
	 * @return the response points
	 * @see FormPoint
	 */
	public HashMap<String, FormPoint> getPoints() {
		return points;
	}

	/**
	 * Sets the FormField object type.
	 *
	 * @author Alberto Borsetta
	 * @param type the new field type
	 * @see FieldType
	 */
	public void setType(FieldType type) {
		this.type = type;
	}

	/**
	 * Returns the FormField object type.
	 *
	 * @author Alberto Borsetta
	 * @return the field type
	 * @see FieldType
	 */
	public FieldType getType() {
		return type;
	}

	/**
	 * Sets the FormField object name.
	 *
	 * @author Alberto Borsetta
	 * @param name the new field name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the FormField object name.
	 *
	 * @author Alberto Borsetta
	 * @return the field name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Checks if it is a multiple choice FormField object.
	 *
	 * @author Alberto Borsetta
	 * @return <code>true</code>, if it is a multiple choice FormField object 
	 */
	public boolean isMultiple() {
		return multiple;
	}

	/**
	 * Sets if it is a multiple choice FormField object.
	 *
	 * @author Alberto Borsetta
	 * @param multiple <code>true</code> if it is a multiple choice FormField object
	 */
	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}

	/**
	 * Returns the values of the reponses.
	 *
	 * @author Alberto Borsetta
	 * @return the values of the responses
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
	 * Returns the xml representation of the FormField object.
	 *
	 * @author Alberto Borsetta
	 * @param doc the parent document
	 * @return the xml representation of the FormField object
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
	 * Sets if the FormField object has to be rejected in case of multiple responses.
	 *
	 * @author Alberto Borsetta
	 * @param rejectMultiple <code>true</code> if the FormField object has to be rejected in case of multiple responses
	 */
	public void setRejectMultiple(boolean rejectMultiple) {
		this.rejectMultiple = rejectMultiple;
	}
	
	/**
	 * Returns if the FormField object has to be rejected in case of multiple responses.
	 *
	 * @author Alberto Borsetta
	 * @return <code>true</code>, if the FormField object has to be rejected in case of multiple responses
	 */
	public boolean rejectMultiple() {
		return rejectMultiple;
	}
}
