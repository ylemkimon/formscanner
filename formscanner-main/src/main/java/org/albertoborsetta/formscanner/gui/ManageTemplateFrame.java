package org.albertoborsetta.formscanner.gui;

import javax.swing.JInternalFrame;
import javax.swing.JTabbedPane;
import java.awt.BorderLayout;
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
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.controller.InternalFrameController;
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
	private JPanel fieldListButtonPanel;
	private JButton addButton;
	private JButton removeButton;
		
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
	// private ManageTemplateController manageTemplateController;

	/**
	 * Create the frame.
	 */
	public ManageTemplateFrame(FormScannerModel formScannerModel, String fileName) {
		this.formScannerModel = formScannerModel;
		// manageTemplateController = new ManageTemplateController(formScannerModel);		
		setName(FormScannerConstants.MANAGE_TEMPLATE_FRAME_NAME);		
		setTitle(formScannerModel.getTranslationFor(FormScannerTranslationKeys.MANAGE_TEMPLATE_FRAME_TITLE));
		setBounds(100, 100, 450, 300);
		setClosable(true);
		setMaximizable(true);
		setIconifiable(true);
		
		InternalFrameController internalFrameController = InternalFrameController.getInstance(formScannerModel);
		addInternalFrameListener(internalFrameController);
		
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		// Field List
		fieldListPanel = new JPanel();
		tabbedPane.addTab(formScannerModel.getTranslationFor(FormScannerTranslationKeys.FIELD_LIST_TAB_NAME), null, fieldListPanel, null);
				
		fieldList = new JList();		
		fieldListScrollPane = new JScrollPane();	
		fieldListScrollPane.setViewportView(fieldList);
		fieldListPanel.add(fieldListScrollPane, BorderLayout.CENTER);	

		fieldListButtonPanel = new JPanel();
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
		
		addButton = new JButton(formScannerModel.getTranslationFor(FormScannerTranslationKeys.ADD_BUTTON));
		fieldListButtonPanel.add(addButton, "2, 2");
		
		removeButton = new JButton(formScannerModel.getTranslationFor(FormScannerTranslationKeys.REMOVE_BUTTON));
		fieldListButtonPanel.add(removeButton, "4, 2");
		
		fieldListPanel.add(fieldListButtonPanel, BorderLayout.SOUTH);
		
		
		// Field Properties
		fieldPropertiesPanel = new JPanel();
		tabbedPane.addTab(formScannerModel.getTranslationFor(FormScannerTranslationKeys.FIELD_PROPERTIES_TAB_NAME), null, fieldPropertiesPanel, null);
				
		propertiesPanel = new JPanel();
		fieldPropertiesPanel.add(propertiesPanel, BorderLayout.CENTER);
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
		
		fieldPropertiesPanel.add(propertiesButtonPanel, BorderLayout.SOUTH);
		
		okPropertiesButton = new JButton(formScannerModel.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON));
		propertiesButtonPanel.add(okPropertiesButton, "2, 2, right, default");
		
		cancelPropertiesButton = new JButton(formScannerModel.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON));
		propertiesButtonPanel.add(cancelPropertiesButton, "4, 2, right, default");
		
		// Field Positions
		fieldPositionPanel = new JPanel();
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
		
		okPositionButton = new JButton(formScannerModel.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON));
		positionButtonPanel.add(okPositionButton, "2, 2, right, default");
		
		cancelPositionButton = new JButton(formScannerModel.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON));
		positionButtonPanel.add(cancelPositionButton, "4, 2, right, default");
		
		fieldPositionPanel.add(positionButtonPanel, BorderLayout.SOUTH);
		
		fieldPositionScrollPane = new JScrollPane();
		table = new JTable();
		fieldPositionScrollPane.add(table);
		fieldPositionPanel.add(fieldPositionScrollPane, BorderLayout.CENTER);		
	}	
	
}
