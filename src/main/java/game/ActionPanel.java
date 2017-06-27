package game;

import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author Dan
 *
 */
public class ActionPanel extends JPanel {

	public JButton tradeButton;
	public JButton buyButton;
	public JButton endButton;
	public JButton rollButton;

	
	public ActionPanel(Random random, ActionListener bl, ActionListener ml, ActionListener el) {
		this.setBackground(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
		this.setOpaque(true);
		this.setLayout(new GridBagLayout());


		GridBagConstraints c = new GridBagConstraints();

		tradeButton = new JButton("trade");
		c.gridy = 0;
		c.weighty = 0.1;
		add(tradeButton, c);

		buyButton = new JButton("Buy");
		buyButton.addActionListener(bl);
		c.gridy = 1;
		c.weighty = 0.1;
		add(buyButton, c);

		endButton = new JButton("End");
		endButton.addActionListener(el);
		c.gridy = 2;
		c.weighty = 0.1;
		add(endButton, c);

		rollButton = new JButton("Roll The Dice Giant Button!");
		rollButton.addActionListener(ml);
		c.gridy = 3;
		c.weighty = 0.6;
		c.ipadx = 100;
		c.ipady = 40;

		add(rollButton, c);
	}
}
