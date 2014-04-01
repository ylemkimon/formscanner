package org.albertoborsetta.formscanner.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SpringLayout;

import org.albertoborsetta.formscanner.commons.FormScannerConstants;
import org.albertoborsetta.formscanner.commons.resources.FormScannerResources;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import org.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import org.albertoborsetta.formscanner.gui.builder.ButtonBuilder;
import org.albertoborsetta.formscanner.gui.builder.PanelBuilder;
import org.albertoborsetta.formscanner.gui.builder.TabbedPaneBuilder;
import org.albertoborsetta.formscanner.gui.controller.AboutFrameController;
import org.albertoborsetta.formscanner.model.FormScannerModel;

public class AboutFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private FormScannerModel model;
	private AboutFrameController aboutFrameController;

	/**
	 * Create the frame.
	 */
	public AboutFrame(FormScannerModel model) {
		this.model = model;

		aboutFrameController = new AboutFrameController(this.model);
		aboutFrameController.add(this);

		setName(FormScannerConstants.ABOUT_FRAME_NAME);
		setTitle(FormScannerTranslation
				.getTranslationFor(FormScannerTranslationKeys.ABOUT_FRAME_TITLE));
		setBounds(100, 100, 600, 500);
		setMinimumSize(new Dimension(300, 500));
		setResizable(false);

		JPanel aboutPanel = getAboutPanel();
		JPanel licensePanel = getLicensePanel();
		JPanel buttonPanel = getButtonPanel();

		JTabbedPane tabbedPane = new TabbedPaneBuilder(JTabbedPane.TOP)
				.addTab(FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.ABOUT_TAB_NAME),
						aboutPanel)
				.addTab(FormScannerTranslation
						.getTranslationFor(FormScannerTranslationKeys.LICENSE_TAB_NAME),
						licensePanel).build();
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);

		tabbedPane.setEnabledAt(1, true);
	}

	private JPanel getLicensePanel() {
		JScrollPane licenseTextPanel = getLiceseTextPanel();

		return new PanelBuilder().withLayout(new BorderLayout())
				.addComponent(licenseTextPanel, BorderLayout.CENTER).build();
	}

	private JScrollPane getLiceseTextPanel() {
		JTextArea textArea = new JTextArea(300, 500);
		textArea.setEditable(false);
		textArea.setTabSize(4);
		textArea.getCaret().setDot(0);
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		textArea.setEnabled(true);

		JScrollPane scrollPane = new JScrollPane(textArea);
		scrollPane.setPreferredSize(new Dimension(400, 300));
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		scrollPane.setAlignmentY(Component.TOP_ALIGNMENT);

		String licenseText = "";
		try {
			File license = FormScannerResources.getLicense();
			FileReader fileReader;
			fileReader = new FileReader(license);
			BufferedReader reader = new BufferedReader(fileReader);

			String temp;
			while ((temp = reader.readLine()) != null) {
				licenseText += temp + "\n";
			}

			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		textArea.append(licenseText);
		textArea.getCaret().setDot(0);

		return scrollPane;
	}

	private JPanel getAboutPanel() {
		// JLabel logo = new JLabel(
		// FormScannerResources
		// .getIconFor(FormScannerResourcesKeys.FORMSCANNER_ICON));

		JEditorPane aboutTextPanel = getAboutTextPanel();

		return new PanelBuilder().withLayout(new BorderLayout())
		// .addComponent(logo, BorderLayout.WEST)
				.addComponent(aboutTextPanel, BorderLayout.CENTER).build();
	}

	private JEditorPane getAboutTextPanel() {
		JEditorPane text = new JEditorPane();
		text.setAlignmentX(Component.CENTER_ALIGNMENT);
		text.setAlignmentY(Component.BOTTOM_ALIGNMENT);
		text.setContentType("text/html");
		text.setOpaque(true);
		text.addHyperlinkListener(aboutFrameController);
		text.setText(FormScannerTranslation
				.getTranslationFor(FormScannerTranslationKeys.ABOUT_TEXT));
		text.setEditable(false);

		return text;
	}

	private JPanel getButtonPanel() {
		JButton okButton = new ButtonBuilder()
				.withText(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON))
				.withToolTip(
						FormScannerTranslation
								.getTranslationFor(FormScannerTranslationKeys.OK_BUTTON_TOOLTIP))
				.withActionCommand(FormScannerConstants.CONFIRM)
				.withActionListener(aboutFrameController).setEnabled(true)
				.build();

		return new PanelBuilder().withLayout(new SpringLayout())
				.addComponent(okButton).withGrid(1, 1).build();
	}
}
