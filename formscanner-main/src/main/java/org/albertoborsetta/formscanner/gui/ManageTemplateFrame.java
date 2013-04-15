package org.albertoborsetta.formscanner.gui;

import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JList;
import javax.swing.JButton;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JSpinner;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.resources.FormScannerResourcesKeys;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.controller.InternalFrameController;
import org.albertoborsetta.formscanner.controller.ManageTemplateController;
import org.albertoborsetta.formscanner.gui.builder.Button;
import org.albertoborsetta.formscanner.gui.builder.TabbedPane;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class ManageTemplateFrame extends JInternalFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTabbedPane tabbedPane;
	
	private JPanel fieldListPanel;
	private JScrollPane fieldListScrollPane;
	private JList fieldList;
	private JPanel fieldListManageButtonPanel;
	private JPanel fieldListButtonPanel;
	private JButton addFieldButton;
	private JButton removeFieldButton;
	private JButton saveTemplateButton;
	private JButton cancelTemplateButton;
		
	private JPanel fieldPropertiesPanel;
	private JPanel propertiesPanel;
	private JSpinner numberValues;
	private JSpinner numberRowsCols;
	private JComboBox typeComboBox;
	private JPanel propertiesButtonPanel;
	private JButton okPropertiesButton;
	private JButton cancelPropertiesButton;
	
	private JPanel fieldPositionPanel;
	private JScrollPane fieldPositionScrollPane;
	private JTable table;
	private JPanel positionButtonPanel;
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
		
		// Field List
		fieldListPanel = new FieldListPanel();
		// Field Properties
		fieldPropertiesPanel = new FieldPropertiesPanel();
		// Field Positions
		fieldPositionPanel = new FieldPositionPanel();
		
		tabbedPane = new TabbedPane.Builder(JTabbedPane.TOP)
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
		
		Object[][] tableData = new Object[((Integer) numberRowsCols.getValue())+1][((Integer) numberValues.getValue())+1];
		table = createTable(tableData);
				
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
	
	public boolean verifyAdvancement() {
		return (((Integer)numberValues.getValue()>0) && 
				((Integer)numberRowsCols.getValue()>0) && 
				(typeComboBox.getSelectedItem()!=null));
	}
	
	public void setAdvancement(boolean enabled) {
		okPropertiesButton.setEnabled(enabled);
	}
	
	public Integer getCurrentTabbedPaneIndex() {
		return tabbedPane.getSelectedIndex();
	}
	
	private JTable createTable(Object[][] data) {
		TableModel tableModel = new PersonalTableModel(data.length, data[0].length);
		TableColumnModel columnModel = new DefaultTableColumnModel();
		
		for (int i=0; i<data[0].length; i++) {
			TableColumn column = new TableColumn(i);
			column.setMinWidth(100);
			columnModel.addColumn(column);
		}		
		
		JTable table = new JTable(tableModel, columnModel);
		table.setValueAt((String)"Campo\\Valori", 0, 0);
		table.setCellSelectionEnabled(true);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		return table;
	}
	
	private class PersonalTableModel extends DefaultTableModel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public PersonalTableModel(int rows, int cols) {
			super(rows, cols);
		}
		
		public boolean isCellEditable(int row, int col) {
			if (row==0 && col==0) {  
		        return false;  
		    } 
		    return super.isCellEditable(row, col);
		}
	}
	private class FieldListPanel extends JPanel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public FieldListPanel() {
			super();
			setLayout(new BorderLayout());
					
			fieldList = new JList();
			fieldListScrollPane = new JScrollPane();	
			fieldListScrollPane.setViewportView(fieldList);
			add(fieldListScrollPane, BorderLayout.CENTER);		

			fieldListManageButtonPanel = new ManageListButtonPanel();
			add(fieldListManageButtonPanel, BorderLayout.EAST);
			
			fieldListButtonPanel = new FieldListButtonPanel();
			add(fieldListButtonPanel, BorderLayout.SOUTH);
		}
		
	}
	
	private class FieldPropertiesPanel extends JPanel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public FieldPropertiesPanel() {
			super();
			setLayout(new BorderLayout());
			
			propertiesPanel = new PropertiesPanel();
			add(propertiesPanel, BorderLayout.CENTER);
						
			propertiesButtonPanel = new PropertiesButtonPanel();
			add(propertiesButtonPanel, BorderLayout.SOUTH);
		}
		
	}
	
	private class FieldPositionPanel extends JPanel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public FieldPositionPanel() {
			
			super();
			setLayout(new BorderLayout());
			
			positionButtonPanel = new PositionButtonPanel();
			add(positionButtonPanel, BorderLayout.SOUTH);
			
			fieldPositionScrollPane = new JScrollPane();
			table = new JTable();
			fieldPositionScrollPane.setViewportView(table);
			add(fieldPositionScrollPane, BorderLayout.CENTER);
		}
	}
	
	private class PositionButtonPanel extends JPanel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public PositionButtonPanel() {
			super();
			setLayout(new FormLayout(new ColumnSpec[] {
					FormFactory.RELATED_GAP_COLSPEC,
					FormFactory.GROWING_BUTTON_COLSPEC,
					FormFactory.RELATED_GAP_COLSPEC,
					FormFactory.BUTTON_COLSPEC,
					FormFactory.RELATED_GAP_COLSPEC,},
				new RowSpec[] {
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.PREF_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,}));
			
			okPositionButton = new Button.Builder()
				.withText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON))
				.withToolTip(formScannerModel.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.CONFIRM)
				.withActionListener(manageTemplateController)
				.isEnabled(false)
				.build();
			add(okPositionButton, "2, 2, right, default");
			
			cancelPositionButton = new Button.Builder()
				.withText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON))
				.withToolTip(formScannerModel.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.CANCEL)
				.withActionListener(manageTemplateController)
				.build();
			add(cancelPositionButton, "4, 2, right, default");
		}
	}
	
	private class PropertiesButtonPanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public PropertiesButtonPanel() {
			super();
			setLayout(new FormLayout(new ColumnSpec[] {
					FormFactory.RELATED_GAP_COLSPEC,
					FormFactory.GROWING_BUTTON_COLSPEC,
					FormFactory.RELATED_GAP_COLSPEC,
					FormFactory.BUTTON_COLSPEC,
					FormFactory.RELATED_GAP_COLSPEC,},
				new RowSpec[] {
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.PREF_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,}));
						
			okPropertiesButton = new Button.Builder()
				.withText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON))
				.withToolTip(formScannerModel.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.CONFIRM)
				.withActionListener(manageTemplateController)
				.isEnabled(false)
				.build();
			add(okPropertiesButton, "2, 2, right, default");
			
			cancelPropertiesButton = new Button.Builder()
				.withText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON))
				.withToolTip(formScannerModel.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.CANCEL)
				.withActionListener(manageTemplateController)
				.build();
			add(cancelPropertiesButton, "4, 2, right, default");
		}
	}
	
	private class PropertiesPanel extends JPanel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public PropertiesPanel() {
			super();
			setLayout(new FormLayout(new ColumnSpec[] {
					FormFactory.RELATED_GAP_COLSPEC,
					FormFactory.DEFAULT_COLSPEC,
					FormFactory.RELATED_GAP_COLSPEC,
					ColumnSpec.decode("pref:grow"),
					FormFactory.RELATED_GAP_COLSPEC,},
				new RowSpec[] {
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.DEFAULT_ROWSPEC,}));
			
			JLabel lblType = new JLabel(formScannerModel.getTranslationFor(FormScannerTranslationKeys.FIELD_PROPERTIES_TYPE_LABEL));
			add(lblType, "2, 2, right, default");
			
			typeComboBox = new TypeComboBox();			
			add(typeComboBox, "4, 2, fill, default");
			
			JLabel lblNumberOfRowsColumns = new JLabel(formScannerModel.getTranslationFor(FormScannerTranslationKeys.FIELD_PROPERTIES_N_ROW_COL_LABEL));
			add(lblNumberOfRowsColumns, "2, 4, right, default");
			
			numberRowsCols = new ValuesSpinner(FormScannerConstants.NUMBER_COLS_ROWS);
			add(numberRowsCols, "4, 4");
			
			JLabel lblNumberOfValues = new JLabel(formScannerModel.getTranslationFor(FormScannerTranslationKeys.FIELD_PROPERTIES_N_VALUES_LABEL));
			add(lblNumberOfValues, "2, 6, right, default");
			
			numberValues = new ValuesSpinner(FormScannerConstants.NUMBER_VALUES);
			add(numberValues, "4, 6");
		}
	}
	
	private class TypeComboBox extends JComboBox {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public TypeComboBox() {
			super();
			setName(FormScannerConstants.TYPE_COMBO_BOX);
			setComboItems();
			addItemListener(manageTemplateController);
		}
		
		private void setComboItems() {
			addItem("item 1");
			addItem("item 2");
			addItem("item 3");
		}
	}
	
	private class ValuesSpinner extends JSpinner {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ValuesSpinner(String name) {
			super();
			setName(name);
			addChangeListener(manageTemplateController);
		}
	}
	
	private class FieldListButtonPanel extends JPanel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public FieldListButtonPanel() {
			super();
			setLayout(new FormLayout(new ColumnSpec[] {
					FormFactory.RELATED_GAP_COLSPEC,
					FormFactory.GROWING_BUTTON_COLSPEC,
					FormFactory.RELATED_GAP_COLSPEC,
					FormFactory.BUTTON_COLSPEC,
					FormFactory.RELATED_GAP_COLSPEC,},
				new RowSpec[] {
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.PREF_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,}));	
			
			saveTemplateButton = new Button.Builder()
				.withText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.SAVE_TEMPLATE_BUTTON))
				.withToolTip(formScannerModel.getTranslationFor(FormScannerTranslationKeys.SAVE_TEMPLATE_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.SAVE_TEMPLATE)
				.withActionListener(manageTemplateController)
				.isEnabled(false)
				.build();
			add(saveTemplateButton, "2, 2, right, default");
			
			cancelTemplateButton = new Button.Builder()
				.withText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON))
				.withToolTip(formScannerModel.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.CANCEL)
				.withActionListener(manageTemplateController)
				.build();
			add(cancelTemplateButton, "4, 2, right, default");
		}
	}
	
	private class ManageListButtonPanel extends JPanel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ManageListButtonPanel() {
			super();
			
			setLayout(new FormLayout(new ColumnSpec[] {
					FormFactory.RELATED_GAP_COLSPEC,
					FormFactory.GROWING_BUTTON_COLSPEC,
					FormFactory.RELATED_GAP_COLSPEC,},
				new RowSpec[] {
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.PREF_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.PREF_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,}));
			
			addFieldButton = new Button.Builder()
				.withIcon(formScannerModel.getIconFor(FormScannerResourcesKeys.ADD_FIELD_BUTTON))
				.withToolTip(formScannerModel.getTranslationFor(FormScannerTranslationKeys.ADD_FIELD_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.ADD_FIELD)
				.withActionListener(manageTemplateController)
				.build();
			
			add(addFieldButton, "2, 2, fill, default");
			
			removeFieldButton = new Button.Builder()
				.withIcon(formScannerModel.getIconFor(FormScannerResourcesKeys.REMOVE_FIELD_BUTTON))
				.withToolTip(formScannerModel.getTranslationFor(FormScannerTranslationKeys.REMOVE_FIELD_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.REMOVE_FIELD)
				.withActionListener(manageTemplateController)
				.isEnabled(false)
				.build();
			add(removeFieldButton, "2, 4, fill, default");
		}
	}
}
