/*
 * ProcessImage.java
 *
 * Created on June 29, 2007, 10:11 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.albertoborsetta.formscanner.jiuparser;

import net.sourceforge.jiu.data.Gray8Image;

/**
 *
 * @author Aaditeshwar Seth
 */
public class ProcessForm {
    
    public static void main(String[] args) {
        String imgfilename = args[0];
        String templatefilename = args[1];
        
        Gray8Image grayimage = ImageUtil.readImage(imgfilename);

        ImageManipulation image = new ImageManipulation(grayimage);
        image.locateConcentricCircles();

        image.readConfig(templatefilename + ".config");
        image.readFields(templatefilename + ".fields");
        image.readAscTemplate(templatefilename + ".asc");
        image.searchMarks();
        image.saveData(imgfilename + ".dat");
    }

}
