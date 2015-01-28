package com.albertoborsetta.formscanner.gui.view;


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

import com.albertoborsetta.formscanner.api.FormField;
import com.albertoborsetta.formscanner.api.FormTemplate;
import com.albertoborsetta.formscanner.commons.FormFileUtils;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.Frame;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import com.albertoborsetta.formscanner.gui.builder.ScrollPaneBuilder;
import com.albertoborsetta.formscanner.gui.model.FormScannerModel;

public class ResultsGridFrame extends InternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTable table;

	private JScrollPane responsesGridPanel;
	private FormTemplate form;
	private int rows;
	private int cols;
	private FormFileUtils fileUtils;
	private String[] header;

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
	public ResultsGridFrame(FormScannerModel model) {
		super(model);
		fileUtils = FormFileUtils.getInstance(model.getLocale());
		
		form = model.getFilledForm();

		FormTemplate template = model.getTemplate();
		header = (String[]) fileUtils.getHeader(template);
		rows = template.getFields().size() + 1;
		cols = 2;
		
		setBounds(model.getLastPosition(Frame.RESULTS_GRID_FRAME));
		setName(Frame.RESULTS_GRID_FRAME.name());
		setTitle(FormScannerTranslation
				.getTranslationFor(FormScannerTranslationKeys.RESULTS_GRID_FRAME_TITLE));
		setClosable(true);
		setIconifiable(true);
		setResizable(true);
		setMaximizable(true);

		table = createTable();
		clearTable();
		setupTable();
		responsesGridPanel = new ScrollPaneBuilder(table, orientation).build();

		getContentPane().add(responsesGridPanel, BorderLayout.CENTER);
	}

	private void clearTable() {
		table.selectAll();
		table.clearSelection();
	}

	private void setupTable() {
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
		table.setComponentOrientation(orientation);
		table.setCellSelectionEnabled(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		return table;
	}

	public void updateResults() {
		form = model.getFilledForm();
		clearTable();
		setupTable();
	}
}
