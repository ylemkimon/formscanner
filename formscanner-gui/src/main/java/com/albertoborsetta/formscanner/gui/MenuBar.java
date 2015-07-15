package com.albertoborsetta.formscanner.gui;

import java.awt.ComponentOrientation;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import com.albertoborsetta.formscanner.commons.FormScannerConstants;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.Language;
import com.albertoborsetta.formscanner.commons.resources.FormScannerResources;
import com.albertoborsetta.formscanner.commons.resources.FormScannerResourcesKeys;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import com.albertoborsetta.formscanner.gui.builder.MenuBuilder;
import com.albertoborsetta.formscanner.gui.builder.MenuItemBuilder;
import com.albertoborsetta.formscanner.controller.FormScannerController;
import com.albertoborsetta.formscanner.model.FormScannerModel;

public final class MenuBar extends JMenuBar implements MenuView {

	private static final long serialVersionUID = 1L;

	private final FormScannerModel model;
	private final FormScannerController formScannerController;

	private JMenuItem openMenuItem;
	private JMenuItem exitMenuItem;
	private JMenuItem renameMenuItem;
	private JMenuItem languageMenuItem;
	private JMenuItem optionsMenuItem;
	private JMenuItem helpMenuItem;
	private JMenuItem aboutMenuItem;
	private JMenuItem createTemplateMenuItem;
	private JMenuItem loadTemplateMenuItem;
	private JMenuItem scanAllMenuItem;
	private JMenuItem scanMenuItem;
	private final ComponentOrientation orientation;

	public MenuBar(final FormScannerModel model) {
		this.model = model;
		orientation = model.getOrientation();
		formScannerController = FormScannerController.getInstance(model);

		setComponentOrientation(model.getOrientation());

		JMenu fileMenu = getFileMenu();
		JMenu editMenu = getEditMenu();
		JMenu templateMenu = getTemplateMenu();
		JMenu helpMenu = getHelpMenu();
		JMenu settingsMenu = getSettingsMenu();
		add(fileMenu);
		add(editMenu);
		add(templateMenu);
		add(settingsMenu);
		add(helpMenu);
	}

