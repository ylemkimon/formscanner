package org.albertoborsetta.formscanner.api;

import java.awt.Point;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The Class FormPoint.
 */
public class FormPoint {

	/** The x. */
	private double x;
	
	/** The y. */
	private double y;

	/**
	 * Instantiates a new form point.
	 */
	public FormPoint() {
		this(0, 0);
	}
	
	/**
	 * Instantiates a new form point.
	 *
	 * @author Alberto Borsetta
	 * @param p the p
	 */
	public FormPoint(Point p) {
		this(p.getX(), p.getY());
	}
	
	/**
	 * Instantiates a new form point.
	 *
	 * @author Alberto Borsetta
	 * @param x the x
	 * @param y the y
	 */
	public FormPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Dist2.
	 *
	 * @author Alberto Borsetta
	 * @param c2 the c2
	 * @return the double
	 */
	public double dist2(FormPoint c2) {
		double dx = x - c2.getX();
		double dy = y - c2.getY();
		return (dx * dx) + (dy * dy);
	}

	/**
	 * Dist2.
	 *
	 * @author Alberto Borsetta
	 * @param x2 the x2
	 * @param y2 the y2
	 * @return the double
	 */
	public double dist2(double x2, double y2) {
		double dx = x - x2;
		double dy = y - y2;
		return (dx * dx) + (dy * dy);
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return "[" + (int) getX() + "," + (int) getY() + "]";
	}

	/**
	 * To point.
	 *
	 * @author Alberto Borsetta
	 * @param str the str
	 * @return the form point
	 */
	public static FormPoint toPoint(String str) {
		try {
			String vals = StringUtils.remove(str, '[');
			vals = StringUtils.remove(vals, ']');
			String[] coords = StringUtils.split(vals, ',');

			DecimalFormat formatter = (DecimalFormat) DecimalFormat
					.getInstance();
			DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
			decimalFormatSymbols.setDecimalSeparator('.');
			formatter.setDecimalFormatSymbols(decimalFormatSymbols);

			return new FormPoint(formatter.parse(coords[0]).doubleValue(), formatter.parse(coords[1]).doubleValue());
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Rotate.
	 *
	 * @author Alberto Borsetta
	 * @param alfa the alfa
	 */
	private void rotate(double alfa) {
		x = ((x * Math.cos(alfa)) - (y * Math.sin(alfa)));
		y = ((x * Math.sin(alfa)) + (y * Math.cos(alfa)));
	}

	/**
	 * Translate.
	 *
	 * @author Alberto Borsetta
	 * @param dx the dx
	 * @param dy the dy
	 */
	private void translate(double dx, double dy) {
		x = x + dx;
		y = y + dy;
	}

	/**
	 * Scale.
	 *
	 * @author Alberto Borsetta
	 * @param scaleFactor the scale factor
	 */
	public void scale(double scaleFactor) {
		x = (scaleFactor * x);
		y = (scaleFactor * y);
	}

	/**
	 * Relative position to.
	 *
	 * @author Alberto Borsetta
	 * @param o the o
	 * @param alfa the alfa
	 */
	public void relativePositionTo(FormPoint o, double alfa) {
		translate(0 - o.getX(), 0 - o.getY());
		rotate(alfa);
	}

	/**
	 * Original position from.
	 *
	 * @author Alberto Borsetta
	 * @param o the o
	 * @param alfa the alfa
	 */
	public void originalPositionFrom(FormPoint o, double alfa) {
		rotate(0 - alfa);
		translate(o.getX(), o.getY());
	}

	/**
	 * Sets the location.
	 *
	 * @author Alberto Borsetta
	 * @param x the x
	 * @param y the y
	 */
	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Gets the x.
	 *
	 * @author Alberto Borsetta
	 * @return the x
	 */
	public double getX() {
		return x;
	}

	/**
	 * Sets the x.
	 *
	 * @author Alberto Borsetta
	 * @param x the new x
	 */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Gets the y.
	 *
	 * @author Alberto Borsetta
	 * @return the y
	 */
	public double getY() {
		return y;
	}

	/**
	 * Sets the y.
	 *
	 * @author Alberto Borsetta
	 * @param y the new y
	 */
	public void setY(double y) {
		this.y = y;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#clone()
	 */
	public FormPoint clone() {
		return new FormPoint(x, y);
	}

	/**
	 * Gets the xml.
	 *
	 * @author Alberto Borsetta
	 * @param doc the doc
	 * @return the xml
	 */
	public Element getXml(Document doc) {
		Element pointElement = doc.createElement("point");
		pointElement.setAttribute("x", String.valueOf(x));
		pointElement.setAttribute("y", String.valueOf(y));
		return pointElement;
	}
}
