package org.albertoborsetta.formscanner.model;

import org.albertoborsetta.formscanner.commons.FormField;
import org.albertoborsetta.formscanner.commons.FormFileUtils;
import org.albertoborsetta.formscanner.commons.FormPoint;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Action;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Frame;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Mode;
import org.albertoborsetta.formscanner.commons.FormTemplate;
import org.albertoborsetta.formscanner.commons.configuration.FormScannerConfiguration;
import org.albertoborsetta.formscanner.commons.configuration.FormScannerConfigurationKeys;
import org.albertoborsetta.formscanner.commons.resources.FormScannerResources;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import org.albertoborsetta.formscanner.gui.FileListFrame;
import org.albertoborsetta.formscanner.gui.FormScanner;
import org.albertoborsetta.formscanner.gui.ImageView;
import org.albertoborsetta.formscanner.gui.ManageTemplateFrame;
import org.albertoborsetta.formscanner.gui.ImageFrame;
import org.albertoborsetta.formscanner.gui.RenameFileFrame;
import org.albertoborsetta.formscanner.gui.ScrollableImageView;
import org.albertoborsetta.formscanner.gui.TabbedView;

import java.awt.Cursor;
import java.awt.Dimension;
import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JInternalFrame;

import org.apache.commons.io.FilenameUtils;

public class FormScannerModel {
	
	public static final String COL_DX = "COL_DX";
	public static final String COL_DY = "COL_DY";
	public static final String ROW_DX = "ROW_DX";
	public static final String ROW_DY = "ROW_DY";
	
	private String path;
	private HashMap<Integer, File> openedFiles = new HashMap<Integer, File>();
	private FileListFrame fileListFrame;
	private RenameFileFrame renameFileFrame;
	private FormScanner view;
	private int renamedFileIndex = 0;
	private int analyzedFileIndex = 0;
	
	private FormScannerConfiguration configurations;
	// private AnalyzeFileResultsFrame analyzeFileResultsFrame;
	private ManageTemplateFrame manageTemplateFrame;
	private ImageFrame imageFrame;
	private FormTemplate formTemplate;
	private File templateImage;
	FormFileUtils fileUtils = FormFileUtils.getInstance();
	
	private ArrayList<FormPoint> points = new ArrayList<FormPoint>();
    
	public FormScannerModel(FormScanner view) {
		this.view = view;
		
		path = System.getProperty("FormScanner_HOME");		
		configurations = FormScannerConfiguration.getConfiguration(path);
		
		String lang = configurations.getProperty(FormScannerConfigurationKeys.LANG, FormScannerConfigurationKeys.DEFAULT_LANG);
		FormScannerTranslation.setTranslation(path, lang);
		FormScannerResources.setResources(path);
		FormScannerResources.setTemplate(configurations.getProperty(FormScannerConfigurationKeys.TEMPLATE, FormScannerConfigurationKeys.DEFAULT_TEMPLATE));
	}

	public void openImages() {
		File[] fileArray = fileUtils.chooseImages();
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
		Action act = Action.valueOf(action);
		switch (act) {
		case RENAME_FILE_FIRST:
			if (!openedFiles.isEmpty()) {
				renamedFileIndex = fileListFrame.getSelectedItemIndex();
				fileListFrame.selectFile(renamedFileIndex);
				File imageFile = openedFiles.get(renamedFileIndex);
				FormTemplate template = new FormTemplate(FilenameUtils.removeExtension(imageFile.getName()));
				template.findCorners(imageFile);
				
				createFormImageFrame(imageFile, template, Mode.VIEW);
				
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
				imageFrame.updateImage(openedFiles.get(renamedFileIndex));
				renameFileFrame.updateRenamedFile(getFileNameByIndex(renamedFileIndex));
				view.arrangeFrame(renameFileFrame);
			} else {
				view.disposeFrame(renameFileFrame);
				view.disposeFrame(imageFrame);
			}
			break;
		default:
			break;
		}		
	}
	
