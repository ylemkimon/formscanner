package org.albertoborsetta.formscanner.model;

import net.sourceforge.jiu.data.Gray8Image;

import org.albertoborsetta.formscanner.commons.FormScannerConstants.Actions;
import org.albertoborsetta.formscanner.commons.configuration.FormScannerConfiguration;
import org.albertoborsetta.formscanner.commons.configuration.FormScannerConfigurationKeys;
import org.albertoborsetta.formscanner.commons.resources.FormScannerResources;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import org.albertoborsetta.formscanner.gui.AnalyzeFileImageFrame;
import org.albertoborsetta.formscanner.gui.AnalyzeFileResultsFrame;
import org.albertoborsetta.formscanner.gui.FileListFrame;
import org.albertoborsetta.formscanner.gui.FormScanner;
import org.albertoborsetta.formscanner.gui.RenameFileFrame;
import org.albertoborsetta.formscanner.gui.RenameFileImageFrame;
import org.albertoborsetta.formscanner.parser.ImageManipulation;
import org.albertoborsetta.formscanner.parser.ImageUtil;

import java.awt.Dimension;
import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;

import org.apache.commons.io.FilenameUtils;

public class FormScannerModel {

	private Map<Integer, File> openedFiles = new HashMap<Integer, File>();
	private FileListFrame fileListFrame;
	private RenameFileFrame renameFileFrame;
	private RenameFileImageFrame renameFileImageFrame;
	private FormScanner view;
	private int renamedFileIndex = 0;
	private int analyzedFileIndex = 0;
	
	private FormScannerConfiguration configurations;
	private FormScannerTranslation translations;
	private FormScannerResources resources;
	private AnalyzeFileImageFrame analyzeFileImageFrame;
	private AnalyzeFileResultsFrame analyzeFileResultsFrame;
	
	private ImageManipulation image;
    
	public FormScannerModel(FormScanner view) {
		this.view = view;
		
		String path = System.getProperty("FormScanner_HOME");		
		configurations = FormScannerConfiguration.getConfiguration(path);
		
		String lang = configurations.getProperty(FormScannerConfigurationKeys.LANG, FormScannerConfigurationKeys.DEFAULT_LANG);
		translations = FormScannerTranslation.getTranslation(path, lang);
		resources = FormScannerResources.getResources(path);
		resources.setTemplate(configurations.getProperty(FormScannerConfigurationKeys.TEMPLATE, FormScannerConfigurationKeys.DEFAULT_TEMPLATE));
	}

	public void openFiles(File[] fileArray) {
		Integer fileIndex = 0;
		for (File file: fileArray) {
			openedFiles.put(fileIndex++, file);
		}
		if (!openedFiles.isEmpty()) {
			fileListFrame = new FileListFrame(this, getOpenedFileList());
			view.arrangeFrame(fileListFrame);
			view.setRenameControllersEnabled(true);
			view.setScanControllersEnabled(true);
		}
	}
	
	public void renameFiles(String action) {
		Actions act = Actions.valueOf(action);
		switch (act) {
		case RENAME_FILE_FIRST:
			if (!openedFiles.isEmpty()) {
				renamedFileIndex = fileListFrame.getSelectedItemIndex();
				fileListFrame.selectFile(renamedFileIndex);
				
				renameFileImageFrame = new RenameFileImageFrame(this, openedFiles.get(renamedFileIndex));
				view.arrangeFrame(renameFileImageFrame);
				
				renameFileFrame = new RenameFileFrame(this, getFileNameByIndex(renamedFileIndex)); 
				view.arrangeFrame(renameFileFrame);
			}			
			break;
		case RENAME_FILE_CURRENT:
			String newFileName = renameFileFrame.getNewFileName();
			String oldFileName = getFileNameByIndex(renamedFileIndex);
			
			if (!newFileName.equalsIgnoreCase(oldFileName)) {		
				File newFile = renameFile(renamedFileIndex, newFileName, oldFileName);
			
				updateFileList(renamedFileIndex, newFile);
			}
			
			fileListFrame.updateFileList(getOpenedFileList());
		case RENAME_FILE_SKIP:
			renamedFileIndex++;
			
			if (openedFiles.size() > renamedFileIndex) {	
				fileListFrame.selectFile(renamedFileIndex);
			}
			
			if (openedFiles.size()>renamedFileIndex) {
				renameFileFrame.updateRenamedFile(getFileNameByIndex(renamedFileIndex));
				renameFileImageFrame.updateImage(openedFiles.get(renamedFileIndex));
			} else {
				view.disposeFrame(renameFileFrame);
				view.disposeFrame(renameFileImageFrame);
			}
			break;
		default:
			break;
		}		
	}
	
