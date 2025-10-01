package com.github.achaaab;

import javax.swing.JPanel;
import javax.swing.JSlider;
import java.awt.GridLayout;

public class MovingPlasmaPanel extends JPanel {

	private final JSlider a;
	private final JSlider b;
	private final JSlider c;
	private final JSlider d;
	private final JSlider e;

	public MovingPlasmaPanel() {

		setLayout(new GridLayout(5, 1));

		a = new JSlider(1, 100);
		b = new JSlider(1, 100);
		c = new JSlider(1, 1000);
		d = new JSlider(-1000, 1000);
		e = new JSlider(1, 1000);

		add(a);
		add(b);
		add(c);
		add(d);
		add(e);
	}

	public JSlider a() {
		return a;
	}

	public JSlider b() {
		return b;
	}

	public JSlider c() {
		return c;
	}

	public JSlider d() {
		return d;
	}

	public JSlider e() {
		return e;
	}
}
