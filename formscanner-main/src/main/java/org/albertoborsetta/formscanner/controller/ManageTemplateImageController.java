package org.albertoborsetta.formscanner.controller;

import java.awt.event.MouseEvent;

import javax.swing.JInternalFrame;

import org.albertoborsetta.formscanner.gui.ImageView;
import org.albertoborsetta.formscanner.gui.ManageTemplateImageFrame;
import org.albertoborsetta.formscanner.gui.ZoomImageFrame;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class ManageTemplateImageController implements ImageController {
	
	private FormScannerModel model;
	private ManageTemplateImageFrame view;
	private ZoomImageFrame zoom;
	private int x1;
	private int y1;
	private int x2; 
	private int y2;
	private double scaleFactor;
	
	public ManageTemplateImageController(FormScannerModel model) {
		this.model = model;
	}
	
	@Override
	public void add(JInternalFrame view) {
		this.view = (ManageTemplateImageFrame) view;
	}

	@Override
	public ImageView getView() {
		return view;
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
		
		model.showZoom(view.getZoom(), x, y);				
		model.drawRect(this, x1, y1, deltaX, deltaY);
	}

	public void mouseMoved(MouseEvent e) {
		scaleFactor = view.getScaleFactor();
		int x = (int) Math.round(e.getX() / scaleFactor);
		int y = (int) Math.round(e.getY() / scaleFactor);
		model.showZoom(view.getZoom(), x, y);
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
		model.setCrossCursor(this);
	}

	public void mouseExited(MouseEvent e) {
		model.setDefaultCursor(this);
	}
}
