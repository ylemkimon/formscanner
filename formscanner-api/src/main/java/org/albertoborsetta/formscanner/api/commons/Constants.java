package org.albertoborsetta.formscanner.api.commons;

import org.albertoborsetta.formscanner.api.commons.translation.TranslationKeys;

public class Constants {

	// Corners
	public static final String TOP_LEFT = "TOP_LEFT";
	public static final String TOP_RIGHT = "TOP_RIGHT";
	public static final String BOTTOM_LEFT = "BOTTOM_LEFT";
	public static final String BOTTOM_RIGHT = "BOTTOM_RIGHT";
	
	// GridType
	public static final String QUESTIONS_BY_ROWS = "QUESTIONS_BY_ROWS";
	public static final String QUESTIONS_BY_COLS = "QUESTIONS_BY_COLS";

	public enum Corners {
		TOP_LEFT(Constants.TOP_LEFT, TranslationKeys.TOP_LEFT_CORNER), 
		TOP_RIGHT(Constants.TOP_RIGHT, TranslationKeys.TOP_RIGHT_CORNER), 
		BOTTOM_RIGHT(Constants.BOTTOM_RIGHT, TranslationKeys.BOTTOM_RIGHT_CORNER), 
		BOTTOM_LEFT(Constants.BOTTOM_LEFT, TranslationKeys.BOTTOM_LEFT_CORNER);

		private String name;
		private String value;

		Corners(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String getName() {
			return name;
		}
		
		public String getValue() {
			return value;
		}
	}

	
	public enum FieldType {
		QUESTIONS_BY_ROWS(Constants.QUESTIONS_BY_ROWS, TranslationKeys.QUESTIONS_BY_ROWS), 
		QUESTIONS_BY_COLS(Constants.QUESTIONS_BY_COLS, TranslationKeys.QUESTIONS_BY_COLS);

		private String name;
		private String value;

		FieldType(String name, String value) {
			this.name = name;
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
		public String getName() {
			return name;
		}
	}
}
