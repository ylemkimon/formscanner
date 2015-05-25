package com.albertoborsetta.formscanner.gui;

import java.awt.BorderLayout;
import java.awt.GraphicsEnvironment;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.albertoborsetta.formscanner.commons.FormScannerConstants;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.FontSize;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.Frame;
import com.albertoborsetta.formscanner.api.commons.Constants.CornerType;
import com.albertoborsetta.formscanner.api.commons.Constants.ShapeType;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import com.albertoborsetta.formscanner.gui.builder.ButtonBuilder;
import com.albertoborsetta.formscanner.gui.builder.ComboBoxBuilder;
import com.albertoborsetta.formscanner.gui.builder.LabelBuilder;
import com.albertoborsetta.formscanner.gui.builder.PanelBuilder;
import com.albertoborsetta.formscanner.gui.builder.SpinnerBuilder;
import com.albertoborsetta.formscanner.controller.OptionsFrameController;
import com.albertoborsetta.formscanner.model.FormScannerModel;
import javax.swing.LookAndFeel;

public class OptionsFrame extends InternalFrame {

	/**
     *
     */
	private static final long serialVersionUID = 1L;
	private final OptionsFrameController optionsFrameController;
	private InternalShapeType[] types;
	private JSpinner thresholdValue;
	private JSpinner densityValue;
	private JButton saveButton;
	private JButton cancelButton;
	private JComboBox<InternalShapeType> shapeTypeComboBox;
	private JSpinner shapeSizeValue;
	private JComboBox<String> fontTypeComboBox;
	private JComboBox<Integer> fontSizeComboBox;
	private JComboBox<InternalCornerType> cornerTypeComboBox;
	private InternalCornerType[] corners;
	private JComboBox<String> lookAndFeelComboBox;

	private class InternalShapeType {

		private final ShapeType type;

		protected InternalShapeType(ShapeType type) {
			this.type = type;
		}

		protected ShapeType getType() {
			return type;
		}

		@Override
		public String toString() {
			return FormScannerTranslation.getTranslationFor(type.getValue());
		}
	}

	private class InternalCornerType {

		private final CornerType type;

		protected InternalCornerType(CornerType type) {
			this.type = type;
		}

		protected CornerType getType() {
			return type;
		}

		@Override
		public String toString() {
			return FormScannerTranslation.getTranslationFor(type.getValue());
		}
	}

