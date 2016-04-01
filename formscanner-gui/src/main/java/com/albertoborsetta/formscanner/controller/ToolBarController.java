package com.albertoborsetta.formscanner.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JRadioButtonMenuItem;

import com.albertoborsetta.formscanner.commons.FormScannerConstants;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.Action;
import com.albertoborsetta.formscanner.model.FormScannerModel;

public class ToolBarController implements ActionListener {

	private final FormScannerModel model;
	private static ToolBarController instance;

	public static ToolBarController getInstance(FormScannerModel model) {
		if (instance == null) {
			instance = new ToolBarController(model);
		}
		return instance;
	}

	private ToolBarController(FormScannerModel model) {
		this.model = model;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Action act = Action.valueOf(e.getActionCommand());
		switch (act) {
		case OPEN_IMAGES:
			model.openImages();
			break;
		case RENAME_FILES_FIRST:
			model.renameFiles(FormScannerConstants.RENAME_FILES_FIRST);
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
		case HELP:
			try {
				model.linkToHelp(new URL(FormScannerConstants.WIKI_PAGE));
			} catch (MalformedURLException e1) {
				e1.printStackTrace();
			}
			break;
		case OPTIONS:
			model.showOptionsFrame();
			break;
		default:
			break;
		}
	}
}
