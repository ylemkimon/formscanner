package com.albertoborsetta.formscanner.gui;

import javax.swing.JTabbedPane;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.albertoborsetta.formscanner.api.FormArea;
import com.albertoborsetta.formscanner.api.FormGroup;
import com.albertoborsetta.formscanner.api.FormQuestion;
import com.albertoborsetta.formscanner.api.FormPoint;
import com.albertoborsetta.formscanner.api.commons.Constants.Corners;
import com.albertoborsetta.formscanner.api.commons.Constants.FieldType;
import com.albertoborsetta.formscanner.commons.CharSequenceGenerator;
import com.albertoborsetta.formscanner.commons.FormScannerConstants;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.Action;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.FieldsTableColumn;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.Frame;
import com.albertoborsetta.formscanner.commons.resources.FormScannerResources;
import com.albertoborsetta.formscanner.commons.resources.FormScannerResourcesKeys;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import com.albertoborsetta.formscanner.gui.builder.ButtonBuilder;
import com.albertoborsetta.formscanner.gui.builder.CheckBoxBuilder;
import com.albertoborsetta.formscanner.gui.builder.ComboBoxBuilder;
import com.albertoborsetta.formscanner.gui.builder.LabelBuilder;
import com.albertoborsetta.formscanner.gui.builder.PanelBuilder;
import com.albertoborsetta.formscanner.gui.builder.ScrollPaneBuilder;
import com.albertoborsetta.formscanner.gui.builder.SpinnerBuilder;
import com.albertoborsetta.formscanner.gui.builder.TabbedPaneBuilder;
import com.albertoborsetta.formscanner.gui.builder.TextFieldBuilder;
import com.albertoborsetta.formscanner.controller.ManageTemplateController;
import com.albertoborsetta.formscanner.model.FormScannerModel;

public class ManageTemplateFrame extends InternalFrame implements TabbedView {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private final JTabbedPane tabbedPane;
	private JScrollPane positionsTableScrollPane;
	private JScrollPane fieldsTableScrollPane;

	private JButton addFieldButton;
	private JButton removeFieldButton;
	private JButton saveTemplateButton;
	private JButton cancelTemplateButton;

	private JSpinner colsNumber;
	private JSpinner rowsNumber;
	private JComboBox<InternalFieldType> typeComboBox;
	private JCheckBox isMultiple;
	private JCheckBox rejectMultiple;
	private JButton okPropertiesButton;
	private JButton cancelPropertiesButton;
	private JButton okTypesButton;
	private JButton cancelTypesButton;

	private JTable positionsTable;
	private JTable fieldsTable;
	private JButton okPositionButton;
	private JButton cancelPositionButton;

	private final ManageTemplateController manageTemplateController;
	private InternalFieldType[] types;
	private JPanel fieldsTypeButtonPanel;
	private JTextField questionLabel;
	private String fieldsType;

	private int rowsCount;
	private int barcodeCount;
	
	private int previousRowsCount;
	private int previousBarcodeCount;
	
	private JComboBox<String> setOfQuestionsCombo;

	private static final Logger logger = LogManager.getLogger(ManageTemplateFrame.class.getName());

	private class InternalFieldType {

		private final FieldType type;

		protected InternalFieldType(FieldType type) {
			this.type = type;
		}

		protected FieldType getType() {
			return type;
		}

		@Override
		public String toString() {
			return FormScannerTranslation.getTranslationFor(type.getValue());
		}
	}

	protected class FieldsTableModel extends DefaultTableModel {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		public FieldsTableModel() {
			super();
		}

		@Override
		public boolean isCellEditable(int row, int col) {
			return false;
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			if ((model.isGroupsEnabled() && columnIndex == 3) || (!model.isGroupsEnabled() && columnIndex == 2)) {
				return Boolean.class;
			}
			return super.getColumnClass(columnIndex);
		}
	}

	public class PositionsTableCellRenderer extends JTextArea implements TableCellRenderer {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		private final String type;
		private final ArrayList<ArrayList<Integer>> rowColHeight = new ArrayList<>();

