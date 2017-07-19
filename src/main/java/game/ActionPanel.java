package game;

import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author Dan Gilmour
 */
public class ActionPanel extends JPanel {

	public JButton tradeButton;
	public JButton buyButton;
	public JButton endButton;
	public JButton rollButton;
	public JButton payButton;

	/**
	 * The constructor of the ActionPanel UI element
	 *
	 * @param	random	A psuedo-random number generator used to select the background color
	 * @param	bl		An actionlistener for the buy button
	 * @param	ml		An actionlistener for the move button
	 * @param	el		An actionlistener for the end button
	 * @param	jl		An actionlistener to pay to get out of jail
	 */
	public ActionPanel(Random random, ActionListener bl, ActionListener ml, ActionListener el, ActionListener jl) {
		this.setBackground(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
		this.setOpaque(true);
		this.setLayout(new GridBagLayout());


		GridBagConstraints c = new GridBagConstraints();

		buyButton = new JButton("Buy");
		buyButton.addActionListener(bl);
		c.gridy = 0;
		c.weighty = 0.1;
		add(buyButton, c);
		

		endButton = new JButton("End");
		endButton.addActionListener(el);
		c.gridy = 1;
		c.weighty = 0.1;
		add(endButton, c);

		rollButton = new JButton("Roll The Dice Giant Button!");
		rollButton.addActionListener(ml);
		c.gridy = 2;
		c.weighty = 0.6;
		c.ipadx = 100;
		c.ipady = 40;
		add(rollButton, c);

		payButton = new JButton("Pay"); //the gridbagconstraints were not really checked here
		payButton.addActionListener(jl);
		c.gridy = 3;
		c.weighty = 0.1;
		payButton.setEnabled(false);
		payButton.setVisible(false);
		payButton.setToolTipText("Pay $" + OaklandOligarchy.JAIL_COST + " to get out of jail.");
		add(payButton, c);
	}

}
