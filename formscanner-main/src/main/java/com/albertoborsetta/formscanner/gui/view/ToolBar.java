package com.albertoborsetta.formscanner.gui.view;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.border.EtchedBorder;

import com.albertoborsetta.formscanner.commons.FormScannerConstants;
import com.albertoborsetta.formscanner.commons.resources.FormScannerResources;
import com.albertoborsetta.formscanner.commons.resources.FormScannerResourcesKeys;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import com.albertoborsetta.formscanner.gui.builder.ButtonBuilder;
import com.albertoborsetta.formscanner.gui.builder.ToolBarBuilder;
import com.albertoborsetta.formscanner.gui.controller.FormScannerController;
import com.albertoborsetta.formscanner.gui.model.FormScannerModel;

public class ToolBar extends JPanel implements MenuView {

	private static final long serialVersionUID = 1L;

	private FormScannerController formScannerController;

	private JButton openButton;
	// private JButton saveButton;
	private JButton renameButton;
	private JButton startButton;
	private JButton startAllButton;
	private JButton reloadButton;
	private FormScannerModel model;
	private ComponentOrientation orientation;

	/**
	 * Create the panel.
	 */
	public ToolBar(FormScannerModel model) {
		this.model = model;
		orientation = model.getOrientation();

		formScannerController = FormScannerController
				.getInstance(model);

		setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
		setComponentOrientation(model.getOrientation());

		if (model.getOrientation().isLeftToRight()) {
			setLayout(new FlowLayout(FlowLayout.LEFT));
		} else {
			setLayout(new FlowLayout(FlowLayout.RIGHT));
		}

		JToolBar fileToolBar = getFileToolBar();
		add(fileToolBar);

		JToolBar editToolBar = getEditToolBar();
		add(editToolBar);
	}

	public JToolBar getFileToolBar() {

		openButton = new ButtonBuilder(orientation)
				.withActionCommand(FormScannerConstants.OPEN_IMAGES)
				.withActionListener(formScannerController)
				.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null))
				.withToolTip(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.OPEN_IMAGES_TOOLTIP))
				.withIcon(
						FormScannerResources
								.getIconFor(FormScannerResourcesKeys.OPEN_IMAGES_ICON))
				.build();
		// saveButton = new ButtonBuilder()
		// .withActionCommand(FormScannerConstants.SAVE_RESULTS)
		// .withActionListener(formScannerController)
		// .setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null))
		// .withToolTip(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.SAVE_RESULTS_TOOLTIP))
		// .withIcon(FormScannerResources.getIconFor(FormScannerResourcesKeys.SAVE_RESULTS_ICON))
		// .setEnabled(false)
		// .build();

		return new ToolBarBuilder(orientation).withAlignmentY(Component.CENTER_ALIGNMENT)
				.withAlignmentX(Component.LEFT_ALIGNMENT)
				.add(openButton)
				// .add(saveButton)
				.build();
	}

	public JToolBar getEditToolBar() {

		renameButton = new ButtonBuilder(orientation)
				.withActionCommand(FormScannerConstants.RENAME_FILES_FIRST)
				.withActionListener(formScannerController)
				.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null))
				.withToolTip(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.RENAME_FILES_TOOLTIP))
				.withIcon(
						FormScannerResources
								.getIconFor(FormScannerResourcesKeys.RENAME_FILES_ICON))
				.setEnabled(false).build();
		startButton = new ButtonBuilder(orientation)
				.withActionCommand(FormScannerConstants.ANALYZE_FILES_FIRST)
				.withActionListener(formScannerController)
				.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null))
				.withToolTip(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.ANALYZE_FILES_TOOLTIP))
				.withIcon(
						FormScannerResources
								.getIconFor(FormScannerResourcesKeys.ANALYZE_FILES_ICON))
				.setEnabled(false).build();
		startAllButton = new ButtonBuilder(orientation)
				.withActionCommand(FormScannerConstants.ANALYZE_FILES_ALL)
				.withActionListener(formScannerController)
				.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null))
				.withToolTip(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.ANALYZE_FILES_ALL_TOOLTIP))
				.withIcon(
						FormScannerResources
								.getIconFor(FormScannerResourcesKeys.ANALYZE_FILES_ALL_ICON))
				.setEnabled(false).build();
		reloadButton = new ButtonBuilder(orientation)
				.withActionCommand(FormScannerConstants.ANALYZE_FILES_CURRENT)
				.withActionListener(formScannerController)
				.setBorder(new EtchedBorder(EtchedBorder.LOWERED, null, null))
				.withToolTip(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.ANALYZE_FILES_CURRENT_TOOLTIP))
				.withIcon(
						FormScannerResources
								.getIconFor(FormScannerResourcesKeys.ANALYZE_FILES_CURRENT_ICON))
				.setEnabled(false).build();

		ToolBarBuilder toolBarBuilder = new ToolBarBuilder(orientation).withAlignmentY(Component.CENTER_ALIGNMENT)
				.withAlignmentX(Component.LEFT_ALIGNMENT);
		
		if (orientation.isLeftToRight()) {
			toolBarBuilder.add(renameButton).add(startAllButton).add(startButton).add(reloadButton);
		} else {
			toolBarBuilder.add(reloadButton).add(startButton).add(startAllButton).add(renameButton);
		}
		return toolBarBuilder.build();
	}

	public void setRenameControllersEnabled(boolean enable) {
		renameButton.setEnabled(enable);
	}

	public void setScanControllersEnabled(boolean enable) {
		startButton.setEnabled(enable);
	}
	
	public void setScanAllControllersEnabled(boolean enable) {
		startAllButton.setEnabled(enable);
	}

	public void setScanCurrentControllersEnabled(boolean enable) {
		reloadButton.setEnabled(enable);
	}
}
