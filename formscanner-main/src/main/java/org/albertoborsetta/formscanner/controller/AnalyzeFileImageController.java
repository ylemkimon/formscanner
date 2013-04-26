package org.albertoborsetta.formscanner.controller;

import java.awt.event.MouseEvent;
import javax.swing.JInternalFrame;

import org.albertoborsetta.formscanner.gui.AnalyzeFileImageFrame;
import org.albertoborsetta.formscanner.gui.ScrollableImageView;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class AnalyzeFileImageController implements ScrollableImageController {
	
	private FormScannerModel model;
	private AnalyzeFileImageFrame view;
	private int x1; 
	private int y1;
	
	public AnalyzeFileImageController(FormScannerModel model) {
		this.model = model;
	}
	
	@Override
	public void add(JInternalFrame view) {
		this.view = (AnalyzeFileImageFrame) view;
		
	}

	@Override
	public ScrollableImageView getView() {
		return view;
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

}
