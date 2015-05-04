package com.albertoborsetta.formscanner.gui.builder;

import java.awt.ComponentOrientation;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import com.albertoborsetta.formscanner.commons.FormScannerFont;

public class ButtonBuilder {

    private final JButton button;
    private final ComponentOrientation orientation;

    public ButtonBuilder(ComponentOrientation orientation) {
        button = new JButton();
        button.setFont(FormScannerFont.getFont());
        button.setComponentOrientation(orientation);
        this.orientation = orientation;
    }

    public ButtonBuilder withActionListener(ActionListener listener) {
        button.addActionListener(listener);
        return this;
    }

    public ButtonBuilder withActionCommand(String action) {
        button.setActionCommand(action);
        return this;
    }

    public ButtonBuilder withToolTip(String tooltip) {
        button.setToolTipText(tooltip);
        return this;
    }

    public ButtonBuilder withIcon(ImageIcon icon) {
        button.setIcon(icon);
        return this;
    }

    public ButtonBuilder withSelectedIcon(ImageIcon icon) {
        button.setSelectedIcon(icon);
        return this;
    }

    public ButtonBuilder withText(String text) {
        button.setText(text);
        return this;
    }

    public ButtonBuilder setEnabled(boolean enabled) {
        button.setEnabled(enabled);
        return this;
    }

    public ButtonBuilder setSelected(boolean selected) {
        button.setSelected(selected);
        return this;
    }

    public ButtonBuilder setBorder(EtchedBorder border) {
        button.setBorder(border);
        return this;
    }

    public JButton build() {
        return button;
    }

    public ButtonBuilder setAlignment() {
        if (orientation.isLeftToRight()) {
            button.setHorizontalAlignment(SwingConstants.LEFT);
        } else {
            button.setHorizontalAlignment(SwingConstants.RIGHT);
        }
        return this;
    }

}
