package org.albertoborsetta.formscanner.gui;

import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.FormScannerFont;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.gui.controller.InternalFrameController;
import org.albertoborsetta.formscanner.gui.controller.ManageTemplateImageController;
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


public class ManageTemplateImageFrame extends JInternalFrame implements ScrollableImageView {
	
	private static final long serialVersionUID = 1L;
	
	private ImagePanel imagePanel;
	private FormScannerModel formScannerModel;
	private ImageScrollPane scrollPane;
	private ManageTemplateImageController manageTemplateImageController;
	private InternalFrameController internalFrameController;
	private ZoomImageFrame zoom;
	

	/**
	 * Create the frame.
	 */	
	public ManageTemplateImageFrame(FormScannerModel formScannerModel, File file) {
		this.formScannerModel = formScannerModel;
		manageTemplateImageController = new ManageTemplateImageController(formScannerModel);
		manageTemplateImageController.add(this);
		internalFrameController = InternalFrameController.getInstance(formScannerModel);
		addInternalFrameListener(internalFrameController);
		
		int desktopHeight = formScannerModel.getDesktopSize().height;
		int desktopWidth = formScannerModel.getDesktopSize().width;
		
		setClosable(true);
		setIconifiable(true);
		setResizable(true);
		setMaximizable(true);
		
		setName(FormScannerConstants.MANAGE_TEMPLATE_IMAGE_FRAME_NAME);	
		setTitle(formScannerModel.getTranslationFor(FormScannerTranslationKeys.MANAGE_TEMPLATE_FRAME_TITLE));					
		
		setBounds(320, 10, desktopWidth - 500, desktopHeight - 20);
		
		imagePanel = new ImagePanel(file);
		scrollPane = new ImageScrollPane(imagePanel);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
	}
	
	public void addZoom(ZoomImageFrame zoom) {
		this.zoom = zoom;
	} 
	
	public ZoomImageFrame getZoom() {
		return zoom;
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
			addMouseMotionListener(manageTemplateImageController);
			addMouseListener(manageTemplateImageController);
			addMouseWheelListener(manageTemplateImageController);
		}
		
		public void setScrollBars(int deltaX, int deltaY) {
			horizontalScrollBar.setValue(horizontalScrollBar.getValue() + deltaX);
			verticalScrollBar.setValue(verticalScrollBar.getValue() + deltaY);			
		}
	}
	
	private class ImagePanel extends JPanel {
		
		private int width;
		private int height;
		private double scaleFactor = 1;
		private int rectX = 0;
		private int rectY = 0;
		private int rectWidth = 0;
		private int rectHeight = 0;
		private BufferedImage image;
		
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public ImagePanel(File file) {
			super();
			setImage(file);
			setFont(FormScannerFont.getFont());
			scaleFactor = 1;
		}
		
		@Override
	    public void paintComponent(Graphics g) {
			width = (int) Math.floor(image.getWidth() * scaleFactor);
			height = (int) Math.floor(image.getHeight() * scaleFactor);
			setPreferredSize(new Dimension(width, height));
			g.drawImage(image, 0, 0, width, height, this);
			g.setColor(Color.green);
	        g.drawRect(rectX, rectY, rectWidth, rectHeight);
	        g.setColor(Color.black);
	    } 
		
		public void setImage(File file) {
			try {		
				image = ImageIO.read(file);
			} catch (IOException ex) {
				image = null;
			}
		}
		
		public double getScaleFactor() {
			return scaleFactor;
		}
		
		public void setScaleFactor(double scaleFactor) {
			this.scaleFactor = scaleFactor;
		}
		
		public void setRect(int rectX, int rectY, int rectWidth, int rectHeight) {
			this.rectWidth = rectWidth;
			this.rectHeight = rectHeight;
			this.rectX = rectX;
			this.rectY = rectY;
		}
		
	}
	
	@Override
	public void updateImage(File file) {
		update(getGraphics());
	}
	
	@Override
	public void drawRect(int x, int y, int width, int height) {
		imagePanel.setRect(x, y, width, height);
		update(getGraphics());
	}

	@Override
	public void setImageCursor(Cursor cursor) {
		imagePanel.setCursor(cursor);
	}

	@Override
	public void setScrollBars(int deltaX, int deltaY) {
		scrollPane.setScrollBars(deltaX, deltaY);
	}
	
	@Override
	public void zoomImage(double zoom) {
		imagePanel.setScaleFactor(imagePanel.getScaleFactor() + zoom);
		update(getGraphics());
	}

	@Override
	public double getScaleFactor() {
		return imagePanel.getScaleFactor();
	}
}