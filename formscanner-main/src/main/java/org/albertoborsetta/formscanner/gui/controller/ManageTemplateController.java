package org.albertoborsetta.formscanner.gui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JInternalFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.albertoborsetta.formscanner.commons.FormScannerConstants.Actions;
import org.albertoborsetta.formscanner.gui.ManageTemplateFrame;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class ManageTemplateController implements Controller, ActionListener, ChangeListener, ItemListener {
	
	private FormScannerModel formScannerModel;
	private ManageTemplateFrame view;
	
	public ManageTemplateController(FormScannerModel model) {
		this.formScannerModel = model;
	}
	
	@Override
	public void add(JInternalFrame view) {
		this.view = (ManageTemplateFrame) view;		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Actions act = Actions.valueOf(e.getActionCommand());
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
