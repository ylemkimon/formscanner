package com.albertoborsetta.formscanner.main;

import java.awt.EventQueue;
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
import org.xml.sax.SAXException;

import com.albertoborsetta.formscanner.api.FormTemplate;
import com.albertoborsetta.formscanner.api.exceptions.FormScannerException;
import com.albertoborsetta.formscanner.commons.FormFileUtils;
import com.albertoborsetta.formscanner.commons.FormScannerConstants;
import com.albertoborsetta.formscanner.gui.FormScannerWorkspace;
import com.albertoborsetta.formscanner.model.FormScannerModel;

import java.io.UnsupportedEncodingException;

import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import ch.randelshofer.quaqua.QuaquaLookAndFeel;

public class FormScanner {

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
						
						for (LookAndFeelInfo info : UIManager
								.getInstalledLookAndFeels()) {
							if (model.getLookAndFeel().equals(info.getName())) {
								UIManager.setLookAndFeel(info.getClassName());
								break;
							}
						}
						FormScannerWorkspace desktop = new FormScannerWorkspace(model);
						desktop.setIconImage(model.getIcon());
					} catch (UnsupportedEncodingException
							| ClassNotFoundException | InstantiationException
							| IllegalAccessException
							| UnsupportedLookAndFeelException e) {
						e.printStackTrace();
					}
				}
			});
		} else {
			Locale locale = Locale.getDefault();
			FormFileUtils fileUtils = FormFileUtils.getInstance(locale);
			
			File templateFile = new File(args[0]);
			FormTemplate template = null;
			try {
				template = new FormTemplate();
				template.presetFormTemplate(templateFile);
				if (!FormScannerConstants.CURRENT_TEMPLATE_VERSION.equals(template.getVersion())) {
					fileUtils.saveToFile(FilenameUtils.getFullPath(args[0]), template, false);
				}
			} catch (ParserConfigurationException | SAXException | IOException e) {
				e.printStackTrace();
				System.exit(-1);
			}
			String[] extensions = ImageIO.getReaderFileSuffixes();
			Iterator<?> fileIterator = FileUtils.iterateFiles(
					new File(args[1]), extensions, false);
			HashMap<String, FormTemplate> filledForms = new HashMap<>();
			while (fileIterator.hasNext()) {
				File imageFile = (File) fileIterator.next();
				
				try {
					FormTemplate filledForm = new FormTemplate(
							imageFile, template);
					filledForm.findCorners(
							template.getThreshold(),
							template.getDensity(), template.getCornerType(), template.getCrop());
					filledForm.findPoints(
							template.getThreshold(),
							template.getDensity(), template.getSize());
					filledForm.findAreas();
					filledForms
					.put(
							filledForm.getName(),
							filledForm);
				} catch (IOException | FormScannerException e) {
					e.printStackTrace();;
					System.exit(-1);
				}
				
			}

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
