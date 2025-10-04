package com.github.achaaab;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import static java.lang.Math.clamp;
import static java.lang.Math.cos;
import static java.lang.Math.fma;
import static java.lang.Math.round;
import static java.lang.Math.sin;
import static java.lang.Math.toIntExact;
import static java.util.stream.IntStream.range;

/**
 * Moving plasma model.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class MovingPlasmaModel {

	private double time;

	private int factorX;
	private int factorY;
	private int colorFactor;
	private int colorOffset;
	private int timeFactor;

	private int width;
	private int height;
	private int[] data;

	private double[] redX;
	private double[] greenX;
	private double[] blueX;
	private double[] redY;
	private double[] greenY;
	private double[] blueY;

	/**
	 * Creates a moving plasma.
	 *
	 * @since 0.0.0
	 */
	public MovingPlasmaModel() {

		time = 0.0;

		factorX = 20;
		factorY = 25;
		colorFactor = 127;
		colorOffset = 128;
		timeFactor = 100;
	}

	/**
	 * Update image data.
	 *
	 * @param deltaTime elapsed time since the last update, in seconds
	 * @param image image to update
	 * @since 0.0.0
	 */
	public void update(double deltaTime, BufferedImage image) {

		time += (deltaTime * timeFactor / 100.0);

		width = image.getWidth();
		height = image.getHeight();

		var dataBuffer = (DataBufferInt) image.getRaster().getDataBuffer();
		data = dataBuffer.getData();

		memoize();
		range(0, height).parallel().forEach(this::computeRow);
	}

	/**
	 * Memoizes sines and cosines.
	 *
	 * @since 0.0.0
	 */
	private void memoize() {

		redX = new double[width];
		greenX = new double[width];
		blueX = new double[width];
		redY = new double[height];
		greenY = new double[height];
		blueY = new double[height];

		for (var x = 0; x < width; x++) {

			redX[x] = sin((double) x / factorX + time);
			greenX[x] = sin((double) x / factorY - time);
			blueX[x] = cos((double) x / factorX + time);
		}

		for (var y = 0; y < height; y++) {

			redY[y] = sin((double) y / factorY + time);
			greenY[y] = cos((double) y / factorX + time);
			blueY[y] = sin((double) y / factorY - time);
		}
	}

	/**
	 * Compute a whole row.
	 *
	 * @param y row index
	 * @since 0.0.0
	 */
	private void computeRow(int y) {

		var i = y * width;

		for (var x = 0; x < width; x++) {
			data[i++] = getRgb(x, y);
		}
	}

	/**
	 * Get RGB value for a given pixel.
	 *
	 * @param x x coordinate of the pixel
	 * @param y y coordinate of the pixel
	 * @return RGB value for the given pixel
	 * @since 0.0.0
	 */
	private int getRgb(int x, int y) {

		var red = fma(redX[x] + redY[y], colorFactor, colorOffset);
		var green = fma(greenX[x] + greenY[y], colorFactor, colorOffset);
		var blue = fma(blueX[x] + blueY[y], colorFactor, colorOffset);

		var r = clamp(toIntExact(round(red)), 0, 255);
		var g = clamp(toIntExact(round(green)), 0, 255);
		var b = clamp(toIntExact(round(blue)), 0, 255);

		return r << 16 | g << 8 | b;
	}

	public int getxFactor() {
		return factorX;
	}

	public void setxFactor(int xFactor) {
		this.factorX = xFactor;
	}

	public int getFactorY() {
		return factorY;
	}

	public void setFactorY(int factorY) {
		this.factorY = factorY;
	}

	public int getColorFactor() {
		return colorFactor;
	}

	public void setColorFactor(int colorFactor) {
		this.colorFactor = colorFactor;
	}

	public int getColorOffset() {
		return colorOffset;
	}

	public void setColorOffset(int colorOffset) {
		this.colorOffset = colorOffset;
	}

	public int getTimeFactor() {
		return timeFactor;
	}

	public void setTimeFactor(int timeFactor) {
		this.timeFactor = timeFactor;
	}
}
