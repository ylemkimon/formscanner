package org.albertoborsetta.formscanner.api.commons;

import org.albertoborsetta.formscanner.api.commons.translation.TranslationKeys;

// TODO: Auto-generated Javadoc
/**
 * The Class Constants.
 */
public class Constants {

	// Corners
	/** The Constant TOP_LEFT. */
	public static final String TOP_LEFT = "TOP_LEFT";
	
	/** The Constant TOP_RIGHT. */
	public static final String TOP_RIGHT = "TOP_RIGHT";
	
	/** The Constant BOTTOM_LEFT. */
	public static final String BOTTOM_LEFT = "BOTTOM_LEFT";
	
	/** The Constant BOTTOM_RIGHT. */
	public static final String BOTTOM_RIGHT = "BOTTOM_RIGHT";
	
	// GridType
	/** The Constant QUESTIONS_BY_ROWS. */
	public static final String QUESTIONS_BY_ROWS = "QUESTIONS_BY_ROWS";
	
	/** The Constant QUESTIONS_BY_COLS. */
	public static final String QUESTIONS_BY_COLS = "QUESTIONS_BY_COLS";

	/**
	 * The Enum Corners.
	 * 
	 * @author Alberto Borsetta
	 */
	public enum Corners {
		
		/** The top left. */
		TOP_LEFT(Constants.TOP_LEFT, TranslationKeys.TOP_LEFT_CORNER), 
		
		/** The top right. */
		TOP_RIGHT(Constants.TOP_RIGHT, TranslationKeys.TOP_RIGHT_CORNER), 
		
		/** The bottom right. */
		BOTTOM_RIGHT(Constants.BOTTOM_RIGHT, TranslationKeys.BOTTOM_RIGHT_CORNER), 
		
		/** The bottom left. */
		BOTTOM_LEFT(Constants.BOTTOM_LEFT, TranslationKeys.BOTTOM_LEFT_CORNER);

		/** The name. */
		private String name;
		
		/** The value. */
		private String value;

		/**
		 * Instantiates a new corners.
		 *
		 * @author Alberto Borsetta
		 * @param name the name
		 * @param value the value
		 */
		Corners(String name, String value) {
			this.name = name;
			this.value = value;
		}

		/**
		 * Gets the name.
		 *
		 * @author Alberto Borsetta
		 * @return the name
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * Gets the value.
		 *
		 * @author Alberto Borsetta
		 * @return the value
		 */
		public String getValue() {
			return value;
		}
	}

	
	/**
	 * The Enum FieldType.
	 * 
	 * @author Alberto Borsetta
	 */
	public enum FieldType {
		
		/** The questions by rows. */
		QUESTIONS_BY_ROWS(Constants.QUESTIONS_BY_ROWS, TranslationKeys.QUESTIONS_BY_ROWS), 
		
		/** The questions by cols. */
		QUESTIONS_BY_COLS(Constants.QUESTIONS_BY_COLS, TranslationKeys.QUESTIONS_BY_COLS);

		/** The name. */
		private String name;
		
		/** The value. */
		private String value;

		/**
		 * Instantiates a new field type.
		 *
		 * @author Alberto Borsetta
		 * @param name the name
		 * @param value the value
		 */
		FieldType(String name, String value) {
			this.name = name;
			this.value = value;
		}
		
		/**
		 * Gets the value.
		 *
		 * @author Alberto Borsetta
		 * @return the value
		 */
		public String getValue() {
			return value;
		}
		
		/**
		 * Gets the name.
		 *
		 * @author Alberto Borsetta
		 * @return the name
		 */
		public String getName() {
			return name;
		}
	}
}
