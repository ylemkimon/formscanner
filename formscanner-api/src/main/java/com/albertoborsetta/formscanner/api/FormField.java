package com.albertoborsetta.formscanner.api;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.albertoborsetta.formscanner.api.commons.Constants.FieldType;

/**
 * The <code>FormField</code> class represents a field into the scanned form.
 * <p>
 * A FormField object has this attributes:
 * <ul>
 * <li>A name
 * <li>A specific type
 * <li>A group
 * </ul>
 *
 * @author Alberto Borsetta
 * @version 1.0.2
 * @see FieldType
 */
public abstract class FormField {

	protected String name;
	protected FieldType type;

	/**
	 * Instantiates an empty FormField.
	 *
	 * @author Alberto Borsetta
	 */
	public FormField() {
	}

	/**
	 * Instantiates a new FormField with the responses points.
	 *
	 * @author Alberto Borsetta
	 * @param name
	 *            the name of the field
	 */
	public FormField(String name) {
		this.name = name;
	}

	/**
	 * Instantiates a new FormField with the responses points.
	 *
	 * @author Alberto Borsetta
	 * @param name
	 *            the name of the field
	 * @param type
	 *            the type of the field
	 */
	public FormField(String name, FieldType type) {
		this.name = name;
		this.type = type;
	}

	/**
	 * Sets the FormField object type.
	 *
	 * @author Alberto Borsetta
	 * @param type
	 *            the new field type
	 */
	public void setType(FieldType type) {
		this.type = type;
	}

	/**
	 * Returns the FormField object type.
	 *
	 * @author Alberto Borsetta
	 * @return the field type
	 */
	public FieldType getType() {
		return type;
	}

	/**
	 * Sets the FormField object name.
	 *
	 * @author Alberto Borsetta
	 * @param name
	 *            the new field name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the FormField object name.
	 *
	 * @author Alberto Borsetta
	 * @return the field name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns the xml representation of the FormField object.
	 *
	 * @author Alberto Borsetta
	 * @param doc
	 *            the parent document
	 * @return the xml representation of the FormQuestion object
	 */
	public Element getXml(Document doc) {
		Element fieldElement = doc.createElement("field");

		fieldElement.setAttribute("type", type.name());
		fieldElement.setAttribute("name", name);

		return fieldElement;
	}
}
