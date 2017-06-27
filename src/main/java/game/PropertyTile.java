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
	
	/**
	 * @param n		Tile number identifier so that the tile knows what number it is.
	 * @param prop	The property object that is corresponding to this tile.
	 */
	public PropertyTile(int n, Property prop){
		super(n, prop);
		
		JPanel owner = new JPanel();
		owner.setBackground(Color.white);
		owner.setPreferredSize(new Dimension(20,30));
		this.add(owner);
	}
}
