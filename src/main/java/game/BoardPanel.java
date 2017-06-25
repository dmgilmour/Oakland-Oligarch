package game;

import java.awt.*;
import java.awt.event.*; 
import javax.swing.*;

/**
 * @author Dan
 *
 */
public class BoardPanel extends JPanel {

	private Tile[] tiles;
	
	/**
	 * Constructor to build the board object.
	 * 
	 * @param playerList	Array of players to be added to the game.
	 * @param properties	Array of properties to add to the game.
	 */
	public BoardPanel(Property[] properties){
		//The buttons are just place holders for tiles right now. The layout itself is complicated 
		//current button size is 60X60 just for easy math
		this.setLayout(new GridBagLayout());
		tiles = new Tile[OaklandOligarchy.NUMBER_OF_TILES];
		
		for(int i = 0; i < OaklandOligarchy.NUMBER_OF_TILES; i++){ //property numbers are currently hard coded
			tiles[i] = new PropertyTile(i, properties[i]);
			//associate action listeners here
			tiles[i].setPreferredSize(new Dimension(60, 60));
		}
		

		GridBagConstraints constraint = new GridBagConstraints();

		/*
		JButton centerSquare = new JButton("Center");
		constraint.gridx = 1;
		constraint.gridy = 1;
		constraint.gridheight = 8;
		constraint.gridwidth = 8;
		constraint.weightx = 0.8;
		constraint.weighty = 0.8;
		add(centerSquare, constraint);
		*/
		
		//r = button ROW
		//c = button COLLUMN
		//i = button number

		int i=0;
		constraint.weightx = 0.1;
		constraint.weighty = 0.1;
		constraint.fill = GridBagConstraints.BOTH;
		
		//Fills the buttons in one side at a time
		for(int t = 0; t < 10; t++){ //top loop
			constraint.gridx = t;
			constraint.gridy = 0;
			this.add(tiles[i],constraint);
			i++;
		}
		for(int r = 1; r < 10; r++){ //right loop
			constraint.gridx = 9;
			constraint.gridy = r;
			this.add(tiles[i],constraint);
			i++;
		}
		for(int b = 8; b >= 0; b--){ //bottom loop
			constraint.gridx = b;
			constraint.gridy = 9;
			this.add(tiles[i],constraint);
			i++;
		}
		for(int l = 8; l > 0; l--){ //left loop
			constraint.gridx = 0;
			constraint.gridy = l;
			this.add(tiles[i],constraint);
			i++;
		}
	}	
	
	/**
	 * Retrival function to get a particular tile from the board
	 * 
	 * @param 	num			The integer value identifying which tile to get
	 * @return				The Tile in the board at location num
	 */
	public Tile getTile(int num) {
		if(num < OaklandOligarchy.NUMBER_OF_TILES)
			return tiles[num];
		return null;
	}
	
	public void update(Player p) {
		for(Tile t: tiles) {
			t.update(p);
		}
	}
}
