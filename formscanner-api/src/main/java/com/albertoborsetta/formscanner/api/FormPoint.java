package com.albertoborsetta.formscanner.api;

import java.awt.Point;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The <code>FormPoint</code> class represents a point into the scanned form.
 * <p>
 * A FormPoint object has only the (x,y) coordinates attributes
 *
 * @author Alberto Borsetta
 * @version 1.1.2
 * @see Point
 */
public class FormPoint {

	private double x;
	private double y;

	/**
	 * Instantiates a new FormPoint object with (x,y)=(0,0).
	 *
	 * @author Alberto Borsetta
	 */
	public FormPoint() {
		this(0, 0);
	}

	/**
	 * Instantiates a new FormPoint object from a <code>java.awt.Point</code>.
	 *
	 * @author Alberto Borsetta
	 * @param p the <code>java.awt.Point</code> object
	 * @see Point
	 */
	public FormPoint(Point p) {
		this(p.getX(), p.getY());
	}

	/**
	 * Instantiates a new FormPoint object with given (x,y) coordinates.
	 *
	 * @author Alberto Borsetta
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public FormPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the quadratic distance to the given FormPoint object.
	 *
	 * @author Alberto Borsetta
	 * @param c2 the point to calculate the distance from
	 * @return the calculated quadratic distance
	 */
	public double dist2(FormPoint c2) {
		double dx = x - c2.getX();
		double dy = y - c2.getY();
		return (dx * dx) + (dy * dy);
	}

	/**
	 * Returns the quadratic distance to the given (x,y) coordinates.
	 *
	 * @author Alberto Borsetta
	 * @param x2 the x coordinate to calculate the distance from
	 * @param y2 the y coordinate to calculate the distance from
	 * @return the calculated quadratic distance
	 */
	public double dist2(double x2, double y2) {
		double dx = x - x2;
		double dy = y - y2;
		return (dx * dx) + (dy * dy);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[").append((int) getX()).append(",").append((int) getY()).append("]");
		return sb.toString();
	}

	/**
	 * Retruns the FormPoint object from the string representation of it.
	 *
	 * @author Alberto Borsetta
	 * @param str the string representation of a FormPoint object
	 * @return the FormPoint object
	 * @throws ParseException throws ParseExcepion
	 */
	public static FormPoint toPoint(String str) throws ParseException {
		String vals = StringUtils.remove(str, '[');
		vals = StringUtils.remove(vals, ']');
		String[] coords = StringUtils.split(vals, ',');

		DecimalFormat formatter = (DecimalFormat) DecimalFormat.getInstance();
		DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
		decimalFormatSymbols.setDecimalSeparator('.');
		formatter.setDecimalFormatSymbols(decimalFormatSymbols);

		return new FormPoint(formatter.parse(coords[0]).doubleValue(), formatter.parse(coords[1]).doubleValue());
	}

	private void rotate(double alfa) {
		x = ((x * Math.cos(alfa)) - (y * Math.sin(alfa)));
		y = ((x * Math.sin(alfa)) + (y * Math.cos(alfa)));
	}

	private void translate(double dx, double dy) {
		x = x + dx;
		y = y + dy;
	}

	/**
	 * Calculate the rescaled position according to the given scaleFactor.
	 *
	 * @author Alberto Borsetta
	 * @param scaleFactor the scale factor
	 */
	public void scale(double scaleFactor) {
		x = (scaleFactor * x);
		y = (scaleFactor * y);
	}

	/**
	 * Roto-translate the point using the given origin and angle. The direction
	 * indicates:
	 * <ul>
	 * <li><code>true</code> - calculate the relative position to the given
	 * origin FormPoint object
	 * <li><code>false</code> - calculate the original position from the given
	 * origin FormPoint object
	 * </ul>
	 *
	 * @author Alberto Borsetta
	 * @param o the origin FormPoint object
	 * @param alfa the angle (in radiants)
	 * @param direct the direction
	 */
	public void rotoTranslate(FormPoint o, double alfa, boolean direct) {
		if (direct) {
			relativePositionTo(o, alfa);
		} else {
			originalPositionFrom(o, alfa);
		}
	}

	private void relativePositionTo(FormPoint o, double alfa) {
		translate(0 - o.getX(), 0 - o.getY());
		rotate(alfa);
	}

	private void originalPositionFrom(FormPoint o, double alfa) {
		rotate(0 - alfa);
		translate(o.getX(), o.getY());
	}

	/**
	 * Sets the (x,y) coordinates of the FormPoint object.
	 *
	 * @author Alberto Borsetta
	 * @param x the x coordinate
	 * @param y the y coordinate
	 */
	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Returns the x coordinate.
	 *
	 * @author Alberto Borsetta
	 * @return the x coordinate
	 */
	public double getX() {
		return x;
	}

	/**
	 * Sets the x coordinate.
	 *
	 * @author Alberto Borsetta
	 * @param x the new x coordinate
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Returns the y coordinate.
	 *
	 * @author Alberto Borsetta
	 * @return the y coordinate
	 */
	public double getY() {
		return y;
	}

	/**
	 * Sets the y coordinate.
	 *
	 * @author Alberto Borsetta
	 * @param y the new y coordinate
	 */
	public void setY(double y) {
		this.y = y;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */
	@Override
	public FormPoint clone() {
		return new FormPoint(x, y);
	}

	/**
	 * Returns the xml representation of the FormPoint object.
	 *
	 * @author Alberto Borsetta
	 * @param doc the parent document
	 * @return the xml representation of the FormPoint object
	 */
	public Element getXml(Document doc) {
		Element pointElement = doc.createElement("point");
		pointElement.setAttribute("x", String.valueOf(x));
		pointElement.setAttribute("y", String.valueOf(y));
		return pointElement;
	}
}
