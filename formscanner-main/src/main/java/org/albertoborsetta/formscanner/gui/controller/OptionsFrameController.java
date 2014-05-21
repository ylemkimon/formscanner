package org.albertoborsetta.formscanner.gui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.albertoborsetta.formscanner.commons.FormScannerConstants.Action;
import org.albertoborsetta.formscanner.gui.OptionsFrame;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class OptionsFrameController implements ActionListener, ChangeListener, ItemListener {
	
	private FormScannerModel formScannerModel;
	private OptionsFrame optionsFrame;

	public OptionsFrameController(FormScannerModel formScannerModel) {
		this.formScannerModel = formScannerModel;
	}

	public void add(OptionsFrame optionsFrame) {
		 this.optionsFrame = optionsFrame;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Action act = Action.valueOf(e.getActionCommand());
		switch (act) {
		case SAVE_OPTIONS:
			formScannerModel.saveOptions(optionsFrame);
		case CANCEL:
			optionsFrame.dispose();
			break;
		default:
			break;
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		optionsFrame.setSaveEnabled();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		optionsFrame.setSaveEnabled();		
	}

}
