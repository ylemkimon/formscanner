package org.albertoborsetta.formscanner.commons;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.albertoborsetta.formscanner.api.FormField;
import org.albertoborsetta.formscanner.api.FormTemplate;
import org.albertoborsetta.formscanner.api.FormTemplateWrapper;
import org.albertoborsetta.formscanner.api.commons.translation.TranslationKeys;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;
import org.w3c.dom.Document;

public class FormFileUtils extends JFileChooser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static FormFileUtils instance;

	public static FormFileUtils getInstance() {
		if (instance == null) {
			instance = new FormFileUtils();
		}
		return instance;
	}

	private FormFileUtils() {
		super();
		setFont(FormScannerFont.getFont());
	}

	private File chooseFile() {
		File file = null;
		int returnValue = showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			file = getSelectedFile();
		}
		return file;
	}

	private File[] chooseFiles() {
		File[] files = null;
		int returnValue = showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) {
			files = getSelectedFiles();
		}
		return files;
	}

	public File[] chooseImages() {
		setMultiSelectionEnabled(true);
		setImagesFilter();
		return chooseFiles();
	}

	public File chooseImage() {
		setMultiSelectionEnabled(false);
		setImagesFilter();
		return chooseFile();
	}

	public File chooseTemplate() {
		setMultiSelectionEnabled(false);
		setTemplateFilter();
		return chooseFile();
	}

	private void setImagesFilter() {
		resetChoosableFileFilters();

		// Creating a set for preveting duplication of entries
		Set<String> setOfExtensions = new TreeSet<String>();
		// Iterating all possible image suffixes the current jvm can open
		for (String suffix : ImageIO.getReaderFileSuffixes()) {
			setOfExtensions.add(suffix);
		}
		// Creating individual file filters
		for (String ext : setOfExtensions) {
			setFileFilter(new FileNameExtensionFilter(
					FormScannerTranslation.getTranslationFor(ext + ".images"), ext));
		}
		// Creating "all images" file filter (the one which opens any supported
		// image type)
		FileNameExtensionFilter allImagesFilter = new FileNameExtensionFilter(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.ALL_IMAGES),
				ImageIO.getReaderFileSuffixes());
		setFileFilter(allImagesFilter);

		// resetChoosableFileFilters();
		//
		// FileNameExtensionFilter allImagesFilter = new
		// FileNameExtensionFilter(
		// FormScannerTranslation
		// .getTranslationFor(FormScannerTranslationKeys.ALL_IMAGES),
		// "jpg", "jpeg", "tif", "tiff", "png", "bmp");
		// FileNameExtensionFilter pmbImagesFilter = new
		// FileNameExtensionFilter(
		// FormScannerTranslation
		// .getTranslationFor(FormScannerTranslationKeys.BMP_IMAGES),
		// "bmp");
		// FileNameExtensionFilter pngImagesFilter = new
		// FileNameExtensionFilter(
		// FormScannerTranslation
		// .getTranslationFor(FormScannerTranslationKeys.PNG_IMAGES),
		// "png");
		// FileNameExtensionFilter jpegImagesFilter = new
		// FileNameExtensionFilter(
		// FormScannerTranslation
		// .getTranslationFor(FormScannerTranslationKeys.JPEG_IMAGES),
		// "jpg", "jpeg");
		// FileNameExtensionFilter tiffImagesFilter = new
		// FileNameExtensionFilter(
		// FormScannerTranslation
		// .getTranslationFor(FormScannerTranslationKeys.TIFF_IMAGES),
		// "tif", "tiff");
		//
		// setFileFilter(pmbImagesFilter);
		// setFileFilter(pngImagesFilter);
		// setFileFilter(jpegImagesFilter);
		// setFileFilter(tiffImagesFilter);
		// setFileFilter(allImagesFilter);
	}

	private void setTemplateFilter() {
		resetChoosableFileFilters();
		FileNameExtensionFilter templateFilter = new FileNameExtensionFilter(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.TEMPLATE_FILE),
				"xtmpl");
		setFileFilter(templateFilter);
	}

	private void setCsvFilter() {
		resetChoosableFileFilters();
		FileNameExtensionFilter templateFilter = new FileNameExtensionFilter(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.CSV_FILE),
				"csv");
		setFileFilter(templateFilter);
	}

	public File saveTemplateAs(File file, Document doc) {
		try {
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "4");
			DOMSource source = new DOMSource(doc);

			setMultiSelectionEnabled(false);
			setTemplateFilter();
			setSelectedFile(file);

			int returnValue = showSaveDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				file = getSelectedFile();
				StreamResult result = new StreamResult(file);
				transformer.transform(source, result);
			}
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return file;
	}

	public File saveCsvAs(File file, HashMap<String, FormTemplate> filledForms) {
		String aKey = (String) filledForms.keySet().toArray()[0];
		FormTemplate aForm = filledForms.get(aKey);
		String[] header = getHeader(aForm);
		ArrayList<HashMap<String, String>> results = getResults(filledForms,
				header);

		ICsvMapWriter mapWriter = null;
		try {
			try {
				setMultiSelectionEnabled(false);
				setCsvFilter();
				setSelectedFile(file);

				int returnValue = showSaveDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					file = getSelectedFile();
					mapWriter = new CsvMapWriter(new FileWriter(file),
							CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
					mapWriter.writeHeader(header);

					for (HashMap<String, String> result : results) {
						mapWriter.write(result, header);
					}
				}
			} finally {
				if (mapWriter != null) {
					mapWriter.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	private ArrayList<HashMap<String, String>> getResults(
			HashMap<String, FormTemplate> filledForms, String[] header) {
		ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
		for (Entry<String, FormTemplate> filledForm : filledForms.entrySet()) {
			FormTemplate form = filledForm.getValue();
			HashMap<String, FormField> fields = form.getFields();

			HashMap<String, String> result = new HashMap<String, String>();
			result.put(header[0], filledForm.getKey());
			for (int i = 1; i < header.length; i++) {
				FormField field = fields.get(header[i]);
				result.put(header[i], field.getValues());
			}

			results.add(result);
		}
		return results;
	}

	public static String[] getHeader(FormTemplate template) {
		HashMap<String, FormField> fields = template.getFields();
		String[] header = new String[fields.size() + 1];
		int i = 0;
		header[i++] = FormScannerTranslation
				.getTranslationFor(FormScannerTranslationKeys.FIRST_CSV_COLUMN);

		ArrayList<String> keys = new ArrayList<String>(fields.keySet());
		Collections.sort(keys);
		for (String key : keys) {
			header[i++] = key;
		}

		return header;
	}
	
	public static File saveToFile(String path, FormTemplate template) {
		File outputFile = null;
		
		try {
			outputFile = new File(path + "/template/" + template.getName() + ".xtmpl");
			Document xml = FormTemplateWrapper.getXml(template);
			outputFile = FormFileUtils.getInstance().saveTemplateAs(outputFile, xml);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		
		return outputFile;
	}
}
