package com.albertoborsetta.formscanner.gui.desktop;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.albertoborsetta.formscanner.commons.FormScannerConstants;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import com.albertoborsetta.formscanner.gui.menubar.MenuBarController;
import com.albertoborsetta.formscanner.model.FormScannerModel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DesktopController {

	private FormScannerModel model;
	private MenuBarController menuBarController;
	private Parent root;
	private Stage stage;
	private static final Logger logger = LogManager.getLogger(DesktopController.class.getName());

	@FXML
	private VBox desktop;
	@FXML
	private SplitPane verticalSplitPane;

	public DesktopController(Stage stage) throws UnsupportedEncodingException {
		model = new FormScannerModel(stage);
		menuBarController = new MenuBarController(this);
	}

	public VBox getDesktop() {
		assert getRoot() instanceof VBox;
		desktop = (VBox) getRoot();

		desktop.getChildren().add(0, menuBarController.getMenuBar());

		return desktop;
	}

	public Parent getRoot() {
		if (root == null) {
			makeRoot();
			assert root != null;
		}

		return root;
	}

	private void makeRoot() {
		// Load root layout from fxml file.
		FXMLLoader loader = new FXMLLoader();
		final URL fxmlURL = DesktopController.class.getResource("Desktop.fxml");
		try {
			loader.setResources(FormScannerTranslation.getResourceBundle());
			loader.setLocation(fxmlURL);
			loader.setController(this);
			setRoot((Region) loader.load());
		} catch (RuntimeException | IOException e) {
			logger.debug("loader.getController()=" + loader.getController());
			logger.debug("loader.getLocation()=" + loader.getLocation());
			logger.debug("Failed to load " + fxmlURL.getFile());
			logger.debug(e.getMessage());
		}
	}

	private void setRoot(Region root) {
		this.root = root;
	}
	
	public Stage getStage() {
		return stage;
	}

	public void setPrimaryStage(Stage stage) {
		this.stage = stage;
	}

	public String getTitle() {
		return StringUtils.replace(
				FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.FORMSCANNER_MAIN_TITLE),
				FormScannerConstants.VERSION_KEY, FormScannerConstants.VERSION);
	}

	public NodeOrientation getOrientation() {
		return model.getOrientation();
	}

	public void setupFileList(String[] openedFileList) {
		menuBarController.enableScanFunctions();
		
	}
}
