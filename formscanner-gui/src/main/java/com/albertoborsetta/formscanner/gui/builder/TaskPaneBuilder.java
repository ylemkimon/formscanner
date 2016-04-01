package com.albertoborsetta.formscanner.gui.builder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JComponent;
import javax.swing.SpringLayout;
import javax.swing.border.Border;

import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.JXTaskPane;
import org.uncommons.swing.SpringUtilities;

import com.albertoborsetta.formscanner.commons.FormScannerFont;

public class TaskPaneBuilder {

	private final ComponentOrientation orientation;
	private String title;
	private Color bgColor = null;
	private Object layout = null;
	private Border border = null;
	private ArrayList<JComponent> componentsArray;
	private HashMap<String, JComponent> componentsMap;
	private Dimension size = null;
	private int rows = 0;
	private int cols = 0;

	public TaskPaneBuilder(ComponentOrientation orientation) {
		this.orientation = orientation;
	}

	public TaskPaneBuilder withBackgroundColor(Color color) {
		this.bgColor = color;
		return this;
	}

	public TaskPaneBuilder withLayout(Object layout) {
		if (layout instanceof BorderLayout) {
			componentsMap = new HashMap<>();
		} else if ((layout instanceof SpringLayout) || (layout instanceof FlowLayout)){
			componentsArray = new ArrayList<>();
		}
		this.layout = layout;
		return this;
	}

	public TaskPaneBuilder withGrid(int rows, int cols) {
		this.rows = rows;
		this.cols = cols;
		return this;
	}

	public TaskPaneBuilder addComponent(JComponent component, String position) {
		componentsMap.put(position, component);
		return this;
	}

	public TaskPaneBuilder add(JComponent component, String position) {
		return addComponent(component, position);
	}

	public TaskPaneBuilder withBorder(Border border) {
		this.border = border;
		return this;
	}

	public TaskPaneBuilder addComponent(JComponent component) {
		componentsArray.add(component);
		return this;
	}

	public TaskPaneBuilder add(JComponent component) {
		return addComponent(component);
	}

	public JXTaskPane build() {
		JXTaskPane panel = new JXTaskPane();
		panel.setFont(FormScannerFont.getFont());
		panel.setComponentOrientation(orientation);
		
		if (StringUtils.isNotBlank(title))
			panel.setTitle(title);

		if (bgColor != null) {
			panel.setBackground(bgColor);
		}

		if (border != null) {
			panel.setBorder(border);
		}

		if (!orientation.isLeftToRight()) {
			switchPositions();
		}

		if (layout instanceof BorderLayout) {
			panel.setLayout((BorderLayout) layout);

			for (String position : componentsMap.keySet()) {
				if (componentsMap.get(position) != null) {
					panel.add(componentsMap.get(position), position);
				}
			}
		} else if (layout instanceof SpringLayout) {
			panel.setLayout((SpringLayout) layout);

			for (JComponent component : componentsArray) {
				panel.add(component);
			}
			SpringUtilities.makeCompactGrid(panel, rows, cols, 3, 3, 3, 3);
		} else if (layout instanceof FlowLayout) {
				panel.setLayout((FlowLayout) layout);

				for (JComponent component : componentsArray) {
					panel.add(component);
				}
			}

		if (size != null) {
			panel.setPreferredSize(size);
		}

		return panel;
	}

	private void switchPositions() {
		if (layout instanceof BorderLayout) {
			JComponent tempComponent = componentsMap.get(BorderLayout.EAST);
			componentsMap.put(
					BorderLayout.EAST, componentsMap.get(BorderLayout.WEST));
			componentsMap.put(BorderLayout.WEST, tempComponent);
		} else if (layout instanceof SpringLayout) {
			for (int j = 0; j < rows; j++) {
				for (int i = 0; i < cols / 2; i++) {
					JComponent tempComponent = componentsArray
							.get(i + (j * cols));
					componentsArray.set(
							i + (j * cols),
							componentsArray.get(((j + 1) * cols) - (i + 1)));
					componentsArray.set(
							((j + 1) * cols) - (i + 1), tempComponent);
				}
			}
		}
	}

	public TaskPaneBuilder withTitle(String title) {
		this.title = title;
		return this;
	}
}
