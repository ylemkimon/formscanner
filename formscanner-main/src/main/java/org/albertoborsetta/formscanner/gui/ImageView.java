package org.albertoborsetta.formscanner.gui;

import java.awt.Cursor;
import java.io.File;

public interface ImageView {
	
	public void updateImage(File file);	
	public void setImageCursor(Cursor cursor);
	public void drawRect(int x, int y, int width, int height);
}
