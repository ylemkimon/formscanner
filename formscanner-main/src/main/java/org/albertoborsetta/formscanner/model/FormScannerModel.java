package org.albertoborsetta.formscanner.model;

import org.albertoborsetta.formscanner.commons.FormField;
import org.albertoborsetta.formscanner.commons.FormPoint;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Action;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Corners;
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
import org.albertoborsetta.formscanner.imageparser.ScanImage;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.io.File;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;

import org.apache.commons.io.FilenameUtils;

public class FormScannerModel {

	private HashMap<Integer, File> openedFiles = new HashMap<Integer, File>();
	private File template;
	private FileListFrame fileListFrame;
	private RenameFileFrame renameFileFrame;
	private FormScanner view;
	private int renamedFileIndex = 0;
	private int analyzedFileIndex = 0;
	
	private FormScannerConfiguration configurations;
	private FormScannerTranslation translations;
	private FormScannerResources resources;
	// private AnalyzeFileResultsFrame analyzeFileResultsFrame;
	private ManageTemplateFrame manageTemplateFrame;
	private ImageFrame imageFrame;
	private FormTemplate formTemplate;
    
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
		Action act = Action.valueOf(action);
		switch (act) {
		case RENAME_FILE_FIRST:
			if (!openedFiles.isEmpty()) {
				renamedFileIndex = fileListFrame.getSelectedItemIndex();
				fileListFrame.selectFile(renamedFileIndex);
				
				createImageFrame(openedFiles.get(renamedFileIndex), Mode.VIEW);
				
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
			
				createImageFrame(openedFiles.get(analyzedFileIndex), Mode.VIEW);
				
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
			manageTemplateFrame = new ManageTemplateFrame(this, template);
			
			view.arrangeFrame(manageTemplateFrame);	
			formTemplate = new FormTemplate(template.getName());
		}
	}

	public void zoomImage(ScrollableImageView view, int direction) {
		if ((direction>0 && (view.getScaleFactor()<1.5)) || ((direction<0) && (view.getScaleFactor()>0.4))) {
			view.zoomImage((double)direction/10);
		}
	}
	
	public void createImageFrame(File file, Mode mode) {
		imageFrame = new ImageFrame(this, file, mode);
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
	
	public void addPoint(ImageView view, FormPoint p) {
		if (manageTemplateFrame!=null) {
			int rows = manageTemplateFrame.getRowsNumber();
			int values = manageTemplateFrame.getValuesNumber();
			
			List<FormPoint> points = view.getPoints();
			/*
			if (rows==1 && values==1) {
				if (points.size() == 0) {
					view.addPoint(p);
					manageTemplateFrame.setupTable(view.getPoints());
				}
				/*
				else {
					view.removePoint(p);
					manageTemplateFrame.clearTable();
				}
				*/
			// } else {  // rows > 1 or values > 1
				if (points.size() == 0) {
					view.addPoint(p);
					manageTemplateFrame.setupTable(view.getPoints());
				} else {
					view.removeAllPoints();
					FormPoint p1 = points.get(0);
					double alfa = formTemplate.getRotation();
					
					double x3 = p.getX();
					double y3 = p1.getY();
					
					if (alfa != 0) {
						double m1 = Math.tan(alfa);
						double m2 = Math.tan(alfa - 90);
						
						double q1 = p1.getY() - (p1.getX() * m1);
						double q2 = p.getY() - (p.getX() * m2);
						
						x3 = (q2 - q1) / (m1 - m2);
						y3 = (m1 * x3) + q1;
					}
					
					double rowdx = (x3 - p1.getX()) / values;
					double rowdy = (y3 - p1.getY()) / values;
					
					double coldx = (x3 - p.getX()) / rows;
					double coldy = (y3 - p.getY()) / rows;
					
					for (int i=0; i<rows; i++) {
						for (int j=0; j<values; j++) {
							FormPoint pi = new FormPoint((int) (p1.x+(coldx*i)+(rowdx*j)),(int) (p1.y+(coldy*i)+(rowdy*j)));
							view.addPoint(pi);
						}						
					}
					/*
					if (rows == 1) { // rows = 1, values > 1
						view.removeAllPoints();
						
						int dx = (p.x - p1.x)/(values-1);
						int y = p1.y + ((p.y - p1.y)/2);
						
						for (int i=0; i<values; i++) {
							Point pi = new Point(p1.x+(dx*i), y);
							view.addPoint(pi);
						}
						manageTemplateFrame.setupTable(view.getPoints());
					} else if (values == 1) { // rows > 1, values =1
						view.removeAllPoints();
						
						int x = p1.x + ((p.x - p1.x)/2);
						int dy = (p.y - p1.y)/(rows-1);
						
						for (int i=0; i<rows; i++) {
							Point pi = new Point(x, p1.y+(dy*i));
							view.addPoint(pi);
						}
						manageTemplateFrame.setupTable(view.getPoints());
					} else { // rows > 1, values > 1
						view.removeAllPoints();
						
						int dx = (p.x - p1.x)/(values-1);
						int dy = (p.y - p1.y)/(rows-1);					
						
						for (int i=0; i<rows; i++) {
							for (int j=0; j<values; j++) {
								Point pi = new Point(p1.x+(dx*j), p1.y+(dy*i));
								view.addPoint(pi);
							}						
						}
						manageTemplateFrame.setupTable(view.getPoints());
					}
				}
				*/
			}
		}
	}
	
	public void removePoint(ImageView view, FormPoint p) {
		view.removePoint(p);
	}

	public void removeAllPoints(ImageView view) {
		view.removeAllPoints();
	}

	public void addFields(HashMap<String, FormField> fields) {
		for (Map.Entry<String, FormField> field : fields.entrySet()) {
			formTemplate.setField(field.getKey(), field.getValue());
        } 
	}

	public void calculateTemplateCorners() {
		HashMap<Corners, FormPoint> corners;
		ScanImage imageScan = new ScanImage(template);
        corners = imageScan.locateCorners();
        formTemplate.setCorners(corners);
	}

	public HashMap<Corners, FormPoint> getTemplateCorners() {
		return formTemplate.getCorners();
	}

	public void calculateRotation() {
		HashMap<Corners, FormPoint> corners = formTemplate.getCorners();
		
		FormPoint topLeftPoint = corners.get(Corners.TOP_LEFT);
		FormPoint topRightPoint = corners.get(Corners.TOP_RIGHT);
		
		double dx = (double) (topRightPoint.getX() - topLeftPoint.getX());
		double dy = (double) (topRightPoint.getY() - topLeftPoint.getY());
		
		double rotation = Math.atan(dx/dy);
		formTemplate.setRotation(rotation);
	}
}
