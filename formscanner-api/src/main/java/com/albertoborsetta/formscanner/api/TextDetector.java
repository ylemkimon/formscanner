package com.albertoborsetta.formscanner.api;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.concurrent.Callable;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

import org.apache.commons.lang3.StringUtils;

import com.albertoborsetta.formscanner.api.commons.Constants.Corners;

public class TextDetector implements Callable<HashMap<String, FormArea>> {

	private FormTemplate template;
	private FormArea textArea;
	private FormTemplate parent;
	private HashMap<String, FormArea> texts;
	private BufferedImage image;

	// TODO Javadoc
	public TextDetector(FormTemplate template, FormArea textArea,
			BufferedImage image) {
		this.template = template;
		this.textArea = textArea;
		parent = template.getParentTemplate();
		this.image = image;
		texts = new HashMap<String, FormArea>();
	}

	// TODO Javadoc
	public HashMap<String, FormArea> call() throws Exception {
		
		Tesseract instance = Tesseract.getInstance(); //

		String result = null;
		try {
			result = instance.doOCR(image);
		} catch (TesseractException e) {
			// Do nothing
		}

		FormArea resultArea = calcResultArea();
		resultArea.setText(result != null ? result : StringUtils.EMPTY);
		texts.put(textArea.getName(), resultArea);
		return texts;
	}

	private FormArea calcResultArea() {
		FormArea responseArea = new FormArea(textArea.getName());

		for (Corners corner : Corners.values()) {
			responseArea.setCorner(corner, calcResponsePoint(textArea.getCorner(corner)));
		}

		responseArea.setType(textArea.getType());
		return responseArea;
	}

	private FormPoint calcResponsePoint(FormPoint responsePoint) {
		FormPoint point = responsePoint.clone();

		FormPoint templateOrigin = parent.getCorner(Corners.TOP_LEFT);
		double templateRotation = parent.getRotation();
		double scale = Math.sqrt(template.getDiagonal() / parent.getDiagonal());

		point.rotoTranslate(templateOrigin, templateRotation, true);
		point.scale(scale);
		point.rotoTranslate(template.getCorners().get(Corners.TOP_LEFT),
				template.getRotation(), false);
		return point;
	}

}
