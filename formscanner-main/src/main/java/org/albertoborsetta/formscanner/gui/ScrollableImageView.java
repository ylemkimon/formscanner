package org.albertoborsetta.formscanner.gui;

import org.albertoborsetta.formscanner.commons.FormScannerConstants.Zoom;

public interface ScrollableImageView extends ImageView {
	
	public void setScrollBars(int deltaX, int deltaY);
	public void zoomImage(Zoom zoom);
	public Zoom getScaleFactor();
	public int getHorizontalScrollbarValue();
	public int getVerticalScrollbarValue();
}
