package com.albertoborsetta.formscanner.api;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map.Entry;

import org.w3c.dom.Element;

import com.albertoborsetta.formscanner.api.commons.Constants.Corners;

/**
 * The <code>FormArea</code> class represents an area (like barcode area) into the scanned form.<p>
 * A FormArea object has the four corners points attributes
 * 
 * @author Alberto Borsetta
 * @version 0.11-SNAPSHOT
 * @see FormPoint
 */
public class FormArea {
	
	private HashMap<Corners, FormPoint> corners;

	/**
	 * Instantiates a new FormArea object with corners at (0,0) coordinates.
	 * 
	 * @author Alberto Borsetta
	 */
	public FormArea() {
		corners = new HashMap<Corners, FormPoint>();
		for (Corners corner : Corners.values()) {
			corners.put(corner, new FormPoint());
		}
	}

	/**
	 * Instantiates a new FormPoint object from four FormPoint.
	 *
	 * @author Alberto Borsetta
	 * @param topLeftPoint the point of the top left corner of the area
	 * @param topRightPoint the point of the top right corner of the area
	 * @param bottomLeftPoint the point of the bottom left corner of the area
	 * @param bottomRightPoint the point of the bottom right corner of the area
	 * @see FormPoint
	 */
	public FormArea(FormPoint topLeftPoint, FormPoint topRightPoint, FormPoint bottomLeftPoint, FormPoint bottomRightPoint) {
		topLeftCorner = topLeftPoint;
		topRightCorner = topRightPoint;
		bottomLeftCorner = bottomLeftPoint;
		bottomRightCorner = bottomRightPoint;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "[" + (int) getX() + "," + (int) getY() + "]";
	}
}
