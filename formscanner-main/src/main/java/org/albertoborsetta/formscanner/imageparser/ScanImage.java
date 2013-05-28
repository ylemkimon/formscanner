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
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.albertoborsetta.formscanner.commons.FormPoint;
import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Corners;

import ij.ImagePlus;
import ij.process.ImageProcessor;

public class ScanImage {
	private BufferedImage image;
	private int height;
	private int width;
	private HashMap<Corners, FormPoint> corners = new HashMap<Corners, FormPoint>();
	
    public ScanImage(File file) {
    	try {
        	image = ImageIO.read(file);
        } catch (Exception e) {
        	image = null;
        }
    	
    	height = image.getHeight();
        width = image.getWidth();
    }
    
    public HashMap<Corners, FormPoint> locateCorners() {
    	ImagePlus imagePlus = new ImagePlus();
    	CornerDetector cornerDetector;
    	ImageProcessor imageProcessor;
    	FormPoint corner;
    	int x;
    	int y;
    	
    	int subImageHeight = height/8;
        int subImageWidth = width/8;
        
        int x1 = (width - (subImageWidth + 1)); 
        int y1 = (height - (subImageHeight + 1));
        
        for (Corners position: Corners.values()) {
        	x = 0;
        	y = 0;
        	
        	switch (position) {
        	case TOP_RIGHT:
        		x = x1;
        		break;
        	case BOTTOM_LEFT:
        		y = y1;
        		break;
        	case BOTTOM_RIGHT:
        		x = x1;
        		y = y1;
        		break;
        	default:
        		break;
        	}
        	
        	BufferedImage cornerImage = image.getSubimage(x, y, subImageWidth, subImageHeight); 	
        	imagePlus.setImage(cornerImage);
        	imageProcessor = imagePlus.getProcessor();
        	cornerDetector = new CornerDetector(imageProcessor, position);
        	corner = cornerDetector.findCorners();
        	corner.setX((int) (x+corner.getX()));
        	corner.setY((int) (y+corner.getY()));
        	corners.put(position, corner);
        }
        
		return corners;
    }
}
