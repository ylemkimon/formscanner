package org.albertoborsetta.formscanner.gui;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.gui.builder.MenuBuilder;
import org.albertoborsetta.formscanner.gui.builder.MenuItemBuilder;
import org.albertoborsetta.formscanner.gui.controller.FormScannerController;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class MenuBar extends JMenuBar implements MenuView {
	
	private static final long serialVersionUID = 1L;
	
	private FormScannerModel formScannerModel;
	private FormScannerController formScannerController;
	
	private JMenuItem openMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem exitMenuItem;
	private JMenuItem renameMenuItem;
	
	private JMenuItem loadTemplateMenuItem;
	// private JMenuItem useTemplateMenuItem;
	// private JMenuItem editTemplateMenuItem; 
	
	public MenuBar(final FormScannerModel model) {
		formScannerModel = model;
		formScannerController = FormScannerController.getInstance(formScannerModel);
		
		JMenu fileMenu = getFileMenu();
		JMenu editMenu = getEditMenu();
		JMenu templateMenu = getTemplateMenu();
		add(fileMenu);
		add(editMenu);
		add(templateMenu);
	}

	private JMenu getFileMenu() {			
		
		openMenuItem = new MenuItemBuilder(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.OPEN_IMAGES))
			.withActionCommand(FormScannerConstants.OPEN_IMAGES)
			.withActionListener(formScannerController)
			.withAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK))
			.withMnemonic(FormScannerTranslation.getMnemonicFor(FormScannerTranslationKeys.OPEN_IMAGES_MNEMONIC))
			.build();
		
		saveMenuItem = new MenuItemBuilder(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.SAVE_RESULTS))
			.withActionCommand(FormScannerConstants.SAVE_RESULTS)
			.withActionListener(formScannerController)
			.withMnemonic(FormScannerTranslation.getMnemonicFor(FormScannerTranslationKeys.SAVE_RESULTS_MNEMONIC))
			.withAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK))
			.setEnabled(false)
			.build();
		
		exitMenuItem = new MenuItemBuilder(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.EXIT))
			.withActionCommand(FormScannerConstants.EXIT)
			.withActionListener(formScannerController)
			.withMnemonic(FormScannerTranslation.getMnemonicFor(FormScannerTranslationKeys.EXIT_MNEMONIC))
			.withAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK))
			.build();
		
		return new MenuBuilder(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.FILE_MENU))
			.withMnemonic(FormScannerTranslation.getMnemonicFor(FormScannerTranslationKeys.FILE_MENU_MNEMONIC))
			.add(openMenuItem)
			.add(saveMenuItem)
			.add(new JSeparator(JSeparator.HORIZONTAL))
			.add(exitMenuItem)
			.build();
	}

	public JMenu getEditMenu() {
		
		renameMenuItem = new MenuItemBuilder(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.RENAME_FILES))
			.withActionCommand(FormScannerConstants.RENAME_FILE_FIRST)
			.withActionListener(formScannerController)
			.withMnemonic(FormScannerTranslation.getMnemonicFor(FormScannerTranslationKeys.RENAME_FILES_MNEMONIC))
			.withAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK))
			.setEnabled(false)
			.build();
		
		return new MenuBuilder(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.EDIT_MENU))
			.withMnemonic(FormScannerTranslation.getMnemonicFor(FormScannerTranslationKeys.EDIT_MENU_MNEMONIC))
			.add(renameMenuItem)
			.build();
	}

	public JMenu getTemplateMenu() {			
				
		loadTemplateMenuItem = new MenuItemBuilder(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.LOAD_TEMPLATE))
			.withActionCommand(FormScannerConstants.LOAD_TEMPLATE)
			.withActionListener(formScannerController)
			.withMnemonic(FormScannerTranslation.getMnemonicFor(FormScannerTranslationKeys.LOAD_TEMPLATE_MNEMONIC))
			.withAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK))
			.build();
		// useTemplateMenuItem = new UseTemplateMenuItem();
		// editTemplateMenuItem = new EditTemplateMenuItem();
		
		return new MenuBuilder(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.TEMPLATE_MENU))
			.withMnemonic(FormScannerTranslation.getMnemonicFor(FormScannerTranslationKeys.TEMPLATE_MENU_MNEMONIC))
			.add(loadTemplateMenuItem)
			.build();
		// add(editTemplateMenuItem);
		// add(new JSeparator(JSeparator.HORIZONTAL));
		// add(useTemplateMenuItem);
	}
	
	public void setRenameControllersEnabled(boolean enable) {
		renameMenuItem.setEnabled(enable);
	}

	public void setScanControllersEnabled(boolean enable) {
		// TODO Auto-generated method stub
		
	}
}
