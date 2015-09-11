package com.albertoborsetta.formscanner.gui.menubar;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.albertoborsetta.formscanner.commons.FormScannerConstants;
import com.albertoborsetta.formscanner.commons.FormScannerConstants.ApplicationControlAction;
import com.albertoborsetta.formscanner.commons.resources.FormScannerResources;
import com.albertoborsetta.formscanner.commons.resources.FormScannerResourcesKeys;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import com.albertoborsetta.formscanner.gui.FileListFrame;
import com.albertoborsetta.formscanner.gui.desktop.DesktopController;
import com.albertoborsetta.formscanner.model.FormScannerModel;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MenuBarController {

	private FormScannerModel model;
	private DesktopController desktopController;
	private static final Logger logger = LogManager.getLogger(MenuBarController.class.getName());

	@FXML
	private MenuBar menuBar;
	@FXML
	private Menu fileMenu;
	@FXML
	private Menu editMenu;
	@FXML
	private Menu templateMenu;
	@FXML
	private Menu settingsMenu;
	@FXML
	private Menu helpMenu;

	// File
	@FXML
	private MenuItem openImagesMenuItem;
	@FXML
	private MenuItem exitMenuItem;

	// Edit
	@FXML
	private MenuItem renameFilesMenuItem;
	@FXML
	private MenuItem analyzeFilesAllMenuItem;
	@FXML
	private MenuItem analyzeFilesMenuItem;

	// Template
	@FXML
	private MenuItem createTemplateMenuItem;
	@FXML
	private MenuItem loadTemplateMenuItem;

	// Settings
	@FXML
	private MenuItem englishLanguageMenuItem;
	@FXML
	private MenuItem italianLanguageMenuItem;
	@FXML
	private MenuItem portuguesLanguageMenuItem;
	@FXML
	private MenuItem spanishLanguageMenuItem;
	@FXML
	private MenuItem germanLanguageMenuItem;
	@FXML
	private MenuItem persianLanhueageMenuItem;
	@FXML
	private MenuItem greekLanguageMenuItem;
	@FXML
	private MenuItem polishLanguageMenuItem;
	@FXML
	private MenuItem dutchLanguageMenuItem;
	@FXML
	private MenuItem japaneseLanguageMenuItem;
	@FXML
	private MenuItem optionsMenuItem;

	// Help
	@FXML
	private MenuItem helpMenuItem;
	@FXML
	private MenuItem aboutMenuItem;

	/*
	 * Private (MenuItemController)
	 */
	abstract class MenuItemController {

		public abstract boolean canPerform();

		public abstract void perform();

		public String getTitle() {
			return null;
		}

		public boolean isSelected() {
			return false;
		}
	}

	public MenuBarController(DesktopController desktopController) {
		this.desktopController = desktopController;
	}

	public MenuBar getMenuBar() {
		if (menuBar == null) {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			final URL fxmlURL = MenuBarController.class.getResource("MenuBar.fxml");
			try {
				loader.setResources(FormScannerTranslation.getResourceBundle());
				loader.setLocation(fxmlURL);
				loader.setController(this);
				loader.load();
				controllerDidLoadFxml();
			} catch (RuntimeException | IOException e) {
				System.out.println(e.getMessage());
				logger.debug("loader.getController()=" + loader.getController());
				logger.debug("loader.getLocation()=" + loader.getLocation());
				logger.debug("Failed to load " + fxmlURL.getFile());
				logger.debug(e.getMessage());
			}
		}
		return menuBar;
	}

	private void controllerDidLoadFxml() {

		assert menuBar != null;
		assert fileMenu != null;
		assert editMenu != null;
		assert templateMenu != null;
		assert settingsMenu != null;
		assert helpMenu != null;

		// File
		assert openImagesMenuItem != null;
		assert exitMenuItem != null;

		// Edit
		assert renameFilesMenuItem != null;
		assert analyzeFilesAllMenuItem != null;
		assert analyzeFilesMenuItem != null;

		// Template
		assert createTemplateMenuItem != null;
		assert loadTemplateMenuItem != null;

		// Settings
		assert englishLanguageMenuItem != null;
		assert italianLanguageMenuItem != null;
		assert portuguesLanguageMenuItem != null;
		assert spanishLanguageMenuItem != null;
		assert germanLanguageMenuItem != null;
		assert persianLanhueageMenuItem != null;
		assert greekLanguageMenuItem != null;
		assert polishLanguageMenuItem != null;
		assert dutchLanguageMenuItem != null;
		assert japaneseLanguageMenuItem != null;
		assert optionsMenuItem != null;

		// Help
		assert helpMenuItem != null;
		assert aboutMenuItem != null;

		openImagesMenuItem.setUserData(new ApplicationControlActionController(ApplicationControlAction.OPEN_IMAGES));
//		openImagesMenuItem.setOnAction(new EventHandler<ActionEvent>() {
//
//			@Override
//			public void handle(ActionEvent event) {
//				handleOpenImages();
//			}
//		});

		exitMenuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				handleExit();
			}
		});
		renameFilesMenuItem.setDisable(true);
		renameFilesMenuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				handleRenameFiles();
			}
		});

		renameFilesMenuItem.setDisable(true);
		analyzeFilesAllMenuItem.setDisable(true);
		analyzeFilesMenuItem.setDisable(true);
	}

	private void handleOpenImages() {
		// TODO
		model = FormScannerModel.getInstance();
		model.openImages(desktopController);
	}

	private void handleExit() {
		// TODO
		System.out.println("exit");
	}

	private void handleRenameFiles() {
		// TODO
		System.out.println("rename");
	}

	private class ApplicationControlActionController extends MenuItemController {

		private final ApplicationControlAction controlAction;

		public ApplicationControlActionController(ApplicationControlAction controlAction) {
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

	public void enableScanFunctions() {
		renameFilesMenuItem.setDisable(false);
		analyzeFilesAllMenuItem.setDisable(false);
		analyzeFilesMenuItem.setDisable(false);
	}
}
