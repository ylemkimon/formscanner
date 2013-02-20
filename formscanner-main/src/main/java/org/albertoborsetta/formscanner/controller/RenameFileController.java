package org.albertoborsetta.formscanner.controller;

import org.albertoborsetta.formscanner.commons.Constants;
import org.albertoborsetta.formscanner.commons.Constants.Actions;
import org.albertoborsetta.formscanner.gui.RenameFileFrame;
import org.albertoborsetta.formscanner.model.FormScannerModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class RenameFileController implements KeyListener, ActionListener {
	
	private FormScannerModel model;
	private RenameFileFrame view;
	
	public RenameFileController(FormScannerModel model) {
		this.model = model;
	}
	
	public void add(RenameFileFrame view) {
		this.view = view;
	}

	// KeyListener	
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	public void keyPressed(KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_ENTER) && (view.isOkEnabled())) {
			view.setOkEnabled(false);
			model.renameFiles(Constants.RENAME_FILE_CURRENT);
		} else if ((e.getKeyCode() == KeyEvent.VK_ENTER) && (!view.isOkEnabled())) {
			model.renameFiles(Constants.RENAME_FILE_SKIP);
		} else {
			view.setOkEnabled(true);
		}
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub			
	}

	// ActionListener
	public void actionPerformed(ActionEvent e) {
		Actions act = Actions.valueOf(e.getActionCommand());
		switch (act) {
		case RENAME_FILE_CURRENT:
			model.renameFiles(Constants.RENAME_FILE_CURRENT);
			break;
		case RENAME_FILE_SKIP:
			model.renameFiles(Constants.RENAME_FILE_SKIP);
			break;
		default:
			break;
		}
	}
}