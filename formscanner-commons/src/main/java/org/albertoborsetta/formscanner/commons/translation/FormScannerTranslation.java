package org.albertoborsetta.formscanner.commons.translation;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class FormScannerTranslation extends Properties {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected static FormScannerTranslation translations = null;

	
	private FormScannerTranslation(String path, String language) {
		super();
		try {
			String translationFile = path + "/language/formscanner-" + language
					+ ".lang";
			final FileInputStream translationInputStream = new FileInputStream(
					translationFile);

			load(translationInputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void setTranslation(String path, String language) {
		translations = new FormScannerTranslation(path, language);
	}

	public static String getTranslationFor(String key) {
		String value = translations.getProperty(key, key);
		return value;
	}
	
	public static char getMnemonicFor(String key) {
		char value = translations.getProperty(key, key).charAt(0);
		return value;
	}
}
