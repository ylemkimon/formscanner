package org.albertoborsetta.formscanner.gui;

import java.awt.EventQueue;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import java.awt.BorderLayout;

import java.awt.Dimension;
import javax.swing.JPanel;

import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;

import org.albertoborsetta.formscanner.model.FormScannerModel;

public class FormScanner extends JFrame {

	private static FormScannerModel model;
	private static JDesktopPane desktopPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {					
					FormScanner window = new FormScanner();					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	private FormScanner() {
		model = new FormScannerModel(this);
		
		setTitle("FormScanner");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getContentPane().setLayout(new BorderLayout(0, 0));
		JMenuBar menuBar = new MenuBar(model);
		setJMenuBar(menuBar);
		
		JPanel toolBar = new ToolBar(model);
		getContentPane().add(toolBar, BorderLayout.NORTH);
		
		JLabel statusBar = new StatusBar(model);
		getContentPane().add(statusBar, BorderLayout.SOUTH);
		
		desktopPane = new JDesktopPane();
		getContentPane().add(desktopPane, BorderLayout.CENTER);	
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		setSize(screenSize);
		setVisible(true);
	}
	
	public void arrangeFrame(JInternalFrame frame) {
		boolean found = false;
		
		for (int i = 0; i < desktopPane.getComponentCount(); i++) {
			if (desktopPane.getComponent(i).getName() == frame.getName()) {
				desktopPane.getComponent(i).setVisible(false);
				desktopPane.remove(desktopPane.getComponent(i));
				found = true;
				break;
			}
		}
		
		if (!found) {
			desktopPane.add(frame);
			frame.setVisible(true);
		} else {
			arrangeFrame(frame);
		}
	}
	
	public void disposeFrame(JInternalFrame frame) {
		for (int i = 0; i < desktopPane.getComponentCount(); i++) {
			if (desktopPane.getComponent(i).getName() == frame.getName()) {
				desktopPane.getComponent(i).setVisible(false);
				desktopPane.remove(desktopPane.getComponent(i));
				break;
			}
		}
	}
	
	public Dimension getDesktopSize() {
		return desktopPane.getSize();
	}
	
}
