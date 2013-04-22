package org.albertoborsetta.formscanner.gui;

import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.FormScannerGridLayouts;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.controller.RenameFileController;
import org.albertoborsetta.formscanner.controller.InternalFrameController;
import org.albertoborsetta.formscanner.gui.builder.ButtonBuilder;
import org.albertoborsetta.formscanner.gui.builder.LabelBuilder;
import org.albertoborsetta.formscanner.gui.builder.PanelBuilder;
import org.albertoborsetta.formscanner.gui.builder.TextFieldBuilder;
import org.albertoborsetta.formscanner.model.FormScannerModel;

import org.apache.commons.io.FilenameUtils;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;

import java.awt.BorderLayout;

public class RenameFileFrame extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	
	private JTextField fileNameField;
	private JLabel fileExtensionField;
	private JLabel newFileNameLabel;
	private FormScannerModel formScannerModel;
	private JButton okButton;
	private JButton cancelButton;
	private RenameFileController renameFileController;
	private InternalFrameController internalFrameController;
	private JPanel buttonPanel;
	private JPanel renamePanel;
	
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

		renamePanel = getRenamePanel();
		buttonPanel = getButtonPanel();
		
		add(renamePanel, BorderLayout.CENTER);
		add(buttonPanel, BorderLayout.SOUTH);
		
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
	
	private JPanel getRenamePanel() {
		newFileNameLabel = new LabelBuilder(formScannerModel.getTranslationFor(FormScannerTranslationKeys.RENAME_FILE_FRAME_LABEL) + ": ")
			.build();
		
		fileNameField = new TextFieldBuilder(10)
			.withKeyListener(renameFileController)
			.build();
		
		fileExtensionField = new LabelBuilder()
			.build();
		
		return new PanelBuilder()
			.withFormLayout(FormScannerGridLayouts.renameFrameLayout())
			.addComponent(fileNameField, "4, 2, fill, default")
			.addComponent(fileExtensionField, "6, 2")
			.addComponent(newFileNameLabel, "2, 2, right, default")
			.build();
	}
	
	private JPanel getButtonPanel() {
		okButton = new ButtonBuilder()
			.withText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON))
			.setEnabled(false)
			.withActionCommand(FormScannerConstants.RENAME_FILE_CURRENT)
			.withActionListener(renameFileController)
			.build();
	
		cancelButton = new ButtonBuilder()
			.withText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON))
			.withActionCommand(FormScannerConstants.RENAME_FILE_SKIP)
			.withActionListener(renameFileController)
			.build();
		
		return new PanelBuilder()
			.withFormLayout(FormScannerGridLayouts.buttonsLayout())
			.addComponent(okButton , "2, 2, right, default")
			.addComponent(cancelButton, "4, 2, right, default")
			.build();
	}
}
