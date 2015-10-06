/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albertoborsetta.formscanner.api;

import com.albertoborsetta.formscanner.api.commons.Constants;
import java.awt.image.BufferedImage;

/**
 *
 * @author Alberto Borsetta
 * @version 1.1.1-SNAPSHOT
 */
public abstract class FormScannerDetector {

    public static final int WHITE_PIXEL = 1;
    public static final int BLACK_PIXEL = 0;
    public static final int HALF_WINDOW_SIZE = 5;
    public static final int WINDOW_SIZE = (HALF_WINDOW_SIZE * 2) + 1;

    public final int threshold;
    public final int density;
    public final BufferedImage image;

    public final int height;
    public final int width;
    public final int subImageWidth;
    public final int subImageHeight;

    public final FormTemplate template;
    public final FormTemplate parent;

    protected FormScannerDetector(BufferedImage image, FormTemplate template) {
        this(0, 0, image, template);
    }

    protected FormScannerDetector(int threshold, int density, BufferedImage image, FormTemplate template) {
        this.image = image;

        height = image.getHeight();
        width = image.getWidth();

        subImageWidth = width / 2;
        subImageHeight = height / 2;
        
        this.template = template;
        parent = template != null ? template.getParentTemplate() : null;
        
        this.threshold = threshold;
        this.density = density;
    }
    
    protected FormScannerDetector(int threshold, int density, BufferedImage image) {
        this(threshold, density, image, null);
    }

    protected FormPoint calcResponsePoint(FormPoint responsePoint) {
        FormPoint point = responsePoint.clone();

        FormPoint templateOrigin = parent.getCorner(Constants.Corners.TOP_LEFT);
        double templateRotation = parent.getRotation();
        double scale = Math.sqrt(template.getDiagonal() / parent.getDiagonal());

        point.rotoTranslate(templateOrigin, templateRotation, true);
        point.scale(scale);
        point.rotoTranslate(template.getCorners().get(Constants.Corners.TOP_LEFT), template.getRotation(), false);
        return point;
    }

    protected int isWhite(int xi, int yi, int[] rgbArray) {
        int blacks = 0;
        int total = WINDOW_SIZE * WINDOW_SIZE;
        for (int i = 0; i < WINDOW_SIZE; i++) {
            for (int j = 0; j < WINDOW_SIZE; j++) {
                int xji = xi - HALF_WINDOW_SIZE + j;
                int yji = yi - HALF_WINDOW_SIZE + i;
                int index = (yji * subImageWidth) + xji;
                if ((rgbArray[index] & (0xFF)) < threshold) {
                    blacks++;
                }
            }
        }
        if ((blacks / (double) total) >= (density / 100.0)) {
            return BLACK_PIXEL;
        }
        return WHITE_PIXEL;
    }
}
