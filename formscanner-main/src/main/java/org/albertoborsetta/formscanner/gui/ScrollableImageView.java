package org.albertoborsetta.formscanner.gui;

public interface ScrollableImageView extends ImageView {
	
	public void setScrollBars(int deltaX, int deltaY);
	public void zoomImage(double zoom);
	public double getScaleFactor();
}
