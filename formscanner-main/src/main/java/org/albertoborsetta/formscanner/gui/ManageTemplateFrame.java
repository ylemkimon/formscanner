package org.albertoborsetta.formscanner.gui;

import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JButton;

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

import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.FormScannerGridLayouts;
import org.albertoborsetta.formscanner.commons.resources.FormScannerResourcesKeys;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.controller.InternalFrameController;
import org.albertoborsetta.formscanner.controller.ManageTemplateController;
import org.albertoborsetta.formscanner.gui.builder.ButtonBuilder;
import org.albertoborsetta.formscanner.gui.builder.ComboBoxBuilder;
import org.albertoborsetta.formscanner.gui.builder.LabelBuilder;
import org.albertoborsetta.formscanner.gui.builder.ListBuilder;
import org.albertoborsetta.formscanner.gui.builder.PanelBuilder;
import org.albertoborsetta.formscanner.gui.builder.SpinnerBuilder;
import org.albertoborsetta.formscanner.gui.builder.TabbedPaneBuilder;
import org.albertoborsetta.formscanner.gui.builder.ScrollPaneBuilder;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class ManageTemplateFrame extends JInternalFrame {
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

	private JSpinner numberValues;
	private JSpinner numberRowsCols;
	private JComboBox typeComboBox;
	private JButton okPropertiesButton;
	private JButton cancelPropertiesButton;
	
	private JTable table;
	private JButton okPositionButton;
	private JButton cancelPositionButton;
	
	private FormScannerModel formScannerModel;
	private ManageTemplateController manageTemplateController;
	private InternalFrameController internalFrameController;

	/**
	 * Create the frame.
	 */
	public ManageTemplateFrame(FormScannerModel model, String fileName) {
		formScannerModel = model;
		
		internalFrameController = InternalFrameController.getInstance(formScannerModel);
		addInternalFrameListener(internalFrameController);
		
		manageTemplateController = new ManageTemplateController(formScannerModel);	
		manageTemplateController.add(this);
		
		setName(FormScannerConstants.MANAGE_TEMPLATE_FRAME_NAME);		
		setTitle(formScannerModel.getTranslationFor(FormScannerTranslationKeys.MANAGE_TEMPLATE_FRAME_TITLE));
		setBounds(100, 100, 600, 500);
		setMinimumSize(new Dimension(300, 500));
		setClosable(true);
		setMaximizable(true);
		setIconifiable(true);
		setResizable(true);
		
		InternalFrameController internalFrameController = InternalFrameController.getInstance(formScannerModel);
		addInternalFrameListener(internalFrameController);
		
		JPanel fieldListPanel = getFieldListPanel();
		JPanel fieldPropertiesPanel = getFieldPropertiesPanel();
		JPanel fieldPositionPanel = getFieldPositionPanel();
		
		tabbedPane = new TabbedPaneBuilder(JTabbedPane.TOP)
			.addTab(formScannerModel.getTranslationFor(FormScannerTranslationKeys.FIELD_LIST_TAB_NAME), fieldListPanel)
			.addTab(formScannerModel.getTranslationFor(FormScannerTranslationKeys.FIELD_PROPERTIES_TAB_NAME), fieldPropertiesPanel)
			.addTab(formScannerModel.getTranslationFor(FormScannerTranslationKeys.FIELD_POSITION_TAB_NAME), fieldPositionPanel)
			.build();
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		enablePropertiesPanel(false);
		enablePositionPanel(false);
	}
	
	public void setupTable() {
		fieldPositionScrollPane.remove(table);
		
		table = createTable(((Integer) numberRowsCols.getValue())+1, ((Integer) numberValues.getValue())+1);
				
		fieldPositionScrollPane.setViewportView(table);
		table.setVisible(true);
	}
	
	public void enableTemplateListPanel(boolean enabled) {
		tabbedPane.setEnabledAt(0, enabled);
	}
	
	public void enablePropertiesPanel(boolean enabled) {
		tabbedPane.setEnabledAt(1, enabled);
	}
	
	public void enablePositionPanel(boolean enabled) {
		tabbedPane.setEnabledAt(2, enabled);
	}
	
	public void setTemplateListPanel() {
		tabbedPane.setSelectedIndex(0);
	}
	
	public void setPropertiesPanel() {
		tabbedPane.setSelectedIndex(1);
	}
	
	public void setPositionPanel() {
		tabbedPane.setSelectedIndex(2);
	}
	
	public void selectField(Integer i) {
		fieldList.setSelectedIndex(0);
		enablePositionPanel(false);
		enablePropertiesPanel(false);
	}
	
	public void resetSelectedValues() {
		numberRowsCols.setValue(0);
		numberValues.setValue(0);
		typeComboBox.setSelectedIndex(0);
	}
	
	public void resetTable() {
		fieldPositionScrollPane.remove(table);
	}
	
	public void verifySpinnerValues() {
		if ((Integer) numberValues.getValue()<0) {
			numberValues.setValue(0);
		}
		if ((Integer) numberRowsCols.getValue()<0) {
			numberRowsCols.setValue(0);
		}
	}
	
	public boolean verifyAdvancement() {		
		return (((Integer) numberValues.getValue()>0) && 
				((Integer) numberRowsCols.getValue()>0) && 
				(typeComboBox.getSelectedItem()!=null));
	}
	
	public void setAdvancement(boolean enabled) {
		okPropertiesButton.setEnabled(enabled);
	}
	
	public Integer getCurrentTabbedPaneIndex() {
		return tabbedPane.getSelectedIndex();
	}
	
	private JTable createTable(int rows, int cols) {
		TemplateTableModel tableModel = new TemplateTableModel(rows, cols);
		TableColumnModel columnModel = new DefaultTableColumnModel();
		
		for (int i=0; i<cols; i++) {
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
		    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
		    {
		        final Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		        if (row == 0 || column == 0) {
					cell.setBackground(new java.awt.Color(238, 238, 238));
				} else {
					cell.setBackground(Color.white);
				}
		        return cell;
		    }
		});
		
		for (int i=1; i<cols; i++) {
			table.setValueAt((String)formScannerModel.getTranslationFor(FormScannerTranslationKeys.RESPONSE) + " " + i, 0, i);
		}
		
		for (int i=1; i<rows; i++) {
			table.setValueAt((String)formScannerModel.getTranslationFor(FormScannerTranslationKeys.QUESTION) + " " + i, i, 0);
		}
		table.setCellSelectionEnabled(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		return table;
	}
	
	private class TemplateTableModel extends DefaultTableModel {
		
		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		public TemplateTableModel(int rows, int cols) {
			super(rows, cols);
		}
		
		public boolean isCellEditable(int row, int col) {
			if (row==0 && col==0) {  
		        return false;  
		    } 
		    return super.isCellEditable(row, col);
		}
	}
	
	private JPanel getFieldListPanel() {
		fieldList = new ListBuilder()
			.withSelectionMode(ListSelectionModel.SINGLE_SELECTION)
			.build();
		
		JScrollPane fieldListScrollPane = new ScrollPaneBuilder(fieldList)
			.build();		

		JPanel fieldListManageButtonPanel = getManageListButtonPanel();
		JPanel fieldListButtonPanel = getFieldListButtonPanel();
		
		return new PanelBuilder()
			.withBorderLayout()
			.addComponent(fieldListScrollPane, BorderLayout.CENTER)
			.addComponent(fieldListManageButtonPanel, BorderLayout.EAST)
			.addComponent(fieldListButtonPanel, BorderLayout.SOUTH)
			.build();
	}
	
	private JPanel getFieldPropertiesPanel() {
		
		JPanel propertiesPanel = getPropertiesPanel();
		JPanel propertiesButtonPanel = getPropertiesButtonPanel();
			
		return new PanelBuilder()
			.withBorderLayout()
			.addComponent(propertiesPanel, BorderLayout.CENTER)
			.addComponent(propertiesButtonPanel, BorderLayout.SOUTH)
			.build();
	}
		
	public JPanel getFieldPositionPanel() {
		
		JPanel positionButtonPanel = getPositionButtonPanel();
		
		table = new JTable();
		fieldPositionScrollPane = new ScrollPaneBuilder(table)
			.build();
		
		return new PanelBuilder()
			.withBorderLayout()
			.addComponent(positionButtonPanel, BorderLayout.SOUTH)
			.addComponent(fieldPositionScrollPane, BorderLayout.CENTER)
			.build();
	}
	
	private JPanel getPropertiesPanel() {
		
		JLabel lblType = new LabelBuilder(formScannerModel.getTranslationFor(FormScannerTranslationKeys.FIELD_PROPERTIES_TYPE_LABEL))
			.build();
		
		typeComboBox = new ComboBoxBuilder(FormScannerConstants.TYPE_COMBO_BOX)
			.addItem(formScannerModel.getTranslationFor(FormScannerTranslationKeys.QUESTIONS_BY_ROWS))
			.addItem(formScannerModel.getTranslationFor(FormScannerTranslationKeys.QUESTIONS_BY_COLS))
			.withItemListener(manageTemplateController)
			.build();
		
		JLabel lblNumberOfRowsColumns = new LabelBuilder(formScannerModel.getTranslationFor(FormScannerTranslationKeys.FIELD_PROPERTIES_N_ROW_COL_LABEL))
			.build();
		
		numberRowsCols = new SpinnerBuilder(FormScannerConstants.NUMBER_COLS_ROWS)
			.withChangeListener(manageTemplateController)
			.build();
		
		JLabel lblNumberOfValues = new LabelBuilder(formScannerModel.getTranslationFor(FormScannerTranslationKeys.FIELD_PROPERTIES_N_VALUES_LABEL))
			.build();
		
		numberValues = new SpinnerBuilder(FormScannerConstants.NUMBER_VALUES)
			.withChangeListener(manageTemplateController)
			.build();
		
		
		return new PanelBuilder()
			.withFormLayout(FormScannerGridLayouts.propertiesFormLayout())
			.addComponent(lblType, "2, 2, right, default")
			.addComponent(typeComboBox, "4, 2, fill, default")
			.addComponent(lblNumberOfRowsColumns, "2, 4, right, default")
			.addComponent(numberRowsCols, "4, 4")
			.addComponent(lblNumberOfValues, "2, 6, right, default")
			.addComponent(numberValues, "4, 6")
			.build();
	}
	
	private JPanel getPropertiesButtonPanel() {
		
		okPropertiesButton = new ButtonBuilder()
			.withText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON))
			.withToolTip(formScannerModel.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON_TOOLTIP))
			.withActionCommand(FormScannerConstants.CONFIRM)
			.withActionListener(manageTemplateController)
			.setEnabled(false)
			.build();
		
		cancelPropertiesButton = new ButtonBuilder()
			.withText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON))
			.withToolTip(formScannerModel.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON_TOOLTIP))
			.withActionCommand(FormScannerConstants.CANCEL)
			.withActionListener(manageTemplateController)
			.build();
		
		return new PanelBuilder()
			.withFormLayout(FormScannerGridLayouts.buttonsLayout())
			.addComponent(okPropertiesButton, "2, 2, right, default")
			.addComponent(cancelPropertiesButton, "4, 2, right, default")
			.build();
	}
	
	private JPanel getFieldListButtonPanel() {	
			
		saveTemplateButton = new ButtonBuilder()
			.withText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.SAVE_TEMPLATE_BUTTON))
			.withToolTip(formScannerModel.getTranslationFor(FormScannerTranslationKeys.SAVE_TEMPLATE_BUTTON_TOOLTIP))
			.withActionCommand(FormScannerConstants.SAVE_TEMPLATE)
			.withActionListener(manageTemplateController)
			.setEnabled(false)
			.build();
		
		cancelTemplateButton = new ButtonBuilder()
			.withText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON))
			.withToolTip(formScannerModel.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON_TOOLTIP))
			.withActionCommand(FormScannerConstants.CANCEL)
			.withActionListener(manageTemplateController)
			.build();
		
		return new PanelBuilder()
			.withFormLayout(FormScannerGridLayouts.buttonsLayout())
			.addComponent(saveTemplateButton, "2, 2, right, default")
			.addComponent(cancelTemplateButton, "4, 2, right, default")
			.build();
	}
	
	private JPanel getManageListButtonPanel() {			
			
		addFieldButton = new ButtonBuilder()
			.withIcon(formScannerModel.getIconFor(FormScannerResourcesKeys.ADD_FIELD_BUTTON))
			.withToolTip(formScannerModel.getTranslationFor(FormScannerTranslationKeys.ADD_FIELD_BUTTON_TOOLTIP))
			.withActionCommand(FormScannerConstants.ADD_FIELD)
			.withActionListener(manageTemplateController)
			.build();
		
		removeFieldButton = new ButtonBuilder()
			.withIcon(formScannerModel.getIconFor(FormScannerResourcesKeys.REMOVE_FIELD_BUTTON))
			.withToolTip(formScannerModel.getTranslationFor(FormScannerTranslationKeys.REMOVE_FIELD_BUTTON_TOOLTIP))
			.withActionCommand(FormScannerConstants.REMOVE_FIELD)
			.withActionListener(manageTemplateController)
			.setEnabled(false)
			.build();
		
		return new PanelBuilder()
			.withFormLayout(FormScannerGridLayouts.manageListButtonsLayout())
			.addComponent(addFieldButton, "2, 2, fill, default")
			.addComponent(removeFieldButton, "2, 4, fill, default")
			.build();
	}
	
	private JPanel getPositionButtonPanel() {
		
		okPositionButton = new ButtonBuilder()
			.withText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON))
			.withToolTip(formScannerModel.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON_TOOLTIP))
			.withActionCommand(FormScannerConstants.CONFIRM)
			.withActionListener(manageTemplateController)
			.setEnabled(false)
			.build();
	
		cancelPositionButton = new ButtonBuilder()
			.withText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON))
			.withToolTip(formScannerModel.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON_TOOLTIP))
			.withActionCommand(FormScannerConstants.CANCEL)
			.withActionListener(manageTemplateController)
			.build();
		
		return new PanelBuilder()
			.withFormLayout(FormScannerGridLayouts.buttonsLayout())
			.addComponent(okPositionButton, "2, 2, right, default")
			.addComponent(cancelPositionButton, "4, 2, right, default")
			.build();
	} 
	
}
