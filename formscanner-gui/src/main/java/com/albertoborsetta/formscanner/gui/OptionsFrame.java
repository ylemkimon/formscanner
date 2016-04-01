package com.albertoborsetta.formscanner.gui;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import com.albertoborsetta.formscanner.commons.FormScannerConstants;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.FontSize;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.Frame;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import com.albertoborsetta.formscanner.gui.builder.ButtonBuilder;
import com.albertoborsetta.formscanner.gui.builder.CheckBoxBuilder;
import com.albertoborsetta.formscanner.gui.builder.ComboBoxBuilder;
import com.albertoborsetta.formscanner.gui.builder.LabelBuilder;
import com.albertoborsetta.formscanner.gui.builder.PanelBuilder;
import com.albertoborsetta.formscanner.gui.builder.TabbedPaneBuilder;
import com.albertoborsetta.formscanner.controller.InternalFrameController;
import com.albertoborsetta.formscanner.controller.OptionsFrameController;
import com.albertoborsetta.formscanner.model.FormScannerModel;

public class OptionsFrame extends JFrame implements WizardTabView {

	/**
     *
     */
	private static final long serialVersionUID = 1L;
	private final OptionsFrameController optionsFrameController;
	private ArrayList<JButton> saveButtons = new ArrayList<>();
	private JComboBox<String> fontTypeComboBox;
	private JComboBox<Integer> fontSizeComboBox;
	private JComboBox<String> lookAndFeelComboBox;
	private JCheckBox groupsEnabled;
	private JCheckBox resetAutoNumberingQuestions;
	private JComboBox<String> groupsNameTemplate;
	private JComboBox<String> questionsNameTemplate;
	private JComboBox<String> barcodeNameTemplate;
	
	protected FormScannerModel model;
	protected InternalFrameController internalFrameController;
	protected ComponentOrientation orientation;

	/**
	 * Create the frame.
	 *
	 * @param model
	 */
	public OptionsFrame(FormScannerModel model) {
		this.model = model;
		orientation = model.getOrientation();
		internalFrameController = InternalFrameController.getInstance(model);
		addWindowListener(internalFrameController);

		optionsFrameController = OptionsFrameController.getInstance(model);
		optionsFrameController.add(this);

		setBounds(model.getLastPosition(Frame.OPTIONS_FRAME));
		setName(Frame.OPTIONS_FRAME.name());
		setTitle(FormScannerTranslation
				.getTranslationFor(FormScannerTranslationKeys.OPTIONS_FRAME_TITLE));
		setResizable(true);

		JPanel templateOptionsPanel = getTemplateOptionsPanel();
		JPanel guiOptionsPanel = getGuiPanel();

		setDefaultValues();

		JTabbedPane masterPanel = new TabbedPaneBuilder(
				JTabbedPane.TOP, orientation)
				.addTab(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.TEMPLATE_OPTIONS),
						templateOptionsPanel)
				.addTab(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.GUI_OPTIONS),
						guiOptionsPanel)
				.build();

		getContentPane().add(masterPanel, BorderLayout.CENTER);
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
		fontTypeComboBox.setSelectedItem(model.getFontType());
		fontSizeComboBox.setSelectedItem(model.getFontSize());
		lookAndFeelComboBox.setSelectedItem(model.getLookAndFeel());
		groupsEnabled.setSelected(model.isGroupsEnabled());
		groupsNameTemplate.setEnabled(model.isGroupsEnabled());
		resetAutoNumberingQuestions.setSelected(model
				.isResetAutoNumberingQuestions());
		resetAutoNumberingQuestions.setEnabled(model.isGroupsEnabled());
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
		return (verifyGuiPanel() || verifyTemplatePanel());
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
}
