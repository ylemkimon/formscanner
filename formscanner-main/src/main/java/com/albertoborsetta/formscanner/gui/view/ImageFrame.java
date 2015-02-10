package com.albertoborsetta.formscanner.gui.view;

import com.albertoborsetta.formscanner.api.FormPoint;
import com.albertoborsetta.formscanner.api.FormTemplate;
import com.albertoborsetta.formscanner.api.commons.Constants.Corners;
import com.albertoborsetta.formscanner.commons.FormScannerConstants;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.Zoom;
import com.albertoborsetta.formscanner.commons.FormScannerFont;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.Frame;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.Mode;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.ShapeType;
import com.albertoborsetta.formscanner.commons.resources.FormScannerResources;
import com.albertoborsetta.formscanner.commons.resources.FormScannerResourcesKeys;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import com.albertoborsetta.formscanner.gui.builder.ButtonBuilder;
import com.albertoborsetta.formscanner.gui.builder.ComboBoxBuilder;
import com.albertoborsetta.formscanner.gui.builder.LabelBuilder;
import com.albertoborsetta.formscanner.gui.builder.PanelBuilder;
import com.albertoborsetta.formscanner.gui.builder.TextFieldBuilder;
import com.albertoborsetta.formscanner.gui.builder.ToolBarBuilder;
import com.albertoborsetta.formscanner.gui.controller.ImageFrameController;
import com.albertoborsetta.formscanner.gui.model.FormScannerModel;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.SpringLayout;
import javax.swing.UIManager;
import javax.swing.border.EtchedBorder;

import java.util.HashMap;
import java.util.Map.Entry;

public class ImageFrame extends InternalFrame implements ScrollableImageView {

	private static final long serialVersionUID = 1L;

	private ImagePanel imagePanel;
	private ImageScrollPane scrollPane;
	private ImageFrameController controller;
	private ImageStatusBar statusBar;
	private Mode mode;
	private FormTemplate template;
	private JComboBox<InternalZoom> zoomComboBox;
	private ImageToolBar toolBar;
	private InternalZoom zoomValue = new InternalZoom(Zoom.ZOOM_100);;

	private class InternalZoom {

		private Zoom zoom;

		protected InternalZoom(Zoom zoom) {
			this.zoom = zoom;
		}

		protected Zoom getZoom() {
			return zoom;
		}

