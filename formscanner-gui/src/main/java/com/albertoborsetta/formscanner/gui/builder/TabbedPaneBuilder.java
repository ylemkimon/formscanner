package com.albertoborsetta.formscanner.gui.builder;

import java.awt.ComponentOrientation;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import com.albertoborsetta.formscanner.commons.FormScannerFont;

public class TabbedPaneBuilder {

	private final JTabbedPane tabbedPane;

	public TabbedPaneBuilder(int tabPosition, ComponentOrientation orientation) {
		tabbedPane = new JTabbedPane(tabPosition);
		tabbedPane.setFont(FormScannerFont.getFont());
		tabbedPane.setComponentOrientation(orientation);
	}

	public TabbedPaneBuilder addTab(String title, JComponent component) {
		tabbedPane.addTab(title, null, component, null);
		return this;
	}

	public TabbedPaneBuilder addTab(String title, Icon icon, JComponent component) {
		tabbedPane.addTab(title, icon, component, null);
		return this;
	}
	
	public JTabbedPane build() {
		return tabbedPane;
	}
}
