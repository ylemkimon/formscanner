package org.albertoborsetta.formscanner.gui;

import org.albertoborsetta.formscanner.controller.InternalFrameController;
import org.albertoborsetta.formscanner.controller.RenameImageController;
import org.albertoborsetta.formscanner.model.FormScannerModel;

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
	
	private ImagePanel imagePanel;
	private ImageScrollPane scrollPane;
	private FormScannerModel model;
	private RenameImageController renameImageController;
	private InternalFrameController internalFrameController;
	private int scrollPositionX = 0;
	private int scrollPositionY = 0;
	

	/**
	 * Create the frame.
	 */
	public RenameFileImageFrame(FormScannerModel formScannerModel, File file) {
		model = formScannerModel;
		renameImageController = new RenameImageController(model);
		internalFrameController = InternalFrameController.getInstance(model);
		
		setClosable(true);
		setName("renameFileImageFrame");
		addInternalFrameListener(internalFrameController);
		
		setTitle("Rename file image");
		
		int desktopWidth = model.getDesktopSize().width;
		setBounds(220, 10, desktopWidth - 230, 300);
		
		imagePanel = new ImagePanel(file);
		scrollPane = new ImageScrollPane(imagePanel);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
	}
	
	private class ImageScrollPane extends JScrollPane {
		
		public ImageScrollPane(JPanel imagePanel) {
			super(imagePanel);
			verticalScrollBar.setValue(0);
			horizontalScrollBar.setValue(0);
			addMouseMotionListener(renameImageController);
			addMouseListener(renameImageController);
			setWheelScrollingEnabled(true);
		}
		
		public void setScrollBars(int deltaX, int deltaY) {
			horizontalScrollBar.setValue(horizontalScrollBar.getValue() + deltaX);
			verticalScrollBar.setValue(verticalScrollBar.getValue() + deltaY);			
		}
	}
	
	private class ImagePanel extends JPanel {
		
		private BufferedImage image;
		
		public ImagePanel(File file) {
			super();
			setImage(file);
			setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
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
}
