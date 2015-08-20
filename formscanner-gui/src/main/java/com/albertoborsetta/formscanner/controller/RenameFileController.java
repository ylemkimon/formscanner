package com.albertoborsetta.formscanner.controller;

import com.albertoborsetta.formscanner.commons.FormScannerConstants;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.Action;
import com.albertoborsetta.formscanner.model.FormScannerModel;
import com.albertoborsetta.formscanner.gui.RenameFileFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

public class RenameFileController implements KeyListener, ActionListener, FocusListener {

    private final FormScannerModel model;
    private RenameFileFrame view;

    public RenameFileController(FormScannerModel model) {
        this.model = model;
    }

    public void add(RenameFileFrame view) {
        this.view = view;
    }

    // KeyListener	
    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if ((e.getKeyCode() == KeyEvent.VK_ENTER) && (view.isOkEnabled())) {
            view.setOkEnabled(false);
            model.renameFiles(FormScannerConstants.RENAME_FILES_CURRENT);
        } else if ((e.getKeyCode() == KeyEvent.VK_ENTER) && (!view.isOkEnabled())) {
            model.renameFiles(FormScannerConstants.RENAME_FILES_SKIP);
        } else {
            view.setOkEnabled(true);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    // ActionListener
    @Override
    public void actionPerformed(ActionEvent e) {
        Action act = Action.valueOf(e.getActionCommand());
        switch (act) {
            case RENAME_FILES_CURRENT:
                model.renameFiles(FormScannerConstants.RENAME_FILES_CURRENT);
                break;
            case RENAME_FILES_SKIP:
                model.renameFiles(FormScannerConstants.RENAME_FILES_SKIP);
                break;
            default:
                break;
        }
    }

    @Override
    public void focusGained(FocusEvent e) {
        ((JTextField) e.getComponent()).selectAll();

    }

    @Override
    public void focusLost(FocusEvent e) {
        view.setOkEnabled(true);
    }
}