package org.albertoborsetta.formscanner.gui;

import java.awt.Component;
import java.awt.EventQueue;
import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JDesktopPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.albertoborsetta.formscanner.commons.FormScannerConstants.Frame;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.gui.controller.FormScannerController;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class FormScanner extends JFrame {

	private static final long serialVersionUID = 1L;

	private static FormScannerModel model;
	private static JDesktopPane desktopPane;
	private ToolBar toolBar;
	private MenuBar menuBar;
	private FormScannerController mainFrameController;

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
		mainFrameController = FormScannerController
				.getInstance(model);
		addWindowListener(mainFrameController);
		
		setName(Frame.DESKTOP_FRAME.name());
		
		setTitle(FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.FORMSCANNER_MAIN_TITLE));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		getContentPane().setLayout(new BorderLayout(0, 0));
		menuBar = new MenuBar(model);
		setJMenuBar(menuBar);
		
		toolBar = new ToolBar(model);
		getContentPane().add(toolBar, BorderLayout.NORTH);
		
		desktopPane = new JDesktopPane();
		getContentPane().add(desktopPane, BorderLayout.CENTER);	
		
		model.setDefaultFramePositions();
		setBounds(model.getLastPosition(Frame.DESKTOP_FRAME));
		setVisible(true);
	}
	
	public void arrangeFrame(InternalFrame frame) {
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
	
	public void disposeFrame(InternalFrame frame) {
		if (frame != null) {
			model.setLastPosition(Frame.valueOf(frame.getName()), frame.getBounds());
			frame.setVisible(false);
			desktopPane.remove(frame);
		}
		setDefaultControllersEnabled();
		model.resetFirstPass();
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
