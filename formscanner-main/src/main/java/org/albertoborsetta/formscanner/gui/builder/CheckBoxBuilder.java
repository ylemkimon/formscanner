package org.albertoborsetta.formscanner.gui.builder;

import java.awt.event.ActionListener;

import javax.swing.JCheckBox;

import org.albertoborsetta.formscanner.commons.FormScannerFont;

public class CheckBoxBuilder {
	
	private JCheckBox chkBox;
	
	public CheckBoxBuilder(String name) {
		chkBox = new JCheckBox();
		chkBox.setName(name);
		chkBox.setFont(FormScannerFont.getFont());
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
