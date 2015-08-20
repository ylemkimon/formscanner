package com.albertoborsetta.formscanner.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JRadioButtonMenuItem;

import com.albertoborsetta.formscanner.commons.FormScannerConstants;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.Action;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.Frame;
import com.albertoborsetta.formscanner.model.FormScannerModel;

public class FormScannerController implements ActionListener, WindowListener {

    private final FormScannerModel model;
    private static FormScannerController instance;

    public static FormScannerController getInstance(FormScannerModel model) {
        if (instance == null) {
            instance = new FormScannerController(model);
        }
        return instance;
    }

    private FormScannerController(FormScannerModel model) {
        this.model = model;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Action act = Action.valueOf(e.getActionCommand());
        switch (act) {
            case RENAME_FILES_FIRST:
                model.renameFiles(FormScannerConstants.RENAME_FILES_FIRST);
                break;
            case OPEN_IMAGES:
                model.openImages();
                break;
            case SAVE_RESULTS:
                break;
            case ANALYZE_FILES_ALL:
                model.analyzeFiles(FormScannerConstants.ANALYZE_FILES_ALL);
                break;
            case ANALYZE_FILES_FIRST:
                model.analyzeFiles(FormScannerConstants.ANALYZE_FILES_FIRST);
                break;
            case ANALYZE_FILES_CURRENT:
                model.analyzeFiles(FormScannerConstants.ANALYZE_FILES_CURRENT);
                break;
            case LOAD_TEMPLATE:
                model.loadTemplate();
                break;
            case USE_TEMPLATE:
                model.openTemplate();
                break;
            case EXIT:
                model.exitFormScanner();
                break;
            case HELP:
                try {
                    model.linkToHelp(new URL(FormScannerConstants.WIKI_PAGE));
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                }
                break;
            case ABOUT:
                model.showAboutFrame();
                break;
            case LANGUAGE:
                JRadioButtonMenuItem object = (JRadioButtonMenuItem) e.getSource();
                model.setLanguage(object.getName());
                break;
            case OPTIONS:
                model.showOptionsFrame();
                break;
            default:
                break;
        }
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        model.setLastPosition(Frame.DESKTOP_FRAME, e.getWindow().getBounds());
        e.getWindow().dispose();
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }
}
