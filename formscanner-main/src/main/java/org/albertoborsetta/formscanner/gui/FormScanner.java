package org.albertoborsetta.formscanner.gui;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class FormScanner extends JFrame {

	private static final long serialVersionUID = 1L;

	private static FormScannerModel model;
	private static JDesktopPane desktopPane;
	private ToolBar toolBar;
	private MenuBar menuBar;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
//					UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			    	UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
//				    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
//				        if ("Nimbus".equals(info.getName())) {
//				            UIManager.setLookAndFeel(info.getClassName());
//				            break;
//				        }
//				    }
				    FormScanner window = new FormScanner();
					window.setIconImage(null);
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
		
		setTitle(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.FORMSCANNER_MAIN_TITLE));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getContentPane().setLayout(new BorderLayout(0, 0));
		menuBar = new MenuBar(model);
		setJMenuBar(menuBar);
		
		toolBar = new ToolBar(model);
		getContentPane().add(toolBar, BorderLayout.NORTH);
		
		desktopPane = new JDesktopPane();
		getContentPane().add(desktopPane, BorderLayout.CENTER);	
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize(); 
		setSize(screenSize);
		setVisible(true);
	}
	
	public void arrangeFrame(JInternalFrame frame) {
		boolean found = false;
		
		for (Component component: desktopPane.getComponents()) {
			if (frame.getName().equals(component.getName())) {
				component.setVisible(false);
				desktopPane.remove(component);
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
		if (frame != null) {
			frame.setVisible(false);
			desktopPane.remove(frame);
		}
		setDefaultControllersEnabled();
		model.resetFirstPass();
	}
	
	public Dimension getDesktopSize() {
		return desktopPane.getSize();
	}
	
	public void setDefaultControllersEnabled() {
		toolBar.setRenameControllersEnabled(true);
		toolBar.setScanControllersEnabled(true);
		toolBar.setScanAllControllersEnabled(true);
		toolBar.setScanCurrentControllersEnabled(false);
		menuBar.setRenameControllersEnabled(true);
		menuBar.setScanControllersEnabled(true);
		menuBar.setScanAllControllersEnabled(true);
	}
	
	public void setRenameControllersEnabled(boolean enable) {
		toolBar.setRenameControllersEnabled(enable);
		menuBar.setRenameControllersEnabled(enable);
	}

	public void setScanControllersEnabled(boolean enable) {
		toolBar.setScanControllersEnabled(enable);
		menuBar.setScanControllersEnabled(enable);
	}
	
	public void setScanAllControllersEnabled(boolean enable) {
		toolBar.setScanAllControllersEnabled(enable);
		menuBar.setScanAllControllersEnabled(enable);
	}
	
	public void setScanCurrentControllersEnabled(boolean enable) {
		toolBar.setScanCurrentControllersEnabled(enable);
	}
}
