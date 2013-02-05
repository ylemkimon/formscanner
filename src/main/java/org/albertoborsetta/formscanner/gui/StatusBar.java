package org.albertoborsetta.formscanner.gui;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import org.albertoborsetta.formscanner.model.FormScannerModel;


public class StatusBar extends JPanel{
	
	private static FormScannerModel model;
	
	public StatusBar(FormScannerModel formScannerModel) {
		
		model = formScannerModel;
		
		setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		setPreferredSize(new Dimension(10, 26));
		setMinimumSize(new Dimension(10, 26));
	}
}
