package org.albertoborsetta.formscanner.gui.builder;

import java.awt.event.ItemListener;

import javax.swing.JComboBox;

import org.albertoborsetta.formscanner.commons.FormScannerFont;

public class ComboBoxBuilder {
	
	private JComboBox comboBox;
	
	public ComboBoxBuilder(String name) {
		comboBox = new JComboBox();
		comboBox.setName(name);
		comboBox.setFont(FormScannerFont.getFont());
	}
	
	public ComboBoxBuilder withItemListener(ItemListener listener) {
		comboBox.addItemListener(listener);
		return this;
	}
	
	public ComboBoxBuilder addItem(String item) {
		comboBox.addItem(makeObj(item));
		return this;
	}
	
	public JComboBox build() {
		return comboBox;
	}
	
	private Object makeObj(final String item)  {
	     return new Object() { public String toString() { return item; } };
	   }
}