		public PositionsTableCellRenderer(String type) {
			setLineWrap(true);
			setWrapStyleWord(true);
			setOpaque(true);
			this.type = type;
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {

			boolean header = false;
			switch (type) {
			case FormScannerConstants.QUESTIONS_BY_ROWS:
			case FormScannerConstants.QUESTIONS_BY_COLS:
				header = ((row == 0) || (column == 0));
				break;
			case FormScannerConstants.RESPONSES_BY_GRID:
			case FormScannerConstants.BARCODE:
				header = ((column % 2) == 0);
				break;
			}

			if (header) {
				setBackground(new java.awt.Color(238, 238, 238));
			} else {
				if (isSelected && !header) {
					setForeground(table.getSelectionForeground());
					setBackground(table.getSelectionBackground());
				} else {
					setForeground(table.getForeground());
					setBackground(table.getBackground());
				}
			}
			setFont(table.getFont());
			if (hasFocus && !header) {
				setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
				if (table.isCellEditable(row, column)) {
					setForeground(UIManager.getColor("Table.focusCellForeground"));
					setBackground(UIManager.getColor("Table.focusCellBackground"));
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
			int cWidth = table.getTableHeader().getColumnModel().getColumn(column).getWidth();
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

	public class FieldsTableCellRenderer extends JTextArea implements TableCellRenderer {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;
		private final ArrayList<ArrayList<Integer>> rowColHeight = new ArrayList<>();

		public FieldsTableCellRenderer() {
			setLineWrap(true);
			setWrapStyleWord(true);
			setOpaque(true);
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column) {

			if (isSelected) {
				setForeground(table.getSelectionForeground());
				setBackground(table.getSelectionBackground());
			} else {
				setForeground(table.getForeground());
				setBackground(table.getBackground());
			}
			setFont(table.getFont());
			if (hasFocus) {
				setBorder(UIManager.getBorder("Table.focusCellHighlightBorder"));
				if (table.isCellEditable(row, column)) {
					setForeground(UIManager.getColor("Table.focusCellForeground"));
					setBackground(UIManager.getColor("Table.focusCellBackground"));
				}
			} else {
				setBorder(new EmptyBorder(1, 2, 1, 2));
			}
			if (value != null) {
				setText(value.toString());
			} else {
				setText(StringUtils.EMPTY);
			}
			adjustRowHeight(table, row, column);
			return this;
		}

		/**
		 * Calculate the new preferred height for a given row, and sets the
		 * height on the table.
		 */
		private void adjustRowHeight(JTable table, int row, int column) {
			int cWidth = table.getTableHeader().getColumnModel().getColumn(column).getWidth();
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
	public ManageTemplateFrame(FormScannerModel model) {
		super(model);

		manageTemplateController = new ManageTemplateController(model);
		manageTemplateController.add(this);

		setName(Frame.MANAGE_TEMPLATE_FRAME.name());
		setTitle(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.MANAGE_TEMPLATE_FRAME_TITLE));
		setBounds(model.getLastPosition(Frame.MANAGE_TEMPLATE_FRAME));
		setMinimumSize(new Dimension(300, 500));
		setClosable(true);
		setMaximizable(true);
		setIconifiable(true);
		setResizable(true);
		setFrameIcon(FormScannerResources.getIconFor(FormScannerResourcesKeys.EDIT_ICON_16));

		rowsCount = 1;
		barcodeCount = 1;

		JPanel fieldListPanel = getFieldListPanel();
		JPanel fieldTypePanel = getFieldTypePanel();

		tabbedPane = new TabbedPaneBuilder(JTabbedPane.TOP, orientation)
				.addTab(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.FIELD_LIST_TAB_NAME),
						fieldListPanel)
				.addTab(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.FIELD_TYPE_TAB_NAME),
						fieldTypePanel)
				.addTab(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.FIELD_PROPERTIES_TAB_NAME),
						null)
				.addTab(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.FIELD_POSITION_TAB_NAME),
						null)
				.build();

		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setEnabledAt(1, false);
		tabbedPane.setEnabledAt(2, false);
		tabbedPane.setEnabledAt(3, false);
	}

	@Override
	public void setupNextTab(String action) {
		int currTab = tabbedPane.getSelectedIndex();
		int nextTab = 0;
		Action act = Action.valueOf(action);
		switch (act) {
		case CONFIRM:
			nextTab = (currTab + 1) % tabbedPane.getTabCount();

			if (currTab == 1) {
				fieldsType = getFieldType().getName();
				if (StringUtils.equals(fieldsType, FormScannerConstants.BARCODE) && !model.isGroupsEnabled())
					nextTab++;
			}

			switch (nextTab) {
			case 0:
				if (model.isGroupsEnabled()) {
						model.addUsedGroupName((String) setOfQuestionsCombo.getSelectedItem());
				}
				for (int i = nextTab + 1; i < tabbedPane.getTabCount(); i++) {
					tabbedPane.setEnabledAt(i, false);
				}
				model.disposeRelatedFrame(this);

				switch (fieldsType) {
				case FormScannerConstants.QUESTIONS_BY_COLS:
				case FormScannerConstants.QUESTIONS_BY_ROWS:
				case FormScannerConstants.RESPONSES_BY_GRID:
					HashMap<String, FormQuestion> fields = createFields();
					model.updateTemplateFields(model.isGroupsEnabled() ? (String) setOfQuestionsCombo.getSelectedItem()
							: FormScannerConstants.EMPTY_GROUP_NAME, fields);
					break;
				case FormScannerConstants.BARCODE:
					FormArea area = createArea();
					model.updateTemplateAreas(model.isGroupsEnabled() ? (String) setOfQuestionsCombo.getSelectedItem()
							: FormScannerConstants.EMPTY_GROUP_NAME, area);
					break;
				}
				setupFieldsTable();
				saveTemplateButton.setEnabled(true);
				resetSelectedValues();
				break;
			case 2:
				JPanel fieldPropertiesPanel = getFieldPropertiesPanel();
				tabbedPane.setComponentAt(nextTab, fieldPropertiesPanel);
				break;
			case 3:
				previousRowsCount = rowsCount;
				previousBarcodeCount = barcodeCount;
				
				if (model.isGroupsEnabled() && model.isResetAutoNumberingQuestions()) {
					previousRowsCount = model.lastIndexOfGroup((String) setOfQuestionsCombo.getSelectedItem());
				}
				JPanel fieldPositionPanel = getFieldPositionPanel();
				tabbedPane.setComponentAt(nextTab, fieldPositionPanel);
				model.createTemplateImageFrame(fieldsType);
				break;
			default:
				break;
			}
			break;
		case CANCEL:
			nextTab = currTab - 1;
			if (currTab > 0) {
				tabbedPane.setEnabledAt(currTab, false);
				tabbedPane.setSelectedIndex(nextTab);
			}
			switch (nextTab) {
			case 0:
				resetFieldsType();
				break;
			case 1:
				resetSelectedValues();
				break;
			case 2:
				rowsCount = previousRowsCount;
				barcodeCount = previousBarcodeCount;
								
				model.resetPoints();
				model.disposeRelatedFrame(this);

				if (StringUtils.equals(fieldsType, FormScannerConstants.BARCODE) && !model.isGroupsEnabled())
					nextTab--;
				break;
			default:
				dispose();
				break;
			}
			break;
		default:
			break;
		}

		if (nextTab >= 0) {
			tabbedPane.setEnabledAt(nextTab, true);
			tabbedPane.setSelectedIndex(nextTab);
		}
	}

	private FormArea createArea() {
		String name = questionLabel.getText();
		FormArea area = new FormArea(name);
		area.setType(getFieldType());
		for (int i = 0; i < 2; i++) {
			for (int j = 1; j < 5; j += 2) {
				FormPoint corner = getPointFromTable(i, j);
				switch (i + j) {
				case 1:
					area.setCorner(Corners.TOP_LEFT, corner);
					break;
				case 2:
					area.setCorner(Corners.BOTTOM_LEFT, corner);
					break;
				case 3:
					area.setCorner(Corners.TOP_RIGHT, corner);
					break;
				case 4:
					area.setCorner(Corners.BOTTOM_RIGHT, corner);
					break;
				}
			}
		}

		return area;
	}

	private void resetFieldsType() {
		typeComboBox.setSelectedIndex(0);
	}

	private HashMap<String, FormQuestion> createFields() {
		HashMap<String, FormQuestion> fields = new HashMap<>();

		Integer rows = (Integer) rowsNumber.getValue();
		Integer cols = (Integer) colsNumber.getValue();

		switch (fieldsType) {
		case FormScannerConstants.QUESTIONS_BY_ROWS:
		case FormScannerConstants.QUESTIONS_BY_COLS:
			for (int i = 1; i < rows + 1; i++) {
				String name = (String) positionsTable.getValueAt(i, 0);
				FormQuestion field = new FormQuestion(name);
				for (int j = 1; j < cols + 1; j++) {
					String value = (String) positionsTable.getValueAt(0, j);
					FormPoint p = getPointFromTable(i, j);
					field.setPoint(value, p);
					field.setType(getFieldType());
				}
				field.setMultiple(isMultiple.isSelected());
				field.setRejectMultiple(!isMultiple.isSelected() && rejectMultiple.isSelected());
				fields.put(name, field);
			}
			break;
		case FormScannerConstants.RESPONSES_BY_GRID:
			String name = questionLabel.getText();
			FormQuestion field = new FormQuestion(name);
			for (int i = 0; i < rows; i++) {
				for (int j = 1; j < (2 * cols) + 1; j += 2) {
					String value = (String) positionsTable.getValueAt(i, j - 1);
					FormPoint p = getPointFromTable(i, j);
					field.setPoint(value, p);
				}
			}
			field.setType(getFieldType());
			field.setMultiple(isMultiple.isSelected());
			field.setRejectMultiple(!isMultiple.isSelected() && rejectMultiple.isSelected());
			fields.put(name, field);
			break;
		}
		return fields;
	}

	private FormPoint getPointFromTable(int i, int j) {
		try {
			return FormPoint.toPoint((String) positionsTable.getValueAt(i, j));
		} catch (ParseException e) {
			logger.debug("Error", e);
			return new FormPoint();
		}
	}

	public int getRowsNumber() {
		switch (fieldsType) {
		case FormScannerConstants.QUESTIONS_BY_ROWS:
		case FormScannerConstants.QUESTIONS_BY_COLS:
		case FormScannerConstants.RESPONSES_BY_GRID:
			return (Integer) rowsNumber.getValue();
		case FormScannerConstants.BARCODE:
			return 2;
		default:
			return 1;
		}
	}

	public int getValuesNumber() {
		switch (fieldsType) {
		case FormScannerConstants.QUESTIONS_BY_ROWS:
		case FormScannerConstants.QUESTIONS_BY_COLS:
		case FormScannerConstants.RESPONSES_BY_GRID:
			return (Integer) colsNumber.getValue();
		case FormScannerConstants.BARCODE:
			return 2;
		default:
			return 1;
		}
	}

	public void setupPositionTable(List<FormPoint> points) {
		Integer rows = 2;
		Integer cols = 2;

		switch (fieldsType) {
		case FormScannerConstants.QUESTIONS_BY_ROWS:
		case FormScannerConstants.QUESTIONS_BY_COLS:
			rows = (Integer) rowsNumber.getValue();
			cols = (Integer) colsNumber.getValue();
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					int index = (cols * i) + j;
					FormPoint p = points.get(index);
					positionsTable.setValueAt(p.toString(), (i + 1), (j + 1));
				}
			}
			break;
		case FormScannerConstants.RESPONSES_BY_GRID:
			rows = (Integer) rowsNumber.getValue();
			cols = (Integer) colsNumber.getValue();
		case FormScannerConstants.BARCODE:
			for (int i = 0; i < rows; i++) {
				for (int j = 0; j < cols; j++) {
					int index = (i * cols) + j;
					FormPoint p = points.get(index);
					positionsTable.setValueAt(p.toString(), i, (2 * j) + 1);
				}
			}
			break;
		}
	}

