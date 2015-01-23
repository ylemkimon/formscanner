package com.albertoborsetta.formscanner.gui.builder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SpringLayout;
import javax.swing.border.Border;

import org.uncommons.swing.SpringUtilities;

import com.albertoborsetta.formscanner.commons.FormScannerFont;

public class PanelBuilder {

	private ComponentOrientation orientation;
	private Color bgColor = null;
	private Object layout = null;
	private Border border = null;
	private ArrayList<JComponent> components = new ArrayList<JComponent>();
	private ArrayList<String> positions = new ArrayList<String>();
	private Dimension size = null;
	private int rows = 0;
	private int cols = 0;
	
	public PanelBuilder() {
		this.orientation = ComponentOrientation.LEFT_TO_RIGHT;
	}
	
	public PanelBuilder(ComponentOrientation orientation) {
		this.orientation = orientation;
	}

	public PanelBuilder withBackgroundColor(Color color) {
		this.bgColor = color;
		return this;
	}

	public PanelBuilder withLayout(Object layout) {
		this.layout = layout;
		return this;
	}

	public PanelBuilder withGrid(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		return this;
	}

	public PanelBuilder withPreferredSize(Dimension size) {
		this.size = size;
		return this;
	}

	public PanelBuilder addComponent(JComponent component, String position) {
		components.add(component);
		positions.add(position);
		return this;
	}

	public PanelBuilder add(JComponent component, String position) {
		return addComponent(component, position);
	}

	public PanelBuilder withBorder(Border border) {
		this.border = border;
		return this;
	}

	public PanelBuilder addComponent(JComponent component) {
		components.add(component);
		return this;
	}

	public PanelBuilder add(JComponent component) {
		return addComponent(component);
	}

	public JPanel build() {
		JPanel panel = new JPanel();
		panel.setFont(FormScannerFont.getFont());
		panel.setComponentOrientation(orientation);

		if (bgColor != null) {
			panel.setBackground(bgColor);
		}

		if (border != null) {
			panel.setBorder(border);
		}

		switch (layout.getClass().getName()) {
		case "java.awt.BorderLayout":
			panel.setLayout((BorderLayout) layout);
			
			if (!orientation.isLeftToRight()) {
				switchEastWestPositions();
			}
			
			for (int i = 0; i < components.size(); i++) {
				panel.add(components.get(i), positions.get(i));
			}
			break;
		case "javax.swing.SpringLayout":
			panel.setLayout((SpringLayout) layout);

			if (!orientation.isLeftToRight()) {
				switchGridPositions();
			}
			
			for (int i = 0; i < components.size(); i++) {	 
				panel.add(components.get(i));
			}
			SpringUtilities.makeCompactGrid(panel, rows, cols, 3, 3, 3, 3);
			break;
		}

		if (size != null) {
			panel.setPreferredSize(size);
		}

		return panel;
	}

	private void switchGridPositions() {
		int size = components.size();
		if ((size % 2) == 0) {
			for (int i=0; i<size; i=i+2) {
				JComponent tempComponent = components.get(i);
				components.set(i, components.get(i+1));
				components.set(i+1, tempComponent);
			}
		} else {
			for (int i=0; i<size/2; i++) {
				JComponent tempComponent = components.get(i);
				components.set(i, components.get(size-i));
				components.set(size-i, tempComponent);
			}
		}
	}

	private void switchEastWestPositions() {
		for (int i=0; i<positions.size(); i++) {
			switch (positions.get(i)) {
			case BorderLayout.EAST:
				positions.set(i, BorderLayout.WEST);
				break;
			case BorderLayout.WEST:
				positions.set(i, BorderLayout.EAST);
				break;
			}
		}
	}
}
