package org.albertoborsetta.formscanner.gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.ScrollPane;

import javax.imageio.ImageIO;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.border.BevelBorder;

import net.sourceforge.jiu.gui.awt.ImageCanvas;

import org.albertoborsetta.formscanner.gui.font.FormScannerFont;
import org.albertoborsetta.formscanner.model.FormScannerModel;
import org.apache.commons.io.FilenameUtils;
import javax.media.jai.NullOpImage;
import javax.media.jai.OpImage;
import com.sun.media.jai.codec.SeekableStream;
import com.sun.media.jai.codec.FileSeekableStream;
import com.sun.media.jai.codec.TIFFDecodeParam;
import com.sun.media.jai.codec.ImageDecoder;
import com.sun.media.jai.codec.ImageCodec;

public class RenameFileImageFrame extends JInternalFrame {
	private JPanel imagePanel;
	private ImageCanvas imageCanvas; 
	private JLabel statusBar;
	private FormScannerModel model;

	/**
	 * Create the frame.
	 */
	public RenameFileImageFrame(FormScannerModel formScannerModel, File file) {
		
		setBounds(100, 100, 756, 268);
		
		imagePanel = new ImagePanel(file);
		getContentPane().add(imagePanel, BorderLayout.CENTER);
		
		statusBar = new StatusBar("Renaming: " + file.getName()); 
		getContentPane().add(statusBar, BorderLayout.SOUTH);
	}
	
	private class ImagePanel extends JPanel {
		
		private Image image;
		
		public ImagePanel(File file) {
			super();
			
			try {
				if (FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("tif") || 
						FilenameUtils.getExtension(file.getName()).equalsIgnoreCase("tiff")) {
					SeekableStream stream = new FileSeekableStream(file);
					TIFFDecodeParam parameters = null;
					ImageDecoder decoder = ImageCodec.createImageDecoder("tiff", stream, parameters);
					RenderedImage op = new NullOpImage(decoder.decodeAsRenderedImage(0),
				                            null,
				                            null,
				                            OpImage.OP_IO_BOUND);
					image = new BufferedImage( op.getWidth(), op.getHeight(), BufferedImage.TYPE_INT_ARGB );
					
				} else {				
					image = ImageIO.read(file);
				}
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
	
	private class StatusBar extends JLabel {
		
		public StatusBar(String label) {
			super(label);
			setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			setFont(FormScannerFont.getFont());
		}
	}
}