	public void analyzeFiles(String action) {
		Actions act = Actions.valueOf(action);
		switch (act) {
		case ANALYZE_FILE_FIRST:
			if (!openedFiles.isEmpty()) {
				analyzedFileIndex  = fileListFrame.getSelectedItemIndex();
				fileListFrame.selectFile(analyzedFileIndex);

				Gray8Image grayimage = ImageUtil.readImage(openedFiles.get(renamedFileIndex).getAbsolutePath());

		        image = new ImageManipulation(grayimage);
		        image.locateConcentricCircles();
		        
		        image.readConfig(resources.getTemplateConfig());
		        image.readFields(resources.getTemplateFields());
		        image.readAscTemplate(resources.getTemplateAsc());
		        image.searchMarks();
		        image.saveData("/home/tecnoteca/Scrivania/results.dat");
			
				analyzeFileImageFrame = new AnalyzeFileImageFrame(this, openedFiles.get(analyzedFileIndex));
				view.arrangeFrame(analyzeFileImageFrame);
				
				analyzeFileResultsFrame = new AnalyzeFileResultsFrame(this); 
				view.arrangeFrame(analyzeFileResultsFrame);				
			}			
			break;
		case ANALYZE_FILE_NEXT:
			System.out.println("Analyze Next file");
			System.out.println("Update analyze frame");
			break;
		case ANALYZE_FILE_SKIP:
			analyzedFileIndex++;
			
			if (openedFiles.size() > analyzedFileIndex) {	
				fileListFrame.selectFile(analyzedFileIndex);
			}
			
			if (openedFiles.size()>analyzedFileIndex) {
				System.out.println("Update analyze frame");
				// analyzeFileGridFrame.updateRenamedFile(getFileNameByIndex(analyzedFileIndex));
				analyzeFileImageFrame.updateImage(openedFiles.get(analyzedFileIndex));
			} else {
				System.out.println("Dispose analyze frame");
				// view.disposeFrame(analyzeFileGridFrame);
				view.disposeFrame(analyzeFileImageFrame);
			}
			break;
		default:
			break;
		}		
	}

	private void updateFileList(Integer index, File file) {
		openedFiles.remove(index);
		openedFiles.put(index, file);
	}

	private File renameFile(int index, String newFileName, String oldFileName) {
		File oldFile = openedFiles.get(index);
		
		String filePath = FilenameUtils.getFullPath(oldFile.getAbsolutePath());

		File newFile = new File(filePath + newFileName);
 
		if (newFile.exists()) {
			newFile = oldFile;
		}

		if (!oldFile.renameTo(newFile)) {
			newFile = oldFile;
		}
		return newFile;
	}
	
	private List<String> getOpenedFileList() {
		List<String> fileList = new ArrayList<String>();
		
		for (int i = 0; i < openedFiles.size(); i++) {
			fileList.add(getFileNameByIndex(i));
		}
		
		return fileList;
	}

	private String getFileNameByIndex(int index) {
		return openedFiles.get(index).getName();
	}
	
	public Dimension getDesktopSize() {
		return view.getDesktopSize();
	}
	
	public void disposeRelatedFrame(JInternalFrame frame) {
		if (frame.getName().equals("renameFileFrame")) {
			view.disposeFrame(renameFileImageFrame);
		} else {
			view.disposeFrame(renameFileFrame);
		}
	}
	
	public String getTranslationFor(String key) {
		String value = translations.getProperty(key, key);
		return value;
	}
	
	public char getMnemonicFor(String key) {
		char value = translations.getProperty(key).charAt(0);
		return value;
	}
	
	public ImageIcon getIconFor(String key) {
		ImageIcon icon = resources.getIconFor(key);
		return icon;
	}
	
	public int getNumFields() {
		return image.getNumfields();
	} 
}
