package org.albertoborsetta.formscanner.gui.builder;

import javax.swing.JSpinner;
import javax.swing.event.ChangeListener;

import org.albertoborsetta.formscanner.commons.FormScannerFont;

public class SpinnerBuilder {
	
	private JSpinner spinner;
	
	public SpinnerBuilder(String name) {
		spinner = new JSpinner();
		spinner.setName(name);
		spinner.setFont(FormScannerFont.getFont());
	}
	
	public SpinnerBuilder withChangeListener(ChangeListener listener) {
		spinner.addChangeListener(listener);
		return this;
	}
	
	public JSpinner build() {
		return spinner;
	}
}
