package org.albertoborsetta.formscanner.model;

import org.albertoborsetta.formscanner.commons.FormScannerConstants.Actions;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Frames;
import org.albertoborsetta.formscanner.commons.configuration.FormScannerConfiguration;
import org.albertoborsetta.formscanner.commons.configuration.FormScannerConfigurationKeys;
import org.albertoborsetta.formscanner.commons.resources.FormScannerResources;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import org.albertoborsetta.formscanner.controller.ImageController;
import org.albertoborsetta.formscanner.controller.ScrollableImageController;
import org.albertoborsetta.formscanner.gui.AnalyzeFileImageFrame;
import org.albertoborsetta.formscanner.gui.FileListFrame;
import org.albertoborsetta.formscanner.gui.FormScanner;
import org.albertoborsetta.formscanner.gui.ManageTemplateFrame;
import org.albertoborsetta.formscanner.gui.ManageTemplateImageFrame;
import org.albertoborsetta.formscanner.gui.RenameFileFrame;
import org.albertoborsetta.formscanner.gui.RenameFileImageFrame;
import org.albertoborsetta.formscanner.gui.ZoomImageFrame;

import java.awt.Cursor;
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
	private File template;
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
	// private AnalyzeFileResultsFrame analyzeFileResultsFrame;
	private ManageTemplateFrame manageTemplateFrame;
	private ManageTemplateImageFrame manageTemplateImageFrame;
	private ZoomImageFrame zoomImageFrame;
    
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
		if (fileArray != null) {
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
			
				analyzeFileImageFrame = new AnalyzeFileImageFrame(this, openedFiles.get(analyzedFileIndex));
				view.arrangeFrame(analyzeFileImageFrame);
				
				// analyzeFileResultsFrame = new AnalyzeFileResultsFrame(this, partialResults, totalResults); 
				// view.arrangeFrame(analyzeFileResultsFrame);				
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
		Frames frm = Frames.valueOf(frame.getName());
		switch (frm) {
		case RENAME_FILE_FRAME_NAME:
			view.disposeFrame(renameFileImageFrame);
			break;
		case RENAME_FILE_IMAGE_FRAME_NAME:
			view.disposeFrame(renameFileFrame);
			break;
		case MANAGE_TEMPLATE_FRAME_NAME:
			view.disposeFrame(manageTemplateImageFrame);
			view.disposeFrame(zoomImageFrame);
			break;
		case MANAGE_TEMPLATE_IMAGE_FRAME_NAME:
			view.disposeFrame(manageTemplateFrame);
			view.disposeFrame(zoomImageFrame);
			break;			
		default:
			break;
		}
	}
	
	public String getTranslationFor(String key) {
		String value = translations.getProperty(key, key);
		return value;
	}
	
	public char getMnemonicFor(String key) {
		char value = translations.getProperty(key, key).charAt(0);
		return value;
	}
	
	public ImageIcon getIconFor(String key) {
		ImageIcon icon = resources.getIconFor(key);
		return icon;
	}

	public void loadTemplate(File template) {
		if (template != null) {
			this.template= template; 
			manageTemplateFrame = new ManageTemplateFrame(this, template.getName());
			manageTemplateImageFrame = new ManageTemplateImageFrame(this, template);
			zoomImageFrame = new ZoomImageFrame(this, template);
			manageTemplateImageFrame.addZoom(zoomImageFrame);
			
			view.arrangeFrame(manageTemplateFrame);
			view.arrangeFrame(manageTemplateImageFrame);
			view.arrangeFrame(zoomImageFrame);
		}
	}
	
	public void showZoom(ZoomImageFrame view, int x, int y) {
		view.showImage(x, y);
	}
	
	public void drawRect(ImageController controller, int x, int y, int width, int height) {
		controller.getView().drawRect(x, y, width, height);
	}
	
	public void setMoveCursor(ImageController controller) {
		controller.getView().setImageCursor(new Cursor(Cursor.MOVE_CURSOR));
	}
	
	public void setDefaultCursor(ImageController controller) {
		controller.getView().setImageCursor(new Cursor(Cursor.MOVE_CURSOR));
	}
	
	public void setCrossCursor(ImageController controller) {
		controller.getView().setImageCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
	}
	
	public void setScrollBars(ScrollableImageController controller, int deltaX, int deltaY) {
		controller.getView().setScrollBars(deltaX, deltaY);
	}
}
