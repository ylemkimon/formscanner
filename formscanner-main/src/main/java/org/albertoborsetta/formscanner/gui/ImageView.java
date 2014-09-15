package org.albertoborsetta.formscanner.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.io.File;

import org.albertoborsetta.formscanner.commons.FormScannerConstants.Mode;

public interface ImageView extends View {
	
	public void updateImage(File file);	
	public void setImageCursor(Cursor cursor);
	public Mode getMode();
	public void setMode(Mode mode);
	public Dimension getImageSize();
	public void repaint();
}
