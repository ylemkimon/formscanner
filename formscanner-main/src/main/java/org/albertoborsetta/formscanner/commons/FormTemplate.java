package org.albertoborsetta.formscanner.commons;

import java.util.HashMap;

import org.albertoborsetta.formscanner.commons.FormScannerConstants.Corners;

public class FormTemplate {
	
	private String name;
	private HashMap<String, FormField> fields;
	private HashMap<Corners, FormPoint> corners;
	private double rotation;
	
	public FormTemplate(String name) {
		this.setName(name);
		fields = new HashMap<String, FormField>();
		corners = new HashMap<Corners, FormPoint>();
		rotation = 0;
	}

	public FormField getField(String name) {
		return fields.get(name);
	}

	public void setField(String name, FormField field) {
		fields.put(name, field);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void setFields() {
		
	}

	public void setCorners(HashMap<Corners, FormPoint> corners) {
		this.corners = corners;
	}

	public HashMap<Corners, FormPoint> getCorners() {
		return corners;
	}

	public double getRotation() {
		return rotation;
	}

	public void setRotation(double rotation) {
		this.rotation = rotation;
	}
}
