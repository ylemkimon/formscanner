package org.albertoborsetta.formscanner.controller;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.event.MouseInputListener;

import org.albertoborsetta.formscanner.gui.RenameFileFrame;
import org.albertoborsetta.formscanner.gui.RenameFileImageFrame;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class RenameImageController implements MouseMotionListener, MouseInputListener {
	
	private FormScannerModel model;
	private RenameFileImageFrame view;
	private int x1; 
	private int y1;
	
	public RenameImageController(FormScannerModel model) {
		this.model = model;
	}
	
	public void add(RenameFileImageFrame view) {
		this.view = view;
	}

	// MouseMotionListener
	public void mouseDragged(MouseEvent e) {
		if (model.getMoveImage()) {
			int deltaX = x1-e.getX();
			int deltaY = y1-e.getY();		
			view.setScrollBars(deltaX, deltaY);
		} else if (model.getHilightImage()) {
			System.out.println("hilight image");
		}
	}

	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub		
	}

	// MouseImputListener
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void mousePressed(MouseEvent e) {
		if (model.getMoveImage()) {
			x1 = e.getX();
			y1 = e.getY();
			view.setImageCursor(Cursor.MOVE_CURSOR);
		}
	}

	public void mouseReleased(MouseEvent e) {
		if (model.getMoveImage()) {
			view.setImageCursor(Cursor.HAND_CURSOR);
		} else if (model.getHilightImage()) {
			view.setImageCursor(Cursor.CROSSHAIR_CURSOR);
		}
	}

	public void mouseEntered(MouseEvent e) {
		if (model.getMoveImage()) {
			view.setImageCursor(Cursor.HAND_CURSOR);
		} else if (model.getHilightImage()) {
			view.setImageCursor(Cursor.CROSSHAIR_CURSOR);
		}
	}

	public void mouseExited(MouseEvent e) {
		view.setImageCursor(Cursor.DEFAULT_CURSOR);
	}

}
