package com.albertoborsetta.formscanner.controller;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import com.albertoborsetta.formscanner.commons.FormScannerConstants.Frame;
import com.albertoborsetta.formscanner.model.FormScannerModel;

public class FormScannerController implements WindowListener {

	private final FormScannerModel model;
	private static FormScannerController instance;

	public static FormScannerController getInstance(FormScannerModel model) {
		if (instance == null) {
			instance = new FormScannerController(model);
		}
		return instance;
	}

	private FormScannerController(FormScannerModel model) {
		this.model = model;
	}

	@Override
	public void windowOpened(WindowEvent e) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		model.setLastPosition(Frame.DESKTOP_FRAME, e.getWindow().getBounds());
		e.getWindow().dispose();
		System.exit(0);
	}

	@Override
	public void windowClosed(WindowEvent e) {
	}

	@Override
	public void windowIconified(WindowEvent e) {
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	@Override
	public void windowActivated(WindowEvent e) {
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
	}
}
