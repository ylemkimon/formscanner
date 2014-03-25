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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.albertoborsetta.formscanner.commons.FormField;
import org.albertoborsetta.formscanner.commons.FormPoint;
import org.albertoborsetta.formscanner.commons.FormTemplate;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Corners;

import ij.ImagePlus;
import ij.process.ImageProcessor;

public class ImageUtils {
	    
    public static HashMap<Corners, FormPoint> findCorners(FormTemplate template) {
    	ImagePlus imagePlus = new ImagePlus();
    	CornerDetector cornerDetector;
    	ImageProcessor imageProcessor;
    	FormPoint corner;
    	
    	BufferedImage image = template.getImage();
    	int x;
    	int y;
    	int height = image.getHeight();
    	int width = image.getWidth();
    	int subImageWidth = width/8;
    	int subImageHeight = height/8;
    	int x1 = (width - (subImageWidth + 1)); 
    	int y1 = (height - (subImageHeight + 1));
    	HashMap<Corners, FormPoint> corners = new HashMap<Corners, FormPoint>();
        
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
        	corner.setLocation((x+corner.getX()), (y+corner.getY()));
			corners.put(position, corner);
        }
        
        return corners;
    }
    
    public static void findPoints(FormTemplate formTemplate, FormTemplate currentForm) {
    	HashMap<String, FormField> fields = new HashMap<String, FormField>();
    	ArrayList<FormPoint> pointList = new ArrayList<FormPoint>();    	
    	
    	BufferedImage image = currentForm.getImage();
		boolean found;
		HashMap<String, FormField> templateFields = formTemplate.getFields();
		ArrayList<String> fieldNames = new ArrayList<String>(templateFields.keySet()); 
		Collections.sort(fieldNames);
		
		for (String fieldName: fieldNames) { 
			FormField templateField = templateFields.get(fieldName);
			HashMap<String, FormPoint> templatePoints = templateField.getPoints();
			
			ArrayList<String> pointNames = new ArrayList<String>(templatePoints.keySet()); 
			Collections.sort(pointNames);
			found = false;
			
			for (String pointName: pointNames) {
				FormPoint responsePoint = calcResponsePoint(formTemplate, currentForm, templatePoints.get(pointName));
				
				double density = calcDensity(image, responsePoint);
				
				if (density > 0.6) {
					found = true;
					FormField filledField = getField(fields, templateField, fieldName);
					filledField.setPoint(pointName, responsePoint);
					fields.put(fieldName, filledField);
					pointList.add(responsePoint);
					if (!templateField.isMultiple()) {
						break;
					}
				}
			}
			
			if (!found) {
				FormField filledField = getField(fields, templateField, fieldName);
				filledField.setPoint("", null);
				fields.put(fieldName, filledField);
			}
		}
	}
    
    private static FormPoint calcResponsePoint(FormTemplate formTemplate, FormTemplate currentForm, FormPoint responsePoint) {
    	FormPoint point = responsePoint;
    	FormPoint templateOrigin = formTemplate.getCorners().get(Corners.TOP_LEFT);
		FormPoint currentOrigin = currentForm.getCorners().get(Corners.TOP_LEFT);
		double templateRotation = formTemplate.getRotation();
		double currentRotation = currentForm.getRotation();
				
		point.relativePositionTo(templateOrigin, templateRotation);
		point.originalPositionFrom(currentOrigin, currentRotation);
		return point;
	}

	private static FormField getField(HashMap<String, FormField> fields, FormField field, String fieldName) {
		FormField filledField = fields.get(fieldName);
		
		if (filledField == null) {
			 filledField = new FormField(fieldName);
			 filledField.setMultiple(field.isMultiple());
		}
		
		return filledField;		
	}

	private static double calcDensity(BufferedImage image, FormPoint responsePoint) {
		int IThreshold = 127;
		int offset = 0;
		int delta = 5;
		int width = 2*delta;
		int height =2*delta;
		int total = width * height;
		int[] rgbArray = new int[total];
		int count = 0;
		
		int xCoord = (int) responsePoint.getX();
		int yCoord = (int) responsePoint.getY();

		image.getRGB(xCoord-delta, yCoord-delta, width, height, rgbArray, offset, width);
		for (int i=0; i<width*height; i++) {
			if ((rgbArray[i] & (0xFF)) < IThreshold) {
				count++;
			}
		}
		return count / (double) total;
	}
	
	public static double calculateRotation(FormTemplate template) {
		HashMap<Corners, FormPoint> corners = template.getCorners();
		
		FormPoint topLeftPoint = corners.get(Corners.TOP_LEFT);
		FormPoint topRightPoint = corners.get(Corners.TOP_RIGHT);
		
		double dx = (double) (topRightPoint.getX() - topLeftPoint.getX());
		double dy = (double) (topLeftPoint.getY() - topRightPoint.getY());
		
		return Math.atan(dy/dx);
	}
}
