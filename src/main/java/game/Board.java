package game;

import java.awt.*;
import java.awt.event.*; 
import javax.swing.*;

/**
 * @author Dan
 *
 */
public class Board extends JPanel {

	Tile[] tiles;
	
	static final int NUMBER_OF_TILES=36; // currently actually correlates to number of tiles AND number of properties
	
	/**
	 * Constructor to build the board object.
	 * 
	 * @param playerList	Array of players to be added to the game.
	 * @param properties	Array of properties to add to the game.
	 */
	public Board(Player[] playerList, Property[] properties){
		//The buttons are just place holders for tiles right now. The layout itself is complicated 
		//current button size is 60X60 just for easy math
		this.setLayout(new GridBagLayout());
		tiles = new Tile[NUMBER_OF_TILES];
		
		for(int i = 0; i < NUMBER_OF_TILES; i++){ //property numbers are currently hard coded
			tiles[i] = new PropertyTile(i, properties[i]);
			//associate action listeners here
			tiles[i].setPreferredSize(new Dimension(60, 60));
		}
		
		for(Player p: playerList){ //adds players to the game
			tiles[0].addPlayer(p.getToken());
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
	 * Changes the position of the player both logically and on the UI
	 *
	 * @param 	player		Player object that is the player to be moved
	 * @param	roll		Integer value of a roll that is how far the player should move
	 */
	public void movePlayer(Player player, int roll)
	{
		int position = player.getPosition();
		tiles[position].removePlayer(player.getToken());		//Remove the player from the tile
		position = (roll + position) % NUMBER_OF_TILES;			//Increment the player position
		player.setPosition(position);							//Set the new player position
		tiles[position].addPlayer(player.getToken());			//Add the player to the tile at the new position
	}
	
	
	/**
	 * Retrival function to get a particular tile from the board
	 * 
	 * @param 	num			The integer value identifying which tile to get
	 * @return				The Tile in the board at location num
	 */
	public Tile getTile(int num) {
		if(num < NUMBER_OF_TILES)
			return tiles[num];
		return null;
	}
}
