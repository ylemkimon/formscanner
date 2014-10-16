package org.albertoborsetta.formscanner.gui.builder;

import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;

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
	
	public CheckBoxBuilder withActionListener(ChangeListener listener) {
		chkBox.addChangeListener(listener);
		return this;
	}
	
	public JCheckBox build() {
		return chkBox;
	}
}
