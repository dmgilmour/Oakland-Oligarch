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
	JPanel owner;
	
	/**
	 * @param n		Tile number identifier so that the tile knows what number it is.
	 * @param prop	The property object that is corresponding to this tile.
	 */
	public PropertyTile(int n, Property prop){
		super(n, prop);
		
		owner = new JPanel();
		owner.setBackground(Color.white);
		owner.setPreferredSize(new Dimension(20,30));
		this.add(owner);
	}
	
	public void update() {
		if(((Property)square).getOwner() != null) {
			owner.add(((Property)square).getOwnerToken(),0);
		}
	}
}
