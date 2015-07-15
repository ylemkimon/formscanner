package com.albertoborsetta.formscanner.gui.builder;

import java.awt.ComponentOrientation;
import java.awt.event.FocusListener;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

import com.albertoborsetta.formscanner.commons.FormScannerFont;

public class TextFieldBuilder {

    private final JTextField textField;

    public TextFieldBuilder(int columns, ComponentOrientation orientation) {
        textField = new JTextField(columns);
        textField.setFont(FormScannerFont.getFont());
        textField.setEditable(true);
        textField.setComponentOrientation(orientation);
    }

    public TextFieldBuilder withActionListener(KeyListener listener) {
        textField.addKeyListener(listener);
        return this;
    }

    public TextFieldBuilder setEditable(boolean editable) {
        textField.setEditable(editable);
        return this;
    }

    public JTextField build() {
        return textField;
    }

    public TextFieldBuilder withFocusListener(
            FocusListener listener) {
        textField.addFocusListener(listener);
        return this;
    }

	public TextFieldBuilder setText(String text) {
		textField.setText(text);
		return this;
	}
}
