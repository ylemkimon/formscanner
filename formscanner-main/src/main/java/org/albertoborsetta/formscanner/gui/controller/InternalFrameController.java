package org.albertoborsetta.formscanner.gui.controller;

import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import org.albertoborsetta.formscanner.gui.model.FormScannerModel;
import org.albertoborsetta.formscanner.gui.view.InternalFrame;

public class InternalFrameController implements InternalFrameListener {

	private FormScannerModel model;
	private static InternalFrameController instance;
	
	public static InternalFrameController getInstance(FormScannerModel model) {
		if (instance == null) {
			instance = new InternalFrameController(model); 
		}
		return instance;
	}
	
	private InternalFrameController(FormScannerModel model) {
		this.model = model;
	}

	// InternalFrameListener
	public void internalFrameOpened(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void internalFrameClosing(InternalFrameEvent e) {
		model.disposeRelatedFrame((InternalFrame) e.getInternalFrame());
	}

	public void internalFrameClosed(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void internalFrameIconified(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void internalFrameDeiconified(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void internalFrameActivated(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void internalFrameDeactivated(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}
}
