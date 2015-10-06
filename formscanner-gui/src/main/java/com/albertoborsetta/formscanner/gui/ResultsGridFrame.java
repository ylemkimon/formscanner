package com.albertoborsetta.formscanner.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.albertoborsetta.formscanner.api.FormArea;
import com.albertoborsetta.formscanner.api.FormGroup;
import com.albertoborsetta.formscanner.api.FormQuestion;
import com.albertoborsetta.formscanner.api.FormTemplate;
import com.albertoborsetta.formscanner.commons.FormFileUtils;
import com.albertoborsetta.formscanner.commons.FormScannerConstants;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.Frame;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import com.albertoborsetta.formscanner.gui.builder.ScrollPaneBuilder;
import com.albertoborsetta.formscanner.model.FormScannerModel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

public class ResultsGridFrame extends InternalFrame {

	/**
     *
     */
	private static final long serialVersionUID = 1L;

	private final JTable table;

	private final JScrollPane responsesGridPanel;
	private FormTemplate form;
	private final int rows;
	private final int cols;
	private final FormFileUtils fileUtils;
//	private final String[] header;

	private class TemplateTableModel extends DefaultTableModel {

		/**
         *
         */
		private static final long serialVersionUID = 1L;

		public TemplateTableModel(int rows, int cols) {
			super(rows, cols);
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			return false;
		}
	}

	/**
	 * Multiline Table Cell Renderer.
	 */
	public class MultilineTableCellRenderer extends JTextArea
			implements TableCellRenderer {

		/**
         *
         */
		private static final long serialVersionUID = 1L;

		private final ArrayList<ArrayList<Integer>> rowColHeight = new ArrayList<>();

		public MultilineTableCellRenderer() {
			setLineWrap(true);
			setWrapStyleWord(true);
			setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {

			int rowHead = (model.isGroupsEnabled()) ? 1 : 0;
			if (row == 0 || column <= rowHead) {
				setBackground(new java.awt.Color(238, 238, 238));
			} else {
				if (isSelected && row != 0 && column > rowHead) {
					setForeground(table.getSelectionForeground());
					setBackground(table.getSelectionBackground());
				} else {
					setForeground(table.getForeground());
					setBackground(table.getBackground());
				}
			}
			setFont(table.getFont());
			if (hasFocus && row != 0 && column > rowHead) {
				setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
				if (table.isCellEditable(row, column)) {
					setForeground(UIManager
							.getColor("Table.focusCellForeground"));
					setBackground(UIManager
							.getColor("Table.focusCellBackground"));
				}
			} else {
				setBorder(new EmptyBorder(1, 2, 1, 2));
			}
			if (value != null) {
				setText(value.toString());
			} else {
				setText("");
			}
			adjustRowHeight(table, row, column);
			return this;
		}

		/**
		 * Calculate the new preferred height for a given row, and sets the
		 * height on the table.
		 */
		private void adjustRowHeight(JTable table, int row, int column) {
			int cWidth = table
					.getTableHeader().getColumnModel().getColumn(column)
					.getWidth();
			setSize(new Dimension(cWidth, 1000));
			int prefH = getPreferredSize().height;
			while (rowColHeight.size() <= row) {
				rowColHeight.add(new ArrayList<Integer>(column));
			}
			ArrayList<Integer> colHeights = rowColHeight.get(row);
			while (colHeights.size() <= column) {
				colHeights.add(0);
			}
			colHeights.set(column, prefH);
			int maxH = prefH;
			for (Integer colHeight : colHeights) {
				if (colHeight > maxH) {
					maxH = colHeight;
				}
			}
			if (table.getRowHeight(row) != maxH) {
				table.setRowHeight(row, maxH);
			}
		}
	}

	/**
	 * Create the frame.
	 *
	 * @param model
	 */
	public ResultsGridFrame(FormScannerModel model) {
		super(model);
		fileUtils = FormFileUtils.getInstance(model.getLocale());

		form = model.getFilledForm();

		FormTemplate template = model.getTemplate();
		rows = fileUtils.getHeader(template).size() + 1;
		cols = (model.isGroupsEnabled()) ? 3 : 2;

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
		int i = 1;
		HashMap<String, FormGroup> groups = form.getGroups();
		for (Entry<String, FormGroup> group : groups.entrySet()) {

			HashMap<String, FormQuestion> fields = group.getValue().getFields();
			ArrayList<String> fieldKeys = new ArrayList<>(fields.keySet());
			Collections.sort(fieldKeys);
			for (String fieldKey : fieldKeys) {
				FormQuestion field = fields.get(fieldKey);
				if (field != null) {
					int c = 0;
					if (model.isGroupsEnabled()) {
						if (!group.getKey().equals(
								FormScannerConstants.EMPTY_GROUP_NAME)) {
							table.setValueAt(group.getKey(), i, c++);
						} else {
							c++;
						}
					}
					table.setValueAt(field.getName(), i, c++);
					table.setValueAt(field.getValues(), i, c);
				}
				i++;
			}

			HashMap<String, FormArea> areas = group.getValue().getAreas();
			ArrayList<String> areaKeys = new ArrayList<>(areas.keySet());
			Collections.sort(areaKeys);
			for (String areaKey : areaKeys) {
				FormArea area = areas.get(areaKey);
				if (area != null) {
					int c = 0;
					if (model.isGroupsEnabled()) {
						if (!group.getKey().equals(
								FormScannerConstants.EMPTY_GROUP_NAME)) {
							table.setValueAt(group.getKey(), i, c++);
						} else {
							c++;
						}
					}
					table.setValueAt(area.getName(), i, c++);
					table.setValueAt(area.getText(), i, c);
				}
				i++;
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

		JTable newTable = new JTable(tableModel, columnModel);
		newTable.setDefaultRenderer(
				Object.class, new MultilineTableCellRenderer());

		int c = 0;
		if (model.isGroupsEnabled()) {
			newTable.setValueAt(
					FormScannerTranslation
							.getTranslationFor(FormScannerTranslationKeys.GROUP),
					0, c++);
		}
		
		newTable.setValueAt(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.QUESTION),
				0, c++);
		
		newTable.setValueAt(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.RESULTS),
				0, c++);

		newTable.setComponentOrientation(orientation);
		newTable.setCellSelectionEnabled(true);
		newTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		return newTable;
	}

	public void updateResults() {
		form = model.getFilledForm();
		clearTable();
		setupTable();
	}
}
