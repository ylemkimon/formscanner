package org.albertoborsetta.formscanner.gui.builder;

import java.awt.event.KeyListener;

import javax.swing.JTextField;

import org.albertoborsetta.formscanner.commons.FormScannerFont;

public class TextFieldBuilder {
	
	private JTextField textField;
	
	public TextFieldBuilder(int columns) {
		textField = new JTextField(columns);
		textField.setFont(FormScannerFont.getFont());
		textField.setEditable(true);
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
}