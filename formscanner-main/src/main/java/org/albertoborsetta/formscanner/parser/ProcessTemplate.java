/*
 * ProcessTemplate.java
 *
 * Created on June 30, 2007, 12:12 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.albertoborsetta.formscanner.parser;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.albertoborsetta.formscanner.newparser.ScanImage;

import net.sourceforge.jiu.data.Gray8Image;

/**
 *
 * @author Aaditeshwar Seth
 */
public class ProcessTemplate {	

    public static void main(String args[]) {
        String filename = args[0];
        BufferedImage image;

        Gray8Image grayimage = ImageUtil.readImage(filename);
        try {
        	File file = new File(filename);
			image = ImageIO.read(file);
		} catch (IOException ex) {
			image = null;
		}
        
        
        ImageManipulation imageMan = new ImageManipulation(grayimage);
        ScanImage imageScan = new ScanImage(image);
        imageMan.locateConcentricCircles();
        imageScan.locateCorners();
        // imageMan.locateMarks();
        
        // imageMan.writeAscTemplate(filename + ".asc");
        // imageMan.writeConfig(filename + ".config");
    }    
}
