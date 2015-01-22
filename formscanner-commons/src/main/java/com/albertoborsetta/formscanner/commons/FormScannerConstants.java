package com.albertoborsetta.formscanner.commons;

import java.awt.Rectangle;

import com.albertoborsetta.formscanner.api.commons.Constants;
import com.albertoborsetta.formscanner.commons.configuration.FormScannerConfigurationKeys;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;

public class FormScannerConstants extends Constants {

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

	// ShapeType
	public static final String SQUARE = "SQUARE";
	public static final String CIRCLE = "CIRCLE";

	// Properties
	public static final String IS_MULTIPLE = "IS_MULTIPLE";
	public static final String NUMBER_VALUES = "NUMBER_VALUES";
	public static final String NUMBER_COLS_ROWS = "NUMBER_COLS_ROWS";
	public static final String TYPE_COMBO_BOX = "TYPE_COMBO_BOX";
	public static final String REJECT_IF_NOT_MULTIPLE = "REJECT_IF_NOT_MULTIPLE";

	// Image Mode
	public static final String VIEW = "VIEW";
	public static final String SETUP_POINTS = "SETUP_POINTS";
	public static final String MODIFY_POINTS = "MODIFY_POINTS";

	// Languages
	public static final String ENGLISH = "ENGLISH";
	public static final String ITALIAN = "ITALIAN";
	public static final String PORTUGUES = "PORTUGUES";
	public static final String SPANISH = "SPANISH";
	public static final String GREEK = "GREEK";
	public static final String POLISH = "POLISH";
	
	// Fields Table Columns

	// support
	public static final String WIKI_PAGE = "http://sourceforge.net/p/formscanner/wiki/Home/";
	public static final String THRESHOLD = "THRESHOLD";
	public static final String DENSITY = "DENSITY";
	public static final String SAVE_OPTIONS = "SAVE_OPTIONS";
	public static final String SHAPE_COMBO_BOX = "SHAPE_COMBO_BOX";
	public static final String SHAPE_SIZE = "SHAPE_SIZE";
	
	// Zoom
	public static final Integer FIT_WIDTH = 0;
	public static final Integer FIT_PAGE = -1;

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

		private Frame(String value, String key, Rectangle position) {
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
		ANALYZE_FILES_CURRENT,
		IS_MULTIPLE;
	}

	public enum Mode {
		VIEW, SETUP_POINTS, MODIFY_POINTS;
	}

	public enum ShapeType {
		SQUARE(0, FormScannerConstants.SQUARE, FormScannerTranslationKeys.SQUARE), 
		CIRCLE(1, FormScannerConstants.CIRCLE, FormScannerTranslationKeys.CIRCLE);

		private int index;
		private String name;
		private String value;

		private ShapeType(int index, String name, String value) {
			this.index = index;
			this.name = name;
			this.value = value;
		}

		public int getIndex() {
			return index;
		}
		
		public String getValue() {
			return value;
		}
		
		public String getName() {
			return name;
		}
	}

	public enum Zoom {

		ZOOM_25(25), 
		ZOOM_50(50), 
		ZOOM_75(75), 
		ZOOM_100(100), 
		ZOOM_125(125), 
		ZOOM_150(150), 
		ZOOM_200(200),
		ZOOM_WIDTH(FIT_WIDTH),
		ZOOM_PAGE(FIT_PAGE);

		private Integer value;

		private Zoom(Integer value) {
			this.value = value;
		}

		public Integer getValue() {
			return value;
		}
	}

	public enum Language {
		ENGLISH("en", FormScannerTranslationKeys.ENGLISH), 
		ITALIAN("it", FormScannerTranslationKeys.ITALIAN),
		PORTUGUES("pt_BR", FormScannerTranslationKeys.PORTUGUES),
		// SPANISH("es", FormScannerTranslationKeys.SPANISH),
		// GERMAN("de", FormScannerTranslationKeys.GERMAN),
		POLISH("pl", FormScannerTranslationKeys.POLISH),
		GREEK("el", FormScannerTranslationKeys.GREEK);

		private String value;
		private String translation;

		private Language(String value, String translation) {
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
		NAME_COLUMN(FormScannerTranslationKeys.NAME_COLUMN), 
		TYPE_COLUMN(FormScannerTranslationKeys.TYPE_COLUMN),
		MULTIPLE_COLUMN(FormScannerTranslationKeys.MULTIPLE_COLUMN),
		RESPONSES_COLUMN(FormScannerTranslationKeys.RESPONSES_COLUMN);

		private String value;

		private FieldsTableColumn(String value) {
			this.value = value;
		}

		public String getValue() {
			return value;
		}
	}
}
