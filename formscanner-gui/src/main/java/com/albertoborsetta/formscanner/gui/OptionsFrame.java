package com.albertoborsetta.formscanner.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.albertoborsetta.formscanner.commons.FormScannerConstants;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.FontSize;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.Frame;
import com.albertoborsetta.formscanner.api.commons.Constants.CornerType;
import com.albertoborsetta.formscanner.api.commons.Constants.ShapeType;
import com.albertoborsetta.formscanner.commons.resources.FormScannerResources;
import com.albertoborsetta.formscanner.commons.resources.FormScannerResourcesKeys;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import com.albertoborsetta.formscanner.gui.builder.ButtonBuilder;
import com.albertoborsetta.formscanner.gui.builder.CheckBoxBuilder;
import com.albertoborsetta.formscanner.gui.builder.ComboBoxBuilder;
import com.albertoborsetta.formscanner.gui.builder.LabelBuilder;
import com.albertoborsetta.formscanner.gui.builder.PanelBuilder;
import com.albertoborsetta.formscanner.gui.builder.SpinnerBuilder;
import com.albertoborsetta.formscanner.gui.builder.TabbedPaneBuilder;
import com.albertoborsetta.formscanner.controller.OptionsFrameController;
import com.albertoborsetta.formscanner.model.FormScannerModel;

public class OptionsFrame extends InternalFrame implements TabbedView {

	/**
     *
     */
	private static final long serialVersionUID = 1L;
	private final OptionsFrameController optionsFrameController;
	private InternalShapeType[] types;
	private JSpinner thresholdValue;
	private JSpinner densityValue;
	private ArrayList<JButton> saveButtons = new ArrayList<>();
	private JComboBox<InternalShapeType> shapeTypeComboBox;
	private JSpinner shapeSizeValue;
	private JComboBox<String> fontTypeComboBox;
	private JComboBox<Integer> fontSizeComboBox;
	private JComboBox<InternalCornerType> cornerTypeComboBox;
	private InternalCornerType[] corners;
	private JComboBox<String> lookAndFeelComboBox;
	private JCheckBox groupsEnabled;
	private JCheckBox resetAutoNumberingQuestions;
	private JComboBox<String> groupsNameTemplate;
	private JComboBox<String> questionsNameTemplate;
	private JComboBox<String> barcodeNameTemplate;
	private JSpinner cropFromTop;
	private JSpinner cropFromBottom;
	private JSpinner cropFromLeft;
	private JSpinner cropFromRight;

	public class IconListRenderer extends DefaultListCellRenderer {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private HashMap<Object, Icon> icons = null;

		public IconListRenderer(HashMap<Object, Icon> icons) {
			this.icons = icons;
		}
		
		@Override
		public Component getListCellRendererComponent(JList<?> list, Object value,
				int index, boolean isSelected, boolean cellHasFocus) {

			JLabel label = (JLabel) super.getListCellRendererComponent(
					list, value, index, isSelected, cellHasFocus);

			Icon icon = icons.get(value);

			label.setIcon(icon);
			return label;
		}
	}

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
		setFrameIcon(FormScannerResources.getIconFor(FormScannerResourcesKeys.CONFIG_ICON_16));

		JPanel scanOptionsPanel = getScanOptionsPanel();
		JPanel templateOptionsPanel = getTemplateOptionsPanel();
		JPanel guiOptionsPanel = getGuiPanel();
		JPanel imagePreprocessingPanel = getImagePreprocessingPanel();

		setDefaultValues();

