package org.albertoborsetta.formscanner.controller;

import org.albertoborsetta.formscanner.commons.Constants;
import org.albertoborsetta.formscanner.commons.Constants.Actions;
import org.albertoborsetta.formscanner.gui.RenameFileFrame;
import org.albertoborsetta.formscanner.model.FormScannerModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.event.MouseInputListener;

public class RenameFileController implements InternalFrameListener, KeyListener, 
	ActionListener, MouseMotionListener, MouseListener {
	
	private FormScannerModel model;
	private RenameFileFrame view;
	private static RenameFileController instance;
	private int x1; 
	private int y1;
	
	public static RenameFileController getInstance(FormScannerModel model) {
		if (instance == null) {
			instance = new RenameFileController(model); 
		}
		return instance;
	}
	
	private RenameFileController(FormScannerModel model) {
		this.model = model;
	}
	
	public void add(RenameFileFrame view) {
		this.view = view;
	}

	// InternalFrameListener
	public void internalFrameOpened(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void internalFrameClosing(InternalFrameEvent e) {
		model.disposeRelatedFrame(e.getInternalFrame());
	}

	public void internalFrameClosed(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void internalFrameIconified(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void internalFrameDeiconified(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void internalFrameActivated(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void internalFrameDeactivated(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	// KeyListener	
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
	}

	public void keyPressed(KeyEvent e) {
		if ((e.getKeyCode() == KeyEvent.VK_ENTER) && (view.isOkEnabled())) {
			view.setOkEnabled(false);
			model.renameFiles(Constants.RENAME_FILE_CURRENT);
		} else if ((e.getKeyCode() == KeyEvent.VK_ENTER) && (!view.isOkEnabled())) {
			model.renameFiles(Constants.RENAME_FILE_SKIP);
		} else {
			view.setOkEnabled(true);
		}
	}

	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub			
	}

	// ActionListener
	public void actionPerformed(ActionEvent e) {
		Actions act = Actions.valueOf(e.getActionCommand());
		switch (act) {
		case RENAME_FILE_CURRENT:
			model.renameFiles(Constants.RENAME_FILE_CURRENT);
			break;
		case RENAME_FILE_SKIP:
			model.renameFiles(Constants.RENAME_FILE_SKIP);
			break;
		}
	}

	// MouseMotionListener
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		
		int deltaX = x1 - e.getX();
		int deltaY = y1 - e.getY(); 
		model.setImagePositionX(deltaX);
		model.setImagePositionY(deltaY);
		model.updateImage();
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
