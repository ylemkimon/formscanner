package org.albertoborsetta.formscanner.gui;

import java.awt.Cursor;
import java.io.File;

public interface ImageView {
	
	public void updateImage(File file);	
	public void setScrollBars(int deltaX, int deltaY);	
	public void setImageCursor(Cursor cursor);
}
