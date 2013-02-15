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
			JButton saveButton = new SaveButton();
			JButton renameButton = new RenameButton();
			
			add(openButton);
			add(renameButton);
			add(saveButton);			
		}
	}
	
	private class EditToolBar extends JToolBar {
		
		public EditToolBar() {
			setAlignmentY(Component.CENTER_ALIGNMENT);
			setAlignmentX(Component.LEFT_ALIGNMENT);
			setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			
			JButton defaultButton = new DefaultButton();
			JButton moveImageButton = new MoveImageButton();
			JButton hilightButton = new HilightImageButton();
			
			add(defaultButton);
			add(moveImageButton);
			add(hilightButton);
		}
	}
	
	private class OpenButton extends JButton {
		
		public OpenButton() {
			setActionCommand(Constants.OPEN_IMAGES);
			addActionListener(controller);
			setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			setToolTipText("Open images");
			setIcon(new ImageIcon(FormScanner.class.getResource("/org/albertoborsetta/formscanner/gui/icons/lc_open.png")));
		}
	}
	
	private class SaveButton extends JButton {
		
		public SaveButton() {
			setActionCommand(Constants.SAVE_RESULTS);
			addActionListener(controller);
			setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			setIcon(new ImageIcon(FormScanner.class.getResource("/org/albertoborsetta/formscanner/gui/icons/lc_save.png")));
			setToolTipText("Save results");
			setEnabled(false);
		}
	}
	
	private class RenameButton extends JButton {
		
		public RenameButton() {
			setActionCommand(Constants.RENAME_FILE_FIRST);
			addActionListener(controller);
			setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			setIcon(new ImageIcon(FormScanner.class.getResource("/org/albertoborsetta/formscanner/gui/icons/lc_drawtext.png")));
			setToolTipText("Rename image files");
			setEnabled(false);
		}		
	}
	
	private class MoveImageButton extends JButton {
		
		public MoveImageButton() {
			setActionCommand(Constants.MOVE_IMAGE);
			addActionListener(controller);
			setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			setToolTipText("Open images");
			setIcon(new ImageIcon(FormScanner.class.getResource("/org/albertoborsetta/formscanner/gui/icons/lc_arrowshapes.quad-arrow.png")));
			setEnabled(false);
		}
	}

	private class HilightImageButton extends JButton {
		
		public HilightImageButton() {
			setActionCommand(Constants.HILIGHT_IMAGE);
			addActionListener(controller);
			setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			setToolTipText("Open images");
			setIcon(new ImageIcon(FormScanner.class.getResource("/org/albertoborsetta/formscanner/gui/icons/lc_basicshapes.rectangle.png")));
			setEnabled(false);
		}
	}
	
	private class DefaultButton extends JButton {
		
		public DefaultButton() {
			setActionCommand(Constants.DEFAULT_ACTION);
			addActionListener(controller);
			setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null));
			setToolTipText("Open images");
			setIcon(new ImageIcon(FormScanner.class.getResource("/org/albertoborsetta/formscanner/gui/icons/lc_drawselect.png")));
		}
	}
}
