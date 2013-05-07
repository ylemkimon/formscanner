package org.albertoborsetta.formscanner.gui.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JInternalFrame;
import javax.swing.event.MouseInputListener;

import org.albertoborsetta.formscanner.gui.AnalyzeFileImageFrame;
import org.albertoborsetta.formscanner.gui.ScrollableImageView;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class AnalyzeFileImageController implements MouseMotionListener, MouseInputListener {
	
	private FormScannerModel model;
	private AnalyzeFileImageFrame view;
	private int x1; 
	private int y1;
	
	public AnalyzeFileImageController(FormScannerModel model) {
		this.model = model;
	}
	
	public void add(AnalyzeFileImageFrame view) {
		this.view = view;
		
	}

	// MouseMotionListener
	public void mouseDragged(MouseEvent e) {
		int x2 = e.getX();
		int y2 = e.getY();
		
		int deltaX = x1 - x2;
		int deltaY = y1 - y2;
		
		x1 = x2;
		y1 = y2;
		
		view.setScrollBars(deltaX, deltaY);
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

}