	private void setupFieldsTable() {
		FieldsTableModel fieldsTableModel = (FieldsTableModel) fieldsTable.getModel();
		while (fieldsTable.getRowCount() > 0) {
			fieldsTableModel.removeRow(fieldsTable.getRowCount() - 1);
		}

		HashMap<String, FormGroup> groups = model.getTemplate().getGroups();

		for (Entry<String, FormGroup> group : groups.entrySet()) {

			HashMap<String, FormQuestion> fields = group.getValue().getFields();
			for (FormQuestion field : fields.values()) {
				if (model.isGroupsEnabled()) {
					fieldsTableModel.addRow(new Object[] { group.getKey(), field.getName(),
							FormScannerTranslation.getTranslationFor(field.getType().getValue()), field.isMultiple(),
							field.getPoints().size() });
				} else {
					fieldsTableModel.addRow(new Object[] { field.getName(),
							FormScannerTranslation.getTranslationFor(field.getType().getValue()), field.isMultiple(),
							field.getPoints().size() });
				}
			}

			HashMap<String, FormArea> areas = group.getValue().getAreas();
			for (FormArea area : areas.values()) {
				if (model.isGroupsEnabled()) {
					fieldsTableModel.addRow(new Object[] { group.getKey(), area.getName(),
							FormScannerTranslation.getTranslationFor(area.getType().getValue()), null, null });
				} else {
					fieldsTableModel.addRow(new Object[] { area.getName(),
							FormScannerTranslation.getTranslationFor(area.getType().getValue()), null, null });
				}
			}
		}
	}

