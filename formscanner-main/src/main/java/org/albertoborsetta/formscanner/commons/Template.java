package org.albertoborsetta.formscanner.commons;

import java.util.HashMap;
import java.util.Map;

public class Template {
	
	private String name;
	private Map<String, Field> fields;

	public Template(String name, HashMap<String, Field> fields) {
		this.setName(name);
		this.fields = fields;
	}
	
	public Template(String name) {
		this.setName(name);
		this.fields = new HashMap<String, Field>();
	}

	public Field getField(String name) {
		return fields.get(name);
	}

	public void setField(String name, Field field) {
		fields.put(name, field);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
