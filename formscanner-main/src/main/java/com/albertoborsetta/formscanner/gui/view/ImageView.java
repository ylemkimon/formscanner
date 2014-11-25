package com.albertoborsetta.formscanner.gui.view;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import com.albertoborsetta.formscanner.commons.FormScannerConstants.Mode;

public interface ImageView extends View {
	
	public void updateImage(BufferedImage image);	
	public void setImageCursor(Cursor cursor);
	public Mode getMode();
	public void setMode(Mode mode);
	public Dimension getImageSize();
	public void repaint();
}
