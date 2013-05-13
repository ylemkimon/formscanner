package org.albertoborsetta.formscanner.gui;

import java.awt.Cursor;
import java.awt.Point;
import java.io.File;
import java.util.List;

import org.albertoborsetta.formscanner.commons.FormScannerConstants.Mode;

public interface ImageView extends View {
	
	public void updateImage(File file);	
	public void setImageCursor(Cursor cursor);
	public void addPoint(Point p);
	public void removePoint(Point p);
	public void removeAllPoints();
	public Mode getMode();
	public List<Point> getPoints();	
}
