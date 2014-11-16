package org.albertoborsetta.formscanner.gui.view;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.ButtonGroup;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Language;
import org.albertoborsetta.formscanner.gui.builder.MenuBuilder;
import org.albertoborsetta.formscanner.gui.builder.MenuItemBuilder;
import org.albertoborsetta.formscanner.gui.controller.FormScannerController;
import org.albertoborsetta.formscanner.gui.model.FormScannerModel;

public class MenuBar extends JMenuBar implements MenuView {

	private static final long serialVersionUID = 1L;

	private FormScannerModel formScannerModel;
	private FormScannerController formScannerController;

	private JMenuItem openMenuItem;
	// private JMenuItem saveMenuItem;
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

	// private JMenuItem editTemplateMenuItem;

	public MenuBar(final FormScannerModel model) {
		formScannerModel = model;
		formScannerController = FormScannerController
				.getInstance(formScannerModel);

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
						.getTranslationFor(FormScannerTranslationKeys.OPEN_IMAGES))
				.withActionCommand(FormScannerConstants.OPEN_IMAGES)
				.withActionListener(formScannerController)
				.withAccelerator(
						KeyStroke.getKeyStroke(KeyEvent.VK_O,
								InputEvent.CTRL_MASK))
				.withMnemonic(
						FormScannerTranslation
								.getMnemonicFor(FormScannerTranslationKeys.OPEN_IMAGES_MNEMONIC))
				.build();

		// saveMenuItem = new
		// MenuItemBuilder(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.SAVE_RESULTS))
		// .withActionCommand(FormScannerConstants.SAVE_RESULTS)
		// .withActionListener(formScannerController)
		// .withMnemonic(FormScannerTranslation.getMnemonicFor(FormScannerTranslationKeys.SAVE_RESULTS_MNEMONIC))
		// .withAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
		// InputEvent.CTRL_MASK))
		// .setEnabled(false)
		// .build();