		JTabbedPane masterPanel = new TabbedPaneBuilder(
				JTabbedPane.TOP, orientation)
				.addTab(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.SCAN_OPTIONS),
						scanOptionsPanel)
				.addTab(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.TEMPLATE_OPTIONS),
						templateOptionsPanel)
				.addTab(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.GUI_OPTIONS),
						guiOptionsPanel)
				.addTab(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.IMAGE_PREPROCESSING_OPTIONS),
						imagePreprocessingPanel).build();

		getContentPane().add(masterPanel, BorderLayout.CENTER);
	}

	private JPanel getImagePreprocessingPanel() {
		JPanel buttonsPanel = getButtonPanel();
		JPanel cropPanel = getCropPanel();

		return new PanelBuilder(orientation)
				.withLayout(new BorderLayout())
				.add(cropPanel, BorderLayout.NORTH)
				.add(buttonsPanel, BorderLayout.SOUTH).build();
	}

	private JPanel getCropPanel() {
		cropFromTop = new SpinnerBuilder(
				FormScannerConstants.CROP_FROM_TOP, orientation)
				.withActionListener(optionsFrameController)
				.withActionListener(optionsFrameController).build();
		cropFromBottom = new SpinnerBuilder(
				FormScannerConstants.CROP_FROM_BOTTOM, orientation)
				.withActionListener(optionsFrameController)
				.withActionListener(optionsFrameController).build();
		cropFromLeft = new SpinnerBuilder(
				FormScannerConstants.CROP_FROM_LEFT, orientation)
				.withActionListener(optionsFrameController)
				.withActionListener(optionsFrameController).build();
		cropFromRight = new SpinnerBuilder(
				FormScannerConstants.CROP_FROM_RIGHT, orientation)
				.withActionListener(optionsFrameController)
				.withActionListener(optionsFrameController).build();

		return new PanelBuilder(orientation)
				.withLayout(new SpringLayout())
				.withBorder(
						BorderFactory.createTitledBorder(FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.CROP_OPTIONS)))
				.add(getLabel("")).add(getLabel(""))
				.add(getLabel(FormScannerTranslationKeys.CROP_FROM_TOP))
				.add(cropFromTop).add(getLabel("")).add(getLabel(""))
				.add(getLabel(FormScannerTranslationKeys.CROP_FROM_LEFT))
				.add(cropFromLeft).add(getLabel("")).add(getLabel(""))
				.add(getLabel(FormScannerTranslationKeys.CROP_FROM_RIGHT))
				.add(cropFromRight).add(getLabel("")).add(getLabel(""))
				.add(getLabel(FormScannerTranslationKeys.CROP_FROM_BOTTOM))
				.add(cropFromBottom).add(getLabel("")).add(getLabel(""))
				.withGrid(3, 6).build();
	}

	private JPanel getTemplateOptionsPanel() {
		JPanel buttonsPanel = getButtonPanel();
		JPanel groupsPanel = getGroupsPanel();
		JPanel questionPanel = getQuestionPanel();

		JPanel fullOptionsPanel = new PanelBuilder(orientation)
				.withLayout(new SpringLayout()).add(groupsPanel)
				.add(questionPanel).withGrid(2, 1).build();

		return new PanelBuilder(orientation)
				.withLayout(new BorderLayout())
				.add(fullOptionsPanel, BorderLayout.NORTH)
				.add(buttonsPanel, BorderLayout.SOUTH).build();
	}

	private JPanel getQuestionPanel() {
		resetAutoNumberingQuestions = new CheckBoxBuilder(
				FormScannerConstants.RESET_AUTO_NUMBERING, orientation)
				.withActionCommand(FormScannerConstants.RESET_AUTO_NUMBERING)
				.withActionListener(optionsFrameController).build();

		ArrayList<String> historyNameTemplatesList;
		String[] historyNameTemplates;

		historyNameTemplatesList = model
				.getHistoryNameTemplate(FormScannerConstants.QUESTION);
		historyNameTemplates = new String[historyNameTemplatesList.size()];
		historyNameTemplatesList.toArray(historyNameTemplates);

		questionsNameTemplate = new ComboBoxBuilder<String>(
				FormScannerConstants.QUESTION_NAME_TEMPLATE, orientation)
				.withModel(new DefaultComboBoxModel<>(historyNameTemplates))
				.setEditable(true).withActionListener(optionsFrameController)
				.withActionCommand(FormScannerConstants.QUESTION).build();

		historyNameTemplatesList = model
				.getHistoryNameTemplate(FormScannerConstants.BARCODE);
		historyNameTemplates = new String[historyNameTemplatesList.size()];
		historyNameTemplatesList.toArray(historyNameTemplates);

		barcodeNameTemplate = new ComboBoxBuilder<String>(
				FormScannerConstants.BARCODE_NAME_TEMPLATE, orientation)
				.withModel(new DefaultComboBoxModel<>(historyNameTemplates))
				.setEditable(true).withActionListener(optionsFrameController)
				.withActionCommand(FormScannerConstants.BARCODE).build();

		return new PanelBuilder(orientation)
				.withLayout(new SpringLayout())
				.withBorder(
						BorderFactory.createTitledBorder(FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.QUESTION_OPTIONS)))
				.add(
						getLabel(FormScannerTranslationKeys.RESET_AUTO_NUMBERING_OPTION_LABEL))
				.add(resetAutoNumberingQuestions)
				.add(
						getLabel(FormScannerTranslationKeys.QUESTIONS_NAME_TEMPLATE_LABEL))
				.add(questionsNameTemplate)
				.add(
						getLabel(FormScannerTranslationKeys.BARCODE_NAME_TEMPLATE_LABEL))
				.add(barcodeNameTemplate).withGrid(3, 2).build();
	}

	private JPanel getGroupsPanel() {
		groupsEnabled = new CheckBoxBuilder(
				FormScannerConstants.GROUPS_ENABLED, orientation)
				.withActionCommand(FormScannerConstants.GROUPS_ENABLED)
				.withActionListener(optionsFrameController).build();

		ArrayList<String> historyNameTemplatesList;
		String[] historyNameTemplates;
		historyNameTemplatesList = model
				.getHistoryNameTemplate(FormScannerConstants.GROUP);
		historyNameTemplates = new String[historyNameTemplatesList.size()];
		historyNameTemplatesList.toArray(historyNameTemplates);

		groupsNameTemplate = new ComboBoxBuilder<String>(
				FormScannerConstants.GROUP_NAME_TEMPLATE, orientation)
				.withModel(new DefaultComboBoxModel<>(historyNameTemplates))
				.setEditable(true).withActionListener(optionsFrameController)
				.withActionCommand(FormScannerConstants.GROUP).build();

		return new PanelBuilder(orientation)
				.withLayout(new SpringLayout())
				.withBorder(
						BorderFactory.createTitledBorder(FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.GROUPS_OPTIONS)))
				.add(
						getLabel(FormScannerTranslationKeys.ENABLE_GROUPS_OPTION_LABEL))
				.add(groupsEnabled)
				.add(
						getLabel(FormScannerTranslationKeys.GROUPS_NAME_TEMPLATE_LABEL))
				.add(groupsNameTemplate).withGrid(2, 2).build();
	}

	private JPanel getScanOptionsPanel() {
		JPanel buttonsPanel = getButtonPanel();
		JPanel optionsPanel = getOptionsPanel();
		JPanel shapePanel = getShapePanel();
		JPanel cornerPanel = getCornerPanel();

		JPanel fullOptionsPanel = new PanelBuilder(orientation)
				.withLayout(new SpringLayout()).add(optionsPanel)
				.add(shapePanel).add(cornerPanel).withGrid(3, 1).build();

		return new PanelBuilder(orientation)
				.withLayout(new BorderLayout())
				.add(fullOptionsPanel, BorderLayout.NORTH)
				.add(buttonsPanel, BorderLayout.SOUTH).build();
	}

	private JPanel getCornerPanel() {
		CornerType cornerTypes[] = CornerType.values();
		corners = new InternalCornerType[cornerTypes.length];

		HashMap<Object, Icon> icons = new HashMap<>();
		for (CornerType corner : cornerTypes) {
			InternalCornerType internalCornerType = new InternalCornerType(
					corner);
			corners[corner.getIndex()] = internalCornerType;
			icons.put(
					internalCornerType,
					FormScannerResources.getIconFor(corner.getName()));
		}

		cornerTypeComboBox = new ComboBoxBuilder<InternalCornerType>(
				FormScannerConstants.CORNER_TYPE_COMBO_BOX, orientation)
				.withModel(new DefaultComboBoxModel<>(corners))
				.withRenderer(new IconListRenderer(icons))
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
		JPanel buttonsPanel = getButtonPanel();

		GraphicsEnvironment e = GraphicsEnvironment
				.getLocalGraphicsEnvironment();
		String[] fonts;
		fonts = e.getAvailableFontFamilyNames(model.getLocale());

		LookAndFeelInfo[] lookAndFeelInfo = UIManager
				.getInstalledLookAndFeels();
		String[] looks = new String[lookAndFeelInfo.length];
		for (int i = 0; i < lookAndFeelInfo.length; i++) {
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
		lookAndFeelComboBox = new ComboBoxBuilder<String>(
				FormScannerConstants.LOOK_AND_FEEL_COMBO_BOX, orientation)
				.withModel(new DefaultComboBoxModel<>(looks))
				.withActionListener(optionsFrameController).build();

		JPanel fontOptionsPanel = new PanelBuilder(orientation)
				.withLayout(new SpringLayout())
				.withBorder(
						BorderFactory.createTitledBorder(FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.FONT_OPTIONS)))
				.add(
						getLabel(FormScannerTranslationKeys.FONT_TYPE_OPTION_LABEL))
				.add(fontTypeComboBox)
				.add(
						getLabel(FormScannerTranslationKeys.FONT_SIZE_OPTION_LABEL))
				.add(fontSizeComboBox).withGrid(2, 2).build();

		JPanel lafOptionsPanel = new PanelBuilder(orientation)
				.withLayout(new SpringLayout())
				.withBorder(
						BorderFactory.createTitledBorder(FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.LOOK_AND_FEEL_OPTIONS)))
				.add(
						getLabel(FormScannerTranslationKeys.PREFERRED_LOOK_AND_FEEL_OPTION_LABEL))
				.add(lookAndFeelComboBox).withGrid(1, 2).build();

		JPanel optionsPanel = new PanelBuilder(orientation)
				.withLayout(new SpringLayout()).add(fontOptionsPanel)
				.add(lafOptionsPanel)
				.add(getLabel(FormScannerTranslationKeys.REQUIRES_RESTART))
				.withGrid(3, 1).build();

		return new PanelBuilder(orientation)
				.withLayout(new BorderLayout())
				.add(optionsPanel, BorderLayout.NORTH)
				.add(buttonsPanel, BorderLayout.SOUTH).build();
	}

	private void setDefaultValues() {
		thresholdValue.setValue(model.getThreshold());
		densityValue.setValue(model.getDensity());
		shapeTypeComboBox.setSelectedIndex(model.getShapeType().getIndex());
		shapeSizeValue.setValue(model.getShapeSize());
		fontTypeComboBox.setSelectedItem(model.getFontType());
		fontSizeComboBox.setSelectedItem(model.getFontSize());
		cornerTypeComboBox.setSelectedIndex(model.getCornerType().getIndex());
		lookAndFeelComboBox.setSelectedItem(model.getLookAndFeel());
		groupsEnabled.setSelected(model.isGroupsEnabled());
		groupsNameTemplate.setEnabled(model.isGroupsEnabled());
		resetAutoNumberingQuestions.setSelected(model
				.isResetAutoNumberingQuestions());
		resetAutoNumberingQuestions.setEnabled(model.isGroupsEnabled());

		HashMap<String, Integer> crop = model.getCrop();
		cropFromTop.setValue(crop.get(FormScannerConstants.TOP));
		cropFromBottom.setValue(crop.get(FormScannerConstants.BOTTOM));
		cropFromLeft.setValue(crop.get(FormScannerConstants.LEFT));
		cropFromRight.setValue(crop.get(FormScannerConstants.RIGHT));
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
								.getTranslationFor(FormScannerTranslationKeys.ACCURACY_OPTIONS)))
				.add(
						getLabel(FormScannerTranslationKeys.THRESHOLD_OPTION_LABEL))
				.add(thresholdValue)
				.add(getLabel(FormScannerTranslationKeys.DENSITY_OPTION_LABEL))
				.add(densityValue).withGrid(2, 2).build();
	}

	private JPanel getShapePanel() {
		
		ShapeType shapes[] = ShapeType.values();
		types = new InternalShapeType[shapes.length];

		HashMap<Object, Icon> icons = new HashMap<>();
		for (ShapeType shape : shapes) {
			InternalShapeType internalShapeType = new InternalShapeType(shape);
			types[shape.getIndex()] = internalShapeType;
			icons.put(
					internalShapeType,
					FormScannerResources.getIconFor(shape.getName()));
		}

		shapeTypeComboBox = new ComboBoxBuilder<InternalShapeType>(
				FormScannerConstants.SHAPE_COMBO_BOX, orientation)
				.withModel(new DefaultComboBoxModel<>(types))
				.withRenderer(new IconListRenderer(icons))
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
		JButton saveButton = new ButtonBuilder(orientation)
				.withText(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.SAVE_OPTIONS_BUTTON))
				.withToolTip(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.SAVE_OPTIONS_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.SAVE_OPTIONS)
				.withActionListener(optionsFrameController).setEnabled(false)
				.build();

		JButton cancelButton = new ButtonBuilder(orientation)
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

		saveButtons.add(saveButton);

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

	public boolean isGroupsEnabled() {
		return groupsEnabled.isSelected();
	}

	public boolean isResetAutoNumberingQuestions() {
		return resetAutoNumberingQuestions.isSelected();
	}

	private void verifySpinnerValues() {
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
		if ((Integer) cropFromTop.getValue() < 0) {
			shapeSizeValue.setValue(0);
		}
		if ((Integer) cropFromLeft.getValue() < 0) {
			shapeSizeValue.setValue(0);
		}
		if ((Integer) cropFromRight.getValue() < 0) {
			shapeSizeValue.setValue(0);
		}
		if ((Integer) cropFromBottom.getValue() < 0) {
			shapeSizeValue.setValue(0);
		}
	}

	public CornerType getCornerType() {
		return (corners[cornerTypeComboBox.getSelectedIndex()]).getType();
	}

	@Override
	public void setupNextTab(String action) {
		// TODO Auto-generated method stub
	}

	@Override
	public void setAdvanceable() {
		for (JButton button : saveButtons) {
			button.setEnabled(isAdvanceable());
		}
	}

	@Override
	public boolean isAdvanceable(int tab) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAdvanceable() {
		verifySpinnerValues();

		return (verifyScanPanel() || verifyGuiPanel() || verifyTemplatePanel());
	}

	private boolean verifyTemplatePanel() {
		return ((groupsEnabled.isSelected() ^ model.isGroupsEnabled()) || (resetAutoNumberingQuestions
				.isEnabled()) ^ model.isResetAutoNumberingQuestions() || model
				.getHistoryNameTemplate(FormScannerConstants.GROUP)
				.containsAll(getHistoryNameTemplate(FormScannerConstants.GROUP)) || model
				.getHistoryNameTemplate(FormScannerConstants.QUESTION)
				.containsAll(
						getHistoryNameTemplate(FormScannerConstants.QUESTION)) || model
				.getHistoryNameTemplate(FormScannerConstants.BARCODE)
				.containsAll(
						getHistoryNameTemplate(FormScannerConstants.BARCODE)));
	}

	private boolean verifyGuiPanel() {
		return (!((String) fontTypeComboBox.getSelectedItem()).equals(model
				.getFontType())) || ((Integer) fontSizeComboBox
				.getSelectedItem() != model.getFontSize()) || (!((String) lookAndFeelComboBox
				.getSelectedItem()).equals(model.getLookAndFeel()));
	}

	private boolean verifyScanPanel() {
		return ((Integer) thresholdValue.getValue() != model.getThreshold()) || ((Integer) densityValue
				.getValue() != model.getDensity()) || ((Integer) shapeSizeValue
				.getValue() != model.getShapeSize()) || (shapeTypeComboBox
				.getSelectedIndex() != model.getShapeType().getIndex()) || (cornerTypeComboBox
				.getSelectedIndex() != model.getCornerType().getIndex());
	}

	public ArrayList<String> getHistoryNameTemplate(String type) {
		ArrayList<String> items = new ArrayList<>();
		switch (type) {
		case FormScannerConstants.BARCODE:
			for (int i = 0; i < barcodeNameTemplate.getItemCount(); i++) {
				items.add(barcodeNameTemplate.getItemAt(i));
			}
			break;
		case FormScannerConstants.GROUP:
			for (int i = 0; i < groupsNameTemplate.getItemCount(); i++) {
				items.add(groupsNameTemplate.getItemAt(i));
			}
			break;
		case FormScannerConstants.QUESTION:
		default:
			for (int i = 0; i < questionsNameTemplate.getItemCount(); i++) {
				items.add(questionsNameTemplate.getItemAt(i));
			}
			break;
		}
		return items;
	}

	public void enableGroups() {
		resetAutoNumberingQuestions.setEnabled(groupsEnabled.isSelected());
		groupsNameTemplate.setEnabled(groupsEnabled.isSelected());
	}

	public void addItem(String type) {
		switch (type) {
		case FormScannerConstants.BARCODE:
			barcodeNameTemplate.removeActionListener(optionsFrameController);
			barcodeNameTemplate.insertItemAt(
					(String) barcodeNameTemplate.getSelectedItem(), 0);
			barcodeNameTemplate.addActionListener(optionsFrameController);
			break;
		case FormScannerConstants.GROUP:
			groupsNameTemplate.removeActionListener(optionsFrameController);
			groupsNameTemplate.insertItemAt(
					(String) groupsNameTemplate.getSelectedItem(), 0);
			groupsNameTemplate.addActionListener(optionsFrameController);
			break;
		case FormScannerConstants.QUESTION:
		default:
			questionsNameTemplate.removeActionListener(optionsFrameController);
			questionsNameTemplate.insertItemAt(
					(String) questionsNameTemplate.getSelectedItem(), 0);
			questionsNameTemplate.addActionListener(optionsFrameController);
			break;
		}
	}

	public HashMap<String, Integer> getCrop() {
		HashMap<String, Integer> crop = new HashMap<>();
		crop.put(FormScannerConstants.TOP, (Integer) cropFromTop.getValue());
		crop.put(FormScannerConstants.LEFT, (Integer) cropFromLeft.getValue());
		crop
				.put(
						FormScannerConstants.RIGHT,
						(Integer) cropFromRight.getValue());
		crop.put(
				FormScannerConstants.BOTTOM,
				(Integer) cropFromBottom.getValue());
		return crop;
	}
}
