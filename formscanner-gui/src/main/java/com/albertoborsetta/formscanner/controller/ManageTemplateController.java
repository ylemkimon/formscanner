package com.albertoborsetta.formscanner.controller;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JComboBox;
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
import com.albertoborsetta.formscanner.model.FormScannerModel;
import com.albertoborsetta.formscanner.gui.ManageTemplateFrame;

public class ManageTemplateController implements ActionListener,
        ChangeListener, ItemListener, TableModelListener, ListSelectionListener, FocusListener, KeyListener {

    private final FormScannerModel formScannerModel;
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
                view.setupNextTab(FormScannerConstants.CONFIRM);
                break;
            case REMOVE_FIELD:
                String fieldName = view.getSelectedField();
                String groupName = view.getSelectedGroup();
                formScannerModel.removeField(groupName, fieldName);
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

    @Override
    public void stateChanged(ChangeEvent e) {
        view.setAdvanceable();
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
    	Action act = Action.valueOf(((JComboBox<?>) e.getSource()).getActionCommand());
		switch (act) {
			case GROUP:
				view.addItem();
				break;
		default:
			break;
		}
        view.setAdvanceable();
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        view.setAdvanceable();
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        view.enableRemoveFields();
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
