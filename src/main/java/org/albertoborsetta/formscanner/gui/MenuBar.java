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
import org.albertoborsetta.formscanner.gui.font.FormScannerFont;
import org.albertoborsetta.formscanner.model.FormScannerModel;


public class MenuBar extends JMenuBar {
	
	private static FormScannerModel model;
	
	public MenuBar(final FormScannerModel formScannerModel) {
		model = formScannerModel;
		
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
	
	private class OpenMenuItem extends JMenuItem implements ActionListener {
		
		public OpenMenuItem() {
			super("Open images");			
			addActionListener(this);
			setMnemonic('O');
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK));
			setToolTipText("Open image files");
			setFont(FormScannerFont.getFont());
		}
		
		public void actionPerformed(ActionEvent e) {
			openFiles();			
		}
	}
	
	private class SaveMenuItem extends JMenuItem implements ActionListener {
		
		public SaveMenuItem() {
			super("Save results");			
			addActionListener(this);
			setMnemonic('S');
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
			setToolTipText("Save results file");
			setFont(FormScannerFont.getFont());
		}
		
		public void actionPerformed(ActionEvent e) {
			// TODO
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
	
	private class RenameMenuItem extends JMenuItem implements ActionListener {
		
		public RenameMenuItem() {
			super("Rename images");			
			addActionListener(this);
			setMnemonic('R');
			setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_MASK));
			setToolTipText("Rename image files");
			setFont(FormScannerFont.getFont());
		}
		
		public void actionPerformed(ActionEvent e) {
			renameFiles(Constants.RENAME_FILE_FIRST);
		}
	}
	
	private void openFiles() {
		model.openFiles(choose());
	}
	
	private void renameFiles(int action) {
		model.renameFiles(action);
	}
	
	private static File[] choose() {
		  
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setFont(FormScannerFont.getFont());
		fileChooser.setMultiSelectionEnabled(true);
		FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("Image files", "tif", "png", "bmp");
		fileChooser.setFileFilter(imageFilter);
		fileChooser.showOpenDialog(null);
		
		return fileChooser.getSelectedFiles(); 
	}
}
