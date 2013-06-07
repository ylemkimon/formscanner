package org.albertoborsetta.formscanner.gui;

import org.albertoborsetta.formscanner.commons.FormPoint;
import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Corners;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Mode;
import org.albertoborsetta.formscanner.commons.FormScannerFont;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.gui.builder.LabelBuilder;
import org.albertoborsetta.formscanner.gui.builder.PanelBuilder;
import org.albertoborsetta.formscanner.gui.builder.StatusBarBuilder;
import org.albertoborsetta.formscanner.gui.controller.InternalFrameController;
import org.albertoborsetta.formscanner.gui.controller.ImageFrameController;
import org.albertoborsetta.formscanner.model.FormScannerModel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;

import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;


public class ImageFrame extends JInternalFrame implements ScrollableImageView {
	
	private static final long serialVersionUID = 1L;
	
	private FormScannerModel formScannerModel;
	private ImagePanel imagePanel;
	private ImageScrollPane scrollPane;
	private ImageFrameController controller;
	private InternalFrameController frameController;
	private Mode mode;
	private double rotation;

	private ImageStatusBar statusBar;
	

	/**
	 * Create the frame.
	 */	
	public ImageFrame(FormScannerModel model, File file, Mode mode) {
		formScannerModel = model;
		controller = new ImageFrameController(formScannerModel);
		controller.add(this);
		frameController = InternalFrameController.getInstance(formScannerModel);
		addInternalFrameListener(frameController);
		
		int desktopHeight = formScannerModel.getDesktopSize().height;
		int desktopWidth = formScannerModel.getDesktopSize().width;
		
		setClosable(true);
		setIconifiable(true);
		setResizable(true);
		setMaximizable(true);
		
		setName(FormScannerConstants.IMAGE_FRAME_NAME);	
		setTitle(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.IMAGE_FRAME_TITLE));					
		
		setBounds(320, 10, desktopWidth - 500, desktopHeight - 20);
		
		imagePanel = new ImagePanel(file);
		scrollPane = new ImageScrollPane(imagePanel);
		statusBar = new ImageStatusBar();
		getContentPane().add(statusBar.getStatusBar(), BorderLayout.SOUTH);
		getContentPane().add(scrollPane, BorderLayout.CENTER);		
		
