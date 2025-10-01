package com.github.achaaab;

import static java.awt.Toolkit.getDefaultToolkit;
import static java.lang.Math.round;
import static java.lang.System.nanoTime;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class MovingPlasmaController implements Runnable {

	private final MovingPlasmaModel model;
	private final MovingPlasmaView view;
	private final MovingPlasmaPanel panel;

	private final long targetFrameDuration;

	public MovingPlasmaController(MovingPlasmaModel model, MovingPlasmaView view, MovingPlasmaPanel panel) {

		this.model = model;
		this.view = view;
		this.panel = panel;

		targetFrameDuration = round(1_000_000_000 / 60.0);

		panel.a().setValue(model.getA());
		panel.b().setValue(model.getB());
		panel.c().setValue(model.getC());
		panel.d().setValue(model.getD());
		panel.e().setValue(model.getE());
	}

	public void update(double deltaTime) {

		model.setA(panel.a().getValue());
		model.setB(panel.b().getValue());
		model.setC(panel.c().getValue());
		model.setD(panel.d().getValue());
		model.setE(panel.e().getValue());

		var image = view.getImage();

		model.update(deltaTime, image);
		view.update();
	}

	@Override
	public void run() {

		var frameStartTime = nanoTime();
		var frameEndTime = frameStartTime;

		while (true) {

			var deltaTime = frameEndTime - frameStartTime;
			frameStartTime = frameEndTime;

			update(deltaTime / 1_000_000_000.0);
			getDefaultToolkit().sync();

			frameEndTime = ensureFrameDuration(frameStartTime, nanoTime());
		}
	}

	/**
	 * Ensures frame duration.
	 *
	 * @param frameStartTime frame start time
	 * @param frameEndTime actual frame end time
	 * @return frame end time after temporisation
	 * @since 0.0.0
	 */
	private long ensureFrameDuration(long frameStartTime, long frameEndTime) {

		var frameDuration = frameEndTime - frameStartTime;

		if (frameDuration < targetFrameDuration) {

			var freeTime = (targetFrameDuration - frameDuration) / 1_000_000;

			if (freeTime > 0) {

				try {

					sleep(freeTime);

				} catch (InterruptedException interruption) {

					interruption.printStackTrace();
					currentThread().interrupt();
				}

				frameEndTime = nanoTime();
			}
		}

		return frameEndTime;
	}
}
