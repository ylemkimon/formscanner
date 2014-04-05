package org.albertoborsetta.formscanner.gui;

import javax.swing.JInternalFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.albertoborsetta.formscanner.commons.FormField;
import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.FormTemplate;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.gui.builder.ScrollPaneBuilder;
import org.albertoborsetta.formscanner.gui.controller.InternalFrameController;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class ResultsGridFrame extends JInternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTable table;

	private FormScannerModel formScannerModel;
	private InternalFrameController internalFrameController;

	private JScrollPane responsesGridPanel;
	private FormTemplate form;
	private int rows;
	private int cols;
	String[] header;

	private class TemplateTableModel extends DefaultTableModel {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		public TemplateTableModel(int rows, int cols) {
			super(rows, cols);
		}

		public boolean isCellEditable(int row, int col) {
			return false;
		}
	}

	/**
	 * Create the frame.
	 */
	public ResultsGridFrame(FormScannerModel model, FormTemplate form, FormTemplate template) {
		formScannerModel = model;
		this.form = form;

		header = (String[]) template.getHeader();
		rows = template.getFields().size() + 1;
		cols = 2;
		
		internalFrameController = InternalFrameController
				.getInstance(formScannerModel);
		addInternalFrameListener(internalFrameController);

		setName(FormScannerConstants.RESULTS_GRID_FRAME_NAME);
		setTitle(FormScannerTranslation
				.getTranslationFor(FormScannerTranslationKeys.RESULTS_GRID_FRAME_TITLE));
		setBounds(700, 100, 230, 300);
		setClosable(true);
		setIconifiable(true);
		setResizable(true);
		setMaximizable(true);

		table = createTable();
		setupTable();
		responsesGridPanel = new ScrollPaneBuilder(table).build();

		getContentPane().add(responsesGridPanel, BorderLayout.CENTER);
	}

	public void setupTable() {
		for (int i = 1; i < rows; i++) {
			FormField field = form.getFields().get(header[i]);
			if (field != null) {
				table.setValueAt(field.getValues(), i, 1);
			}
		}
	}

	private JTable createTable() {
		TemplateTableModel tableModel = new TemplateTableModel(rows, cols);
		TableColumnModel columnModel = new DefaultTableColumnModel();

		for (int i = 0; i < cols; i++) {
			TableColumn column = new TableColumn(i);
			column.setMinWidth(100);
			columnModel.addColumn(column);
		}

		JTable table = new JTable(tableModel, columnModel);
		table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Component getTableCellRendererComponent(JTable table,
					Object value, boolean isSelected, boolean hasFocus,
					int row, int column) {
				final Component cell = super.getTableCellRendererComponent(
						table, value, isSelected, hasFocus, row, column);
				if (row == 0 || column == 0) {
					cell.setBackground(new java.awt.Color(238, 238, 238));
				} else {
					cell.setBackground(Color.white);
				}
				return cell;
			}
		});

		for (int i = 1; i < cols; i++) {
			table.setValueAt(
					(String) FormScannerTranslation
							.getTranslationFor(FormScannerTranslationKeys.RESULTS), 0, i);
		}

		for (int i = 1; i < rows; i++) {
			table.setValueAt(
					header[i], i, 0);
		}
		table.setCellSelectionEnabled(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		
		return table;
	}
}