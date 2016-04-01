/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.albertoborsetta.formscanner.gui;

import com.albertoborsetta.formscanner.commons.FormScannerConstants;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import com.albertoborsetta.formscanner.controller.FormScannerController;
import com.albertoborsetta.formscanner.gui.builder.PanelBuilder;
import com.albertoborsetta.formscanner.model.FormScannerModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.io.UnsupportedEncodingException;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author Alberto Borsetta
 */
public class FormScannerWorkspace extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final ComponentOrientation orientation;
	private final FormScannerModel model;
	private final FormScannerController mainFrameController;
	private final MenuBar menuBar;
	private final ToolBar toolBar;
	private OptionsPanel optionsPanel;
	private DataPanel dataPanel;
	private JDesktopPane desktop;
	private StatusBar statusBar;
	private JSplitPane columnSplit;
	private JSplitPane rowSplit;

	private int lastOptionsDividerLocation;
	private int lastDataDividerLocation;

	/**
	 * Create the application.
	 * 
	 * @throws java.io.UnsupportedEncodingException
	 */
	public FormScannerWorkspace(FormScannerModel model) throws UnsupportedEncodingException {
		this.model = model;
		orientation = model.getOrientation();
		model.setWorkspace(this);
		mainFrameController = FormScannerController.getInstance(model);
		addWindowListener(mainFrameController);

		setName(FormScannerConstants.Frame.DESKTOP_FRAME.name());
		setExtendedState(java.awt.Frame.MAXIMIZED_BOTH);

		setTitle(StringUtils.replace(
				FormScannerTranslation.getTranslationFor(FormScannerTranslationKeys.FORMSCANNER_MAIN_TITLE),
				FormScannerConstants.VERSION_KEY, FormScannerConstants.VERSION));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setDefaultLookAndFeelDecorated(true);
		setComponentOrientation(orientation);

		getContentPane().setLayout(new BorderLayout());
		menuBar = new MenuBar(model);
		setJMenuBar(menuBar);
		model.setDefaultFramePositions();

		desktop = new JDesktopPane();
		desktop.setBackground(new Color(252, 252, 252, 255));
		dataPanel = new DataPanel(model, desktop);

		JPanel desktopPanel = new PanelBuilder(orientation).withLayout(new BorderLayout())
				.add(dataPanel.getPanelControls(), BorderLayout.SOUTH).add(dataPanel, BorderLayout.CENTER).build();

		optionsPanel = new OptionsPanel(model, desktopPanel);

		toolBar = new ToolBar(model);
		toolBar.setup();

		statusBar = new StatusBar(model);
		statusBar.setup();

		getContentPane().add(toolBar, BorderLayout.NORTH);
		getContentPane().add(optionsPanel.getPanelControls(), BorderLayout.WEST);
		getContentPane().add(optionsPanel, BorderLayout.CENTER);
		getContentPane().add(statusBar, BorderLayout.SOUTH);

		setVisible(true);
	}

	public void arrangeFrame(InternalFrame frame) {
		boolean found = false;

		for (Component component : desktop.getComponents()) {
			if (component.getName().equals(frame.getName())) {
				component.setVisible(false);
				desktop.remove(component);
				found = true;
				break;
			}
		}

		if (!found) {
			desktop.add(frame);
			frame.setVisible(true);
		} else {
			arrangeFrame(frame);
		}
	}

	public void disposeFrame(InternalFrame frame) {
		if (frame != null) {
			model.setLastPosition(FormScannerConstants.Frame.valueOf(frame.getName()), frame.getBounds());
			frame.dispose();
		}
		setDefaultControllersEnabled();
		model.resetFirstPass();
	}

	public void setDefaultControllersEnabled() {
		toolBar.setScanControllersEnabled(true);
		toolBar.setScanAllControllersEnabled(true);
		toolBar.setScanCurrentControllersEnabled(false);
		menuBar.setScanControllersEnabled(true);
		menuBar.setScanAllControllersEnabled(true);
	}

	public void setScanControllersEnabled(boolean enable) {
		toolBar.setScanControllersEnabled(enable);
		menuBar.setScanControllersEnabled(enable);
	}

	public void setScanAllControllersEnabled(boolean enable) {
		toolBar.setScanAllControllersEnabled(enable);
		menuBar.setScanAllControllersEnabled(enable);
	}

	public void setScanCurrentControllersEnabled(boolean enable) {
		toolBar.setScanCurrentControllersEnabled(enable);
	}

	public void setTemplateData() {
		optionsPanel.setDefaultValues();
	}

	public void setPanelDivider(JComponent panel) {
		if (panel instanceof DataPanel) {
			boolean collapsed = columnSplit.getDividerLocation() == 0;
			if (!collapsed) {
				lastDataDividerLocation = columnSplit.getDividerLocation();
				columnSplit.getBottomComponent().setMinimumSize(new Dimension());
				columnSplit.setDividerLocation(0.0d);
			} else {
				columnSplit.setDividerLocation(lastDataDividerLocation);
			}
		}
		if (panel instanceof OptionsPanel) {
			boolean collapsed = rowSplit.getDividerLocation() == 0;
			if (!collapsed) {
				lastOptionsDividerLocation = rowSplit.getDividerLocation();
				rowSplit.getLeftComponent().setMinimumSize(new Dimension());
				rowSplit.setDividerLocation(0.0d);
			} else {
				rowSplit.setDividerLocation(lastOptionsDividerLocation);
			}
		}
	}

	public void setupFieldsTable() {
		dataPanel.setupFieldsTable();
	}
}
