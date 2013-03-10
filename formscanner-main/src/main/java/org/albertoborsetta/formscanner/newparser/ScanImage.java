/*
 * InputImage.java
 *
 * Created on June 30, 2007, 9:56 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.albertoborsetta.formscanner.newparser;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.VolatileImage;
import java.awt.image.WritableRaster;

public class ScanImage {
	private BufferedImage image;
    private int height, width;
    
    public ScanImage(BufferedImage image) {
    	this.image = image;
        
        height = image.getHeight();
        width = image.getWidth();
    }
    
    public void locateCorners() {
        int[] topleft = new int[((int)(height/4) + 1) * ((int)(width/4) + 1)];
        int[] bottomright = new int[((int)(height/4) + 1) * ((int)(width/4) + 1)];
        Raster raster = image.getRaster();
        raster.getSamples(0, 0, (int)(width/4)+1, (int)(height/4), 0, topleft);
        raster.getSamples(width - (int)(width/4) - 1, height - (int)(height/4) - 1, (int)(width/4) + 1, (int)(height/4) + 1, 0, bottomright);
        
        BufferedImage leftimg = new BufferedImage((int)(width/4) + 1, (int)(height/4) + 1, BufferedImage.TYPE_BYTE_BINARY);
        WritableRaster leftraster = leftimg.getRaster();
        leftraster.setSamples(0, 0, (int)(width/4) + 1, (int)(height/4) + 1, 0, topleft);
        
        BufferedImage rightimg = new BufferedImage((int)(width/4) + 1, (int)(height/4) + 1, BufferedImage.TYPE_BYTE_BINARY);
        WritableRaster rightraster = rightimg.getRaster();
        rightraster.setSamples(0, 0, (int)(width/4) + 1, (int)(height/4) + 1, bottomright);

        topleftpos = new ConcentricCircle(topleftimg, width, height);
        topleftpos.process();
        bottomrightpos = new ConcentricCircle(bottomrightimg, width, height);
        bottomrightpos.process();

        bottomrightpos.getBestFit().setX(width - (int)(width/4) - 1 + bottomrightpos.getBestFit().getX());
        bottomrightpos.getBestFit().setY(height - (int)(height/4) - 1 + bottomrightpos.getBestFit().getY());
        
        topleftX = topleftpos.getBestFit().getX() + topleftpos.getBestFit().getTemplate().getWidth() / 2;
        topleftY = topleftpos.getBestFit().getY() + topleftpos.getBestFit().getTemplate().getHeight() / 2;
        ImageUtil.putMark(grayimage, topleftX, topleftY, true);
        
        bottomrightX = bottomrightpos.getBestFit().getX() + bottomrightpos.getBestFit().getTemplate().getWidth() / 2;
        bottomrightY = bottomrightpos.getBestFit().getY() + bottomrightpos.getBestFit().getTemplate().getHeight() / 2;
        ImageUtil.putMark(grayimage, bottomrightX, bottomrightY, true);
        
        currAngle = Math.toDegrees(Math.atan2((bottomrightX - topleftX), 
                (bottomrightY - topleftY)));
        currDiag = Math.sqrt(
                Math.pow((bottomrightY - topleftY), 2) + Math.pow((bottomrightX - topleftX), 2));
        */
    }
}
