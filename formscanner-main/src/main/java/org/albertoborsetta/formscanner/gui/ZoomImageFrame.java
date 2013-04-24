package org.albertoborsetta.formscanner.gui;

import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.controller.ManageTemplateImageController;
import org.albertoborsetta.formscanner.controller.InternalFrameController;
import org.albertoborsetta.formscanner.model.FormScannerModel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;


public class ZoomImageFrame extends JInternalFrame {
	
	private static final long serialVersionUID = 1L;
	
	private ImagePanel imagePanel;
	private FormScannerModel formScannerModel;
	private ManageTemplateImageController templateImageController;
	private InternalFrameController internalFrameController;
	

	/**
	 * Create the frame.
	 */
	public ZoomImageFrame(FormScannerModel formScannerModel, File file) {
		this.formScannerModel = formScannerModel;
		// templateImageController = new ManageTemplateImageController(formScannerModel);
		// templateImageController.add(this);
		internalFrameController = InternalFrameController.getInstance(formScannerModel);
		addInternalFrameListener(internalFrameController);
		
		setClosable(false);
		setIconifiable(false);
		setResizable(false);
		setMaximizable(false);
		
		setName(FormScannerConstants.ZOOM_IMAGE_FRAME_NAME);	
		setTitle(formScannerModel.getTranslationFor(FormScannerTranslationKeys.ZOOM_IMAGE_FRAME_TITLE));					
		
		imagePanel = new ImagePanel(file);
		getContentPane().add(imagePanel, BorderLayout.CENTER);
		
		setBounds(220, 10, 400, 320);
	}
	
	private class ImagePanel extends JPanel {
		
		private int x = 0;
		private int y = 0;
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private BufferedImage image;
		
		public ImagePanel(File file) {
			super();
			setImage(file);
		}
		
		@Override
	    public void paintComponent(Graphics g) {
	        g.drawImage(image, 0-x, 0-y, this);
	        g.setColor(Color.white);
	        g.fillRoundRect(198, 137, 4, 10, 2, 2);
	        g.fillRoundRect(203, 148, 10, 4, 2, 2);
	        g.fillRoundRect(198, 153, 4, 10, 2, 2);
	        g.fillRoundRect(187, 148, 10, 4, 2, 2);
	        g.setColor(Color.black);
	        g.drawRoundRect(198, 137, 4, 10, 2, 2);
	        g.drawRoundRect(203, 148, 10, 4, 2, 2);
	        g.drawRoundRect(198, 153, 4, 10, 2, 2);
	        g.drawRoundRect(187, 148, 10, 4, 2, 2);
	        
	    }
		
		public void setImage(File file) {
			try {		
				image = ImageIO.read(file);
			} catch (IOException ex) {
				image = null;
			}
		}
		
		public void setPosition(int x, int y) {
			this.x = x - 200;
			this.y = y - 150;
		}
	}
	
	public void updateImage(File file) {
		imagePanel.setImage(file);
		update(getGraphics());
	}
	
	public void setImageCursor(int moveCursor) {
		Cursor cursor = Cursor.getPredefinedCursor(moveCursor);
		imagePanel.setCursor(cursor);
	}
	
	public void showImage(int x, int y) {
		imagePanel.setPosition(x, y);
		update(getGraphics());
	}
}
