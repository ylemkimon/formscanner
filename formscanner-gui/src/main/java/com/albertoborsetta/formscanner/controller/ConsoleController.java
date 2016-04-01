package com.albertoborsetta.formscanner.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.albertoborsetta.formscanner.commons.FormScannerConstants.Action;
import com.albertoborsetta.formscanner.commons.TextAreaOutputStream;
import com.albertoborsetta.formscanner.model.FormScannerModel;

public class ConsoleController implements ActionListener {

	private final FormScannerModel model;
	private TextAreaOutputStream console;

	public ConsoleController(FormScannerModel model) {
		this.model = model;
	}

	public void add(TextAreaOutputStream console) {
		this.console = console;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Action act = Action.valueOf(e.getActionCommand());
		switch (act) {
		case CLEAR_CONSOLE:
			console.clear();
			break;
		default:
			break;
		}
	}
}