	private void resetSelectedValues() {
		if (!fieldsType.equals(FormScannerConstants.BARCODE)) {
			rowsNumber.setValue(0);
			colsNumber.setValue(0);
			isMultiple.setSelected(false);
			rejectMultiple.setSelected(false);
		}
	}

	public FieldType getFieldType() {
		return (types[typeComboBox.getSelectedIndex()]).getType();
	}

	@Override
	public void setAdvanceable() {
		int currTab = tabbedPane.getSelectedIndex();

		switch (currTab) {
		case 0:
			break;
		case 1:
			break;
		case 2:
			okPropertiesButton.setEnabled(isAdvanceable(currTab));
			break;
		case 3:
			okPositionButton.setEnabled(isAdvanceable(currTab));
			break;
		default:
			break;
		}
	}

	private boolean verifyQuestionLabel() {
		switch (fieldsType) {
		case FormScannerConstants.RESPONSES_BY_GRID:
		case FormScannerConstants.BARCODE:
			return StringUtils.isNotBlank(questionLabel.getText());
		default:
			return true;
		}
	}

	private boolean verifyPropertiesValues() {
		switch (fieldsType) {
		case FormScannerConstants.QUESTIONS_BY_COLS:
		case FormScannerConstants.QUESTIONS_BY_ROWS:
			if ((Integer) colsNumber.getValue() < 0) {
				colsNumber.setValue(0);
			}
			if ((Integer) rowsNumber.getValue() < 0) {
				rowsNumber.setValue(0);
			}
			return (((Integer) colsNumber.getValue() > 0) && ((Integer) rowsNumber.getValue() > 0));
		case FormScannerConstants.RESPONSES_BY_GRID:
		case FormScannerConstants.BARCODE:
		default:
			return true;
		}
	}