	private JMenu getFileMenu() {

		openMenuItem = new MenuItemBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.OPEN_IMAGES),
				orientation)
				.withActionCommand(FormScannerConstants.OPEN_IMAGES)
				.withActionListener(formScannerController)
				.withAccelerator(
						KeyStroke.getKeyStroke(
								KeyEvent.VK_O, InputEvent.CTRL_MASK))
				.withMnemonic(
						FormScannerTranslation
								.getMnemonicFor(FormScannerTranslationKeys.OPEN_IMAGES_MNEMONIC))
				.withIcon(
						FormScannerResources
								.getIconFor(FormScannerResourcesKeys.OPEN_IMAGES_ICON_16))
				.build();

		exitMenuItem = new MenuItemBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.EXIT),
				orientation)
				.withActionCommand(FormScannerConstants.EXIT)
				.withActionListener(formScannerController)
				.withMnemonic(
						FormScannerTranslation
								.getMnemonicFor(FormScannerTranslationKeys.EXIT_MNEMONIC))
				.withAccelerator(
						KeyStroke.getKeyStroke(
								KeyEvent.VK_X, InputEvent.CTRL_MASK))
				.withIcon(
						FormScannerResources
								.getIconFor(FormScannerResourcesKeys.EXIT_ICON_16))
				.build();

		return new MenuBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.FILE_MENU),
				orientation)
				.withMnemonic(
						FormScannerTranslation
								.getMnemonicFor(FormScannerTranslationKeys.FILE_MENU_MNEMONIC))
				.add(openMenuItem).add(new JSeparator(JSeparator.HORIZONTAL))
				.add(exitMenuItem).build();
	}

	public JMenu getEditMenu() {

		renameMenuItem = new MenuItemBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.RENAME_FILES),
				orientation)
				.withActionCommand(FormScannerConstants.RENAME_FILES_FIRST)
				.withActionListener(formScannerController)
				.withMnemonic(
						FormScannerTranslation
								.getMnemonicFor(FormScannerTranslationKeys.RENAME_FILES_MNEMONIC))
				.withAccelerator(
						KeyStroke.getKeyStroke(
								KeyEvent.VK_R, InputEvent.CTRL_MASK))
				.withIcon(
						FormScannerResources
								.getIconFor(FormScannerResourcesKeys.RENAME_FILES_ICON_16))
				.setEnabled(false).build();

		scanAllMenuItem = new MenuItemBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.ANALYZE_FILES_ALL),
				orientation)
				.withActionCommand(FormScannerConstants.ANALYZE_FILES_ALL)
				.withActionListener(formScannerController)
				.withMnemonic(
						FormScannerTranslation
								.getMnemonicFor(FormScannerTranslationKeys.ANALYZE_FILES_ALL_MNEMONIC))
				.withAccelerator(
						KeyStroke.getKeyStroke(
								KeyEvent.VK_A, InputEvent.CTRL_MASK))
				.withIcon(
						FormScannerResources
								.getIconFor(FormScannerResourcesKeys.ANALYZE_FILES_ALL_ICON_16))
				.setEnabled(false).build();

		scanMenuItem = new MenuItemBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.ANALYZE_FILES),
				orientation)
				.withActionCommand(FormScannerConstants.ANALYZE_FILES_FIRST)
				.withActionListener(formScannerController)
				.withMnemonic(
						FormScannerTranslation
								.getMnemonicFor(FormScannerTranslationKeys.ANALYZE_FILES_MNEMONIC))
				.withAccelerator(
						KeyStroke.getKeyStroke(
								KeyEvent.VK_I, InputEvent.CTRL_MASK))
				.withIcon(
						FormScannerResources
								.getIconFor(FormScannerResourcesKeys.ANALYZE_FILES_ICON_16))
				.setEnabled(false).build();

		return new MenuBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.EDIT_MENU),
				orientation)
				.withMnemonic(
						FormScannerTranslation
								.getMnemonicFor(FormScannerTranslationKeys.EDIT_MENU_MNEMONIC))
				.add(renameMenuItem).add(new JSeparator(JSeparator.HORIZONTAL))
				.add(scanAllMenuItem).add(scanMenuItem).build();
	}

	public JMenu getTemplateMenu() {

		createTemplateMenuItem = new MenuItemBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.CREATE_TEMPLATE),
				orientation)
				.withActionCommand(FormScannerConstants.CREATE_TEMPLATE)
				.withActionListener(formScannerController)
				.withMnemonic(
						FormScannerTranslation
								.getMnemonicFor(FormScannerTranslationKeys.CREATE_TEMPLATE_MNEMONIC))
				.withAccelerator(
						KeyStroke.getKeyStroke(
								KeyEvent.VK_C, InputEvent.CTRL_MASK))
				.withIcon(
						FormScannerResources
								.getIconFor(FormScannerResourcesKeys.EDIT_ICON_16))
				.build();

		loadTemplateMenuItem = new MenuItemBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.LOAD_TEMPLATE),
				orientation)
				.withActionCommand(FormScannerConstants.LOAD_TEMPLATE)
				.withActionListener(formScannerController)
				.withMnemonic(
						FormScannerTranslation
								.getMnemonicFor(FormScannerTranslationKeys.LOAD_TEMPLATE_MNEMONIC))
				.withAccelerator(
						KeyStroke.getKeyStroke(
								KeyEvent.VK_L, InputEvent.CTRL_MASK))
				.withIcon(
						FormScannerResources
								.getIconFor(FormScannerResourcesKeys.IMPORT_ICON_16))
				.build();

		return new MenuBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.TEMPLATE_MENU),
				orientation)
				.withMnemonic(
						FormScannerTranslation
								.getMnemonicFor(FormScannerTranslationKeys.TEMPLATE_MENU_MNEMONIC))
				.add(createTemplateMenuItem)
				.add(new JSeparator(JSeparator.HORIZONTAL))
				.add(loadTemplateMenuItem).build();

	}

	@Override
	public JMenu getHelpMenu() {
		helpMenuItem = new MenuItemBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.HELP),
				orientation)
				.withActionCommand(FormScannerConstants.HELP)
				.withActionListener(formScannerController)
				.withAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0))
				.withIcon(
						FormScannerResources
								.getIconFor(FormScannerResourcesKeys.HELP_ICON_16))
				.build();

		aboutMenuItem = new MenuItemBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.ABOUT),
				orientation)
				.withActionCommand(FormScannerConstants.ABOUT)
				.withActionListener(formScannerController)
				.withAccelerator(
						KeyStroke.getKeyStroke(
								KeyEvent.VK_F1, InputEvent.ALT_MASK))
				.withIcon(
						FormScannerResources
								.getIconFor(FormScannerResourcesKeys.ABOUT_ICON_16))
				.build();

		return new MenuBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.HELP_MENU),
				orientation)
				.withMnemonic(
						FormScannerTranslation
								.getMnemonicFor(FormScannerTranslationKeys.HELP_MENU_MNEMONIC))
				.add(helpMenuItem).add(aboutMenuItem).build();
	}

	public JMenu getSettingsMenu() {
		languageMenuItem = getLanguagesMenu();
		optionsMenuItem = new MenuItemBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.OPTIONS),
				orientation)
				.withActionCommand(FormScannerConstants.OPTIONS)
				.withActionListener(formScannerController)
				.withAccelerator(
						KeyStroke.getKeyStroke(
								KeyEvent.VK_PLUS, InputEvent.CTRL_MASK))
				.withIcon(
						FormScannerResources
								.getIconFor(FormScannerResourcesKeys.CONFIG_ICON_16))
				.build();

		return new MenuBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.SETTINGS_MENU),
				orientation)
				.withMnemonic(
						FormScannerTranslation
								.getMnemonicFor(FormScannerTranslationKeys.SETTINGS_MENU_MNEMONIC))
				.add(languageMenuItem).add(optionsMenuItem).build();
	}

	private JMenu getLanguagesMenu() {
		String defaultLanguage = model.getLanguage();

		MenuBuilder menuBuilder = new MenuBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.LANGUAGE),
				orientation)
				.withIcon(FormScannerResources
						.getIconFor(FormScannerResourcesKeys.LANGUAGE_ICON_16));

		JRadioButtonMenuItem languageItem;
		ButtonGroup buttonGroup = new ButtonGroup();
		for (Language language : Language.values()) {
			languageItem = new JRadioButtonMenuItem(
					FormScannerTranslation.getTranslationFor(language
							.getTranslation()));
			if (language.getValue().equals(defaultLanguage)) {
				languageItem.setSelected(true);
			}
			languageItem.addActionListener(formScannerController);
			languageItem.setActionCommand(FormScannerConstants.LANGUAGE);
			languageItem.setName(language.getValue());
			languageItem.setIcon(FormScannerResources
						.getIconFor(language.getValue()));
			buttonGroup.add(languageItem);
			menuBuilder.add(languageItem);
		}

		return menuBuilder.build();
	}

	@Override
	public void setRenameControllersEnabled(boolean enable) {
		renameMenuItem.setEnabled(enable);
	}

	@Override
	public void setScanControllersEnabled(boolean enable) {
		scanMenuItem.setEnabled(enable);
	}

	public void setScanAllControllersEnabled(boolean enable) {
		scanAllMenuItem.setEnabled(enable);
	}
}
