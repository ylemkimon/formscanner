package com.albertoborsetta.formscanner.commons.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

public class FormScannerConfiguration extends Properties {
	
	private static String configFile;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static FormScannerConfiguration configurations = null;

	private FormScannerConfiguration(String propertiesFile) {
		super();
		try {
			load(new FileInputStream(propertiesFile));
		} catch (IOException e) {			
			e.printStackTrace();
		}
	}
	
	public static FormScannerConfiguration getConfiguration(String path, String defaultPath) {
		if (configurations == null) {
			configFile = path + "/formscanner.properties";
			File destFile = new File(configFile);

			if (!destFile.exists() || destFile.isDirectory()) {
				System.out.println("Cannot find properties file into user home directory... try loading default properties");
				String propertiesFile = defaultPath + "/config/formscanner.properties";
				File srcFile = new File(propertiesFile);
				try {
					FileUtils.copyFile(srcFile, destFile);
				} catch (IOException e) {
					configFile = propertiesFile;
					e.printStackTrace();
				}
			}			
			configurations = new FormScannerConfiguration(configFile);
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
