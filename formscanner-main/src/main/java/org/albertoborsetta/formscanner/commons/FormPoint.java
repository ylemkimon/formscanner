package org.albertoborsetta.formscanner.commons;

import java.awt.Point;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class FormPoint {

	private double x;
	private double y;

	public FormPoint() {
		this(0, 0);
	}
	
	public FormPoint(Point p) {
		this(p.getX(), p.getY());
	}
	
	public FormPoint(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double dist2(FormPoint c2) {
		double dx = x - c2.getX();
		double dy = y - c2.getY();
		return (dx * dx) + (dy * dy);
	}

	public double dist2(double x2, double y2) {
		double dx = x - x2;
		double dy = y - y2;
		return (dx * dx) + (dy * dy);
	}

	public String toString() {
		return "[" + (int) getX() + "," + (int) getY() + "]";
	}

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

	protected void rotate(double alfa) {
		x = ((x * Math.cos(alfa)) - (y * Math.sin(alfa)));
		y = ((x * Math.sin(alfa)) + (y * Math.cos(alfa)));
	}

	private void translate(double dx, double dy) {
		x = x + dx;
		y = y + dy;
	}

	public void scale(double scaleFactor) {
		x = (scaleFactor * x);
		y = (scaleFactor * y);
	}

	public void relativePositionTo(FormPoint o, double alfa) {
		translate(0 - o.getX(), 0 - o.getY());
		rotate(alfa);
	}

	public void originalPositionFrom(FormPoint o, double alfa) {
		rotate(0 - alfa);
		translate(o.getX(), o.getY());
	}

	public void setLocation(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public FormPoint clone() {
		return new FormPoint(x, y);
	}

	public Element getXml(Document doc) {
		Element pointElement = doc.createElement("point");
		pointElement.setAttribute("x", String.valueOf(x));
		pointElement.setAttribute("y", String.valueOf(y));
		return pointElement;
	}
}
