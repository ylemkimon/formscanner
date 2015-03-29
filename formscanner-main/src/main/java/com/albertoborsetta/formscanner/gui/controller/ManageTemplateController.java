package com.albertoborsetta.formscanner.gui.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFormattedTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;

import com.albertoborsetta.formscanner.commons.FormScannerConstants;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.Action;
import com.albertoborsetta.formscanner.gui.model.FormScannerModel;
import com.albertoborsetta.formscanner.gui.view.ManageTemplateFrame;

public class ManageTemplateController implements ActionListener,
		ChangeListener, ItemListener, TableModelListener, ListSelectionListener, FocusListener, KeyListener {

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
			view.setupNextTab(FormScannerConstants.CONFIRM);
			break;
		case REMOVE_FIELD:
			String fieldName = view.getSelectedItem();
			formScannerModel.removeField(fieldName);
			view.removeSelectedField();
			break;
		case SAVE_TEMPLATE:
			formScannerModel.saveTemplate(true);
			view.dispose();
			break;
		case CONFIRM:
			view.setupNextTab(FormScannerConstants.CONFIRM);
			break;
		case CANCEL:
			view.setupNextTab(FormScannerConstants.CANCEL);
			break;
		case IS_MULTIPLE:
			view.enableRejectMultiple(!view.getIsMultiple());
			break;
		default:
			break;
		}

	}

	public void stateChanged(ChangeEvent e) {
		view.setAdvanceable();
	}

	public void itemStateChanged(ItemEvent e) {
		view.setAdvanceable();
	}

	public void tableChanged(TableModelEvent e) {
		view.setAdvanceable();
	}

	public void valueChanged(ListSelectionEvent e) {
		view.enableRemoveFields();
	}

	public void focusGained(FocusEvent e) {
		Component c = e.getComponent();
        if (c instanceof JFormattedTextField) {
          final JFormattedTextField textField = (JFormattedTextField) c;
          SwingUtilities.invokeLater(new Runnable() {
            public void run() {
              textField.selectAll();
            }
          });
        }
	}

	public void focusLost(FocusEvent e) {
		view.setAdvanceable();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		view.setAdvanceable();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		view.setAdvanceable();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		view.setAdvanceable();
	}
}
