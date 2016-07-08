package com.albertoborsetta.formscanner.gui;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import org.jdesktop.swingx.JXPanel;

import com.albertoborsetta.formscanner.commons.FormScannerConstants;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.Zoom;
import com.albertoborsetta.formscanner.commons.resources.FormScannerResources;
import com.albertoborsetta.formscanner.commons.resources.FormScannerResourcesKeys;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
//import com.albertoborsetta.formscanner.gui.builder.ButtonBuilder;
import com.albertoborsetta.formscanner.gui.builder.ComboBoxBuilder;
import com.albertoborsetta.formscanner.gui.builder.LabelBuilder;
import com.albertoborsetta.formscanner.gui.builder.PanelBuilder;
import com.albertoborsetta.formscanner.gui.builder.ToolBarBuilder;
import com.albertoborsetta.formscanner.controller.ImageFrameController;
import com.albertoborsetta.formscanner.controller.ToolBarController;
import com.albertoborsetta.formscanner.model.FormScannerModel;

public final class ToolBar extends JXPanel implements MenuView {

	private static final long serialVersionUID = 1L;

	private final ToolBarController toolBarController;

//	private JButton openButton;
//	private JButton renameButton;
//	private JButton startButton;
//	private JButton startAllButton;
//	private JButton reloadButton;
	private final FormScannerModel model;
	private final ComponentOrientation orientation;
	private JComboBox<InternalZoom> zoomComboBox;
	private InternalZoom[] zoom;
	private InternalZoom zoomValue = new InternalZoom(Zoom.ZOOM_100);
	private ImageFrameController imageController;
	
	private class InternalZoom {

		private final Zoom zoom;

		protected InternalZoom(Zoom zoom) {
			this.zoom = zoom;
		}

		protected Zoom getZoom() {
			return zoom;
		}

		@Override
		public String toString() {
			String value;
			switch (zoom) {
			case ZOOM_WIDTH:
				value = FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.FIT_WIDTH);
				break;
			case ZOOM_PAGE:
				value = FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.FIT_PAGE);
				break;
			default:
				value = zoom.getValue().toString();
				break;
			}
			return value;
		}
	}

	/**
	 * Create the panel.
	 *
	 * @param model
	 */
	public ToolBar(FormScannerModel model) {
		this.model = model;
		orientation = this.model.getOrientation();

		toolBarController = ToolBarController.getInstance(model);

		setComponentOrientation(model.getOrientation());

		if (orientation.isLeftToRight()) {
			setLayout(new FlowLayout(FlowLayout.LEFT));
		} else {
			setLayout(new FlowLayout(FlowLayout.RIGHT));
		}

//		JToolBar fileToolBar = getFileToolBar();
//		add(fileToolBar);

//		JToolBar editToolBar = getEditToolBar();
//		add(editToolBar);
		
		JToolBar imageToolBar = getImageToolBar();
		add(imageToolBar);
	}
	
	public void setup() {
		zoomComboBox.setSelectedItem(zoomValue);
	}

