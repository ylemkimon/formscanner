package com.albertoborsetta.formscanner.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.albertoborsetta.formscanner.model.FormScannerModel;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.Action;
import com.albertoborsetta.formscanner.gui.OptionsPanel;

public class OptionsPanelController implements ItemListener, ChangeListener, ActionListener {

	private static OptionsPanelController instance;
	private final FormScannerModel formScannerModel;
	private OptionsPanel dataPanel;

	public static OptionsPanelController getInstance(FormScannerModel model) {
		if (instance == null) {
			instance = new OptionsPanelController(model);
		}
		return instance;
	}

	private OptionsPanelController(FormScannerModel formScannerModel) {
		this.formScannerModel = formScannerModel;
	}

	public void add(OptionsPanel leftPanel) {
		this.dataPanel = leftPanel;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		dataPanel.verifySpinnerValues();
		formScannerModel.saveScanningOptions(dataPanel);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		formScannerModel.saveScanningOptions(dataPanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Action act = Action.valueOf(e.getActionCommand());
		switch (act) {
			case TOGGLE_LEFT_PANEL:
				dataPanel.togglePanel();
				break;
			default:
				break;
		}
	}
}
