package org.albertoborsetta.formscanner.gui.controller;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.event.MouseInputListener;

import org.albertoborsetta.formscanner.commons.FormPoint;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Zoom;
import org.albertoborsetta.formscanner.gui.ImageFrame;
import org.albertoborsetta.formscanner.model.FormScannerModel;

//public class ImageFrameController implements MouseMotionListener, MouseInputListener, MouseWheelListener {
public class ImageFrameController implements MouseMotionListener, MouseInputListener {
	
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
			if (e.isControlDown()) {
				p2 = new FormPoint(e.getPoint());
				
				int deltaX = p1.x - p2.x;
				int deltaY = p1.y - p2.y;
				p1 = p2;
				
				model.setScrollBars(view, deltaX, deltaY);
				model.setImageCursor(view, new Cursor(Cursor.HAND_CURSOR));
			}
			break;
		case VIEW:
			p2 =  new FormPoint(e.getPoint());
			
			int deltaX = p1.x - p2.x;
			int deltaY = p1.y - p2.y;
			p1 = p2;
			
			model.setScrollBars(view, deltaX, deltaY);
			break;
		default:
			break;
		}
				
	}

	public void mouseMoved(MouseEvent e) {
	}

	// MouseImputListener
	public void mouseClicked(MouseEvent e) {
		switch (view.getMode()) {
		case UPDATE:
			if (!e.isControlDown()) {
				switch (e.getButton()) {
				case MouseEvent.BUTTON1:
					// Zoom scaleFactor = view.getScaleFactor();
					
					FormPoint p = new FormPoint(e.getPoint());
					
					int	dx = view.getHorizontalScrollbarValue();
					int dy = view.getVerticalScrollbarValue();
					
//					int x = (int) Math.floor((p.x/scaleFactor.getValue())+dx);
//					int y = (int) Math.floor((p.y/scaleFactor.getValue())+dy);
					int x = p.x+dx;
					int y = p.y+dy;
					
					FormPoint p1 = new FormPoint(x, y);
					model.addPoint(view, p1);
					break;
				case MouseEvent.BUTTON3:
					model.removeTemporaryPoint(view);
					break;
				}			
			}
			break;
		case VIEW:
		default:
			break;
		}
	}

	public void mousePressed(MouseEvent e) {
		switch (view.getMode()) {
		case UPDATE:
			if (e.isControlDown()) {
				p1 = new FormPoint(e.getPoint());
				model.setImageCursor(view, new Cursor(Cursor.HAND_CURSOR));
			}
			break;
		case VIEW:
			p1 = new FormPoint(e.getPoint());
			break;
		default:
			break;
		}
		
	}

	public void mouseReleased(MouseEvent e) {
		switch (view.getMode()) {
		case UPDATE:
			if (e.isControlDown()) {
				model.setImageCursor(view, new Cursor(Cursor.CROSSHAIR_CURSOR));
			}
			break;
		case VIEW:
		default:
			break;
		}
	}

	public void mouseEntered(MouseEvent e) {
		switch (view.getMode()) {
		case UPDATE:
			model.setImageCursor(view, new Cursor(Cursor.CROSSHAIR_CURSOR));
			break;
		case VIEW:
			model.setImageCursor(view, new Cursor(Cursor.HAND_CURSOR));
			break;
		default:
			break;
		}
	}

	public void mouseExited(MouseEvent e) {
		model.setImageCursor(view, new Cursor(Cursor.DEFAULT_CURSOR));
	}

//	public void mouseWheelMoved(MouseWheelEvent e) {
//		model.zoomImage(view, e.getWheelRotation());
//	}
}
