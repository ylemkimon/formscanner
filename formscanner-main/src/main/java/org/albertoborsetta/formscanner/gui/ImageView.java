package org.albertoborsetta.formscanner.gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.io.File;
//import java.util.HashMap;
//import java.util.List;

import org.albertoborsetta.formscanner.commons.FormPoint;
import org.albertoborsetta.formscanner.commons.FormTemplate;
//import org.albertoborsetta.formscanner.commons.FormScannerConstants.Corners;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Mode;

public interface ImageView extends View {
	
	public void updateImage(File file);	
	public void setImageCursor(Cursor cursor);
	public void setTemplate(FormTemplate template);
	public Mode getMode();
	public Dimension getImageSize();
	public void addTemporaryPoint(FormPoint p2);
	public void removeTemporaryPoint();
	public FormPoint getTemporaryPoint();
}
