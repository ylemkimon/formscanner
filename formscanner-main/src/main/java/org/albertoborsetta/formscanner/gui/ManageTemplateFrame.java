package org.albertoborsetta.formscanner.gui;

import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.text.Normalizer.Form;

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

import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.resources.FormScannerResourcesKeys;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.controller.InternalFrameController;
import org.albertoborsetta.formscanner.controller.ManageTemplateController;
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

	/**
	 * Create the frame.
	 */
	public ManageTemplateFrame(FormScannerModel formScannerModel, String fileName) {
		this.formScannerModel = formScannerModel;
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
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		// Field List
		fieldListPanel = new FieldListPanel();	
		tabbedPane.addTab(formScannerModel.getTranslationFor(FormScannerTranslationKeys.FIELD_LIST_TAB_NAME), null, fieldListPanel, null);
		
		// Field Properties
		fieldPropertiesPanel = new FieldPropertiesPanel();
		tabbedPane.addTab(formScannerModel.getTranslationFor(FormScannerTranslationKeys.FIELD_PROPERTIES_TAB_NAME), null, fieldPropertiesPanel, null);
		
		// Field Positions
		fieldPositionPanel = new FieldPositionPanel();
		tabbedPane.addTab(formScannerModel.getTranslationFor(FormScannerTranslationKeys.FIELD_POSITION_TAB_NAME), null, fieldPositionPanel, null);
		
		enablePropertiesPanel(false);
		enablePositionPanel(false);
	}
	
	public void enablePropertiesPanel(boolean enabled) {
		tabbedPane.setEnabledAt(1, enabled);
	}
	
	public void enablePositionPanel(boolean enabled) {
		tabbedPane.setEnabledAt(2, enabled);
	}
	
	public void setPropertiesPanel() {
		tabbedPane.setSelectedIndex(1);
	}
	
	public void setPositionPanel() {
		tabbedPane.setSelectedIndex(2);
	}
	
	public Integer getSpinnerValue(Object spinner) {
		return (Integer) ((JSpinner)spinner).getValue();		
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
			fieldPositionScrollPane.add(table);
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
			
			okPositionButton = new OKButton();
			add(okPositionButton, "2, 2, right, default");
			
			cancelPositionButton = new CancelButton();
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
						
			okPropertiesButton = new OKButton();
			add(okPropertiesButton, "2, 2, right, default");
			
			cancelPropertiesButton = new CancelButton();
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
	
	private void setComboItems() {
		typeComboBox.addItem("item 1");
		typeComboBox.addItem("item 2");
		typeComboBox.addItem("item 3");
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
			
			saveTemplateButton = new SaveButton();
			add(saveTemplateButton, "2, 2, right, default");
			
			cancelTemplateButton = new CancelButton();
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
			
			addFieldButton = new AddButton();			
			add(addFieldButton, "2, 2, fill, default");
			
			removeFieldButton = new RemoveButton();
			add(removeFieldButton, "2, 4, fill, default");
		}
	}
	
	private class AddButton extends JButton {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public AddButton() {
			super();
			setIcon(formScannerModel.getIconFor(FormScannerResourcesKeys.ADD_FIELD_BUTTON));
			setToolTipText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.ADD_FIELD_BUTTON_TOOLTIP));
			setActionCommand(FormScannerConstants.ADD_FIELD);
			addActionListener(manageTemplateController);
		}
	}

	private class RemoveButton extends JButton {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public RemoveButton() {
			super();
			setIcon(formScannerModel.getIconFor(FormScannerResourcesKeys.REMOVE_FIELD_BUTTON));
			setToolTipText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.REMOVE_FIELD_BUTTON_TOOLTIP));
			setEnabled(false);
			setActionCommand(FormScannerConstants.REMOVE_FIELD);
			addActionListener(manageTemplateController);
		}
	}
	
	private class OKButton extends JButton {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public OKButton() {
			super(formScannerModel.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON));
			setToolTipText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON_TOOLTIP));
			setEnabled(false);
			setActionCommand(FormScannerConstants.CONFIRM);
			addActionListener(manageTemplateController);
		}
	}
	
	private class CancelButton extends JButton {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public CancelButton() {
			super(formScannerModel.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON));
			setToolTipText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON_TOOLTIP));
			setActionCommand(FormScannerConstants.CANCEL);
			addActionListener(manageTemplateController);
		}
	}
	
	private class SaveButton extends JButton {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public SaveButton() {
			super(formScannerModel.getTranslationFor(FormScannerTranslationKeys.SAVE_TEMPLATE_BUTTON));
			setToolTipText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.SAVE_TEMPLATE_BUTTON_TOOLTIP));
			setEnabled(false);
			setActionCommand(FormScannerConstants.SAVE_TEMPLATE);
			addActionListener(manageTemplateController);
		}
	}
}
