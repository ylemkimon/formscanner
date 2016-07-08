package com.albertoborsetta.formscanner.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JRadioButtonMenuItem;

import com.albertoborsetta.formscanner.commons.FormScannerConstants;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.Action;
import com.albertoborsetta.formscanner.model.FormScannerModel;

public class MenuBarController implements ActionListener {

	private final FormScannerModel model;
	private static MenuBarController instance;

	public static MenuBarController getInstance(FormScannerModel model) {
		if (instance == null) {
			instance = new MenuBarController(model);
		}
		return instance;
	}

	private MenuBarController(FormScannerModel model) {
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Action act = Action.valueOf(e.getActionCommand());
		switch (act) {
		case RENAME_FILES_FIRST:
			model.renameSelectedFile(FormScannerConstants.RENAME_FILES_FIRST);
			break;
		case OPEN_IMAGES:
			model.openImages();
			break;
		case SAVE_RESULTS:
			break;
		case ANALYZE_FILES_ALL:
			model.analyzeFiles(FormScannerConstants.ANALYZE_FILES_ALL);
			break;
		case ANALYZE_FILES_FIRST:
			model.analyzeFiles(FormScannerConstants.ANALYZE_FILES_FIRST);
			break;
		case ANALYZE_FILES_CURRENT:
			model.analyzeFiles(FormScannerConstants.ANALYZE_FILES_CURRENT);
			break;
		case SAVE_TEMPLATE:
			model.saveTemplate(true);
			break;
		case LOAD_TEMPLATE:
			model.loadTemplate();
			break;
		case USE_TEMPLATE:
			model.openTemplate();
			break;
		case EXIT:
			model.exitFormScanner();
			break;
		case HELP:
			try {
				model.linkToHelp(new URL(FormScannerConstants.WIKI_PAGE));
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			break;
		case ABOUT:
			model.showAboutFrame();
			break;
		case LANGUAGE:
			JRadioButtonMenuItem object = (JRadioButtonMenuItem) e.getSource();
			model.setLanguage(object.getName());
			break;
		case OPTIONS:
			model.showOptionsFrame();
			break;
		default:
			break;
		}
	}
}
