package com.github.achaaab;

import javax.swing.JPanel;
import javax.swing.JSlider;
import java.awt.GridLayout;

public class MovingPlasmaPanel extends JPanel {

	private final JSlider factorX;
	private final JSlider factorY;
	private final JSlider colorFactor;
	private final JSlider colorOffset;
	private final JSlider timeFactor;

	public MovingPlasmaPanel() {

		setLayout(new GridLayout(5, 1));

		factorX = new JSlider(1, 100);
		factorY = new JSlider(1, 100);
		colorFactor = new JSlider(1, 1000);
		colorOffset = new JSlider(-1000, 1000);
		timeFactor = new JSlider(1, 1000);

		add(factorX);
		add(factorY);
		add(colorFactor);
		add(colorOffset);
		add(timeFactor);
	}

	public JSlider factorX() {
		return factorX;
	}

	public JSlider factorY() {
		return factorY;
	}

	public JSlider colorFactor() {
		return colorFactor;
	}

	public JSlider colorOffset() {
		return colorOffset;
	}

	public JSlider timeFactor() {
		return timeFactor;
	}
}
