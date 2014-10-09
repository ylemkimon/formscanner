package org.albertoborsetta.formscanner.commons;

import java.awt.Rectangle;

import org.albertoborsetta.formscanner.commons.configuration.FormScannerConfigurationKeys;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;

public class FormScannerConstants {

	// Actions
	public static final String RENAME_FILES_FIRST = "RENAME_FILES_FIRST";
	public static final String RENAME_FILES_CURRENT = "RENAME_FILES_CURRENT";
	public static final String RENAME_FILES_SKIP = "RENAME_FILES_SKIP";
	public static final String OPEN_IMAGES = "OPEN_IMAGES";
	public static final String SAVE_RESULTS = "SAVE_RESULTS";
	public static final String ANALYZE_FILES_ALL = "ANALYZE_FILES_ALL";
	public static final String ANALYZE_FILES_FIRST = "ANALYZE_FILES_FIRST";
	public static final String ANALYZE_FILES_CURRENT = "ANALYZE_FILES_CURRENT";
	public static final String CREATE_TEMPLATE = "LOAD_TEMPLATE";
	public static final String LOAD_TEMPLATE = "USE_TEMPLATE";
	public static final String EDIT_TEMPLATE = "EDIT_TEMPLATE";
	public static final String CONFIRM = "CONFIRM";
	public static final String CANCEL = "CANCEL";
	public static final String SAVE_TEMPLATE = "SAVE_TEMPLATE";
	public static final String REMOVE_FIELD = "REMOVE_FIELD";
	public static final String ADD_FIELD = "ADD_FIELD";
	public static final String EXIT = "EXIT";
	public static final String HELP = "HELP";
	public static final String ABOUT = "ABOUT";
	public static final String LANGUAGE = "LANGUAGE";
	public static final String OPTIONS = "OPTIONS";

	// Frames
	public static final String RENAME_FILES_FRAME_NAME = "RENAME_FILES_FRAME_NAME";
	public static final String FILE_LIST_FRAME_NAME = "FILE_LIST_FRAME_NAME";
	public static final String MANAGE_TEMPLATE_FRAME_NAME = "MANAGE_TEMPLATE_FRAME_NAME";
	public static final String IMAGE_FRAME_NAME = "IMAGE_FRAME_NAME";
	public static final String ABOUT_FRAME_NAME = "ABOUT_FORM_SCANNER";
	public static final String DESKTOP_NAME = "DESKTOP";
	public static final String OPTIONS_FRAME_NAME = "OPTIONS";
	public static final String RESULTS_GRID_FRAME_NAME = "RESULTS_GRID_FRAME_NAME";
	
	// Frames positions and size
	private static final Rectangle FILE_LIST_FRAME_POSITION = new Rectangle(10, 10, 200, 600);
	private static final Rectangle RENAME_FILES_FRAME_POSITION = new Rectangle(220, 320, 370, 100);
	private static final Rectangle MANAGE_TEMPLATE_FRAME_POSITION = new Rectangle(100, 100, 600, 500);
	private static final Rectangle IMAGE_FRAME_POSITION = new Rectangle(10, 10, 1000, 600);
	private static final Rectangle RESULTS_GRID_FRAME_POSITION = new Rectangle(100, 100, 230, 300);
	private static final Rectangle ABOUT_FRAME_POSITION = new Rectangle(100, 100, 600, 500);
	private static final Rectangle OPTIONS_FRAME_POSITION = new Rectangle(100, 100, 300, 300);
	private static final Rectangle DESKTOP_SIZE = new Rectangle(0, 0, 1024, 768);

	// GridType
	public static final String QUESTIONS_BY_ROWS = "QUESTIONS_BY_ROWS";
	public static final String QUESTIONS_BY_COLS = "QUESTIONS_BY_COLS";

	// ShapeType
	public static final String SQUARE = "SQUARE";
	public static final String CIRCLE = "CIRCLE";

	// Properties
	public static final String IS_MULTIPLE = "IS_MULTIPLE";
	public static final String NUMBER_VALUES = "NUMBER_VALUES";
	public static final String NUMBER_COLS_ROWS = "NUMBER_COLS_ROWS";
	public static final String TYPE_COMBO_BOX = "TYPE_COMBO_BOX";

	// Image Mode
	public static final String VIEW = "VIEW";
	public static final String SETUP_POINTS = "SETUP_POINTS";
	public static final String MODIFY_POINTS = "MODIFY_POINTS";

	// Corners
	public static final String TOP_LEFT = "TOP_LEFT";
	public static final String TOP_RIGHT = "TOP_RIGHT";
	public static final String BOTTOM_LEFT = "BOTTOM_LEFT";
	public static final String BOTTOM_RIGHT = "BOTTOM_RIGHT";

	// Languages
	public static final String ENGLISH = "ENGLISH";
	public static final String ITALIAN = "ITALIAN";
	public static final String PORTUGUES = "PORTUGUES";
	public static final String SPANISH = "SPANISH";
	
	// Fields Table Columns

	// support
	public static final String WIKI_PAGE = "http://sourceforge.net/p/formscanner/wiki/Home/";
	public static final String THRESHOLD = "THRESHOLD";
	public static final String DENSITY = "DENSITY";
	public static final String SAVE_OPTIONS = "SAVE_OPTIONS";
	public static final String SHAPE_COMBO_BOX = "SHAPE_COMBO_BOX";
	public static final String SHAPE_SIZE = "SHAPE_SIZE";

