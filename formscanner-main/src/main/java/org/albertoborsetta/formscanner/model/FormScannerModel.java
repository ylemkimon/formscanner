package org.albertoborsetta.formscanner.model;

import org.albertoborsetta.formscanner.commons.FormField;
import org.albertoborsetta.formscanner.commons.FormFileUtils;
import org.albertoborsetta.formscanner.commons.FormPoint;
import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Action;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Corners;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Frame;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Mode;
import org.albertoborsetta.formscanner.commons.FormTemplate;
import org.albertoborsetta.formscanner.commons.configuration.FormScannerConfiguration;
import org.albertoborsetta.formscanner.commons.configuration.FormScannerConfigurationKeys;
import org.albertoborsetta.formscanner.commons.resources.FormScannerResources;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.gui.AboutFrame;
import org.albertoborsetta.formscanner.gui.FileListFrame;
import org.albertoborsetta.formscanner.gui.FormScanner;
import org.albertoborsetta.formscanner.gui.ImageView;
import org.albertoborsetta.formscanner.gui.ManageTemplateFrame;
import org.albertoborsetta.formscanner.gui.ImageFrame;
import org.albertoborsetta.formscanner.gui.OptionsFrame;
import org.albertoborsetta.formscanner.gui.RenameFileFrame;
import org.albertoborsetta.formscanner.gui.ScrollableImageView;
import org.albertoborsetta.formscanner.gui.TabbedView;

