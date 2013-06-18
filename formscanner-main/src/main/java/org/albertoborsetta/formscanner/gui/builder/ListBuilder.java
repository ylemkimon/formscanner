package org.albertoborsetta.formscanner.gui.builder;

import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionListener;

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

	public ListBuilder withListModel(ListModel listModel) {
		list.setModel(listModel);
		return this;
	}

	public ListBuilder withListSelectionListener(ListSelectionListener listener) {
		list.addListSelectionListener(listener);
		return this;
	}
}
