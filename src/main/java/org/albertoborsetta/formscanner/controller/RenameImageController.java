package org.albertoborsetta.formscanner.controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.event.MouseInputListener;

import org.albertoborsetta.formscanner.model.FormScannerModel;

public class RenameImageController implements MouseMotionListener, MouseInputListener {
	
	private FormScannerModel model;
	private int x1; 
	private int y1;
	
	public RenameImageController(FormScannerModel model) {
		this.model = model;
	}

	// MouseMotionListener
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
		int deltaX = (x1-e.getX()>0)?(x1-e.getX()):0;
		int deltaY = (y1-e.getY()>0)?(y1-e.getY()):0;
		
		model.setScrollBars(deltaX, deltaY);		
	}

	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub		
	}

	// MouseImputListener
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		x1 = e.getX();
		y1 = e.getY();
	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
