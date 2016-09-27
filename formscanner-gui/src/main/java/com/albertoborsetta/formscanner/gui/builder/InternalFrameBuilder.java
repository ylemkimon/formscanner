package com.albertoborsetta.formscanner.gui.builder;

import java.awt.ComponentOrientation;

import javax.swing.JInternalFrame;

import com.albertoborsetta.formscanner.commons.FormScannerFont;

public class InternalFrameBuilder {

    private final JInternalFrame internalFrame;

    public InternalFrameBuilder(ComponentOrientation orientation) {
        internalFrame = new JInternalFrame();
        internalFrame.setFont(FormScannerFont.getFont());
        internalFrame.setComponentOrientation(orientation);
    }

    public JInternalFrame build() {
        return internalFrame;
    }
}
