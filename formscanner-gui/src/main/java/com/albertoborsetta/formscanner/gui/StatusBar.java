package com.albertoborsetta.formscanner.gui;

import java.awt.ComponentOrientation;

import javax.swing.JProgressBar;

import org.apache.commons.lang3.StringUtils;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXStatusBar;

import com.albertoborsetta.formscanner.api.FormPoint;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslation;
import com.albertoborsetta.formscanner.commons.translation.FormScannerTranslationKeys;
import com.albertoborsetta.formscanner.controller.ImageFrameController;
import com.albertoborsetta.formscanner.gui.builder.LabelBuilder;
import com.albertoborsetta.formscanner.model.FormScannerModel;

public class StatusBar extends JXStatusBar {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final FormScannerModel model;

	private JXLabel XPositionValue;
	private JXLabel YPositionValue;
	private JProgressBar progressBar;
	private ComponentOrientation orientation;
	private ImageFrameController imageController;

	public StatusBar(FormScannerModel model) {
		this.model = model;
		orientation = this.model.getOrientation();
		setComponentOrientation(orientation);
		
		imageController = ImageFrameController.getInstance(model);
		imageController.add(this);
		
		setXPosition();
		setYPosition();

		Constraint progressBarConstraint = new Constraint(Constraint.ResizeBehavior.FILL);
		progressBar = new JProgressBar();
		add(progressBar, progressBarConstraint);
	}

	private void setXPosition() {
		XPositionValue = getTextField(FormScannerTranslationKeys.X_CURSOR_POSITION_LABEL);
		Constraint XPositionValueConstraint = new Constraint();
		XPositionValueConstraint.setFixedWidth(70);
		add(XPositionValue, XPositionValueConstraint);
	}

	private void setYPosition() {
		YPositionValue = getTextField(FormScannerTranslationKeys.Y_CURSOR_POSITION_LABEL);
		Constraint YPositionValueConstraint = new Constraint();
		YPositionValueConstraint.setFixedWidth(70);
		add(YPositionValue, YPositionValueConstraint);
	}
	
	private JXLabel getTextField(String label) {
		return new LabelBuilder(orientation)
				.withText(FormScannerTranslation.getTranslationFor(label))
				.withFontSize(11)
				.build();
	}

	public void setCursorPosition(FormPoint p) {
		XPositionValue.setText(StringUtils.substringBefore(XPositionValue.getText(), " ") + " " + p.getX());
		YPositionValue.setText(StringUtils.substringBefore(YPositionValue.getText(), " ") + " " + p.getY());
	}
	
	public void setup() {
	}
}
