package com.albertoborsetta.formscanner.gui.builder;

import java.awt.ComponentOrientation;

import javax.swing.JComponent;
import javax.swing.JToolBar;

import com.albertoborsetta.formscanner.commons.FormScannerFont;

public class ToolBarBuilder {

    private final JToolBar toolBar;

    public ToolBarBuilder(ComponentOrientation orientation) {
        toolBar = new JToolBar();
        toolBar.setFont(FormScannerFont.getFont());
        toolBar.setComponentOrientation(orientation);
    }

    public ToolBarBuilder withAlignmentX(float alignment) {
        toolBar.setAlignmentX(alignment);
        return this;
    }

    public ToolBarBuilder withAlignmentY(float alignment) {
        toolBar.setAlignmentX(alignment);
        return this;
    }

    public ToolBarBuilder add(JComponent component) {
        toolBar.add(component);
        return this;
    }

    public JToolBar build() {
        return toolBar;
    }
}
