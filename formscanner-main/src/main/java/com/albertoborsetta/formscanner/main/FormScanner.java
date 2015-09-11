package com.albertoborsetta.formscanner.main;

import java.awt.EventQueue;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageIO;
import javax.swing.UIManager;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.SAXException;

import com.albertoborsetta.formscanner.api.FormTemplate;
import com.albertoborsetta.formscanner.api.exceptions.FormScannerException;
import com.albertoborsetta.formscanner.commons.FormScannerFileUtils;
import com.albertoborsetta.formscanner.gui.FormScannerDesktop;
import com.albertoborsetta.formscanner.gui.desktop.DesktopController;
import com.albertoborsetta.formscanner.model.FormScannerModel;

import java.io.UnsupportedEncodingException;

import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import ch.randelshofer.quaqua.QuaquaLookAndFeel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class FormScanner extends Application {

	private static Logger logger;
	
	/**
	 * Launch the application.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			launch(args);
//			EventQueue.invokeLater(new Runnable() {
//				@Override
//				public void run() {
//					try {
//						FormScannerModel model = new FormScannerModel();
//						
//						logger = LogManager.getLogger(FormScanner.class.getName());
//						
//						UIManager.installLookAndFeel("Quaqua", QuaquaLookAndFeel.class.getName());
//						
//						for (LookAndFeelInfo info : UIManager
//								.getInstalledLookAndFeels()) {
//							if (model.getLookAndFeel().equals(info.getName())) {
//								UIManager.setLookAndFeel(info.getClassName());
//								break;
//							}
//						}
//						FormScannerDesktop desktop = new FormScannerDesktop(
//								model);
//						model.setDesktop(desktop);
//						desktop.setIconImage(model.getIcon());
//					} catch (UnsupportedEncodingException
//							| ClassNotFoundException | InstantiationException
//							| IllegalAccessException
//							| UnsupportedLookAndFeelException e) {
//						logger.debug("Error", e);
//					}
//				}
//			});
		} else {
			File templateFile = new File(args[0]);
			FormTemplate template = null;
			try {
				template = new FormTemplate(templateFile);
			} catch (ParserConfigurationException | SAXException | IOException e) {
				logger.debug("Error", e);
				System.exit(-1);
			}
			String[] extensions = ImageIO.getReaderFileSuffixes();
			Iterator<?> fileIterator = FileUtils.iterateFiles(
					new File(args[1]), extensions, false);
			HashMap<String, FormTemplate> filledForms = new HashMap<>();
			while (fileIterator.hasNext()) {
				File imageFile = (File) fileIterator.next();
				BufferedImage image = null;
				try {
					image = ImageIO.read(imageFile);
				} catch (IOException e) {
					logger.debug("Error", e);
					System.exit(-1);
				}
				FormTemplate filledForm = new FormTemplate(
						imageFile.getName(), template);
				try {
					filledForm.findCorners(
							image, template.getThreshold(),
							template.getDensity(), template.getCornerType(), template.getCrop());
					filledForm.findPoints(
							image, template.getThreshold(),
							template.getDensity(), template.getSize());
					filledForm.findAreas(image);
				} catch (FormScannerException e) {
					logger.debug("Error", e);
					System.exit(-1);
				}
				filledForms
						.put(
								FilenameUtils.getName(imageFile.toString()),
								filledForm);
			}
			FormScannerFileUtils fileUtils = FormScannerFileUtils.getInstance(null);

			Date today = Calendar.getInstance().getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

			File outputFile = new File(
					args[1] + System.getProperty("file.separator") + "results_" + sdf
							.format(today) + ".csv");
			fileUtils.saveCsvAs(outputFile, filledForms, false);
			System.exit(0);
		}
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		Stage stage = primaryStage;

		try {
			logger = LogManager.getLogger(FormScanner.class.getName());
			
			FormScannerModel model = new FormScannerModel();
			DesktopController desktop = new DesktopController();
			
			Scene scene = new Scene(desktop.getDesktop());
			scene.setNodeOrientation(model.getOrientation());

			stage.setTitle(desktop.getTitle());
			stage.setScene(scene);
			stage.show();
			
		} catch (IOException e) {
			logger.debug("Error starting FormScanner", e);
		}

	}
}
