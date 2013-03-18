package org.albertoborsetta.formscanner.newparser;

import ij.IJ;
import ij.ImagePlus;
import ij.plugin.filter.Convolver;
import ij.process.Blitter;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.albertoborsetta.formscanner.commons.Point;


public class CornerDetector {

	public static final float DEFAULT_ALPHA = 0.050f;
	public static final int DEFAULT_THRESHOLD = 20000;
	private float alpha = DEFAULT_ALPHA;
	private int threshold = DEFAULT_THRESHOLD;
	private double dmin = 10;
	private double dmax = 80;
	private final int border = 20;

	public static final int TOP_LEFT = 1;
	public static final int TOP_RIGHT = 2;
	public static final int BOTTOM_LEFT = 3;
	public static final int BOTTOM_RIGHT = 4;
	
	private final float[] pfilt = {0.223755f,0.552490f,0.223755f};
	private final float[] dfilt = {0.453014f,0.0f,-0.453014f};
	private final float[] bfilt = {0.01563f,0.09375f,0.234375f,0.3125f,0.234375f,0.09375f,0.01563f};
	private ImageProcessor ipOrig;
	private FloatProcessor A;
	private FloatProcessor B;
	private FloatProcessor C;
	private FloatProcessor Q;
	private List<Point> corners;
	private static Point orig;

	public CornerDetector(ImageProcessor ip, int pos) {
		this.ipOrig = ip;
		switch (pos) {
		case TOP_LEFT:
			orig = new Point(0, 0);
			break;
		case TOP_RIGHT:
			orig = new Point(ip.getWidth(), 0);
			break;
		case BOTTOM_LEFT:
			orig = new Point(0, ip.getHeight());
			break;
		case BOTTOM_RIGHT:
			orig = new Point(ip.getWidth(), ip.getHeight());
			break;
	}
	}

	public Point findCorners() {
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
	
	private List<Point> collectCorners(int border) {
		List<Point> cornerList = new Vector<Point>(1000);
		int w = Q.getWidth();
		int h = Q.getHeight();
		float[] Qpix = (float[]) Q.getPixels();
		for (int v=border; v<h-border; v++){
			for (int u=border; u<w-border; u++) {
				float q = Qpix[v*w+u];				
				if (q>threshold && isLocalMax(Q,u,v)) {
					double dist = orig.dist2(u, v);
					Point c = new Point(u, v, dist);
					cornerList.add(c);
				}
			}
		}
		Collections.sort(cornerList);
		return cornerList;
	}
	
	private List<Point> cleanupCorners(List<Point> corners) {
		double dmin2 = dmin*dmin;
		double dmax2 = dmax*dmax;
		Point[] cornerArray = new Point[corners.size()];
		cornerArray = corners.toArray(cornerArray);
		List<Point> goodCorners = new Vector<Point>(corners.size());
		for (int i=0; i<cornerArray.length; i++){
			if (cornerArray[i] != null) {
				Point c1 = cornerArray[i];
				goodCorners.add(c1);
				// delete all remaining corners close to c
				for (int j=i+1; j<cornerArray.length; j++){
					// if (cornerArray[j] != null) {
						// Corner c2 = cornerArray[j];
						// if ((c1.dist2(c2)<dmin2) || (c1.dist2(c2)>dmax2)) {
							cornerArray[j] = null; //delete corner
						// }
					// }
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
	
	void printCornerPoints(List<Point> crf) {
		int i = 0;
		for (Point ipt: crf){
			IJ.write((i++) + ": " + (double)ipt.getDistance() + " " + ipt.getX() + " " + ipt.getY());
		}
	}
	
	public ImageProcessor showCornerPoints(ImageProcessor ip) {
		ImageProcessor ipResult = ip.duplicate();
		int[] lookupTable = new int[256];
		for (int i=0; i<256; i++){
			lookupTable[i] = 128 + (i/2);
		}
		ipResult.applyTable(lookupTable);

		for (Point c: corners) {
			c.draw(ipResult);
		}
		return ipResult;
	}

	void showProcessor(ImageProcessor ip, String title) {
		ImagePlus win = new ImagePlus(title,ip);
		win.show();
	}
}