//	public JToolBar getFileToolBar() {
//
//		openButton = new ButtonBuilder(orientation)
//				.withActionCommand(FormScannerConstants.OPEN_IMAGES)
//				.withActionListener(toolBarController)
//				.withToolTip(
//						FormScannerTranslation
//								.getTranslationFor(FormScannerTranslationKeys.OPEN_IMAGES_TOOLTIP))
//				.withIcon(
//						FormScannerResources
//								.getIconFor(FormScannerResourcesKeys.OPEN_IMAGES_ICON))
//				.build();
//
//		return new ToolBarBuilder(orientation)
//				.withAlignmentY(Component.CENTER_ALIGNMENT)
//				.withAlignmentX(Component.LEFT_ALIGNMENT).add(openButton)
//				.build();
//	}
//
//	public JToolBar getEditToolBar() {
//
//		renameButton = new ButtonBuilder(orientation)
//				.withActionCommand(FormScannerConstants.RENAME_FILES_FIRST)
//				.withActionListener(toolBarController)
//				.withToolTip(
//						FormScannerTranslation
//								.getTranslationFor(FormScannerTranslationKeys.RENAME_FILES_TOOLTIP))
//				.withIcon(
//						FormScannerResources
//								.getIconFor(FormScannerResourcesKeys.RENAME_FILES_ICON))
//				.setEnabled(false).build();
//		startButton = new ButtonBuilder(orientation)
//				.withActionCommand(FormScannerConstants.ANALYZE_FILES_FIRST)
//				.withActionListener(toolBarController)
//				.withToolTip(
//						FormScannerTranslation
//								.getTranslationFor(FormScannerTranslationKeys.ANALYZE_FILES_TOOLTIP))
//				.withIcon(
//						FormScannerResources
//								.getIconFor(FormScannerResourcesKeys.ANALYZE_FILES_ICON))
//				.setEnabled(false).build();
//		startAllButton = new ButtonBuilder(orientation)
//				.withActionCommand(FormScannerConstants.ANALYZE_FILES_ALL)
//				.withActionListener(toolBarController)
//				.withToolTip(
//						FormScannerTranslation
//								.getTranslationFor(FormScannerTranslationKeys.ANALYZE_FILES_ALL_TOOLTIP))
//				.withIcon(
//						FormScannerResources
//								.getIconFor(FormScannerResourcesKeys.ANALYZE_FILES_ALL_ICON))
//				.setEnabled(false).build();
//		reloadButton = new ButtonBuilder(orientation)
//				.withActionCommand(FormScannerConstants.ANALYZE_FILES_CURRENT)
//				.withActionListener(toolBarController)
//				.withToolTip(
//						FormScannerTranslation
//								.getTranslationFor(FormScannerTranslationKeys.ANALYZE_FILES_CURRENT_TOOLTIP))
//				.withIcon(
//						FormScannerResources
//								.getIconFor(FormScannerResourcesKeys.ANALYZE_FILES_CURRENT_ICON))
//				.setEnabled(false).build();
//
//		return new ToolBarBuilder(orientation)
//				.withAlignmentY(Component.CENTER_ALIGNMENT)
//				.withAlignmentX(Component.LEFT_ALIGNMENT).add(renameButton)
//				.add(startAllButton).add(startButton).add(reloadButton).build();
//	}
	
	public JToolBar getImageToolBar() {

		Zoom zoomValues[] = Zoom.values();
		zoom = new InternalZoom[zoomValues.length];
		for (int i = 0; i < zoomValues.length; i++) {
			zoom[i] = new InternalZoom(zoomValues[i]);
			if (zoomValues[i].equals(Zoom.ZOOM_100)) {
				zoomValue = zoom[i];
			}
		}
		
		imageController = ImageFrameController.getInstance(model);
		imageController.add(this);

		JLabel zoomLabel = new LabelBuilder(orientation)
						.withIcon(FormScannerResources.getIconFor(FormScannerResourcesKeys.ZOOM))
						.withBorder(BorderFactory.createEmptyBorder()).build();

		zoomComboBox = new ComboBoxBuilder<InternalZoom>(FormScannerConstants.ZOOM_COMBO_BOX, orientation)
				.withModel(new DefaultComboBoxModel<>(zoom)).withActionListener(imageController).build();

		JPanel zoomPanel = new PanelBuilder(orientation).withLayout(new FlowLayout(FlowLayout.LEFT,2,0))
				.add(zoomLabel).add(zoomComboBox).build();

		return new ToolBarBuilder(orientation).withAlignmentY(Component.CENTER_ALIGNMENT)
				.withAlignmentX(Component.LEFT_ALIGNMENT).add(zoomPanel).build();
	}

	public Double getZoom(ImageFrame imagePanel) {
		double panelWidth = imagePanel.getWidth();
		double imageWidth = imagePanel.getImageWidth();
		double panelHeight = imagePanel.getHeight();
		double imageHeight = imagePanel.getImageHeight();
		zoomValue = (InternalZoom) zoomComboBox.getSelectedItem();
		Double value = zoomValue.getZoom().getValue() / 100d;
		switch (zoomValue.getZoom()) {
		case ZOOM_WIDTH:
			value = panelWidth / imageWidth;
			break;
		case ZOOM_PAGE:
			value = Math.min(panelHeight / imageHeight,
					panelWidth / imageWidth);
			break;
		default:
			break;
		}
		return value;
	}
}
