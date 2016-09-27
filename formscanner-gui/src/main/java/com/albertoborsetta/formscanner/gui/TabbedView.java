package com.albertoborsetta.formscanner.gui;

public interface TabbedView extends View {

	public void setupNextTab(String action);

	public void setAdvanceable();
	
	public boolean isAdvanceable(int tab);
	
	public boolean isAdvanceable();

	public void dispose();
}
