package org.albertoborsetta.formscanner.gui;

import java.awt.Graphics;
import java.awt.ScrollPane;

import javax.imageio.ImageIO;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

import net.sourceforge.jiu.gui.awt.ImageCanvas;

import org.albertoborsetta.formscanner.gui.font.FormScannerFont;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class RenameFileImageFrame extends JInternalFrame {
	private JPanel imagePanel;
	private ScrollPane imageScrollPane;
	private ImageCanvas imageCanvas; 
	private JLabel statusBar;
	private FormScannerModel model;

	/**
	 * Create the frame.
	 */
	public RenameFileImageFrame(FormScannerModel formScannerModel, File file) {
		BufferedImage image;
		
		setBounds(100, 100, 756, 268);
		
		// imagePanel = new ImagePanel(file);
		imageScrollPane = new ImageScrollPane();
		imageCanvas = new ImageCanvas(imageScrollPane);
		
		try {                
			image = ImageIO.read(file);
		} catch (IOException ex) {
			image = null;
		}
		
		imageCanvas.setImage(image);
		imageCanvas.update(getGraphics());
		imageScrollPane.add(imageCanvas);
		getContentPane().add(imageScrollPane, BorderLayout.CENTER);
		// getContentPane().add(imagePanel, BorderLayout.CENTER);
		
		statusBar = new StatusBar("Renaming: " + file.getName()); 
		getContentPane().add(statusBar, BorderLayout.SOUTH);
	}
	
	private class ImagePanel extends JPanel {
		
		private BufferedImage image;
		
		public ImagePanel(File file) {
			super();
			
			try {                
				image = ImageIO.read(file);
			} catch (IOException ex) {
				// handle exception...
			}		
		}
		
		@Override
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters            
	    }
	}
	
	private class ImageScrollPane extends ScrollPane {
		
		public ImageScrollPane() {
			super();		
		}
	}
	
	private class StatusBar extends JLabel {
		
		public StatusBar(String label) {
			super(label);
			setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			setFont(FormScannerFont.getFont());
		}
	}
}
