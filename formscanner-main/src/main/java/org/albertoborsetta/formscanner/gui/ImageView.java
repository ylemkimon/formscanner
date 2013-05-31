package org.albertoborsetta.formscanner.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.albertoborsetta.formscanner.commons.FormPoint;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Corners;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Mode;

public interface ImageView extends View {
	
	public void updateImage(File file);	
	public void setImageCursor(Cursor cursor);
	public void addPoint(FormPoint point);
	public void addPoints(List<FormPoint> points);
	public List<FormPoint> getPoints();
	public void removePoint(FormPoint point);
	public void removeAllPoints();
	public void addCorner(Corners position, FormPoint corner);
	public void addCorners(HashMap<Corners, FormPoint> corners);
	public void setRotation(double rotation);
	public Mode getMode();
	public Dimension getImageSize();
}
