package com.albertoborsetta.formscanner.gui;

public interface WizardTabView {

	public void setupNextTab(String action);

	public void setAdvanceable();
	
	public boolean isAdvanceable(int tab);
	
	public boolean isAdvanceable();

	public void dispose();
}
