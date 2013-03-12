/*
 * InputImage.java
 *
 * Created on June 30, 2007, 9:56 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.albertoborsetta.formscanner.newparser;

import java.awt.image.BufferedImage;

import ij.ImagePlus;
import ij.process.ImageProcessor;

public class ScanImage {
	private ImagePlus image;
	private ImageProcessor ip;
    private int height, width;
    
    public ScanImage(BufferedImage img) {    	
    	image = new ImagePlus();
    	image.setImage(img);
    	ip = image.getProcessor();    	
    	
    	height = image.getHeight();
    	width = image.getWidth();
    }
    
    public void locateCorners() {
    	CornerDetector hcd = new CornerDetector(ip, CornerDetector.TOP_LEFT); //, CornerDetector.DEFAULT_ALPHA, CornerDetector.DEFAULT_THRESHOLD);
    	hcd.findCorners();
    	ImageProcessor result = hcd.showCornerPoints(ip);
    	ImagePlus win = new ImagePlus("Corners from ", result);
    	win.show();

    }
}
