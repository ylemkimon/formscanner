package org.albertoborsetta.formscanner.commons.resources;

import javax.swing.ImageIcon;

public class FormScannerResources {
	
	private static String iconsPath;
	private static String templatePath;
	private static String template;

	/*
	private FormScannerResources(String path) {
		iconsPath = path + "/icons/";
		templatePath = path + "/template/";
	}
	*/
	
	public static void setResources(String path) {
		// new FormScannerResources(path);
		iconsPath = path + "/icons/";
		templatePath = path + "/template/";
	}
	
	public static ImageIcon getIconFor(String key) {
		ImageIcon icon = new ImageIcon(iconsPath + key);
		return icon;
	}
	
	public static void setTemplate(String tpl) {
		template = tpl;
	}

	public String getTemplateConfig() {
		String templateConfig = templatePath + template + ".config";
		return templateConfig;
	}
	
	public String getTemplateFields() {
		String templateFields = templatePath + template + ".fields";
		return templateFields;
	}
	
	public String getTemplateAsc() {
		String templateAsc = templatePath + template + ".asc";
		return templateAsc;
	}
}
