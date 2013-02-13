package org.albertoborsetta.formscanner.gui;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;

import org.albertoborsetta.formscanner.commons.Constants;
import org.albertoborsetta.formscanner.controller.FormScannerController;
import org.albertoborsetta.formscanner.model.FormScannerModel;


public class ToolBar extends JPanel {

	private static FormScannerModel model;
	private FormScannerController controller;
	
	/**
	 * Create the panel.
	 */
	public ToolBar(FormScannerModel formScannerModel) {
		
		model = formScannerModel;
		controller = FormScannerController.getInstance(model);
		
		setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
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
	
	private class OpenButton extends JButton {
		
		public OpenButton() {
			setActionCommand(Constants.OPEN_IMAGES);
			addActionListener(controller);
			setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			setToolTipText("Open images");
			setIcon(new ImageIcon(FormScanner.class.getResource("/org/albertoborsetta/formscanner/gui/icons/crystal/lc_open.png")));
		}
	}
	
	private class SaveButton extends JButton {
		
		public SaveButton() {
			setActionCommand(Constants.SAVE_RESULTS);
			addActionListener(controller);
			setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			setIcon(new ImageIcon(FormScanner.class.getResource("/org/albertoborsetta/formscanner/gui/icons/crystal/lc_save.png")));
			setToolTipText("Save results");
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
	
	private class RenameButton extends JButton {
		
		public RenameButton() {
			setActionCommand(Constants.RENAME_FILE_FIRST);
			addActionListener(controller);
			setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			setIcon(new ImageIcon(FormScanner.class.getResource("/org/albertoborsetta/formscanner/gui/icons/crystal/lc_editdoc.png")));
			setToolTipText("Rename image files");
		}		
	}
}
