package org.albertoborsetta.formscanner.gui.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JInternalFrame;
import javax.swing.event.MouseInputListener;

import org.albertoborsetta.formscanner.gui.RenameFileImageFrame;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class RenameFileImageController implements Controller, MouseMotionListener, MouseInputListener, MouseWheelListener {
	
	private FormScannerModel model;
	private RenameFileImageFrame view;
	private int x1; 
	private int y1;
	
	public RenameFileImageController(FormScannerModel model) {
		this.model = model;
	}
	
	@Override
	public void add(JInternalFrame view) {
		this.view = (RenameFileImageFrame) view;
	}

	// MouseMotionListener
	public void mouseDragged(MouseEvent e) {
		int x2 = e.getX();
		int y2 = e.getY();
		
		int deltaX = x1 - x2;
		int deltaY = y1 - y2;
		
		x1 = x2;
		y1 = y2;
		
		model.setScrollBars(view, deltaX, deltaY);
	}

	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub		
	}

	// MouseImputListener
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public void mousePressed(MouseEvent e) {
		x1 = e.getX();
		y1 = e.getY();
		model.setMoveCursor(view);
	}

	public void mouseReleased(MouseEvent e) {
		model.setDefaultCursor(view);
	}

	public void mouseEntered(MouseEvent e) {
		// TODO
	}

	public void mouseExited(MouseEvent e) {
		// TODO
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		// TODO Auto-generated method stub
	}
}
