package org.albertoborsetta.formscanner.gui;

import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.FormScannerFont;
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
import javax.swing.border.BevelBorder;

import java.awt.BorderLayout;

import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.factories.FormFactory;

public class RenameFileFrame extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	
	private JTextField fileNameField;
	private JLabel fileExtensionField;
	private JLabel statusBar;
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
		setBounds(220, 320, 370, 130);
		setClosable(true);
		
		internalFrameController = InternalFrameController.getInstance(formScannerModel);
		addInternalFrameListener(internalFrameController);
		
		setTitle(formScannerModel.getTranslationFor(FormScannerTranslationKeys.RENAME_FILE_FRAME_TITLE));
		
		JPanel panel = new JPanel();
		getContentPane().add(panel, BorderLayout.CENTER);
		panel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.PREF_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.PREF_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel newFileNameLabel = new JLabel(formScannerModel.getTranslationFor(FormScannerTranslationKeys.RENAME_FILE_FRAME_LABEL) + ": ");
		panel.add(newFileNameLabel, "2, 2, right, default");
		
		fileNameField = new FileNameField();		
		panel.add(fileNameField, "4, 2, 3, 1, fill, default");
		
		fileExtensionField = new JLabel();
		panel.add(fileExtensionField, "8, 2");		
		
		okButton = new OKButton();
		panel.add(okButton , "4, 4");
		
		cancelButton = new CancelButton();
		panel.add(cancelButton, "6, 4");
				
		statusBar = new StatusBar();
		
		updateRenamedFile(fileName);		
		
		getContentPane().add(statusBar, BorderLayout.SOUTH);
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
		statusBar.setText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.RENAME_FILE_FRAME_STATUSBAR) + ": " + fileName);
		fileNameField.setText(FilenameUtils.removeExtension(fileName));
		fileExtensionField.setText('.' + FilenameUtils.getExtension(fileName));
	}
	
	public String getNewFileName() {
		String fileName = fileNameField.getText() + fileExtensionField.getText(); 
		return fileName;
	}
	
	private class StatusBar extends JLabel {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public StatusBar() {
			super();
			setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			setFont(FormScannerFont.getFont());
		}
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
