package org.albertoborsetta.formscanner.gui;

import java.awt.Graphics;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;

import org.albertoborsetta.formscanner.model.FormScannerModel;

public class RenameFileImageFrame extends JInternalFrame {
	private JPanel imagePanel;
	private FormScannerModel model;
	private Image image;

	/**
	 * Create the frame.
	 */
	public RenameFileImageFrame(FormScannerModel formScannerModel, File file) {
		model = formScannerModel;
		
		setClosable(true);
		
		int desktopWidth = model.getDesktopSize().width;
		setBounds(220, 10, desktopWidth - 230, 300);
		
		imagePanel = new ImagePanel(file);
		getContentPane().add(imagePanel, BorderLayout.CENTER);
	}
	
	private class ImagePanel extends JPanel {
		
		public ImagePanel(File file) {
			super();
			
			try {		
				image = ImageIO.read(file);
			} catch (IOException ex) {
				image = null;
			}		
		}
		
		@Override
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        g.drawImage(image, 50, 10, 200, 200, this);            
	    }
	}
	
	public void updateImage(File file) {
		try {		
			image = ImageIO.read(file);
		} catch (IOException ex) {
			image = null;
		}
		
		update(getGraphics());
	}
}
