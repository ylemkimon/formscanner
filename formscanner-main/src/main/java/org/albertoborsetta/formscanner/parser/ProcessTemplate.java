/*
 * ProcessTemplate.java
 *
 * Created on June 30, 2007, 12:12 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.albertoborsetta.formscanner.parser;

import net.sourceforge.jiu.data.Gray8Image;

/**
 *
 * @author Aaditeshwar Seth
 */
public class ProcessTemplate {

    public static void main(String args[]) {
        String filename = args[0];

        Gray8Image grayimage = ImageUtil.readImage(filename);
        
        ImageManipulation image = new ImageManipulation(grayimage);
        image.locateConcentricCircles();
        image.locateMarks();
        
        image.writeAscTemplate(filename + ".asc");
        image.writeConfig(filename + ".config");
    }    
}
