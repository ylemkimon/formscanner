package org.albertoborsetta.formscanner.gui.builder;

import javax.swing.JLabel;
import javax.swing.border.Border;

import org.albertoborsetta.formscanner.commons.FormScannerFont;

public class LabelBuilder {
	
	private JLabel label;
	
	public LabelBuilder() {
		label = new JLabel();
		label.setFont(FormScannerFont.getFont());
	}
	
	public LabelBuilder(String text) {
		label = new JLabel(text);
		label.setFont(FormScannerFont.getFont());
	}
	
	public LabelBuilder withBorder(Border border) {
		label.setBorder(border);
		return this;
	}
	
	public JLabel build() {
		return label;
	}
}
