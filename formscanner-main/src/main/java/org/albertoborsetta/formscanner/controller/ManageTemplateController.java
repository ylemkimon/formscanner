package org.albertoborsetta.formscanner.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.albertoborsetta.formscanner.commons.FormScannerConstants.Actions;
import org.albertoborsetta.formscanner.gui.ManageTemplateFrame;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class ManageTemplateController implements ActionListener, ChangeListener, ItemListener {
	
	private FormScannerModel formScanneModel;
	private ManageTemplateFrame view;
	
	public ManageTemplateController(FormScannerModel model) {
		this.formScanneModel = model;
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
			switch (view.getCurrentTabbedPaneIndex()) {
			case 1:
				view.setupTable();
				view.enablePositionPanel(true);
				view.setPositionPanel();
				break;
			case 2:
				view.enableTemplateListPanel(true);
				view.setTemplateListPanel();
				view.selectField(0);
				break;
			default:
				break;
			}
			break;
		case CANCEL:
			switch (view.getCurrentTabbedPaneIndex()) {
			case 1:
				view.resetSelectedValues();
				view.enableTemplateListPanel(true);
				view.setTemplateListPanel();
				view.selectField(0);
				break;
			case 2:
				view.resetTable();
				view.enablePositionPanel(false);
				view.enablePropertiesPanel(true);
				view.setPropertiesPanel();
				break;
			default:
				view.dispose();
				break;
			}
			break;
		default:
			break;
		}
		
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		view.setAdvancement(view.verifyAdvancement());
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		view.setAdvancement(view.verifyAdvancement());		
	}

}
