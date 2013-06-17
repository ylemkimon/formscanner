package org.albertoborsetta.formscanner.gui;

import org.albertoborsetta.formscanner.commons.FormPoint;
import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Corners;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Mode;
import org.albertoborsetta.formscanner.commons.FormScannerFont;
import org.albertoborsetta.formscanner.commons.FormTemplate;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.gui.controller.InternalFrameController;
import org.albertoborsetta.formscanner.gui.controller.ImageFrameController;
import org.albertoborsetta.formscanner.model.FormScannerModel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

public class ImageFrame extends JInternalFrame implements ScrollableImageView {
	
	private static final long serialVersionUID = 1L;
	
	private FormScannerModel formScannerModel;
	private ImagePanel imagePanel;
	private ImageScrollPane scrollPane;
	private ImageFrameController controller;
	private InternalFrameController frameController;
	private Mode mode;

	// private ImageStatusBar statusBar;
	

	/**
	 * Create the frame.
	 */	
	public ImageFrame(FormScannerModel model, FormTemplate template, Mode mode) {
		formScannerModel = model;
		controller = new ImageFrameController(formScannerModel);
		controller.add(this);
		frameController = InternalFrameController.getInstance(formScannerModel);
		addInternalFrameListener(frameController);
		
		int desktopHeight = formScannerModel.getDesktopSize().height;
		int desktopWidth = formScannerModel.getDesktopSize().width;
		
		setClosable(true);
		setIconifiable(true);
		setResizable(true);
		setMaximizable(true);
		
		setName(FormScannerConstants.IMAGE_FRAME_NAME);	
		setTitle(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.IMAGE_FRAME_TITLE));					
		
		setBounds(320, 10, desktopWidth - 500, desktopHeight - 20);
		
		imagePanel = new ImagePanel(template.getImage());
		scrollPane = new ImageScrollPane(imagePanel);
		getContentPane().add(scrollPane, BorderLayout.CENTER);		
		
		this.mode = mode;
	}
	
	private class ImageScrollPane extends JScrollPane {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ImageScrollPane(JPanel imagePanel) {
			super(imagePanel);
			verticalScrollBar.setValue(0);
			horizontalScrollBar.setValue(0);
			setWheelScrollingEnabled(false);
			addMouseMotionListener(controller);
			addMouseListener(controller);
		}
		
		public void setScrollBars(int deltaX, int deltaY) {
			horizontalScrollBar.setValue(horizontalScrollBar.getValue() + deltaX);
			verticalScrollBar.setValue(verticalScrollBar.getValue() + deltaY);			
		}
		
		public int getHorizontalScrollBarValue() {
			return horizontalScrollBar.getValue();
		}
		
		public int getVerticalScrollBarValue() {
			return verticalScrollBar.getValue();
		}
	}
	
	private class ImagePanel extends JPanel {
		
		private int width;
		private int height;
		private int marker = 20;
		private FormTemplate template;
		private FormPoint tempPoint;
		private BufferedImage image;
		
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public ImagePanel(File file) {
			super();
			setImage(file);
			setFont(FormScannerFont.getFont());
		}
		
		@Override
	    public void paintComponent(Graphics g) {
			width = image.getWidth();
			height = image.getHeight();
			setPreferredSize(new Dimension(width, height));
			g.drawImage(image, 0, 0, width, height, this);
			showPoints(g);
			showCorners(g);
			showTemporaryPoint(g);
	    } 
		
		public void showPoints(Graphics g) {
			g.setColor(Color.RED);
			
			for (FormPoint point: template.getFieldPoints()) {			
				FormPoint p1 = calculatePointPosition(point);
				for (int i=0; i<2; i++) {
					int d = (int) (Math.pow(-1, i) * marker);
					g.drawLine(p1.x-d, p1.y+d, p1.x+d, p1.y+d);
					g.drawLine(p1.x+d, p1.y+d, p1.x+d, p1.y-d);
				}
			}
			
			g.setColor(Color.BLACK);
		}
		
		public void showTemporaryPoint(Graphics g) {
			if (tempPoint != null) {
				g.setColor(Color.RED);
				
				FormPoint p1 = calculatePointPosition(tempPoint);
				for (int i=0; i<2; i++) {
					int d = (int) (Math.pow(-1, i) * 10);
					g.drawLine(p1.x-d, p1.y+d, p1.x+d, p1.y+d);
					g.drawLine(p1.x+d, p1.y+d, p1.x+d, p1.y-d);
				}
	
				g.setColor(Color.BLACK);
			}
		}
		
		public void showCorners(Graphics g) {
			HashMap<Corners, FormPoint> corners = template.getCorners();
			g.setColor(Color.GREEN);
			
			for (int i=0; i<Corners.values().length; i++) {
				FormPoint p1 = calculatePointPosition(corners.get(Corners.values()[i%Corners.values().length]));
				FormPoint p2 = calculatePointPosition(corners.get(Corners.values()[(i+1)%Corners.values().length]));
				
				g.drawLine(p1.x, p1.y, p2.x, p2.y);
			}
			
			g.setColor(Color.BLACK);
		}
		
		private FormPoint calculatePointPosition(FormPoint p) {
			int dx = scrollPane.getHorizontalScrollBarValue();
			int dy = scrollPane.getVerticalScrollBarValue();
			
			int x = p.x-dx;
			int y = p.y-dy;
			
			FormPoint p1 = new FormPoint(x, y);
			return p1;
		}
		
		public void setImage(File file) {
			try {		
				image = ImageIO.read(file);
			} catch (IOException ex) {
				image = null;
			}
		}
		
		public int getImageWidth() {
			return width;
		}
		
		public int getImageHeight() {
			return height;
		}

		public void addTemporaryPoint(FormPoint point) {
			tempPoint = point;
		}

		public void removeTemporaryPoint() {
			tempPoint = null;
		}

		public FormPoint getTemporaryPoint() {
			return tempPoint;
		}

		public void setTemplate(FormTemplate template) {
			this.template = template;
		}
	}
	
	public void updateImage(File file) {
		imagePanel.setImage(file);
		update(getGraphics());
	}
	
	public void addTemporaryPoint(FormPoint point) {
		imagePanel.addTemporaryPoint(point);
		update(getGraphics());
	}

	public void removeTemporaryPoint() {
		imagePanel.removeTemporaryPoint();
	}
	public void setImageCursor(Cursor cursor) {
		imagePanel.setCursor(cursor);
	}

	public void setScrollBars(int deltaX, int deltaY) {
		scrollPane.setScrollBars(deltaX, deltaY);
		update(getGraphics());
	}
	
	public int getHorizontalScrollbarValue() {
		return scrollPane.getHorizontalScrollBarValue();
	}
	
	public int getVerticalScrollbarValue() {
		return scrollPane.getVerticalScrollBarValue();
	}

	public Mode getMode() {
		return mode;
	}

	public Dimension getImageSize() {
		return new Dimension(imagePanel.getImageWidth(), imagePanel.getImageHeight());
	}
	
	public FormPoint getTemporaryPoint() {
		return imagePanel.getTemporaryPoint();
	}

	public void setTemplate(FormTemplate template) {
		imagePanel.setTemplate(template);
	}
}