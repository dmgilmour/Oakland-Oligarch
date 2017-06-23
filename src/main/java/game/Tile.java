package game;

import java.awt.*;
import java.awt.event.*; 
import javax.swing.*;

/**
 * Class that is the base class for every Tile Object on the board.
 * 
 * @author Eddie
 *
 */
public class Tile extends JPanel{

		int number; // what number this tile is, identifier for the board
		
		JPanel players;
		
		public Tile(){
			
		}
		
		/**
		 * @param n	Tile number identifier so that the tile knows what number it is.
		 */
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
		
		/**
		 * Put a player onto this tile. Takes an object that represents the player token.
		 * @param p	The player to put onto this tile.
		 */
		public void addPlayer(JLabel p){
			players.add(p);
		}
		
		/**
		 * Remove a player from this tile. Takes an object that represents the player token.
		 * @param p	The player to remove from this tile.
		 */
		public void removePlayer(JLabel p){
			players.remove(p);
		}
}
