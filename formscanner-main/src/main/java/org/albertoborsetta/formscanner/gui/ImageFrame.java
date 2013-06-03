package org.albertoborsetta.formscanner.gui;

import org.albertoborsetta.formscanner.commons.FormPoint;
import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Corners;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Mode;
import org.albertoborsetta.formscanner.commons.FormScannerFont;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.gui.controller.InternalFrameController;
import org.albertoborsetta.formscanner.gui.controller.ImageFrameController;
import org.albertoborsetta.formscanner.model.FormScannerModel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.BorderLayout;
import java.awt.SystemColor;
import java.awt.image.BufferedImage;

import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
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
		getContentPane().add(statusBar, BorderLayout.SOUTH);
		getContentPane().add(scrollPane, BorderLayout.CENTER);		
		
		this.mode = mode;
	}
	
	private class ImageStatusBar extends JPanel {
		
		private class AngledLinesWindowsCornerIcon implements Icon {
			  private Color WHITE_LINE_COLOR = new Color(255, 255, 255);

			  private Color GRAY_LINE_COLOR = new Color(172, 168, 153);
			  private static final int WIDTH = 13;

			  private static final int HEIGHT = 13;

			  public int getIconHeight() {
			    return WIDTH;
			  }

			  public int getIconWidth() {
			    return HEIGHT;
			  }

			  public void paintIcon(Component c, Graphics g, int x, int y) {

			    g.setColor(WHITE_LINE_COLOR);
			    g.drawLine(0, 12, 12, 0);
			    g.drawLine(5, 12, 12, 5);
			    g.drawLine(10, 12, 12, 10);

			    g.setColor(GRAY_LINE_COLOR);
			    g.drawLine(1, 12, 12, 1);
			    g.drawLine(2, 12, 12, 2);
			    
			    g.drawLine(6, 12, 12, 6);
			    g.drawLine(7, 12, 12, 7);
			    
			    g.drawLine(11, 12, 12, 11);
			    g.drawLine(12, 12, 12, 12);

			  }
			}
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ImageStatusBar() {
			super();
			setFont(FormScannerFont.getFont());
			setLayout(new BorderLayout());
		    setPreferredSize(new Dimension(10, 23));

		    JPanel rightPanel = new JPanel(new BorderLayout());
		    rightPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		    rightPanel.add(new JLabel(new AngledLinesWindowsCornerIcon()), BorderLayout.SOUTH);
		    rightPanel.setOpaque(false);

		    add(rightPanel, BorderLayout.EAST);
		    
		    JPanel centerPanel = new JPanel(new BorderLayout());
		    // centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.X_AXIS));
		    centerPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 0));
		    centerPanel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		    JLabel lblNewLabel = new JLabel("New label");
		    centerPanel.add(lblNewLabel);
			
			JTextField textField = new JTextField("uno");
			textField.setColumns(10);
			centerPanel.add(textField);
			
			JSeparator separator = new JSeparator();
			separator.setOrientation(SwingConstants.VERTICAL);
			centerPanel.add(separator);
			
			JLabel lblNewLabel_1 = new JLabel("New label");
			centerPanel.add(lblNewLabel_1);
			
			JTextField textField_1 = new JTextField("due");
			textField_1.setColumns(10);
			centerPanel.add(textField_1);
			
			JSeparator separator_1 = new JSeparator();
			separator_1.setOrientation(SwingConstants.VERTICAL);
			centerPanel.add(separator_1);
			
			add(centerPanel, BorderLayout.CENTER);
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
		private HashMap<Corners, FormPoint> corners;
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
	}

	public void addPoints(List<FormPoint> points) {
		imagePanel.addPoints(points);
		update(getGraphics());
	}
	
	public void setRotation(double rotation) {
		this.rotation = rotation;
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