		exitMenuItem = new MenuItemBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.EXIT))
				.withActionCommand(FormScannerConstants.EXIT)
				.withActionListener(formScannerController)
				.withMnemonic(
						FormScannerTranslation
								.getMnemonicFor(FormScannerTranslationKeys.EXIT_MNEMONIC))
				.withAccelerator(
						KeyStroke.getKeyStroke(KeyEvent.VK_X,
								InputEvent.CTRL_MASK)).build();

		return new MenuBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.FILE_MENU))
				.withMnemonic(
						FormScannerTranslation
								.getMnemonicFor(FormScannerTranslationKeys.FILE_MENU_MNEMONIC))
				.add(openMenuItem)
				// .add(saveMenuItem)
				.add(new JSeparator(JSeparator.HORIZONTAL)).add(exitMenuItem)
				.build();
	}

	public JMenu getEditMenu() {

		renameMenuItem = new MenuItemBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.RENAME_FILES))
				.withActionCommand(FormScannerConstants.RENAME_FILES_FIRST)
				.withActionListener(formScannerController)
				.withMnemonic(
						FormScannerTranslation
								.getMnemonicFor(FormScannerTranslationKeys.RENAME_FILES_MNEMONIC))
				.withAccelerator(
						KeyStroke.getKeyStroke(KeyEvent.VK_R,
								InputEvent.CTRL_MASK)).setEnabled(false)
				.build();
		
		scanAllMenuItem = new MenuItemBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.ANALYZE_FILES_ALL))
				.withActionCommand(FormScannerConstants.ANALYZE_FILES_ALL)
				.withActionListener(formScannerController)
				.withMnemonic(
						FormScannerTranslation
								.getMnemonicFor(FormScannerTranslationKeys.ANALYZE_FILES_ALL_MNEMONIC))
				.withAccelerator(
						KeyStroke.getKeyStroke(KeyEvent.VK_A,
								InputEvent.CTRL_MASK)).setEnabled(false)
				.build();
		
		scanMenuItem = new MenuItemBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.ANALYZE_FILES))
				.withActionCommand(FormScannerConstants.ANALYZE_FILES_FIRST)
				.withActionListener(formScannerController)
				.withMnemonic(
						FormScannerTranslation
								.getMnemonicFor(FormScannerTranslationKeys.ANALYZE_FILES_MNEMONIC))
				.withAccelerator(
						KeyStroke.getKeyStroke(KeyEvent.VK_I,
								InputEvent.CTRL_MASK)).setEnabled(false)
				.build();

		return new MenuBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.EDIT_MENU))
				.withMnemonic(
						FormScannerTranslation
								.getMnemonicFor(FormScannerTranslationKeys.EDIT_MENU_MNEMONIC))
				.add(renameMenuItem)
				.add(new JSeparator(JSeparator.HORIZONTAL))
				.add(scanAllMenuItem)
				.add(scanMenuItem)
				.build();
	}

	public JMenu getTemplateMenu() {

		createTemplateMenuItem = new MenuItemBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.CREATE_TEMPLATE))
				.withActionCommand(FormScannerConstants.CREATE_TEMPLATE)
				.withActionListener(formScannerController)
				.withMnemonic(
						FormScannerTranslation
								.getMnemonicFor(FormScannerTranslationKeys.CREATE_TEMPLATE_MNEMONIC))
				.withAccelerator(
						KeyStroke.getKeyStroke(KeyEvent.VK_C,
								InputEvent.CTRL_MASK)).build();
		loadTemplateMenuItem = new MenuItemBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.LOAD_TEMPLATE))
				.withActionCommand(FormScannerConstants.LOAD_TEMPLATE)
				.withActionListener(formScannerController)
				.withMnemonic(
						FormScannerTranslation
								.getMnemonicFor(FormScannerTranslationKeys.LOAD_TEMPLATE_MNEMONIC))
				.withAccelerator(
						KeyStroke.getKeyStroke(KeyEvent.VK_L,
								InputEvent.CTRL_MASK)).build();
		// editTemplateMenuItem = new
		// MenuItemBuilder(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.EDIT_TEMPLATE))
		// .withActionCommand(FormScannerConstants.EDIT_TEMPLATE)
		// .withActionListener(formScannerController)
		// .withMnemonic(FormScannerTranslation.getMnemonicFor(FormScannerTranslationKeys.EDIT_TEMPLATE_MNEMONIC))
		// .withAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,
		// InputEvent.CTRL_MASK))
		// .build();

		return new MenuBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.TEMPLATE_MENU))
				.withMnemonic(
						FormScannerTranslation
								.getMnemonicFor(FormScannerTranslationKeys.TEMPLATE_MENU_MNEMONIC))
				.add(createTemplateMenuItem)
				// .add(editTemplateMenuItem)
				.add(new JSeparator(JSeparator.HORIZONTAL))
				.add(loadTemplateMenuItem).build();

	}

	public JMenu getHelpMenu() {
		helpMenuItem = new MenuItemBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.HELP))
				.withActionCommand(FormScannerConstants.HELP)
				.withActionListener(formScannerController)
				.withAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, 0))
				.build();

		aboutMenuItem = new MenuItemBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.ABOUT))
				.withActionCommand(FormScannerConstants.ABOUT)
				.withActionListener(formScannerController)
				.withAccelerator(
						KeyStroke.getKeyStroke(KeyEvent.VK_F1,
								InputEvent.ALT_MASK)).build();

		return new MenuBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.HELP_MENU))
				.withMnemonic(
						FormScannerTranslation
								.getMnemonicFor(FormScannerTranslationKeys.HELP_MENU_MNEMONIC))
				.add(helpMenuItem).add(aboutMenuItem).build();
	}

	public JMenu getSettingsMenu() {
		languageMenuItem = getLanguagesMenu();
		optionsMenuItem = new MenuItemBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.OPTIONS))
				.withActionCommand(FormScannerConstants.OPTIONS)
				.withActionListener(formScannerController)
				.withAccelerator(
						KeyStroke.getKeyStroke(KeyEvent.VK_PLUS,
								InputEvent.CTRL_MASK)).build();

		return new MenuBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.SETTINGS_MENU))
				.withMnemonic(
						FormScannerTranslation
								.getMnemonicFor(FormScannerTranslationKeys.SETTINGS_MENU_MNEMONIC))
				.add(languageMenuItem).add(optionsMenuItem).build();
	}

	private JMenu getLanguagesMenu() {
		String defaultLanguage = formScannerModel.getLanguage();

		MenuBuilder menuBuilder = new MenuBuilder(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.LANGUAGE));

		JRadioButtonMenuItem languageItem;
		ButtonGroup buttonGroup = new ButtonGroup();
		for (Language language : Language.values()) {
			languageItem = new JRadioButtonMenuItem(FormScannerTranslation.getTranslationFor(language.getTranslation()));
			if (language.getValue().equals(defaultLanguage)) {
				languageItem.setSelected(true);
			}
			languageItem.addActionListener(formScannerController);
			languageItem.setActionCommand(FormScannerConstants.LANGUAGE);
			languageItem.setName(language.getValue());
			buttonGroup.add(languageItem);
			menuBuilder.add(languageItem);
		}

		return menuBuilder.build();
	}

	public void setRenameControllersEnabled(boolean enable) {
		renameMenuItem.setEnabled(enable);
	}

	public void setScanControllersEnabled(boolean enable) {
		scanMenuItem.setEnabled(enable);
	}

	public void setScanAllControllersEnabled(boolean enable) {
		scanAllMenuItem.setEnabled(enable);
	}
}
