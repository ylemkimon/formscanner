package com.albertoborsetta.formscanner.gui.view;

import org.apache.commons.io.FilenameUtils;

import com.albertoborsetta.formscanner.commons.FormScannerConstants;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.Frame;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import com.albertoborsetta.formscanner.gui.builder.ButtonBuilder;
import com.albertoborsetta.formscanner.gui.builder.LabelBuilder;
import com.albertoborsetta.formscanner.gui.builder.PanelBuilder;
import com.albertoborsetta.formscanner.gui.builder.TextFieldBuilder;
import com.albertoborsetta.formscanner.gui.controller.RenameFileController;
import com.albertoborsetta.formscanner.gui.model.FormScannerModel;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SpringLayout;

import java.awt.BorderLayout;

public class RenameFileFrame extends InternalFrame implements View {

	private static final long serialVersionUID = 1L;

	private JTextField fileNameField;
	private JLabel fileExtensionField;
	private JButton okButton;
	private JButton cancelButton;
	private RenameFileController renameFileController;
	private JPanel buttonPanel;
	private JPanel renamePanel;

	/**
	 * Create the frame.
	 */
	public RenameFileFrame(FormScannerModel model, String fileName) {
		super(model);
		renameFileController = new RenameFileController(model);
		renameFileController.add(this);

		setBounds(model.getLastPosition(Frame.RENAME_FILES_FRAME));
		setName(Frame.RENAME_FILES_FRAME.name());
		setClosable(true);
		setLayout(new BorderLayout());

		renamePanel = getRenamePanel();
		buttonPanel = getButtonPanel();

		add(renamePanel, BorderLayout.NORTH);
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
		setTitle(FormScannerTranslation
				.getTranslationFor(FormScannerTranslationKeys.RENAME_FILE_FRAME_TITLE)
				+ ": " + fileName);
		fileNameField.setText(FilenameUtils.removeExtension(fileName));
		fileExtensionField.setText('.' + FilenameUtils.getExtension(fileName));
		fileNameField.selectAll();
	}

	public String getNewFileName() {
		String fileName = fileNameField.getText()
				+ fileExtensionField.getText();
		return fileName;
	}

	private JPanel getRenamePanel() {
		fileNameField = new TextFieldBuilder(10).withActionListener(
				renameFileController).withFocusListener(renameFileController).build();
		fileNameField.selectAll();

		fileExtensionField = new LabelBuilder().build();

		return new PanelBuilder()
				.withLayout(new SpringLayout())
				.add(getLabel(FormScannerTranslationKeys.RENAME_FILE_FRAME_LABEL))
				.add(fileNameField).add(fileExtensionField).withGrid(1, 3)
				.build();
	}

	private JLabel getLabel(String value) {
		return new LabelBuilder(FormScannerTranslation.getTranslationFor(value)
				+ ": ").build();
	}

	private JPanel getButtonPanel() {
		okButton = new ButtonBuilder()
				.withText(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON))
				.setEnabled(false)
				.withActionCommand(FormScannerConstants.RENAME_FILES_CURRENT)
				.withActionListener(renameFileController).build();

		cancelButton = new ButtonBuilder()
				.withText(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON))
				.withActionCommand(FormScannerConstants.RENAME_FILES_SKIP)
				.withActionListener(renameFileController).build();

		return new PanelBuilder().withLayout(new SpringLayout()).add(okButton)
				.add(cancelButton).withGrid(1, 2).build();
	}
}
