package com.albertoborsetta.formscanner.controller;

import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import com.albertoborsetta.formscanner.model.FormScannerModel;
import com.albertoborsetta.formscanner.gui.InternalFrame;

public class InternalFrameController implements InternalFrameListener {

    private final FormScannerModel model;
    private static InternalFrameController instance;

    public static InternalFrameController getInstance(FormScannerModel model) {
        if (instance == null) {
            instance = new InternalFrameController(model);
        }
        return instance;
    }

    private InternalFrameController(FormScannerModel model) {
        this.model = model;
    }

    // InternalFrameListener
    @Override
    public void internalFrameOpened(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameClosing(InternalFrameEvent e) {
        model.disposeRelatedFrame((InternalFrame) e.getInternalFrame());
    }

    @Override
    public void internalFrameClosed(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameIconified(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameDeiconified(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameActivated(InternalFrameEvent e) {
    }

    @Override
    public void internalFrameDeactivated(InternalFrameEvent e) {
    }
}
