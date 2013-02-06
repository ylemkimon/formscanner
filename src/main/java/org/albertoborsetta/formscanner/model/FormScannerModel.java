package org.albertoborsetta.formscanner.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.albertoborsetta.formscanner.gui.FileListFrame;
import org.albertoborsetta.formscanner.gui.FormScanner;
import org.albertoborsetta.formscanner.gui.RenameFileFrame;
import org.apache.commons.io.FilenameUtils;

public class FormScannerModel {

	private Map<Integer, File> openedFiles = new HashMap<Integer, File>();
	private FileListFrame fileListFrame;
	private RenameFileFrame renameFileFrame;
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
			fileListFrame = new FileListFrame(getOpenedFileList());
			view.arrangeFrame(fileListFrame);
		}
	}
	
	public void renameFiles() {
		if (!openedFiles.isEmpty()) {
			int renamedFileIndex = fileListFrame.getSelectedItemIndex();
			fileListFrame.selectFile(renamedFileIndex);
			view.arrangeFrame(fileListFrame);
			
			renameFileFrame = new RenameFileFrame(this, getFileNameByIndex(renamedFileIndex)); 
			view.arrangeFrame(renameFileFrame);			
		}		
	}
	
	public void renameNextFile() {
		String newFileName = renameFileFrame.getNewFileName();
		String oldFileName = getFileNameByIndex(renamedFileIndex);
		
		if (!newFileName.equalsIgnoreCase(oldFileName)) {		
			File newFile = renameFile(renamedFileIndex, newFileName, oldFileName);
		
			updateFileList(renamedFileIndex, newFile);
		}
		
		fileListFrame.updateFileList(getOpenedFileList());
		renamedFileIndex++;
		
		if (openedFiles.size() > renamedFileIndex) {	
			fileListFrame.selectFile(renamedFileIndex);
		}
		view.arrangeFrame(fileListFrame);
		
		if (openedFiles.size()>renamedFileIndex) {
			renameFileFrame.updateRenamedFile(getFileNameByIndex(renamedFileIndex));
			view.arrangeFrame(renameFileFrame);
		} else {
			view.disposeFrame(renameFileFrame);
		}
	}

	private void updateFileList(Integer index, File file) {
		openedFiles.remove(index);
		openedFiles.put(index, file);
	}

	private File renameFile(int index, String newFileName, String oldFileName) {
		File oldFile = openedFiles.get(oldFileName);
		String absolutePath = oldFile.getPath();
		String filePath = absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));

		File newFile = new File(filePath + File.separator + newFileName);
 
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
	
}
