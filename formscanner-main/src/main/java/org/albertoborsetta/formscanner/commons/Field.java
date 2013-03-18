package org.albertoborsetta.formscanner.commons;

import java.util.HashMap;
import java.util.Map;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.FieldType;

public class Field {
    private String name;
    private FieldType type;
    private boolean multiple;
    private Map<String, Point> points;
    
    public Field(String name, HashMap<String, Point> points) {
    		this.name = name;
    		this.points = points;
    }
    
    public Field(String name) {
		this.name = name;
		points = new HashMap<String, Point>();
    }
    
    public void setPoint(String value, Point point) {
    	points.put(value, point);
    }
    
    public Point getPoint(String value) {
    	return points.get(value);
    }
    
    public void setType(FieldType type) {
    	this.type = type;
    }
    
    public FieldType getType() {
        return type;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
    
    public String getName() {
        return name;
    }

	public boolean isMultiple() {
		return multiple;
	}

	public void setMultiple(boolean multiple) {
		this.multiple = multiple;
	}
}
