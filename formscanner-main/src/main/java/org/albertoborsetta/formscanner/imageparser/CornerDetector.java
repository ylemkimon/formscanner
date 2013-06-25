package org.albertoborsetta.formscanner.imageparser;

import ij.plugin.filter.Convolver;
import ij.process.Blitter;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.albertoborsetta.formscanner.commons.FormPoint;
import org.albertoborsetta.formscanner.commons.FormScannerConstants.Corners;

public class CornerDetector {

	public static final float DEFAULT_ALPHA = 0.050f;
	public static final int DEFAULT_THRESHOLD = 20000;
	private float alpha = DEFAULT_ALPHA;
	private int threshold = DEFAULT_THRESHOLD;
	private final int border = 20;
	
	private final float[] pfilt = {0.223755f,0.552490f,0.223755f};
	private final float[] dfilt = {0.453014f,0.0f,-0.453014f};
	private final float[] bfilt = {0.01563f,0.09375f,0.234375f,0.3125f,0.234375f,0.09375f,0.01563f};
	private ImageProcessor ipOrig;
	private FloatProcessor A;
	private FloatProcessor B;
	private FloatProcessor C;
	private FloatProcessor Q;
	private List<FormPoint> corners;
	private static FormPoint orig;

	public CornerDetector(ImageProcessor ip, Corners position) {
		this.ipOrig = ip;
		switch (position) {
		case TOP_LEFT:
			orig = new FormPoint(0, 0);
			break;
		case TOP_RIGHT:
			orig = new FormPoint(ip.getWidth(), 0);
			break;
		case BOTTOM_LEFT:
			orig = new FormPoint(0, ip.getHeight());
			break;
		case BOTTOM_RIGHT:
			orig = new FormPoint(ip.getWidth(), ip.getHeight());
			break;
	}
	}

	public FormPoint findCorners() {
		makeDerivatives();
		makeCrf(); //corner response function (CRF)
		corners = collectCorners(border);
		corners = cleanupCorners(corners);
		return corners.get(0);
	}
	
	private void makeDerivatives() {
		FloatProcessor Ix = (FloatProcessor) ipOrig.convertToFloat();
		FloatProcessor Iy = (FloatProcessor) ipOrig.convertToFloat();

		Ix = convolve1h(convolve1h(Ix,pfilt),dfilt);
		Iy = convolve1v(convolve1v(Iy,pfilt),dfilt);

		A = sqr((FloatProcessor) Ix.duplicate());
		A = convolve2(A,bfilt);

		B = sqr((FloatProcessor) Iy.duplicate());
		B = convolve2(B,bfilt);

		C = mult((FloatProcessor)Ix.duplicate(),Iy);
		C = convolve2(C,bfilt);
	}
	
	private void makeCrf() { // corner response function (CRF)
		int w = ipOrig.getWidth();
		int h = ipOrig.getHeight();
		Q = new FloatProcessor(w,h);
		float[] Apix = (float[]) A.getPixels();
		float[] Bpix = (float[]) B.getPixels();
		float[] Cpix = (float[]) C.getPixels();
		float[] Qpix = (float[]) Q.getPixels();
		for (int v=0; v<h; v++) {
			for (int u=0; u<w; u++) {
				int i = v*w+u;
				float a = Apix[i], b = Bpix[i], c = Cpix[i];
				float det = a*b-c*c;
				float trace = a+b;
				Qpix[i] = det - alpha * (trace * trace);
			}
		}
	}
	
	private List<FormPoint> collectCorners(int border) {
		List<FormPoint> cornerList = new Vector<FormPoint>(1000);
		int w = Q.getWidth();
		int h = Q.getHeight();
		float[] Qpix = (float[]) Q.getPixels();
		for (int v=border; v<h-border; v++){
			for (int u=border; u<w-border; u++) {
				float q = Qpix[v*w+u];				
				if (q>threshold && isLocalMax(Q,u,v)) {
					double dist = orig.dist2(u, v);
					FormPoint c = new FormPoint(u, v, dist);
					cornerList.add(c);
				}
			}
		}
		Collections.sort(cornerList);
		return cornerList;
	}
	
	private List<FormPoint> cleanupCorners(List<FormPoint> corners) {
		FormPoint[] cornerArray = new FormPoint[corners.size()];
		cornerArray = corners.toArray(cornerArray);
		List<FormPoint> goodCorners = new Vector<FormPoint>(corners.size());
		for (int i=0; i<cornerArray.length; i++){
			if (cornerArray[i] != null) {
				FormPoint c1 = cornerArray[i];
				goodCorners.add(c1);
				for (int j=i+1; j<cornerArray.length; j++){
					cornerArray[j] = null;
				}
			}
		}
		return goodCorners;
	}
	
	private static FloatProcessor convolve1h (FloatProcessor p, float[] h) {
		Convolver conv = new Convolver();
		conv.setNormalize(false);
		conv.convolve(p, h, 1, h.length);
		return p;
	}

	private static FloatProcessor convolve1v (FloatProcessor p, float[] h) {
		Convolver conv = new Convolver();
		conv.setNormalize(false);
		conv.convolve(p, h, h.length, 1);
		return p;
	}

	private static FloatProcessor convolve2 (FloatProcessor p, float[] h) {
		convolve1h(p,h);
		convolve1v(p,h);
		return p;
	}

	private static FloatProcessor sqr (FloatProcessor fp1) {
		fp1.sqr();
		return fp1;
	}

	private static FloatProcessor mult (FloatProcessor fp1, FloatProcessor fp2) {
		int mode = Blitter.MULTIPLY;
		fp1.copyBits(fp2, 0, 0, mode);
		return fp1;
	}

	private static boolean isLocalMax (FloatProcessor fp, int u, int v) {
		int w = fp.getWidth();
		int h = fp.getHeight();
		if (u<=0 || u>=w-1 || v<=0 || v>=h-1) {
			return false;
		} else {
			float[] pix = (float[]) fp.getPixels();
			int i0 = (v-1)*w+u, i1 = v*w+u, i2 = (v+1)*w+u;
			float cp = pix[i1];
			return
					cp > pix[i0-1] && cp > pix[i0] && cp > pix[i0+1] &&
					cp > pix[i1-1] && cp > pix[i1+1] && cp > pix[i2-1] && 
					cp > pix[i2] && cp > pix[i2+1] ;
		}
	}
}
