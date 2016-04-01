package com.albertoborsetta.formscanner.commons.resources;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class FormScannerResources {

	private static final String PNG = ".png";
	private static String iconsPath;
	private static String licensePath;
	private static String template;

	public static void setResources(String path) {
		iconsPath = path + "/icons/";
		licensePath = path + "/license/";
	}

	public static ImageIcon getIconFor(String key) {
		ImageIcon icon = new ImageIcon(iconsPath + key + PNG);
		return icon;
	}

	public static ImageIcon getIconFor(String key, int size) {
			try {
				Image image = ImageIO.read(new File(iconsPath + key + PNG));
				Image scaledImage = image.getScaledInstance(size, size, Image.SCALE_SMOOTH);
				ImageIcon icon = new ImageIcon(scaledImage);
				return icon;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
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
			e.printStackTrace();
			return null;
		}
	}
}
