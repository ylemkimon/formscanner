package org.albertoborsetta.formscanner.gui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.albertoborsetta.formscanner.commons.FormScannerConstants.Action;
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
		Action act = Action.valueOf(e.getActionCommand());
		switch (act) {
		case ADD_FIELD:
			formScannerModel.setNextTab(view);
			break;
		case REMOVE_FIELD:
			break;
		case SAVE_TEMPLATE:
			break;
		case CONFIRM:
			formScannerModel.setNextTab(view);
			break;
		case CANCEL:
			formScannerModel.setPrevTab(view);
			break;
		default:
			break;
		}
		
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		formScannerModel.setAdvanceable(view);
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		formScannerModel.setAdvanceable(view);		
	}
}
