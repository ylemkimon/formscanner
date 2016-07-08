package com.albertoborsetta.formscanner.controller;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import com.albertoborsetta.formscanner.gui.DataPanel;
import com.albertoborsetta.formscanner.model.FormScannerModel;

public class FieldsGridController implements TableModelListener, ListSelectionListener  {

	private static FieldsGridController instance;
	private FormScannerModel formScannerModel;
	private DataPanel bottomPanel;

	private FieldsGridController(FormScannerModel formScannerModel) {
		this.formScannerModel = formScannerModel;
	}

	public static FieldsGridController getInstance(FormScannerModel formScannerModel) {
		if (instance == null) {
			instance = new FieldsGridController(formScannerModel);
		}
		return instance;
	}

	public void add(DataPanel bottomPanel) {
			this.bottomPanel = bottomPanel;
	}
	
	@Override
	public void tableChanged(TableModelEvent e) {
		if (e.getType() == TableModelEvent.UPDATE) {
			int row = e.getFirstRow();
			int col = e.getColumn();
			TableModel model = (TableModel) e.getSource();
			if ((row < 0) || (col < 0)) 
					return;
	        // String newFileName = (String) model.getValueAt(row, col);
	        // formScannerModel.renameSelectedFile(newFileName);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent e) {
		// TODO Auto-generated method stub
		
	}

}
