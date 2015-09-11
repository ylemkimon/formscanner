package com.albertoborsetta.formscanner.commons.translation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class FormScannerTranslation extends Properties {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private static final Logger logger = LogManager.getLogger(FormScannerTranslation.class.getName());
	private static FormScannerTranslation translations = null;
	private static String translationFile;
	private static ResourceBundle resourceBundle;

	private FormScannerTranslation(String path, String language) {
		super();
		try {
			translationFile = path + "/language/formscanner-" + language + ".lang";
			final FileInputStream translationInputStream = new FileInputStream(translationFile);

			load(translationInputStream);

		} catch (IOException e) {
			logger.debug("Error loading translation file", e);
		}
	}

	public static void setTranslation(String path, String language) {
		translations = new FormScannerTranslation(path, language);
	}

	public static String getTranslationFor(String key) {
		String value = translations.getProperty(key, key);
		try {
			value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.debug("Error loading translation", e);
		}
		return value;
	}

	public static char getMnemonicFor(String key) {
		String value = translations.getProperty(key, key);
		try {
			value = new String(value.getBytes("ISO-8859-1"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			logger.debug("Error loading translation", e);
		}
		return value.charAt(0);
	}

	public static ResourceBundle getResourceBundle() {
		if (resourceBundle == null) {
			try {
				final InputStream translationInputStream = new FileInputStream(translationFile);
				if (translationInputStream != null) {
					try {
						resourceBundle = new PropertyResourceBundle(
								new InputStreamReader(translationInputStream, "UTF-8"));
					} finally {
						translationInputStream.close();
					}
				}
			} catch (IOException e) {
				logger.debug("Error loading translation file", e);
			}
		}
		return resourceBundle;
	}
}
