package com.albertoborsetta.formscanner.gui.view;

import javax.swing.JInternalFrame;

import com.albertoborsetta.formscanner.gui.controller.InternalFrameController;
import com.albertoborsetta.formscanner.gui.model.FormScannerModel;

public class InternalFrame extends JInternalFrame {

	private static final long serialVersionUID = 1L;
	protected FormScannerModel model;
	protected InternalFrameController internalFrameController;
	
	protected InternalFrame(FormScannerModel model) {
		this.model = model;
		internalFrameController = InternalFrameController
				.getInstance(model);
		addInternalFrameListener(internalFrameController);
	}
}
