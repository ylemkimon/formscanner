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
		setBounds(100, 100, 300, 500);
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
		fieldPositionPanel = new JPanel();
		fieldPositionPanel.setLayout(new BorderLayout());
		tabbedPane.addTab(formScannerModel.getTranslationFor(FormScannerTranslationKeys.FIELD_POSITION_TAB_NAME), null, fieldPositionPanel, null);
		
		positionButtonPanel = new JPanel();
		positionButtonPanel.setLayout(new FormLayout(new ColumnSpec[] {
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
		positionButtonPanel.add(okPositionButton, "2, 2, right, default");
		
		cancelPositionButton = new CancelButton();
		positionButtonPanel.add(cancelPositionButton, "4, 2, right, default");
		
		fieldPositionPanel.add(positionButtonPanel, BorderLayout.SOUTH);
		
		fieldPositionScrollPane = new JScrollPane();
		table = new JTable();
		fieldPositionScrollPane.add(table);
		fieldPositionPanel.add(fieldPositionScrollPane, BorderLayout.CENTER);
	
		disableUnusedTab();
	}
	
	private void disableUnusedTab() {
		for (int i=1; i<tabbedPane.getTabCount(); i++) {
			tabbedPane.setEnabledAt(i, false);
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

			fieldListManageButtonPanel = new JPanel();
			fieldListPanel.add(fieldListManageButtonPanel, BorderLayout.EAST);
			fieldListManageButtonPanel.setLayout(new FormLayout(new ColumnSpec[] {
					FormFactory.RELATED_GAP_COLSPEC,
					FormFactory.GROWING_BUTTON_COLSPEC,
					FormFactory.RELATED_GAP_COLSPEC,},
				new RowSpec[] {
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.PREF_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.PREF_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,}));
			
			addFieldButton = new JButton();
			addFieldButton.setIcon(formScannerModel.getIconFor(FormScannerResourcesKeys.ADD_FIELD_BUTTON));
			addFieldButton.setToolTipText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.ADD_FIELD_BUTTON_TOOLTIP));
			fieldListManageButtonPanel.add(addFieldButton, "2, 2, fill, default");
			
			removeFieldButton = new JButton();
			removeFieldButton.setIcon(formScannerModel.getIconFor(FormScannerResourcesKeys.REMOVE_FIELD_BUTTON));
			removeFieldButton.setToolTipText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.REMOVE_FIELD_BUTTON_TOOLTIP));
			removeFieldButton.setEnabled(false);
			fieldListManageButtonPanel.add(removeFieldButton, "2, 4, fill, default");
			
			fieldListButtonPanel = new JPanel();
			add(fieldListButtonPanel, BorderLayout.SOUTH);
			
			fieldListButtonPanel.setLayout(new FormLayout(new ColumnSpec[] {
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
			fieldListButtonPanel.add(saveTemplateButton, "2, 2, right, default");
			
			cancelTemplateButton = new CancelButton();
			fieldListButtonPanel.add(cancelTemplateButton, "4, 2, right, default");
		}
		
	}
	
	private class FieldPropertiesPanel extends JPanel {
		
		public FieldPropertiesPanel() {
			super();
			setLayout(new BorderLayout());
			
			propertiesPanel = new JPanel();
			add(propertiesPanel, BorderLayout.CENTER);
			
			propertiesPanel.setLayout(new FormLayout(new ColumnSpec[] {
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
			propertiesPanel.add(lblType, "2, 2, right, default");
			
			JComboBox typeComboBox = new JComboBox();
			propertiesPanel.add(typeComboBox, "4, 2, fill, default");
			
			JLabel lblNumberOfRowsColumns = new JLabel(formScannerModel.getTranslationFor(FormScannerTranslationKeys.FIELD_PROPERTIES_N_ROW_COL_LABEL));
			propertiesPanel.add(lblNumberOfRowsColumns, "2, 4, right, default");
			
			JSpinner numberRowsCols = new JSpinner();
			propertiesPanel.add(numberRowsCols, "4, 4");
			
			JLabel lblNumberOfValues = new JLabel(formScannerModel.getTranslationFor(FormScannerTranslationKeys.FIELD_PROPERTIES_N_VALUES_LABEL));
			propertiesPanel.add(lblNumberOfValues, "2, 6, right, default");
			
			JSpinner numberValues = new JSpinner();
			propertiesPanel.add(numberValues, "4, 6");
						
			propertiesButtonPanel = new JPanel();
			propertiesButtonPanel.setLayout(new FormLayout(new ColumnSpec[] {
					FormFactory.RELATED_GAP_COLSPEC,
					FormFactory.GROWING_BUTTON_COLSPEC,
					FormFactory.RELATED_GAP_COLSPEC,
					FormFactory.BUTTON_COLSPEC,
					FormFactory.RELATED_GAP_COLSPEC,},
				new RowSpec[] {
					FormFactory.RELATED_GAP_ROWSPEC,
					FormFactory.PREF_ROWSPEC,
					FormFactory.RELATED_GAP_ROWSPEC,}));
			
			add(propertiesButtonPanel, BorderLayout.SOUTH);
			
			okPropertiesButton = new OKButton();
			propertiesButtonPanel.add(okPropertiesButton, "2, 2, right, default");
			
			cancelPropertiesButton = new CancelButton();
			propertiesButtonPanel.add(cancelPropertiesButton, "4, 2, right, default");
		}
		
	}
	
	private class FieldPositionPanel extends JPanel {
		
	}
}
