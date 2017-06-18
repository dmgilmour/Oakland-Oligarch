package game;

import java.awt.*;
import java.awt.event.*; 
import javax.swing.*;

/**
 * Extends Tile to hold a property within the tile.
 * 
 * @author Eddie
 *
 */
public class PropertyTile extends Tile {
	
	Property property;
	
	/**
	 * @param n		Tile number identifier so that the tile knows what number it is.
	 * @param prop	The property object that is corresponding to this tile.
	 */
	public PropertyTile(int n, Property prop){
		number=n;
		property=prop;
		this.setLayout(new GridBagLayout());
		
		players = new JPanel();
		players.setLayout(new GridLayout(1, 4));
		
		JButton button = new JButton(prop.getPropertyName());
		button.setPreferredSize(new Dimension(40, 30));
		
		JPanel owner = new JPanel();
		owner.setBackground(Color.white);
		owner.setPreferredSize(new Dimension(20,30));
		
		GridBagConstraints constraint = new GridBagConstraints();
		
		constraint.weightx = 0.06; //This can be adjusted accordingly.
		constraint.weighty = 0.05;
		constraint.fill = GridBagConstraints.BOTH;
		
		constraint.gridx = 0;
		constraint.gridy = 0;
		
		this.add(button, constraint);
		
		constraint.weightx = 0.04;
		
		this.add(owner);
		
		constraint.weightx = 0.1;
		constraint.gridy = 1;
		
		this.add(players, constraint);
	}

}
