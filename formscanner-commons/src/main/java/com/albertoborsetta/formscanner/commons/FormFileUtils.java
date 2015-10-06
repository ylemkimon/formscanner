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
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.supercsv.io.CsvMapWriter;
import org.supercsv.io.ICsvMapWriter;
import org.supercsv.prefs.CsvPreference;
import org.w3c.dom.Document;

import com.albertoborsetta.formscanner.api.FormArea;
import com.albertoborsetta.formscanner.api.FormGroup;
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
	private static final Logger logger = LogManager.getLogger(FormFileUtils.class.getName());

	public class Header {

		private ArrayList<String> headerKeys;
		private HashMap<String, String> fieldNames;
		private HashMap<String, String> groupNames;

		public Header() {
			headerKeys = new ArrayList<>();
			fieldNames = new HashMap<>();
			groupNames = new HashMap<>();
		}

		public Integer size() {
			return headerKeys.size();
		}

		public String[] getHeaderKeys() {
			String[] header = new String[headerKeys.size() + 1];

			int i = 0;
			header[i++] = getFirstHeader();

			Collections.sort(headerKeys);
			for (String headerKey : headerKeys) {
				header[i++] = headerKey;
			}

			return header;
		}

		public void addHeaderKey(String key) {
			headerKeys.add(key);
		}

		public void addFieldName(String key, String name) {
			fieldNames.put(key, name);
		}

		public void addGroupName(String key, String name) {
			groupNames.put(key, name);
		}

		public String getGroupForKey(String headerKey) {
			return groupNames.get(headerKey);
		}

		public String getFieldForKey(String headerKey) {
			return fieldNames.get(headerKey);
		}

		public ArrayList<String> getKeys() {
			return headerKeys;
		}

		public String getFirstHeader() {
			String firstHeader;
			try {
				firstHeader = FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.FIRST_CSV_COLUMN);
			} catch (Exception e) {
				firstHeader = StringUtils.EMPTY;
			}
			return firstHeader;
		}
	}

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

		// Creating a set for preventing duplication of entries
		ArrayList<String> setOfExtensions = new ArrayList<>();
		// Iterating all possible image suffixes the current jvm can open
		for (String suffix : ImageIO.getReaderFileSuffixes()) {
			if (StringUtils.isNotBlank(suffix)) {
				setFileFilter(new FileNameExtensionFilter(FormScannerTranslation.getTranslationFor(suffix + ".images"),
						suffix));
				setOfExtensions.add(suffix);
			}
		}

		// Creating "all images" file filter (the one which opens any supported
		// image type)
		String[] arrayOfExtensions = new String[setOfExtensions.size()];
		arrayOfExtensions = setOfExtensions.toArray(arrayOfExtensions);
		FileNameExtensionFilter allImagesFilter = new FileNameExtensionFilter(
				FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.ALL_IMAGES), arrayOfExtensions);
		setFileFilter(allImagesFilter);
	}

	private void setTemplateFilter() {
		resetChoosableFileFilters();
		FileNameExtensionFilter templateFilter = new FileNameExtensionFilter(
				FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.TEMPLATE_FILE), "xtmpl");
		setFileFilter(templateFilter);
	}

	private void setCsvFilter() {
		resetChoosableFileFilters();
		FileNameExtensionFilter templateFilter = new FileNameExtensionFilter(
				FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.CSV_FILE), "csv");
		setFileFilter(templateFilter);
	}
	
	

	private File saveTemplateAs(File file, Document doc, boolean notify) {
		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.setOutputProperty(OutputKeys.METHOD, "xml");
			transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
			transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			DOMSource source = new DOMSource(doc);

			setMultiSelectionEnabled(false);
			setSelectedFile(file);

			if (notify) {
				setTemplateFilter();
				int returnValue = showSaveDialog(null);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					file = getSelectedFile();
					FileOutputStream fos = new FileOutputStream(file);
					OutputStreamWriter out = new OutputStreamWriter(fos, Charset.forName("UTF-8"));
					StreamResult result = new StreamResult(out);
					transformer.transform(source, result);
				}
			} else {
				FileOutputStream fos = new FileOutputStream(file);
				OutputStreamWriter out = new OutputStreamWriter(fos, Charset.forName("UTF-8"));
				StreamResult result = new StreamResult(out);
				transformer.transform(source, result);
			}
		} catch (TransformerException | IOException e) {
			logger.debug("Error", e);
		}
		return file;
	}

	public File saveCsvAs(File file, HashMap<String, FormTemplate> filledForms, boolean notify) {
		String aKey = (String) filledForms.keySet().toArray()[0];
		FormTemplate aForm = filledForms.get(aKey);
		Header header = getHeader(aForm);
		String[] headerKeys = header.getHeaderKeys();
		ArrayList<HashMap<String, String>> results = getResults(filledForms, header);

		ICsvMapWriter mapWriter = null;
		try {
			try {
				if (notify) {
					setMultiSelectionEnabled(false);
					setCsvFilter();
					setSelectedFile(file);

					int returnValue = showSaveDialog(null);
					if (returnValue == JFileChooser.APPROVE_OPTION) {
						file = getSelectedFile();
						FileOutputStream fos = new FileOutputStream(file);
						OutputStreamWriter out = new OutputStreamWriter(fos, Charset.forName("UTF-8"));
						mapWriter = new CsvMapWriter(out, CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
						mapWriter.writeHeader(headerKeys);

						for (HashMap<String, String> result : results) {
							mapWriter.write(result, headerKeys);
						}
					}
				} else {
					FileOutputStream fos = new FileOutputStream(file);
					OutputStreamWriter out = new OutputStreamWriter(fos, Charset.forName("UTF-8"));
					mapWriter = new CsvMapWriter(out, CsvPreference.EXCEL_NORTH_EUROPE_PREFERENCE);
					mapWriter.writeHeader(headerKeys);

					for (HashMap<String, String> result : results) {
						mapWriter.write(result, headerKeys);
					}
				}
			} finally {
				if (mapWriter != null) {
					mapWriter.close();
				}
			}
		} catch (IOException e) {
			logger.debug("Error", e);
		}
		return file;
	}

	private static ArrayList<HashMap<String, String>> getResults(HashMap<String, FormTemplate> filledForms,
			Header header) {
		ArrayList<HashMap<String, String>> results = new ArrayList<>();
		for (Entry<String, FormTemplate> filledForm : filledForms.entrySet()) {
			FormTemplate form = filledForm.getValue();
			HashMap<String, FormGroup> groups = form.getGroups();
			HashMap<String, String> result = new HashMap<>();
			
			result.put(header.getFirstHeader(), filledForm.getKey());
			
			for (String headerKey: header.getKeys()) {
				FormGroup group = groups.get(header.getGroupForKey(headerKey));
				String fieldName = header.getFieldForKey(headerKey);
				
				FormQuestion field = group.getFields().get(fieldName);
				if (field != null) {
					result.put(headerKey, field.getValues());
				}
				
				FormArea area = group.getAreas().get(fieldName);
				if (area != null) {
					result.put(headerKey, area.getText());
				}
			}
			
			results.add(result);

		}
		return results;
	}

	public Header getHeader(FormTemplate template) {
		Header header = new Header();

		HashMap<String, FormGroup> groups = template.getGroups();
		for (Entry<String, FormGroup> groupEntry : groups.entrySet()) {
			FormGroup group = groupEntry.getValue();
			HashMap<String, FormQuestion> fields = group.getFields();
			for (Entry<String, FormQuestion> fieldEntry : fields.entrySet()) {
				String headerKey = "[" + groupEntry.getKey() + "] " + fieldEntry.getKey();
				header.addHeaderKey(headerKey);
				header.addGroupName(headerKey, groupEntry.getKey());
				header.addFieldName(headerKey, fieldEntry.getKey());
			}
		}
		for (Entry<String, FormGroup> groupEntry : groups.entrySet()) {
			FormGroup group = groupEntry.getValue();
			HashMap<String, FormArea> areas = group.getAreas();
			for (Entry<String, FormArea> areaEntry : areas.entrySet()) {
				String headerKey = "[" + groupEntry.getKey() + "] " + areaEntry.getKey();
				header.addHeaderKey(headerKey);
				header.addGroupName(headerKey, groupEntry.getKey());
				header.addFieldName(headerKey, areaEntry.getKey());
			}
		}

		return header;
	}

	public File saveToFile(String path, FormTemplate template, boolean notify) {
		File outputFile = null;

		try {
			outputFile = new File(path + template.getName() + ".xtmpl");
			Document xml = template.getXml();
			outputFile = saveTemplateAs(outputFile, xml, notify);
		} catch (ParserConfigurationException e) {
			logger.debug("Error", e);
		}

		return outputFile;
	}
}
