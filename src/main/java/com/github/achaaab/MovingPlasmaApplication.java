package com.github.achaaab;

import javax.swing.JFrame;
import java.util.logging.Logger;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.EAST;
import static java.util.logging.Level.WARNING;
import static java.util.logging.Logger.getLogger;
import static javax.swing.UIManager.getSystemLookAndFeelClassName;
import static javax.swing.UIManager.setLookAndFeel;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

class MovingPlasmaApplication {

	private static final Logger LOGGER = getLogger(MovingPlasmaApplication.class.getName());

	void main() {

		try {
			setLookAndFeel(getSystemLookAndFeelClassName());
		} catch (Exception exception) {
			LOGGER.log(WARNING, "Unable to apply system look and feel.", exception);
		}

		var model = new MovingPlasmaModel();
		var view = new MovingPlasmaView();
		var panel = new MovingPlasmaPanel();

		var frame = new JFrame("Moving plasma");
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.add(panel, EAST);
		frame.add(view, CENTER);
		frame.setSize(1600, 900);
		frame.setVisible(true);

		var controller = new MovingPlasmaController(model, view, panel);
		controller.run();
	}
}
