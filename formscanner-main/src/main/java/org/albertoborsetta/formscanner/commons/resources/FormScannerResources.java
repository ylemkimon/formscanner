package org.albertoborsetta.formscanner.commons.resources;

import javax.swing.ImageIcon;

import org.albertoborsetta.formscanner.gui.FormScanner;

public class FormScannerResources {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static FormScannerResources resources = null;
	private static String iconsPath;
	private static String templatePath;
	private static String template;

	private FormScannerResources(String path) {
		iconsPath = path + "/icons/";
		templatePath = path + "/template/";
	}
	
	public static FormScannerResources getResources(String path) {
		if (resources == null) {
			resources = new FormScannerResources(path);
		}
		return resources;
	}
	
	public ImageIcon getIconFor(String key) {
		ImageIcon icon = new ImageIcon(iconsPath + key);
		return icon;
	}
	
	public void setTemplate(String template) {
		this.template = template;
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
