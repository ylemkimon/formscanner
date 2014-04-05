package org.albertoborsetta.formscanner.gui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpringLayout;

import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.gui.builder.ButtonBuilder;
import org.albertoborsetta.formscanner.gui.builder.LabelBuilder;
import org.albertoborsetta.formscanner.gui.builder.PanelBuilder;
import org.albertoborsetta.formscanner.gui.builder.SpinnerBuilder;
import org.albertoborsetta.formscanner.gui.controller.OptionsFrameController;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class OptionsFrame extends JInternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FormScannerModel model;
	private OptionsFrameController optionsFrameController;
	private JSpinner thresholdValue;
	private JSpinner densityValue;
	private JButton saveButton;
	private JButton cancelButton;

	/**
	 * Create the frame.
	 */
	public OptionsFrame(FormScannerModel model) {
		this.model = model;

		optionsFrameController = new OptionsFrameController(model);
		optionsFrameController.add(this);

		setName(FormScannerConstants.OPTIONS_FRAME_NAME);
		setTitle(FormScannerTranslation
				.getTranslationFor(FormScannerTranslationKeys.OPTIONS_FRAME_TITLE));
		setBounds(100, 100, 300, 300);
		setResizable(false);

		JPanel optionsPanel = getOptionsPanel();
		JPanel buttonPanel = getButtonPanel();
		setDefaultValues();

		getContentPane().add(optionsPanel, BorderLayout.NORTH);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}

	private void setDefaultValues() {
		thresholdValue.setValue(model.getThreshold());
		densityValue.setValue(model.getDensity());
	}

	private JPanel getOptionsPanel() {
		thresholdValue = new SpinnerBuilder(FormScannerConstants.THRESHOLD)
				.withActionListener(optionsFrameController)
				.build();

		densityValue = new SpinnerBuilder(FormScannerConstants.DENSITY)
				.withActionListener(optionsFrameController)
				.build();
		
		return new PanelBuilder()
				.withLayout(new SpringLayout())
				.add(getLabel(FormScannerTranslationKeys.THRESHOLD_OPTION_LABEL))
				.add(thresholdValue)
				.add(getLabel(FormScannerTranslationKeys.DENSITY_OPTION_LABEL))
				.add(densityValue).withGrid(2, 2).build();
	}

	private JLabel getLabel(String value) {
		return new LabelBuilder(FormScannerTranslation.getTranslationFor(value))
				.withBorder(BorderFactory.createEmptyBorder()).build();
	}

	private JPanel getButtonPanel() {
		saveButton = new ButtonBuilder()
				.withText(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.SAVE_OPTIONS_BUTTON))
				.withToolTip(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.SAVE_OPTIONS_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.SAVE_OPTIONS)
				.withActionListener(optionsFrameController).setEnabled(false)
				.build();

		cancelButton = new ButtonBuilder()
				.withText(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON))
				.withToolTip(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.CANCEL)
				.withActionListener(optionsFrameController).build();

		return new PanelBuilder().withLayout(new SpringLayout())
				.add(saveButton).add(cancelButton).withGrid(1, 2).build();
	}

	public int getThresholdValue() {
		return (Integer) thresholdValue.getValue();
	}

	public int getDensityValue() {
		return (Integer) densityValue.getValue();
	}

	public void setSaveEnabled() {
		saveButton.setEnabled(verifySpinnerValues());
	}

	private boolean verifySpinnerValues() {
		if ((Integer) thresholdValue.getValue() < 0) {
			thresholdValue.setValue(0);
		}
		if ((Integer) thresholdValue.getValue() > 255) {
			thresholdValue.setValue(255);
		}
		
		if ((Integer) densityValue.getValue() < 0) {
			densityValue.setValue(0);
		}
		if ((Integer) densityValue.getValue() > 100) {
			densityValue.setValue(100);
		}
		
		return (((Integer) thresholdValue.getValue() != model.getThreshold())
				|| ((Integer) densityValue.getValue() != model.getDensity()));
	}
}