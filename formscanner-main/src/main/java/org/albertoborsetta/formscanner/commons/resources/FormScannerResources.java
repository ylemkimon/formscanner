package org.albertoborsetta.formscanner.commons.resources;

import java.io.File;

import javax.swing.ImageIcon;

import org.albertoborsetta.formscanner.commons.configuration.FormScannerConfigurationKeys;
import org.apache.commons.lang3.StringUtils;

public class FormScannerResources {
	
	private static String iconsPath;
	private static String templatePath;
	private static String licensePath;
	private static String template;

	public static void setResources(String path) {
		iconsPath = path + "/icons/";
		templatePath = path + "/template/";
		licensePath = path + "/license/";
	}
	
	public static ImageIcon getIconFor(String key) {
		ImageIcon icon = new ImageIcon(iconsPath + key);
		return icon;
	}
	
	public static void setTemplate(String tpl) {
		template = tpl;
		
		if (StringUtils.isEmpty(template)) {
			template = templatePath + FormScannerConfigurationKeys.DEFAULT_TEMPLATE;
		}
	}

	public static File getTemplate() {
		return new File(template);
	}
	
	public static File getLicense() {
		return new File(licensePath + "license.txt");
	}
}
