package org.albertoborsetta.formscanner.gui.builder;

import java.awt.event.ItemListener;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;

import org.albertoborsetta.formscanner.commons.FormScannerConstants.FieldType;
import org.albertoborsetta.formscanner.commons.FormScannerFont;

public class ComboBoxBuilder {
	
	private JComboBox<FieldType> comboBox;
	
	public ComboBoxBuilder(String name) {
		comboBox = new JComboBox<FieldType>();
		comboBox.setName(name);
		comboBox.setFont(FormScannerFont.getFont());
	}
	
	public ComboBoxBuilder withActionListener(ItemListener listener) {
		comboBox.addItemListener(listener);
		return this;
	}
	
	public ComboBoxBuilder withModel(ComboBoxModel<FieldType> defaultComboBoxModel) {
		comboBox.setModel(defaultComboBoxModel);
		return this;
	}
	
	public ComboBoxBuilder addItem(FieldType item) {
		comboBox.addItem(item);
		return this;
	}
	
	public JComboBox<FieldType> build() {
		return comboBox;
	}
}
