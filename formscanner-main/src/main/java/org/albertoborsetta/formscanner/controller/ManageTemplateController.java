package org.albertoborsetta.formscanner.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Actions;
import org.albertoborsetta.formscanner.gui.ManageTemplateFrame;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class ManageTemplateController implements ActionListener, ChangeListener, ItemListener {
	
	private FormScannerModel model;
	private ManageTemplateFrame view;
	
	public ManageTemplateController(FormScannerModel model) {
		this.model = model;
	}
	
	public void add(ManageTemplateFrame view) {
		this.view = view;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Actions act = Actions.valueOf(e.getActionCommand());
		switch (act) {
		case ADD_FIELD:
			view.enablePropertiesPanel(true);
			view.setPropertiesPanel();
			break;
		case REMOVE_FIELD:
			break;
		case SAVE_TEMPLATE:
			break;
		case CONFIRM:
			break;
		case CANCEL:
			break;
		default:
			break;
		}
		
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		// TODO Auto-generated method stub
		
	}

}
