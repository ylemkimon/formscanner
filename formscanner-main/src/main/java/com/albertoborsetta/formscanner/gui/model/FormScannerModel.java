package com.albertoborsetta.formscanner.gui.model;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map.Entry;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import com.albertoborsetta.formscanner.api.FormField;
import com.albertoborsetta.formscanner.api.FormPoint;
import com.albertoborsetta.formscanner.api.FormTemplate;
import com.albertoborsetta.formscanner.api.commons.Constants.Corners;
import com.albertoborsetta.formscanner.commons.FormFileUtils;
import com.albertoborsetta.formscanner.commons.FormScannerConstants;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.Action;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.Frame;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.Mode;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.ShapeType;
import com.albertoborsetta.formscanner.commons.configuration.FormScannerConfiguration;
import com.albertoborsetta.formscanner.commons.configuration.FormScannerConfigurationKeys;
import com.albertoborsetta.formscanner.commons.resources.FormScannerResources;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import com.albertoborsetta.formscanner.gui.FormScanner;
import com.albertoborsetta.formscanner.gui.view.AboutFrame;
import com.albertoborsetta.formscanner.gui.view.FileListFrame;
import com.albertoborsetta.formscanner.gui.view.ImageFrame;
import com.albertoborsetta.formscanner.gui.view.InternalFrame;
import com.albertoborsetta.formscanner.gui.view.ManageTemplateFrame;
import com.albertoborsetta.formscanner.gui.view.OptionsFrame;
import com.albertoborsetta.formscanner.gui.view.RenameFileFrame;
import com.albertoborsetta.formscanner.gui.view.ResultsGridFrame;

public class FormScannerModel {

	public static final String COL_DX = "COL_DX";
	public static final String COL_DY = "COL_DY";
	public static final String ROW_DX = "ROW_DX";
	public static final String ROW_DY = "ROW_DY";

	private String templatePath;
	private String resultsPath;
	private String propertiesPath;
	private HashMap<Integer, File> openedFiles = new HashMap<Integer, File>();
	private FileListFrame fileListFrame;
	private RenameFileFrame renameFileFrame;
	private FormScanner view;
	private int renamedFileIndex = 0;
	private int analyzedFileIndex = 0;
	private boolean firstPass = true;

	private FormScannerConfiguration configurations;
	private ManageTemplateFrame manageTemplateFrame;
	private ImageFrame imageFrame;
	private FormTemplate formTemplate;
	private BufferedImage templateImage;
	private FormFileUtils fileUtils;
	private HashMap<String, FormTemplate> filledForms = new HashMap<String, FormTemplate>();

	private ArrayList<FormPoint> points = new ArrayList<FormPoint>();
	private String lang;
	private int threshold;
	private int density;
	private ResultsGridFrame resultsGridFrame;
	private FormTemplate filledForm;
	private int shapeSize;
	private ShapeType shapeType;
	private Rectangle fileListFramePosition;
	private Rectangle renameFilesFramePosition;
	private Rectangle manageTemplateFramePosition;
	private Rectangle imageFramePosition;
	private Rectangle resultsGridFramePosition;
	private Rectangle defaultPosition;
	private Rectangle aboutFramePosition;
	private Rectangle optionsFramePosition;
	private Rectangle desktopSize;
	private Locale locale;

