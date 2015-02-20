package com.albertoborsetta.formscanner.api.commons;

import com.albertoborsetta.formscanner.api.commons.translation.TranslationKeys;

/**
 * The Class Constants.
 * 
 * @author Alberto Borsetta
 * @version 0.11-SNAPSHOT
 */
public class Constants {

	// Corners
	/** Identifies the TOP_LEFT corner. */
	public static final String TOP_LEFT = "TOP_LEFT";
	
	/** Identifies the TOP_RIGHT corner. */
	public static final String TOP_RIGHT = "TOP_RIGHT";
	
	/** Identifies the BOTTOM_LEFT corner. */
	public static final String BOTTOM_LEFT = "BOTTOM_LEFT";
	
	/** Identifies the BOTTOM_RIGHT corner. */
	public static final String BOTTOM_RIGHT = "BOTTOM_RIGHT";
	
	// GridType
	/** Identifies the QUESTIONS_BY_ROWS field type. */
	public static final String QUESTIONS_BY_ROWS = "QUESTIONS_BY_ROWS";
	
	/** Identifies the QUESTIONS_BY_COLS field type. */
	public static final String QUESTIONS_BY_COLS = "QUESTIONS_BY_COLS";

	/**
	 * The Enum Corners.
	 * 
	 * @author Alberto Borsetta
	 * @version 0.11-SNAPSHOT
	 */
	public enum Corners {
		
		/** The TOP_LEFT corner. */
		TOP_LEFT(Constants.TOP_LEFT, TranslationKeys.TOP_LEFT_CORNER), 
		
		/** The TOP_RIGHT corner. */
		TOP_RIGHT(Constants.TOP_RIGHT, TranslationKeys.TOP_RIGHT_CORNER), 
		
		/** The BOTTOM_RIGHT corner. */
		BOTTOM_RIGHT(Constants.BOTTOM_RIGHT, TranslationKeys.BOTTOM_RIGHT_CORNER), 
		
		/** The BOTTOM_LEFT corner. */
		BOTTOM_LEFT(Constants.BOTTOM_LEFT, TranslationKeys.BOTTOM_LEFT_CORNER);

		private String name;
		private String value;

		/**
		 * Instantiates a new corners.
		 *
		 * @author Alberto Borsetta
		 * @param name the name
		 * @param value the value
		 */
		private Corners(String name, String value) {
			this.name = name;
			this.value = value;
		}

		/**
		 * Returns the constant corresponding to the corner.
		 *
		 * @author Alberto Borsetta
		 * @return the constant corresponding to the corner
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * Returns the value used for translation.
		 *
		 * @author Alberto Borsetta
		 * @return the value used for translation
		 */
		public String getValue() {
			return value;
		}
	}

	
	/**
	 * The Enum FieldType.
	 * 
	 * @author Alberto Borsetta
	 * @version 0.11-SNAPSHOT
	 */
	public enum FieldType {
		
		/** The QUESTIONS_BY_ROWS field type. */
		QUESTIONS_BY_ROWS(0, Constants.QUESTIONS_BY_ROWS, TranslationKeys.QUESTIONS_BY_ROWS), 
		
		/** The QUESTIONS_BY_COLS field type. */
		QUESTIONS_BY_COLS(1, Constants.QUESTIONS_BY_COLS, TranslationKeys.QUESTIONS_BY_COLS);

		private int index;
		private String name;
		private String value;

		/**
		 * Instantiates a new field type.
		 *
		 * @author Alberto Borsetta
		 * @param index the index
		 * @param name the name
		 * @param value the value
		 */
		private FieldType(int index, String name, String value) {
			this.index = index;
			this.name = name;
			this.value = value;
		}
		
		/**
		 * Returns the index.
		 *
		 * @author Alberto Borsetta
		 * @return the index
		 */
		public int getIndex() {
			return index;
		}
		
		/**
		 * Returns the value used for translation.
		 *
		 * @author Alberto Borsetta
		 * @return the value used for translation
		 */
		public String getValue() {
			return value;
		}
		
		/**
		 * Returns the constant corresponding to the field type.
		 *
		 * @author Alberto Borsetta
		 * @return the constant corresponding to the field type
		 */
		public String getName() {
			return name;
		}
	}
}
