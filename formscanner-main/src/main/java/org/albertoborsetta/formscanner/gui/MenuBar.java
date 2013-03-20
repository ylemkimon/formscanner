package org.albertoborsetta.formscanner.gui;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.text.Normalizer.Form;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import javax.swing.KeyStroke;

import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.FormScannerFont;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.controller.FormScannerController;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class MenuBar extends JMenuBar {
	
	private static final long serialVersionUID = 1L;
	
	private FormScannerModel formScannerModel;
	private FormScannerController formScannerController;
	
	private JMenuItem openMenuItem;
	private JMenuItem saveMenuItem;
	private JMenuItem exitMenuItem;
	private JMenuItem renameMenuItem;
	
	private JMenuItem loadTemplateMenuItem;
	private JMenuItem useTemplateMenuItem;
	private JMenuItem editTemplateMenuItem; 
	
	public MenuBar(final FormScannerModel formScannerModel) {
		this.formScannerModel = formScannerModel;
		formScannerController = FormScannerController.getInstance(formScannerModel);
		
		JMenu fileMenu = new FileMenu();
		JMenu editMenu = new EditMenu();
		JMenu templateMenu = new TemplateMenu();
		add(fileMenu);
		add(editMenu);
		add(templateMenu);
	}
	
	private class FileMenu extends JMenu {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public FileMenu() {			
			super(formScannerModel.getTranslationFor(FormScannerTranslationKeys.FILE_MENU));
			setMnemonic(formScannerModel.getMnemonicFor(FormScannerTranslationKeys.FILE_MENU_MNEMONIC));
			setToolTipText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.FILE_MENU_TOOLTIP));
			setFont(FormScannerFont.getFont());
			
			openMenuItem = new OpenMenuItem();
			saveMenuItem = new SaveMenuItem();
			exitMenuItem = new ExitMenuItem();
			
			add(openMenuItem);
			add(saveMenuItem);
			add(new JSeparator(JSeparator.HORIZONTAL));
			add(exitMenuItem);
		}
	}
	
	private class EditMenu extends JMenu {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public EditMenu() {
			super(formScannerModel.getTranslationFor(FormScannerTranslationKeys.EDIT_MENU));
			setMnemonic(formScannerModel.getMnemonicFor(FormScannerTranslationKeys.EDIT_MENU_MNEMONIC));
			setToolTipText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.EDIT_MENU_TOOLTIP));
			setFont(FormScannerFont.getFont());
			
			renameMenuItem = new RenameMenuItem();
			add(renameMenuItem);
		}
	}
	
	private class TemplateMenu extends JMenu {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public TemplateMenu() {			
			super(formScannerModel.getTranslationFor(FormScannerTranslationKeys.TEMPLATE_MENU));
			setMnemonic(formScannerModel.getMnemonicFor(FormScannerTranslationKeys.TEMPLATE_MENU_MNEMONIC));
			setToolTipText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.TEMPLATE_MENU_TOOLTIP));
			setFont(FormScannerFont.getFont());
			
			loadTemplateMenuItem = new LoadTemplateMenuItem();
			// useTemplateMenuItem = new UseTemplateMenuItem();
			// editTemplateMenuItem = new EditTemplateMenuItem();
			
			add(loadTemplateMenuItem);
			// add(editTemplateMenuItem);
			// add(new JSeparator(JSeparator.HORIZONTAL));
			// add(useTemplateMenuItem);
		}
	}
	
	private class OpenMenuItem extends JMenuItem {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public OpenMenuItem() {
			super(formScannerModel.getTranslationFor(FormScannerTranslationKeys.OPEN_IMAGES));
			setActionCommand(FormScannerConstants.OPEN_IMAGES);
			addActionListener(formScannerController);
			setMnemonic(formScannerModel.getMnemonicFor(FormScannerTranslationKeys.OPEN_IMAGES_MNEMONIC));
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
			setToolTipText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.OPEN_IMAGES_TOOLTIP));
			setFont(FormScannerFont.getFont());
		}
	}
	
	private class SaveMenuItem extends JMenuItem {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public SaveMenuItem() {
			super(formScannerModel.getTranslationFor(FormScannerTranslationKeys.SAVE_RESULTS));	
			setActionCommand(FormScannerConstants.SAVE_RESULTS);
			addActionListener(formScannerController);
			setMnemonic(formScannerModel.getMnemonicFor(FormScannerTranslationKeys.SAVE_RESULTS_MNEMONIC));
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
			setToolTipText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.SAVE_RESULTS_TOOLTIP));
			setFont(FormScannerFont.getFont());
			setEnabled(false);
		}
	}
	
	private class ExitMenuItem extends JMenuItem {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ExitMenuItem() {
			super(formScannerModel.getTranslationFor(FormScannerTranslationKeys.EXIT));	
			setActionCommand(FormScannerConstants.EXIT);
			addActionListener(formScannerController);
			setMnemonic(formScannerModel.getMnemonicFor(FormScannerTranslationKeys.EXIT_MNEMONIC));
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
			setToolTipText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.EXIT_TOOLTIP));
			setFont(FormScannerFont.getFont());
		}
	}
	
	private class RenameMenuItem extends JMenuItem {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public RenameMenuItem() {
			super(formScannerModel.getTranslationFor(FormScannerTranslationKeys.RENAME_FILES));
			setActionCommand(FormScannerConstants.RENAME_FILE_FIRST);
			addActionListener(formScannerController);
			setMnemonic(formScannerModel.getMnemonicFor(FormScannerTranslationKeys.RENAME_FILES_MNEMONIC));
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
			setToolTipText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.RENAME_FILES_TOOLTIP));
			setFont(FormScannerFont.getFont());
			setEnabled(false);
		}
	}
	
	private class LoadTemplateMenuItem extends JMenuItem {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public LoadTemplateMenuItem() {
			super(formScannerModel.getTranslationFor(FormScannerTranslationKeys.LOAD_TEMPLATE));
			setActionCommand(FormScannerConstants.LOAD_TEMPLATE);
			addActionListener(formScannerController);
			setMnemonic(formScannerModel.getMnemonicFor(FormScannerTranslationKeys.LOAD_TEMPLATE_MNEMONIC));
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_MASK));
			setToolTipText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.LOAD_TEMPLATE_TOOLTIP));
			setFont(FormScannerFont.getFont());
		}
	}
	
	public void setRenameControllersEnabled(boolean enable) {
		renameMenuItem.setEnabled(enable);
	}
}
