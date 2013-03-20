package org.albertoborsetta.formscanner.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.FormScannerFont;
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
		switch (act) {
		case RENAME_FILE_FIRST:
			model.renameFiles(FormScannerConstants.RENAME_FILE_FIRST);
			break;
		case OPEN_IMAGES:
			model.openFiles(chooseImages(true));
			break;
		case SAVE_RESULTS:
			break;
		case ANALYZE_FILE_FIRST:
			model.analyzeFiles(FormScannerConstants.ANALYZE_FILE_FIRST);
		case LOAD_TEMPLATE:
			model.loadTemplate(chooseImages(false));
		default:
			break;
		}
	}
	
	private static File[] chooseImages(boolean multiple) {
		  
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFont(FormScannerFont.getFont());
		fileChooser.setMultiSelectionEnabled(multiple);
		FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("Image files", "jpg", "jpeg", "tif", "tiff");
		fileChooser.setFileFilter(imageFilter);
		fileChooser.showOpenDialog(null);
		
		return fileChooser.getSelectedFiles(); 
	}

}
