package org.albertoborsetta.formscanner.gui;

import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;

import org.albertoborsetta.formscanner.model.FormScannerModel;
import org.albertoborsetta.formscanner.commons.configuration.ConfigurationException;
import org.albertoborsetta.formscanner.commons.configuration.ConfigurationLoader;
import org.albertoborsetta.formscanner.commons.translations.TranslationException;
import org.albertoborsetta.formscanner.commons.translations.TranslationLoader;
import org.albertoborsetta.formscanner.commons.FormScannerConfigurationKeys;
import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.FormScannerTranslationKeys;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.apache.commons.configuration.Configuration;

public class FormScanner extends JFrame {

	private static final long serialVersionUID = 1L;

	private static FormScannerModel model;
	private static JDesktopPane desktopPane;
	private ToolBar toolBar;
	private JLabel statusBar;
	private MenuBar menuBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				Properties properties = new Properties();
				Properties translations = new Properties();
				
				try {
					
					properties.load(FormScanner.class.getClassLoader().getResourceAsStream("config/formscanner.properties"));
					String language = properties.getProperty(FormScannerConfigurationKeys.LANG);
					
					translations.load(FormScanner.class.getClassLoader().getResourceAsStream("language/formscanner-"+language+".lang"));
					System.out.println(translations.getProperty(FormScannerTranslationKeys.OPEN_FILES));
					
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
		menuBar = new MenuBar(model);
		setJMenuBar(menuBar);
		
		toolBar = new ToolBar(model);
		getContentPane().add(toolBar, BorderLayout.NORTH);
		
		statusBar = new StatusBar(model);
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
	
	public void setRenameControllersEnabled(boolean enable) {
		toolBar.setRenameControllersEnabled(enable);
		menuBar.setRenameControllersEnabled(enable);
	}

	public void setScanControllersEnabled(boolean enable) {
		toolBar.setScanControllersEnabled(enable);
		// menuBar.setScanControllersEnabled(enable);		
	}
}
