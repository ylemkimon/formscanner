package org.albertoborsetta.formscanner.gui.controller;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JInternalFrame;
import javax.swing.event.MouseInputListener;

import org.albertoborsetta.formscanner.gui.ManageTemplateImageFrame;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class ManageTemplateImageController implements MouseMotionListener, MouseInputListener, MouseWheelListener {
	
	private FormScannerModel model;
	private ManageTemplateImageFrame view;
	private Point p1;
	private Point p2;
	private Point p3;
	private Point p4;
	// private double scaleFactor;
	
	public ManageTemplateImageController(FormScannerModel model) {
		this.model = model;
	}
	
	@Override
	public void add(ManageTemplateImageFrame view) {
		this.view = view;
	}

	// MouseMotionListener
	public void mouseDragged(MouseEvent e) {
		System.out.println(e.getButton());
		switch (e.getButton()) {
		case MouseEvent.BUTTON1:
			if (p1==null) {
				p1 = e.getPoint();
				model.drawRect(view, p1.x-5, p1.y-5, 10, 10);
			} else {
				p2 = e.getPoint();				
				model.drawRect(view, p2.x-5, p2.y-5, 10, 10);
			}
			break;
		case MouseEvent.BUTTON2:
			p4 = e.getPoint();
			
			int deltaX = p3.x - p4.x;
			int deltaY = p3.y - p4.y;
			
			p3 = p4;
			
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
	}

	public void mousePressed(MouseEvent e) {
		switch (e.getButton()) {
		case MouseEvent.BUTTON2:
			p3 = e.getPoint();
			model.setMoveCursor(view);
			break;
		default:
			break;
		}
	}

	public void mouseReleased(MouseEvent e) {
		switch (e.getButton()) {
		case MouseEvent.BUTTON2:
			model.setCrossCursor(view);
			break;
		default:
			break;
		}
	}

	public void mouseEntered(MouseEvent e) {
		model.setCrossCursor(view);
	}

	public void mouseExited(MouseEvent e) {
		model.setDefaultCursor(view);
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		model.zoomImage(view, e.getWheelRotation());
	}
}
