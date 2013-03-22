package org.albertoborsetta.formscanner.gui;

import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.controller.RenameFileController;
import org.albertoborsetta.formscanner.controller.InternalFrameController;
import org.albertoborsetta.formscanner.model.FormScannerModel;

import org.apache.commons.io.FilenameUtils;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.BorderLayout;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

public class RenameFileFrame extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	
	private JTextField fileNameField;
	private JLabel fileExtensionField;
	private FormScannerModel formScannerModel;
	private JButton okButton;
	private JButton cancelButton;
	private RenameFileController renameFileController;
	private InternalFrameController internalFrameController;
	
	/**
	 * Create the frame.
	 */
	public RenameFileFrame(FormScannerModel formScannerModel, String fileName) {
		this.formScannerModel = formScannerModel;
		renameFileController = new RenameFileController(formScannerModel);
		renameFileController.add(this);
		
		setName(FormScannerConstants.RENAME_FILE_FRAME_NAME);
		setBounds(220, 320, 370, 100);
		setClosable(true);
		setLayout(new BorderLayout());
		
		internalFrameController = InternalFrameController.getInstance(formScannerModel);
		addInternalFrameListener(internalFrameController);
		
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("pref:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		JLabel newFileNameLabel = new JLabel(formScannerModel.getTranslationFor(FormScannerTranslationKeys.RENAME_FILE_FRAME_LABEL) + ": ");
		panel.add(newFileNameLabel, "2, 2, right, default");
		
		fileNameField = new FileNameField();		
		panel.add(fileNameField, "4, 2, fill, default");
		
		fileExtensionField = new JLabel();
		panel.add(fileExtensionField, "6, 2");
		
		JPanel buttonPanel = new JPanel();
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
		buttonPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.GROWING_BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.BUTTON_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.PREF_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,}));
		
		okButton = new OKButton();
		buttonPanel.add(okButton , "2, 2, right, default");
		
		cancelButton = new CancelButton();
		buttonPanel.add(cancelButton, "4, 2, right, default");
		
		updateRenamedFile(fileName);		
	}
	
	public boolean isOkEnabled() {
		return okButton.isEnabled();
	}
	
	public boolean isCancelEnabled() {
		return cancelButton.isEnabled();
	}
	
	public void setOkEnabled(boolean value) {
		okButton.setEnabled(value);
	}
	
	public void updateRenamedFile(String fileName) {
		setTitle(formScannerModel.getTranslationFor(FormScannerTranslationKeys.RENAME_FILE_FRAME_TITLE) + ": " + fileName);
		fileNameField.setText(FilenameUtils.removeExtension(fileName));
		fileExtensionField.setText('.' + FilenameUtils.getExtension(fileName));
	}
	
	public String getNewFileName() {
		String fileName = fileNameField.getText() + fileExtensionField.getText(); 
		return fileName;
	}
	
	private class OKButton extends JButton {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public OKButton() {
			super(formScannerModel.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON));
			setEnabled(false);
			setActionCommand(FormScannerConstants.RENAME_FILE_CURRENT);
			addActionListener(renameFileController);
		}
	}
	
	private class CancelButton extends JButton {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public CancelButton() {
			super(formScannerModel.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON));
			setActionCommand(FormScannerConstants.RENAME_FILE_SKIP);
			addActionListener(renameFileController);
		}
	}
	
	private class FileNameField extends JTextField {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public FileNameField() {
			super();
			setColumns(10);
			addKeyListener(renameFileController);
		}		
	}
}