	public FormScannerModel(FormScanner view) {
		this.view = view;

		String installPath = StringUtils.defaultIfBlank(
				System.getProperty("FormScanner_HOME"),
				System.getenv("FormScanner_HOME"));
		String userHome = System.getProperty("user.home");
		String osName = System.getProperty("os.name");

		if (StringUtils.contains(osName, "Windows")) {
			resultsPath = userHome + "/Documents";
			templatePath = userHome + "/Documents";
			propertiesPath = userHome + "/AppData/Local/FormScanner";
		} else {
			propertiesPath = userHome + "/.FormScanner";
		}

		propertiesPath = propertiesPath + "/properties/";

		configurations = FormScannerConfiguration.getConfiguration(
				propertiesPath, installPath + "/");

		templatePath = configurations.getProperty(
				FormScannerConfigurationKeys.TEMPLATE_SAVE_PATH, templatePath
						+ "/FormScanner/templates/");
		resultsPath = configurations.getProperty(
				FormScannerConfigurationKeys.RESULTS_SAVE_PATH, resultsPath
						+ "/FormScanner/results/");

		lang = configurations.getProperty(FormScannerConfigurationKeys.LANG,
				FormScannerConfigurationKeys.DEFAULT_LANG);
		String[] locales = StringUtils.split(lang, '_');
		if (locales.length == 2) {
			locale = new Locale(locales[0], locales[1]);
		} else {
			locale = new Locale(locales[0]);
		}
		fileUtils = FormFileUtils.getInstance(locale);

		FormScannerTranslation.setTranslation(installPath, lang);
		FormScannerResources.setResources(installPath);

		threshold = Integer.valueOf(configurations.getProperty(
				FormScannerConfigurationKeys.THRESHOLD,
				FormScannerConfigurationKeys.DEFAULT_THRESHOLD));
		density = Integer.valueOf(configurations.getProperty(
				FormScannerConfigurationKeys.DENSITY,
				FormScannerConfigurationKeys.DEFAULT_DENSITY));
		shapeSize = Integer.valueOf(configurations.getProperty(
				FormScannerConfigurationKeys.SHAPE_SIZE,
				FormScannerConfigurationKeys.DEFAULT_SHAPE_SIZE));
		shapeType = ShapeType.valueOf(configurations.getProperty(
				FormScannerConfigurationKeys.SHAPE_TYPE,
				FormScannerConfigurationKeys.DEFAULT_SHAPE_TYPE));

		String tmpl = configurations.getProperty(
				FormScannerConfigurationKeys.TEMPLATE, null);
		if (!StringUtils.isEmpty(tmpl)) {
			FormScannerResources.setTemplate(templatePath + tmpl);
			openTemplate(FormScannerResources.getTemplate(), false);
		}
	}

	public void setDefaultFramePositions() {
		for (Frame frame : Frame.values()) {
			String pos = configurations.getProperty(
					frame.getConfigurationKey(), null);
			Rectangle position;
			if (pos == null) {
				position = frame.getDefaultPosition();
			} else {
				String[] positions = StringUtils.split(pos, ',');

				position = new Rectangle(Integer.parseInt(positions[0]),
						Integer.parseInt(positions[1]),
						Integer.parseInt(positions[2]),
						Integer.parseInt(positions[3]));
			}

			setLastPosition(frame, position);
		}
	}