import java.awt.Cursor;
import java.awt.Dimension;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

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
	private FormFileUtils fileUtils = FormFileUtils.getInstance();
	private HashMap<String, FormTemplate> filledForms = new HashMap<String, FormTemplate>();

	private ArrayList<FormPoint> points = new ArrayList<FormPoint>();
	private String lang;
	private int threshold;
	private int density;

	public FormScannerModel(FormScanner view) {
		this.view = view;

		path = System.getProperty("FormScanner_HOME");
		configurations = FormScannerConfiguration.getConfiguration(path);

		lang = configurations.getProperty(FormScannerConfigurationKeys.LANG,
				FormScannerConfigurationKeys.DEFAULT_LANG);
		threshold = Integer.valueOf(configurations.getProperty(
				FormScannerConfigurationKeys.THRESHOLD,
				FormScannerConfigurationKeys.DEFAULT_THRESHOLD));
		density = Integer.valueOf(configurations.getProperty(
				FormScannerConfigurationKeys.DENSITY,
				FormScannerConfigurationKeys.DEFAULT_DENSITY));
		FormScannerTranslation.setTranslation(path, lang);
		FormScannerResources.setResources(path);
		FormScannerResources.setTemplate(configurations.getProperty(
				FormScannerConfigurationKeys.TEMPLATE, null));
		openTemplate(FormScannerResources.getTemplate(), false);
	}

	public void openImages() {
		File[] fileArray = fileUtils.chooseImages();
		if (fileArray != null) {
			Integer fileIndex = 0;
			for (File file : fileArray) {
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
				FormTemplate filledForm = new FormTemplate(imageFile,
						formTemplate);

				createFormImageFrame(imageFile, filledForm, Mode.VIEW);

				renameFileFrame = new RenameFileFrame(this,
						getFileNameByIndex(renamedFileIndex));
				view.arrangeFrame(renameFileFrame);
			}
			break;
		case RENAME_FILE_CURRENT:
			String newFileName = renameFileFrame.getNewFileName();
			String oldFileName = getFileNameByIndex(renamedFileIndex);

			if (!newFileName.equalsIgnoreCase(oldFileName)) {
				File newFile = renameFile(renamedFileIndex, newFileName,
						oldFileName);

				updateFileList(renamedFileIndex, newFile);
			}

			fileListFrame.updateFileList(getOpenedFileList());
		case RENAME_FILE_SKIP:
			renamedFileIndex++;

			if (openedFiles.size() > renamedFileIndex) {
				fileListFrame.selectFile(renamedFileIndex);
			}

			if (openedFiles.size() > renamedFileIndex) {
				imageFrame.updateImage(openedFiles.get(renamedFileIndex));
				renameFileFrame
						.updateRenamedFile(getFileNameByIndex(renamedFileIndex));
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
				for (Entry<Integer, File> openedFile : openedFiles.entrySet()) {
					// analyzedFileIndex = fileListFrame.getSelectedItemIndex();
					analyzedFileIndex = openedFile.getKey();
					fileListFrame.selectFile(analyzedFileIndex);
					File imageFile = openedFiles.get(analyzedFileIndex);

					FormTemplate filledForm = new FormTemplate(imageFile,
							formTemplate);
					filledForm.findCorners(threshold);
					filledForm.findPoints(threshold, density);
					filledForms.put(filledForm.getName(), filledForm);
					createFormImageFrame(imageFile, filledForm, Mode.VIEW);
				}

				// analyzeFileResultsFrame = new AnalyzeFileResultsFrame(this,
				// partialResults, totalResults);
				// view.arrangeFrame(analyzeFileResultsFrame);
				Date today = Calendar.getInstance().getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				File outputFile = new File(
						path
								+ "/results/"
								+ FormScannerTranslation
										.getTranslationFor(FormScannerTranslationKeys.RESULTS_DEFAULT_FILE)
								+ "_" + sdf.format(today) + ".csv");
				fileUtils.saveCsvAs(outputFile, filledForms);
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

			if (openedFiles.size() > analyzedFileIndex) {
				File imageFile = openedFiles.get(analyzedFileIndex);
				FormTemplate filledForm = new FormTemplate(imageFile,
						formTemplate);
				filledForm.findCorners(threshold);
				filledForm.findPoints(threshold, density);
				filledForms.put(filledForm.getName(), filledForm);

				// createFormImageFrame(imageFile, filledForm, Mode.VIEW);
				// analyzeFileResultsFrame = new AnalyzeFileResultsFrame(this,
				// partialResults, totalResults);
				// view.arrangeFrame(analyzeFileResultsFrame);
			} else {
				Date today = Calendar.getInstance().getTime();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				File outputFile = new File(
						path
								+ "/results/"
								+ FormScannerTranslation
										.getTranslationFor(FormScannerTranslationKeys.RESULTS_DEFAULT_FILE)
								+ "_" + sdf.format(today) + ".csv");

				fileUtils.saveCsvAs(outputFile, filledForms);
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
			formTemplate = new FormTemplate(templateImage);
			formTemplate.findCorners(threshold);
			manageTemplateFrame = new ManageTemplateFrame(this);

			view.arrangeFrame(manageTemplateFrame);
		}
	}

	public void createTemplateImageFrame() {
		imageFrame = new ImageFrame(this, templateImage, formTemplate,
				Mode.UPDATE);
		view.arrangeFrame(imageFrame);
	}

	public void createFormImageFrame(File image, FormTemplate template,
			Mode mode) {
		imageFrame = new ImageFrame(this, image, template, mode);
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
		if (manageTemplateFrame != null) {
			int rows = manageTemplateFrame.getRowsNumber();
			int values = manageTemplateFrame.getValuesNumber();

			if (rows == 1 && values == 1) {
				if (points.isEmpty()) {
					points.add(p2);
					view.repaint();
					manageTemplateFrame.setupTable(points);
					manageTemplateFrame.toFront();
				}
			} else {
				if (points.isEmpty() || (points.size() > 1)) {
					points.clear();
					points.add(p2);
					view.repaint();
				} else {
					FormPoint p1 = points.get(0);
					points.clear();

					FormPoint orig = formTemplate.getCorners().get(
							Corners.TOP_LEFT);
					double rotation = formTemplate.getRotation();

					p1.relativePositionTo(orig, rotation);
					p2.relativePositionTo(orig, rotation);

					HashMap<String, Double> delta = calcDelta(rows, values, p1,
							p2);

					for (int i = 0; i < rows; i++) {
						for (int j = 0; j < values; j++) {
							FormPoint pi = new FormPoint(
									(int) (p1.getX() + delta.get("x") * j),
									(int) (p1.getY() + delta.get("y") * i));
							pi.originalPositionFrom(orig, rotation);
							points.add(pi);
						}
					}
					view.repaint();
					manageTemplateFrame.setupTable(points);
					manageTemplateFrame.toFront();
				}
			}
		}
	}

	private HashMap<String, Double> calcDelta(int rows, int values,
			FormPoint p1, FormPoint p2) {
		double dX = Math.abs((p2.getX() - p1.getX()));
		double dY = Math.abs((p2.getY() - p1.getY()));

		double valuesDivider = ((values > 1) ? (values - 1) : 1);
		double questionDivider = ((rows > 1) ? (rows - 1) : 1);

		HashMap<String, Double> delta = new HashMap<String, Double>();

		switch (manageTemplateFrame.getFieldType()) {
		case QUESTIONS_BY_ROWS:
			delta.put("x", dX / valuesDivider);
			delta.put("y", dY / questionDivider);
			break;
		case QUESTIONS_BY_COLS:
			delta.put("x", dX / questionDivider);
			delta.put("y", dY / valuesDivider);
			break;
		default:
			break;
		}
		return delta;
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
		File template = formTemplate.saveToFile(path);
		JOptionPane
				.showMessageDialog(
						null,
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.TEMPLATE_SAVED),
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.TEMPLATE_SAVED_POPUP),
						JOptionPane.INFORMATION_MESSAGE);
		String templatePath = template.getAbsolutePath();
		configurations.setProperty(FormScannerConfigurationKeys.TEMPLATE,
				templatePath);
		configurations.store();
		view.dispose();
	}

	public void exitFormScanner() {
		view.dispose();
		System.exit(0);
	}

	public void openTemplate() {
		openTemplate(fileUtils.chooseTemplate(), true);
	}

	private void openTemplate(File template, boolean notify) {
		if (template != null) {
			formTemplate = new FormTemplate(template);
			formTemplate.presetFromTemplate();
			if (notify) {
				JOptionPane
						.showMessageDialog(
								null,
								FormScannerTranslation
										.getTranslationFor(FormScannerTranslationKeys.TEMPLATE_LOADED),
								FormScannerTranslation
										.getTranslationFor(FormScannerTranslationKeys.TEMPLATE_LOADED_POPUP),
								JOptionPane.INFORMATION_MESSAGE);
				configurations.setProperty(
						FormScannerConfigurationKeys.TEMPLATE,
						template.getAbsolutePath());
				configurations.store();
			}
		}
	}

	public void linkToHelp(URL url) {
		String osName = System.getProperty("os.name");
		try {

			if (osName.startsWith("Windows")) {
				Runtime.getRuntime()
						.exec((new StringBuilder())
								.append("rundll32   url.dll,FileProtocolHandler ")
								.append(url).toString());
			} else {
				String browsers[] = { "firefox", "opera", "konqueror",
						"epiphany", "mozilla", "netscape" };
				String browser = null;
				for (int i = 0; i < browsers.length && browser == null; i++) {
					if (Runtime.getRuntime()
							.exec(new String[] { "which", browsers[i] })
							.waitFor() == 0) {
						browser = browsers[i];
					}
				}

				if (browser == null) {
					throw new Exception("Could not find web browser");
				}

				Runtime.getRuntime()
						.exec(new String[] { browser,
								FormScannerConstants.WIKI_PAGE });
			}
		} catch (Exception exception) {
			System.out
					.println("An error occured while trying to open the web browser!\n");
		}
	}

	public void showAboutFrame() {
		JFrame aboutFrame = new AboutFrame(this);
		aboutFrame.setVisible(true);
	}

	public String getLanguage() {
		return lang;
	}

	public void setLanguage(String name) {
		configurations.setProperty(FormScannerConfigurationKeys.LANG, name);
		configurations.store();
		JOptionPane
				.showMessageDialog(
						null,
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.LANGUAGE_CHANGED),
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.SETTINGS_POPUP),
						JOptionPane.INFORMATION_MESSAGE);
	}

	public void resetPoints() {
		points.clear();
	}

	public void showCursorPosition(ImageFrame view, FormPoint point) {
		view.showCursorPosition(point);
	}

	public void addTemporaryPoint(ImageFrame view, FormPoint point) {
		view.setTemporaryPoint(point);
		view.repaint();
	}

	public void setSelectedCorner(ImageFrame view, Corners corner) {
		view.toggleCornerButton(corner);
	}

	public void setCorner(ImageView view, Corners corner, FormPoint point) {
		formTemplate.setCorner(corner, point);
		view.showCornerPosition();
		view.repaint();
	}

	public void resetCornerButtons(ImageFrame view) {
		view.resetCornerButtons();
	}

	public void showOptionsFrame() {
		JFrame optionsFrame = new OptionsFrame(this);
		optionsFrame.setVisible(true);
	}

	public int getThreshold() {
		return threshold;
	}

	public int getDensity() {
		return density;
	}

	public void saveOptions(OptionsFrame view) {
		threshold = view.getThresholdValue();
		density = view.getDensityValue();
		
		configurations.setProperty(FormScannerConfigurationKeys.THRESHOLD, String.valueOf(view.getThresholdValue()));
		configurations.setProperty(FormScannerConfigurationKeys.DENSITY, String.valueOf(view.getDensityValue()));
		configurations.store();
	}
}