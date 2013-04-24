package org.albertoborsetta.formscanner.controller;

import java.awt.event.MouseEvent;
import javax.swing.JInternalFrame;

import org.albertoborsetta.formscanner.gui.ImageView;
import org.albertoborsetta.formscanner.gui.RenameFileImageFrame;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class RenameImageController implements ImageController {
	
	private FormScannerModel model;
	private RenameFileImageFrame view;
	private int x1; 
	private int y1;
	
	public RenameImageController(FormScannerModel model) {
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
		model.setMoveCursor(this);
	}

	public void mouseReleased(MouseEvent e) {
		model.setDefaultCursor(this);
	}

	public void mouseEntered(MouseEvent e) {
		// TODO
	}

	public void mouseExited(MouseEvent e) {
		// TODO
	}

	@Override
	public ImageView getView() {
		// TODO Auto-generated method stub
		return view;
	}
}
