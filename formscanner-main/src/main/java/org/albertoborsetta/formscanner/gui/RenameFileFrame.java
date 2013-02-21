package org.albertoborsetta.formscanner.gui;

import org.albertoborsetta.formscanner.commons.Constants;
import org.albertoborsetta.formscanner.commons.FormScannerFont;
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
	private FormScannerModel model;
	private JButton okButton;
	private JButton cancelButton;
	private RenameFileController renameFileController;
	private InternalFrameController internalFrameController;
	
	/**
	 * Create the frame.
	 */
	public RenameFileFrame(FormScannerModel formScannerModel, String fileName) {
		model = formScannerModel;
		renameFileController = new RenameFileController(model);
		renameFileController.add(this);
		
		setBounds(220, 320, 300, 130);
		setName("renameFileFrame");
		setClosable(true);
		
		internalFrameController = InternalFrameController.getInstance(model);
		addInternalFrameListener(internalFrameController);
		
		setTitle("Rename file");
		
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
		
		JLabel newFileNameLabel = new JLabel("New name:");
		panel.add(newFileNameLabel, "2, 2, right, default");
		
		fileNameField = new FileNameField(FilenameUtils.removeExtension(fileName));		
		panel.add(fileNameField, "4, 2, 3, 1, fill, default");
		
		fileExtensionField = new JLabel('.' + FilenameUtils.getExtension(fileName));
		panel.add(fileExtensionField, "8, 2");		
		
		okButton = new OKButton();
		panel.add(okButton , "4, 4");
		
		cancelButton = new CancelButton();
		panel.add(cancelButton, "6, 4");
				
		statusBar = new StatusBar("Renaming: " + fileName);
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
		statusBar.setText("Rename: " + fileName);
		fileNameField.setText(FilenameUtils.removeExtension(fileName));
		fileExtensionField.setText('.' + FilenameUtils.getExtension(fileName));
	}
	
	public String getNewFileName() {
		String fileName = fileNameField.getText() + fileExtensionField.getText(); 
		return fileName;
	}
	
	private class StatusBar extends JLabel {
		
		public StatusBar(String label) {
			super(label);
			setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
			setFont(FormScannerFont.getFont());
		}
	}
	
	private class OKButton extends JButton {
		
		public OKButton() {
			super("OK");
			setEnabled(false);
			setActionCommand(Constants.RENAME_FILE_CURRENT);
			addActionListener(renameFileController);
		}
	}
	
	private class CancelButton extends JButton {
		
		public CancelButton() {
			super("Cancel");
			setActionCommand(Constants.RENAME_FILE_SKIP);
			addActionListener(renameFileController);
		}
	}
	
	private class FileNameField extends JTextField {
		
		public FileNameField(String text) {
			super(text);
			setColumns(10);
			addKeyListener(renameFileController);
		}		
	}
}
