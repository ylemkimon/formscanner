package org.albertoborsetta.formscanner.gui;

import org.albertoborsetta.formscanner.controller.RenameFileController;
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
	private JScrollPane scrollPane;
	private FormScannerModel model;
	private RenameFileController controller;

	/**
	 * Create the frame.
	 */
	public RenameFileImageFrame(FormScannerModel formScannerModel, File file) {
		model = formScannerModel;
		controller = RenameFileController.getInstance(model);
		
		setClosable(true);
		setName("renameFileImageFrame");
		addInternalFrameListener(controller);
		
		setTitle("Rename file image");
		
		int desktopWidth = model.getDesktopSize().width;
		setBounds(220, 10, desktopWidth - 230, 300);
		
		imagePanel = new ImagePanel(file);
		scrollPane = new JScrollPane(imagePanel);
		scrollPane.addMouseMotionListener(controller);
		scrollPane.addMouseListener(controller);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
	}
	
	private class ImagePanel extends JPanel {
		
		private BufferedImage image;
		private int x=0;
		private int y=0;
		
		public ImagePanel(File file) {
			super();
			setImage(file);
			setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
		}
		
		@Override
	    public void paintComponent(Graphics g) {
			setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
	        g.drawImage(image, x, y, this);            
	    }
		
		public void setImage(File file) {
			try {		
				image = ImageIO.read(file);
			} catch (IOException ex) {
				image = null;
			}
		}
		
		public void setPosition(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	public void updateImage(File file) {
		imagePanel.setImage(file);
		update(getGraphics());
	}
	
	public void moveImage(int x, int y) {
		imagePanel.setPosition(x, y);
		update(getGraphics());
	}
}
