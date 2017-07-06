package game;

import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author Dan Gilmour
 */
public class StatusPanel extends JPanel {
	
	private JButton[] playerButtons;
	private Player[] playerList;
	private int num_players;
	
	/**
	 * Initializes the status panel showing players and their properties
	 *
	 * @param	random		A psuedo-random number generator used to select the background color
	 */
	public StatusPanel(Random random) {
		this.setBackground(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
		this.setOpaque(true);
		this.setLayout(new GridBagLayout());
	}

	public void setPlayers(Player[] _playerList) {
		playerList = _playerList;
		num_players = playerList.length;


		GridBagConstraints c = new GridBagConstraints();

		c.weightx = 1;
		c.weighty = (1 / num_players);
		c.gridx = 0;

		playerButtons = new JButton[num_players];

		for (int id = 0; id < num_players; id++) {
			playerButtons[id] = new JButton(playerList[id].getName() + ": $" + playerList[id].getMoney());	
			ActionListener playerButtonListener = new PlayerButtonListener(playerList[id]);
			playerButtons[id].addActionListener(playerButtonListener);

			c.gridy = id;
			add(playerButtons[id], c);
		}
	}
	
	/**
	 * An ActionListener which will display the player information as a JPane when it is triggered
	 */
	private class PlayerButtonListener implements ActionListener {

		Player player;

		PlayerButtonListener(Player player) {
			this.player = player;
		}

		public void actionPerformed(ActionEvent e) {
			JFrame playerInfo = new JFrame(player.getName());
			playerInfo.setSize(400, 600);
			String dialog = "Money: $" + player.getMoney() + "\nProperties: ";
			for(Property property: player.getProperties())
				dialog += "\n" + property.getName();
			JOptionPane.showMessageDialog(playerInfo, dialog, player.getName(), JOptionPane.PLAIN_MESSAGE);	
		}
	}
	
	/**
	 * Updates the panel with the new money values of the players
	 */
	public void update() {

		for (int i = 0; i < num_players; i++) {
			playerButtons[i].setText(playerList[i].getName() + ": $" + playerList[i].getMoney());
		}
	}
}
