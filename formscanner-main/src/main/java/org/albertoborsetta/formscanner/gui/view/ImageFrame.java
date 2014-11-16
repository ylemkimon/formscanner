package org.albertoborsetta.formscanner.gui.view;

import org.albertoborsetta.formscanner.api.FormPoint;
import org.albertoborsetta.formscanner.api.FormTemplate;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.api.commons.Constants.Corners;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Frame;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Mode;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.ShapeType;
import org.albertoborsetta.formscanner.commons.FormScannerFont;
import org.albertoborsetta.formscanner.commons.resources.FormScannerResources;
import org.albertoborsetta.formscanner.commons.resources.FormScannerResourcesKeys;
import org.albertoborsetta.formscanner.gui.builder.ButtonBuilder;
import org.albertoborsetta.formscanner.gui.builder.LabelBuilder;
import org.albertoborsetta.formscanner.gui.builder.TextFieldBuilder;
import org.albertoborsetta.formscanner.gui.controller.ImageFrameController;
import org.albertoborsetta.formscanner.gui.model.FormScannerModel;
import org.uncommons.swing.SpringUtilities;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.UIManager;

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
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		getContentPane().add(statusBar, BorderLayout.SOUTH);
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

		public ImageStatusBar(Mode mode) {
			super();
			SpringLayout layout = new SpringLayout();
			setLayout(layout);

			XPositionValue = getTextField();
			YPositionValue = getTextField();

			setCornerPositions();
			showCornerPosition();

			setCornerButtons(mode);

			add(getLabel(FormScannerTranslationKeys.X_CURSOR_POSITION_LABEL));
			add(XPositionValue);
			add(cornerButtons.get(Corners.TOP_LEFT));
			add(cornerPositions.get(Corners.TOP_LEFT));
			add(cornerButtons.get(Corners.TOP_RIGHT));
			add(cornerPositions.get(Corners.TOP_RIGHT));
			add(getLabel(FormScannerTranslationKeys.Y_CURSOR_POSITION_LABEL));
			add(YPositionValue);
			add(cornerButtons.get(Corners.BOTTOM_LEFT));
			add(cornerPositions.get(Corners.BOTTOM_LEFT));
			add(cornerButtons.get(Corners.BOTTOM_RIGHT));
			add(cornerPositions.get(Corners.BOTTOM_RIGHT));

			SpringUtilities.makeCompactGrid(this, 2, 6, 3, 3, 3, 3);
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
			return new TextFieldBuilder(10).setEditable(false).build();
		}

		private ButtonBuilder getButtonBuilder(Mode mode) {
			return new ButtonBuilder()
					.withActionListener(controller)
					.withIcon(
							FormScannerResources
									.getIconFor(FormScannerResourcesKeys.DISABLED_BUTTON))
					.withSelectedIcon(
							FormScannerResources
									.getIconFor(FormScannerResourcesKeys.ENABLED_BUTTON))
					.withLeftAlignment().setEnabled(mode != Mode.VIEW)
					.setSelected(false);
		}

		private JLabel getLabel(String value) {
			return new LabelBuilder(
					FormScannerTranslation.getTranslationFor(value))
					.withBorder(BorderFactory.createEmptyBorder()).build();
		}

		public void setCursorPosition(FormPoint p) {
			XPositionValue.setText("" + (int) p.getX());
			YPositionValue.setText("" + (int) p.getY());
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

		private int width;
		private int height;
		private int marker;
		private ShapeType markerType;
		private int border = 1;
		private BufferedImage image;
		private FormPoint temporaryPoint;

		public ImagePanel(BufferedImage image) {
			super();
			setImage(image);
			setFont(FormScannerFont.getFont());
			if ("Nimbus".equals(UIManager.getLookAndFeel().getName())) {
				border = 4;
			}
			marker = model.getShapeSize();
			markerType = model.getShapeType();
		}

		@Override
		public void paintComponent(Graphics g) {
			width = image.getWidth();
			height = image.getHeight();
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
				int x = (int) point.getX() - border;
				int y = (int) point.getY() - border;

				g.setColor(Color.RED);
				if (markerType.equals(ShapeType.CIRCLE)) {
					g.fillArc(x - marker, y - marker, 2 * marker, 2 * marker, 0,
						360);
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

				g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(),
						(int) p2.getY());
			}

			g.setColor(Color.BLACK);
		}

		private void setImage(BufferedImage image) {
			this.image = image;
		}

		public int getImageWidth() {
			return width;
		}

		public int getImageHeight() {
			return height;
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
		return scrollPane.getHorizontalScrollBarValue();
	}

	public int getVerticalScrollbarValue() {
		return scrollPane.getVerticalScrollBarValue();
	}

	public Mode getMode() {
		return mode;
	}

	public Dimension getImageSize() {
		return new Dimension(imagePanel.getImageWidth(),
				imagePanel.getImageHeight());
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
}