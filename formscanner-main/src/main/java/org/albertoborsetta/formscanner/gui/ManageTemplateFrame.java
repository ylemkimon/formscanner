package org.albertoborsetta.formscanner.gui;

import javax.swing.JTabbedPane;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.util.HashMap;
import java.util.List;

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
import javax.swing.ListSelectionModel;
import javax.swing.SpringLayout;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableRowSorter;

import org.apache.commons.lang3.StringUtils;
import org.albertoborsetta.formscanner.commons.CharSequenceGenerator;
import org.albertoborsetta.formscanner.commons.FormField;
import org.albertoborsetta.formscanner.commons.FormPoint;
import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.FieldType;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Action;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Frame;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.FieldsTableColumn;
import org.albertoborsetta.formscanner.commons.resources.FormScannerResources;
import org.albertoborsetta.formscanner.commons.resources.FormScannerResourcesKeys;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.gui.builder.ButtonBuilder;
import org.albertoborsetta.formscanner.gui.builder.CheckBoxBuilder;
import org.albertoborsetta.formscanner.gui.builder.ComboBoxBuilder;
import org.albertoborsetta.formscanner.gui.builder.LabelBuilder;
import org.albertoborsetta.formscanner.gui.builder.PanelBuilder;
import org.albertoborsetta.formscanner.gui.builder.SpinnerBuilder;
import org.albertoborsetta.formscanner.gui.builder.TabbedPaneBuilder;
import org.albertoborsetta.formscanner.gui.builder.ScrollPaneBuilder;
import org.albertoborsetta.formscanner.gui.controller.ManageTemplateController;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class ManageTemplateFrame extends InternalFrame implements TabbedView {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTabbedPane tabbedPane;
	private JScrollPane positionsTableScrollPane;
	private JScrollPane fieldsTableScrollPane;

	private JButton addFieldButton;
	private JButton removeFieldButton;
	private JButton saveTemplateButton;
	private JButton cancelTemplateButton;

	private JSpinner valuesNumber;
	private JSpinner rowsNumber;
	private JComboBox<FieldType> typeComboBox;
	private JCheckBox isMultiple;
	private JButton okPropertiesButton;
	private JButton cancelPropertiesButton;

	private JTable positionsTable;
	private JTable fieldsTable;
	private JButton okPositionButton;
	private JButton cancelPositionButton;

	private ManageTemplateController manageTemplateController;
	private int previousRowsCount;

	public class FieldsTableModel extends DefaultTableModel {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		public FieldsTableModel() {
			super();
		}

		public boolean isCellEditable(int row, int col) {
			return false;
		}

		@Override
		public Class<?> getColumnClass(int columnIndex) {
			return (columnIndex == 2) ? Boolean.class : super
					.getColumnClass(columnIndex);
		}
	}

	/**
	 * Create the frame.
	 */
	public ManageTemplateFrame(FormScannerModel model) {
		super(model);

		manageTemplateController = new ManageTemplateController(model);
		manageTemplateController.add(this);

		setName(Frame.MANAGE_TEMPLATE_FRAME.name());
		setTitle(FormScannerTranslation
				.getTranslationFor(FormScannerTranslationKeys.MANAGE_TEMPLATE_FRAME_TITLE));
		setBounds(model.getLastPosition(Frame.MANAGE_TEMPLATE_FRAME));
		setMinimumSize(new Dimension(300, 500));
		setClosable(true);
		setMaximizable(true);
		setIconifiable(true);
		setResizable(true);
		previousRowsCount = 1;

		JPanel fieldListPanel = getFieldListPanel();
		JPanel fieldPropertiesPanel = getFieldPropertiesPanel();
		JPanel fieldPositionPanel = getFieldPositionPanel();

		tabbedPane = new TabbedPaneBuilder(JTabbedPane.TOP)
				.addTab(FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.FIELD_LIST_TAB_NAME),
						fieldListPanel)
				.addTab(FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.FIELD_PROPERTIES_TAB_NAME),
						fieldPropertiesPanel)
				.addTab(FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.FIELD_POSITION_TAB_NAME),
						fieldPositionPanel).build();

		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		tabbedPane.setEnabledAt(1, false);
		tabbedPane.setEnabledAt(2, false);
	}

	public void setupNextTab(String action) {
		int currTab = tabbedPane.getSelectedIndex();
		int nextTab = 0;
		Action act = Action.valueOf(action);
		switch (act) {
		case CONFIRM:
			nextTab = (currTab + 1) % tabbedPane.getTabCount();

			tabbedPane.setEnabledAt(nextTab, true);
			tabbedPane.setSelectedIndex(nextTab);

			switch (nextTab) {
			case 0:
				for (int i = nextTab + 1; i < tabbedPane.getTabCount(); i++) {
					tabbedPane.setEnabledAt(i, false);
				}
				model.disposeRelatedFrame(this);

				HashMap<String, FormField> fields = createFields();
				model.updateTemplate(fields);
				setupFieldsTable(model.getTemplate().getFields());
				saveTemplateButton.setEnabled(true);
				resetSelectedValues();
				break;
			case 2:
				resetPositionsTable();
				model.createTemplateImageFrame();
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
				resetSelectedValues();
				break;
			case 1:
				model.resetPoints();
				model.disposeRelatedFrame(this);
				break;
			default:
				dispose();
				break;
			}
			break;
		default:
			break;
		}
	}

	private HashMap<String, FormField> createFields() {
		HashMap<String, FormField> fields = new HashMap<String, FormField>();

		for (int i = 1; i < (Integer) rowsNumber.getValue() + 1; i++) {
			String name = (String) positionsTable.getValueAt(i, 0);
			FormField field = new FormField(name);
			for (int j = 1; j < (Integer) valuesNumber.getValue() + 1; j++) {
				String value = (String) positionsTable.getValueAt(0, j);
				FormPoint p = getPointFromTable(i, j);
				field.setPoint(value, p);
				field.setType(getFieldType());
			}
			field.setMultiple(isMultiple.isSelected());
			fields.put(name, field);
		}
		return fields;
	}

	private FormPoint getPointFromTable(int i, int j) {
		return FormPoint.toPoint((String) positionsTable.getValueAt(i, j));
	}

	public int getRowsNumber() {
		return (Integer) rowsNumber.getValue();
	}

	public int getValuesNumber() {
		return (Integer) valuesNumber.getValue();
	}

	public void setupPositionTable(List<FormPoint> points) {
		for (int i = 0; i < (Integer) rowsNumber.getValue(); i++) {
			for (int j = 0; j < (Integer) valuesNumber.getValue(); j++) {
				int index = ((Integer) valuesNumber.getValue() * i) + j;
				FormPoint p = points.get(index);
				positionsTable.setValueAt(p.toString(), (i + 1), (j + 1));
			}
		}
	}

	private void removePositionsTable() {
		positionsTableScrollPane.remove(positionsTable);
	}

	private void resetPositionsTable() {
		removePositionsTable();

		positionsTable = createPositionsTable(
				((Integer) rowsNumber.getValue()) + 1,
				((Integer) valuesNumber.getValue()) + 1);

		positionsTableScrollPane.setViewportView(positionsTable);
		positionsTable.setVisible(true);
	}

	public void setupFieldsTable(HashMap<String, FormField> fields) {
		FieldsTableModel fieldsTableModel = (FieldsTableModel) fieldsTable
				.getModel();
		while (fieldsTable.getRowCount() > 0) {
			fieldsTableModel.removeRow(fieldsTable.getRowCount() - 1);
		}

		for (FormField field : fields.values()) {
			fieldsTableModel.addRow(new Object[] { field.getName(),
					field.getType().getValue(), field.isMultiple(),
					field.getPoints().size() });
		}

	}

	private void resetSelectedValues() {
		rowsNumber.setValue(0);
		valuesNumber.setValue(0);
		typeComboBox.setSelectedIndex(0);
	}

	public FieldType getFieldType() {
		return (FieldType) typeComboBox.getSelectedItem();
	}

	public void setAdvanceable() {
		int currTab = tabbedPane.getSelectedIndex();

		switch (currTab) {
		case 0:
			break;
		case 1:
			okPropertiesButton.setEnabled(verifySpinnerValues());
			break;
		case 2:
			okPositionButton.setEnabled(true);
			break;
		default:
			break;
		}

	}

	private boolean verifySpinnerValues() {
		if ((Integer) valuesNumber.getValue() < 0) {
			valuesNumber.setValue(0);
		}
		if ((Integer) rowsNumber.getValue() < 0) {
			rowsNumber.setValue(0);
		}
		return (((Integer) valuesNumber.getValue() > 0)
				&& ((Integer) rowsNumber.getValue() > 0) && (typeComboBox
					.getSelectedItem() != null));
	}

	private JTable createPositionsTable(int rows, int cols) {
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
			public void addTableModelListener(
					TableModelListener tableModelListener) {
				super.addTableModelListener(manageTemplateController);
			}

			public boolean isCellEditable(int row, int col) {
				if (row == 0 && col == 0) {
					return false;
				}
				return ((row * col) == 0);
			}
		}, columnModel);

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
					if (isSelected) {
						cell.setBackground(Color.lightGray);
					} else {
						cell.setBackground(Color.white);
					}
				}
				cell.repaint();
				return cell;
			}
		});

		CharSequenceGenerator charSequence = new CharSequenceGenerator();

		for (int i = 1; i < cols; i++) {
			table.setValueAt(charSequence.next(), 0, i);
		}

		for (int i = 1; i < rows; i++) {
			table.setValueAt(
					(String) FormScannerTranslation
							.getTranslationFor(FormScannerTranslationKeys.QUESTION)
							+ " " + StringUtils.leftPad("" + previousRowsCount++, 2, "0"), i, 0);
		}
		table.setCellSelectionEnabled(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		return table;
	}

	private JTable createFieldsTable() {
		FieldsTableModel fieldsTableModel = new FieldsTableModel();
		JTable table = new JTable(fieldsTableModel);
		table.setRowSorter(new TableRowSorter<FieldsTableModel>(
				fieldsTableModel));

		for (FieldsTableColumn tableColumn : FieldsTableColumn.values()) {
			fieldsTableModel.addColumn(tableColumn.getValue());
		}

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
				if (isSelected) {
					cell.setBackground(Color.lightGray);
				} else {
					cell.setBackground(Color.white);
				}
				cell.repaint();
				return cell;
			}
		});

		table.setRowSelectionAllowed(true);
		table.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		table.getSelectionModel().addListSelectionListener(
				manageTemplateController);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		return table;
	}

	private JPanel getFieldListPanel() {

		fieldsTable = createFieldsTable();
		fieldsTableScrollPane = new ScrollPaneBuilder(fieldsTable).build();

		JPanel fieldListManageButtonPanel = getManageListButtonPanel();
		JPanel fieldListButtonPanel = getFieldListButtonPanel();

		return new PanelBuilder().withLayout(new BorderLayout())
				.addComponent(fieldsTableScrollPane, BorderLayout.CENTER)
				.addComponent(fieldListManageButtonPanel, BorderLayout.EAST)
				.addComponent(fieldListButtonPanel, BorderLayout.SOUTH).build();
	}

	private JPanel getFieldPropertiesPanel() {

		JPanel propertiesPanel = getPropertiesPanel();
		JPanel propertiesButtonPanel = getPropertiesButtonPanel();

		return new PanelBuilder().withLayout(new BorderLayout())
				.addComponent(propertiesPanel, BorderLayout.NORTH)
				.addComponent(propertiesButtonPanel, BorderLayout.SOUTH)
				.build();
	}

	public JPanel getFieldPositionPanel() {

		JPanel positionButtonPanel = getPositionButtonPanel();

		positionsTable = new JTable();
		positionsTableScrollPane = new ScrollPaneBuilder(positionsTable)
				.build();

		return new PanelBuilder().withLayout(new BorderLayout())
				.addComponent(positionButtonPanel, BorderLayout.SOUTH)
				.addComponent(positionsTableScrollPane, BorderLayout.CENTER)
				.build();
	}

	private JPanel getPropertiesPanel() {

		typeComboBox = new ComboBoxBuilder<FieldType>(
				FormScannerConstants.TYPE_COMBO_BOX)
				.withModel(
						new DefaultComboBoxModel<FieldType>(FieldType.values()))
				.withActionListener(manageTemplateController).build();

		isMultiple = new CheckBoxBuilder(FormScannerConstants.IS_MULTIPLE)
				.withActionListener(manageTemplateController).setChecked(false)
				.build();

		rowsNumber = new SpinnerBuilder(FormScannerConstants.NUMBER_COLS_ROWS)
				.withActionListener(manageTemplateController).build();

		valuesNumber = new SpinnerBuilder(FormScannerConstants.NUMBER_VALUES)
				.withActionListener(manageTemplateController).build();

		return new PanelBuilder()
				.withLayout(new SpringLayout())
				.add(getLabel(FormScannerTranslationKeys.FIELD_PROPERTIES_TYPE_LABEL))
				.add(typeComboBox)
				.add(getLabel(FormScannerTranslationKeys.FIELD_PROPERTIES_IS_MULTIPLE))
				.add(isMultiple)
				.add(getLabel(FormScannerTranslationKeys.FIELD_PROPERTIES_N_ROW_COL_LABEL))
				.add(rowsNumber)
				.add(getLabel(FormScannerTranslationKeys.FIELD_PROPERTIES_N_VALUES_LABEL))
				.add(valuesNumber).withGrid(4, 2).build();
	}

	private JLabel getLabel(String value) {
		return new LabelBuilder(FormScannerTranslation.getTranslationFor(value))
				.withBorder(BorderFactory.createEmptyBorder()).build();
	}

	private JPanel getPropertiesButtonPanel() {

		okPropertiesButton = new ButtonBuilder()
				.withText(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON))
				.withToolTip(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.CONFIRM)
				.withActionListener(manageTemplateController).setEnabled(false)
				.build();

		cancelPropertiesButton = new ButtonBuilder()
				.withText(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON))
				.withToolTip(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.CANCEL)
				.withActionListener(manageTemplateController).build();

		return new PanelBuilder().withLayout(new SpringLayout())
				.add(okPropertiesButton).add(cancelPropertiesButton)
				.withGrid(1, 2).build();
	}

	private JPanel getFieldListButtonPanel() {

		saveTemplateButton = new ButtonBuilder()
				.withText(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.SAVE_TEMPLATE_BUTTON))
				.withToolTip(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.SAVE_TEMPLATE_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.SAVE_TEMPLATE)
				.withActionListener(manageTemplateController).setEnabled(false)
				.build();

		cancelTemplateButton = new ButtonBuilder()
				.withText(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON))
				.withToolTip(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.CANCEL)
				.withActionListener(manageTemplateController).build();

		return new PanelBuilder().withLayout(new SpringLayout())
				.add(saveTemplateButton).add(cancelTemplateButton)
				.withGrid(1, 2).build();
	}

	private JPanel getManageListButtonPanel() {

		addFieldButton = new ButtonBuilder()
				.withIcon(
						FormScannerResources
								.getIconFor(FormScannerResourcesKeys.ADD_FIELD_BUTTON))
				.withToolTip(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.ADD_FIELD_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.ADD_FIELD)
				.withActionListener(manageTemplateController).build();

		removeFieldButton = new ButtonBuilder()
				.withIcon(
						FormScannerResources
								.getIconFor(FormScannerResourcesKeys.REMOVE_FIELD_BUTTON))
				.withToolTip(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.REMOVE_FIELD_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.REMOVE_FIELD)
				.withActionListener(manageTemplateController).setEnabled(false)
				.build();

		return new PanelBuilder().withLayout(new SpringLayout())
				.add(addFieldButton).add(removeFieldButton).withGrid(2, 1)
				.build();
	}

	private JPanel getPositionButtonPanel() {

		okPositionButton = new ButtonBuilder()
				.withText(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON))
				.withToolTip(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.CONFIRM)
				.withActionListener(manageTemplateController).setEnabled(false)
				.build();

		cancelPositionButton = new ButtonBuilder()
				.withText(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON))
				.withToolTip(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.CANCEL)
				.withActionListener(manageTemplateController).build();

		return new PanelBuilder().withLayout(new SpringLayout())
				.add(okPositionButton).add(cancelPositionButton).withGrid(1, 2)
				.build();
	}

	public String getSelectedItem() {

		String fieldName = (String) fieldsTable.getValueAt(
				fieldsTable.getSelectedRow(), 0);
		return fieldName;
	}

	public void enableRemoveFields() {
		removeFieldButton.setEnabled(true);
	}

	public void removeSelectedField() {
		FieldsTableModel fieldsTableModel = (FieldsTableModel) fieldsTable
				.getModel();

		fieldsTableModel.removeRow(fieldsTable.getSelectedRow());
	}
}
