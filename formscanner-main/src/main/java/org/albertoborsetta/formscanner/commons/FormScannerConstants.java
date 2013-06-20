package org.albertoborsetta.formscanner.commons;

import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;

public class FormScannerConstants {

	// Actions
	public static final String RENAME_FILE_FIRST = "RENAME_FILE_FIRST";
	public static final String RENAME_FILE_CURRENT = "RENAME_FILE_CURRENT";
	public static final String RENAME_FILE_SKIP = "RENAME_FILE_SKIP";
	public static final String OPEN_IMAGES = "OPEN_IMAGES";
	public static final String SAVE_RESULTS = "SAVE_RESULTS";
	public static final String ANALYZE_FILE_FIRST = "ANALYZE_FILE_FIRST";
	public static final String ANALYZE_FILE_CURRENT = "ANALYZE_FILE_NEXT";
	public static final String ANALYZE_FILE_SKIP = "ANALYZE_FILE_SKIP";
	public static final String LOAD_TEMPLATE = "LOAD_TEMPLATE";
	public static final String USE_TEMPLATE = "USE_TEMPLATE";
	public static final String EDIT_TEMPLATE = "EDIT_TEMPLATE";
	public static final String CONFIRM = "CONFIRM";
	public static final String CANCEL = "CANCEL";
	public static final String SAVE_TEMPLATE = "SAVE_TEMPLATE";
	public static final String REMOVE_FIELD = "REMOVE_FIELD";
	public static final String ADD_FIELD = "ADD_FIELD";
	public static final String EXIT = "EXIT";	

	// Frames
	public static final String RENAME_FILE_FRAME_NAME = "RENAME_FILE_FRAME_NAME";
	public static final String FILE_LIST_FRAME_NAME = "FILE_LIST_FRAME_NAME";
	public static final String MANAGE_TEMPLATE_FRAME_NAME = "MANAGE_TEMPLATE_FRAME_NAME";
	public static final String IMAGE_FRAME_NAME = "IMAGE_FRAME_NAME";
	
	// GridType
	public static final String QUESTIONS_BY_ROWS = "QUESTIONS_BY_ROWS";
	public static final String QUESTIONS_BY_COLS = "QUESTIONS_BY_COLS";
	
	// Properties
	public static final String IS_MULTIPLE = "IS_MULTIPLE";
	public static final String NUMBER_VALUES = "NUMBER_VALUES";
	public static final String NUMBER_COLS_ROWS = "NUMBER_COLS_ROWS";
	public static final String TYPE_COMBO_BOX = "TYPE_COMBO_BOX";
	
	// Image Mode
	public static final String VIEW = "VIEW";
	public static final String UPDATE = "UPDATE";
	
	// Corners
	public static final String TOP_LEFT = "TOP_LEFT";
	public static final String TOP_RIGHT = "TOP_RIGHT";
	public static final String BOTTOM_LEFT = "BOTTOM_LEFT";
	public static final String BOTTOM_RIGHT = "BOTTOM_RIGHT";
	
	public enum Corners {
		TOP_LEFT(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.TOP_LEFT_CORNER)),
		TOP_RIGHT(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.TOP_RIGHT_CORNER)),
		BOTTOM_RIGHT(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.BOTTOM_RIGHT_CORNER)),
		BOTTOM_LEFT(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.BOTTOM_LEFT_CORNER));
		
		private String value; 

		Corners(String value) { 
			this.value = value;
		}
		
		@Override
		public String toString() {
			return value;
		}
	}
	
	public enum Frame {
		RENAME_FILE_FRAME_NAME,
		FILE_LIST_FRAME_NAME,
		MANAGE_TEMPLATE_FRAME_NAME,
		IMAGE_FRAME_NAME;
	}
	
	public enum Action {
		RENAME_FILE_FIRST,
		RENAME_FILE_CURRENT,
		RENAME_FILE_SKIP,
		OPEN_IMAGES,
		SAVE_RESULTS,
		ANALYZE_FILE_FIRST,
		ANALYZE_FILE_NEXT,
		ANALYZE_FILE_SKIP,
		LOAD_TEMPLATE,
		USE_TEMPLATE,
		EDIT_TEMPLATE,
		CONFIRM,
		CANCEL,
		SAVE_TEMPLATE,
		REMOVE_FIELD,
		ADD_FIELD,
		EXIT;
	}
	
	public enum Mode {
		VIEW,
		UPDATE;
	}
	
	public enum FieldType {
		QUESTIONS_BY_ROWS(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.QUESTIONS_BY_ROWS)),
		QUESTIONS_BY_COLS(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.QUESTIONS_BY_COLS));
		
		private String value; 

		FieldType(String value) { 
			this.value = value;
		}
		
		@Override
		public String toString() {
			return value;
		}
	}
	public enum Zoom {
		
		PERCENT_25(25),
		PERCENT_50(50),
		PERCENT_75(75),
		PERCENT_100(100),
		PERCENT_125(125),
		PERCENT_150(150),
		PERCENT_200(200);
		
		private Integer value;
		
		Zoom(Integer value) {
			this.value = value;
		}
		
		public double getValue() {
			return value/100.0;
		}
	}
}
