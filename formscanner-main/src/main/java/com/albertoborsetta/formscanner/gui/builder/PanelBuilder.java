package com.albertoborsetta.formscanner.gui.builder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.Border;

import org.uncommons.swing.SpringUtilities;

import com.albertoborsetta.formscanner.commons.FormScannerFont;

public class PanelBuilder {
	
	protected JPanel panel;
	
	public PanelBuilder(ComponentOrientation orientation) {
		panel = new JPanel();
		panel.setFont(FormScannerFont.getFont());
		panel.setComponentOrientation(orientation);
	}
	
	public PanelBuilder withBackgroundColor(Color color) {
		panel.setBackground(color);
		return this;
	}
	
	public PanelBuilder withLayout(BorderLayout layout) {
		panel.setLayout(layout);
		return this;
	}
	
	public PanelBuilder withLayout(SpringLayout layout) {
		panel.setLayout(layout);
		return this;
	}
	
	public PanelBuilder withGrid(int rows, int cols) {
		SpringUtilities.makeCompactGrid(panel, rows, cols, 3, 3, 3, 3);
		return this;
	}
	
	public PanelBuilder withPreferredSize(Dimension size) {
		panel.setPreferredSize(size);
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
	
	public PanelBuilder withLayout(FlowLayout layout) {
		panel.setLayout(layout);
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
