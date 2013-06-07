package org.albertoborsetta.formscanner.gui.builder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;

import org.albertoborsetta.formscanner.commons.FormScannerFont;

public class StatusBarBuilder extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JPanel internalPanel;

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

	public StatusBarBuilder() {
		internalPanel = new JPanel();
		internalPanel.setFont(FormScannerFont.getFont());
		internalPanel.setPreferredSize(new Dimension(10, 23));
	}
	
	public StatusBarBuilder add(JComponent component) {
		
		internalPanel = new PanelBuilder().withBorderLayout()
				.add(component, BorderLayout.WEST)
				.add(internalPanel, BorderLayout.CENTER)
				.build();
		return this;
	}
	
	public JPanel build() {
		
		JPanel rightPanel = new PanelBuilder().withBorderLayout()
				.add(new JLabel(new AngledLinesWindowsCornerIcon()), BorderLayout.EAST)
				.withBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null))
				.build();
		
		internalPanel = new PanelBuilder().withBorderLayout()
				.add(internalPanel, BorderLayout.CENTER)
				.add(rightPanel, BorderLayout.EAST)
				.build();
		
		return internalPanel;
	}
}