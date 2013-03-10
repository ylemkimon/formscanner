package org.albertoborsetta.formscanner.gui;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.HashMap;

import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.albertoborsetta.formscanner.commons.FormScannerFont;
import org.albertoborsetta.formscanner.controller.AnalyzeImageController;
import org.albertoborsetta.formscanner.controller.InternalFrameController;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class AnalyzeFileResultsFrame extends JInternalFrame {
	
	private static final long serialVersionUID = 1L;
	
	private JTabbedPane tabbedPane;
	
	private JScrollPane partialResultsScrollPane;
	private JScrollPane totalResultsScrollPane;
	
	private JTable partialResultsTable;
	private JTable totalResultsTable;
	
	private FormScannerModel formScannerModel;
	
	/**
	 * Create the frame.
	 */
	public AnalyzeFileResultsFrame(FormScannerModel formScannerModel, HashMap<String, String> partialResults, HashMap<String, Object> totalResults) {
		
		this.formScannerModel = formScannerModel;
		InternalFrameController internalFrameController = InternalFrameController.getInstance(formScannerModel);
		addInternalFrameListener(internalFrameController);	
		
		int desktopHeight = formScannerModel.getDesktopSize().height;
		int fieldsNumber = 20; // formScannerModel.getNumFields();
		
		setBounds(300, 10, 400, desktopHeight-20);  // change	
		
		setClosable(true);
		setIconifiable(true);
		setResizable(true);
		setMaximizable(true);
		
		setName("results grid"); // change
		setTitle("results grid"); // change
		
		String[] partialTableColumnNames = {"Question n.", "Result"};
		Object[][] partialTableData = new String[fieldsNumber][2];		
		partialResultsTable = createTable(partialTableData, partialTableColumnNames);
		
		partialResultsScrollPane = new JScrollPane(partialResultsTable);
		// partialResultsScrollPane.setViewportView(partialResultsTable);
				
		String[] totalTableColumnNames = new String[fieldsNumber+1];
		totalTableColumnNames[0] = "Matricola";
		for (int i=1; i<totalTableColumnNames.length; i++) {
			totalTableColumnNames[i] = "Question " + ((Integer) i).toString();
		}
		Object[][] totalData = new Object[1][fieldsNumber+1];
		totalResultsTable = createTable(totalData, totalTableColumnNames);
		
		totalResultsScrollPane = new JScrollPane(totalResultsTable);		
		// totalResultsScrollPane.setViewportView(totalResultsTable);
		
		tabbedPane = new JTabbedPane();
		tabbedPane.addTab("Partial results", partialResultsScrollPane);
		tabbedPane.addTab("Total results", totalResultsScrollPane);
		
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
	}
	
	private JTable createTable(Object[][] data, Object[] columnNames) {
		TableModel tableModel = new DefaultTableModel(data, columnNames);
		TableColumnModel columnModel = new DefaultTableColumnModel();
		
		for (int i=0; i<data[0].length; i++) {
			TableColumn column = new TableColumn(i);
			column.setHeaderValue(columnNames[i]);
			column.setMinWidth(100);
			columnModel.addColumn(column);
		}
		
		JTable table = new JTable(tableModel, columnModel);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		return table;
	}
}
