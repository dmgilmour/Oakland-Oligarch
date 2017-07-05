package game;

import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author Dan
 *
 */

public class PropertyInfoPanel extends JPanel {

	Property property;
	
	public PropertyInfoPanel(Property _property) {

		property = _property;
		
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();

		JLabel propertyName = new JLabel(property.getName());
		c.gridx = 0;
		c.ipadx = 10;
		c.ipady = 5;
		this.add(propertyName, c);

		JButton tradeButton = new JButton("Trade");
		c.gridx = 1;
		c.ipadx = 5;
		c.ipady = 5;
		this.add(tradeButton, c);

		JButton sellButton = new JButton("Sell");
		sellButton.addActionListener(new sellListener());
		c.gridx = 2;
		c.ipadx = 5;
		c.ipady = 5;
		this.add(sellButton, c);

	}

	public class sellListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// Game.sellProperty((JButton)(e.getSource()).getParent().property);
		}
	}
}
		

		
		