	private JTable createSimplePositionsTable(int rows, int cols) {
		TableColumnModel columnModel = new DefaultTableColumnModel();

		for (int i = 0; i < cols; i++) {
			TableColumn column = new TableColumn(i);
			column.setMinWidth(100);
			columnModel.addColumn(column);
		}

		JTable table = new JTable(new DefaultTableModel(rows, cols) {

			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void addTableModelListener(TableModelListener tableModelListener) {
				super.addTableModelListener(manageTemplateController);
			}

			@Override
			public boolean isCellEditable(int row, int col) {
				if (row == 0 && col == 0) {
					return false;
				}
				return ((row * col) == 0);
			}
		}, columnModel);

		table.setDefaultRenderer(Object.class, new PositionsTableCellRenderer(fieldsType));

		CharSequenceGenerator charSequence = new CharSequenceGenerator();

		for (int i = 1; i < cols; i++) {
			table.setValueAt(charSequence.next(), 0, i);
		}

		for (int i = 1; i < rows; i++) {
			table.setValueAt(getNameFromTemplate(FormScannerConstants.QUESTION), i, 0);
		}
		table.setComponentOrientation(orientation);
		table.setCellSelectionEnabled(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setVisible(true);
		return table;
	}

	private String getNameFromTemplate(String type) {
		String nameTemplate = model.getNameTemplate(type);
		int length = StringUtils.countMatches(nameTemplate, "#");

		int count;
		switch (type) {
		case FormScannerConstants.BARCODE:
			count = barcodeCount++;
			break;
		case FormScannerConstants.GROUP:
			count = model.getLastGroupIndex();
			break;
		case FormScannerConstants.QUESTION:
		default:
			count = rowsCount++;
			break;
		}

		return StringUtils.replace(nameTemplate, StringUtils.repeat("#", length),
				StringUtils.leftPad("" + count, length, "0"));
	}

	private JTable createGridPositionsTable(int rows, int cols) {
		TableColumnModel columnModel = new DefaultTableColumnModel();

		for (int i = 0; i < cols; i++) {
			TableColumn column = new TableColumn(i);
			column.setMinWidth(100);
			columnModel.addColumn(column);
		}

		JTable table = new JTable(new DefaultTableModel(rows, cols) {

			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void addTableModelListener(TableModelListener tableModelListener) {
				super.addTableModelListener(manageTemplateController);
			}

			@Override
			public boolean isCellEditable(int row, int col) {
				return (col % 2 == 0);
			}
		}, columnModel);

		table.setDefaultRenderer(Object.class, new PositionsTableCellRenderer(fieldsType));

		CharSequenceGenerator charSequence = new CharSequenceGenerator();

		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < cols; j += 2) {
				table.setValueAt(charSequence.next(), i, j);
			}
		}
		table.setComponentOrientation(orientation);
		table.setCellSelectionEnabled(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		return table;
	}

