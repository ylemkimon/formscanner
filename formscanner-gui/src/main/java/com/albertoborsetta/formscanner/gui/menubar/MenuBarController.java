package com.albertoborsetta.formscanner.gui.menubar;

import java.io.IOException;
import java.net.URL;

import com.albertoborsetta.formscanner.gui.desktop.DesktopController;
import com.albertoborsetta.formscanner.model.FormScannerModel;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.MenuBar;

public class MenuBarController {

	private DesktopController desktopController;

	@FXML
	private MenuBar menuBar;
	

	public MenuBarController(DesktopController desktopController) {
		this.desktopController = desktopController;
	}

	public MenuBar getMenuBar() {
		if (menuBar == null) {
			// Load root layout from fxml file.
			FXMLLoader loader = new FXMLLoader();
			final URL fxmlURL = MenuBarController.class.getResource("MenuBar.fxml");
			try {
				loader.setResources(model.getResourceBundle());
				loader.setLocation(fxmlURL);
				loader.setController(this);
				loader.load();
			} catch (RuntimeException | IOException e) {
				System.out.println("loader.getController()=" + loader.getController());
				System.out.println("loader.getLocation()=" + loader.getLocation());
				throw new RuntimeException("Failed to load " + fxmlURL.getFile(), e);
			}
		}
		return menuBar;
	}

}
