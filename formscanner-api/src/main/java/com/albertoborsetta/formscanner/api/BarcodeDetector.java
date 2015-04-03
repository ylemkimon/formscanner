package com.albertoborsetta.formscanner.api;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.concurrent.Callable;

import com.albertoborsetta.formscanner.api.commons.Constants;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Reader;
import com.google.zxing.ReaderException;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.multi.GenericMultipleBarcodeReader;
import com.google.zxing.multi.MultipleBarcodeReader;
import com.google.zxing.Result;

public class BarcodeDetector implements Callable<HashMap<String, FormArea>> {

	// private static final Map<DecodeHintType,Object> HINTS;
	// private static final Map<DecodeHintType,Object> HINTS_PURE;
	//
	// static {
	// HINTS = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
	// HINTS.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
	// HINTS.put(DecodeHintType.POSSIBLE_FORMATS,
	// EnumSet.allOf(BarcodeFormat.class));
	// HINTS_PURE = new EnumMap<DecodeHintType, Object>(HINTS);
	// HINTS_PURE.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
	// }

	private FormTemplate template;
	private FormArea barcodeArea;
	private FormTemplate parent;
	private HashMap<String, FormArea> barcodes;
	private BufferedImage image;

	public BarcodeDetector(FormTemplate template, FormArea barcodeArea,
			BufferedImage image) {
		this.barcodeArea = barcodeArea;
		parent = template.getParentTemplate();
		this.image = image;
		barcodes = new HashMap<String, FormArea>();
	}

	public HashMap<String, FormArea> call() throws Exception {
		LuminanceSource source = new BufferedImageLuminanceSource(image);
		BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(
				source));
//		ArrayList<Result> results = new ArrayList<Result>();

		try {

			Reader reader = new MultiFormatReader();
//			try {
//				// Look for multiple barcodes
//				MultipleBarcodeReader multiReader = new GenericMultipleBarcodeReader(
//						reader);
//				Result[] multipleBarcodesResult = multiReader.decodeMultiple(bitmap,
//						Constants.HINTS);
//				if (multipleBarcodesResult != null) {
//					results.addAll(Arrays.asList(multipleBarcodesResult));
//				}
//			} catch (ReaderException re) {
//				savedException = re;
//			}

				try {
					// Look for pure barcode
					Result resultBarcode = reader.decode(bitmap,
							Constants.HINTS_PURE);
					if (resultBarcode != null) {
						FormArea resultArea = new FormArea(barcodeArea.getName(), barcodeArea.getCorners());
						resultArea.setType(barcodeArea.getType());
						resultArea.setText(resultBarcode.getText());
						barcodes.put(barcodeArea.getName(), resultArea);
					}
				} catch (ReaderException re) {
					re.printStackTrace();
				}

			if (barcodes.isEmpty()) {
				try {
					// Look for normal barcode in photo
					Result resultBarcode = reader.decode(bitmap, Constants.HINTS);
					if (resultBarcode != null) {
						FormArea resultArea = new FormArea(barcodeArea.getName(), barcodeArea.getCorners());
						resultArea.setType(barcodeArea.getType());
						resultArea.setText(resultBarcode.getText());
						barcodes.put(barcodeArea.getName(), resultArea);
					}
				} catch (ReaderException re) {
					re.printStackTrace();
				}
			}

			if (barcodes.isEmpty()) {
				try {
					// Try again with other binarizer
					BinaryBitmap hybridBitmap = new BinaryBitmap(
							new HybridBinarizer(source));
					Result resultBarcode = reader.decode(hybridBitmap,
							Constants.HINTS);
					if (resultBarcode != null) {
						FormArea resultArea = new FormArea(barcodeArea.getName(), barcodeArea.getCorners());
						resultArea.setType(barcodeArea.getType());
						resultArea.setText(resultBarcode.getText());
						barcodes.put(barcodeArea.getName(), resultArea);
					}
				} catch (ReaderException re) {
					re.printStackTrace();
				}
			}
		} catch (RuntimeException re) {
			re.printStackTrace();
		}
		return barcodes;
	}

}
