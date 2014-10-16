package org.albertoborsetta.formscanner.gui.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Action;
import org.albertoborsetta.formscanner.gui.ManageTemplateFrame;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class ManageTemplateController implements ActionListener,
		ChangeListener, ItemListener, TableModelListener {

	private FormScannerModel formScannerModel;
	private ManageTemplateFrame view;

	public ManageTemplateController(FormScannerModel model) {
		this.formScannerModel = model;
	}

	public void add(ManageTemplateFrame view) {
		this.view = view;
	}

	public void actionPerformed(ActionEvent e) {
		Action act = Action.valueOf(e.getActionCommand());
		switch (act) {
		case ADD_FIELD:
			formScannerModel.setNextTab(FormScannerConstants.CONFIRM, view);
			break;
		case REMOVE_FIELD:
			formScannerModel.removeField(view);
			break;
		case SAVE_TEMPLATE:
			formScannerModel.saveTemplate(view);
			break;
		case CONFIRM:
			formScannerModel.setNextTab(FormScannerConstants.CONFIRM, view);
			break;
		case CANCEL:
			formScannerModel.setNextTab(FormScannerConstants.CANCEL, view);
			break;
		case IS_MULTIPLE:
			formScannerModel.enableRejectIfNotMultiple(view)
		default:
			break;
		}

	}

	public void stateChanged(ChangeEvent e) {
		formScannerModel.setAdvanceable(view);
	}

	public void itemStateChanged(ItemEvent e) {
		formScannerModel.setAdvanceable(view);
	}

	public void tableChanged(TableModelEvent e) {
		formScannerModel.setAdvanceable(view);
	}

	public void valueChanged(ListSelectionEvent e) {
		formScannerModel.enableRemoveFields(view);
	}
}
