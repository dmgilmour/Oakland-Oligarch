package game;

import java.util.Random;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author Dan
 *
 */
public class StatusPanel extends JPanel {
	
	private JButton[] playerButtons;
	private Player[] playerList;
	private int num_players;
	private JButton[] propertyButtons;	
	private ActionListener mortgageListener;

	/**
	 * Initializes the status panel showing players and their properties
	 *
	 * @param	playerList		the list of Players to be listed on this panel
	 */
	public StatusPanel(Random random, ActionListener ml) {
		this.setBackground(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
		this.setOpaque(true);
		this.setLayout(new GridBagLayout());
		mortgageListener = ml;
	}

	public void setPlayers(Player[] _playerList, ActionListener[] tradeListeners) {
		playerList = _playerList;
		num_players = playerList.length;


		GridBagConstraints c = new GridBagConstraints();

		c.gridx = 0;

		playerButtons = new JButton[num_players];

		for (int id = 0; id < num_players; id++) {
			playerButtons[id] = new JButton(playerList[id].getName() + ": $" + playerList[id].getMoney());	
			playerButtons[id].addActionListener(tradeListeners[id]);

			c.gridy = id;
			add(playerButtons[id], c);
		}
	}

	public void updateStatusProperties(ArrayList<Property> properties, ActionListener[] mortgageListeners) {

		GridBagConstraints c = new GridBagConstraints();
		c.gridy = this.getComponents().length;
		c.weighty = 0.05;

		if (propertyButtons != null) {
			for (JButton button : propertyButtons) {
				this.remove(button);
			}
		}

		propertyButtons = new JButton[properties.size()];

		for (int i = 0; i < properties.size(); i++) {

			Property prop = properties.get(i);
			propertyButtons[i] = new JButton();
			if (prop.getMortgaged()) {
				propertyButtons[i].setText("Buy back " + prop.getName() + " for $" + prop.getPrice());
			} else {
				propertyButtons[i].setText("Sell " + prop.getName() + " for $" + (prop.getPrice() / 2));
			}
			propertyButtons[i].addActionListener(mortgageListeners[i]);
			this.add(propertyButtons[i], c);
			c.gridy++;
		}
	}

		



	/*
	 * Updates the panel with the new money values of the players
	 */
	public void update(Player player) {

		// Update the Player buttons text
		for (int i = 0; i < num_players; i++) {	//Visit each status button and update the test to indicate player currency
			playerButtons[i].setText(playerList[i].getName() + ": $" + playerList[i].getMoney());
			if (player.getId() == i) {
				// This turn make bold
				playerButtons[i].setFont(playerButtons[i].getFont().deriveFont(Font.BOLD));
			}
		}

		// Remove the previous buttons
		Component[] buttonArr = this.getComponents();

		for (int i = num_players; i < buttonArr.length; i++) {
			this.remove(buttonArr[i]);
		}

		GridBagConstraints c = new GridBagConstraints();

		// Make new Mortgage/Unmortgage buttons
		int index = 0;
		for (Property prop : player.getProperties()) {
			JButton propButton = new JButton();
			if (prop.getMortgaged()) {
				propButton.setText(prop.getName() + ": buy back for $" + prop.getPrice());
			} else {
				propButton.setText(prop.getName() + ": mortgage for $" + prop.getPrice() / 2);
			}
			propButton.addActionListener(mortgageListener);
			propButton.setActionCommand(Integer.toString(index));
			c.gridy = index + num_players;
			this.add(propButton, c);
			index ++;
		}
			
	}
}
