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
	
	public static final int ROW_CHOICE = 0;
	public static final int COLUMN_CHOICE = 1;
	public static final int GRID_CHOICE = 2;
	public static final int SINGLE = 0;
	public static final int MULTIPLE = 1;
	public static final int COLUMN = 2;
	public static final int ROW = 3;
}
