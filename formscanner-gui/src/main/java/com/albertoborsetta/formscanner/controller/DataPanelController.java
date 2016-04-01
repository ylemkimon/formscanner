package com.albertoborsetta.formscanner.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.albertoborsetta.formscanner.commons.FormScannerConstants.Action;
import com.albertoborsetta.formscanner.gui.DataPanel;
import com.albertoborsetta.formscanner.model.FormScannerModel;

public class DataPanelController implements ActionListener, ListSelectionListener {

	private static DataPanelController instance;
	private FormScannerModel formScannerModel;
	private DataPanel bottomPanel;

	private DataPanelController(FormScannerModel formScannerModel) {
		this.formScannerModel = formScannerModel;
	}

	public static DataPanelController getInstance(FormScannerModel formScannerModel) {
		if (instance == null) {
			instance = new DataPanelController(formScannerModel);
		}
		return instance;
	}

	public void add(DataPanel bottomPanel) {
			this.bottomPanel = bottomPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Action act = Action.valueOf(e.getActionCommand());
		switch (act) {
		case OPEN_IMAGES:
			formScannerModel.openImages();
			bottomPanel.isTemplateLoaded(formScannerModel.getTemplate() != null);
			bottomPanel.setupImagesTable();
			break;
		case TOGGLE_BOTTOM_PANEL:
			bottomPanel.togglePanel();
			break;
		case ADD_FIELD:
			formScannerModel.addField();
			break;
		case REMOVE_FIELD:
			String fieldName = bottomPanel.getSelectedField();
			String groupName = bottomPanel.getSelectedGroup();
			formScannerModel.removeField(groupName, fieldName);
			bottomPanel.removeSelectedField();
			break;
		case SAVE_TEMPLATE:
			formScannerModel.saveTemplate(true);
			bottomPanel.setTemplateData();
			bottomPanel.isTemplateLoaded(true);
			bottomPanel.setupFieldsTable();
			bottomPanel.setupImagesTable();
			break;
		case LOAD_TEMPLATE:
			formScannerModel.loadTemplate();
			bottomPanel.isTemplateLoaded(true);
			bottomPanel.setupFieldsTable();
//			bottomPanel.enableEditTemplate();
			break;
		case USE_TEMPLATE:
			formScannerModel.openTemplate();
			bottomPanel.setTemplateData();
			bottomPanel.isTemplateLoaded(true);
			bottomPanel.setupFieldsTable();
			bottomPanel.setupImagesTable();
//			bottomPanel.enableEditTemplate();
			break;
		case CLEAR_TEMPLATE:
			formScannerModel.removeAllFields();
			bottomPanel.removeAllFields();
			break;
		case OPEN_TEMPLATE_IMAGE:
			formScannerModel.loadTemplateImage();
			break;
		default:
			break;
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
//		bottomPanel.enableRemoveFields();
	}
}
