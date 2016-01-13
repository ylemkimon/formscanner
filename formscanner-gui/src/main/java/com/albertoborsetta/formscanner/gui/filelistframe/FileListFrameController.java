package com.albertoborsetta.formscanner.gui.filelistframe;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map.Entry;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.albertoborsetta.formscanner.commons.FormScannerConstants.Action;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import com.albertoborsetta.formscanner.gui.desktop.DesktopController;
import com.albertoborsetta.formscanner.gui.menubar.MenuBarController;
import com.albertoborsetta.formscanner.model.FormScannerModel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.TextFieldTableCell;

public class FileListFrameController {

	@FXML
	private TableView<FormImage> fileListFrame;
	@FXML
	private TableColumn<FormImage, String> fileNameColumn;
	@FXML
	private TableColumn<FormImage, Boolean> fileDoneColumn;

	private DesktopController desktopController;
	private static final Logger logger = LogManager.getLogger(MenuBarController.class.getName());

	private class FormImage {

		private int index;
		private StringProperty fileName;
		private BooleanProperty done;

		public FormImage(String fileName, int index) {
			this.index = index;
			this.fileName = new SimpleStringProperty(fileName);
			this.done = new SimpleBooleanProperty(false);
		}

		public String getFileName() {
			return fileName.get();
		}

		public Boolean isDone() {
			return done.get();
		}

		public void setDone(Boolean done) {
			this.done = new SimpleBooleanProperty(done);
		}

		public StringProperty fileNameProperty() {
			return fileName;
		}

		public BooleanProperty fileDoneProperty() {
			return done;
		}

		public int getIndex() {
			return index;
		}
	}

	public FileListFrameController(DesktopController desktopController) {
		this.desktopController = desktopController;
	}

	public TableView<FormImage> getFileListFrame() {
		if (fileListFrame == null) {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			final URL fxmlURL = FileListFrameController.class.getResource("FileListFrame.fxml");
			try {
				loader.setResources(FormScannerTranslation.getResourceBundle());
				loader.setLocation(fxmlURL);
				loader.setController(this);
				loader.load();
				controllerDidLoadFxml();
			} catch (RuntimeException | IOException e) {
				System.out.println("loader.getController()=" + loader.getController()); // NOI18N
				System.out.println("loader.getLocation()=" + loader.getLocation()); // NOI18N
				System.out.println("Failed to load " + fxmlURL.getFile()); // NOI18N
				e.printStackTrace();
//				logger.debug("loader.getController()=" + loader.getController());
//				logger.debug("loader.getLocation()=" + loader.getLocation());
//				logger.debug("Failed to load " + fxmlURL.getFile());
//				logger.debug(e.getMessage());
			}
		}
		return fileListFrame;
	}

	private void controllerDidLoadFxml() {
		fileListFrame.setEditable(true);
		fileNameColumn.setEditable(true);
		fileNameColumn.setCellValueFactory(cellData -> cellData.getValue().fileNameProperty());
		fileNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		fileNameColumn.setUserData(new FileListFrameActionController(Action.RENAME_FILES_CURRENT));

		fileDoneColumn.setEditable(false);
		fileDoneColumn.setCellValueFactory(cellData -> cellData.getValue().fileDoneProperty());
		fileDoneColumn.setCellFactory(CheckBoxTableCell.forTableColumn(fileDoneColumn));

		setupTableColumsHandlers(fileNameColumn);
	}

	private void setupTableColumsHandlers(TableColumn<FormImage, String> column) {
		column.setOnEditCommit(onEditCommitEventHandler);
	}

	@SuppressWarnings("unchecked")
	private final EventHandler<CellEditEvent<FormImage, String>> onEditCommitEventHandler = type -> {
		assert type.getSource() instanceof TableColumn;
		handleOnEditCommit((TableColumn<FormImage, String>) type.getSource());
	};

	private void handleOnEditCommit(TableColumn<FormImage, String> column) {
		assert column.getUserData() instanceof ColumnController;
		final ColumnController controller = (ColumnController) column.getUserData();
		controller.perform();
	}

	private abstract class ColumnController {

		public abstract boolean canPerform();

		public abstract void perform();
	}

	class FileListFrameActionController extends ColumnController {

		private final Action controlAction;

		public FileListFrameActionController(Action controlAction) {
			this.controlAction = controlAction;
		}

		@Override
		public boolean canPerform() {
			return FormScannerModel.getInstance().canPerformControlAction(controlAction, desktopController);
		}

		@Override
		public void perform() {
			FormScannerModel.getInstance().performControlAction(controlAction, desktopController);
		}

	}

	public void addFiles(HashMap<Integer, File> openedFiles) {
		ObservableList<FormImage> fileList = FXCollections.observableArrayList();
		for (Entry<Integer, File> openedFile : openedFiles.entrySet()) {
			fileList.add(
					new FormImage(FilenameUtils.getBaseName(openedFile.getValue().getName()), openedFile.getKey()));
		}
		fileListFrame.setItems(fileList);
	}

	public int getSelectedFileIndex() {
		return fileListFrame.getSelectionModel().getSelectedItem().getIndex();
	}

	public String getNewFileName() {
		return fileListFrame.getSelectionModel().getSelectedItem().getFileName();
	}
}
