package org.albertoborsetta.formscanner.commons;

import java.awt.Point;

import org.apache.commons.lang3.StringUtils;

public class FormPoint extends Point implements Comparable<FormPoint>{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private double dist = 0;
	
	public FormPoint (Point p) {
		super(p);
	}

	public FormPoint (int x, int y) {
		super(x, y);
	}
	
	public FormPoint (int x, int y, double dist) {
		super(x, y);
		this.dist = dist;
	}

	public int compareTo(FormPoint c2) { 
		if (dist < c2.getDistance()) return -1;
		if (dist > c2.getDistance()) return 1;
		else return 0;
	}

	public double dist2 (FormPoint c2) {
		int dx = this.x - c2.x;
		int dy = this.y - c2.y;
		return (dx*dx)+(dy*dy);
	}
	
	public double dist2 (int x2, int y2) {
		int dx = this.x - x2;
		int dy = this.y - y2;
		return (dx*dx)+(dy*dy);
	}

	public void setX(int x) {
		super.x = x;
	}

	public void setY(int y) {
		super.y=y;
	}
	
	public void setDistance(double dist) {
		this.dist = dist;		
	}
	
	public double getDistance() {
		return dist;		
	}
	
	public String toString() {
		return "["+x+","+y+"]";
	}
	
	public static FormPoint toPoint(String str) {
		String vals = StringUtils.remove(str, '[');
		vals = StringUtils.remove(vals, ']');
		String[] coords = StringUtils.split(vals, ',');
		
		int x = Integer.parseInt(coords[0]);
		int y = Integer.parseInt(coords[1]);
		
		return new FormPoint(x, y);
	}
}
