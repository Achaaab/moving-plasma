package com.github.achaaab;

import javax.swing.JFrame;

import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.EAST;
import static javax.swing.UIManager.getSystemLookAndFeelClassName;
import static javax.swing.UIManager.setLookAndFeel;
import static javax.swing.WindowConstants.EXIT_ON_CLOSE;

class MovingPlasmaApplication {

	void main() {

		try {
			setLookAndFeel(getSystemLookAndFeelClassName());
		} catch (Exception exception) {
			exception.printStackTrace();
		}

		var model = new MovingPlasmaModel();
		var view = new MovingPlasmaView();
		var panel = new MovingPlasmaPanel();

		var frame = new JFrame("Moving plasma");
		frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.add(panel, EAST);
		frame.add(view, CENTER);
		frame.setSize(800, 600);
		frame.setVisible(true);

		var controller = new MovingPlasmaController(model, view, panel);
		controller.run();
	}
}
