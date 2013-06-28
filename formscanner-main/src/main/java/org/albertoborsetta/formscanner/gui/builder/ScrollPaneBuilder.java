package org.albertoborsetta.formscanner.gui.builder;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

import org.albertoborsetta.formscanner.commons.FormScannerFont;

public class ScrollPaneBuilder {
	
	private JScrollPane scrollPane;
	
	public ScrollPaneBuilder(JComponent component) {
		scrollPane = new JScrollPane(component);
		scrollPane.setFont(FormScannerFont.getFont());
	}

	public JScrollPane build() {
		return scrollPane;
	}
}
