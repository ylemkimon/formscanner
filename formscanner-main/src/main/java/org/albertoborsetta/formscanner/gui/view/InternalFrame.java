package org.albertoborsetta.formscanner.gui.view;

import javax.swing.JInternalFrame;

import org.albertoborsetta.formscanner.gui.controller.InternalFrameController;
import org.albertoborsetta.formscanner.gui.model.FormScannerModel;

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
