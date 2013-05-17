package org.albertoborsetta.formscanner.commons;

import ij.process.ImageProcessor;

public class FormPoint implements Comparable<FormPoint> {
	
	private int x;
	private int y;
	private double dist = 0;

	public FormPoint (int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public FormPoint (int x, int y, double dist) {
		this.x = x;
		this.y = y;
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
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x=x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y=y;
	}
	
	public void setDistance(double dist) {
		this.dist = dist;		
	}
	
	public double getDistance() {
		return dist;		
	}
	
	public void draw(ImageProcessor ip) {
		// draw this corner as a black cross in ip
		int paintvalue = 0; // black
		int size = 5;
		ip.setValue(paintvalue);
		ip.drawLine(x-size,y,x+size,y);
		ip.drawLine(x,y-size,x,y+size);
	}	

}
