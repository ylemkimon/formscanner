package com.albertoborsetta.formscanner.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.SpringLayout;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.plaf.basic.BasicSplitPaneUI;

import org.jdesktop.swingx.JXMultiSplitPane;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTaskPane;
import org.jdesktop.swingx.MultiSplitLayout;
import org.jdesktop.swingx.MultiSplitLayout.Split;
import org.jdesktop.swingx.MultiSplitLayout.Leaf;
import org.jdesktop.swingx.MultiSplitLayout.Divider;

import com.albertoborsetta.formscanner.api.commons.Constants.CornerType;
import com.albertoborsetta.formscanner.api.commons.Constants.ShapeType;
import com.albertoborsetta.formscanner.commons.FormScannerConstants;
import com.albertoborsetta.formscanner.commons.FormScannerFont;
import com.albertoborsetta.formscanner.commons.IconListRenderer;
import com.albertoborsetta.formscanner.commons.RotatedIcon.Rotate;
import com.albertoborsetta.formscanner.commons.resources.FormScannerResources;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import com.albertoborsetta.formscanner.controller.OptionsPanelController;
import com.albertoborsetta.formscanner.gui.builder.ComboBoxBuilder;
import com.albertoborsetta.formscanner.gui.builder.LabelBuilder;
import com.albertoborsetta.formscanner.gui.builder.PanelBuilder;
import com.albertoborsetta.formscanner.gui.builder.SpinnerBuilder;
import com.albertoborsetta.formscanner.gui.builder.TaskPaneBuilder;
import com.albertoborsetta.formscanner.gui.builder.ToggleButtonBuilder;
import com.albertoborsetta.formscanner.model.FormScannerModel;

public class OptionsPanel extends JSplitPane {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private OptionsPanelController leftPanelController;
	private JSpinner thresholdValue;
	private JSpinner densityValue;
	private JComboBox<InternalShapeType> shapeTypeComboBox;
	private JSpinner shapeSizeValue;
	private JComboBox<InternalCornerType> cornerTypeComboBox;
	private InternalCornerType[] corners;
	private InternalShapeType[] types;
	private JSpinner cropFromTop;
	private JSpinner cropFromBottom;
	private JSpinner cropFromLeft;
	private JSpinner cropFromRight;
	private JXPanel leftPanelControls;
	
	private ComponentOrientation orientation;
	private FormScannerModel model;
	private FormScannerWorkspace workspace;
	
	private float defaultDividerLocation = 0.25F;
	
	private class InternalShapeType {

		private final ShapeType type;

		protected InternalShapeType(ShapeType type) {
			this.type = type;
		}

		protected ShapeType getType() {
			return type;
		}

		@Override
		public String toString() {
			return FormScannerTranslation.getTranslationFor(type.getValue());
		}
	}

	private class InternalCornerType {

		private final CornerType type;

		protected InternalCornerType(CornerType type) {
			this.type = type;
		}

		protected CornerType getType() {
			return type;
		}

		@Override
		public String toString() {
			return FormScannerTranslation.getTranslationFor(type.getValue());
		}
	}
	
	private class OptionsPanelControls extends JXPanel{

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		protected OptionsPanelControls() {
			JToggleButton leftToggleButton = new ToggleButtonBuilder(orientation)
					.withActionListener(leftPanelController)
					.withActionCommand(FormScannerConstants.TOGGLE_LEFT_PANEL)
					.withText("Options")
					.withRotation(Rotate.UP).withBorder(new EmptyBorder(5, 2, 5, 3)).withFontSize(11).build();
			
			setComponentOrientation(orientation);
			setBorder(new EtchedBorder(EtchedBorder.LOWERED));
			setLayout(new FlowLayout(FlowLayout.RIGHT, 2, 2));
			add(leftToggleButton);
		}
	}

