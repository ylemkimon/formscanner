package com.albertoborsetta.formscanner.gui;

import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.EventQueue;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JDesktopPane;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;
import org.xml.sax.SAXException;

import com.albertoborsetta.formscanner.api.FormTemplate;
import com.albertoborsetta.formscanner.commons.FormFileUtils;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.Frame;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import com.albertoborsetta.formscanner.gui.controller.FormScannerController;
import com.albertoborsetta.formscanner.gui.model.FormScannerModel;
import com.albertoborsetta.formscanner.gui.view.InternalFrame;
import com.albertoborsetta.formscanner.gui.view.MenuBar;
import com.albertoborsetta.formscanner.gui.view.ToolBar;
import com.sun.jna.platform.FileUtils;

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
		if (args.length == 0) {
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
		} else {
			File templateFile = new File(args[0]);
			FormTemplate template = null;
			File imageFile = new File(args[1]);
			BufferedImage image = null;
			try {
				template = new FormTemplate(templateFile);
				image = ImageIO.read(imageFile);
			} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
			}
			FormTemplate filledForm = new FormTemplate(imageFile.getName(), template);
			filledForm.findCorners(image, template.getThreshold(), template.getDensity());
			filledForm.findPoints(image, template.getThreshold(), template.getDensity(), template.getSize());
			filledForm.findAreas(image);
			HashMap<String, FormTemplate> filledForms = new HashMap<String, FormTemplate>();
			filledForms.put(FilenameUtils.getName(args[1]), filledForm);
			
			Locale locale = Locale.getDefault();
			FormFileUtils fileUtils = FormFileUtils.getInstance(locale);
			
			Date today = Calendar.getInstance().getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			File outputFile = new File(FilenameUtils.getFullPath(args[1]) + sdf.format(today) + ".csv");
			fileUtils.saveCsvAs(outputFile, filledForms, false);
			System.out.println("saved");
			System.exit(0);
		}
	}

	/**
	 * Create the application.
	 */
	private FormScanner() {
		model = new FormScannerModel(this);
		mainFrameController = FormScannerController.getInstance(model);
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
		setDefaultLookAndFeelDecorated(true);
		setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
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
			frame.dispose();
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
