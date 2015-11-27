package com.albertoborsetta.formscanner.api;

import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.albertoborsetta.formscanner.api.commons.Constants.Corners;

/**
 * The <code>FormArea</code> class represents an area (like barcode area) into
 * the scanned form.
 * <p>
 * A FormArea object has the four corners points attributes
 * <ul>
 * <li>A set of corners that identify the area
 * <li>A text content
 * </ul>
 *
 * @author Alberto Borsetta
 * @version 1.1.2-SNAPSHOT
 * @see FormPoint
 * @see Corners
 */
public class FormArea extends FormField {

	private final HashMap<Corners, FormPoint> area;
	private String text;

	/**
	 * Instantiates a new Empty FormArea.
	 *
	 * @author Alberto Borsetta
	 */
	public FormArea() {
		super();
		area = new HashMap<>();
	}

	/**
	 * Instantiates a new FormArea with the corner points.
	 *
	 * @author Alberto Borsetta
	 * @param name
	 *            the name of the area
	 * @param area
	 *            the points which indicates the position of the corners
	 * @see FormPoint
	 * @see Corners
	 */
	public FormArea(String name, HashMap<Corners, FormPoint> area) {
		super(name);
		this.area = area;
	}

	/**
	 * Instantiates a new empty FormArea. Initialize an empty set of corners
	 * points.
	 *
	 * @author Alberto Borsetta
	 * @param name
	 *            the name of the area
	 * @see FormPoint
	 * @see Corners
	 */
	public FormArea(String name) {
		super(name);
		area = new HashMap<>();
	}

	/**
	 * Sets a corner of the FormArea object.
	 *
	 * @author Alberto Borsetta
	 * @param corner
	 *            the corner to set
	 * @param point
	 *            the point of the corner
	 * @see FormPoint
	 * @see Corners
	 */
	public void setCorner(Corners corner, FormPoint point) {
		area.put(corner, point);
	}

	/**
	 * Returns the point of a corner.
	 *
	 * @author Alberto Borsetta
	 * @param corner
	 *            the corner of the area
	 * @return the point of the corner
	 */
	public FormPoint getCorner(Corners corner) {
		return area.get(corner);
	}

	/**
	 * Clear all corners of the FormArea object.
	 *
	 * @author Alberto Borsetta
	 */
	public void clearCorners() {
		area.clear();
	}

	/**
	 * Returns the corners of the FormArea object.
	 *
	 * @author Alberto Borsetta
	 * @return the corners
	 * @see FormPoint
	 * @see Corners
	 */
	public HashMap<Corners, FormPoint> getCorners() {
		return area;
	}

	/**
	 * Returns the xml representation of the FormArea object.
	 *
	 * @author Alberto Borsetta
	 * @param doc
	 *            the parent document
	 * @return the xml representation of the FormArea object
	 */
	@Override
	public Element getXml(Document doc) {
		Element areaElement = doc.createElement("area");

		areaElement.setAttribute("type", type.name());
		areaElement.setAttribute("name", StringUtils.trim(name));

		// corners element
		Element cornersElement = doc.createElement("corners");
		areaElement.appendChild(cornersElement);

		// corner elements
		for (Entry<Corners, FormPoint> corner : area.entrySet()) {
			Element cornerElement = doc.createElement("corner");
			cornerElement.setAttribute("position", corner.getKey().getName());
			cornerElement.appendChild(corner.getValue().getXml(doc));
			cornersElement.appendChild(cornerElement);
		}

		return areaElement;
	}

	/**
	 * Sets the FormArea object text.
	 *
	 * @author Alberto Borsetta
	 * @param text
	 *            the text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Returns the FormArea text.
	 *
	 * @author Alberto Borsetta
	 * @return the text
	 */
	public String getText() {
		return text;
	}
}
