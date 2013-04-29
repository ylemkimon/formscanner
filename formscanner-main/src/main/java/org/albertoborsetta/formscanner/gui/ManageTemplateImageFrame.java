package org.albertoborsetta.formscanner.gui;

import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.gui.controller.InternalFrameController;
import org.albertoborsetta.formscanner.gui.controller.ManageTemplateImageController;
import org.albertoborsetta.formscanner.model.FormScannerModel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;


public class ManageTemplateImageFrame extends JInternalFrame implements ImageView {
	
	private static final long serialVersionUID = 1L;
	
	private ImagePanel imagePanel;
	private FormScannerModel formScannerModel;
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
		
		setClosable(true);
		setIconifiable(true);
		setResizable(true);
		setMaximizable(true);
		
		setName(FormScannerConstants.MANAGE_TEMPLATE_IMAGE_FRAME_NAME);	
		setTitle(formScannerModel.getTranslationFor(FormScannerTranslationKeys.MANAGE_TEMPLATE_FRAME_TITLE));					
		
		imagePanel = new ImagePanel(file);
		getContentPane().add(imagePanel, BorderLayout.CENTER);
		
		setBounds(220, 10, imagePanel.getWidth() + 100, desktopHeight - 20);
	}
	
	public void addZoom(ZoomImageFrame zoom) {
		this.zoom = zoom;
	} 
	
	public ZoomImageFrame getZoom() {
		return zoom;
	}
	
	private class ImagePanel extends JPanel {
		
		private int width;
		private int height;
		private double scaleFactor;
		private int rectX = 0;
		private int rectY = 0;
		private int rectWidth = 0;
		private int rectHeight = 0;
		
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private BufferedImage image;
		
		public ImagePanel(File file) {
			super();
			setImage(file);
			scaleFactor = (formScannerModel.getDesktopSize().height - 100) / (double) image.getHeight();
			width = (int) Math.floor(image.getWidth() * scaleFactor);
			height = (int) Math.floor(image.getHeight() * scaleFactor);
			addMouseMotionListener(manageTemplateImageController);
			addMouseListener(manageTemplateImageController);
		}
		
		@Override
	    public void paintComponent(Graphics g) {
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
		
		public int getWidth() {
			return width;
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
	
	public double getScaleFactor() {
		return imagePanel.getScaleFactor();
	}
	
	public void setScaleFactor(double scaleFactor) {
		imagePanel.setScaleFactor(scaleFactor);
	}
	
	@Override
	public void updateImage(File file) {
		imagePanel.setImage(file);
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
}