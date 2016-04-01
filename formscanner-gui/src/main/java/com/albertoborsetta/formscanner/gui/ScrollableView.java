package com.albertoborsetta.formscanner.gui;

public interface ScrollableView {

	public void setScrollBars(int deltaX, int deltaY);

	public int getHorizontalScrollbarValue(Double zoom);

	public int getVerticalScrollbarValue(Double zoom);
}