	private JTable createBarcodePositionsTable(int rows, int cols) {
		TableColumnModel columnModel = new DefaultTableColumnModel();

		for (int i = 0; i < cols; i++) {
			TableColumn column = new TableColumn(i);
			column.setMinWidth(100);
			columnModel.addColumn(column);
		}

		JTable table = new JTable(new DefaultTableModel(rows, cols) {

			/**
			 *
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void addTableModelListener(TableModelListener tableModelListener) {
				super.addTableModelListener(manageTemplateController);
			}

			@Override
			public boolean isCellEditable(int row, int col) {
				return (false);
			}
		}, columnModel);

		table.setDefaultRenderer(Object.class, new PositionsTableCellRenderer(fieldsType));

		table.setValueAt(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.TOP_LEFT_CORNER), 0, 0);
		table.setValueAt(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.TOP_RIGHT_CORNER), 0, 2);
		table.setValueAt(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.BOTTOM_LEFT_CORNER), 1, 0);
		table.setValueAt(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.BOTTOM_RIGHT_CORNER), 1,
				2);

		table.setComponentOrientation(orientation);
		table.setCellSelectionEnabled(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		return table;
	}

	private JTable createFieldsTable() {
		FieldsTableModel fieldsTableModel = new FieldsTableModel();
		JTable table = new JTable(fieldsTableModel);
		table.setRowSorter(new TableRowSorter<>(fieldsTableModel));

		for (FieldsTableColumn tableColumn : FieldsTableColumn.values()) {
			if (tableColumn.equals(FieldsTableColumn.GROUP_COLUMN) && !model.isGroupsEnabled()) {
				continue;
			}
			fieldsTableModel.addColumn(FormScannerTranslation.getTranslationFor(tableColumn.getValue()));
		}

		table.setDefaultRenderer(Object.class, new FieldsTableCellRenderer());

		table.setComponentOrientation(orientation);
		table.setRowSelectionAllowed(true);
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(manageTemplateController);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		return table;
	}

	private JPanel getFieldListPanel() {

		fieldsTable = createFieldsTable();
		fieldsTableScrollPane = new ScrollPaneBuilder(fieldsTable, orientation).build();

		JPanel fieldListManageButtonPanel = getManageListButtonPanel();
		JPanel fieldListButtonPanel = getFieldListButtonPanel();

		return new PanelBuilder(orientation).withLayout(new BorderLayout())
				.addComponent(fieldsTableScrollPane, BorderLayout.CENTER)
				.addComponent(fieldListManageButtonPanel, BorderLayout.EAST)
				.addComponent(fieldListButtonPanel, BorderLayout.SOUTH).build();
	}

	private JPanel getFieldPropertiesPanel() {

		fieldsType = getFieldType().getName();

		JPanel propertiesButtonPanel = getPropertiesButtonPanel();

		PanelBuilder pPanelBuilder = new PanelBuilder(orientation).withLayout(new BorderLayout())
				.add(propertiesButtonPanel, BorderLayout.SOUTH);

		switch (fieldsType) {
		case FormScannerConstants.BARCODE:
			JPanel barcodePanel = getBarcodePanel();
			pPanelBuilder.add(barcodePanel, BorderLayout.NORTH);
			break;
		case FormScannerConstants.RESPONSES_BY_GRID:
		case FormScannerConstants.QUESTIONS_BY_ROWS:
		case FormScannerConstants.QUESTIONS_BY_COLS:
			JPanel rowsColsPanel = getRowsColsPanel();
			pPanelBuilder.add(rowsColsPanel, BorderLayout.NORTH);
			break;
		}

		return pPanelBuilder.build();
	}

	private JPanel getBarcodePanel() {
		String[] usedGroupNames = getUsedGroupNamesList();

		setOfQuestionsCombo = new ComboBoxBuilder<String>(FormScannerConstants.GROUP, orientation)
				.withModel(new DefaultComboBoxModel<>(usedGroupNames)).setEditable(true)
				.withActionListener(manageTemplateController).withActionCommand(FormScannerConstants.GROUP).build();
		
		setOfQuestionsCombo.setSelectedIndex(setOfQuestionsCombo.getItemCount()-1);
		
		return new PanelBuilder(orientation).withLayout(new SpringLayout())
				.add(getLabel(FormScannerTranslationKeys.SET_OF_QUESTIONS_LABEL)).add(setOfQuestionsCombo)
				.withGrid(1, 2).build();
	}

	private String[] getUsedGroupNamesList() {
		ArrayList<String> usedGroupNamesList = new ArrayList<String>();
		usedGroupNamesList.addAll(model.getUsedGroupNamesList());

		Collections.sort(usedGroupNamesList);
		String groupName = getNameFromTemplate(FormScannerConstants.GROUP);
		if (!usedGroupNamesList.contains(groupName)) 
			usedGroupNamesList.add(groupName);
		String[] usedGroupNames = new String[usedGroupNamesList.size()];
		usedGroupNamesList.toArray(usedGroupNames);
		return usedGroupNames;
	}

	protected JPanel getFieldPositionPanel() {

		JPanel positionButtonPanel = getPositionButtonPanel();

		positionsTable = getPositionsTable();

		positionsTableScrollPane = new ScrollPaneBuilder(positionsTable, orientation).build();

		PanelBuilder positionPanelBuilder = new PanelBuilder(orientation).withLayout(new BorderLayout())
				.addComponent(positionButtonPanel, BorderLayout.SOUTH)
				.addComponent(positionsTableScrollPane, BorderLayout.CENTER);

		switch (fieldsType) {
		case FormScannerConstants.RESPONSES_BY_GRID:
		case FormScannerConstants.BARCODE:

			questionLabel = new TextFieldBuilder(10, orientation).withActionListener(manageTemplateController)
					.setText(getNameFromTemplate(StringUtils.equals(fieldsType, FormScannerConstants.RESPONSES_BY_GRID)
							? FormScannerConstants.QUESTION : FormScannerConstants.BARCODE))
					.build();
			JPanel questionPanel = new PanelBuilder(orientation).withLayout(new SpringLayout())
					.add(getLabel(StringUtils.equals(fieldsType, FormScannerConstants.RESPONSES_BY_GRID)
							? FormScannerTranslationKeys.SINGLE_QUESTION_LABEL
							: FormScannerTranslationKeys.BARCODE_LABEL))
					.add(questionLabel).withGrid(1, 2).build();
			positionPanelBuilder.add(questionPanel, BorderLayout.NORTH);
			break;
		}

		return positionPanelBuilder.build();
	}

	private JTable getPositionsTable() {
		JTable table = null;

		switch (fieldsType) {
		case FormScannerConstants.QUESTIONS_BY_ROWS:
		case FormScannerConstants.QUESTIONS_BY_COLS:
			table = createSimplePositionsTable(((Integer) rowsNumber.getValue()) + 1,
					((Integer) colsNumber.getValue()) + 1);
			break;
		case FormScannerConstants.RESPONSES_BY_GRID:
			table = createGridPositionsTable(((Integer) rowsNumber.getValue()), ((Integer) colsNumber.getValue()) * 2);
			break;
		case FormScannerConstants.BARCODE:
			table = createBarcodePositionsTable(2, 4);
			break;
		}

		return table;
	}

	private JPanel getFieldTypePanel() {

		FieldType fields[] = FieldType.values();
		types = new InternalFieldType[fields.length];

		for (FieldType type : fields) {
			types[type.getIndex()] = new InternalFieldType(type);
		}

		typeComboBox = new ComboBoxBuilder<InternalFieldType>(FormScannerConstants.TYPE_COMBO_BOX, orientation)
				.withModel(new DefaultComboBoxModel<>(types)).withActionListener(manageTemplateController).withActionCommand(FormScannerConstants.FIELD_TYPE).build();

		fieldsTypeButtonPanel = getFieldsTypeButtonPanel();

		JPanel typePanel = new PanelBuilder(orientation).withLayout(new SpringLayout())
				.add(getLabel(FormScannerTranslationKeys.FIELD_PROPERTIES_TYPE_LABEL)).add(typeComboBox).withGrid(1, 2)
				.build();

		return new PanelBuilder(orientation).withLayout(new BorderLayout())
				.addComponent(fieldsTypeButtonPanel, BorderLayout.SOUTH).addComponent(typePanel, BorderLayout.NORTH)
				.build();
	}

	private JPanel getRowsColsPanel() {

		boolean isGrid = fieldsType.equals(FormScannerConstants.RESPONSES_BY_GRID);

		isMultiple = new CheckBoxBuilder(FormScannerConstants.IS_MULTIPLE, orientation)
				.withActionCommand(FormScannerConstants.IS_MULTIPLE).withActionListener(manageTemplateController)
				.setChecked(false).build();

		rejectMultiple = new CheckBoxBuilder(FormScannerConstants.REJECT_IF_NOT_MULTIPLE, orientation).setChecked(false)
				.setEnabled(!isMultiple.isSelected()).build();

		rowsNumber = new SpinnerBuilder(FormScannerConstants.NUMBER_ROWS, orientation)
				.withActionListener(manageTemplateController).withFocusListener(manageTemplateController).build();

		colsNumber = new SpinnerBuilder(FormScannerConstants.NUMBER_COLS, orientation)
				.withActionListener(manageTemplateController).withFocusListener(manageTemplateController).build();

		PanelBuilder panelBuilder = new PanelBuilder(orientation).withLayout(new SpringLayout());

		if (model.isGroupsEnabled()) {
			String[] usedGroupNames = getUsedGroupNamesList();
			setOfQuestionsCombo = new ComboBoxBuilder<String>(FormScannerConstants.GROUP, orientation)
					.withModel(new DefaultComboBoxModel<>(usedGroupNames)).setEditable(true)
					.withActionListener(manageTemplateController).withActionCommand(FormScannerConstants.GROUP).build();
			
			setOfQuestionsCombo.setSelectedIndex(setOfQuestionsCombo.getItemCount()-1);
			
			panelBuilder.withGrid(5, 2).add(getLabel(FormScannerTranslationKeys.SET_OF_QUESTIONS_LABEL))
					.add(setOfQuestionsCombo);
		} else {
			panelBuilder.withGrid(4, 2);
		}

		return panelBuilder.add(getLabel(FormScannerTranslationKeys.FIELD_PROPERTIES_IS_MULTIPLE)).add(isMultiple)
				.add(getLabel(FormScannerTranslationKeys.FIELD_PROPERTIES_REJECT_MULTIPLE)).add(rejectMultiple)
				.add(getLabel(isGrid ? FormScannerTranslationKeys.FIELD_PROPERTIES_N_ROWS_LABEL
						: FormScannerTranslationKeys.FIELD_PROPERTIES_N_QUESTIONS_LABEL))
				.add(rowsNumber).add(getLabel(isGrid ? FormScannerTranslationKeys.FIELD_PROPERTIES_N_COLS_LABEL
						: FormScannerTranslationKeys.FIELD_PROPERTIES_N_VALUES_LABEL))
				.add(colsNumber).build();
	}

	private JLabel getLabel(String value) {
		return new LabelBuilder(FormScannerTranslation.getTranslationFor(value), orientation)
				.withBorder(BorderFactory.createEmptyBorder()).build();
	}

	private JPanel getFieldsTypeButtonPanel() {

		okTypesButton = new ButtonBuilder(orientation)
				.withText(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON))
				.withToolTip(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.CONFIRM).withActionListener(manageTemplateController)
				.setEnabled(true).build();

		cancelTypesButton = new ButtonBuilder(orientation)
				.withText(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON))
				.withToolTip(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.CANCEL).withActionListener(manageTemplateController).build();

		JPanel innerPanel = new PanelBuilder(orientation).withLayout(new SpringLayout()).add(okTypesButton)
				.add(cancelTypesButton).withGrid(1, 2).build();

		return new PanelBuilder(orientation).withLayout(new BorderLayout()).add(innerPanel, BorderLayout.EAST).build();
	}

	private JPanel getPropertiesButtonPanel() {

		okPropertiesButton = new ButtonBuilder(orientation)
				.withText(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON))
				.withToolTip(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.CONFIRM).withActionListener(manageTemplateController)
				.setEnabled(fieldsType.equals(FormScannerConstants.BARCODE)).build();

		cancelPropertiesButton = new ButtonBuilder(orientation)
				.withText(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON))
				.withToolTip(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.CANCEL).withActionListener(manageTemplateController).build();

		JPanel innerPanel = new PanelBuilder(orientation).withLayout(new SpringLayout()).add(okPropertiesButton)
				.add(cancelPropertiesButton).withGrid(1, 2).build();

		return new PanelBuilder(orientation).withLayout(new BorderLayout()).add(innerPanel, BorderLayout.EAST).build();
	}

	private JPanel getFieldListButtonPanel() {

		saveTemplateButton = new ButtonBuilder(orientation)
				.withText(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.SAVE_TEMPLATE_BUTTON))
				.withToolTip(FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.SAVE_TEMPLATE_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.SAVE_TEMPLATE).withActionListener(manageTemplateController)
				.setEnabled(false).build();

		cancelTemplateButton = new ButtonBuilder(orientation)
				.withText(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON))
				.withToolTip(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.CANCEL).withActionListener(manageTemplateController).build();

		JPanel innerPanel = new PanelBuilder(orientation).withLayout(new SpringLayout()).add(saveTemplateButton)
				.add(cancelTemplateButton).withGrid(1, 2).build();

		return new PanelBuilder(orientation).withLayout(new BorderLayout()).add(innerPanel, BorderLayout.EAST).build();
	}

	private JPanel getManageListButtonPanel() {

		addFieldButton = new ButtonBuilder(orientation)
				.withIcon(FormScannerResources.getIconFor(FormScannerResourcesKeys.ADD_FIELD_BUTTON))
				.withToolTip(
						FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.ADD_FIELD_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.ADD_FIELD).withActionListener(manageTemplateController).build();

		removeFieldButton = new ButtonBuilder(orientation)
				.withIcon(FormScannerResources.getIconFor(FormScannerResourcesKeys.REMOVE_FIELD_BUTTON))
				.withToolTip(FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.REMOVE_FIELD_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.REMOVE_FIELD).withActionListener(manageTemplateController)
				.setEnabled(false).build();

		return new PanelBuilder(orientation).withLayout(new SpringLayout()).add(addFieldButton).add(removeFieldButton)
				.withGrid(2, 1).build();
	}

	private JPanel getPositionButtonPanel() {

		okPositionButton = new ButtonBuilder(orientation)
				.withText(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON))
				.withToolTip(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.CONFIRM).withActionListener(manageTemplateController)
				.setEnabled(false).build();

		cancelPositionButton = new ButtonBuilder(orientation)
				.withText(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON))
				.withToolTip(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.CANCEL).withActionListener(manageTemplateController).build();

		JPanel innerPanel = new PanelBuilder(orientation).withLayout(new SpringLayout()).add(okPositionButton)
				.add(cancelPositionButton).withGrid(1, 2).build();

		return new PanelBuilder(orientation).withLayout(new BorderLayout()).add(innerPanel, BorderLayout.EAST).build();
	}

	public String getSelectedField() {
		String fieldName = (String) fieldsTable.getValueAt(fieldsTable.getSelectedRow(), 1);
		return fieldName;
	}

	public String getSelectedGroup() {
		String groupName = (String) fieldsTable.getValueAt(fieldsTable.getSelectedRow(), 0);
		return groupName;
	}

	public void enableRemoveFields() {
		removeFieldButton.setEnabled(true);
	}

	public void removeSelectedField() {
		FieldsTableModel fieldsTableModel = (FieldsTableModel) fieldsTable.getModel();

		fieldsTableModel.removeRow(fieldsTable.getSelectedRow());
	}

	public boolean getIsMultiple() {
		return isMultiple.isSelected();
	}

	public void enableRejectMultiple(boolean enable) {
		rejectMultiple.setEnabled(enable);
	}

	@Override
	public boolean isAdvanceable(int tab) {
		boolean advanceable = false;
		switch (tab) {
		case 0:
			break;
		case 1:
			break;
		case 2:
			advanceable = verifyPropertiesValues();
			break;
		case 3:
			advanceable = verifyQuestionLabel();
			break;
		default:
			break;
		}
		return advanceable;
	}

	@Override
	public boolean isAdvanceable() {
		// TODO Auto-generated method stub
		return false;
	}

	public void addItem() {
		ArrayList<String> items = new ArrayList<String>();
		for (int i=0; i<setOfQuestionsCombo.getItemCount(); i++) {
			items.add(setOfQuestionsCombo.getItemAt(i));
		}
				// model.getUsedGroupNamesList();
		String item = (String) setOfQuestionsCombo.getSelectedItem();
		if (items.contains(item)) return;
		setOfQuestionsCombo.removeItemListener(manageTemplateController);
		setOfQuestionsCombo.insertItemAt(item, 0);
		setOfQuestionsCombo.addItemListener(manageTemplateController);
		//model.addUsedGroupName(item);
	}
}
