package com.albertoborsetta.formscanner.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.albertoborsetta.formscanner.api.commons.Constants;

/**
 * The <code>FormQuestion</code> class represents a question field into the
 * scanned form.
 * <p>
 * A FormQuestion object has this attributes:
 * <ul>
 * <li>A set of points which indicates the position of the responses
 * <li>Can be a multiple choice field
 * <li>Can be rejected if the response is multiple (default <code>false</code>
 * </ul>
 *
 * @author Alberto Borsetta
 * @version 1.1.2
 * @see FormField
 * @see FormPoint
 */
public class FormQuestion extends FormField {

	private boolean multiple;
	private final HashMap<String, FormPoint> points;
	private boolean rejectMultiple = false;

	/**
	 * Instantiates a new FormQuestion with the responses points.
	 *
	 * @author Alberto Borsetta
	 * @param name
	 *            the name of the field
	 * @param points
	 *            the points which indicates the position of the responses
	 * @see FormPoint
	 */
	public FormQuestion(String name, HashMap<String, FormPoint> points) {
		super(name);
		this.points = points;
	}

	/**
	 * Instantiates a new empty FormQuestion. Initialize an empty set of
	 * response points.
	 *
	 * @author Alberto Borsetta
	 * @param name
	 *            the name of the field
	 * @see FormPoint
	 */
	public FormQuestion(String name) {
		super(name);
		points = new HashMap<>();
	}

	/**
	 * Sets a point of a single response.
	 *
	 * @author Alberto Borsetta
	 * @param value
	 *            the value of the response
	 * @param point
	 *            the point of the response
	 * @see FormPoint
	 */
	public void setPoint(String value, FormPoint point) {
		points.put(value, point);
	}

	/**
	 * Returns a point of a single response.
	 *
	 * @author Alberto Borsetta
	 * @param value
	 *            the value of the response
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
	 * Returns the response points of a FormQuestion object.
	 *
	 * @author Alberto Borsetta
	 * @return the response points
	 * @see FormPoint
	 */
	public HashMap<String, FormPoint> getPoints() {
		return points;
	}

	/**
	 * Checks if it is a multiple choice FormQuestion object.
	 *
	 * @author Alberto Borsetta
	 * @return <code>true</code>, if it is a multiple choice FormField object
	 */
	public boolean isMultiple() {
		return multiple;
	}

	/**
	 * Sets if it is a multiple choice FormQuestion object.
	 *
	 * @author Alberto Borsetta
	 * @param multiple
	 *            <code>true</code> if it is a multiple choice FormField object
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
		ArrayList<String> results = new ArrayList<>(points.keySet());
		Collections.sort(results);
		StringBuilder ret = new StringBuilder();
		for (String result : results) {
			if (StringUtils.isEmpty(ret.toString())) {
				ret.append(result);
			} else {
				ret.append("|").append(result);
			}
		}
		return ret.toString();
	}

	/**
	 * Returns the xml representation of the FormQuestion object.
	 *
	 * @author Alberto Borsetta
	 * @param doc
	 *            the parent document
	 * @return the xml representation of the FormQuestion object
	 */
	@Override
	public Element getXml(Document doc) {
		Element questionElement = doc.createElement("question");

		questionElement.setAttribute("type", type.name());
		questionElement.setAttribute("question", StringUtils.trim(name));
		questionElement.setAttribute("multiple", String.valueOf(multiple));
		questionElement.setAttribute("rejectMultiple", String.valueOf(rejectMultiple));

		// values element
		Element valuesElement = doc.createElement("values");
		questionElement.appendChild(valuesElement);

		// value elements
		for (Entry<String, FormPoint> point : points.entrySet()) {
			if (point.getValue() != Constants.EMPTY_POINT) {
				Element valueElement = doc.createElement("value");
				valueElement.setAttribute("response", point.getKey());
				valueElement.appendChild(point.getValue().getXml(doc));
				valuesElement.appendChild(valueElement);
			}
		}

		return questionElement;
	}

	/**
	 * Sets if the FormQuestion object has to be rejected in case of multiple
	 * responses.
	 *
	 * @author Alberto Borsetta
	 * @param rejectMultiple
	 *            <code>true</code> if the FormQuestion object has to be
	 *            rejected in case of multiple responses
	 */
	public void setRejectMultiple(boolean rejectMultiple) {
		this.rejectMultiple = rejectMultiple;
	}

	/**
	 * Returns if the FormQuestion object has to be rejected in case of multiple
	 * responses.
	 *
	 * @author Alberto Borsetta
	 * @return <code>true</code>, if the FormQuestion object has to be rejected
	 *         in case of multiple responses
	 */
	public boolean rejectMultiple() {
		return rejectMultiple;
	}
}
