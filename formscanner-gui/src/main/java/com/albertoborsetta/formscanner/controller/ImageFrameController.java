package com.albertoborsetta.formscanner.controller;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelListener;

import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;

import com.albertoborsetta.formscanner.api.FormPoint;
import com.albertoborsetta.formscanner.model.FormScannerModel;
import com.albertoborsetta.formscanner.gui.ImageFrame;
import com.albertoborsetta.formscanner.gui.ToolBar;
import com.albertoborsetta.formscanner.gui.StatusBar;

public class ImageFrameController
		implements MouseMotionListener, MouseInputListener, MouseWheelListener, ActionListener, ItemListener {

	private final FormScannerModel model;
	private ImageFrame imagePanel;
	private ToolBar imageToolBar;
	private StatusBar statusBar;
	private FormPoint p1;
	private FormPoint p2;
	private int buttonPressed;
	private static ImageFrameController instance;

	private ImageFrameController(FormScannerModel model) {
		this.model = model;
	}

	public static ImageFrameController getInstance(FormScannerModel model) {
		if (instance == null) {
			instance = new ImageFrameController(model);
		}
		return instance;
	}

	public void add(Component view) {
		if (view instanceof ImageFrame)
			this.imagePanel = (ImageFrame) view;
		if (view instanceof ToolBar)
			this.imageToolBar = (ToolBar) view;
		if (view instanceof StatusBar)
			this.statusBar = (StatusBar) view;
	}

	// MouseMotionListener
	@Override
	public void mouseDragged(MouseEvent e) {
		switch (imagePanel.getMode()) {
		case SETUP_AREA:
		case SETUP_POINTS:
		case MODIFY_POINTS:
			if (!e.isControlDown() && (buttonPressed == MouseEvent.BUTTON1)) {
				FormPoint p = showCursorPosition(e);
				// Corners corner = imagePanel.getSelectedButton();
				// if (corner != null) {
				// FormTemplate template = imagePanel.getTemplate();
				// template.setCorner(corner, p);
				// imagePanel.showCornerPosition();
				// imagePanel.repaint();
				// } else {
				imagePanel.setTemporaryPoint(p);
				imagePanel.repaint();
				// }
				break;
			}
		case VIEW:
			if (e.isControlDown()) {
				showCursorPosition(e);
				p2 = new FormPoint(e.getPoint());
				if (p1.dist2(p2) >= 10) {
					double deltaX = (p1.getX() - p2.getX());
					double deltaY = (p1.getY() - p2.getY());

					p1 = p2;

					imagePanel.setScrollBars((int) deltaX, (int) deltaY);
				}
			}
		default:
			break;
		}

	}

	private FormPoint showCursorPosition(MouseEvent e) {
		FormPoint p = getCursorPoint(e);
		statusBar.setCursorPosition(p);
		return p;
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		showCursorPosition(e);
	}

	// MouseInputListener
	@Override
	public void mouseClicked(MouseEvent e) {
		switch (imagePanel.getMode()) {
		case MODIFY_POINTS:
			if (e.getButton() == MouseEvent.BUTTON3) {
				model.deleteNearestPointTo(getCursorPoint(e));
			}
			imagePanel.repaint();
			break;
		case VIEW:
		default:
			break;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		buttonPressed = e.getButton();
		switch (imagePanel.getMode()) {
		case SETUP_AREA:
		case SETUP_POINTS:
		case MODIFY_POINTS:
			if (!e.isControlDown() && (buttonPressed == MouseEvent.BUTTON1)) {
				FormPoint p = getCursorPoint(e);
				// Corners corner = imagePanel.getSelectedButton();
				//
				// if (corner != null) {
				// FormTemplate template = imagePanel.getTemplate();
				// template.setCorner(corner, p);
				// imagePanel.showCornerPosition();
				// imagePanel.repaint();
				// } else {
				imagePanel.setTemporaryPoint(p);
				imagePanel.repaint();
				// }
				break;
			}
		case VIEW:
			if (e.isControlDown()) {
				p1 = new FormPoint(e.getPoint());
				imagePanel.setImageCursor(new Cursor(Cursor.HAND_CURSOR));
			}
		default:
			break;
		}

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		switch (imagePanel.getMode()) {
		case SETUP_AREA:
		case SETUP_POINTS:
		case MODIFY_POINTS:
			if (!e.isControlDown() && (buttonPressed == MouseEvent.BUTTON1)) {
				FormPoint p = getCursorPoint(e);
				// Corners corner = imagePanel.getSelectedButton();
				// if (corner != null) {
				// FormTemplate template = imagePanel.getTemplate();
				// template.setCorner(corner, p);
				// imagePanel.showCornerPosition();
				// imagePanel.repaint();
				// imagePanel.resetCornerButtons();
				// } else {
				imagePanel.clearTemporaryPoint();
				model.addPoint(imagePanel, p);
				// }
				break;
			}
		case VIEW:
			if (e.isControlDown()) {
				imagePanel.setImageCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			}
		default:
			break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		imagePanel.setImageCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
	}

	@Override
	public void mouseExited(MouseEvent e) {
		imagePanel.setImageCursor(new Cursor(Cursor.DEFAULT_CURSOR));
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int delta = e.getUnitsToScroll() * 5;

		if (e.isControlDown()) {
			imagePanel.setScrollBars(delta, 0);
		} else {
			imagePanel.setScrollBars(0, delta);
		}

		showCursorPosition(e);
	}

	private FormPoint getCursorPoint(MouseEvent e) {
		double zoom = imageToolBar.getZoom(imagePanel);
		int dx = imagePanel.getHorizontalScrollbarValue(zoom);
		int dy = imagePanel.getVerticalScrollbarValue(zoom);

		int x = (int) (e.getX() / zoom) + dx;
		int y = (int) (e.getY() / zoom) + dy;

		FormPoint point = new FormPoint(x, y);
		return point;
	}

	// @Override
	// public void actionPerformed(ActionEvent e) {
	// Corners corner = Corners.valueOf(e.getActionCommand());
	// imagePanel.toggleCornerButton(corner);
	// }

	@Override
	public void itemStateChanged(ItemEvent e) {
		if ((imagePanel != null) && (e.getStateChange() == ItemEvent.SELECTED)) {
			imagePanel.setZoom(imageToolBar);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
	}
}
