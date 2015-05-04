package com.albertoborsetta.formscanner.gui.builder;

import java.awt.ComponentOrientation;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import com.albertoborsetta.formscanner.commons.FormScannerFont;

public class ScrollPaneBuilder {

    private final JScrollPane scrollPane;

    public ScrollPaneBuilder(JComponent component, ComponentOrientation orientation) {
        scrollPane = new JScrollPane(component);
        scrollPane.setFont(FormScannerFont.getFont());
        scrollPane.setComponentOrientation(orientation);
    }

    public JScrollPane build() {
        return scrollPane;
    }
}
