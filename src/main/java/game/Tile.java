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

		protected int number; // what number this tile is, identifier for the board
		protected Square square;
		
		protected JPanel players;
		
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
		
		public void update(Player player) {
			if(square.hasPlayer(player)) {
				players.add(player.getToken());
			}
			else {
				players.remove(player.getToken());
			}
		}
}
