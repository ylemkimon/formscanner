package org.albertoborsetta.formscanner.commons.translation;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class FormScannerTranslation extends Properties {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static FormScannerTranslation translations = null;

	private FormScannerTranslation(String path, String language) {
		super();
		try {
			String translationFile = path+"/language/formscanner-"+language+".lang";
			final FileInputStream translationInputStream = new FileInputStream(translationFile);
			load(translationInputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static FormScannerTranslation getTranslation(String path, String language) {
		if (translations == null) {
			translations = new FormScannerTranslation(path, language);
		}
		return translations;
	}

}
