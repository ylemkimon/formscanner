package com.albertoborsetta.formscanner.commons;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;

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

import org.apache.commons.lang3.StringUtils;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;
import org.w3c.dom.Document;

import com.albertoborsetta.formscanner.api.FormArea;
import com.albertoborsetta.formscanner.api.FormQuestion;
import com.albertoborsetta.formscanner.api.FormTemplate;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;

public class FormFileUtils extends JFileChooser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static FormFileUtils instance;

	public static FormFileUtils getInstance(Locale locale) {
		if (instance == null) {
			instance = new FormFileUtils(locale);
		}
		return instance;
	}

	private FormFileUtils(Locale locale) {
		super();
		setFont(FormScannerFont.getFont());
		setLocale(locale);
		updateUI();
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
		ArrayList<String> setOfExtensions = new ArrayList<String>();
		// Iterating all possible image suffixes the current jvm can open
		for (String suffix : ImageIO.getReaderFileSuffixes()) {
			if (StringUtils.isNotBlank(suffix)) {
				setFileFilter(new FileNameExtensionFilter(
						FormScannerTranslation.getTranslationFor(suffix + ".images"),
						suffix));
				setOfExtensions.add(suffix);
			}
		}

		// Creating "all images" file filter (the one which opens any supported
		// image type)
		String[] arrayOfExtensions = new String[setOfExtensions.size()];
		arrayOfExtensions = setOfExtensions.toArray(arrayOfExtensions);
		FileNameExtensionFilter allImagesFilter = new FileNameExtensionFilter(
				FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.ALL_IMAGES),
						arrayOfExtensions);
		setFileFilter(allImagesFilter);
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

	private File saveTemplateAs(File file, Document doc) {
		try {
			TransformerFactory transformerFactory = TransformerFactory
					.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			DOMSource source = new DOMSource(doc);

			setMultiSelectionEnabled(false);
			setTemplateFilter();
			setSelectedFile(file);

			int returnValue = showSaveDialog(null);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				file = getSelectedFile();
				FileOutputStream fos = new FileOutputStream(file);
				OutputStreamWriter out = new OutputStreamWriter(fos,
						Charset.forName("UTF-8"));
				StreamResult result = new StreamResult(out);
				transformer.transform(source, result);
			}
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
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
					FileOutputStream fos = new FileOutputStream(file);
					OutputStreamWriter out = new OutputStreamWriter(fos,
							Charset.forName("UTF-8"));
					mapWriter = new CsvMapWriter(out,
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
			HashMap<String, FormQuestion> fields = form.getFields();
			HashMap<String, FormArea> areas = form.getAreas();

			HashMap<String, String> result = new HashMap<String, String>();
			result.put(header[0], filledForm.getKey());
			
			for (int i = 1; i < header.length; i++) {
				FormQuestion field = fields.get(header[i]);
				if (field != null) {
					result.put(header[i], field.getValues());
				} else {
					FormArea area = areas.get(header[i]);
					result.put(header[i], area.getText());
				}
			}

			results.add(result);
		}
		return results;
	}

	public String[] getHeader(FormTemplate template) {
		HashMap<String, FormQuestion> fields = template.getFields();
		HashMap<String, FormArea> areas = template.getAreas();
		String[] header = new String[fields.size() + areas.size() + 1];
		int i = 0;
		header[i++] = FormScannerTranslation
				.getTranslationFor(FormScannerTranslationKeys.FIRST_CSV_COLUMN);

		ArrayList<String> fieldKeys = new ArrayList<String>(fields.keySet());
		Collections.sort(fieldKeys);
		for (String fieldKey : fieldKeys) {
			header[i++] = fieldKey;
		}
		
		ArrayList<String> areaKeys = new ArrayList<String>(areas.keySet());
		Collections.sort(areaKeys);
		for (String areaKey : areaKeys) {
			header[i++] = areaKey;
		}
		
		return header;
	}

	public File saveToFile(String path, FormTemplate template) {
		File outputFile = null;

		try {
			outputFile = new File(path + template.getName() + ".xtmpl");
			Document xml = template.getXml();
			outputFile = saveTemplateAs(outputFile, xml);
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		return outputFile;
	}
}
