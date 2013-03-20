/*
 * BestFitCoords.java
 *
 * Created on June 30, 2007, 12:16 AM
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
public class BestFitCoords {
    
    int x, y;
    double approxCircleOuterX, approxCircleInnerX, aspectScale;
    Gray8Image template;
    double maxsim = -1;

    public BestFitCoords(int x, int y, Gray8Image template, double approxCircleOuterX, double approxCircleInnerX, double aspectScale) {
        this.x = x;
        this.y = y;
        this.template = template;
        this.approxCircleOuterX = approxCircleOuterX;
        this.approxCircleInnerX = approxCircleInnerX;
        this.aspectScale = aspectScale;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Gray8Image getTemplate() {
        return template;
    }

    public double getApproxCircleOuterX() {
        return approxCircleOuterX;
    }

    public double getApproxCircleInnerX() {
        return approxCircleInnerX;
    }

    public double getAspectScale() {
        return aspectScale;
    }

    public double getSim() {
        return maxsim;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setApproxCircleOuterX(double approxCircleOuterX) {
        this.approxCircleOuterX = approxCircleOuterX;
    }

    public void setApproxCircleInnerX(double approxCircleInnerX) {
        this.approxCircleInnerX = approxCircleInnerX;
    }

    public void setAspectScale(double aspectscale) {
        this.aspectScale = aspectscale;
    }

    public void setTemplate(Gray8Image template) {
        this.template = template;
    }

    public void setSim(double maxsim) {
        this.maxsim = maxsim;
    }
    
}
