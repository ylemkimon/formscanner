package com.albertoborsetta.formscanner.commons.resources;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.apache.logging.log4j.Logger;
import javafx.scene.image.Image;
import org.apache.logging.log4j.LogManager;

public class FormScannerResources {

	private static final String PNG = ".png";
	private static String iconsPath;
	private static String licensePath;
	private static String template;
	private static final Logger logger = LogManager.getLogger(FormScannerResources.class.getName());

	public static void setResources(String path) {
		iconsPath = path + "/icons/";
		licensePath = path + "/license/";
	}

	public static Image getIconFor(String key) {
		return new Image("file:" + iconsPath + key + PNG);
//		Image icon = new Image(new URL(iconsPath + key + PNG));
//		return null;
	}

	public static void setTemplate(String tpl) {
		template = tpl;
	}

	public static File getTemplate() {
		return new File(template);
	}

	public static File getLicense() {
		return new File(licensePath + "license.txt");
	}

	public static Image getFormScannerIcon() {
		Image icon = new Image(iconsPath + FormScannerResourcesKeys.FORMSCANNER_ICON + PNG);
		return icon;
	}
}
