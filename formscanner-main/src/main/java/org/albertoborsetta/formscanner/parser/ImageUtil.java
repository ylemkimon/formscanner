/*
 * ImageUtil.java
 *
 * Created on June 30, 2007, 7:55 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.albertoborsetta.formscanner.parser;

import net.sourceforge.jiu.codecs.CodecMode;
import net.sourceforge.jiu.codecs.ImageLoader;
import net.sourceforge.jiu.codecs.PNGCodec;
import net.sourceforge.jiu.color.reduction.RGBToGrayConversion;
import net.sourceforge.jiu.data.Gray8Image;
import net.sourceforge.jiu.data.PixelImage;
import net.sourceforge.jiu.data.RGBImage;
import net.sourceforge.jiu.data.GrayImage;

/**
 *
 * @author Aaditeshwar Seth
 */
public class ImageUtil {

    public static Gray8Image readImage(String filename) {
        Gray8Image grayimage = null;
        try {
            PixelImage image = ImageLoader.load(filename);
            if (image instanceof RGBImage) {
                RGBToGrayConversion rgbtogray = new RGBToGrayConversion();
        		rgbtogray.setInputImage(image);
        		// adjust this if needed
        		// rgbtogray.setColorWeights(0.3f, 0.3f, 0.4f);
        		rgbtogray.process();
        		grayimage = (Gray8Image)(rgbtogray.getOutputImage());
                    
            } else if (image instanceof GrayImage) {
                grayimage = (Gray8Image)(image);
            } else {
                grayimage = null;
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
            System.exit(-1);
        }

        return grayimage;
    }
    
    public static void saveImage(PixelImage img, String filename) {
        try {
            PNGCodec codec = new PNGCodec();    
            codec.setFile(filename, CodecMode.SAVE);
            codec.setImage(img);
            codec.setCompressionLevel(0);
            codec.process();
        } catch(Exception ex) {
            ex.printStackTrace(System.out);
        } 
    }
    
    public static void putMark(Gray8Image img, int x, int y, boolean color) {
        if(color) {
            img.putBlack(x, y);
            img.putBlack(x + 1, y + 1);
            img.putBlack(x - 1, y - 1);
            img.putBlack(x + 1, y);
            img.putBlack(x - 1, y);
            img.putBlack(x, y + 1);
            img.putBlack(x, y - 1);        
            img.putBlack(x + 1, y - 1);        
            img.putBlack(x - 1, y + 1);
            img.putBlack(x - 1, y - 1);        
        } else {
            img.putWhite(x, y);
            img.putWhite(x + 1, y + 1);
            img.putWhite(x - 1, y - 1);
            img.putWhite(x + 1, y);
            img.putWhite(x - 1, y);
            img.putWhite(x, y + 1);
            img.putWhite(x, y - 1);        
            img.putWhite(x + 1, y - 1);        
            img.putWhite(x - 1, y + 1);
            img.putWhite(x - 1, y - 1);        
        }
    }

}
