package org.albertoborsetta.formscanner.gui;

import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.controller.InternalFrameController;
import org.albertoborsetta.formscanner.controller.RenameImageController;
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


public class RenameFileImageFrame extends JInternalFrame {
	
	private static final long serialVersionUID = 1L;
	
	private ImagePanel imagePanel;
	private ImageScrollPane scrollPane;
	private FormScannerModel formScanneModel;
	private RenameImageController renameImageController;
	private InternalFrameController internalFrameController;
	

	/**
	 * Create the frame.
	 */
	public RenameFileImageFrame(FormScannerModel formScannerModel, File file) {
		this.formScanneModel = formScannerModel;
		renameImageController = new RenameImageController(formScanneModel);
		renameImageController.add(this);
		internalFrameController = InternalFrameController.getInstance(formScanneModel);
		
		setClosable(true);
		setName(FormScannerConstants.RENAME_FILE_IMAGE_FRAME_NAME);
		addInternalFrameListener(internalFrameController);
		setIconifiable(true);
		setResizable(true);
		setMaximizable(true);
		setTitle(formScanneModel.getTranslationFor(FormScannerTranslationKeys.RENAME_FILE_FRAME_TITLE));
		
		int desktopWidth = formScanneModel.getDesktopSize().width;
		setBounds(220, 10, desktopWidth - 230, 300);
		
		imagePanel = new ImagePanel(file);
		scrollPane = new ImageScrollPane(imagePanel);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
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
			addMouseMotionListener(renameImageController);
			addMouseListener(renameImageController);
		}
		
		public void setScrollBars(int deltaX, int deltaY) {
			horizontalScrollBar.setValue(horizontalScrollBar.getValue() + deltaX);
			verticalScrollBar.setValue(verticalScrollBar.getValue() + deltaY);			
		}
	}
	
	private class ImagePanel extends JPanel {
		
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
			setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
	        g.drawImage(image, 0, 0, this);            
	    }
		
		public void setImage(File file) {
			try {		
				image = ImageIO.read(file);
			} catch (IOException ex) {
				image = null;
			}
		}
	}
	
	public void updateImage(File file) {
		imagePanel.setImage(file);
		update(getGraphics());
	}
	
	public void setScrollBars(int deltaX, int deltaY) {
		scrollPane.setScrollBars(deltaX, deltaY);
	}
	
	public void setImageCursor(int moveCursor) {
		Cursor cursor = Cursor.getPredefinedCursor(moveCursor);
		scrollPane.setCursor(cursor);
	}
}
