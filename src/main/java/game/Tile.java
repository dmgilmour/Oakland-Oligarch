package game;

import java.awt.*;
import java.awt.event.*; 
import javax.swing.*;

public class Tile extends JPanel{

		int number; // what number this tile is, identifier for the board
		
		JPanel players;
		
		public Tile(int n){
			number=n;
			this.setLayout(new GridBagLayout());
			
			this.setBorder(BorderFactory.createLineBorder(Color.black));
			
			players = new JPanel();
			players.setLayout(new GridLayout(1, 4));
			
			JButton button = new JButton("Property " + n);
			button.setPreferredSize(new Dimension(60, 30));
			
			GridBagConstraints constraint = new GridBagConstraints();
			
			constraint.weightx = 0.1; //This can be adjusted accordingly.
			constraint.weighty = 0.05;
			constraint.fill = GridBagConstraints.BOTH;
			
			constraint.gridx = 0;
			constraint.gridy = 0;
			
			this.add(button, constraint);
			
			constraint.gridy = 1;
			
			this.add(players, constraint);
			
		}
		
		//Put a player onto this tile. Takes an object that represents the player token.
		public void addPlayer(JLabel p){
			players.add(p);
		}
		
		//Remove a player from this tile. Takes an object that represents the player token.
		public void removePlayer(JLabel p){
			players.remove(p);
		}
}
