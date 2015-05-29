package com.albertoborsetta.formscanner.gui;

public interface ScrollableImageView extends ImageView {

	public void setScrollBars(int deltaX, int deltaY);

	public int getHorizontalScrollbarValue();

	public int getVerticalScrollbarValue();
}
