package org.albertoborsetta.formscanner.gui;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JList;

import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.FormScannerFont;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class AnalyzeFileResultsFrame extends JInternalFrame {
	
	private static final long serialVersionUID = 1L;
	
	private JTabbedPane tabbedPane;
	
	private JScrollPane partialResultsScrollPane;
	private JScrollPane totalResultsScrollPane;
	
	private ResultsTable partialResultsTable;
	private ResultsTable totalResultsTable;
	
	private FormScannerModel formScannerModel;
	
	/**
	 * Create the frame.
	 */
	public AnalyzeFileResultsFrame(FormScannerModel formScannerModel) {
		
		this.formScannerModel = formScannerModel;
		int desktopHeight = formScannerModel.getDesktopSize().height;
		
		setBounds(300, 10, 400, desktopHeight-20);  // change
		
		setName("results grid"); // change
		setTitle("results grid"); // change		
		
		setResizable(true);
		setMaximizable(true);
		
		tabbedPane = new JTabbedPane();
		
		partialResultsScrollPane = new JScrollPane();
		totalResultsScrollPane = new JScrollPane();
		
		String[] columnNames = {"Question n.", "Result"};
		
		Object[][] data = new Object[20][2];
		for (int i=0; i<data.length; i++) {
			data[i][0] = (Object) ("Question " + ((Integer) i).toString());
			data[i][1] = "";
		}
		
		partialResultsTable = new ResultsTable(data, columnNames);
		
		String[] columnNames1 = new String[20];
		columnNames1[0] = "Matricola";
		for (int i=1; i<columnNames1.length; i++) {
			columnNames1[i] = "Question " + ((Integer) i).toString();
		}
		
		Object[][] data1 = new Object[1][21];
		for (int i=0; i<data1.length; i++) {
			data[0][i] = "";
		}
		
		totalResultsTable = new ResultsTable(data, columnNames);
		
		partialResultsScrollPane.setViewportView(partialResultsTable);
		totalResultsScrollPane.setViewportView(totalResultsTable);
		
		tabbedPane.addTab("Partial results", partialResultsScrollPane);
		tabbedPane.addTab("Total results", totalResultsScrollPane);
	}
	/*
	public void updatePartialResultsTable(String[][] results) {
		
		for (int i
		partialResultsTable.setValueAt(aValue, row, column)  .setModel(results);
	}
	
	public void selectFile(int index) {
		list.setSelectedIndex(index);
	}
	
	public String getSelectedItem() {
		return list.getSelectedValue().toString();
	}
	
	public String getItemByIndex(int index) {
		list.setSelectedIndex(index);
		return list.getSelectedValue().toString();
	}
	
	public int getSelectedItemIndex() {
		int index = 0;
		if (!list.isSelectionEmpty()) {
			index = list.getSelectedIndex(); 
		}
		return index;
	}
	*/
	private class ResultsTable extends JTable {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ResultsTable(Object[][] data, Object[] columnName) {
			super(data, columnName); 
			setFont(FormScannerFont.getFont());
			setSelectionMode(ListSelectionModel.SINGLE_SELECTION);			
		}
	}
}
