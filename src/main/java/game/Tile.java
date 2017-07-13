package game;

import java.awt.*;
import java.awt.event.*; 
import javax.swing.*;

/**
 * Class that is the base class for every Tile Object on the BoardPanel.
 * 
 * @author Eddie Hartman
 */
public class Tile extends JPanel{

		protected int number;
		protected Square square;
		protected JPanel players;
		protected JButton button;
		
		public Tile() {	}
		
		/**
		 * The constructor for tiles that make up the UI board
		 *
		 * @param	n		Tile number identifier so that the tile knows what number it is.
		 * @param	s		The square associated with this tile
		 */
		public Tile(int n, Square s){
			number = n;
			square = s;
			this.setLayout(new GridBagLayout());
			
			this.setBorder(BorderFactory.createLineBorder(Color.black));
			
			players = new JPanel();
			players.setLayout(new GridLayout(1, 4));
			
			button = new JButton(square.getName());
			button.setPreferredSize(new Dimension(60, 30));
			
			GridBagConstraints constraint = new GridBagConstraints();
			
			constraint.weightx = 0.1;
			constraint.weighty = 0.05;
			constraint.fill = GridBagConstraints.BOTH;
			
			constraint.gridx = 0;
			constraint.gridy = 0;
			
			this.add(button, constraint);
			
			constraint.gridy = 1;
			
			this.add(players, constraint);
			
		}
		
		public void add(Player player) {
			players.add(player.getToken());
		}
		
		public void remove(Player player) {
			players.remove(player.getToken());
		}

		public JButton getButton() {
			return button;
		}
}
