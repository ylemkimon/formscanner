package com.albertoborsetta.formscanner.gui.builder;

import java.awt.ComponentOrientation;
import java.awt.event.ItemListener;

import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;

import com.albertoborsetta.formscanner.commons.FormScannerFont;
import com.albertoborsetta.formscanner.gui.OptionsFrame.IconListRenderer;

public class ComboBoxBuilder<T> {

    private final JComboBox<T> comboBox;

    public ComboBoxBuilder(String name, ComponentOrientation orientation) {
        comboBox = new JComboBox<>();
        comboBox.setName(name);
        comboBox.setFont(FormScannerFont.getFont());
        comboBox.setComponentOrientation(orientation);
        if (!orientation.isLeftToRight()) {
            ((JLabel) comboBox.getRenderer()).setHorizontalAlignment(JLabel.RIGHT);
        }
    }

    public ComboBoxBuilder<T> withActionListener(ItemListener listener) {
        comboBox.addItemListener(listener);
        return this;
    }

    public ComboBoxBuilder<T> withModel(ComboBoxModel<T> defaultComboBoxModel) {
        comboBox.setModel(defaultComboBoxModel);
        return this;
    }

    public ComboBoxBuilder<T> addItem(T item) {
        comboBox.addItem(item);
        return this;
    }

    public JComboBox<T> build() {
        return comboBox;
    }

	public ComboBoxBuilder<T> setEditable(boolean b) {
		comboBox.setEditable(b);
		return this;
	}

	public ComboBoxBuilder<T> withActionCommand(String command) {
		comboBox.setActionCommand(command);
		return this;
	}

	public ComboBoxBuilder<T> withRenderer(IconListRenderer countryItemRenderer) {
		comboBox.setRenderer(countryItemRenderer);
		return this;
	}
}
