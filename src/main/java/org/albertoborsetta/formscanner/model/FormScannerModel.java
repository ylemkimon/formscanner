package org.albertoborsetta.formscanner.model;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.albertoborsetta.formscanner.gui.FileListFrame;
import org.albertoborsetta.formscanner.gui.FormScanner;
import org.albertoborsetta.formscanner.gui.RenameFileFrame;


public class FormScannerModel {

	private Map<String, File> fileList = new HashMap<String, File>();
	private List<String> openedFiles = new ArrayList<String>();
	private FileListFrame fileListFrame;
	private RenameFileFrame renameFileFrame;
	private FormScanner view;
	private int renamedFileIndex = 0;
    
	public FormScannerModel(FormScanner view) {
		this.view = view;
	}

	public void setFileList(File[] fileArray) {		
		for (File file: fileArray) {
			fileList.put(getFileName(file), file);
			openedFiles.add(getFileName(file));
		}
		if (!openedFiles.isEmpty()) {
			fileListFrame = new FileListFrame(openedFiles);
			view.arrangeFrame(fileListFrame);
		}
	}
	
	public void renameFiles() {
		if (!openedFiles.isEmpty()) {
			fileListFrame.selectFile(renamedFileIndex);
			view.arrangeFrame(fileListFrame);
			
			renamedFileIndex = 0;
			renameFileFrame = new RenameFileFrame(this, openedFiles.get(renamedFileIndex));
			view.arrangeFrame(renameFileFrame);
			
		}		
	}
	
	public void renameNextFile() {
		String newFileName = renameFileFrame.getNewFileName();
		String oldFileName = openedFiles.get(renamedFileIndex);
		
		if (!newFileName.equalsIgnoreCase(oldFileName)) {		
			File newFile = renameFile(newFileName, oldFileName);
		
			updateFileList(newFileName, oldFileName, newFile);
			
			updateOpenedFiles(newFileName);
		}
		
		// fileListFrame = new FileListFrame(openedFiles);
		fileListFrame.updateFileList(openedFiles);
		renamedFileIndex++;
		
		if (openedFiles.size()>renamedFileIndex) {			
			fileListFrame.selectFile(renamedFileIndex);
		}
		view.arrangeFrame(fileListFrame);
		
		if (openedFiles.size()>renamedFileIndex) {
			renameFileFrame.updateRenamedFile(openedFiles.get(renamedFileIndex));
			view.arrangeFrame(renameFileFrame);
		} else {
			view.disposeFrame(renameFileFrame);
		}
	}

	private void updateOpenedFiles(String newFileName) {
		openedFiles.remove(renamedFileIndex);
		openedFiles.add(renamedFileIndex, newFileName);
	}

	private void updateFileList(String newFileName, String oldFileName, File newFile) {
		fileList.remove(oldFileName);
		fileList.put(newFileName, newFile);
	}

	private File renameFile(String newFileName, String oldFileName) {
		File oldFile = fileList.get(oldFileName);
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
	
	private String getFileName(File file) {
		return file.getName();		
	}
}
