package org.albertoborsetta.formscanner.commons.translation;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

public class FormScannerTranslation extends Properties {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static FormScannerTranslation translations = null;

	private FormScannerTranslation(String path, String language) {
		super();
		try {
			String translationFile = path + "/language/formscanner-" + language
					+ ".lang";
			final FileInputStream translationInputStream = new FileInputStream(
					translationFile);

			InputStreamReader isr = new InputStreamReader(translationInputStream, "UTF-8");
			byte[] bs = new String(IOUtils.toByteArray(isr), "UTF-8").getBytes("ISO-8859-1");
			isr.close();
						
			ByteArrayInputStream bais = new ByteArrayInputStream(bs);
			
			load(bais);
		} catch (IOException e) {
			// TODO Auto-generated catch block
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
