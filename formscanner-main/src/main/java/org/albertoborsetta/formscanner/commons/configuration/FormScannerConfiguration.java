package org.albertoborsetta.formscanner.commons.configuration;

import java.io.IOException;
import java.util.Properties;

public class FormScannerConfiguration extends Properties {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static FormScannerConfiguration configurations = null;

	private FormScannerConfiguration() {
		super();
		try {
			load(getClass().getClassLoader().getResourceAsStream("/scrc/main/resources/config/formscanner.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static FormScannerConfiguration getConfiguration() {
		if (configurations == null) {
			configurations = new FormScannerConfiguration();
		}
		return configurations;
	}

}
