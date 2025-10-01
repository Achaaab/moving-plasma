package com.github.achaaab;

import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.Thread.currentThread;
import static javax.swing.SwingUtilities.invokeAndWait;

public class MovingPlasmaView extends JComponent {

	private BufferedImage image;

	public void update() {

		try {

			invokeAndWait(() -> paintImmediately(0, 0, getWidth() ,getHeight()));

		} catch (InterruptedException interruptedException) {

			interruptedException.printStackTrace();
			currentThread().interrupt();

		} catch (InvocationTargetException invocationTargetException) {

			invocationTargetException.printStackTrace();
		}
	}

	@Override
	public void paintComponent(Graphics graphics) {

		super.paintComponent(graphics);
		graphics.drawImage(image, 0, 0, this);
	}

	public BufferedImage getImage() {

		var width = getWidth();
		var height = getHeight();

		if (image == null || image.getWidth() != width || image.getHeight() != height) {
			image = new BufferedImage(width, height, TYPE_INT_RGB);
		}

		return image;
	}
}
