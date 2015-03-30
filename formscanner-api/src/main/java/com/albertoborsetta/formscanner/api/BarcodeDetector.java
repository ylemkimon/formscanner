package com.albertoborsetta.formscanner.api;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

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
import com.google.common.collect.Lists;

public class BarcodeDetector implements Callable<HashMap<String, FormArea>> {
	
	private static final Map<DecodeHintType,Object> HINTS;
	private static final Map<DecodeHintType,Object> HINTS_PURE;
	
	static {
	    HINTS = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);
	    HINTS.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
	    HINTS.put(DecodeHintType.POSSIBLE_FORMATS, EnumSet.allOf(BarcodeFormat.class));
	    HINTS_PURE = new EnumMap<DecodeHintType, Object>(HINTS);
	    HINTS_PURE.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
	  }
	
	private FormTemplate template;
	private FormArea barcodeArea;
	private FormTemplate parent;
	private HashMap<String, FormArea> barcodes;
	private BufferedImage image;

	public BarcodeDetector(FormTemplate template,
			FormArea barcodeArea, BufferedImage image) {
		this.barcodeArea = barcodeArea;
		parent = template.getParentTemplate();
		this.image = image;
	}

	public HashMap<String, FormArea> call() throws Exception {
		LuminanceSource source = new BufferedImageLuminanceSource(image);
	    BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(source));
	    Collection<Result> results = Lists.newArrayListWithCapacity(1);

	    try {

	      Reader reader = new MultiFormatReader();
	      ReaderException savedException = null;
	      try {
	        // Look for multiple barcodes
	        MultipleBarcodeReader multiReader = new GenericMultipleBarcodeReader(reader);
	        Result[] theResults = multiReader.decodeMultiple(bitmap, HINTS);
	        if (theResults != null) {
	          results.addAll(Arrays.asList(theResults));
	        }
	      } catch (ReaderException re) {
	        savedException = re;
	      }
	  
	      if (results.isEmpty()) {
	        try {
	          // Look for pure barcode
	          Result theResult = reader.decode(bitmap, HINTS_PURE);
	          if (theResult != null) {
	            results.add(theResult);
	          }
	        } catch (ReaderException re) {
	          savedException = re;
	        }
	      }
	  
	      if (results.isEmpty()) {
	        try {
	          // Look for normal barcode in photo
	          Result theResult = reader.decode(bitmap, HINTS);
	          if (theResult != null) {
	            results.add(theResult);
	          }
	        } catch (ReaderException re) {
	          savedException = re;
	        }
	      }
	  
	      if (results.isEmpty()) {
	        try {
	          // Try again with other binarizer
	          BinaryBitmap hybridBitmap = new BinaryBitmap(new HybridBinarizer(source));
	          Result theResult = reader.decode(hybridBitmap, HINTS);
	          if (theResult != null) {
	            results.add(theResult);
	          }
	        } catch (ReaderException re) {
	          savedException = re;
	        }
	      }
	  
	      if (results.isEmpty()) {
	        return null;
	      }

	    } catch (RuntimeException re) {
	    	return null;
	    }
		return null;
	}

}
