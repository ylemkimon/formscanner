/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albertoborsetta.formscanner.gui;

import com.albertoborsetta.formscanner.commons.FormScannerConstants;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import com.albertoborsetta.formscanner.controller.FormScannerController;
import com.albertoborsetta.formscanner.model.FormScannerModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import static javax.swing.JFrame.setDefaultLookAndFeelDecorated;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author aborsetta
 */
public class FormScannerDesktop extends JFrame {
    
    private final FormScannerModel model;
    private final FormScannerController mainFrameController;
    private final MenuBar menuBar;
    private final ToolBar toolBar;
    private final JDesktopPane desktopPane;
    
    /**
     * Create the application.
     */
    public FormScannerDesktop() {
        model = new FormScannerModel(this);
        mainFrameController = FormScannerController.getInstance(model);
        addWindowListener(mainFrameController);

        setName(FormScannerConstants.Frame.DESKTOP_FRAME.name());

        setTitle(StringUtils.replace(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.FORMSCANNER_MAIN_TITLE), FormScannerConstants.VERSION_KEY, FormScannerConstants.VERSION));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        getContentPane().setLayout(new BorderLayout(0, 0));
        menuBar = new MenuBar(model);
        setJMenuBar(menuBar);

        toolBar = new ToolBar(model);
        getContentPane().add(toolBar, BorderLayout.NORTH);

        desktopPane = new JDesktopPane();
        getContentPane().add(desktopPane, BorderLayout.CENTER);

        model.setDefaultFramePositions();
        setBounds(model.getLastPosition(FormScannerConstants.Frame.DESKTOP_FRAME));
        setVisible(true);
        setDefaultLookAndFeelDecorated(true);
        setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
    }

    public void arrangeFrame(InternalFrame frame) {
        boolean found = false;

        for (Component component : desktopPane.getComponents()) {
            if (frame.getName().equals(component.getName())) {
                component.setVisible(false);
                desktopPane.remove(component);
                found = true;
                break;
            }
        }

        if (!found) {
            desktopPane.add(frame);
            frame.setVisible(true);
        } else {
            arrangeFrame(frame);
        }
    }

    public void disposeFrame(InternalFrame frame) {
        if (frame != null) {
            model.setLastPosition(FormScannerConstants.Frame.valueOf(frame.getName()), frame.getBounds());
            frame.dispose();
        }
        setDefaultControllersEnabled();
        model.resetFirstPass();
    }

    public void setDefaultControllersEnabled() {
        toolBar.setRenameControllersEnabled(true);
        toolBar.setScanControllersEnabled(true);
        toolBar.setScanAllControllersEnabled(true);
        toolBar.setScanCurrentControllersEnabled(false);
        menuBar.setRenameControllersEnabled(true);
        menuBar.setScanControllersEnabled(true);
        menuBar.setScanAllControllersEnabled(true);
    }

    public void setRenameControllersEnabled(boolean enable) {
        toolBar.setRenameControllersEnabled(enable);
        menuBar.setRenameControllersEnabled(enable);
    }

    public void setScanControllersEnabled(boolean enable) {
        toolBar.setScanControllersEnabled(enable);
        menuBar.setScanControllersEnabled(enable);
    }

    public void setScanAllControllersEnabled(boolean enable) {
        toolBar.setScanAllControllersEnabled(enable);
        menuBar.setScanAllControllersEnabled(enable);
    }

    public void setScanCurrentControllersEnabled(boolean enable) {
        toolBar.setScanCurrentControllersEnabled(enable);
    }
    
}
