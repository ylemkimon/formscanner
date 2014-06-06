package org.albertoborsetta.formscanner.commons.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class FormScannerConfiguration extends Properties {
	
	private String configFile;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static FormScannerConfiguration configurations = null;

	private FormScannerConfiguration(String path) {
		super();
		try {
			configFile = path + "/config/formscanner.properties";
			load(new FileInputStream(configFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static FormScannerConfiguration getConfiguration(String path) {
		if (configurations == null) {
			configurations = new FormScannerConfiguration(path);
		}
		return configurations;
	}
	
	public void store() {
		try {
			store(new FileOutputStream(configFile), null);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
