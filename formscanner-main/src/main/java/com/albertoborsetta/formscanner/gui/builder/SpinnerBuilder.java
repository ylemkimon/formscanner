package com.albertoborsetta.formscanner.gui.builder;

import javax.swing.JSpinner;
import javax.swing.event.ChangeListener;

import com.albertoborsetta.formscanner.commons.FormScannerFont;

public class SpinnerBuilder {
	
	private JSpinner spinner;
	
	public SpinnerBuilder(String name) {
		spinner = new JSpinner();
		spinner.setName(name);
		spinner.setFont(FormScannerFont.getFont());
	}
	
	public SpinnerBuilder withActionListener(ChangeListener listener) {
		spinner.addChangeListener(listener);
		return this;
	}
	
	public SpinnerBuilder withStartValue(int value) {
		spinner.setValue(value);
		return this;
	}
	
	public JSpinner build() {
		return spinner;
	}
}