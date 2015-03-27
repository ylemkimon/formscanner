package com.albertoborsetta.formscanner.api;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map.Entry;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.albertoborsetta.formscanner.api.commons.Constants.Corners;
import com.albertoborsetta.formscanner.api.commons.Constants.FieldType;

/**
 * The <code>FormArea</code> class represents an area (like barcode area) into the scanned form.<p>
 * A FormArea object has the four corners points attributes
 * 
 * @author Alberto Borsetta
 * @version 0.11-SNAPSHOT
 * @see FormPoint
 * @see Corners
 */
public class FormArea {
	
	private String name;
	private FieldType type;
	private HashMap<Corners, FormPoint> area;

	/**
	 * Instantiates a new FormArea with the corner points.
	 *
	 * @author Alberto Borsetta
	 * @param name the name of the area
	 * @param points the points which indicates the position of the corners
	 * @see FormPoint
	 * @see Corners
	 */
	public FormArea(String name, HashMap<Corners, FormPoint> area) {
		this.name = name;
		this.area = area;
	}
	
	/**
	 * Instantiates a new empty FormArea. Initialize an empty set of corners points.
	 *
	 * @author Alberto Borsetta
	 * @param name the name of the area
	 * @see FormPoint
	 * @see Corners
	 */
	public FormArea(String name) {
		this.name = name;
		area = new HashMap<Corners, FormPoint>();
	}

	/**
	 * Sets a corner of the FormArea object.
	 *
	 * @author Alberto Borsetta
	 * @param corner the corner to set
	 * @param point the point of the corner
	 * @see FormPoint
	 * @see Corners
	 */
	public void setPoint(Corners corner, FormPoint point) {
		area.put(corner, point);
	}
	
	/**
	 * Returns the point of a corner.
	 *
	 * @author Alberto Borsetta
	 * @param corner the corner of the area
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
	 * Returns the area indentified by the FormArea object.
	 *
	 * @author Alberto Borsetta
	 * @return the area
	 * @see FormPoint
	 * @see Corners
	 */
	public HashMap<Corners, FormPoint> getArea() {
		return area;
	}
	
	/**
	 * Sets the FormArea object type.
	 *
	 * @author Alberto Borsetta
	 * @param type the new area type
	 * @see FieldType
	 */
	public void setType(FieldType type) {
		this.type = type;
	}
	

	/**
	 * Returns the FormArea object type.
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
	 * Returns the xml representation of the FormArea object.
	 *
	 * @author Alberto Borsetta
	 * @param doc the parent document
	 * @return the xml representation of the FormArea object
	 */
	public Element getXml(Document doc) {
		Element pointElement = doc.createElement("point");
		pointElement.setAttribute("x", String.valueOf(x));
		pointElement.setAttribute("y", String.valueOf(y));
		return pointElement;
	}
}
