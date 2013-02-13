package org.albertoborsetta.formscanner.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.albertoborsetta.formscanner.commons.Constants;
import org.albertoborsetta.formscanner.controller.FormScannerController;
import org.albertoborsetta.formscanner.gui.font.FormScannerFont;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class MenuBar extends JMenuBar {
	
	private static FormScannerModel model;
	private FormScannerController controller;
	
	public MenuBar(final FormScannerModel formScannerModel) {
		model = formScannerModel;
		controller = FormScannerController.getInstance(model);
		
		JMenu fileMenu = new FileMenu();
		JMenu editMenu = new EditMenu();
		add(fileMenu);
		add(editMenu);
	}
	
private class FileMenu extends JMenu {
		
		public FileMenu() {
			super("File");
			setMnemonic('F');
			setToolTipText("File menu");
			setFont(FormScannerFont.getFont());
			
			JMenuItem openMenuItem = new OpenMenuItem();
			JMenuItem saveMenuItem = new SaveMenuItem();
			add(openMenuItem);
			add(saveMenuItem);
		}
	}
	
	private class OpenMenuItem extends JMenuItem {
		
		public OpenMenuItem() {
			super("Open images");			
			setActionCommand(Constants.OPEN_IMAGES);
			addActionListener(controller);
			setMnemonic('O');
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
			setToolTipText("Open image files");
			setFont(FormScannerFont.getFont());
		}
	}
	
	private class SaveMenuItem extends JMenuItem {
		
		public SaveMenuItem() {
			super("Save results");	
			setActionCommand(Constants.SAVE_RESULTS);
			addActionListener(controller);
			setMnemonic('S');
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
			setToolTipText("Save results file");
			setFont(FormScannerFont.getFont());
		}
	}
	
	private class EditMenu extends JMenu {
		
		public EditMenu() {
			super("Edit");
			setMnemonic('E');
			setToolTipText("Edit menu");
			setFont(FormScannerFont.getFont());
			
			JMenuItem renameMenuItem = new RenameMenuItem();
			add(renameMenuItem);
		}
	}
	
	private class RenameMenuItem extends JMenuItem {
		
		public RenameMenuItem() {
			super("Rename files");			
			setActionCommand(Constants.RENAME_FILE_FIRST);
			addActionListener(controller);
			setMnemonic('R');
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
			setToolTipText("Rename image files");
			setFont(FormScannerFont.getFont());
		}
	}
}
