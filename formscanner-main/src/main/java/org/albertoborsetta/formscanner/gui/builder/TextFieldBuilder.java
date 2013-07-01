package org.albertoborsetta.formscanner.gui.builder;

import java.awt.event.KeyListener;

import javax.swing.JTextField;

import org.albertoborsetta.formscanner.commons.FormScannerFont;

public class TextFieldBuilder {
	
	private JTextField textField;
	
	public TextFieldBuilder(int columns) {
		textField = new JTextField(columns);
		textField.setFont(FormScannerFont.getFont());
	}
	
	public TextFieldBuilder withKeyListener(KeyListener listener) {
		textField.addKeyListener(listener);
		return this;
	} 
	
	public JTextField build() {
		return textField;
	}
}