package org.albertoborsetta.formscanner.commons;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;

public class FileOpener extends JFileChooser {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static FileOpener instance;

	public static FileOpener getInstance() {
		if (instance == null) {
			instance = new FileOpener();
		}
		return instance;
	}
	
	private FileOpener() {
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
		
		FileNameExtensionFilter allImagesFilter = new FileNameExtensionFilter(
				FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.ALL_IMAGES),
				"jpg", "jpeg", "tif", "tiff", "png", "bmp");
		FileNameExtensionFilter pmbImagesFilter = new FileNameExtensionFilter(
				FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.BMP_IMAGES),
				"bmp");
		FileNameExtensionFilter pngImagesFilter = new FileNameExtensionFilter(
				FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.PNG_IMAGES),
				"png");
		FileNameExtensionFilter jpegImagesFilter = new FileNameExtensionFilter(
				FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.JPEG_IMAGES),
				"jpg", "jpeg");
		FileNameExtensionFilter tiffImagesFilter = new FileNameExtensionFilter(
				FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.TIFF_IMAGES),
				"tif", "tiff");
		
		setFileFilter(pmbImagesFilter);
		setFileFilter(pngImagesFilter);
		setFileFilter(jpegImagesFilter);
		setFileFilter(tiffImagesFilter);
		setFileFilter(allImagesFilter);
	}
	
	private void setTemplateFilter() {
		resetChoosableFileFilters();
		FileNameExtensionFilter templateFilter = new FileNameExtensionFilter(
				FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.TEMPLATE_FILE),
				"xtmpl");
		setFileFilter(templateFilter);
	}
}
