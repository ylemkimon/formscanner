package com.albertoborsetta.formscanner.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JFormattedTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.albertoborsetta.formscanner.commons.FormScannerConstants.Action;
import com.albertoborsetta.formscanner.model.FormScannerModel;
import com.albertoborsetta.formscanner.gui.OptionsFrame;

public class OptionsFrameController
		implements ActionListener, ChangeListener, ItemListener, FocusListener {

	private final FormScannerModel formScannerModel;
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
		case GROUPS_ENABLED:
			optionsFrame.enableGroups();
		case RESET_AUTO_NUMBERING:
			optionsFrame.setAdvanceable();
			break;
		case BARCODE:
		case QUESTION:
		case GROUP:
			optionsFrame.addItem(e.getActionCommand());
			break;
		default:
			break;
		}
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		optionsFrame.setAdvanceable();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		optionsFrame.setAdvanceable();
	}

	@Override
	public void focusGained(FocusEvent e) {
		Component c = e.getComponent();
		if (c instanceof JFormattedTextField) {
			final JFormattedTextField textField = (JFormattedTextField) c;
			SwingUtilities.invokeLater(new Runnable() {

				@Override
				public void run() {
					textField.selectAll();
				}
			});
		}
	}

	@Override
	public void focusLost(FocusEvent e) {
		optionsFrame.setAdvanceable();
	}

}
