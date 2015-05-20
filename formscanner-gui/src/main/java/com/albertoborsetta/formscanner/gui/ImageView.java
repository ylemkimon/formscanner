package com.albertoborsetta.formscanner.gui;

import java.awt.Cursor;
import java.awt.image.BufferedImage;

import com.albertoborsetta.formscanner.commons.FormScannerConstants.Mode;

public interface ImageView extends View {

    public void updateImage(BufferedImage image);

    public void setImageCursor(Cursor cursor);

    public Mode getMode();

    public void setMode(Mode mode);

    public void repaint();
}
