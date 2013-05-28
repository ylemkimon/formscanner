package org.albertoborsetta.formscanner.gui;

import java.awt.Cursor;
import java.io.File;
import java.util.List;

import org.albertoborsetta.formscanner.commons.FormPoint;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Mode;

public interface ImageView extends View {
	
	public void updateImage(File file);	
	public void setImageCursor(Cursor cursor);
	public void addPoint(FormPoint p);
	public void removePoint(FormPoint p);
	public void removeAllPoints();
	public Mode getMode();
	public List<FormPoint> getPoints();	
}
