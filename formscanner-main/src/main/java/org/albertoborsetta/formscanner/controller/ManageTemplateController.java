package org.albertoborsetta.formscanner.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.albertoborsetta.formscanner.gui.ManageTemplateFrame;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class ManageTemplateController implements ActionListener {
	
	private FormScannerModel model;
	private ManageTemplateFrame view;
	
	public ManageTemplateController(FormScannerModel model) {
		this.model = model;
	}
	
	public void add(ManageTemplateFrame view) {
		this.view = view;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
