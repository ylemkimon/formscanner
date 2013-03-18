package org.albertoborsetta.formscanner.commons;

public class FormScannerConstants {

	public static final String RENAME_FILE_FIRST = "RENAME_FILE_FIRST";
	public static final String RENAME_FILE_CURRENT = "RENAME_FILE_CURRENT";
	public static final String RENAME_FILE_SKIP = "RENAME_FILE_SKIP";
	public static final String OPEN_IMAGES = "OPEN_IMAGES";
	public static final String SAVE_RESULTS = "SAVE_RESULTS";
	public static final String ANALYZE_FILE_FIRST = "ANALYZE_FILE_FIRST";
	public static final String ANALYZE_FILE_CURRENT = "ANALYZE_FILE_NEXT";
	public static final String ANALYZE_FILE_SKIP = "ANALYZE_FILE_SKIP";
	public static final String EXIT = "EXIT";	
	
	public static final String RENAME_FILE_FRAME_NAME = "rename.file.frame";
	public static final String RENAME_FILE_IMAGE_FRAME_NAME = "rename.file.image.frame";
	public static final String FILE_LIST_FRAME_NAME = "file.list.frame";
	public static final String ANALYZE_IMAGE_FRAME_NAME = "analyze.image.frame";
	public static final String MANAGE_TEMPLATE_FRAME_NAME = "manage.template.frame";
	
	public enum Actions {
		RENAME_FILE_FIRST,
		RENAME_FILE_CURRENT,
		RENAME_FILE_SKIP,
		OPEN_IMAGES,
		SAVE_RESULTS,
		ANALYZE_FILE_FIRST,
		ANALYZE_FILE_NEXT,
		ANALYZE_FILE_SKIP,
		EXIT
	  }
	
	public static final boolean SINGLE = false;
	public static final boolean MULTIPLE = !SINGLE;
	public static final String COLUMN = "column";
	public static final String ROW = "row";
	public static final String GRID_ROWS = "grid.by.rows";
	public static final String GRID_COLUMNS = "grid.by.columns";
	
	public enum FieldType {
		COLUMN,
		ROW,
		GRID_ROWS,
		GRID_COLUMNS
	}
}
