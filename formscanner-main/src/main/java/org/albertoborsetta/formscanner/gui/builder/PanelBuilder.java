package org.albertoborsetta.formscanner.gui.builder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;

import org.albertoborsetta.formscanner.commons.FormScannerFont;

import com.jgoodies.forms.layout.FormLayout;

public class PanelBuilder {
	
	protected JPanel panel;
	
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
	
	public PanelBuilder add(JComponent component, String position) {
		return addComponent(component, position);
	}
	
	public JPanel build() {
		return panel;
	}
	
	public PanelBuilder withFlowLayout(int align, int hgap, int vgap) {
		panel.setLayout(new FlowLayout(align, hgap, vgap));
		return this;
	}
	
	public PanelBuilder withBorder(Border border) {
		panel.setBorder(border);
		return this;
	}
	
	public PanelBuilder addComponent(JComponent component) {
		panel.add(component);
		return this;
	}
	
	public PanelBuilder add(JComponent component) {
		return addComponent(component);
	}
}
