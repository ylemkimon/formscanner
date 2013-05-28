package org.albertoborsetta.formscanner.commons;

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
	public static final String CONFIRM = "CONFIRM";
	public static final String CANCEL = "CANCEL";
	public static final String SAVE_TEMPLATE = "SAVE_TEMPLATE";
	public static final String REMOVE_FIELD = "REMOVE_TEMPLATE";
	public static final String ADD_FIELD = "ADD_FIELD";
	public static final String EXIT = "EXIT";	

	// Frames
	public static final String RENAME_FILE_FRAME_NAME = "RENAME_FILE_FRAME_NAME";
	public static final String FILE_LIST_FRAME_NAME = "FILE_LIST_FRAME_NAME";
	public static final String MANAGE_TEMPLATE_FRAME_NAME = "MANAGE_TEMPLATE_FRAME_NAME";
	public static final String IMAGE_FRAME_NAME = "IMAGE_FRAME_NAME";
	
	// GridType
	public static final String COLUMN = "COLUMN";
	public static final String ROW = "ROW";
	public static final String GRID_ROWS = "GRID_ROWS";
	public static final String GRID_COLUMNS = "GRID_COLUMNS";
	
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
		TOP_LEFT,
		TOP_RIGHT,
		BOTTOM_LEFT,
		BOTTOM_RIGHT
	}
	
	public enum Frame {
		RENAME_FILE_FRAME_NAME,
		FILE_LIST_FRAME_NAME,
		MANAGE_TEMPLATE_FRAME_NAME,
		IMAGE_FRAME_NAME
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
		CONFIRM,
		CANCEL,
		SAVE_TEMPLATE,
		REMOVE_FIELD,
		ADD_FIELD,
		EXIT
	  }
	
	public enum FieldType {
		COLUMN,
		ROW,
		GRID_ROWS,
		GRID_COLUMNS
	}
	
	public enum Mode {
		VIEW,
		UPDATE
	}
}
