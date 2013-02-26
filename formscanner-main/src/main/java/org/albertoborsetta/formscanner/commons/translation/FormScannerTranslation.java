package org.albertoborsetta.formscanner.commons.translation;

import java.io.IOException;
import java.util.Properties;

public class FormScannerTranslation extends Properties {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static FormScannerTranslation translations = null;

	private FormScannerTranslation(String language) {
		super();
		try {
			load(getClass().getClassLoader().getResourceAsStream("language/formscanner-"+language+".lang"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static FormScannerTranslation getTranslation(String language) {
		if (translations == null) {
			translations = new FormScannerTranslation(language);
		}
		return translations;
	}

}
