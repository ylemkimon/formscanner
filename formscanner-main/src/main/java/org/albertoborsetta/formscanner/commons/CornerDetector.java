package org.albertoborsetta.formscanner.commons;

import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;

import org.albertoborsetta.formscanner.commons.FormScannerConstants.Corners;

public class CornerDetector implements Callable<FormPoint> {
	
	private static final int WHITE = 1;
	private static final int BLACK = 0;
	private static final int HALF_WINDOW_SIZE = 5;
	private static final int WINDOW_SIZE = (HALF_WINDOW_SIZE * 2) + 1;
	private static final int PAGE_WIDTH = 210; // millimetri
	private static final int PAGE_HEIGHT = 297; // millimetri
	
	private int height;
	private int width;
	private int subImageWidth;
	private int subImageHeight;
	private BufferedImage image;
	
	private Corners position;
	private int threshold;
	private int density;

	CornerDetector(int thr, int ds, Corners pos,
			BufferedImage img) {
		image = img;
		height = image.getHeight();
		width = image.getWidth();

		double scale = width / PAGE_WIDTH;
		subImageWidth = (int) ((PAGE_WIDTH / 2) * scale);
		subImageHeight = (int) ((PAGE_HEIGHT / 2) * scale);
		position = pos;
		threshold = thr;
		density = ds;
		
	}

	private int isWhite(int xi, int yi, int[] rgbArray, int threshold, int density) {
		int blacks = 0;
		int total = WINDOW_SIZE * WINDOW_SIZE;
		for (int i=0; i<WINDOW_SIZE; i++) {
			for (int j=0; j<WINDOW_SIZE; j++) {
				int xji = xi - HALF_WINDOW_SIZE + j;
				int yji = yi - HALF_WINDOW_SIZE + i;
				int index = (yji * subImageWidth) + xji;
				if ((rgbArray[index] & (0xFF)) < threshold) {
					blacks++;
				}
			}
		}
		if ((blacks/ (double) total) >= (density / 100.0))
			return BLACK;
		return WHITE;
	}

	@Override
	public FormPoint call() throws Exception {
		boolean found = false;
		boolean passed = false;
		double Xc = 0;
		double Yc = 0;
		int centralPoints = 0;
		int dx = 1;
		int dy = 1;
		int x = HALF_WINDOW_SIZE;
		int y = HALF_WINDOW_SIZE;
		int x1 = 0;
		int y1 = 0;
		int stato;
		int pixel;
		int old_pixel;
		int[] rgbArray = new int[subImageWidth * subImageHeight];
		FormPoint[] points = new FormPoint[4];
		
		switch (position) {
		case TOP_RIGHT:
			x = subImageWidth - (HALF_WINDOW_SIZE + 1);
			x1 = width - (subImageWidth + 1);
			dx = -1;
			break;
		case BOTTOM_LEFT:
			y = subImageHeight - (HALF_WINDOW_SIZE + 1);
			y1 = height - (subImageHeight + 1);
			dy = -1;
			break;
		case BOTTOM_RIGHT:
			x = subImageWidth - (HALF_WINDOW_SIZE + 1);
			y = subImageHeight - (HALF_WINDOW_SIZE + 1);
			x1 = width - (subImageWidth + 1);
			y1 = height - (subImageHeight + 1);
			dx = -1;
			dy = -1;
			break;
		default:
			break;
		}

		image.getRGB(x1, y1, subImageWidth, subImageHeight, rgbArray, 0,
				subImageWidth);

		for (int yi = y; (yi < (subImageHeight - HALF_WINDOW_SIZE)) && (yi >= HALF_WINDOW_SIZE); yi += dy) {
			stato = 0;
			pixel = WHITE;
			old_pixel = pixel;

			for (int xi = x; (xi < (subImageWidth - HALF_WINDOW_SIZE)) && (xi >= HALF_WINDOW_SIZE); xi += dx) {

				pixel = isWhite(xi, yi, rgbArray, threshold, density);

				if (pixel != old_pixel) {
					stato++;
					old_pixel = pixel;
					switch (stato) {
					case 1:
						points[0] = new FormPoint(x1 + xi, y1 + yi);
					case 3:
						points[2] = new FormPoint(x1 + xi, y1 + yi);
						break;
					case 2:
						points[1] = new FormPoint(x1 + xi, y1 + yi);
					case 4:
						points[3] = new FormPoint(x1 + xi, y1 + yi);
						found = found || (stato==4);
						break;
					default:
						break;
					}
				}
				
				if ((found && (stato==4)) || (passed && (stato==2))) {
					break;
				}
			}

			switch (stato) {
			case 2:
				passed = passed || (found && (stato==2));
			case 4:
				double Xc1 = (points[0].getX() + points[3].getX()) / 2;
				double Xc2 = (points[1].getX() + points[2].getX()) / 2;
				centralPoints++;
				Xc += (Xc1 + Xc2) / 2;
				Yc += points[0].getY();
				break;
			case 0:
			case 1:
			case 3:
			default:
				break;
			}

			if (passed && found && (stato==0)) {
				break;
			}
		}

		if (centralPoints == 0) {
			return null;
		}
		Xc = Xc / centralPoints;
		Yc = Yc / centralPoints;
		
		FormPoint p = new FormPoint(Xc, Yc);
		return p;
	}

}
