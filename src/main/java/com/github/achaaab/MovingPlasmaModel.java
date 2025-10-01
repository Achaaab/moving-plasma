package com.github.achaaab;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

import static java.lang.Math.clamp;
import static java.lang.Math.cos;
import static java.lang.Math.max;
import static java.lang.Math.round;
import static java.lang.Math.sin;
import static java.lang.Math.toIntExact;
import static java.lang.Runtime.getRuntime;
import static java.lang.Thread.currentThread;

public class MovingPlasmaModel {

	private static final int THREAD_COUNT = max(1, getRuntime().availableProcessors() - 1);

	private final AtomicInteger columnProvider;

	private double time;

	private int a;
	private int b;
	private int c;
	private int d;
	private int e;

	private int width;
	private int height;
	private int[] data;

	public MovingPlasmaModel() {

		time = 0.0;

		a = 20;
		b = 25;
		c = 127;
		d = 128;
		e = 100;

		columnProvider = new AtomicInteger(0);
	}

	public void update(double deltaTime, BufferedImage image) {

		time += (deltaTime * e / 100.0);

		width = image.getWidth();
		height = image.getHeight();

		var dataBuffer = (DataBufferInt) image.getRaster().getDataBuffer();
		data = dataBuffer.getData();

		columnProvider.set(0);

		var threads = Stream.generate(this::columnComputingTask).
				map(Thread::new).limit(THREAD_COUNT).
				toList();

		for (var thread : threads) {
			thread.start();
		}

		for (var thread : threads) {

			try {
				thread.join();
			} catch (InterruptedException interruptedException) {
				currentThread().interrupt();
			}
		}
	}

	private Runnable columnComputingTask() {

		return () -> {

			int x;

			while ((x = columnProvider.getAndIncrement()) < width) {
				computeColumn(x);
			}
		};
	}

	private void computeColumn(int x) {

		var i = x;

		for (var y = 0; y < height; y++) {

			data[i] = getRgb(x, y);
			i += width;
		}
	}

	private int getRgb(double x, double y) {

		var red = (sin(x / a + time) + sin(y / b + time)) * c + d;
		var green = (sin(x / b - time) + cos(y / a + time)) * c + d;
		var blue = (cos(x / a + time) + sin(y / b - time)) * c + d;

		return ((clamp(toIntExact(round(red)), 0, 255) & 0xFF) << 16) |
				((clamp(toIntExact(round(green)), 0, 255) & 0xFF) << 8) |
				((clamp(toIntExact(round(blue)), 0, 255) & 0xFF));
	}

	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	public int getC() {
		return c;
	}

	public void setC(int c) {
		this.c = c;
	}

	public int getD() {
		return d;
	}

	public void setD(int d) {
		this.d = d;
	}

	public int getE() {
		return e;
	}

	public void setE(int e) {
		this.e = e;
	}
}
