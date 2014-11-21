package com.albertoborsetta.formscanner.gui.builder;

import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.EtchedBorder;

import com.albertoborsetta.formscanner.commons.FormScannerFont;

public class ButtonBuilder {

	private JButton button;
	
	public ButtonBuilder() {
		button = new JButton();
		button.setFont(FormScannerFont.getFont());
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
	
	public ButtonBuilder withLeftAlignment() {
		button.setHorizontalAlignment(SwingConstants.LEFT);
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

}
