package org.albertoborsetta.formscanner.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.albertoborsetta.formscanner.model.FormScannerModel;

public class StatusBar extends JLabel {
	
	private static FormScannerModel model;
	
	public StatusBar(FormScannerModel formScannerModel) {
		super("Status label");
		model = formScannerModel;
		
		setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
	}
}
