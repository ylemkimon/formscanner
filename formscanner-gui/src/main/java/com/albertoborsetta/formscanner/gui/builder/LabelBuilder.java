package com.albertoborsetta.formscanner.gui.builder;

import java.awt.ComponentOrientation;

import javax.swing.JLabel;
import javax.swing.border.Border;

import com.albertoborsetta.formscanner.commons.FormScannerFont;

public class LabelBuilder {

    private final JLabel label;

    public LabelBuilder(ComponentOrientation orientation) {
        label = new JLabel();
        setCustomParams(orientation);
    }

    public LabelBuilder(String text, ComponentOrientation orientation) {
        label = new JLabel(text);
        setCustomParams(orientation);
    }

    private void setCustomParams(ComponentOrientation orientation) {
        label.setFont(FormScannerFont.getFont());
        label.setComponentOrientation(orientation);
        if (orientation.isLeftToRight()) {
            label.setAlignmentX(JLabel.LEFT_ALIGNMENT);
        } else {
            label.setAlignmentX(JLabel.RIGHT_ALIGNMENT);
        }
    }

    public LabelBuilder withBorder(Border border) {
        label.setBorder(border);
        return this;
    }

    public JLabel build() {
        return label;
    }
}