	public enum Corners {
		TOP_LEFT(FormScannerConstants.TOP_LEFT, FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.TOP_LEFT_CORNER)), 
		TOP_RIGHT(FormScannerConstants.TOP_RIGHT, FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.TOP_RIGHT_CORNER)), 
		BOTTOM_RIGHT(FormScannerConstants.BOTTOM_RIGHT, FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.BOTTOM_RIGHT_CORNER)), 
		BOTTOM_LEFT(FormScannerConstants.BOTTOM_LEFT, FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.BOTTOM_LEFT_CORNER));

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
		
		public String toString() {
			return value;
		}
	}

	public enum Frame {
		RENAME_FILES_FRAME(RENAME_FILES_FRAME_NAME,	FormScannerConfigurationKeys.RENAME_FILES_FRAME, RENAME_FILES_FRAME_POSITION), 
		FILE_LIST_FRAME(FILE_LIST_FRAME_NAME, FormScannerConfigurationKeys.FILE_LIST_FRAME, FILE_LIST_FRAME_POSITION), 
		MANAGE_TEMPLATE_FRAME(MANAGE_TEMPLATE_FRAME_NAME, FormScannerConfigurationKeys.MANAGE_TEMPLATE_FRAME, MANAGE_TEMPLATE_FRAME_POSITION), 
		IMAGE_FRAME(IMAGE_FRAME_NAME, FormScannerConfigurationKeys.IMAGE_FRAME, IMAGE_FRAME_POSITION), 
		RESULTS_GRID_FRAME(RESULTS_GRID_FRAME_NAME, FormScannerConfigurationKeys.RESULTS_GRID_FRAME, RESULTS_GRID_FRAME_POSITION), 
		OPTIONS_FRAME(OPTIONS_FRAME_NAME, FormScannerConfigurationKeys.OPTIONS_FRAME, OPTIONS_FRAME_POSITION), 
		ABOUT_FRAME(ABOUT_FRAME_NAME, FormScannerConfigurationKeys.ABOUT_FRAME, ABOUT_FRAME_POSITION), 
		DESKTOP_FRAME(DESKTOP_NAME, FormScannerConfigurationKeys.DESKTOP_FRAME, DESKTOP_SIZE);

		private String value;
		private String key;
		private Rectangle position;

		Frame(String value, String key, Rectangle position) {
			this.value = value;
			this.key = key;
			this.position = position;
		}

		public String getValue() {
			return value;
		}

		public String getConfigurationKey() {
			return key;
		}

		public Rectangle getDefaultPosition() {
			return position;
		}
	}

	public enum Action {
		RENAME_FILES_FIRST, 
		RENAME_FILES_CURRENT, 
		RENAME_FILES_SKIP, 
		OPEN_IMAGES, 
		SAVE_RESULTS, 
		ANALYZE_FILES_FIRST, 
		LOAD_TEMPLATE, 
		USE_TEMPLATE, 
		EDIT_TEMPLATE, 
		CONFIRM, 
		CANCEL, 
		SAVE_TEMPLATE, 
		REMOVE_FIELD, 
		ADD_FIELD, 
		HELP, 
		ABOUT, 
		LANGUAGE, 
		EXIT, 
		TOP_LEFT, 
		TOP_RIGHT, 
		BOTTOM_LEFT, 
		BOTTOM_RIGHT, 
		OPTIONS, 
		SAVE_OPTIONS, 
		ANALYZE_FILES_ALL, 
		ANALYZE_FILES_CURRENT;
	}

	public enum Mode {
		VIEW, SETUP_POINTS, MODIFY_POINTS;
	}

	public enum FieldType {
		QUESTIONS_BY_ROWS(FormScannerConstants.QUESTIONS_BY_ROWS, FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.QUESTIONS_BY_ROWS)), 
		QUESTIONS_BY_COLS(FormScannerConstants.QUESTIONS_BY_COLS, FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.QUESTIONS_BY_COLS));

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
		
		public String toString() {
			return value;
		}
	}

	public enum ShapeType {
		SQUARE(FormScannerConstants.SQUARE, FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.SQUARE)), 
		CIRCLE(FormScannerConstants.CIRCLE, FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.CIRCLE));

		private String name;
		private String value;

		ShapeType(String name, String value) {
			this.name = name;
			this.value = value;
		}

		public String getValue() {
			return value;
		}
		
		public String getName() {
			return name;
		}
		
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
			return value / 100.0;
		}
	}

	public enum Language {
		ENGLISH("en", FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.ENGLISH)), 
		ITALIAN("it", FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.ITALIAN)),
		PORTUGUES("pt", FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.PORTUGUES)), 
		SPANISH("es", FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.SPANISH));

		private String value;
		private String translation;

		Language(String value, String translation) {
			this.value = value;
			this.translation = translation;
		}

		public String getValue() {
			return value;
		}

		public String getTranslation() {
			return translation;
		}
	}
	
	public enum FieldsTableColumn {
		NAME_COLUMN(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.NAME_COLUMN)), 
		TYPE_COLUMN(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.TYPE_COLUMN)),
		MULTIPLE_COLUMN(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.MULTIPLE_COLUMN)),
		RESPONSES_COLUMN(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.RESPONSES_COLUMN));

		private String value;

		FieldsTableColumn(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
}
