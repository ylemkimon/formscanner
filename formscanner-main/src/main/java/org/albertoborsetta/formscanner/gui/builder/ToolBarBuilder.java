package org.albertoborsetta.formscanner.gui.builder;

import java.awt.ComponentOrientation;

import javax.swing.JComponent;
import javax.swing.JToolBar;

import org.albertoborsetta.formscanner.commons.FormScannerFont;

public class ToolBarBuilder {
	
	private JToolBar toolBar;
	
	public ToolBarBuilder() {
		toolBar = new JToolBar();
		toolBar.setFont(FormScannerFont.getFont());
	}
	
	public ToolBarBuilder withAlignmentX(float alignment) {
		toolBar.setAlignmentX(alignment);
		return this;
	}
	
	public ToolBarBuilder withAlignmentY(float alignment) {
		toolBar.setAlignmentX(alignment);
		return this;
	}
	
	public ToolBarBuilder withComponentOrientation(ComponentOrientation orientation) {
		toolBar.setComponentOrientation(orientation);
		return this;
	}
	
	public ToolBarBuilder add(JComponent component) {
		toolBar.add(component);
		return this;
	}
	
	public JToolBar build() {
		return toolBar;
	}
}
