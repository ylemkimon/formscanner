package com.albertoborsetta.formscanner.gui.view;

import java.awt.ComponentOrientation;

import javax.swing.JInternalFrame;

import com.albertoborsetta.formscanner.gui.controller.InternalFrameController;
import com.albertoborsetta.formscanner.gui.model.FormScannerModel;

public class InternalFrame extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	protected FormScannerModel model;
	protected InternalFrameController internalFrameController;
	protected ComponentOrientation orientation;
	
	protected InternalFrame(FormScannerModel model) {
		this.model = model;
		orientation = model.getOrientation();
		internalFrameController = InternalFrameController
				.getInstance(model);
		addInternalFrameListener(internalFrameController);
	}
}
