import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class StatusPanel extends JPanel {
	
	public StatusPanel() {
		Random rand = new Random(System.currentTimeMillis());
                setBackground(new Color(rand.nextFloat(), rand.nextFloat(), rand.nextFloat()));
                setOpaque(true);

		final int numPlayers = 4;

		setBackground(Color.BLACK);
		setOpaque(true);
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		c.weightx = 1;
		c.weighty = (1 / numPlayers);
		c.gridx = 0;

		JButton[] playerButtons = new JButton[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			playerButtons[i] = new JButton("Player " + (i + 1));	
			c.gridy = i;
			add(playerButtons[i], c);
		}
	}
}
