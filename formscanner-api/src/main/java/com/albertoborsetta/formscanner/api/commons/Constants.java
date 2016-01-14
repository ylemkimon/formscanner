package com.albertoborsetta.formscanner.api.commons;

import java.util.EnumSet;
import java.util.HashMap;

import com.albertoborsetta.formscanner.api.FormPoint;
import com.albertoborsetta.formscanner.api.commons.translation.TranslationKeys;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;

/**
 * The Class Constants.
 *
 * @author Alberto Borsetta
 * @version 1.1.2
 */
public class Constants {

	/*
	 * Identifies a field with no response
	 */
	public static final String NO_RESPONSE = "";
	
	/*
	 * Identifies a point with no response
	 */
	public static final FormPoint EMPTY_POINT = null;
	
	// Margins
	/**
	 * Identifies the TOP margin
	 */
	public static final String TOP = "TOP";
	
	/**
	 * Identifies the LEFT margin
	 */
	public static final String LEFT = "LEFT";
	
	/**
	 * Identifies the RIGHT margin
	 */
	public static final String RIGHT = "RIGHT";
	
	/**
	 * Identifies the BOTTOM margin
	 */
	public static final String BOTTOM = "BOTTOM";

	// Corners
	/**
	 * Identifies the TOP_LEFT corner.
	 */
	public static final String TOP_LEFT = "TOP_LEFT";

	/**
	 * Identifies the TOP_RIGHT corner.
	 */
	public static final String TOP_RIGHT = "TOP_RIGHT";

	/**
	 * Identifies the BOTTOM_LEFT corner.
	 */
	public static final String BOTTOM_LEFT = "BOTTOM_LEFT";

	/**
	 * Identifies the BOTTOM_RIGHT corner.
	 */
	public static final String BOTTOM_RIGHT = "BOTTOM_RIGHT";

	// CornerType
	/**
	 * Identifies the ANGULAR corner type.
	 */
	public static final String ANGULAR = "ANGULAR";

	/**
	 * Identifies the ROUND corner type.
	 */
	public static final String ROUND = "ROUND";

	// GridType
	/**
	 * Identifies the QUESTIONS_BY_ROWS field type.
	 */
	public static final String QUESTIONS_BY_ROWS = "QUESTIONS_BY_ROWS";

	/**
	 * Identifies the QUESTIONS_BY_COLS field type.
	 */
	public static final String QUESTIONS_BY_COLS = "QUESTIONS_BY_COLS";

	/**
	 * Identifies the RESPONSES_BY_GRID field type.
	 */
	public static final String RESPONSES_BY_GRID = "RESPONSES_BY_GRID";

	/**
	 * Identifies the QRCODE_BARCODE field type.
	 */
	public static final String BARCODE = "BARCODE";

	/**
	 * Identifies the current version of the template
	 */
	public static final String CURRENT_TEMPLATE_VERSION = "2.1";

	/**
	 * The Enum Corners.
	 *
	 * @author Alberto Borsetta
	 */
	public enum Corners {

		/**
		 * The TOP_LEFT corner.
		 */
		TOP_LEFT(Constants.TOP_LEFT, TranslationKeys.TOP_LEFT_CORNER),
		/**
		 * The TOP_RIGHT corner.
		 */
		TOP_RIGHT(Constants.TOP_RIGHT, TranslationKeys.TOP_RIGHT_CORNER),
		/**
		 * The BOTTOM_RIGHT corner.
		 */
		BOTTOM_RIGHT(Constants.BOTTOM_RIGHT, TranslationKeys.BOTTOM_RIGHT_CORNER),
		/**
		 * The BOTTOM_LEFT corner.
		 */
		BOTTOM_LEFT(Constants.BOTTOM_LEFT, TranslationKeys.BOTTOM_LEFT_CORNER);

		private final String name;
		private final String value;

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
	 */
	public enum FieldType {

		/**
		 * The QUESTIONS_BY_ROWS field type.
		 */
		QUESTIONS_BY_ROWS(0, Constants.QUESTIONS_BY_ROWS, TranslationKeys.QUESTIONS_BY_ROWS),
		/**
		 * The QUESTIONS_BY_COLS field type.
		 */
		QUESTIONS_BY_COLS(1, Constants.QUESTIONS_BY_COLS, TranslationKeys.QUESTIONS_BY_COLS),
		/**
		 * The RESPONSES_BY_GRID field type.
		 */
		RESPONSES_BY_GRID(2, Constants.RESPONSES_BY_GRID, TranslationKeys.RESPONSES_BY_GRID),
		/**
		 * The QRCODE_BARCODE field type.
		 */
		BARCODE(3, Constants.BARCODE, TranslationKeys.BARCODE);

		private final int index;
		private final String name;
		private final String value;

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

	/**
	 * The HINTS for barcode detecting
	 */
	public static final HashMap<DecodeHintType, Object> HINTS = new HashMap<>();

	/**
	 * The HINTS_PURE for barcode detecting
	 */
	public static final HashMap<DecodeHintType, Object> HINTS_PURE = new HashMap<>();

	static {
		HINTS.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
		HINTS.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.allOf(BarcodeFormat.class));
		HINTS_PURE.putAll(HINTS);
		HINTS_PURE.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
	}

	// ShapeType
	/**
	 * Identifies the SQUARE marker shape.
	 */
	public static final String SQUARE = "SQUARE";

	/**
	 * Identifies the CIRCLE marker shape.
	 */
	public static final String CIRCLE = "CIRCLE";

	/**
	 * Default name for empty group name
	 */
	public static final String EMPTY_GROUP_NAME = "EMPTY";

	/**
	 * The Enum ShapeType.
	 *
	 * @author Alberto Borsetta
	 */
	public enum ShapeType {

		/**
		 * The SQUARE marker shape
		 */
		SQUARE(0, Constants.SQUARE, TranslationKeys.SQUARE),
		/**
		 * The CIRCLE marker shape
		 */
		CIRCLE(1, Constants.CIRCLE, TranslationKeys.CIRCLE);

		private final int index;
		private final String name;
		private final String value;

		/**
		 * Instantiates a new shape type.
		 *
		 * @author Alberto Borsetta
		 * @param index the index
		 * @param name the name
		 * @param value the value
		 */
		private ShapeType(int index, String name, String value) {
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
		 * Returns the value.
		 *
		 * @author Alberto Borsetta
		 * @return the value
		 */
		public String getValue() {
			return value;
		}

		/**
		 * Returns the name.
		 *
		 * @author Alberto Borsetta
		 * @return the name
		 */
		public String getName() {
			return name;
		}
	}


	/**
	 * The Enum CornerType.
	 *
	 * @author Alberto Borsetta
	 */
	public enum CornerType {

		/**
		 * The ROUND corner type.
		 */
		ROUND(0, Constants.ROUND, TranslationKeys.ROUND),

		/**
		 * The SQUARE corner type.
		 */
		ANGULAR(1, Constants.ANGULAR, TranslationKeys.ANGULAR);

		private final int index;
		private final String name;
		private final String value;

		/**
		 * Instantiates a new corner type.
		 *
		 * @author Alberto Borsetta
		 * @param name the name
		 * @param value the value
		 */
		private CornerType(int index, String name, String value) {
			this.index = index;
			this.name = name;
			this.value = value;
		}

		/**
		 * Returns the constant corresponding to the corner type.
		 *
		 * @author Alberto Borsetta
		 * @return the constant corresponding to the corner type
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

		/**
		 * Returns the index.
		 *
		 * @author Alberto Borsetta
		 * @return the index
		 */
		public int getIndex() {
			return index;
		}
	}
}
