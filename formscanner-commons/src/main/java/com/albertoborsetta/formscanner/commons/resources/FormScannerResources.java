package com.albertoborsetta.formscanner.commons.resources;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

public class FormScannerResources {

	private static final String PNG = ".png";
	private static String iconsPath;
	private static String licensePath;
	private static String template;
	private static final Logger logger = LogManager
			.getLogger(FormScannerResources.class.getName());

	public static void setResources(String path) {
		iconsPath = path + "/icons/";
		licensePath = path + "/license/";
	}

	public static ImageIcon getIconFor(String key) {
		ImageIcon icon = new ImageIcon(iconsPath + key + PNG);
		return icon;
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
		try {
			Image icon = ImageIO.read(new File(
					iconsPath + FormScannerResourcesKeys.FORMSCANNER_ICON + PNG));
			return icon;
		} catch (IOException e) {
			logger.catching(e);
			return null;
		}
	}
}
