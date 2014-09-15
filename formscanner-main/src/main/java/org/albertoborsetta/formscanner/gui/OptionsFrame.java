package org.albertoborsetta.formscanner.gui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpringLayout;

import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Frame;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.ShapeType;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.gui.builder.ButtonBuilder;
import org.albertoborsetta.formscanner.gui.builder.ComboBoxBuilder;
import org.albertoborsetta.formscanner.gui.builder.LabelBuilder;
import org.albertoborsetta.formscanner.gui.builder.PanelBuilder;
import org.albertoborsetta.formscanner.gui.builder.SpinnerBuilder;
import org.albertoborsetta.formscanner.gui.controller.OptionsFrameController;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class OptionsFrame extends InternalFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OptionsFrameController optionsFrameController;
	private JSpinner thresholdValue;
	private JSpinner densityValue;
	private JButton saveButton;
	private JButton cancelButton;
	private JComboBox<ShapeType> shapeTypeComboBox;
	private JSpinner shapeSizeValue;

	/**
	 * Create the frame.
	 */
	public OptionsFrame(FormScannerModel model) {
		super(model);

		optionsFrameController = new OptionsFrameController(model);
		optionsFrameController.add(this);

		setBounds(model.getLastPosition(Frame.OPTIONS_FRAME));
		setName(Frame.OPTIONS_FRAME.name());
		setTitle(FormScannerTranslation
				.getTranslationFor(FormScannerTranslationKeys.OPTIONS_FRAME_TITLE));
		setResizable(false);

		JPanel optionsPanel = getOptionsPanel();
		JPanel shapePanel = getShapePanel();

		JPanel masterPanel = new PanelBuilder().withLayout(new SpringLayout())
				.add(optionsPanel).add(shapePanel).withGrid(2, 1).build();

		JPanel buttonPanel = getButtonPanel();
		setDefaultValues();

		getContentPane().add(masterPanel, BorderLayout.NORTH);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}

	private void setDefaultValues() {
		thresholdValue.setValue(model.getThreshold());
		densityValue.setValue(model.getDensity());
		shapeTypeComboBox.setSelectedItem(model.getShapeType());
		shapeSizeValue.setValue(model.getShapeSize());
	}

	private JPanel getOptionsPanel() {
		thresholdValue = new SpinnerBuilder(FormScannerConstants.THRESHOLD)
				.withActionListener(optionsFrameController).build();

		densityValue = new SpinnerBuilder(FormScannerConstants.DENSITY)
				.withActionListener(optionsFrameController).build();

		return new PanelBuilder()
				.withLayout(new SpringLayout())
				.add(getLabel(FormScannerTranslationKeys.THRESHOLD_OPTION_LABEL))
				.add(thresholdValue)
				.add(getLabel(FormScannerTranslationKeys.DENSITY_OPTION_LABEL))
				.add(densityValue).withGrid(2, 2)
				.withBorder(BorderFactory.createTitledBorder(
						FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.SCAN_OPTIONS)))
				.build();
	}

	private JPanel getShapePanel() {
		shapeTypeComboBox = new ComboBoxBuilder<ShapeType>(
				FormScannerConstants.SHAPE_COMBO_BOX)
				.withModel(
						new DefaultComboBoxModel<ShapeType>(ShapeType.values()))
				.withActionListener(optionsFrameController).build();

		shapeSizeValue = new SpinnerBuilder(FormScannerConstants.SHAPE_SIZE)
				.withActionListener(optionsFrameController).build();

		return new PanelBuilder()
				.withLayout(new SpringLayout())
				.add(getLabel(FormScannerTranslationKeys.SHAPE_TYPE_OPTION_LABEL))
				.add(shapeTypeComboBox)
				.add(getLabel(FormScannerTranslationKeys.SHAPE_SIZE_OPTION_LABEL))
				.add(shapeSizeValue).withGrid(2, 2)
				.withBorder(BorderFactory.createTitledBorder(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.MARKER_OPTIONS)))
				.build();
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

	public int getShapeSize() {
		return (Integer) shapeSizeValue.getValue();
	}

	public ShapeType getShape() {
		return (ShapeType) shapeTypeComboBox.getSelectedItem();
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

		if ((Integer) shapeSizeValue.getValue() < 0) {
			shapeSizeValue.setValue(0);
		}
		if ((Integer) shapeSizeValue.getValue() > 100) {
			shapeSizeValue.setValue(100);
		}

		return (((Integer) thresholdValue.getValue() != model.getThreshold())
				|| ((Integer) densityValue.getValue() != model.getDensity())
				|| ((Integer) shapeSizeValue.getValue() != model.getShapeSize()) || (!((ShapeType) shapeTypeComboBox
					.getSelectedItem()).equals(model.getShapeType())));
	}
}