	public void openImages() {
		filledForms.clear();
		openedFiles.clear();
		firstPass = true;
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
				view.setScanAllControllersEnabled(true);
				view.setScanCurrentControllersEnabled(false);
			}
		}
	}

	public void renameFiles(String action) {
		Action act = Action.valueOf(action);
		switch (act) {
		case RENAME_FILES_FIRST:
			if (!openedFiles.isEmpty()) {
				view.setRenameControllersEnabled(false);
				view.setScanControllersEnabled(false);
				view.setScanAllControllersEnabled(false);
				view.setScanCurrentControllersEnabled(false);

				renamedFileIndex = fileListFrame.getSelectedItemIndex();
				fileListFrame.selectFile(renamedFileIndex);
				File imageFile = openedFiles.get(renamedFileIndex);

				try {
					BufferedImage image = ImageIO.read(imageFile);
					String name = FilenameUtils.removeExtension(imageFile
							.getName());
					filledForm = new FormTemplate(name);
					createFormImageFrame(image, filledForm, Mode.VIEW);
					renameFileFrame = new RenameFileFrame(this,
							getFileNameByIndex(renamedFileIndex));
					view.arrangeFrame(renameFileFrame);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			break;
		case RENAME_FILES_CURRENT:
			String newFileName = renameFileFrame.getNewFileName();
			String oldFileName = getFileNameByIndex(renamedFileIndex);

			if (!newFileName.equalsIgnoreCase(oldFileName)) {
				File newFile = renameFile(renamedFileIndex, newFileName,
						oldFileName);

				updateFileList(renamedFileIndex, newFile);
			}

			fileListFrame.updateFileList(getOpenedFileList());
		case RENAME_FILES_SKIP:
			renamedFileIndex++;

			if (openedFiles.size() > renamedFileIndex) {
				fileListFrame.selectFile(renamedFileIndex);
				File imageFile = openedFiles.get(renamedFileIndex);
				try {
					BufferedImage image = ImageIO.read(imageFile);
					imageFrame.updateImage(image);
					renameFileFrame = new RenameFileFrame(this,
							getFileNameByIndex(renamedFileIndex));
//					renameFileFrame
//							.updateRenamedFile(getFileNameByIndex(renamedFileIndex));
					view.arrangeFrame(renameFileFrame);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				view.disposeFrame(renameFileFrame);
				view.disposeFrame(imageFrame);

				view.setRenameControllersEnabled(true);
				view.setScanControllersEnabled(true);
				view.setScanAllControllersEnabled(true);
				view.setScanCurrentControllersEnabled(false);
			}
			break;
		default:
			break;
		}
	}

	public void analyzeFiles(String action) {
		if (formTemplate != null) {

			Action act = Action.valueOf(action);
			switch (act) {
			case ANALYZE_FILES_ALL:
				if (!openedFiles.isEmpty()) {
					view.setRenameControllersEnabled(false);
					view.setScanControllersEnabled(false);
					view.setScanAllControllersEnabled(false);
					view.setScanCurrentControllersEnabled(false);

					for (Entry<Integer, File> openedFile : openedFiles
							.entrySet()) {
						analyzedFileIndex = openedFile.getKey();
						fileListFrame.selectFile(analyzedFileIndex);
						File imageFile = openedFiles.get(analyzedFileIndex);
						try {
							BufferedImage image = ImageIO.read(imageFile);
							String name = FilenameUtils
									.removeExtension(imageFile.getName());
							filledForm = new FormTemplate(name, formTemplate);
							filledForm.findCorners(image, threshold, density);
							filledForm.findPoints(image, threshold, density,
									shapeSize);
							filledForms.put(filledForm.getName(), filledForm);
						} catch (Exception e) {
							e.printStackTrace();
						}

					}

					Date today = Calendar.getInstance().getTime();
					SimpleDateFormat sdf = new SimpleDateFormat(
							"yyyyMMddHHmmss");
					File outputFile = new File(
							resultsPath
									+ FormScannerTranslation
											.getTranslationFor(FormScannerTranslationKeys.RESULTS_DEFAULT_FILE)
									+ "_" + sdf.format(today) + ".csv");
					File savedFile = fileUtils.saveCsvAs(outputFile,
							filledForms);
					configurations.setProperty(
							FormScannerConfigurationKeys.RESULTS_SAVE_PATH, FilenameUtils.getFullPath(savedFile.getAbsolutePath()));
					configurations.store();

					view.setRenameControllersEnabled(true);
					view.setScanControllersEnabled(true);
					view.setScanAllControllersEnabled(true);
					view.setScanCurrentControllersEnabled(false);
					resetFirstPass();
				}
				break;
			case ANALYZE_FILES_FIRST:
				if (!openedFiles.isEmpty()) {
					view.setRenameControllersEnabled(false);
					view.setScanAllControllersEnabled(false);
					view.setScanControllersEnabled(true);
					view.setScanCurrentControllersEnabled(true);

					if (firstPass) {
						analyzedFileIndex = fileListFrame
								.getSelectedItemIndex();
						firstPass = false;
					} else {
						analyzedFileIndex++;
					}

					if (openedFiles.size() > analyzedFileIndex) {
						fileListFrame.selectFile(analyzedFileIndex);
						File imageFile = openedFiles.get(analyzedFileIndex);
						try {
							BufferedImage image = ImageIO.read(imageFile);
							String name = FilenameUtils
									.removeExtension(imageFile.getName());
							filledForm = new FormTemplate(name, formTemplate);
							filledForm.findCorners(image, threshold, density);
							filledForm.findPoints(image, threshold, density,
									shapeSize);
							points = filledForm.getFieldPoints();
							filledForms.put(filledForm.getName(), filledForm);
							createFormImageFrame(image, filledForm,
									Mode.MODIFY_POINTS);
							createResultsGridFrame();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						Date today = Calendar.getInstance().getTime();
						SimpleDateFormat sdf = new SimpleDateFormat(
								"yyyyMMddHHmmss");
						File outputFile = new File(
								resultsPath
										+ FormScannerTranslation
												.getTranslationFor(FormScannerTranslationKeys.RESULTS_DEFAULT_FILE)
										+ "_" + sdf.format(today) + ".csv");
						File savedFile = fileUtils.saveCsvAs(outputFile, filledForms);
						configurations.setProperty(
								FormScannerConfigurationKeys.RESULTS_SAVE_PATH,
								FilenameUtils.getFullPath(savedFile.getAbsolutePath()));
						configurations.store();

						view.disposeFrame(imageFrame);
						view.disposeFrame(resultsGridFrame);

						view.setRenameControllersEnabled(true);
						view.setScanControllersEnabled(true);
						view.setScanAllControllersEnabled(true);
						view.setScanCurrentControllersEnabled(false);
						resetFirstPass();
					}
				}
				break;
			case ANALYZE_FILES_CURRENT:
				fileListFrame.selectFile(analyzedFileIndex);
				File imageFile = openedFiles.get(analyzedFileIndex);

				try {
					BufferedImage image = ImageIO.read(imageFile);
					filledForm = imageFrame.getTemplate();
					filledForm.clearPoints();
					filledForm.findPoints(image, threshold, density, shapeSize);
					points = filledForm.getFieldPoints();
					filledForms.put(filledForm.getName(), filledForm);
					createFormImageFrame(image, filledForm, Mode.MODIFY_POINTS);
					createResultsGridFrame();
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			default:
				break;
			}
		} else {
			JOptionPane
					.showMessageDialog(
							null,
							FormScannerTranslation
									.getTranslationFor(FormScannerTranslationKeys.TEMPLATE_NOT_FOUND),
							FormScannerTranslation
									.getTranslationFor(FormScannerTranslationKeys.TEMPLATE_NOT_FOUND_POPUP),
							JOptionPane.WARNING_MESSAGE);
		}
	}

	public void createResultsGridFrame() {
		if (resultsGridFrame == null) {
			resultsGridFrame = new ResultsGridFrame(this);
		} else {
			resultsGridFrame.updateResults();
		}
		view.arrangeFrame(resultsGridFrame);
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

	private String[] getOpenedFileList() {
		String[] fileList = new String[openedFiles.size()];

		for (int i = 0; i < openedFiles.size(); i++) {
			fileList[i] = getFileNameByIndex(i);
		}

		return fileList;
	}

	private String getFileNameByIndex(int index) {
		return openedFiles.get(index).getName();
	}

	public void disposeRelatedFrame(InternalFrame frame) {
		Frame frm = Frame.valueOf(frame.getName());
		setLastPosition(frm, frame.getBounds());
		switch (frm) {
		case RENAME_FILES_FRAME:
			view.disposeFrame(imageFrame);
			break;
		case IMAGE_FRAME:
			view.disposeFrame(renameFileFrame);
			view.disposeFrame(manageTemplateFrame);
			view.disposeFrame(resultsGridFrame);
			resetPoints();
			break;
		case MANAGE_TEMPLATE_FRAME:
			view.disposeFrame(imageFrame);
			break;
		default:
			break;
		}
	}

	public void loadTemplate() {
		File templateFile = fileUtils.chooseImage();
		if (templateFile != null) {
			try {
				templateImage = ImageIO.read(templateFile);
				String name = FilenameUtils.removeExtension(templateFile
						.getName());
				formTemplate = new FormTemplate(name);
				formTemplate.findCorners(templateImage, threshold, density);
				manageTemplateFrame = new ManageTemplateFrame(this);

				view.arrangeFrame(manageTemplateFrame);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void createTemplateImageFrame() {
		imageFrame = new ImageFrame(this, templateImage, formTemplate,
				Mode.SETUP_POINTS);
		view.arrangeFrame(imageFrame);
	}

	public void createFormImageFrame(BufferedImage image,
			FormTemplate template, Mode mode) {
		if (imageFrame == null) {
			imageFrame = new ImageFrame(this, image, template, mode);
		} else {
			imageFrame.updateImage(image, template);
		}
		view.arrangeFrame(imageFrame);
	}

	public void addPoint(ImageFrame view, FormPoint p) {
		switch (view.getMode()) {
		case SETUP_POINTS:
			if (manageTemplateFrame != null) {
				int rows = manageTemplateFrame.getRowsNumber();
				int values = manageTemplateFrame.getValuesNumber();

				if (rows == 1 && values == 1) {
					if (points.isEmpty()) {
						points.add(p);
						manageTemplateFrame.setupPositionTable(points);
						manageTemplateFrame.toFront();
					}
				} else {
					if (points.isEmpty() || (points.size() > 1)) {
						resetPoints();
						points.add(p);
					} else {
						FormPoint p1 = points.get(0);
						resetPoints();

						FormPoint orig = formTemplate.getCorners().get(
								Corners.TOP_LEFT);
						double rotation = formTemplate.getRotation();

						p1.rotoTranslate(orig, rotation, true);
						p.rotoTranslate(orig, rotation, true);

						HashMap<String, Double> delta = calcDelta(rows, values,
								p1, p);

						double rowsMultiplier;
						double colsMultiplier;
						for (int i = 0; i < rows; i++) {
							for (int j = 0; j < values; j++) {
								switch (manageTemplateFrame.getFieldType()) {
								case QUESTIONS_BY_COLS:
									rowsMultiplier = j;
									colsMultiplier = i;
									break;
								case QUESTIONS_BY_ROWS:
								default:
									rowsMultiplier = i;
									colsMultiplier = j;
									break;
								}
								FormPoint pi = new FormPoint(
										(p1.getX() + (delta.get("x") * colsMultiplier)),
										(p1.getY() + (delta.get("y") * rowsMultiplier)));
								pi.rotoTranslate(orig, rotation, false);
								points.add(pi);
							}
						}
						view.setMode(Mode.VIEW);
						manageTemplateFrame.setupPositionTable(points);
						manageTemplateFrame.toFront();
					}
				}
			}
			break;
		case MODIFY_POINTS:
			filledForm.addPoint(p);
			createResultsGridFrame();
			break;
		default:
			break;
		}
		view.repaint();
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
		resetPoints();
	}

	public ArrayList<FormPoint> getPoints() {
		return points;
	}

	public void removeField(String fieldName) {
		formTemplate.removeFieldByName(fieldName);
	}

	public void saveTemplate() {
		File template = fileUtils.saveToFile(templatePath, formTemplate);

		if (template != null) {
			JOptionPane
					.showMessageDialog(
							null,
							FormScannerTranslation
									.getTranslationFor(FormScannerTranslationKeys.TEMPLATE_SAVED),
							FormScannerTranslation
									.getTranslationFor(FormScannerTranslationKeys.TEMPLATE_SAVED_POPUP),
							JOptionPane.INFORMATION_MESSAGE);
			templatePath = FilenameUtils.getFullPath(template.getAbsolutePath());
			String templateFile = FilenameUtils.getName(template.getAbsolutePath());
			configurations.setProperty(FormScannerConfigurationKeys.TEMPLATE,
					templateFile);
			configurations.setProperty(FormScannerConfigurationKeys.TEMPLATE_SAVE_PATH, templatePath);
			configurations.store();
		} else {
			JOptionPane
					.showMessageDialog(
							null,
							FormScannerTranslation
									.getTranslationFor(FormScannerTranslationKeys.TEMPLATE_NOT_SAVED),
							FormScannerTranslation
									.getTranslationFor(FormScannerTranslationKeys.TEMPLATE_NOT_SAVED_POPUP),
							JOptionPane.ERROR_MESSAGE);
		}
	}

	public void exitFormScanner() {
		view.dispose();
		System.exit(0);
	}

	public boolean openTemplate() {
		return openTemplate(fileUtils.chooseTemplate(), true);
	}

	private boolean openTemplate(File template, boolean notify) {
		if (template == null) {
			return false;
		}

		try {
			formTemplate = new FormTemplate(template);
			if (notify) {
				JOptionPane
						.showMessageDialog(
								null,
								FormScannerTranslation
										.getTranslationFor(FormScannerTranslationKeys.TEMPLATE_LOADED),
								FormScannerTranslation
										.getTranslationFor(FormScannerTranslationKeys.TEMPLATE_LOADED_POPUP),
								JOptionPane.INFORMATION_MESSAGE);
			}
			templatePath = FilenameUtils.getFullPath(template.getAbsolutePath());
			String templateFile = FilenameUtils.getName(template.getAbsolutePath());
			configurations.setProperty(FormScannerConfigurationKeys.TEMPLATE,
					templateFile);
			configurations.setProperty(FormScannerConfigurationKeys.TEMPLATE_SAVE_PATH, templatePath);
			configurations.store();
			return true;
		} catch (Exception e) {
			JOptionPane
					.showMessageDialog(
							null,
							FormScannerTranslation
									.getTranslationFor(FormScannerTranslationKeys.TEMPLATE_NOT_LOADED),
							FormScannerTranslation
									.getTranslationFor(FormScannerTranslationKeys.TEMPLATE_NOT_LOADED_POPUP),
							JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return false;
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
		InternalFrame aboutFrame = new AboutFrame(this);
		view.arrangeFrame(aboutFrame);
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

	public void showOptionsFrame() {
		InternalFrame optionsFrame = new OptionsFrame(this);
		view.arrangeFrame(optionsFrame);
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
		shapeSize = view.getShapeSize();
		shapeType = view.getShape();

		configurations.setProperty(FormScannerConfigurationKeys.THRESHOLD,
				String.valueOf(threshold));
		configurations.setProperty(FormScannerConfigurationKeys.DENSITY,
				String.valueOf(density));
		configurations.setProperty(FormScannerConfigurationKeys.SHAPE_SIZE,
				String.valueOf(shapeSize));
		configurations.setProperty(FormScannerConfigurationKeys.SHAPE_TYPE,
				shapeType.getName());
		configurations.store();
	}

	public void resetFirstPass() {
		firstPass = true;
	}

	public void deleteNearestPointTo(FormPoint cursorPoint) {
		if (filledForm != null) {
			filledForm.removePoint(cursorPoint);
			createResultsGridFrame();
		} else {
			formTemplate.removePoint(cursorPoint);
		}
		view.repaint();
	}

	public void clearTemporaryPoint(ImageFrame view) {
		view.clearTemporaryPoint();
	}

	public int getShapeSize() {
		return shapeSize;
	}

	public ShapeType getShapeType() {
		return shapeType;
	}

	public FormTemplate getFilledForm() {
		return filledForm;
	}

	public FormTemplate getTemplate() {
		return formTemplate;
	}

	public Rectangle getLastPosition(Frame frame) {
		Frame frm = Frame.valueOf(frame.name());
		switch (frm) {
		case FILE_LIST_FRAME:
			return fileListFramePosition;
		case RENAME_FILES_FRAME:
			return renameFilesFramePosition;
		case MANAGE_TEMPLATE_FRAME:
			return manageTemplateFramePosition;
		case IMAGE_FRAME:
			return imageFramePosition;
		case RESULTS_GRID_FRAME:
			return resultsGridFramePosition;
		case ABOUT_FRAME:
			return aboutFramePosition;
		case OPTIONS_FRAME:
			return optionsFramePosition;
		case DESKTOP_FRAME:
			return desktopSize;
		default:
			return defaultPosition;
		}
	}

	public void setLastPosition(Frame frame, Rectangle position) {
		Frame frm = Frame.valueOf(frame.name());

		String[] positions = { String.valueOf(position.x),
				String.valueOf(position.y), String.valueOf(position.width),
				String.valueOf(position.height) };

		configurations.setProperty(frm.getConfigurationKey(),
				StringUtils.join(positions, ','));
		configurations.store();

		switch (frm) {
		case FILE_LIST_FRAME:
			fileListFramePosition = position;
			break;
		case RENAME_FILES_FRAME:
			renameFilesFramePosition = position;
			break;
		case MANAGE_TEMPLATE_FRAME:
			manageTemplateFramePosition = position;
			break;
		case IMAGE_FRAME:
			imageFramePosition = position;
			break;
		case RESULTS_GRID_FRAME:
			resultsGridFramePosition = position;
			break;
		case ABOUT_FRAME:
			aboutFramePosition = position;
			break;
		case OPTIONS_FRAME:
			optionsFramePosition = position;
			break;
		case DESKTOP_FRAME:
			desktopSize = position;
			break;
		default:
			defaultPosition = position;
			break;
		}
	}

	public Locale getLocale() {
		return locale;
	}
}