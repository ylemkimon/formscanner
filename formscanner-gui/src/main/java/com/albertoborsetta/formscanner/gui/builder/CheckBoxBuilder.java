package com.albertoborsetta.formscanner.gui.builder;

import java.awt.ComponentOrientation;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import com.albertoborsetta.formscanner.commons.FormScannerFont;

public class CheckBoxBuilder {

    private final JCheckBox chkBox;

    public CheckBoxBuilder(String name, ComponentOrientation orientation) {
        chkBox = new JCheckBox();
        chkBox.setName(name);
        chkBox.setFont(FormScannerFont.getFont());
        chkBox.setComponentOrientation(orientation);
    }

    public CheckBoxBuilder setChecked(boolean checked) {
        chkBox.setSelected(checked);
        return this;
    }

    public CheckBoxBuilder setEnabled(boolean enabled) {
        chkBox.setEnabled(enabled);
        return this;
    }

    public CheckBoxBuilder withActionListener(ActionListener listener) {
        chkBox.addActionListener(listener);
        return this;
    }

    public JCheckBox build() {
        return chkBox;
    }

    public CheckBoxBuilder withActionCommand(String action) {
        chkBox.setActionCommand(action);
        return this;
    }
}
