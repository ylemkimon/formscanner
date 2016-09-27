package com.albertoborsetta.formscanner.commons.configuration;

import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;

public class FormScannerConfigurationKeys {
	/*
	 * Configuration Constants
	 */

	public static final String LANG = "lang";
	public static final String DEFAULT_LANG = "en";

	public static final String TEMPLATE = "template";
	public static final String DEFAULT_TEMPLATE = "template.xtmpl";

	public static final String THRESHOLD = "threshold";
	public static final Integer DEFAULT_THRESHOLD = 127;

	public static final String DENSITY = "density";
	public static final Integer DEFAULT_DENSITY = 60;

	public static final String SHAPE_SIZE = "shape.size";
	public static final Integer DEFAULT_SHAPE_SIZE = 10;

	public static final String SHAPE_TYPE = "shape.type";
	public static final String DEFAULT_SHAPE_TYPE = "CIRCLE";

	public static final String RENAME_FILES_FRAME = "renameFilesFramePosition";
	public static final String FILE_LIST_FRAME = "fileListFramePosition";
	public static final String MANAGE_TEMPLATE_FRAME = "manageTemplateFramePosition";
	public static final String IMAGE_FRAME = "imageFramePosition";
	public static final String RESULTS_GRID_FRAME = "resultsGridFramePosition";
	public static final String OPTIONS_FRAME = "optionsFramePosition";
	public static final String ABOUT_FRAME = "aboutFramePosition";
	public static final String DESKTOP_FRAME = "desktopSize";
	public static final String TEMPLATE_SAVE_PATH = "templateSavePath";
	public static final String RESULTS_SAVE_PATH = "resultsSavePath";

	public static final String FONT_TYPE = "font.type";
	public static final String DEFAULT_FONT_TYPE = "SansSerif";

	public static final String FONT_SIZE = "font.size";
	public static final Integer DEFAULT_FONT_SIZE = 12;

	public static final String CORNER_TYPE = "corner.type";
	public static final String DEFAULT_CORNER_TYPE = "ROUND";

	public static final String LOOK_AND_FEEL = "lookAndFeel";
	public static final String DEFAULT_LOOK_AND_FEEL = "MetalLookAndFeel";
	
	public static final String RESET_AUTO_NUMBERING = "resetAutoNumbering";
	public static final Boolean DEFAULT_RESET_AUTO_NUMBERING = false;
	
	public static final String GROUPS_ENABLED = "groupsEnabled";
	public static final Boolean DEFAULT_GROUPS_ENABLED = true;
	
	public static final String GROUP_NAME_TEMPLATE = "groupNameTemplate";
	public static final String DEFAULT_GROUP_NAME_TEMPLATE = FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.GROUP) + "###";
	public static final String HISTORY_GROUP_NAME_TEMPLATE = "historyGroupNameTemplate";
	
	public static final String QUESTION_NAME_TEMPLATE = "questionNameTemplate";
	public static final String DEFAULT_QUESTION_NAME_TEMPLATE = FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.QUESTION) + "###";
	public static final String HISTORY_QUESTION_NAME_TEMPLATE = "historyQuestionNameTemplate";
	
	public static final String BARCODE_NAME_TEMPLATE = "barcodeNameTemplate";
	public static final String DEFAULT_BARCODE_NAME_TEMPLATE = FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.BARCODE) + "###";
	public static final String HISTORY_BARCODE_NAME_TEMPLATE = "historyBarcodeNameTemplate";
}
