package com.albertoborsetta.formscanner.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;

import com.albertoborsetta.formscanner.commons.FormScannerConstants.Action;
import com.albertoborsetta.formscanner.model.FormScannerModel;
import com.albertoborsetta.formscanner.gui.AboutFrame;

public class AboutFrameController implements ActionListener, HyperlinkListener {

    private final FormScannerModel formScannerModel;
    private AboutFrame aboutFrame;

    public AboutFrameController(FormScannerModel formScannerModel) {
        this.formScannerModel = formScannerModel;
    }

    public void add(AboutFrame aboutFrame) {
        this.aboutFrame = aboutFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Action act = Action.valueOf(e.getActionCommand());
        switch (act) {
            case CONFIRM:
                aboutFrame.dispose();
                break;
            default:
                break;
        }
    }

    @Override
    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (HyperlinkEvent.EventType.ACTIVATED.equals(e.getEventType())) {
            formScannerModel.linkToHelp(e.getURL());
        }
    }
}