package com.github.achaaab;

import javax.swing.JComponent;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Logger;

import static java.awt.Toolkit.getDefaultToolkit;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import static java.lang.Math.max;
import static java.lang.Thread.currentThread;
import static java.util.logging.Level.WARNING;
import static java.util.logging.Logger.getLogger;
import static javax.swing.SwingUtilities.invokeAndWait;

/**
 * Moving plasma control view.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
public class MovingPlasmaView extends JComponent {

	private static final Logger LOGGER = getLogger(MovingPlasmaView.class.getName());

	private BufferedImage image;

	public void update() {

		try {

			invokeAndWait(() -> paintImmediately(0, 0, getWidth() ,getHeight()));
			getDefaultToolkit().sync();

		} catch (InterruptedException interruptedException) {

			currentThread().interrupt();

		} catch (InvocationTargetException invocationTargetException) {

			LOGGER.log(WARNING, invocationTargetException.getLocalizedMessage(), invocationTargetException);
		}
	}

	@Override
	public void paintComponent(Graphics graphics) {

		super.paintComponent(graphics);
		graphics.drawImage(image, 0, 0, this);
	}

	public BufferedImage getImage() {

		var width = max(1, getWidth());
		var height = max(1, getHeight());

		if (image == null || image.getWidth() != width || image.getHeight() != height) {
			image = new BufferedImage(width, height, TYPE_INT_RGB);
		}

		return image;
	}
}