		public String toString() {
			String value;
			switch (zoom) {
			case ZOOM_WIDTH:
				value = FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.FIT_WIDTH);
				break;
			case ZOOM_PAGE:
				value = FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.FIT_PAGE);
				break;
			default:
				value = zoom.getValue().toString();
				break;
			}
			return value;
		}
	}

	/**
	 * Create the frame.
	 */
	public ImageFrame(FormScannerModel model, BufferedImage image,
			FormTemplate template, Mode mode) {
		super(model);
		this.mode = mode;
		this.template = template;
		controller = new ImageFrameController(this.model);
		controller.add(this);

		setBounds(model.getLastPosition(Frame.IMAGE_FRAME));
		setMinimumSize(new Dimension(300, 500));
		setClosable(true);
		setIconifiable(true);
		setResizable(true);
		setMaximizable(true);

		setName(Frame.IMAGE_FRAME.name());
		setTitle(FormScannerTranslation
				.getTranslationFor(FormScannerTranslationKeys.IMAGE_FRAME_TITLE));

		imagePanel = new ImagePanel(image);
		scrollPane = new ImageScrollPane(imagePanel);
		statusBar = new ImageStatusBar(this.mode);
		toolBar = new ImageToolBar();
		getContentPane().add(toolBar, BorderLayout.NORTH);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		getContentPane().add(statusBar.getPanel(), BorderLayout.SOUTH);
	}

	private class ImageToolBar extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private InternalZoom[] zoom;

		public ImageToolBar() {
			setBorder(new EtchedBorder(EtchedBorder.RAISED, null, null));
			setComponentOrientation(orientation);

			if (orientation.isLeftToRight()) {
				setLayout(new FlowLayout(FlowLayout.LEFT));
			} else {
				setLayout(new FlowLayout(FlowLayout.RIGHT));
			}

			JToolBar zoomToolBar = getZoomToolBar();
			add(zoomToolBar);
		}

		public JToolBar getZoomToolBar() {

			Zoom zoomValues[] = Zoom.values();
			zoom = new InternalZoom[zoomValues.length];
			for (int i = 0; i < zoomValues.length; i++) {
				zoom[i] = new InternalZoom(zoomValues[i]);
				if (zoomValues[i].equals(Zoom.ZOOM_100)) {
					zoomValue = zoom[i];
				}
			}

			JLabel zoomLabel = new LabelBuilder(
					FormScannerTranslation
							.getTranslationFor(FormScannerTranslationKeys.ZOOM_LABEL),
					orientation).withBorder(BorderFactory.createEmptyBorder()).build();

			zoomComboBox = new ComboBoxBuilder<InternalZoom>(
					FormScannerConstants.ZOOM_COMBO_BOX, orientation)
					.withModel(new DefaultComboBoxModel<InternalZoom>(zoom))
					.withActionListener(controller).build();

			zoomComboBox.setSelectedItem(zoomValue);
			
			JPanel zoomPanel = new PanelBuilder(ComponentOrientation.LEFT_TO_RIGHT)
			.withLayout(new SpringLayout())
			.withGrid(1, 2).add(zoomLabel).add(zoomComboBox).build();

			return new ToolBarBuilder(orientation)
					.withAlignmentY(Component.CENTER_ALIGNMENT)
					.withAlignmentX(Component.LEFT_ALIGNMENT).add(zoomPanel)
					.build();
		}
	}

	private class ImageStatusBar extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private JTextField XPositionValue;
		private JTextField YPositionValue;
		private HashMap<Corners, JButton> cornerButtons = new HashMap<Corners, JButton>();
		private HashMap<Corners, JTextField> cornerPositions = new HashMap<Corners, JTextField>();
		private JPanel panel;

		public ImageStatusBar(Mode mode) {
			XPositionValue = getTextField();
			YPositionValue = getTextField();

			setCornerPositions();
			showCornerPosition();

			setCornerButtons(mode);

			panel = new PanelBuilder(ComponentOrientation.LEFT_TO_RIGHT)
					.withLayout(new SpringLayout())
					.withGrid(2, 6)
					.add(getLabel(FormScannerTranslationKeys.X_CURSOR_POSITION_LABEL))
					.add(XPositionValue)
					.add(cornerButtons.get(Corners.TOP_LEFT))
					.add(cornerPositions.get(Corners.TOP_LEFT))
					.add(cornerButtons.get(Corners.TOP_RIGHT))
					.add(cornerPositions.get(Corners.TOP_RIGHT))
					.add(getLabel(FormScannerTranslationKeys.Y_CURSOR_POSITION_LABEL))
					.add(YPositionValue)
					.add(cornerButtons.get(Corners.BOTTOM_LEFT))
					.add(cornerPositions.get(Corners.BOTTOM_LEFT))
					.add(cornerButtons.get(Corners.BOTTOM_RIGHT))
					.add(cornerPositions.get(Corners.BOTTOM_RIGHT)).build();
		}

		public JPanel getPanel() {
			return panel;
		}

		private void setCornerButtons(Mode mode) {

			cornerButtons
					.put(Corners.TOP_LEFT,
							getButtonBuilder(mode)
									.withText(
											FormScannerTranslation
													.getTranslationFor(FormScannerTranslationKeys.TOP_LEFT_CORNER))
									.withToolTip(
											FormScannerTranslation
													.getTranslationFor(FormScannerTranslationKeys.TOP_LEFT_CORNER_TOOLTIP))
									.withActionCommand(
											FormScannerConstants.TOP_LEFT)
									.build());
			cornerButtons
					.put(Corners.BOTTOM_LEFT,
							getButtonBuilder(mode)
									.withText(
											FormScannerTranslation
													.getTranslationFor(FormScannerTranslationKeys.BOTTOM_LEFT_CORNER))
									.withToolTip(
											FormScannerTranslation
													.getTranslationFor(FormScannerTranslationKeys.BOTTOM_LEFT_CORNER_TOOLTIP))
									.withActionCommand(
											FormScannerConstants.BOTTOM_LEFT)
									.build());
			cornerButtons
					.put(Corners.TOP_RIGHT,
							getButtonBuilder(mode)
									.withText(
											FormScannerTranslation
													.getTranslationFor(FormScannerTranslationKeys.TOP_RIGHT_CORNER))
									.withToolTip(
											FormScannerTranslation
													.getTranslationFor(FormScannerTranslationKeys.TOP_RIGHT_CORNER_TOOLTIP))
									.withActionCommand(
											FormScannerConstants.TOP_RIGHT)
									.build());
			cornerButtons
					.put(Corners.BOTTOM_RIGHT,
							getButtonBuilder(mode)
									.withText(
											FormScannerTranslation
													.getTranslationFor(FormScannerTranslationKeys.BOTTOM_RIGHT_CORNER))
									.withToolTip(
											FormScannerTranslation
													.getTranslationFor(FormScannerTranslationKeys.BOTTOM_RIGHT_CORNER_TOOLTIP))
									.withActionCommand(
											FormScannerConstants.BOTTOM_RIGHT)
									.build());
		}

		private void setCornerPositions() {
			cornerPositions.put(Corners.TOP_LEFT, getTextField());
			cornerPositions.put(Corners.BOTTOM_LEFT, getTextField());
			cornerPositions.put(Corners.TOP_RIGHT, getTextField());
			cornerPositions.put(Corners.BOTTOM_RIGHT, getTextField());
		}

		private JTextField getTextField() {
			return new TextFieldBuilder(10, orientation).setEditable(false)
					.build();
		}

		private ButtonBuilder getButtonBuilder(Mode mode) {
			return new ButtonBuilder(orientation)
					.withActionListener(controller)
					.withIcon(
							FormScannerResources
									.getIconFor(FormScannerResourcesKeys.DISABLED_BUTTON))
					.withSelectedIcon(
							FormScannerResources
									.getIconFor(FormScannerResourcesKeys.ENABLED_BUTTON))
					.setAlignment().setEnabled(mode != Mode.VIEW)
					.setSelected(false);
		}

		private JLabel getLabel(String value) {
			return new LabelBuilder(
					FormScannerTranslation.getTranslationFor(value),
					orientation).withBorder(BorderFactory.createEmptyBorder())
					.build();
		}

		public void setCursorPosition(FormPoint p) {
			XPositionValue.setText("" + p.getX());
			YPositionValue.setText("" + p.getY());
		}

		public void toggleCornerButton(Corners corner) {
			for (Entry<Corners, JButton> entryCorner : cornerButtons.entrySet()) {
				JButton button = entryCorner.getValue();

				if (entryCorner.getKey().equals(corner)) {
					button.setSelected(!button.isSelected());
				} else {
					if (button.isSelected()) {
						button.setSelected(false);
					}
				}
			}
		}

		public Corners getSelectedButton() {
			for (Entry<Corners, JButton> entryCorner : cornerButtons.entrySet()) {
				JButton button = entryCorner.getValue();

				if (button.isSelected()) {
					return entryCorner.getKey();
				}
			}
			return null;
		}

		public void resetCornerButtons() {
			for (Entry<Corners, JButton> entryCorner : cornerButtons.entrySet()) {
				JButton button = entryCorner.getValue();

				button.setSelected(false);
			}
		}

		public void setCornerButtonsEnabled(Mode mode) {
			for (Entry<Corners, JButton> entryCorner : cornerButtons.entrySet()) {
				JButton button = entryCorner.getValue();
				button.setEnabled(mode != Mode.VIEW);
			}
		}

		public void showCornerPosition() {
			for (Entry<Corners, JTextField> entryCorner : cornerPositions
					.entrySet()) {
				JTextField cornerPosition = entryCorner.getValue();

				cornerPosition.setText(template.getCorner(entryCorner.getKey())
						.toString());
			}
		}
	}

	private class ImageScrollPane extends JScrollPane {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ImageScrollPane(JPanel imagePanel) {
			super(imagePanel);
			verticalScrollBar.setValue(0);
			horizontalScrollBar.setValue(0);
			setWheelScrollingEnabled(false);
			addMouseMotionListener(controller);
			addMouseListener(controller);
			addMouseWheelListener(controller);
		}

		public void setScrollBars(int deltaX, int deltaY) {
			horizontalScrollBar.setValue(horizontalScrollBar.getValue()
					+ deltaX);
			verticalScrollBar.setValue(verticalScrollBar.getValue() + deltaY);
		}

		public int getHorizontalScrollBarValue() {
			return horizontalScrollBar.getValue();
		}

		public int getVerticalScrollBarValue() {
			return verticalScrollBar.getValue();
		}
	}

	private class ImagePanel extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private int imageWidth;
		private int imageHeight;
		private int marker;
		private ShapeType markerType;
		private int border = 1;
		private BufferedImage image;
		private FormPoint temporaryPoint;
		private Double zoom;

		public ImagePanel(BufferedImage image) {
			super();
			setImage(image);
			setFont(FormScannerFont.getFont());
			if ("Nimbus".equals(UIManager.getLookAndFeel().getName())) {
				border = 4;
			}
			marker = model.getShapeSize();
			markerType = model.getShapeType();
			imageWidth = image.getWidth();
			imageHeight = image.getHeight();
			zoom = 1d;
		}

		public void setZoom(Double zoom) {
			this.zoom = zoom;
		}

		@Override
		public void paintComponent(Graphics g) {
			marker = (int) (model.getShapeSize() * zoom);
			int width = (int) (imageWidth * zoom);
			int height = (int) (imageHeight * zoom);
			setPreferredSize(new Dimension(width, height));
			g.drawImage(image, 0, 0, width, height, this);
			showPoints(g);
			showCorners(g);
		}

		private void showPoints(Graphics g) {

			for (FormPoint point : template.getFieldPoints()) {
				showPoint(g, point);
			}

			if (!model.getPoints().isEmpty()) {
				for (FormPoint point : model.getPoints()) {
					showPoint(g, point);
				}
			}

			showPoint(g, temporaryPoint);
		}

		private void showPoint(Graphics g, FormPoint point) {
			if (point != null) {
				int x = (int) ((point.getX() * zoom) - border);
				int y = (int) ((point.getY() * zoom) - border);

				g.setColor(Color.RED);
				if (markerType.equals(ShapeType.CIRCLE)) {
					g.fillArc(x - marker, y - marker, 2 * marker, 2 * marker,
							0, 360);
				} else {
					g.fillRect(x - marker, y - marker, 2 * marker, 2 * marker);
				}
				g.setColor(Color.BLACK);
			}
		}

		public void showCorners(Graphics g) {
			HashMap<Corners, FormPoint> corners = template.getCorners();
			if (corners.isEmpty()) {
				return;
			}
			g.setColor(Color.GREEN);

			for (int i = 0; i < Corners.values().length; i++) {
				FormPoint p1 = corners.get(Corners.values()[i
						% Corners.values().length]);
				FormPoint p2 = corners.get(Corners.values()[(i + 1)
						% Corners.values().length]);

				g.drawLine((int) (p1.getX() * zoom), (int) (p1.getY() * zoom),
						(int) (p2.getX() * zoom), (int) (p2.getY() * zoom));
			}

			g.setColor(Color.BLACK);
		}

		private void setImage(BufferedImage image) {
			this.image = image;
		}

		public int getImageWidth() {
			return imageWidth;
		}

		public int getImageHeight() {
			return imageHeight;
		}

		public void setTemporaryPoint(FormPoint p) {
			temporaryPoint = p;
		}

		public void clearTemporaryPoint() {
			temporaryPoint = null;
		}
	}

	public void updateImage(BufferedImage file) {
		imagePanel.setImage(file);
		repaint();
	}

	public void updateImage(BufferedImage file, FormTemplate filledForm) {
		imagePanel.setImage(file);
		template = filledForm;
		repaint();
	}

	public void setImageCursor(Cursor cursor) {
		imagePanel.setCursor(cursor);
	}

	public void setScrollBars(int deltaX, int deltaY) {
		scrollPane.setScrollBars(deltaX, deltaY);
		repaint();
	}

	public int getHorizontalScrollbarValue() {
		return (int) (scrollPane.getHorizontalScrollBarValue() / getZoom());
	}

	public int getVerticalScrollbarValue() {
		return (int) (scrollPane.getVerticalScrollBarValue() / getZoom());
	}

	public Mode getMode() {
		return mode;
	}

	public void showCursorPosition(FormPoint p) {
		statusBar.setCursorPosition(p);
	}

	public void setTemporaryPoint(FormPoint p) {
		imagePanel.setTemporaryPoint(p);
	}

	public void toggleCornerButton(Corners corner) {
		statusBar.toggleCornerButton(corner);
	}

	public Corners getSelectedButton() {
		return statusBar.getSelectedButton();
	}

	public void resetCornerButtons() {
		statusBar.resetCornerButtons();
	}

	public void showCornerPosition() {
		statusBar.showCornerPosition();
	}

	public FormTemplate getTemplate() {
		return template;
	}

	public void clearTemporaryPoint() {
		imagePanel.clearTemporaryPoint();
	}

	public void setMode(Mode mode) {
		statusBar.setCornerButtonsEnabled(mode);
		this.mode = mode;
	}

	public void setZoom() {
		imagePanel.setZoom(getZoom());
		repaint();
	}

	public Double getZoom() {
		zoomValue = (InternalZoom) zoomComboBox.getSelectedItem();
		Double value = zoomValue.getZoom().getValue() / 100d;
		switch (zoomValue.getZoom()) {
		case ZOOM_WIDTH:
			value = scrollPane.getWidth()
					/ ((double) imagePanel.getImageWidth());
			break;
		case ZOOM_PAGE:
			value = Math.min(
					scrollPane.getHeight()
							/ ((double) imagePanel.getImageHeight()),
					scrollPane.getWidth()
							/ ((double) imagePanel.getImageWidth()));
			break;
		default:
			break;
		}
		return value;
	}
}