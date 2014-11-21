package com.albertoborsetta.formscanner.gui.builder;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;

import com.albertoborsetta.formscanner.commons.FormScannerFont;

public class TabbedPaneBuilder {
	
	private JTabbedPane tabbedPane;
	
	public TabbedPaneBuilder(int tabPosition) {
		tabbedPane = new JTabbedPane(tabPosition);
		tabbedPane.setFont(FormScannerFont.getFont());
	}
	
	public TabbedPaneBuilder addTab(String title, JComponent component) {
		tabbedPane.addTab(title, null, component, null);
		return this;
	}
	
	public JTabbedPane build() {
		return tabbedPane;
	}
}
