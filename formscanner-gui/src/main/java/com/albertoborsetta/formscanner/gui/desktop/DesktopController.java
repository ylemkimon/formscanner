package com.albertoborsetta.formscanner.gui.desktop;

import java.io.IOException;
import java.net.URL;

import com.albertoborsetta.formscanner.gui.menubar.MenuBarController;
import com.albertoborsetta.formscanner.model.FormScannerModel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class DesktopController {

	private FormScannerModel model;
	MenuBarController menuBarController;
	private Parent root;

	@FXML
	private VBox desktop;

	public DesktopController(FormScannerModel model) {
		this.model = model;
		menuBarController = new MenuBarController(model);
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
			loader.setResources(model.getResourceBundle());
			loader.setLocation(fxmlURL);
			loader.setController(this);
			setRoot((Region) loader.load());
		} catch (RuntimeException | IOException e) {
			System.out.println("loader.getController()=" + loader.getController());
			System.out.println("loader.getLocation()=" + loader.getLocation());
			throw new RuntimeException("Failed to load " + fxmlURL.getFile(), e);
		}
	}

	private void setRoot(Region root) {
		this.root = root;
	}
}
