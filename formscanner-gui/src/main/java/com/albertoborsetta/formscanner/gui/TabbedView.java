package com.albertoborsetta.formscanner.gui;

public interface TabbedView extends View {

	public void setupNextTab(String action);

	public void setAdvanceable();

	public String getSelectedField();

	public String getSelectedGroup();

	public void enableRemoveFields();

	public void removeSelectedField();

	public boolean getIsMultiple();

	public void enableRejectMultiple(boolean enable);

	public void dispose();
}
