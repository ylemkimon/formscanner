package org.albertoborsetta.formscanner.gui.builder;

import javax.swing.JList;

import org.albertoborsetta.formscanner.commons.FormScannerFont;

public class ListBuilder {
	
	private JList list;
	
	public ListBuilder(Object[] objectList) {
		list = new JList(objectList);
		list.setFont(FormScannerFont.getFont());
	}
	
	public ListBuilder() {
		list = new JList();
		list.setFont(FormScannerFont.getFont());
	}
	
	public ListBuilder withSelectionMode(int selectionMode) {
		list.setSelectionMode(selectionMode);
		return this;
	}	
	
	public JList build() {
		return list;
	}
}