	public OptionsPanel(FormScannerModel model, JPanel desktop) {
		super(JSplitPane.HORIZONTAL_SPLIT);
		this.model = model;
		orientation = model.getOrientation();

		BasicSplitPaneUI rowBasicSplit = (BasicSplitPaneUI) getUI();
		rowBasicSplit.getDivider().setBorder(new LineBorder(Color.WHITE, 1) {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
				g.setColor(Color.LIGHT_GRAY);
				g.drawLine(c.getWidth() - 1, 0, c.getWidth() - 1, c.getHeight());
			}
		});
		
		setFont(FormScannerFont.getFont());
		setComponentOrientation(orientation);
		setDividerSize(4);
		setBorder(null);
		setOneTouchExpandable(false);
		setContinuousLayout(true);
		setLeftComponent(getContentPanel());
		setRightComponent(desktop);
		setResizeWeight(0.2);
		setDividerLocation(defaultDividerLocation);
		
		workspace = model.getWorkspace();
	}
	
	private JXMultiSplitPane getContentPanel() {
		leftPanelController = OptionsPanelController.getInstance(model);
		leftPanelController.add(this);
		
		JXMultiSplitPane contentSplitPane = new JXMultiSplitPane();
		contentSplitPane.setComponentOrientation(orientation);
		
		Split contentSplit = new Split();
		contentSplit.setRowLayout(false);
		contentSplit.setChildren(new Leaf(FormScannerTranslationKeys.SCAN_OPTIONS), new Divider(),
				new Leaf(FormScannerTranslationKeys.TEMPLATE_OPTIONS), new Divider(),
				new Leaf(FormScannerTranslationKeys.IMAGE_PREPROCESSING_OPTIONS));
		
		MultiSplitLayout contentLayout = new MultiSplitLayout(contentSplit);
		contentLayout.setFloatingDividers(true);
		
		contentSplitPane.setLayout(contentLayout);
		contentSplitPane.add(getScanOptionsPanel(), FormScannerTranslationKeys.SCAN_OPTIONS);
		contentSplitPane.add(getImagePreprocessingPanel(), FormScannerTranslationKeys.IMAGE_PREPROCESSING_OPTIONS);
		setDefaultValues();
		return contentSplitPane;
	}
	
	public JXPanel getPanelControls() {
		if (leftPanelControls == null) {
			leftPanelControls = new OptionsPanelControls();
		}
		return leftPanelControls;
	}

	public void setDefaultValues() {
		Integer threshold = model.getThreshold();
		Integer density = model.getDensity();
		Integer shapeSize = model.getShapeSize();
		int shapeType = model.getShapeType().getIndex();
		int cornerType = model.getCornerType().getIndex();
		
		thresholdValue.setValue(threshold);
		densityValue.setValue(density);
		shapeSizeValue.setValue(shapeSize);
		shapeTypeComboBox.setSelectedIndex(shapeType);
		cornerTypeComboBox.setSelectedIndex(cornerType);

		HashMap<String, Integer> crop = model.getCrop();
		cropFromTop.setValue(crop.get(FormScannerConstants.TOP));
		cropFromBottom.setValue(crop.get(FormScannerConstants.BOTTOM));
		cropFromLeft.setValue(crop.get(FormScannerConstants.LEFT));
		cropFromRight.setValue(crop.get(FormScannerConstants.RIGHT));
	}
	
	private JXTaskPane getImagePreprocessingPanel() {
		JPanel cropPanel = getCropPanel();

		return new TaskPaneBuilder(orientation)
				.withTitle(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.IMAGE_PREPROCESSING_OPTIONS))
				.withLayout(new BorderLayout())
				.add(cropPanel, BorderLayout.NORTH)
				.build();
	}
	
	private JPanel getCropPanel() {
		cropFromTop = new SpinnerBuilder(
				FormScannerConstants.CROP_FROM_TOP, orientation)
				.withActionListener(leftPanelController).build();
		cropFromBottom = new SpinnerBuilder(
				FormScannerConstants.CROP_FROM_BOTTOM, orientation)
				.withActionListener(leftPanelController).build();
		cropFromLeft = new SpinnerBuilder(
				FormScannerConstants.CROP_FROM_LEFT, orientation)
				.withActionListener(leftPanelController).build();
		cropFromRight = new SpinnerBuilder(
				FormScannerConstants.CROP_FROM_RIGHT, orientation)
				.withActionListener(leftPanelController).build();

		return new PanelBuilder(orientation)
				.withLayout(new SpringLayout())
				.withBorder(
						BorderFactory.createTitledBorder(FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.CROP_OPTIONS)))
				.add(getLabel("")).add(getLabel(""))
				.add(getLabel(FormScannerTranslationKeys.CROP_FROM_TOP))
				.add(cropFromTop).add(getLabel("")).add(getLabel(""))
				.add(getLabel(FormScannerTranslationKeys.CROP_FROM_LEFT))
				.add(cropFromLeft).add(getLabel("")).add(getLabel(""))
				.add(getLabel(FormScannerTranslationKeys.CROP_FROM_RIGHT))
				.add(cropFromRight).add(getLabel("")).add(getLabel(""))
				.add(getLabel(FormScannerTranslationKeys.CROP_FROM_BOTTOM))
				.add(cropFromBottom).add(getLabel("")).add(getLabel(""))
				.withGrid(3, 6).build();
	}

	private JXTaskPane getScanOptionsPanel() {
		JPanel optionsPanel = getOptionsPanel();
		JPanel shapePanel = getShapePanel();
		JPanel cornerPanel = getCornerPanel();

		JPanel fullOptionsPanel = new PanelBuilder(orientation).withLayout(new SpringLayout()).add(optionsPanel)
				.add(shapePanel).add(cornerPanel).withGrid(3, 1).build();

		return new TaskPaneBuilder(orientation).withLayout(new BorderLayout())
				.withTitle(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.SCAN_OPTIONS))
				.add(fullOptionsPanel, BorderLayout.NORTH).build();
	}

	private JPanel getOptionsPanel() {
		thresholdValue = new SpinnerBuilder(FormScannerConstants.THRESHOLD, orientation)
				.withActionListener(leftPanelController).build();

		densityValue = new SpinnerBuilder(FormScannerConstants.DENSITY, orientation)
				.withActionListener(leftPanelController).build();

		return new PanelBuilder(orientation).withLayout(new SpringLayout())
				.withBorder(BorderFactory.createTitledBorder(
						FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.ACCURACY_OPTIONS)))
				.add(getLabel(FormScannerTranslationKeys.THRESHOLD_OPTION_LABEL)).add(thresholdValue)
				.add(getLabel(FormScannerTranslationKeys.DENSITY_OPTION_LABEL)).add(densityValue).withGrid(2, 2)
				.build();
	}

	private JPanel getShapePanel() {

		ShapeType shapes[] = ShapeType.values();
		types = new InternalShapeType[shapes.length];

		HashMap<Object, Icon> icons = new HashMap<>();
		for (ShapeType shape : shapes) {
			InternalShapeType internalShapeType = new InternalShapeType(shape);
			types[shape.getIndex()] = internalShapeType;
			icons.put(internalShapeType, FormScannerResources.getIconFor(shape.getName()));
		}

		shapeTypeComboBox = new ComboBoxBuilder<InternalShapeType>(FormScannerConstants.SHAPE_COMBO_BOX, orientation)
				.withModel(new DefaultComboBoxModel<>(types)).withRenderer(new IconListRenderer(icons))
				.withActionListener(leftPanelController).build();

		shapeSizeValue = new SpinnerBuilder(FormScannerConstants.SHAPE_SIZE, orientation)
				.withActionListener(leftPanelController).build();

		return new PanelBuilder(orientation).withLayout(new SpringLayout())
				.withBorder(BorderFactory.createTitledBorder(
						FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.MARKER_OPTIONS)))
				.add(getLabel(FormScannerTranslationKeys.SHAPE_TYPE_OPTION_LABEL)).add(shapeTypeComboBox)
				.add(getLabel(FormScannerTranslationKeys.SHAPE_SIZE_OPTION_LABEL)).add(shapeSizeValue).withGrid(2, 2)
				.build();
	}

	private JPanel getCornerPanel() {
		CornerType cornerTypes[] = CornerType.values();
		corners = new InternalCornerType[cornerTypes.length];

		HashMap<Object, Icon> icons = new HashMap<>();
		for (CornerType corner : cornerTypes) {
			InternalCornerType internalCornerType = new InternalCornerType(corner);
			corners[corner.getIndex()] = internalCornerType;
			icons.put(internalCornerType, FormScannerResources.getIconFor(corner.getName()));
		}

		cornerTypeComboBox = new ComboBoxBuilder<InternalCornerType>(FormScannerConstants.CORNER_TYPE_COMBO_BOX,
				orientation)
				.withModel(new DefaultComboBoxModel<>(corners))
				.withRenderer(new IconListRenderer(icons))
				.withActionListener(leftPanelController).build();

		return new PanelBuilder(orientation).withLayout(new SpringLayout())
				.withBorder(BorderFactory.createTitledBorder(
						FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.CORNERS_OPTIONS)))
				.add(getLabel(FormScannerTranslationKeys.CORNER_TYPE_OPTION_LABEL)).add(cornerTypeComboBox)
				.withGrid(1, 2).build();
	}

	private JLabel getLabel(String value) {
		return new LabelBuilder(FormScannerTranslation.getTranslationFor(value), orientation)
				.withBorder(BorderFactory.createEmptyBorder()).build();
	}
	
	public void verifySpinnerValues() {
		if ((Integer) thresholdValue.getValue() < 0) {
			thresholdValue.setValue(0);
		}
		if ((Integer) thresholdValue.getValue() > 255) {
			thresholdValue.setValue(255);
		}

		if ((Integer) densityValue.getValue() < 0) {
			densityValue.setValue(0);
		}
		if ((Integer) densityValue.getValue() > 100) {
			densityValue.setValue(100);
		}

		if ((Integer) shapeSizeValue.getValue() < 0) {
			shapeSizeValue.setValue(0);
		}
		if ((Integer) shapeSizeValue.getValue() > 100) {
			shapeSizeValue.setValue(100);
		}
		if ((Integer) cropFromTop.getValue() < 0) {
			shapeSizeValue.setValue(0);
		}
		if ((Integer) cropFromLeft.getValue() < 0) {
			shapeSizeValue.setValue(0);
		}
		if ((Integer) cropFromRight.getValue() < 0) {
			shapeSizeValue.setValue(0);
		}
		if ((Integer) cropFromBottom.getValue() < 0) {
			shapeSizeValue.setValue(0);
		}
	}

	public CornerType getCornerType() {
		return (corners[cornerTypeComboBox.getSelectedIndex()]).getType();
	}

	public ShapeType getShapeType() {
		return (types[shapeTypeComboBox.getSelectedIndex()]).getType();
	}

	public int getThresholdValue() {
		return (Integer) thresholdValue.getValue();
	}

	public int getDensityValue() {
		return (Integer) densityValue.getValue();
	}

	public int getShapeSize() {
		return (Integer) shapeSizeValue.getValue();
	}
	
	public HashMap<String, Integer> getCrop() {
		HashMap<String, Integer> crop = new HashMap<>();
		crop.put(FormScannerConstants.TOP, (Integer) cropFromTop.getValue());
		crop.put(FormScannerConstants.LEFT, (Integer) cropFromLeft.getValue());
		crop
				.put(
						FormScannerConstants.RIGHT,
						(Integer) cropFromRight.getValue());
		crop.put(
				FormScannerConstants.BOTTOM,
				(Integer) cropFromBottom.getValue());
		return crop;
	}
	
	public void togglePanel() {
		boolean collapsed = getDividerLocation() == getMinimumDividerLocation();
		if (!collapsed) {
			lastDividerLocation = getDividerLocation();
			getLeftComponent().setMinimumSize(new Dimension());
			setDividerLocation(0.0d);
		} else {
			setDividerLocation(lastDividerLocation);
		}
	}
}
