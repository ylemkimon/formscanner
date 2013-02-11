package org.albertoborsetta.formscanner.gui;

import java.awt.Graphics;

import javax.imageio.ImageIO;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.albertoborsetta.formscanner.model.FormScannerModel;

public class RenameFileImageFrame extends JInternalFrame implements InternalFrameListener {
	private JPanel imagePanel;
	private FormScannerModel model;
	private BufferedImage image;

	/**
	 * Create the frame.
	 */
	public RenameFileImageFrame(FormScannerModel formScannerModel, File file) {
		model = formScannerModel;
		
		setClosable(true);
		setName("renameFileImageFrame");
		addInternalFrameListener(this);
		
		setTitle("Rename file image");
		
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
			setBounds(0, 0, image.getWidth(), image.getHeight());
		}
		
		@Override
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        int imgWidth = image.getWidth();
	        int imgHeight = image.getHeight();
	        int panelWidth = getWidth();
	        int panelHeight = getHeight();
	        g.drawImage(image, 0, 0, panelWidth, panelHeight, 
	        		0, 0, imgWidth, imgHeight, this);            
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

	public void internalFrameOpened(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void internalFrameClosing(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		model.disposeRelatedFrame(this);
	}

	public void internalFrameClosed(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void internalFrameIconified(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void internalFrameDeiconified(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void internalFrameActivated(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void internalFrameDeactivated(InternalFrameEvent e) {
		// TODO Auto-generated method stub
		
	}
}
