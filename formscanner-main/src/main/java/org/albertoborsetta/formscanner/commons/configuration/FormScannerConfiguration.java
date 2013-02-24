package org.albertoborsetta.formscanner.commons.configuration;

import java.io.IOException;
import java.util.Properties;

import org.albertoborsetta.formscanner.gui.FormScanner;

public class FormScannerConfiguration extends Properties {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private FormScannerConfiguration properties = null;

	private FormScannerConfiguration() {
		super();
		try {
			load(FormScanner.class.getClassLoader().getResourceAsStream("config/formscanner.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public FormScannerConfiguration getProperties() {
		if (properties == null) {
			properties = new FormScannerConfiguration();
		}
		return properties;
	}

}
