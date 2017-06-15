package game;

import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ActionPanel extends JPanel {
	
	public ActionPanel() {
		Random rand = new Random(System.currentTimeMillis());
		setBackground(new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
		setOpaque(true);
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		JButton tradeButton = new JButton("trade");
		c.gridy = 0;
		c.weighty = 0.1;
		add(tradeButton, c);



		JButton rollButton = new JButton("Roll The Dice Giant Button!");
		rollButton.addActionListener(new rollListener());
		c.gridy = 1;
		c.weighty = 0.6;
		c.ipadx = 100;
		c.ipady = 40;

		add(rollButton, c);
	}
	
	private class rollListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			OaklandOligarchy.movePhase();
		}
	}
}
