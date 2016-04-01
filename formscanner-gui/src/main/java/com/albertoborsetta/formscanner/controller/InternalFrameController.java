package com.albertoborsetta.formscanner.controller;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import com.albertoborsetta.formscanner.model.FormScannerModel;
import com.albertoborsetta.formscanner.gui.InternalFrame;

public class InternalFrameController implements InternalFrameListener, WindowListener {

	private final FormScannerModel model;
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

	@Override
	public void internalFrameOpened(InternalFrameEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void internalFrameClosing(InternalFrameEvent e) {
		model.disposeRelatedFrame((InternalFrame) e.getInternalFrame());
	}

	@Override
	public void internalFrameClosed(InternalFrameEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void internalFrameIconified(InternalFrameEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void internalFrameDeiconified(InternalFrameEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void internalFrameActivated(InternalFrameEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void internalFrameDeactivated(InternalFrameEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
}
