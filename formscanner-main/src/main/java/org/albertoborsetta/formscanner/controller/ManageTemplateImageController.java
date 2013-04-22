package org.albertoborsetta.formscanner.controller;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.event.MouseInputListener;

import org.albertoborsetta.formscanner.gui.ManageTemplateImageFrame;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class ManageTemplateImageController implements MouseMotionListener, MouseInputListener {
	
	private FormScannerModel model;
	private ManageTemplateImageFrame view;
	private int x1;
	private int y1;
	private int x2; 
	private int y2;
	private double scaleFactor;
	
	public ManageTemplateImageController(FormScannerModel model) {
		this.model = model;
	}
	
	public void add(ManageTemplateImageFrame view) {
		this.view = view;
	}

	// MouseMotionListener
	public void mouseDragged(MouseEvent e) {
		scaleFactor = view.getScaleFactor();
		
		int x2 = e.getX();
		int y2 = e.getY();
		
		int deltaX = x2 - x1;
		int deltaY = y2 - y1;
		
		int x = (int) Math.round(x2 / scaleFactor);
		int y = (int) Math.round(y2 / scaleFactor);
		
		model.showZoom(x, y);				
		view.drawRect(x1, y1, deltaX, deltaY);
	}

	public void mouseMoved(MouseEvent e) {
		scaleFactor = view.getScaleFactor();
		int x = (int) Math.round(e.getX() / scaleFactor);
		int y = (int) Math.round(e.getY() / scaleFactor);
		model.showZoom(x, y);
	}

	// MouseImputListener
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		x1 = e.getX();
		y1 = e.getY();
	}

	public void mouseReleased(MouseEvent e) {
		x2 = e.getX();
		y2 = e.getY();
	}

	public void mouseEntered(MouseEvent e) {
		view.setImageCursor(Cursor.CROSSHAIR_CURSOR);
	}

	public void mouseExited(MouseEvent e) {
		view.setImageCursor(Cursor.DEFAULT_CURSOR);
	}
}
