package org.albertoborsetta.formscanner.gui;

import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;

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
	
	
	public MenuBar(final FormScannerModel formScannerModel) {
		this.formScannerModel = formScannerModel;
		formScannerController = FormScannerController.getInstance(formScannerModel);
		
		JMenu fileMenu = new FileMenu();
		JMenu editMenu = new EditMenu();
		add(fileMenu);
		add(editMenu);
	}
	
	private class FileMenu extends JMenu {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public FileMenu() {			
			super(formScannerModel.getTranslationFor(FormScannerTranslationKeys.FILE_MENU));
			setMnemonic(FormScannerTranslationKeys.FILE_MENU_MNEMONIC);
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
			setMnemonic('E');
			setToolTipText(formScannerModel.getTranslationFor(FormScannerTranslationKeys.EDIT_MENU_TOOLTIP));
			setFont(FormScannerFont.getFont());
			
			renameMenuItem = new RenameMenuItem();
			add(renameMenuItem);
		}
	}
	
	private class OpenMenuItem extends JMenuItem {
		
		public OpenMenuItem() {
			super("Open images");			
			setActionCommand(FormScannerConstants.OPEN_IMAGES);
			addActionListener(formScannerController);
			setMnemonic('O');
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
			setToolTipText("Open image files");
			setFont(FormScannerFont.getFont());
		}
	}
	
	private class SaveMenuItem extends JMenuItem {
		
		public SaveMenuItem() {
			super("Save results");	
			setActionCommand(FormScannerConstants.SAVE_RESULTS);
			addActionListener(formScannerController);
			setMnemonic('S');
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
			setToolTipText("Save results file");
			setFont(FormScannerFont.getFont());
			setEnabled(false);
		}
	}
	
	private class ExitMenuItem extends JMenuItem {
		
		public ExitMenuItem() {
			super("Exit");	
			setActionCommand(FormScannerConstants.EXIT);
			addActionListener(formScannerController);
			setMnemonic('X');
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, InputEvent.CTRL_MASK));
			setToolTipText("Exit");
			setFont(FormScannerFont.getFont());
		}
	}
	
	private class RenameMenuItem extends JMenuItem {
		
		public RenameMenuItem() {
			super("Rename files");			
			setActionCommand(FormScannerConstants.RENAME_FILE_FIRST);
			addActionListener(formScannerController);
			setMnemonic('R');
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
			setToolTipText("Rename image files");
			setFont(FormScannerFont.getFont());
			setEnabled(false);
		}
	}
	
	public void setRenameControllersEnabled(boolean enable) {
		renameMenuItem.setEnabled(enable);
	}
}
