package org.albertoborsetta.formscanner.newparser;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class ProcessImage {

	private static BufferedImage image;

	public static void main(String args[]) {
        String fileName = args[0];        
        
        try {
        	File file = new File(fileName);
        	image = ImageIO.read(file);
        } catch (Exception e) {
        	image = null;
        }
        
        BufferedImage image1 = image.getSubimage(0,	0, (image.getWidth()/8), (image.getHeight()/8));
        
        ScanImage imageScan = new ScanImage(image1);
        imageScan.locateCorners();
    }   

}