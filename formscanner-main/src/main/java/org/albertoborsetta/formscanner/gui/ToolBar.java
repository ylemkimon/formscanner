package org.albertoborsetta.formscanner.gui;

import java.awt.Component;
import java.awt.ComponentOrientation;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;

import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.resources.FormScannerResourcesKeys;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.controller.FormScannerController;
import org.albertoborsetta.formscanner.gui.builder.ButtonBuilder;
import org.albertoborsetta.formscanner.gui.builder.ToolBarBuilder;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class ToolBar extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private FormScannerModel formScannerModel;
	private FormScannerController formScannerController;
	
	private JButton openButton;
	private JButton saveButton;
	private JButton renameButton;
	private JButton startButton;
	
	/**
	 * Create the panel.
	 */
	public ToolBar(FormScannerModel formScannerModel) {
		
		this.formScannerModel = formScannerModel;
		formScannerController = FormScannerController.getInstance(formScannerModel);
		
		setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		
		JToolBar fileToolBar = getFileToolBar();
		add(fileToolBar);
		
		JToolBar editToolBar = getEditToolBar();
		add(editToolBar);
	}

	public JToolBar getFileToolBar() {			
		
		openButton = new ButtonBuilder()
			.withActionCommand(FormScannerConstants.OPEN_IMAGES)
			.withActionListener(formScannerController)
			.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null))
			.withToolTip(formScannerModel.getTranslationFor(FormScannerTranslationKeys.OPEN_IMAGES_TOOLTIP))
			.withIcon(formScannerModel.getIconFor(FormScannerResourcesKeys.OPEN_IMAGES_ICON))
			.build();
		saveButton = new ButtonBuilder()
			.withActionCommand(FormScannerConstants.SAVE_RESULTS)
			.withActionListener(formScannerController)
			.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null))
			.withToolTip(formScannerModel.getTranslationFor(FormScannerTranslationKeys.SAVE_RESULTS_TOOLTIP))
			.withIcon(formScannerModel.getIconFor(FormScannerResourcesKeys.SAVE_RESULTS_ICON))
			.setEnabled(false)
			.build();
		
		return new ToolBarBuilder()
			.withAlignmentY(Component.CENTER_ALIGNMENT)
			.withAlignmentX(Component.LEFT_ALIGNMENT)
			.withComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT)
			.add(openButton)
			.add(saveButton)
			.build();
	}

	public JToolBar getEditToolBar() {
		
		renameButton = new ButtonBuilder()
			.withActionCommand(FormScannerConstants.RENAME_FILE_FIRST)
			.withActionListener(formScannerController)
			.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null))
			.withToolTip(formScannerModel.getTranslationFor(FormScannerTranslationKeys.RENAME_FILES_TOOLTIP))
			.withIcon(formScannerModel.getIconFor(FormScannerResourcesKeys.RENAME_FILES_ICON))
			.setEnabled(false)
			.build();
		startButton = new ButtonBuilder()
			.withActionCommand(FormScannerConstants.ANALYZE_FILE_FIRST)
			.withActionListener(formScannerController)
			.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null))
			.withToolTip(formScannerModel.getTranslationFor(FormScannerTranslationKeys.ANALYZE_FILES_TOOLTIP))
			.withIcon(formScannerModel.getIconFor(FormScannerResourcesKeys.ANALYZE_FILES_ICON))
			.setEnabled(false)
			.build();
		
		return new ToolBarBuilder()
			.withAlignmentY(Component.CENTER_ALIGNMENT)
			.withAlignmentX(Component.LEFT_ALIGNMENT)
			.withComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT)
			.add(renameButton)
			.add(startButton)
			.build();
	}
	
	public void setRenameControllersEnabled(boolean enable) {
		renameButton.setEnabled(enable);
	}

	public void setScanControllersEnabled(boolean enable) {
		startButton.setEnabled(enable);		
	}
}
