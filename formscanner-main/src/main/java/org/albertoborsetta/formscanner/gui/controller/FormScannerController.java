package org.albertoborsetta.formscanner.gui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.albertoborsetta.formscanner.commons.FileOpener;
import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Actions;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class FormScannerController implements ActionListener {
	
	private FormScannerModel model;
	private static FormScannerController instance;
	
	public static FormScannerController getInstance(FormScannerModel model) {
		if (instance == null) {
			instance = new FormScannerController(model); 
		}
		return instance;
	}
	
	private FormScannerController(FormScannerModel model) {
		this.model = model;
	}
	
	public void actionPerformed(ActionEvent e) {		
		Actions act = Actions.valueOf(e.getActionCommand());
		FileOpener fileOpener = FileOpener.getInstance();
		switch (act) {
		case RENAME_FILE_FIRST:
			model.renameFiles(FormScannerConstants.RENAME_FILE_FIRST);
			break;
		case OPEN_IMAGES:
			model.openFiles(fileOpener.chooseImages());
			break;
		case SAVE_RESULTS:
			break;
		case ANALYZE_FILE_FIRST:
			model.analyzeFiles(FormScannerConstants.ANALYZE_FILE_FIRST);
			break;
		case LOAD_TEMPLATE:
			model.loadTemplate(fileOpener.chooseImage());
			break;
		default:
			break;
		}
	}

}
