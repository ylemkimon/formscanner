package com.albertoborsetta.formscanner.gui.builder;

import java.awt.ComponentOrientation;

import javax.swing.ImageIcon;
import javax.swing.border.Border;

import org.jdesktop.swingx.JXLabel;

import com.albertoborsetta.formscanner.commons.FormScannerFont;

public class LabelBuilder {

	private final JXLabel label;
	private Integer fontSize;

	public LabelBuilder(ComponentOrientation orientation) {
		label = new JXLabel();
		setCustomParams(orientation);
	}

	public LabelBuilder(String text, ComponentOrientation orientation) {
		label = new JXLabel(text);
		setCustomParams(orientation);
	}

	private void setCustomParams(ComponentOrientation orientation) {
		label.setComponentOrientation(orientation);
		if (orientation.isLeftToRight()) {
			label.setAlignmentX(JXLabel.LEFT_ALIGNMENT);
		} else {
			label.setAlignmentX(JXLabel.RIGHT_ALIGNMENT);
		}
	}

	public LabelBuilder withBorder(Border border) {
		label.setBorder(border);
		return this;
	}

	public LabelBuilder withFontSize(Integer fontSize) {
		this.fontSize = fontSize;
		return this;
	}

	public LabelBuilder withIcon(ImageIcon icon) {
		label.setIcon(icon);
		return this;
	}
	
	public JXLabel build() {
		if (fontSize != null)
			label.setFont(FormScannerFont.getFont(fontSize));
		else
			label.setFont(FormScannerFont.getFont());

		return label;
	}

	public LabelBuilder withText(String text) {
		label.setText(text);
		return this;
	}
}
