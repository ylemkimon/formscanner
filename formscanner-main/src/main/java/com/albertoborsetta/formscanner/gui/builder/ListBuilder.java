package com.albertoborsetta.formscanner.gui.builder;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionListener;

import com.albertoborsetta.formscanner.commons.FormScannerFont;

public class ListBuilder {
	
	private JList<String> list;
	
	public ListBuilder(String[] objectList) {
		list = new JList<String>(objectList);
		list.setFont(FormScannerFont.getFont());
	}
	
	public ListBuilder() {
		list = new JList<String>();
		list.setFont(FormScannerFont.getFont());
	}
	
	public ListBuilder withSelectionMode(int selectionMode) {
		list.setSelectionMode(selectionMode);
		return this;
	}	
	
	public JList<String> build() {
		return list;
	}

	public ListBuilder withListModel(ListModel<String> listModel) {
		list.setModel(listModel);
		return this;
	}

	public ListBuilder withListSelectionListener(ListSelectionListener listener) {
		list.addListSelectionListener(listener);
		return this;
	}
}
