package com.github.achaaab;

import static java.lang.Math.round;
import static java.lang.System.nanoTime;
import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

/**
 * Moving plasma controller.
 *
 * @author Jonathan Guéhenneux
 * @since 0.0.0
 */
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

		panel.factorX().setValue(model.getxFactor());
		panel.factorY().setValue(model.getFactorY());
		panel.colorFactor().setValue(model.getColorFactor());
		panel.colorOffset().setValue(model.getColorOffset());
		panel.timeFactor().setValue(model.getTimeFactor());
	}

	public void update(double deltaTime) {

		model.setxFactor(panel.factorX().getValue());
		model.setFactorY(panel.factorY().getValue());
		model.setColorFactor(panel.colorFactor().getValue());
		model.setColorOffset(panel.colorOffset().getValue());
		model.setTimeFactor(panel.timeFactor().getValue());

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

			frameEndTime = ensureFrameDuration(frameStartTime, nanoTime());
		}
	}

	/**
	 * Ensures that the frame meets the target duration by adding a delay if needed.
	 * <p>
	 * This method compares the actual frame duration with the target frame duration.
	 * If the frame is shorter, the current thread is put to sleep to match the target
	 * timing, and the adjusted end time is returned.
	 * </p>
	 *
	 * @param frameStartTime timestamp (in nanoseconds) when the frame started
	 * @param frameEndTime timestamp (in nanoseconds) when the frame ended
	 * @return adjusted frame end time (in nanoseconds) after applying the delay,
	 * or the original {@code frameEndTime} if no delay was required
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
					currentThread().interrupt();
				}

				frameEndTime = nanoTime();
			}
		}

		return frameEndTime;
	}
}
