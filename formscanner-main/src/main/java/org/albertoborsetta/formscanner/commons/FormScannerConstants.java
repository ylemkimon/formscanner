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
	public static final String RENAME_FILE_IMAGE_FRAME_NAME = "RENAME_FILE_IMAGE_FRAME_NAME";
	public static final String FILE_LIST_FRAME_NAME = "FILE_LIST_FRAME_NAME";
	public static final String ANALYZE_IMAGE_FRAME_NAME = "ANALYZE_IMAGE_FRAME_NAME";
	public static final String MANAGE_TEMPLATE_FRAME_NAME = "MANAGE_TEMPLATE_FRAME_NAME";
	public static final String MANAGE_TEMPLATE_IMAGE_FRAME_NAME = "MANAGE_TEMPLATE_IMAGE_FRAME_NAME";
	public static final String ZOOM_IMAGE_FRAME_NAME = "ZOOM_IMAGE_FRAME_NAME";
	
	// GridType
	public static final String COLUMN = "COLUMN";
	public static final String ROW = "ROW";
	public static final String GRID_ROWS = "GRID_ROWS";
	public static final String GRID_COLUMNS = "GRID_COLUMNS";
	
	// Properties
	public static final String NUMBER_VALUES = "NUMBER_VALUES";
	public static final String NUMBER_COLS_ROWS = "NUMBER_COLS_ROWS";
	public static final String TYPE_COMBO_BOX = "TYPE_COMBO_BOX";
	
	public enum Frames {
		RENAME_FILE_FRAME_NAME,
		RENAME_FILE_IMAGE_FRAME_NAME,
		FILE_LIST_FRAME_NAME,
		ANALYZE_IMAGE_FRAME_NAME,
		MANAGE_TEMPLATE_FRAME_NAME,
		MANAGE_TEMPLATE_IMAGE_FRAME_NAME,
		ZOOM_IMAGE_FRAME_NAME
	}
	
	public enum Actions {
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
}
