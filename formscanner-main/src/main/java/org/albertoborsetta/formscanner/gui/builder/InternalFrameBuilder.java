package org.albertoborsetta.formscanner.gui.builder;

import javax.swing.JInternalFrame;

import org.albertoborsetta.formscanner.commons.FormScannerFont;

public class InternalFrameBuilder {
	
	private JInternalFrame internalFrame;
	
	public InternalFrameBuilder() {
		internalFrame = new JInternalFrame();
		internalFrame.setFont(FormScannerFont.getFont());
	}
	
	public JInternalFrame build() {
		return internalFrame;
	}
}
