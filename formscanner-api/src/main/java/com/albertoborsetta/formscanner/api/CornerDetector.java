package com.albertoborsetta.formscanner.api;

import com.albertoborsetta.formscanner.api.commons.Constants.CornerType;

import java.awt.image.BufferedImage;
import java.util.concurrent.Callable;

import com.albertoborsetta.formscanner.api.commons.Constants.Corners;

/**
*
* @author Alberto Borsetta
* @version 1.2-SNAPSHOT
*/
public class CornerDetector extends FormScannerDetector
		implements Callable<FormPoint> {

	private static final int BEFORE = 0;
	private static final int BLACK = 1;
	private static final int WHITE = 2;
	private static final int OUT = 3;
	private static final int AFTER = 4;

	private final Corners position;
	private CornerType type;

	CornerDetector(int threshold, int density, Corners position, BufferedImage image, CornerType type) {
		super(threshold, density, image);
		this.type = type;
		this.position = position;
	}

	CornerDetector(int threshold, int density, Corners position, BufferedImage image) {
		this(threshold, density, position, image, CornerType.ROUND);
	}

	@Override
	public FormPoint call() throws Exception {
		int dx = 1;
		int dy = 1;
		int x = HALF_WINDOW_SIZE;
		int y = HALF_WINDOW_SIZE;
		int x0 = 0;
		int y0 = 0;

		switch (position) {
		case TOP_RIGHT:
			x = subImageWidth - (HALF_WINDOW_SIZE + 1);
			x0 = width - (subImageWidth + 1);
			dx = -1;
			break;
		case BOTTOM_LEFT:
			y = subImageHeight - (HALF_WINDOW_SIZE + 1);
			y0 = height - (subImageHeight + 1);
			dy = -1;
			break;
		case BOTTOM_RIGHT:
			x = subImageWidth - (HALF_WINDOW_SIZE + 1);
			y = subImageHeight - (HALF_WINDOW_SIZE + 1);
			x0 = width - (subImageWidth + 1);
			y0 = height - (subImageHeight + 1);
			dx = -1;
			dy = -1;
			break;
		default:
			break;
		}

		return (type.equals(CornerType.ROUND))
				? searchRoundCorner(x0, y0, x, y, dx, dy)
				: searchAngularCorner(x0, y0, x, y, dx, dy);
	}

	private FormPoint searchRoundCorner(int x0, int y0, int x, int y, int dx,
			int dy) {
		Long sumX = 0L;
		Long sumY = 0L;
		Integer count = 0;

		int pixel;
		int old_pixel;

		int stato = BEFORE;
		
		FormPoint p = null;
		
		switch (position) {
		case TOP_LEFT:
			p = new FormPoint(0, 0);
			break;
		case TOP_RIGHT:
			p = new FormPoint(width, 0);
			break;
		case BOTTOM_RIGHT:
			p = new FormPoint(width, height);
			break;
		case BOTTOM_LEFT:
			p = new FormPoint(0, height);
			break;
		default:
			break;
		}

		int[] rgbArray = new int[subImageWidth * subImageHeight];
		image.getRGB(x0, y0, subImageWidth, subImageHeight, rgbArray, 0, subImageWidth);

		for (int yi = y; (yi < (subImageHeight - HALF_WINDOW_SIZE)) && (yi >= HALF_WINDOW_SIZE); yi += dy) {
			pixel = WHITE_PIXEL;
			old_pixel = pixel;

			for (int xi = x; (xi < (subImageWidth - HALF_WINDOW_SIZE)) && (xi >= HALF_WINDOW_SIZE); xi += dx) {

				pixel = isWhite(xi, yi, rgbArray);
				double delta = dx * (x0 + xi - p.getX());

				if (pixel != old_pixel) {
					old_pixel = pixel;
					if ((stato == BLACK) && (delta > 0)) {
						p.setX(sumX / count);
						stato = OUT;
						break;
					} else if ((stato == BLACK) && (delta <= 0)) {
						stato = WHITE;
					} else if ((stato == WHITE) || (stato == BEFORE)) {
						sumX += (x0 + xi);
						sumY += (y0 + yi);
						count++;
						stato = BLACK;
					} else if (stato == OUT) {
						sumX += (x0 + xi);
						sumY += (y0 + yi);
						count++;
						stato = BLACK;
					}
				} else {
					if ((stato == BLACK)) {
						sumX += (x0 + xi);
						sumY += (y0 + yi);
						count++;
					} else if ((stato == OUT) && (delta > 0)) {
						stato = AFTER;
						break;
					}

				}
			}

			if (stato == AFTER) {
				break;
			}
		}

		if (count != 0) {
			p.setX(sumX / count);
			p.setY(sumY / count);
		}

		return p;
	}

	private FormPoint searchAngularCorner(int x0, int y0, int x, int y, int dx,
			int dy) {
		int pixel;
		int old_pixel;

		int stato = BEFORE;

		FormPoint p = null;

		switch (position) {
		case TOP_LEFT:
			p = new FormPoint(0, 0);
			break;
		case TOP_RIGHT:
			p = new FormPoint(width, 0);
			break;
		case BOTTOM_RIGHT:
			p = new FormPoint(width, height);
			break;
		case BOTTOM_LEFT:
			p = new FormPoint(0, height);
			break;
		default:
			break;
		}

		int[] rgbArray = new int[subImageWidth * subImageHeight];
		image.getRGB(x0, y0, subImageWidth, subImageHeight, rgbArray, 0, subImageWidth);

		for (int yi = y; (yi < (subImageHeight - HALF_WINDOW_SIZE)) && (yi >= HALF_WINDOW_SIZE); yi += dy) {
			pixel = WHITE_PIXEL;
			old_pixel = pixel;

			for (int xi = x; (xi < (subImageWidth - HALF_WINDOW_SIZE)) && (xi >= HALF_WINDOW_SIZE); xi += dx) {

				pixel = isWhite(xi, yi, rgbArray);

				if (pixel != old_pixel) {
					old_pixel = pixel;

					if (stato == BEFORE) {
						p.setX(x0 + xi - (dx * HALF_WINDOW_SIZE));
						p.setY(y0 + yi);
						stato = BLACK;
						break;
					}
					double delta = dx * (p.getX() - (x0 + xi));
					if (delta >= 0) {
						p.setX(x0 + xi - (dx * HALF_WINDOW_SIZE));
						p.setY(y0 + yi);
						break;
					}
					stato = AFTER;
					break;
				}
			}

			if (stato == AFTER) {
				break;
			}
		}
		return p;
	}
}
