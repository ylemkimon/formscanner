package org.albertoborsetta.formscanner.model;

import org.albertoborsetta.formscanner.commons.Constants;
import org.albertoborsetta.formscanner.gui.FileListFrame;
import org.albertoborsetta.formscanner.gui.FormScanner;
import org.albertoborsetta.formscanner.gui.RenameFileFrame;
import org.albertoborsetta.formscanner.gui.RenameFileImageFrame;

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
		}
	}
	
	public void renameFiles(int action) {
		switch (action) {
		case Constants.RENAME_FILE_FIRST:
			if (!openedFiles.isEmpty()) {
				renamedFileIndex = fileListFrame.getSelectedItemIndex();
				fileListFrame.selectFile(renamedFileIndex);
				
				renameFileImageFrame = new RenameFileImageFrame(this, openedFiles.get(renamedFileIndex));
				view.arrangeFrame(renameFileImageFrame);
				
				renameFileFrame = new RenameFileFrame(this, getFileNameByIndex(renamedFileIndex)); 
				view.arrangeFrame(renameFileFrame);				
			}			
			break;
		case Constants.RENAME_FILE_CURRENT:
			String newFileName = renameFileFrame.getNewFileName();
			String oldFileName = getFileNameByIndex(renamedFileIndex);
			
			if (!newFileName.equalsIgnoreCase(oldFileName)) {		
				File newFile = renameFile(renamedFileIndex, newFileName, oldFileName);
			
				updateFileList(renamedFileIndex, newFile);
			}
			
			fileListFrame.updateFileList(getOpenedFileList());
		case Constants.RENAME_FILE_SKIP:
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

	private void updateFileList(Integer index, File file) {
		openedFiles.remove(index);
		openedFiles.put(index, file);
	}

	private File renameFile(int index, String newFileName, String oldFileName) {
		File oldFile = openedFiles.get(index);
		
		String filePath = FilenameUtils.getFullPath(oldFile.getAbsolutePath());

		File newFile = new File(filePath + newFileName);
 
		if (newFile.exists()) {
			// TODO
			System.out.println("file already exists!");
			newFile = oldFile;
		}

		if (oldFile.renameTo(newFile)){
			System.out.println("Rename succesful");
		} else {
			// TODO
			System.out.println("Rename failed");
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