		this.mode = mode;
	}
	
	private class ImageStatusBar {
		
		private JLabel rotationValue;
		private HashMap<Corners, JLabel> cornerValues = new HashMap<Corners, JLabel>();
		private JPanel statusBar;
		
		public ImageStatusBar() {
			JLabel rotationLabel = new LabelBuilder(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.ROTATION_LABEL) + ": ")
					.build();
			rotationValue = new LabelBuilder(rotation + "")
					.withName("rotation")
					.withBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null))
					.build();
			
			JPanel rotationField = new PanelBuilder().withBorderLayout()
					.add(rotationLabel, BorderLayout.WEST)
					.add(rotationValue, BorderLayout.CENTER)
					.build();
			
			JLabel cornerLabel = new LabelBuilder("corners: ")
					.build();
			
			StatusBarBuilder statusBarBuilder = new StatusBarBuilder();
			
			for (int index=Corners.values().length-1; index>=0; index--) {
				double xCoord = imagePanel.getCorner(Corners.values()[index]).getX();
				double yCoord = imagePanel.getCorner(Corners.values()[index]).getY();
				String position = "(" + xCoord + "," + yCoord + ")";
				JLabel cornerValue = new LabelBuilder(position)
						.withName(Corners.values()[index].name())
						.withBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null))
						.build();
				cornerValues.put(Corners.values()[index], cornerValue);
				statusBarBuilder.add(cornerValue);
			}
			statusBarBuilder.add(cornerLabel);
			statusBarBuilder.add(rotationValue);
			statusBar = statusBarBuilder.add(rotationField).build();
		}
		
		public JPanel getStatusBar() {
			return statusBar;
		}
		
		public void updateRotation() {
			rotationValue.setText(rotation + "");
		}
		
		public void updateCorners() {
			for (Corners corner: Corners.values()) {
				double xCoord = imagePanel.getCorner(corner).getX();
				double yCoord = imagePanel.getCorner(corner).getY();
				String position = "(" + xCoord + "," + yCoord + ")";
				cornerValues.get(corner).setText(position);
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
			horizontalScrollBar.setValue(horizontalScrollBar.getValue() + deltaX);
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
		private double scaleFactor = 1;
		private List<FormPoint> points = new ArrayList<FormPoint>();
		private HashMap<Corners, FormPoint> corners = new HashMap<Corners, FormPoint>();
		private BufferedImage image;
		
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		
		public ImagePanel(File file) {
			super();
			setImage(file);
			setFont(FormScannerFont.getFont());
			scaleFactor = 0.5;
		}
		
		public HashMap<Corners, FormPoint> getCorners() {
			return corners;
		}
		
		public FormPoint getCorner(Corners position) {
			FormPoint p = new FormPoint(0, 0); 
			if (!corners.isEmpty()) {
				p = corners.get(position);
			} 
			return p;
		}

		@Override
	    public void paintComponent(Graphics g) {
			width = (int) Math.floor(image.getWidth() * scaleFactor);
			height = (int) Math.floor(image.getHeight() * scaleFactor);
			setPreferredSize(new Dimension(width, height));
			g.drawImage(image, 0, 0, width, height, this);
			showPoints(g);
			showCorners(g);
	    } 
		
		public void showPoints(Graphics g) {
			g.setColor(Color.RED);
			for (FormPoint p: points) {
				FormPoint p1 = calculatePointPosition(p);
				g.drawLine(p1.x-5, p1.y-5, p1.x+5, p1.y+5);
				g.drawLine(p1.x-5, p1.y+5, p1.x+5, p1.y-5);
			}
			g.setColor(Color.BLACK);
		}
		
		public void showCorners(Graphics g) {
			g.setColor(Color.GREEN);
			for (Entry<Corners, FormPoint> corner: corners.entrySet()) {
				FormPoint p1 = calculatePointPosition(corner.getValue());
				g.drawLine(p1.x-5, p1.y-5, p1.x+5, p1.y+5);
				g.drawLine(p1.x-5, p1.y+5, p1.x+5, p1.y-5);
			}
			g.setColor(Color.BLACK);
		}
		
		private FormPoint calculatePointPosition(FormPoint p) {
			int dx = scrollPane.getHorizontalScrollBarValue();
			int dy = scrollPane.getVerticalScrollBarValue();
			
			int x = (int) Math.floor((p.x-dx)*scaleFactor);
			int y = (int) Math.floor((p.y-dy)*scaleFactor);
			
			FormPoint p1 = new FormPoint(x, y);
			return p1;
		}
		
		public void setImage(File file) {
			try {		
				image = ImageIO.read(file);
			} catch (IOException ex) {
				image = null;
			}
		}
		
		public double getScaleFactor() {
			return scaleFactor;
		}
		
		public void setScaleFactor(double scaleFactor) {
			this.scaleFactor = scaleFactor;
		}
		
		public void addPoint(FormPoint p) {
			points.add(p);
		}

		public void removePoint(FormPoint p) {
			points.remove(p);
		}

		public void removeAllPoints() {
			points.clear();
		}

		public List<FormPoint> getPoints() {
			return points;
		}

		public void addCorner(Corners position, FormPoint corner) {
			corners.put(position, corner);			
		}

		public void addCorners(HashMap<Corners, FormPoint> corners) {
			this.corners = corners;
		}

		public void addPoints(List<FormPoint> points) {
			this.points = points;
		}
		
		public int getImageWidth() {
			return width;
		}
		
		public int getImageHeight() {
			return height;
		}
	}
	
	public void updateImage(File file) {
		imagePanel.setImage(file);
		update(getGraphics());
	}
	
	public void addPoint(FormPoint point) {
		imagePanel.addPoint(point);
		update(getGraphics());
	}
	
	public void removePoint(FormPoint point) {
		imagePanel.removePoint(point);
		update(getGraphics());
	}
	
	public void removeAllPoints() {
		imagePanel.removeAllPoints();
	}

	public void setImageCursor(Cursor cursor) {
		imagePanel.setCursor(cursor);
	}

	public void setScrollBars(int deltaX, int deltaY) {
		scrollPane.setScrollBars(deltaX, deltaY);
		update(getGraphics());
	}
	
	public void zoomImage(double zoom) {
		double scaleFactor = imagePanel.getScaleFactor();
		imagePanel.setScaleFactor(scaleFactor + zoom);
		update(getGraphics());
	}

	public double getScaleFactor() {
		return imagePanel.getScaleFactor();
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

	public List<FormPoint> getPoints() {
		return imagePanel.getPoints();
	}

	public void addCorner(Corners position, FormPoint corner) {
		imagePanel.addCorner(position, corner);
		update(getGraphics());
	}

	public void addCorners(HashMap<Corners, FormPoint> corners) {
		imagePanel.addCorners(corners);
		update(getGraphics());
		statusBar.updateCorners();
	}

	public void addPoints(List<FormPoint> points) {
		imagePanel.addPoints(points);
		update(getGraphics());
	}
	
	public void setRotation(double rotation) {
		this.rotation = rotation;
		statusBar.updateRotation();
	}

	public Dimension getImageSize() {
		return new Dimension(imagePanel.getImageWidth(), imagePanel.getImageHeight());
	}
	
	private void updateGraphics() {
		Graphics g = getGraphics();		
		update(g);
		g.dispose();
	}
}