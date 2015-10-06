package com.albertoborsetta.formscanner.api;

import java.util.HashMap;

/**
*
* @author Alberto Borsetta
* @version 1.0.4-SNAPSHOT
*/
public class FormGroup {

	private HashMap<String, FormQuestion> fields;
	private HashMap<String, FormArea> areas;
	private int lastFieldIndex;

	/**
	 * Instantiates a new Empty FormGroup object.
	 *
	 * @author Alberto Borsetta
	 */
	public FormGroup() {
		fields = new HashMap<>();
		areas = new HashMap<>();
		lastFieldIndex = 1;
	}

	/**
	 * Returns all the fields in the FormGroup object.
	 *
	 * @author Alberto Borsetta
	 * @return the fields of the FormGroup object
	 * @see FormQuestion
	 */
	public HashMap<String, FormQuestion> getFields() {
		return fields;
	}

	/**
	 * Sets the field with the given name.
	 *
	 * @author Alberto Borsetta
	 * @param name the name of the field
	 * @param field the field to set
	 * @see FormQuestion
	 */
	public void setField(String name, FormQuestion field) {
		if (fields.get(name) == null) lastFieldIndex++;
		fields.put(name, field);
	}

	/**
	 * Returns the areas of the FormGroup object.
	 *
	 * @author Alberto Borsetta
	 * @return the areas of the FormGroup object
	 * @see FormArea
	 */
	public HashMap<String, FormArea> getAreas() {
		return areas;
	}

	/**
	 * Sets an area in the FormGroup object.
	 *
	 * @author Alberto Borsetta
	 * @param areaName the name of the area to set
	 * @param area the area to set
	 */
	public void setArea(String areaName, FormArea area) {
		if (areas.get(areaName) == null) lastFieldIndex++;
		areas.put(areaName, area);
	}

	/**
	 * Clear the fields into a Group.
	 *
	 * @author Alberto Borsetta
	 */
	public void clearFields() {
		fields.clear();
		lastFieldIndex = 1;
	}

	/**
	 * Remove a named field from a Group
	 * 
	 * @author Alberto Borsetta
	 * @param fieldName the field name
	 */
	public void removeField(String fieldName) {
		fields.remove(fieldName);
		lastFieldIndex--;
	}

	/**
	 * Returns the last index of the FormGroup object.
	 *
	 * @author Alberto Borsetta
	 * @return the last index of the FormGroup object
	 */
	public int getLastFieldIndex() {
		return lastFieldIndex;
	}
}
