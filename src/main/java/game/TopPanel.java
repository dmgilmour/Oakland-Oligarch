package game;

import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TopPanel extends JPanel {
	
	public TopPanel() {
		Random rand = new Random(System.currentTimeMillis());
		setBackground(new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
		setOpaque(true);
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.weightx = 0.25;

		constraints.gridx = 0;
		JButton saveButton = new JButton("Save Game");
		add(saveButton, constraints);

		constraints.gridx = 1;
		JButton loadButton = new JButton("Load Game");
		add(loadButton, constraints);

		constraints.gridx = 2;
		JButton optionsButton = new JButton("Options");
		add(optionsButton, constraints);

		constraints.gridx = 3;
		JButton instructionsButton = new JButton("Instructions");
		add(instructionsButton, constraints);

	}
}