	public void analyzeFiles(String action) {
		Action act = Action.valueOf(action);
		switch (act) {
		case ANALYZE_FILE_FIRST:
			if (!openedFiles.isEmpty()) {
				analyzedFileIndex  = fileListFrame.getSelectedItemIndex();
				fileListFrame.selectFile(analyzedFileIndex);
				File imageFile = openedFiles.get(analyzedFileIndex);
				FormTemplate template = new FormTemplate(FilenameUtils.removeExtension(imageFile.getName()));
				template.findCorners(imageFile);
			
				createFormImageFrame(imageFile, template, Mode.VIEW);
				
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
				imageFrame.updateImage(openedFiles.get(analyzedFileIndex));
			} else {
				System.out.println("Dispose analyze frame");
				// view.disposeFrame(analyzeFileGridFrame);
				view.disposeFrame(imageFrame);
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
		Frame frm = Frame.valueOf(frame.getName());
		switch (frm) {
		case RENAME_FILE_FRAME_NAME:
			view.disposeFrame(imageFrame);
			break;
		case IMAGE_FRAME_NAME:
			view.disposeFrame(renameFileFrame);
			view.disposeFrame(manageTemplateFrame);
			break;
		case MANAGE_TEMPLATE_FRAME_NAME:
			view.disposeFrame(imageFrame);
			break;			
		default:
			break;
		}
	}

	public void loadTemplate() {
		templateImage = fileUtils.chooseImage();
		if (templateImage != null) {
			formTemplate = new FormTemplate(FilenameUtils.removeExtension(templateImage.getName()));
			formTemplate.findCorners(templateImage);
			manageTemplateFrame = new ManageTemplateFrame(this);
			
			view.arrangeFrame(manageTemplateFrame);	
		}
	}
	
	public void createTemplateImageFrame() {
		imageFrame = new ImageFrame(this, templateImage, Mode.UPDATE);
		imageFrame.setTemplate(formTemplate);
		view.arrangeFrame(imageFrame);
	}
	
	public void createFormImageFrame(File image, FormTemplate template, Mode mode) {
		imageFrame = new ImageFrame(this, image, mode);
		imageFrame.setTemplate(template);
		view.arrangeFrame(imageFrame);
	}
	
	public void setImageCursor(ImageView view, Cursor cursor) {
		view.setImageCursor(cursor);
	}
	
	public void setScrollBars(ScrollableImageView view, int deltaX, int deltaY) {
		view.setScrollBars(deltaX, deltaY);
	}
	
	public void setNextTab(String action, TabbedView view) {
		view.setupNextTab(action);		
	}
	
	public void setAdvanceable(TabbedView view) {
		view.setAdvanceable();
	}
	
	public void addPoint(ImageView view, FormPoint p2) {
		if (manageTemplateFrame!=null) {
			int rows = manageTemplateFrame.getRowsNumber();
			int values = manageTemplateFrame.getValuesNumber();
			
			if (rows==1 && values==1) {
				if (points.isEmpty()) {
					points.add(p2);
					view.update();
					manageTemplateFrame.setupTable(points);
					manageTemplateFrame.toFront();
				}
			} else {
				if (points.isEmpty()) {
					points.add(p2);
					view.update();
				} else {
					FormPoint p1 = points.get(0);
					points.clear();		
					
					FormPoint p3 = calculateThirdPoint(p2, p1);
					
					HashMap<String, Double> delta = calculateDelta(p1, p2, p3);
					
					for (int i=0; i<rows; i++) {
						for (int j=0; j<values; j++) {
							FormPoint pi = new FormPoint((int) (p1.x+(delta.get(COL_DX)*i)+(delta.get(ROW_DX)*j)),(int) (p1.y+(delta.get(COL_DY)*i)+(delta.get(ROW_DY)*j)));
							points.add(pi);
						}						
					}
					view.update();
					manageTemplateFrame.setupTable(points);
					manageTemplateFrame.toFront();
				}
			}
		}
	}

	private HashMap<String, Double> calculateDelta(FormPoint p1, FormPoint p2, FormPoint p3) {
		int rows = manageTemplateFrame.getRowsNumber();
		int values = manageTemplateFrame.getValuesNumber();
		
		HashMap<String, Double> delta = new HashMap<String, Double>();
		
		int rowDivider = ((values > 1)?(values - 1):1);
		int colDivider = ((rows > 1)?(rows - 1):1);
		
		switch (manageTemplateFrame.getFieldType()) {
		case QUESTIONS_BY_ROWS:
			delta.put(ROW_DX, (p3.getX() - p1.getX()) / rowDivider);
			delta.put(ROW_DY, (p3.getY() - p1.getY()) / rowDivider);

			delta.put(COL_DX, (p2.getX() - p3.getX()) / colDivider);
			delta.put(COL_DY, (p2.getY() - p3.getY()) / colDivider);
			break;
		case QUESTIONS_BY_COLS:
			delta.put(ROW_DX, (p2.getX() - p3.getX()) / rowDivider);
			delta.put(ROW_DY, (p2.getY() - p3.getY()) / rowDivider);

			delta.put(COL_DX, (p3.getX() - p1.getX()) / colDivider);
			delta.put(COL_DY, (p3.getY() - p1.getY()) / colDivider);
			break;
		default:
			break;	
		}
		return delta;
	}

	private FormPoint calculateThirdPoint(FormPoint p, FormPoint p1) {
		double alfa = formTemplate.getRotation();
		
		double x3 = p.getX();
		double y3 = p1.getY();
		
		if (alfa != 0) {
			double m1 = Math.tan(alfa);
			double m2 = Math.tan(alfa - Math.PI/2);
			
			double q1 = p1.getY() + (p1.getX() * m1);
			double q2 = p.getY() + (p.getX() * m2);
			
			x3 = (q1 - q2) / (m1 - m2);
			y3 = q1 - (m1 * x3);
		}
		
		FormPoint p3 = new FormPoint((int) x3, (int) y3);
		return p3;
	}

	public void updateTemplate(HashMap<String, FormField> fields) {
		formTemplate.setFields(fields);
		points.clear();		
	}

	public ArrayList<FormPoint> getPoints() {
		return points;
	}

	public void removeField(TabbedView view) {
		String fieldName = view.getSelectedItem();
		formTemplate.removeFieldByName(fieldName);
		view.removeFieldByName(fieldName);
	}

	public void enableRemoveFields(TabbedView view) {
		view.enableRemoveFields();
	}

	public void saveTemplate(TabbedView view) {
		formTemplate.saveToFile(path);
		view.dispose();
	}
	
	public void exitFormScanner() {
		view.dispose();
		System.exit(0);
	}

	public void openTemplate() {
		System.out.println("Open template file");
		File template = fileUtils.chooseTemplate();
		formTemplate = new FormTemplate(FilenameUtils.removeExtension(template.getName()));
		System.out.println("Parse template XML");
		System.out.println("Setup template");
		formTemplate.presetFromTemplate(template);
	}
}