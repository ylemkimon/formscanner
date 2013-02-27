package org.albertoborsetta.formscanner.commons.configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class FormScannerConfiguration extends Properties {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static FormScannerConfiguration configurations = null;

	private FormScannerConfiguration(String path) {
		super();
		try {
			String configFile = path + "/config/formscanner.properties";
			final FileInputStream configInputStream = new FileInputStream(configFile);
			load(configInputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static FormScannerConfiguration getConfiguration(String path) {
		if (configurations == null) {
			configurations = new FormScannerConfiguration(path);
		}
		return configurations;
	}

}
