package org.albertoborsetta.formscanner.gui;

public interface TabbedView extends View {

	public void setupNextTab(String action);
	public void setAdvanceable();
	public String getSelectedItem();
	public void enableRemoveFields();
	public void removeFieldByName(String fieldName);
	public void dispose();
}
