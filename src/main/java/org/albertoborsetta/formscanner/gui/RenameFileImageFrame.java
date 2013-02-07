package org.albertoborsetta.formscanner.gui;

import java.awt.Image;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;

import org.albertoborsetta.formscanner.model.FormScannerModel;

import net.sourceforge.jiu.data.Gray8Image;
import net.sourceforge.jiu.gui.awt.ImageCanvas;

public class RenameFileImageFrame extends JInternalFrame {

	/**
	 * Create the frame.
	 */
	public RenameFileImageFrame(BufferedImage image) {
		setBounds(100, 100, 756, 268);
		
		JLabel lblNewLabel = new JLabel("New label");
		getContentPane().add(lblNewLabel, BorderLayout.SOUTH);
		
		JScrollPane scrollPane = new JScrollPane();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		scrollPane.paintComponents(g);
		g.drawImage(image, 0, 0, null);
	}

	    @Override
	    public void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        g.drawImage(image, 0, 0, null); // see javadoc for more info on the parameters            
	    }

	}

}
