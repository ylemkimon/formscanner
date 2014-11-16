package org.albertoborsetta.formscanner.api;

import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;

import org.albertoborsetta.formscanner.api.FormPoint;
import org.albertoborsetta.formscanner.api.commons.Constants.Corners;

/**
 * The Class CornerDetector.
 */
public class CornerDetector implements Callable<FormPoint> {
	
	/** The Constant WHITE. */
	private static final int WHITE = 1;
	
	/** The Constant BLACK. */
	private static final int BLACK = 0;
	
	/** The Constant HALF_WINDOW_SIZE. */
	private static final int HALF_WINDOW_SIZE = 5;
	
	/** The Constant WINDOW_SIZE. */
	private static final int WINDOW_SIZE = (HALF_WINDOW_SIZE * 2) + 1;
	
	/** The Constant BEFORE_CIRCLES. */
	private static final int BEFORE_CIRCLES = 0;
	
	/** The Constant BLACK_CIRCLE. */
	private static final int BLACK_CIRCLE = 1;
	
	/** The Constant WHITE_CIRCLE. */
	private static final int WHITE_CIRCLE = 2;
	
	/** The Constant OUT_OF_CIRCLES. */
	private static final int OUT_OF_CIRCLES = 3;
	
	/** The Constant AFTER_CIRCLES. */
	private static final int AFTER_CIRCLES = 4;
	
	/** The height. */
	private int height;
	
	/** The width. */
	private int width;
	
	/** The sub image width. */
	private int subImageWidth;
	
	/** The sub image height. */
	private int subImageHeight;
	
	/** The image. */
	private BufferedImage image;
	
	/** The position. */
	private Corners position;
	
	/** The threshold. */
	private int threshold;
	
	/** The density. */
	private int density;

	/**
	 * Instantiates a new corner detector.
	 *
	 * @author Alberto Borsetta
	 * @param threshold the threshold
	 * @param density the density
	 * @param position the position
	 * @param image the image
	 */
	CornerDetector(int threshold, int density, Corners position,
			BufferedImage image) {
		this.image = image;
		height = image.getHeight();
		width = image.getWidth();

		subImageWidth = (int) (width / 2);
		subImageHeight = (int) (height / 2);
		this.position = position;
		this.threshold = threshold;
		this.density = density;
		
	}

	/**
	 * Checks if is white.
	 *
	 * @author Alberto Borsetta
	 * @param xi the xi
	 * @param yi the yi
	 * @param rgbArray the rgb array
	 * @return the int
	 */
	private int isWhite(int xi, int yi, int[] rgbArray) {
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

	/* (non-Javadoc)
	 * @see java.util.concurrent.Callable#call()
	 */
	public FormPoint call() throws Exception {
		Long sumX = 0L;
		Long sumY = 0L;
		Integer count = 0;
		double xc = 0;
		double yc = 0;
		int dx = 1;
		int dy = 1;
		int x = HALF_WINDOW_SIZE;
		int y = HALF_WINDOW_SIZE;
		int x0 = 0;
		int y0 = 0;
		int stato = BEFORE_CIRCLES;
		int pixel;
		int old_pixel;
		int[] rgbArray = new int[subImageWidth * subImageHeight];
		
		switch (position) {
		case TOP_RIGHT:
			x = subImageWidth - (HALF_WINDOW_SIZE + 1);
			x0 = width - (subImageWidth + 1);
			xc = width;
			yc = 0;
			dx = -1;
			break;
		case BOTTOM_LEFT:
			y = subImageHeight - (HALF_WINDOW_SIZE + 1);
			y0 = height - (subImageHeight + 1);
			xc = 0;
			yc = height;;
			dy = -1;
			break;
		case BOTTOM_RIGHT:
			x = subImageWidth - (HALF_WINDOW_SIZE + 1);
			y = subImageHeight - (HALF_WINDOW_SIZE + 1);
			x0 = width - (subImageWidth + 1);
			y0 = height - (subImageHeight + 1);
			xc = width;
			yc = height;
			dx = -1;
			dy = -1;
			break;
		default:
			break;
		}

		try {
			image.getRGB(x0, y0, subImageWidth, subImageHeight, rgbArray, 0,
					subImageWidth);
			
			for (int yi = y; (yi < (subImageHeight - HALF_WINDOW_SIZE)) && (yi >= HALF_WINDOW_SIZE); yi += dy) {
				pixel = WHITE;
				old_pixel = pixel;
	
				for (int xi = x; (xi < (subImageWidth - HALF_WINDOW_SIZE)) && (xi >= HALF_WINDOW_SIZE); xi += dx) {
	
					pixel = isWhite(xi, yi, rgbArray);
					double delta = dx * (x0 + xi - xc);
	
					if (pixel != old_pixel) {
						old_pixel = pixel;
						if ((stato == BLACK_CIRCLE) && (delta > 0)) {
							xc = sumX / count;
							stato = OUT_OF_CIRCLES;
							break;
						} else if ((stato == BLACK_CIRCLE) && (delta <= 0)) {
							stato = WHITE_CIRCLE;
						} else if ((stato == WHITE_CIRCLE) || (stato == BEFORE_CIRCLES)) {
							sumX += (x0 + xi);
							sumY += (y0 + yi);
							count++;
							stato = BLACK_CIRCLE;
						} else if (stato == OUT_OF_CIRCLES) {
							sumX += (x0 + xi);
							sumY += (y0 + yi);
							count++;
							stato = BLACK_CIRCLE;
						}
					} else {
						if ((stato == BLACK_CIRCLE)) {
							sumX += (x0 + xi);
							sumY += (y0 + yi);
							count++;
						} else if ((stato == OUT_OF_CIRCLES) && (delta > 0)) {
							stato = AFTER_CIRCLES;
							break;
						}
						
					}
				}
	
				if (stato == AFTER_CIRCLES) {
					break;
				}
			}
	
			if (count == 0) {
				return null;
			}
			
			xc = sumX / count;
			yc = sumY / count;
		} catch (Exception e) {
			e.printStackTrace();
		}
		FormPoint p = new FormPoint(xc, yc);
		return p;
	}

}