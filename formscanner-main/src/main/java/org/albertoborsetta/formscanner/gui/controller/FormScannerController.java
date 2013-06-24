package org.albertoborsetta.formscanner.gui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Action;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class FormScannerController implements ActionListener, WindowListener {
	
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
		Action act = Action.valueOf(e.getActionCommand());
		switch (act) {
		case RENAME_FILE_FIRST:
			model.renameFiles(FormScannerConstants.RENAME_FILE_FIRST);
			break;
		case OPEN_IMAGES:
			model.openImages();
			break;
		case SAVE_RESULTS:
			break;
		case ANALYZE_FILE_FIRST:
			model.analyzeFiles(FormScannerConstants.ANALYZE_FILE_FIRST);
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
		default:
			break;
		}
	}

	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowClosing(WindowEvent e) {
		e.getWindow().dispose();
		System.exit(0);
	}

	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

}
