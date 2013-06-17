package org.albertoborsetta.formscanner.commons;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;

import org.albertoborsetta.formscanner.commons.FormScannerConstants.Corners;
import org.albertoborsetta.formscanner.imageparser.ScanImage;

public class FormTemplate {
	
	private File image;
	private HashMap<String, FormField> fields;
	private HashMap<Corners, FormPoint> corners;
	private double rotation;
	private ArrayList<FormPoint> pointList;
	private ArrayList<String> fieldsName;
	
	public FormTemplate(File image) {
		this.image = image;
		
		ScanImage imageScan = new ScanImage(image);
        corners = imageScan.locateCorners();
        
        rotation = calculateRotation();        
        fields = new HashMap<String, FormField>();        
        fieldsName = new ArrayList<String>();
		pointList = new ArrayList<FormPoint>();
	}
	
	public String getName() {
		return image.getName();
	}

	public FormField getField(String name) {
		return fields.get(name);
	}
	
	public HashMap<String, FormField> getFields() {
		return fields;
	}

	public void setField(String name, FormField field) {
		fields.put(name, field);		
		fieldsName.add(name);
		for (Entry<String, FormPoint> point : field.getPoints().entrySet()) {
			pointList.add(point.getValue());
		}
	}
	
	public void setFields(HashMap<String, FormField> fields) {
		for (Entry<String, FormField> field : fields.entrySet()) {
			setField(field.getKey(), field.getValue());
			fieldsName.add(field.getKey());
			
			for (Entry<String, FormPoint> point : field.getValue().getPoints().entrySet()) {
				pointList.add(point.getValue());
			}
        }
	}
	
	public ArrayList<String> getFieldsName() {
		return fieldsName;
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
	
	public ArrayList<FormPoint> getFieldPoints() {
		return pointList;
	}
	
	private double calculateRotation() {
		FormPoint topLeftPoint = corners.get(Corners.TOP_LEFT);
		FormPoint topRightPoint = corners.get(Corners.TOP_RIGHT);
		
		double dx = (double) (topRightPoint.getX() - topLeftPoint.getX());
		double dy = (double) (topLeftPoint.getY() - topRightPoint.getY());
		
		return Math.atan(dy/dx);
	}
	
	public File getImage() {
		return image;
	}
}
