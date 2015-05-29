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
import com.albertoborsetta.formscanner.commons.FormFileUtils;
import com.albertoborsetta.formscanner.gui.FormScannerDesktop;
import com.albertoborsetta.formscanner.model.FormScannerModel;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;

import java.io.UnsupportedEncodingException;

import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import ch.randelshofer.quaqua.QuaquaLookAndFeel;

public class FormScanner {

	private static final Logger logger = LogManager.getLogger(FormScanner.class.getName());
	
	/**
	 * Launch the application.
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length == 0) {
			EventQueue.invokeLater(new Runnable() {
				@Override
				public void run() {
					try {
						FormScannerModel model = new FormScannerModel();
						
						UIManager.installLookAndFeel("Quaqua", QuaquaLookAndFeel.class.getName());
						UIManager.installLookAndFeel("Seaglass", SeaGlassLookAndFeel.class.getName());
						
						for (LookAndFeelInfo info : UIManager
								.getInstalledLookAndFeels()) {
							if (model.getLookAndFeel().equals(info.getName())) {
								UIManager.setLookAndFeel(info.getClassName());
								break;
							}
						}
						FormScannerDesktop desktop = new FormScannerDesktop(
								model);
						model.setDesktop(desktop);
						desktop.setIconImage(model.getIcon());
					} catch (UnsupportedEncodingException
							| ClassNotFoundException | InstantiationException
							| IllegalAccessException
							| UnsupportedLookAndFeelException e) {
						logger.debug(e);
					}
				}
			});
		} else {
			File templateFile = new File(args[0]);
			FormTemplate template = null;
			try {
				template = new FormTemplate(templateFile);
			} catch (ParserConfigurationException | SAXException | IOException e) {
				logger.debug(e);
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
					logger.debug(e);
					System.exit(-1);
				}
				FormTemplate filledForm = new FormTemplate(
						imageFile.getName(), template);
				try {
					filledForm.findCorners(
							image, template.getThreshold(),
							template.getDensity());
					filledForm.findPoints(
							image, template.getThreshold(),
							template.getDensity(), template.getSize());
					filledForm.findAreas(image);
				} catch (FormScannerException e) {
					logger.debug(e);
					System.exit(-1);
				}
				filledForms
						.put(
								FilenameUtils.getName(imageFile.toString()),
								filledForm);
			}
			Locale locale = Locale.getDefault();
			FormFileUtils fileUtils = FormFileUtils.getInstance(locale);

			Date today = Calendar.getInstance().getTime();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

			File outputFile = new File(
					args[1] + System.getProperty("file.separator") + "results_" + sdf
							.format(today) + ".csv");
			fileUtils.saveCsvAs(outputFile, filledForms, false);
			System.exit(0);
		}
	}
}
