package org.albertoborsetta.formscanner.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.albertoborsetta.formscanner.commons.Constants;
import org.albertoborsetta.formscanner.commons.Constants.Actions;
import org.albertoborsetta.formscanner.gui.font.FormScannerFont;
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
			model.renameFiles(Constants.RENAME_FILE_FIRST);
			break;
		case OPEN_IMAGES:
			model.openFiles(choose());
			break;
		case SAVE_RESULTS:
			System.out.println(e.getActionCommand());
		default:
			break;
		}
	}
	
	private static File[] choose() {
		  
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFont(FormScannerFont.getFont());
		fileChooser.setMultiSelectionEnabled(true);
		FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("Image files", "tif", "tiff");
		fileChooser.setFileFilter(imageFilter);
		fileChooser.showOpenDialog(null);
		
		return fileChooser.getSelectedFiles(); 
	}

}
