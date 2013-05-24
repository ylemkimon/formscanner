package org.albertoborsetta.formscanner.gui;

import org.apache.commons.lang3.StringUtils;

import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JButton;

import javax.swing.JCheckBox;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.albertoborsetta.formscanner.commons.FormField;
import org.albertoborsetta.formscanner.commons.FormPoint;
import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.FormScannerGridLayouts;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Action;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Mode;
import org.albertoborsetta.formscanner.commons.resources.FormScannerResourcesKeys;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.gui.builder.ButtonBuilder;
import org.albertoborsetta.formscanner.gui.builder.CheckBoxBuilder;
import org.albertoborsetta.formscanner.gui.builder.ComboBoxBuilder;
import org.albertoborsetta.formscanner.gui.builder.LabelBuilder;
import org.albertoborsetta.formscanner.gui.builder.ListBuilder;
import org.albertoborsetta.formscanner.gui.builder.PanelBuilder;
import org.albertoborsetta.formscanner.gui.builder.SpinnerBuilder;
import org.albertoborsetta.formscanner.gui.builder.TabbedPaneBuilder;
import org.albertoborsetta.formscanner.gui.builder.ScrollPaneBuilder;
import org.albertoborsetta.formscanner.gui.controller.InternalFrameController;
import org.albertoborsetta.formscanner.gui.controller.ManageTemplateController;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class ManageTemplateFrame extends JInternalFrame implements TabbedView {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTabbedPane tabbedPane;
	private JScrollPane fieldPositionScrollPane;

	private JList fieldList;
	private JButton addFieldButton;
	private JButton removeFieldButton;
	private JButton saveTemplateButton;
	private JButton cancelTemplateButton;

	private JSpinner valuesNumber;
	private JSpinner rowsNumber;
	private JComboBox typeComboBox;
	private JCheckBox isMultiple;
	private JButton okPropertiesButton;
	private JButton cancelPropertiesButton;

	private JTable table;
	private JButton okPositionButton;
	private JButton cancelPositionButton;

	private FormScannerModel formScannerModel;
	private ManageTemplateController manageTemplateController;
	private InternalFrameController internalFrameController;

	private File file;

	private class TemplateTableModel extends DefaultTableModel {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		public TemplateTableModel(int rows, int cols) {
			super(rows, cols);
			addTableModelListener(manageTemplateController);
		}

		public boolean isCellEditable(int row, int col) {
			if (row == 0 && col == 0) {
				return false;
			}
			return super.isCellEditable(row, col);
		}
	}

	/**
	 * Create the frame.
	 */
	public ManageTemplateFrame(FormScannerModel model, File file) {
		this.file = file;
		formScannerModel = model;

		internalFrameController = InternalFrameController
				.getInstance(formScannerModel);
		addInternalFrameListener(internalFrameController);

		manageTemplateController = new ManageTemplateController(
				formScannerModel);
		manageTemplateController.add(this);

		setName(FormScannerConstants.MANAGE_TEMPLATE_FRAME_NAME);
		setTitle(formScannerModel
				.getTranslationFor(FormScannerTranslationKeys.MANAGE_TEMPLATE_FRAME_TITLE));
		setBounds(100, 100, 600, 500);
		setMinimumSize(new Dimension(300, 500));
		setClosable(true);
		setMaximizable(true);
		setIconifiable(true);
		setResizable(true);

		InternalFrameController internalFrameController = InternalFrameController
				.getInstance(formScannerModel);
		addInternalFrameListener(internalFrameController);

		JPanel fieldListPanel = getFieldListPanel();
		JPanel fieldPropertiesPanel = getFieldPropertiesPanel();
		JPanel fieldPositionPanel = getFieldPositionPanel();

		tabbedPane = new TabbedPaneBuilder(JTabbedPane.TOP)
				.addTab(formScannerModel
						.getTranslationFor(FormScannerTranslationKeys.FIELD_LIST_TAB_NAME),
						fieldListPanel)
				.addTab(formScannerModel
						.getTranslationFor(FormScannerTranslationKeys.FIELD_PROPERTIES_TAB_NAME),
						fieldPropertiesPanel)
				.addTab(formScannerModel
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
			if (currTab < tabbedPane.getTabCount() - 1) {
				nextTab = currTab + 1;
			}
			tabbedPane.setEnabledAt(nextTab, true);
			tabbedPane.setSelectedIndex(nextTab);

			switch (nextTab) {
			case 0:
				tabbedPane.setEnabledAt(1, false);
				tabbedPane.setEnabledAt(2, false);
				formScannerModel.disposeRelatedFrame(this);

				HashMap<String, FormField> fields = new HashMap<String, FormField>();
				for (int i = 1; i < (Integer) rowsNumber.getValue(); i++) {
					String name = (String) table.getValueAt(i, 0);
					FormField field = new FormField(name);
					for (int j = 1; j < (Integer) valuesNumber.getValue(); j++) {
						String value = (String) table.getValueAt(0, j);
						FormPoint p = getPointFromTable(i, j);
						field.setPoint(value, p);
					}
					field.setMultiple(isMultiple.isSelected());
					fields.put(name, field);
				}
				formScannerModel.addFields(fields);
				resetSelectedValues();
				resetTable();
				break;
			case 2:
				setupTable();
				formScannerModel.createImageFrame(file, Mode.UPDATE);
				if (formScannerModel.getTemplateCorners().isEmpty()) {
					formScannerModel.calculateTemplateCorners();
					formScannerModel.calculateRotation();
				}
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
				resetTable();
				formScannerModel.disposeRelatedFrame(this);
				break;
			default:
				formScannerModel.disposeRelatedFrame(this);
				dispose();
				break;
			}
			break;
		default:
			break;
		}
	}

	private FormPoint getPointFromTable(int i, int j) {
		String vals = (String)table.getValueAt(i,j);
		vals = StringUtils.remove(vals, '(');
		vals = StringUtils.remove(vals, ')');
		String[] coords = StringUtils.split(vals, ',');
		
		int x = Integer.parseInt(coords[0]);
		int y = Integer.parseInt(coords[1]);

		return new FormPoint(x, y);
	}

	public int getRowsNumber() {
		return (Integer) rowsNumber.getValue();
	}

	public int getValuesNumber() {
		return (Integer) valuesNumber.getValue();
	}

	public void setupTable(List<Point> points) {
		for (int i = 0; i < (Integer) rowsNumber.getValue(); i++) {
			for (int j = 0; j < (Integer) valuesNumber.getValue(); j++) {
				int index = ((Integer) valuesNumber.getValue() * i) + j;
				Point p = points.get(index);
				String point = "(" + p.x + "," + p.y + ")";
				table.setValueAt(point, (i + 1), (j + 1));
			}
		}
	}

	public void clearTable() {
		table.selectAll();
		table.clearSelection();
	}

	private void setupTable() {
		fieldPositionScrollPane.remove(table);

		table = createTable(((Integer) rowsNumber.getValue()) + 1,
				((Integer) valuesNumber.getValue()) + 1);

		fieldPositionScrollPane.setViewportView(table);
		table.setVisible(true);
	}

	private void resetSelectedValues() {
		rowsNumber.setValue(0);
		valuesNumber.setValue(0);
		typeComboBox.setSelectedIndex(0);
	}

	private void resetTable() {
		fieldPositionScrollPane.remove(table);
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

	private JTable createTable(int rows, int cols) {
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
					(String) formScannerModel
							.getTranslationFor(FormScannerTranslationKeys.RESPONSE)
							+ " " + i, 0, i);
		}

		for (int i = 1; i < rows; i++) {
			table.setValueAt(
					(String) formScannerModel
							.getTranslationFor(FormScannerTranslationKeys.QUESTION)
							+ " " + i, i, 0);
		}
		table.setCellSelectionEnabled(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		return table;
	}

	private JPanel getFieldListPanel() {
		fieldList = new ListBuilder().withSelectionMode(
				ListSelectionModel.SINGLE_SELECTION).build();

		JScrollPane fieldListScrollPane = new ScrollPaneBuilder(fieldList)
				.build();

		JPanel fieldListManageButtonPanel = getManageListButtonPanel();
		JPanel fieldListButtonPanel = getFieldListButtonPanel();

		return new PanelBuilder().withBorderLayout()
				.addComponent(fieldListScrollPane, BorderLayout.CENTER)
				.addComponent(fieldListManageButtonPanel, BorderLayout.EAST)
				.addComponent(fieldListButtonPanel, BorderLayout.SOUTH).build();
	}

	private JPanel getFieldPropertiesPanel() {

		JPanel propertiesPanel = getPropertiesPanel();
		JPanel propertiesButtonPanel = getPropertiesButtonPanel();

		return new PanelBuilder().withBorderLayout()
				.addComponent(propertiesPanel, BorderLayout.CENTER)
				.addComponent(propertiesButtonPanel, BorderLayout.SOUTH)
				.build();
	}

	public JPanel getFieldPositionPanel() {

		JPanel positionButtonPanel = getPositionButtonPanel();

		table = new JTable();
		fieldPositionScrollPane = new ScrollPaneBuilder(table).build();

		return new PanelBuilder().withBorderLayout()
				.addComponent(positionButtonPanel, BorderLayout.SOUTH)
				.addComponent(fieldPositionScrollPane, BorderLayout.CENTER)
				.build();
	}

	private JPanel getPropertiesPanel() {

		JLabel lblType = new LabelBuilder(
				formScannerModel
						.getTranslationFor(FormScannerTranslationKeys.FIELD_PROPERTIES_TYPE_LABEL))
				.build();

		typeComboBox = new ComboBoxBuilder(FormScannerConstants.TYPE_COMBO_BOX)
				.addItem(
						formScannerModel
								.getTranslationFor(FormScannerTranslationKeys.QUESTIONS_BY_ROWS))
				.addItem(
						formScannerModel
								.getTranslationFor(FormScannerTranslationKeys.QUESTIONS_BY_COLS))
				.withItemListener(manageTemplateController).build();

		JLabel lblIsMultiple = new LabelBuilder(
				formScannerModel
						.getTranslationFor(FormScannerTranslationKeys.FIELD_PROPERTIES_IS_MULTIPLE))
				.build();

		isMultiple = new CheckBoxBuilder(FormScannerConstants.IS_MULTIPLE)
				.withChangeListener(manageTemplateController).setChecked(false)
				.build();

		JLabel lblNumberOfRowsColumns = new LabelBuilder(
				formScannerModel
						.getTranslationFor(FormScannerTranslationKeys.FIELD_PROPERTIES_N_ROW_COL_LABEL))
				.build();

		rowsNumber = new SpinnerBuilder(FormScannerConstants.NUMBER_COLS_ROWS)
				.withChangeListener(manageTemplateController).build();

		JLabel lblNumberOfValues = new LabelBuilder(
				formScannerModel
						.getTranslationFor(FormScannerTranslationKeys.FIELD_PROPERTIES_N_VALUES_LABEL))
				.build();

		valuesNumber = new SpinnerBuilder(FormScannerConstants.NUMBER_VALUES)
				.withChangeListener(manageTemplateController).build();

		return new PanelBuilder()
				.withFormLayout(FormScannerGridLayouts.propertiesFormLayout())
				.addComponent(lblType, "2, 2, right, default")
				.addComponent(typeComboBox, "4, 2, fill, default")
				.addComponent(lblIsMultiple, "2, 4, right, default")
				.addComponent(isMultiple, "4, 4, fill, default")
				.addComponent(lblNumberOfRowsColumns, "2, 6, right, default")
				.addComponent(rowsNumber, "4, 6")
				.addComponent(lblNumberOfValues, "2, 8, right, default")
				.addComponent(valuesNumber, "4, 8").build();
	}

	private JPanel getPropertiesButtonPanel() {

		okPropertiesButton = new ButtonBuilder()
				.withText(
						formScannerModel
								.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON))
				.withToolTip(
						formScannerModel
								.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.CONFIRM)
				.withActionListener(manageTemplateController).setEnabled(false)
				.build();

		cancelPropertiesButton = new ButtonBuilder()
				.withText(
						formScannerModel
								.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON))
				.withToolTip(
						formScannerModel
								.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.CANCEL)
				.withActionListener(manageTemplateController).build();

		return new PanelBuilder()
				.withFormLayout(FormScannerGridLayouts.buttonsLayout())
				.addComponent(okPropertiesButton, "2, 2, right, default")
				.addComponent(cancelPropertiesButton, "4, 2, right, default")
				.build();
	}

	private JPanel getFieldListButtonPanel() {

		saveTemplateButton = new ButtonBuilder()
				.withText(
						formScannerModel
								.getTranslationFor(FormScannerTranslationKeys.SAVE_TEMPLATE_BUTTON))
				.withToolTip(
						formScannerModel
								.getTranslationFor(FormScannerTranslationKeys.SAVE_TEMPLATE_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.SAVE_TEMPLATE)
				.withActionListener(manageTemplateController).setEnabled(false)
				.build();

		cancelTemplateButton = new ButtonBuilder()
				.withText(
						formScannerModel
								.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON))
				.withToolTip(
						formScannerModel
								.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.CANCEL)
				.withActionListener(manageTemplateController).build();

		return new PanelBuilder()
				.withFormLayout(FormScannerGridLayouts.buttonsLayout())
				.addComponent(saveTemplateButton, "2, 2, right, default")
				.addComponent(cancelTemplateButton, "4, 2, right, default")
				.build();
	}

	private JPanel getManageListButtonPanel() {

		addFieldButton = new ButtonBuilder()
				.withIcon(
						formScannerModel
								.getIconFor(FormScannerResourcesKeys.ADD_FIELD_BUTTON))
				.withToolTip(
						formScannerModel
								.getTranslationFor(FormScannerTranslationKeys.ADD_FIELD_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.ADD_FIELD)
				.withActionListener(manageTemplateController).build();

		removeFieldButton = new ButtonBuilder()
				.withIcon(
						formScannerModel
								.getIconFor(FormScannerResourcesKeys.REMOVE_FIELD_BUTTON))
				.withToolTip(
						formScannerModel
								.getTranslationFor(FormScannerTranslationKeys.REMOVE_FIELD_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.REMOVE_FIELD)
				.withActionListener(manageTemplateController).setEnabled(false)
				.build();

		return new PanelBuilder()
				.withFormLayout(
						FormScannerGridLayouts.manageListButtonsLayout())
				.addComponent(addFieldButton, "2, 2, fill, default")
				.addComponent(removeFieldButton, "2, 4, fill, default").build();
	}

	private JPanel getPositionButtonPanel() {

		okPositionButton = new ButtonBuilder()
				.withText(
						formScannerModel
								.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON))
				.withToolTip(
						formScannerModel
								.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.CONFIRM)
				.withActionListener(manageTemplateController).setEnabled(false)
				.build();

		cancelPositionButton = new ButtonBuilder()
				.withText(
						formScannerModel
								.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON))
				.withToolTip(
						formScannerModel
								.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.CANCEL)
				.withActionListener(manageTemplateController).build();

		return new PanelBuilder()
				.withFormLayout(FormScannerGridLayouts.buttonsLayout())
				.addComponent(okPositionButton, "2, 2, right, default")
				.addComponent(cancelPositionButton, "4, 2, right, default")
				.build();
	}
}
