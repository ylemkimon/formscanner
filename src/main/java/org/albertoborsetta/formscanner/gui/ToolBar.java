package org.albertoborsetta.formscanner.gui;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.albertoborsetta.formscanner.commons.Constants;
import org.albertoborsetta.formscanner.gui.font.FormScannerFont;
import org.albertoborsetta.formscanner.model.FormScannerModel;


public class ToolBar extends JPanel {

	private static FormScannerModel model;
	
	/**
	 * Create the panel.
	 */
	public ToolBar(FormScannerModel formScannerModel) {
		
		model = formScannerModel;
		
		setPreferredSize(new Dimension(10, 30));
		setMinimumSize(new Dimension(10, 50));
		setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JToolBar fileToolBar = new FileToolBar();
		add(fileToolBar);
		
		JToolBar editToolBar = new EditToolBar();
		add(editToolBar);
	}
	
	private class FileToolBar extends JToolBar {

		public FileToolBar() {
			setAlignmentY(Component.CENTER_ALIGNMENT);
			setAlignmentX(Component.LEFT_ALIGNMENT);
			setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			
			JButton openButton = new OpenButton();
			add(openButton);
			
			JButton saveButton = new SaveButton();
			add(saveButton);	
		}
	}
	
	private class OpenButton extends JButton implements ActionListener {
		
		public OpenButton() {
			addActionListener(this);
			setMinimumSize(new Dimension(26, 26));
			setPreferredSize(new Dimension(26, 26));
			setMaximumSize(new Dimension(26, 26));
			setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			setToolTipText("Open images");
			setIcon(new ImageIcon(FormScanner.class.getResource("/org/albertoborsetta/formscanner/gui/icons/crystal/lc_open.png")));
		}
		
		public void actionPerformed(ActionEvent e) {
			openFiles();
		}
	}
	
	private class SaveButton extends JButton implements ActionListener {
		
		public SaveButton() {
			addActionListener(this);
			setMinimumSize(new Dimension(26, 26));
			setPreferredSize(new Dimension(26, 26));
			setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			setMaximumSize(new Dimension(26, 26));
			setIcon(new ImageIcon(FormScanner.class.getResource("/org/albertoborsetta/formscanner/gui/icons/crystal/lc_save.png")));
			setToolTipText("Save results");
		}
		
		public void actionPerformed(ActionEvent e) {
			// TODO
		}
	}
	
	private class EditToolBar extends JToolBar {
		
		public EditToolBar() {
			setAlignmentY(Component.CENTER_ALIGNMENT);
			setAlignmentX(Component.LEFT_ALIGNMENT);
			setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			
			JButton renameButton = new RenameButton();
			add(renameButton);	
		}
	}
	
	private class RenameButton extends JButton implements ActionListener {
		
		public RenameButton() {
			addActionListener(this);
			setMinimumSize(new Dimension(26, 26));
			setPreferredSize(new Dimension(26, 26));
			setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			setMaximumSize(new Dimension(26, 26));
			setIcon(new ImageIcon(FormScanner.class.getResource("/org/albertoborsetta/formscanner/gui/icons/crystal/lc_editdoc.png")));
			setToolTipText("Rename image files");
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
