package org.albertoborsetta.formscanner.gui;

import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.gui.controller.AnalyzeFileImageController;
import org.albertoborsetta.formscanner.gui.controller.InternalFrameController;
import org.albertoborsetta.formscanner.model.FormScannerModel;

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


public class AnalyzeFileImageFrame extends JInternalFrame implements ScrollableImageView {
	
	private static final long serialVersionUID = 1L;
	
	private ImagePanel imagePanel;
	private ImageScrollPane scrollPane;
	private FormScannerModel formScannerModel;
	private AnalyzeFileImageController analyzeImageController;
	private InternalFrameController internalFrameController;
	

	/**
	 * Create the frame.
	 */
	public AnalyzeFileImageFrame(FormScannerModel formScannerModel, File file) {
		this.formScannerModel = formScannerModel;
		analyzeImageController = new AnalyzeFileImageController(formScannerModel);
		analyzeImageController.add(this);
		internalFrameController = InternalFrameController.getInstance(formScannerModel);
		addInternalFrameListener(internalFrameController);
		
		int desktopHeight = formScannerModel.getDesktopSize().height;	
		
		setClosable(true);
		setIconifiable(true);
		setResizable(true);
		setMaximizable(true);
		
		setName(FormScannerConstants.ANALYZE_IMAGE_FRAME_NAME);	
		setTitle(formScannerModel.getTranslationFor(FormScannerTranslationKeys.ANALYZE_FILE_FRAME_TITLE));					
		
		imagePanel = new ImagePanel(file);
		scrollPane = new ImageScrollPane(imagePanel);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		setBounds(220, 10, imagePanel.getWidth() + 100, desktopHeight - 20);
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
			addMouseMotionListener(analyzeImageController);
			addMouseListener(analyzeImageController);
		}
		
		public void setScrollBars(int deltaX, int deltaY) {
			horizontalScrollBar.setValue(horizontalScrollBar.getValue() + deltaX);
			verticalScrollBar.setValue(verticalScrollBar.getValue() + deltaY);			
		}
	}
	
	private class ImagePanel extends JPanel {
		
		private int width;
		private int height;
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		private BufferedImage image;
		
		public ImagePanel(File file) {
			super();
			setImage(file);
			double scaleFactor = (formScannerModel.getDesktopSize().height - 100) / (double) image.getHeight();
			width = (int) Math.floor(image.getWidth() * scaleFactor);
			height = (int) Math.floor(image.getHeight() * scaleFactor);
		}
		
		@Override
	    public void paintComponent(Graphics g) {
			double scaleFactor = (formScannerModel.getDesktopSize().height - 100) / (double) image.getHeight();
			width = (int) Math.floor(image.getWidth() * scaleFactor);
			height = (int) Math.floor(image.getHeight() * scaleFactor);
			setPreferredSize(new Dimension(width, height));
	        g.drawImage(image, 0, 0, width, height, this);            
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
	}
	
	@Override
	public void updateImage(File file) {
		imagePanel.setImage(file);
		update(getGraphics());
	}
	
	@Override
	public void setScrollBars(int deltaX, int deltaY) {
		scrollPane.setScrollBars(deltaX, deltaY);
	}

	@Override
	public void setImageCursor(Cursor cursor) {
		scrollPane.setCursor(cursor);
	}

	@Override
	public void drawRect(int x, int y, int width, int height) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void zoomImage(double zoom) {
		// TODO Auto-generated method stub
	}

	@Override
	public double getScaleFactor() {
		// TODO Auto-generated method stub
		return 0;
	}
}
