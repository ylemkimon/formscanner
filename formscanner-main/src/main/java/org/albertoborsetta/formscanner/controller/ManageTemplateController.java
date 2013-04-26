package org.albertoborsetta.formscanner.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.albertoborsetta.formscanner.commons.FormScannerConstants.Actions;
import org.albertoborsetta.formscanner.gui.ManageTemplateFrame;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class ManageTemplateController implements ActionListener, ChangeListener, ItemListener {
	
	private FormScannerModel formScannerModel;
	private ManageTemplateFrame view;
	
	public ManageTemplateController(FormScannerModel model) {
		this.formScannerModel = model;
	}
	
	public void add(ManageTemplateFrame view) {
		this.view = view;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Actions act = Actions.valueOf(e.getActionCommand());
		switch (act) {
		case ADD_FIELD:
			formScannerModel.setNextTab();
			break;
		case REMOVE_FIELD:
			break;
		case SAVE_TEMPLATE:
			break;
		case CONFIRM:
			formScannerModel.setNextTab();
			break;
		case CANCEL:
			formScannerModel.setPrevTab();
			break;
		default:
			break;
		}
		
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		formScannerModel.setAdvanceable();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		formScannerModel.setAdvanceable();		
	}
}
