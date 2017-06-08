import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class StatusPanel extends JPanel {
	
	public StatusPanel(Player[] playerList) {

		int num_players = playerList.length;

		Random rand = new Random(System.currentTimeMillis());
                setBackground(new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
                setOpaque(true);

		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.weightx = 1;
		c.weighty = (1 / num_players);
		c.gridx = 0;

		JButton[] playerButtons = new JButton[num_players];

		for (Player player : playerList) {
			int id = player.id;

			playerButtons[id] = new JButton(player.name + ": $" + player.money);	
			ActionListener playerButtonListener = new PlayerButtonListener(player);
			playerButtons[id].addActionListener(playerButtonListener);

			c.gridy = id;
			add(playerButtons[id], c);
		}
	}

	private class PlayerButtonListener implements ActionListener {

		Player player;

		PlayerButtonListener(Player player) {
			this.player = player;
		}

		public void actionPerformed(ActionEvent e) {
			JFrame playerInfo = new JFrame(player.name);
			playerInfo.setSize(400, 600);
			
			JOptionPane.showMessageDialog(playerInfo, "Money: $" + player.money + "\nProperties", player.name, JOptionPane.PLAIN_MESSAGE);	
		}
	}
}
