package org.albertoborsetta.formscanner.gui;

import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

import org.albertoborsetta.formscanner.model.FormScannerModel;

public class StatusBar extends JLabel {
	
	private static final long serialVersionUID = 1L;
	
	private static FormScannerModel model;
	
	public StatusBar(FormScannerModel formScannerModel) {
		super("Status label");
		model = formScannerModel;
		
		setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
	}
}
