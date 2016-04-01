package com.albertoborsetta.formscanner.gui.builder;

import java.awt.ComponentOrientation;

import javax.swing.ListModel;
import javax.swing.event.ListSelectionListener;

import org.jdesktop.swingx.JXList;

import com.albertoborsetta.formscanner.commons.FormScannerFont;

public class ListBuilder {

    private final JXList list;

    public ListBuilder(String[] objectList, ComponentOrientation orientation) {
        list = new JXList(objectList);
        list.setFont(FormScannerFont.getFont());
        list.setComponentOrientation(orientation);
    }

    public ListBuilder() {
        list = new JXList();
        list.setFont(FormScannerFont.getFont());
    }

    public ListBuilder withSelectionMode(int selectionMode) {
        list.setSelectionMode(selectionMode);
        return this;
    }

    public JXList build() {
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
