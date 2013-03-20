/*
 * InputImage.java
 *
 * Created on June 30, 2007, 9:56 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.albertoborsetta.formscanner.imageparser;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import ij.ImagePlus;
import ij.process.ImageProcessor;

public class ScanImage {
	private BufferedImage image;
	private int height;
	private int width;
    
    public ScanImage(File file) {
    	try {
        	image = ImageIO.read(file);
        } catch (Exception e) {
        	image = null;
        }
    	
    	height = image.getHeight();
        width = image.getWidth();
    }
    
    public void locateCorners() {
    	ImagePlus imp = new ImagePlus();
    	ImagePlus win;
    	ImageProcessor ip;
    	ImageProcessor result;
        CornerDetector hcd;
        
        int subImageHeight = height/8;
        int subImageWidth = width/8;
        
        int x1 = (width - (subImageWidth + 1));
        int y1 = (height - (subImageHeight + 1));
        
        BufferedImage topLeftImg = image.getSubimage(0,	0, subImageWidth, subImageHeight);        
    	imp.setImage(topLeftImg);
    	ip = imp.getProcessor();
    	hcd = new CornerDetector(ip, CornerDetector.TOP_LEFT); //, CornerDetector.DEFAULT_ALPHA, CornerDetector.DEFAULT_THRESHOLD);
    	hcd.findCorners();
    	result = hcd.showCornerPoints(ip);
    	win = new ImagePlus("Corners from ", result);
    	win.show();
    	
    	
    	BufferedImage topRightImg = image.getSubimage(x1, 0, subImageWidth, subImageHeight);
    	imp.setImage(topRightImg);
    	ip = imp.getProcessor();
    	hcd = new CornerDetector(ip, CornerDetector.TOP_RIGHT); //, CornerDetector.DEFAULT_ALPHA, CornerDetector.DEFAULT_THRESHOLD);
    	hcd.findCorners();
    	result = hcd.showCornerPoints(ip);
    	win = new ImagePlus("Corners from ", result);
    	win.show();
    	
        BufferedImage bottomLeftImg = image.getSubimage(0, y1, subImageWidth, subImageHeight);
        imp.setImage(bottomLeftImg);
    	ip = imp.getProcessor();
    	hcd = new CornerDetector(ip, CornerDetector.BOTTOM_LEFT); //, CornerDetector.DEFAULT_ALPHA, CornerDetector.DEFAULT_THRESHOLD);
    	hcd.findCorners();
    	result = hcd.showCornerPoints(ip);
    	win = new ImagePlus("Corners from ", result);
    	win.show();
    	
        BufferedImage bottomRightImg = image.getSubimage(x1, y1, subImageWidth, subImageHeight);
        imp.setImage(bottomRightImg);
    	ip = imp.getProcessor();
    	hcd = new CornerDetector(ip, CornerDetector.BOTTOM_RIGHT); //, CornerDetector.DEFAULT_ALPHA, CornerDetector.DEFAULT_THRESHOLD);
    	hcd.findCorners();
    	result = hcd.showCornerPoints(ip);
    	win = new ImagePlus("Corners from ", result);
    	win.show();
    }
}
