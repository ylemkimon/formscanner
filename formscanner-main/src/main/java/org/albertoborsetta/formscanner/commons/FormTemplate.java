package org.albertoborsetta.formscanner.commons;

import java.util.HashMap;
import java.util.Map;

public class FormTemplate {
	
	private String name;
	private Map<String, FormField> fields;

	public FormTemplate(String name, HashMap<String, FormField> fields) {
		this.setName(name);
		this.fields = fields;
	}
	
	public FormTemplate(String name) {
		this.setName(name);
		this.fields = new HashMap<String, FormField>();
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
}
