package org.albertoborsetta.formscanner.gui;

import org.albertoborsetta.formscanner.commons.FormPoint;
import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Corners;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Mode;
import org.albertoborsetta.formscanner.commons.FormScannerFont;
import org.albertoborsetta.formscanner.commons.FormTemplate;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.gui.controller.InternalFrameController;
import org.albertoborsetta.formscanner.gui.controller.ImageFrameController;
import org.albertoborsetta.formscanner.model.FormScannerModel;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class ImageFrame extends JInternalFrame implements ScrollableImageView {

	private static final long serialVersionUID = 1L;

	private FormScannerModel model;
	private ImagePanel imagePanel;
	private ImageScrollPane scrollPane;
	private ImageFrameController controller;
	private InternalFrameController frameController;
	private Mode mode;

	/**
	 * Create the frame.
	 */
	public ImageFrame(FormScannerModel model, File image, Mode mode) {
		this.model = model;
		this.mode = mode;
		controller = new ImageFrameController(this.model);
		controller.add(this);
		frameController = InternalFrameController.getInstance(this.model);
		addInternalFrameListener(frameController);

		int desktopHeight = model.getDesktopSize().height;
		int desktopWidth = model.getDesktopSize().width;

		setClosable(true);
		setIconifiable(true);
		setResizable(true);
		setMaximizable(true);

		setName(FormScannerConstants.IMAGE_FRAME_NAME);
		setTitle(FormScannerTranslation
				.getTranslationFor(FormScannerTranslationKeys.IMAGE_FRAME_TITLE));

		setBounds(320, 10, desktopWidth - 500, desktopHeight - 20);

		imagePanel = new ImagePanel(image);
		scrollPane = new ImageScrollPane(imagePanel);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
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

		private int width;
		private int height;
		private int marker = 20;
		private BufferedImage image;
		private FormTemplate template;

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ImagePanel(File image) {
			super();
			setImage(image);
			setFont(FormScannerFont.getFont());
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

		public void showPoints(Graphics g) {
			g.setColor(Color.RED);

			for (FormPoint point : template.getFieldPoints()) {
				for (int i = 0; i < 2; i++) {
					int d = (int) (Math.pow(-1, i) * marker);
					g.drawLine((int) point.getX()-d, (int) point.getY()+d, (int) point.getX()+d, 
							(int) point.getY()+d);
					g.drawLine((int) point.getX()+d, (int) point.getY()+d, (int) point.getX()+d, 
							(int) point.getY()-d);
				}
			}

			if (!model.getPoints().isEmpty()) {
				for (FormPoint point : model.getPoints()) {
					for (int i = 0; i < 2; i++) {
						int d = (int) (Math.pow(-1, i) * marker);
						g.drawLine((int) point.getX()-d, (int) point.getY()+d, (int) point.getX()+d,
								(int) point.getY()+d);
						g.drawLine((int) point.getX()+d, (int) point.getY()+d, (int) point.getX()+d,
								(int) point.getY()-d);
					}
				}
			}

			g.setColor(Color.BLACK);
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

				g.drawLine((int) p1.getX(), (int) p1.getY(), (int) p2.getX(), (int) p2.getY());
			}

			g.setColor(Color.BLACK);
		}

		private void setImage(File file) {
			try {
				image = ImageIO.read(file);
			} catch (IOException ex) {
				image = null;
			}
		}

		public int getImageWidth() {
			return width;
		}

		public int getImageHeight() {
			return height;
		}

		public void setTemplate(FormTemplate template) {
			this.template = template;
		}
	}

	public void updateImage(File file) {
		imagePanel.setImage(file);
		update();
	}

	public void setImageCursor(Cursor cursor) {
		imagePanel.setCursor(cursor);
	}

	public void setScrollBars(int deltaX, int deltaY) {
		scrollPane.setScrollBars(deltaX, deltaY);
		update();
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

	public void setTemplate(FormTemplate template) {
		imagePanel.setTemplate(template);
	}

	public void update() {
		update(getGraphics());
	}
}