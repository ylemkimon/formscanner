/*
 * ImageRoutines.java
 *
 * Created on June 30, 2007, 12:13 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package org.albertoborsetta.formscanner.newparser;

import net.sourceforge.jiu.data.Gray8Image;
import net.sourceforge.jiu.data.MemoryGray8Image;
import net.sourceforge.jiu.filters.MedianFilter;

/**
 *
 * @author Aaditeshwar Seth
 */
public class ConcentricCircle {
    public static double a4width = 21.0;       // cm
    public static double a4height = 29.7;      // cm

    public static double circleOuter = 1.5;    // cm   1.44
    public static double circleInner = 0.6;    // cm   0.54

    public static double vertDist = 23.1;      // cm
    public static double horizDist = 16.8;     // cm
    public static double diagDist = 28.5;      // cm
    public static double markDiam = 0.3;       // cm
    
//    static double vertDist = 1821;        // pixels
//    static double horizDist = 1309;       // pixels
//    static double diagDist = Math.sqrt(vertDist * vertDist + horizDist * horizDist);    // pixels = 2242.7
//    static double markDiam = 25;                // pixels

    Gray8Image img;
    int bigimgWidth, bigimgHeight;
    BestFitCoords bestfit;

    public ConcentricCircle(Gray8Image img, int bigimgWidth, int bigimgHeight) {
        this.img = img;
        this.bigimgWidth = bigimgWidth;
        this.bigimgHeight = bigimgHeight;
    }
    
    public void process() {
        try {
            MedianFilter filter = new MedianFilter();
            filter.setArea((int)((bigimgWidth / 1700 * 15) / 2) * 2 + 1, 
                    (int)(bigimgHeight / 2339 * 15 / 2) * 2 + 1);
            filter.setInputImage(img);
            filter.process();
            img = (Gray8Image)(filter.getOutputImage());            
        } catch(Exception exe) {
            exe.printStackTrace(System.out);
        }

        // this results in a slight over-estimate due to extra borders put in by the scanner
        double approxXscale = bigimgWidth / a4width;      // 80.95 pixel/cm
        double approxYscale = bigimgHeight / a4height;    // 78.75 pixel/cm
        double aspectScale = approxXscale / approxYscale;
        double approxCircleOuterX = circleOuter * approxXscale;  // 121 pixels
        double approxCircleInnerX = circleInner * approxXscale;  // 48 pixels
        
        MemoryGray8Image template = new MemoryGray8Image((int)(approxCircleOuterX * 1.15) + 1, (int)(approxCircleOuterX / aspectScale * 1.15) + 1);
        fillTemplate(template, approxCircleOuterX, approxCircleInnerX, aspectScale);
        
        bestfit = new BestFitCoords(-1, -1, template, approxCircleOuterX, approxCircleInnerX, aspectScale);
        
        fitTemplate(); 
    }

    public BestFitCoords getBestFit() {
        return bestfit;
    }

    public Gray8Image getImg() {
        return img;
    }
    
    private void fillTemplate(Gray8Image templateimg, double outerdiamX, double innerdiamX, double aspect) {
        double centerX = templateimg.getWidth() / 2;
        double centerY = templateimg.getHeight() / 2;
        double outerrad = outerdiamX / 2;
        double innerrad = innerdiamX / 2;
        for(int i = 0; i < templateimg.getWidth(); i++) {
            for(int j = 0; j < templateimg.getHeight(); j++) {
                double dist = Math.sqrt((i - centerX) * (i-centerX) + (j - centerY) / aspect * (j - centerY) / aspect);
                if(dist <= outerrad && dist > innerrad) {
                    templateimg.putBlack(i, j);
                }
                else {
                    templateimg.putWhite(i, j);
                }
            }
        }
    }

    private void fitTemplate() {        
        int startX = 0, startY = 0;
        int endX = img.getWidth() - bestfit.getTemplate().getWidth(), endY = img.getHeight() - bestfit.getTemplate().getHeight();

        centerTemplate(startX, startY, endX, endY, 3);
        templateXOR(img, bestfit.getX(), bestfit.getY(), bestfit.getTemplate(), true);

        sizeTemplate();
        aspectTemplate();
        shiftTemplate();
        sizeTemplate();
        aspectTemplate();
        shiftTemplate();
        templateXOR(img, bestfit.getX(), bestfit.getY(), bestfit.getTemplate(), true);
    }
    
    private void centerTemplate(int startX, int startY, int endX, int endY, int granularity) {
        int stepX = bestfit.getTemplate().getWidth() / granularity;
        int stepY = bestfit.getTemplate().getHeight() / granularity;
        System.out.println("stepX = " + stepX + ": stepY = " + stepY);
        
        double maxsim = -1;
        int simi = -1, simj = -1;
        for(int i = startX; i <= endX; i += stepX) {
            for(int j = startY; j <= endY; j += stepY) {
                double currsim = 1.0 - templateXOR(img, i, j, bestfit.getTemplate(), false);
                System.out.println(i + ":" + j + ":" + currsim);
                if(maxsim == -1 || maxsim < currsim) {
                    maxsim = currsim;
                    simi = i; simj = j;
                }
            }
        }
        
        System.out.println("--- maxsim = " + maxsim + ":" + simi + ":" + simj);
        if(maxsim > 0.5) {
            if(stepX >= 4) {    // up to an accuracy of 2 pixels
                centerTemplate(Math.max(simi - stepX / 2, 0), Math.max(simj - stepY / 2, 0),
                    Math.min(simi + stepX / 2, img.getWidth()), Math.min(simj + stepY / 2, img.getHeight()),
                    granularity * 2);
            }
            else {
                bestfit.setX(simi); bestfit.setY(simj);
                bestfit.setSim(maxsim);
            }
        }
    }

