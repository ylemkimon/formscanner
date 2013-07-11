package org.albertoborsetta.formscanner.commons;

import java.awt.Point;
import java.text.NumberFormat;
import java.text.ParseException;

import org.apache.commons.lang3.StringUtils;

public class FormPoint extends Point implements Comparable<FormPoint>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private double dist = 0;
	
	public FormPoint(Point p) {
		super();
		setLocation(p.getX(), p.getY());
	}
	
	public FormPoint (double x, double y) {
		super();
		setLocation(x, y);
	}
	
	public FormPoint (int x, int y, double dist) {
		super();
		setLocation(x, y);
		this.dist = dist;
	}

	public int compareTo(FormPoint c2) { 
		if (dist < c2.getDistance()) return -1;
		if (dist > c2.getDistance()) return 1;
		else return 0;
	}

	public double dist2 (FormPoint c2) {
		double dx = this.getX() - c2.getX();
		double dy = this.getY() - c2.getY();
		return (dx*dx)+(dy*dy);
	}
	
	public double dist2 (double x2, double y2) {
		double dx = this.getX() - x2;
		double dy = this.getY() - y2;
		return (dx*dx)+(dy*dy);
	}

	public void setDistance(double dist) {
		this.dist = dist;		
	}
	
	public double getDistance() {
		return dist;		
	}
	
	public String toString() {
		return "["+x+", "+y+"]";
	}
	
	public static FormPoint toPoint(String str) {
		try {
			String vals = StringUtils.remove(str, '[');
			vals = StringUtils.remove(vals, ']');
			String[] coords = StringUtils.split(vals, ',');
			
			NumberFormat nf = NumberFormat.getInstance();
			Number a = nf.parse(coords[0]);
			Number b = nf.parse(coords[1]);
			
			double x = a.doubleValue();
			double y = b.doubleValue();
			
			return new FormPoint(x, y);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void rotate(double alfa) {
		double x = ((getX() * Math.cos(alfa)) - (getY() * Math.sin(alfa)));
		double y = ((getX() * Math.sin(alfa)) + (getY() * Math.cos(alfa)));
		setLocation(x, y);
	}
	
	public void translate(double dx, double dy) {
		setLocation(getX()+dx, getY()+dy);
	}
	
	public void relativePositionTo(FormPoint orig, double alfa) {
		translate(0-orig.getX(), 0-orig.getY());
		rotate(alfa);
	}
	
	public void originalPositionFrom(FormPoint orig, double alfa) {
		rotate(0-alfa);
		translate(orig.getX(), orig.getY());
	}
}
