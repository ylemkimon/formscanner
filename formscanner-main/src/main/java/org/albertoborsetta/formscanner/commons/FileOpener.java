package org.albertoborsetta.formscanner.commons;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

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
	
	public File[] chooseImages() {
		File[] files = null;
		setMultiSelectionEnabled(true);
		setImagesFilter();
		int returnValue = showOpenDialog(null);
		
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			files = getSelectedFiles();
        }
		return files;
	}
	
	public File chooseImage() {
		File file = null;
		setMultiSelectionEnabled(false);
		setImagesFilter();
		int returnValue = showOpenDialog(null);
		
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			file = getSelectedFile();
        }
		
		return file;		
	}
	
	private void setImagesFilter() {
		resetChoosableFileFilters();
		
		FileNameExtensionFilter allImagesFilter = new FileNameExtensionFilter("Tutte le immagini", "jpg", "jpeg", "tif", "tiff", "png", "bmp");
		FileNameExtensionFilter pmbImagesFilter = new FileNameExtensionFilter("Immagine BMP (*.bmp)", "pmb");
		FileNameExtensionFilter pngImagesFilter = new FileNameExtensionFilter("Immagine PNG (*.png)", "png");
		FileNameExtensionFilter jpegImagesFilter = new FileNameExtensionFilter("Immagine JPEG (*.jpg, *.jpeg)", "jpg", "jpeg");
		FileNameExtensionFilter tiffImagesFilter = new FileNameExtensionFilter("Immagine TIFF (*.tif, *.tiff)", "tif", "tiff");
		
		setFileFilter(pmbImagesFilter);
		setFileFilter(pngImagesFilter);
		setFileFilter(jpegImagesFilter);
		setFileFilter(tiffImagesFilter);
		setFileFilter(allImagesFilter);
	}
}
