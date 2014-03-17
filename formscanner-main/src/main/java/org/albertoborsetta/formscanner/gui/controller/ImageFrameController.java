package org.albertoborsetta.formscanner.gui.controller;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;
import javax.swing.event.MouseInputListener;

import org.albertoborsetta.formscanner.commons.FormPoint;
import org.albertoborsetta.formscanner.gui.ImageFrame;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class ImageFrameController implements MouseMotionListener, MouseInputListener, MouseWheelListener {
	
	private FormScannerModel model;
	private ImageFrame view;
	private FormPoint p1;
	private FormPoint p2;
	
	public ImageFrameController(FormScannerModel model) {
		this.model = model;
	}
	
	public void add(ImageFrame view) {
		this.view = view;
	}

	// MouseMotionListener
	public void mouseDragged(MouseEvent e) {
		switch (view.getMode()) {
		case UPDATE:
		case VIEW:
			p2 = new FormPoint(e.getPoint());
			
			if (p1.dist2(p2) >= 10) {
				double deltaX = (p1.getX() - p2.getX());
				double deltaY = (p1.getY() - p2.getY());
				
				p1 = p2;
				
				model.setScrollBars(view, (int) deltaX, (int) deltaY);
			}			
		default:
			break;
		}
				
	}

	public void mouseMoved(MouseEvent e) {
		FormPoint p = getCursorPoint(e);
		
		model.showCursorPosition(view, p);
	}


	// MouseInputListener
	public void mouseClicked(MouseEvent e) {
		switch (view.getMode()) {
		case UPDATE:
			FormPoint p = getCursorPoint(e);
			model.addPoint(view, p);
			break;
		case VIEW:
		default:
			break;
		}
	}

	public void mousePressed(MouseEvent e) {
		switch (view.getMode()) {
		case UPDATE:
		case VIEW:
			p1 = new FormPoint(e.getPoint());
			model.setImageCursor(view, new Cursor(Cursor.HAND_CURSOR));
		default:
			break;
		}
		
	}

	public void mouseReleased(MouseEvent e) {
		switch (view.getMode()) {
		case UPDATE:
		case VIEW:
			model.setImageCursor(view, new Cursor(Cursor.CROSSHAIR_CURSOR));
		default:
			break;
		}
	}

	public void mouseEntered(MouseEvent e) {
		model.setImageCursor(view, new Cursor(Cursor.CROSSHAIR_CURSOR));
	}

	public void mouseExited(MouseEvent e) {
		model.setImageCursor(view, new Cursor(Cursor.DEFAULT_CURSOR));
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
		int delta = e.getUnitsToScroll()*5;
		
		if (e.isControlDown()) {
			model.setScrollBars(view, delta, 0);
		} else {
			model.setScrollBars(view, 0, delta);
		}
		
		FormPoint p = getCursorPoint(e);
		model.showCursorPosition(view, p);
	}
	
	private FormPoint getCursorPoint(MouseEvent e) {
		int	dx = view.getHorizontalScrollbarValue();
		int dy = view.getVerticalScrollbarValue();
		
		int x = e.getX()+dx;
		int y = e.getY()+dy;
		
		FormPoint p1 = new FormPoint(x, y);
		return p1;
	}
}
