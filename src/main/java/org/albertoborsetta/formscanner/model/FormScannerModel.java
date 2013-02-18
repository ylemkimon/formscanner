package org.albertoborsetta.formscanner.model;

import net.sourceforge.jiu.data.Gray8Image;

import org.albertoborsetta.formscanner.commons.Constants.Actions;
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
    
	public FormScannerModel(FormScanner view) {
		this.view = view;
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
								
				System.out.println("Analyze current file");
				Gray8Image grayimage = ImageUtil.readImage(getFileNameByIndex(analyzedFileIndex));

		        ImageManipulation image = new ImageManipulation(grayimage);
		        image.locateConcentricCircles();

		        image.readConfig("/home/tecnoteca/workspace/formscanner-project/src/main/resources/template.config");
		        image.readFields("/home/tecnoteca/workspace/formscanner-project/src/main/resources/template.fields");
		        image.readAscTemplate("/home/tecnoteca/workspace/formscanner-project/src/main/resources/template.asc");
		        image.searchMarks();
		        image.saveData("/home/tecnoteca/Scrivania/results.dat");
				
				
				System.out.println("Create/Update frames");
				// analyzeFileImageFrame = new AnalyzeFileImageFrame(this, openedFiles.get(analyzedFileIndex));
				// view.arrangeFrame(analyzeFileImageFrame);
				
				// analyzeFileGridFrame = new AnalyzeFileGridFrame(this, getFileNameByIndex(analyzedFileIndex)); 
				// view.arrangeFrame(analyzeFileGridFrame);
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
				// analyzeFileImageFrame.updateImage(openedFiles.get(analyzedFileIndex));
			} else {
				System.out.println("Dispose analyze frame");
				// view.disposeFrame(analyzeFileGridFrame);
				// view.disposeFrame(analyzeFileImageFrame);
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
}