	/**
	 * Create the frame.
	 *
	 * @param model
	 */
	public OptionsFrame(FormScannerModel model) {
		super(model);

		optionsFrameController = new OptionsFrameController(model);
		optionsFrameController.add(this);

		setBounds(model.getLastPosition(Frame.OPTIONS_FRAME));
		setName(Frame.OPTIONS_FRAME.name());
		setTitle(FormScannerTranslation
				.getTranslationFor(FormScannerTranslationKeys.OPTIONS_FRAME_TITLE));
		setResizable(true);

		JPanel optionsPanel = getOptionsPanel();
		JPanel shapePanel = getShapePanel();
		JPanel cornerPanel = getCornerPanel();
		JPanel guiPanel = getGuiPanel();

		JPanel masterPanel = new PanelBuilder(orientation)
				.withLayout(new SpringLayout()).add(optionsPanel)
				.add(shapePanel).add(cornerPanel).add(guiPanel).withGrid(4, 1)
				.build();

		JPanel buttonPanel = getButtonPanel();
		setDefaultValues();

		getContentPane().add(masterPanel, BorderLayout.NORTH);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);
	}

	private JPanel getCornerPanel() {
		CornerType cornerTypes[] = CornerType.values();
		corners = new InternalCornerType[cornerTypes.length];

		for (CornerType corner : cornerTypes) {
			corners[corner.getIndex()] = new InternalCornerType(corner);
		}

		cornerTypeComboBox = new ComboBoxBuilder<InternalCornerType>(
				FormScannerConstants.CORNER_TYPE_COMBO_BOX, orientation)
				.withModel(new DefaultComboBoxModel<>(corners))
				.withActionListener(optionsFrameController).build();

		return new PanelBuilder(orientation)
				.withLayout(new SpringLayout())
				.withBorder(
						BorderFactory.createTitledBorder(FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.CORNERS_OPTIONS)))
				.add(
						getLabel(FormScannerTranslationKeys.CORNER_TYPE_OPTION_LABEL))
				.add(cornerTypeComboBox).withGrid(1, 2).build();
	}

	private JPanel getGuiPanel() {

		GraphicsEnvironment e = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		String[] fonts;
		fonts = e.getAvailableFontFamilyNames(model.getLocale());
		
                LookAndFeelInfo[] lookAndFeelInfo = UIManager.getInstalledLookAndFeels();
		String[] looks = new String[lookAndFeelInfo.length];
                for (int i=0; i< lookAndFeelInfo.length; i++) {
                    looks[i] = lookAndFeelInfo[i].getName();
                }

		FontSize[] fontSizes = FontSize.values();
		Integer[] sizes = new Integer[fontSizes.length];
		for (int i = 0; i < fontSizes.length; i++) {
			sizes[i] = fontSizes[i].getValue();
		}

		fontTypeComboBox = new ComboBoxBuilder<String>(
				FormScannerConstants.FONT_TYPE_COMBO_BOX, orientation)
				.withModel(new DefaultComboBoxModel<>(fonts))
				.withActionListener(optionsFrameController).build();
		fontSizeComboBox = new ComboBoxBuilder<Integer>(
				FormScannerConstants.FONT_SIZE_COMBO_BOX, orientation)
				.withModel(new DefaultComboBoxModel<>(sizes))
				.withActionListener(optionsFrameController).build();
		lookAndFeelComboBox = new ComboBoxBuilder<String>(FormScannerConstants.LOOK_AND_FEEL_COMBO_BOX, orientation)
				.withModel(new DefaultComboBoxModel<>(looks))
				.withActionListener(optionsFrameController).build();

		return new PanelBuilder(orientation)
				.withLayout(new SpringLayout())
				.withBorder(
						BorderFactory.createTitledBorder(FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.GUI_OPTIONS)))
				.add(
						getLabel(FormScannerTranslationKeys.FONT_TYPE_OPTION_LABEL))
				.add(fontTypeComboBox)
				.add(
						getLabel(FormScannerTranslationKeys.FONT_SIZE_OPTION_LABEL))
				.add(fontSizeComboBox)
				.add(
						getLabel(FormScannerTranslationKeys.LOOK_AND_FEEL_OPTION_LABEL))
				.add(lookAndFeelComboBox)
				.withGrid(3, 2).build();
	}

	private void setDefaultValues() {
		thresholdValue.setValue(model.getThreshold());
		densityValue.setValue(model.getDensity());
		shapeTypeComboBox.setSelectedIndex(model.getShapeType().getIndex());
		shapeSizeValue.setValue(model.getShapeSize());
		fontTypeComboBox.setSelectedItem(model.getFontType());
		fontSizeComboBox.setSelectedItem(model.getFontSize());
		cornerTypeComboBox.setSelectedItem(model.getCornerType());
		lookAndFeelComboBox.setSelectedItem(model.getLookAndFeel());
	}

	private JPanel getOptionsPanel() {
		thresholdValue = new SpinnerBuilder(
				FormScannerConstants.THRESHOLD, orientation)
				.withActionListener(optionsFrameController)
				.withActionListener(optionsFrameController).build();

		densityValue = new SpinnerBuilder(
				FormScannerConstants.DENSITY, orientation)
				.withActionListener(optionsFrameController)
				.withActionListener(optionsFrameController).build();

		return new PanelBuilder(orientation)
				.withLayout(new SpringLayout())
				.withBorder(
						BorderFactory.createTitledBorder(FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.SCAN_OPTIONS)))
				.add(
						getLabel(FormScannerTranslationKeys.THRESHOLD_OPTION_LABEL))
				.add(thresholdValue)
				.add(getLabel(FormScannerTranslationKeys.DENSITY_OPTION_LABEL))
				.add(densityValue).withGrid(2, 2).build();
	}

	private JPanel getShapePanel() {

		ShapeType shapes[] = ShapeType.values();
		types = new InternalShapeType[shapes.length];

		for (ShapeType shape : shapes) {
			types[shape.getIndex()] = new InternalShapeType(shape);
		}

		shapeTypeComboBox = new ComboBoxBuilder<InternalShapeType>(
				FormScannerConstants.SHAPE_COMBO_BOX, orientation)
				.withModel(new DefaultComboBoxModel<>(types))
				.withActionListener(optionsFrameController).build();

		shapeSizeValue = new SpinnerBuilder(
				FormScannerConstants.SHAPE_SIZE, orientation)
				.withActionListener(optionsFrameController)
				.withActionListener(optionsFrameController).build();

		return new PanelBuilder(orientation)
				.withLayout(new SpringLayout())
				.withBorder(
						BorderFactory.createTitledBorder(FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.MARKER_OPTIONS)))
				.add(
						getLabel(FormScannerTranslationKeys.SHAPE_TYPE_OPTION_LABEL))
				.add(shapeTypeComboBox)
				.add(
						getLabel(FormScannerTranslationKeys.SHAPE_SIZE_OPTION_LABEL))
				.add(shapeSizeValue).withGrid(2, 2).build();
	}

	private JLabel getLabel(String value) {
		return new LabelBuilder(
				FormScannerTranslation.getTranslationFor(value), orientation)
				.withBorder(BorderFactory.createEmptyBorder()).build();
	}

	private JPanel getButtonPanel() {
		saveButton = new ButtonBuilder(orientation)
				.withText(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.SAVE_OPTIONS_BUTTON))
				.withToolTip(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.SAVE_OPTIONS_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.SAVE_OPTIONS)
				.withActionListener(optionsFrameController).setEnabled(false)
				.build();

		cancelButton = new ButtonBuilder(orientation)
				.withText(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON))
				.withToolTip(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.CANCEL_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.CANCEL)
				.withActionListener(optionsFrameController).build();

		JPanel innerPanel = new PanelBuilder(orientation)
				.withLayout(new SpringLayout()).add(saveButton)
				.add(cancelButton).withGrid(1, 2).build();

		return new PanelBuilder(orientation)
				.withLayout(new BorderLayout())
				.add(innerPanel, BorderLayout.EAST).build();
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
		return (types[shapeTypeComboBox.getSelectedIndex()]).getType();
	}

	public String getFontType() {
		return (String) fontTypeComboBox.getSelectedItem();
	}

	public Integer getFontSize() {
		return (Integer) fontSizeComboBox.getSelectedItem();
	}
	
	public String getLookAndFeel() {
		return (String) lookAndFeelComboBox.getSelectedItem();
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

		return (((Integer) thresholdValue.getValue() != model.getThreshold()) || ((Integer) densityValue
				.getValue() != model.getDensity()) || ((Integer) shapeSizeValue
				.getValue() != model.getShapeSize()) || (shapeTypeComboBox
				.getSelectedIndex() != model.getShapeType().getIndex()) || (!((String) fontTypeComboBox
				.getSelectedItem()).equals(model.getFontType())) || ((Integer) fontSizeComboBox
					.getSelectedItem() != model.getFontSize()));
	}

	public CornerType getCornerType() {
		return (corners[cornerTypeComboBox.getSelectedIndex()]).getType();
	}
}