    private void sizeTemplate() {
        Gray8Image template = (Gray8Image)(bestfit.getTemplate().createCopy());
        double maxsim = 1.0 - templateXOR(img, bestfit.getX(), bestfit.getY(), template, false);
        for(double outerdiam = bestfit.getApproxCircleOuterX() - 1; outerdiam > 0; outerdiam --) {
            fillTemplate(template, outerdiam, bestfit.getApproxCircleInnerX(), bestfit.getAspectScale());
            double currsim = 1.0 - templateXOR(img, bestfit.getX(), bestfit.getY(), template, false);
            if(currsim < maxsim) {
                break;
            }
            else {
                System.out.println("--outerdiam = " + outerdiam + ":" + currsim);
                bestfit.setTemplate(template);
                bestfit.setApproxCircleOuterX(outerdiam);
                bestfit.setSim(currsim);
                template = (Gray8Image)(bestfit.getTemplate().createCopy());
                maxsim = currsim;
            }
        }
        
        for(double innerdiam = bestfit.approxCircleInnerX - 1; innerdiam > 0; innerdiam --) {
            fillTemplate(template, bestfit.getApproxCircleOuterX(), innerdiam, bestfit.getAspectScale());
            double currsim = 1.0 - templateXOR(img, bestfit.getX(), bestfit.getY(), template, false);
            if(currsim < maxsim) {
                break;
            }
            else {
                System.out.println("--innerdiam = " + innerdiam + ":" + currsim);
                bestfit.setTemplate(template);
                bestfit.setApproxCircleInnerX(innerdiam);
                bestfit.setSim(currsim);
                template = (Gray8Image)(bestfit.getTemplate().createCopy());
                maxsim = currsim;
            }
        }        
    }
    
    private void aspectTemplate() {
        Gray8Image template = (Gray8Image)(bestfit.getTemplate().createCopy());
        double maxsim = 1.0 - templateXOR(img, bestfit.getX(), bestfit.getY(), template, false);
        System.out.println("maxsim = " + maxsim + ":" + bestfit.getSim());
        double oldaspectscale = bestfit.getAspectScale();
        for(double aspectscale = oldaspectscale - 0.05; 
                   aspectscale <= oldaspectscale + 0.05; aspectscale += 0.0025) {
            fillTemplate(template, bestfit.getApproxCircleOuterX(), bestfit.getApproxCircleInnerX(), aspectscale);
            double currsim = 1.0 - templateXOR(img, bestfit.getX(), bestfit.getY(), template, false);
            if(currsim > maxsim) {
                System.out.println("--aspectscale = " + aspectscale + ":" + currsim);
                bestfit.setTemplate(template);
                bestfit.setAspectScale(aspectscale);
                bestfit.setSim(currsim);
                template = (Gray8Image)(bestfit.getTemplate().createCopy());
                maxsim = currsim;
            }
        }
    }

    private void shiftTemplate() {
        double maxsim = 1.0 - templateXOR(img, bestfit.getX(), bestfit.getY(), bestfit.getTemplate(), false);
        System.out.println("maxsim = " + maxsim + ":" + bestfit.getSim());
        int oldX = bestfit.getX();
        int oldY = bestfit.getY();
        for(int newX = oldX - 2; newX <= oldX + 2; newX ++) {
            for(int newY = oldY - 2; newY <= oldY + 2; newY ++) {
                double currsim = 1.0 - templateXOR(img, newX, newY, bestfit.getTemplate(), false);
                if(currsim > maxsim) {
                    System.out.println("--newX = " + newX + ": newY = " + newY + ":" + currsim);
                    bestfit.setX(newX); bestfit.setY(newY);
                    bestfit.setSim(currsim);
                    maxsim = currsim;
                }    
            }            
        }
    }
    
    public static double templateXOR(Gray8Image img, int x, int y, Gray8Image template, boolean dump) {
        int diff = 0, total = 0;
        for(int j = y; j < y + template.getHeight() && j < img.getHeight(); j++) {
            for(int i = x; i < x + template.getWidth() && i < img.getWidth(); i++) {
                boolean isblack = (img.getSample(i, j) < 200 ? true : false);       // XXX
                if(dump) { System.out.print((isblack & template.isWhite(i - x, j - y) ? "1" : ((!isblack) & template.isBlack(i - x, j - y)) ? "-" : "0")); }
                if((isblack & template.isWhite(i - x, j - y) | (!isblack) & template.isBlack(i - x, j - y))) {
                    diff ++;
                }
                total ++;
            }
            if(dump) { System.out.println(); }
        }
        return ((double)diff) / total;
    }

    
}
