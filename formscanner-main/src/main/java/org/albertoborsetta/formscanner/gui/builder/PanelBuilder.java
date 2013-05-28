package org.albertoborsetta.formscanner.gui.builder;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.albertoborsetta.formscanner.commons.FormScannerFont;

import com.jgoodies.forms.layout.FormLayout;

public class PanelBuilder {
	
	private JPanel panel;
	
	public PanelBuilder() {
		panel = new JPanel();
		panel.setFont(FormScannerFont.getFont());
	}
	
	public PanelBuilder withBorderLayout() {
		panel.setLayout(new BorderLayout());
		return this;
	}
	
	public PanelBuilder withFormLayout(FormLayout layout) {
		panel.setLayout(layout);
		return this;
	}
	
	public PanelBuilder addComponent(JComponent component, String position) {
		panel.add(component, position);
		return this;
	}

	public JPanel build() {
		return panel;
	}
}
