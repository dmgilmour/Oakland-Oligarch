package game;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

/**
 * @author Eddie
 *
 */
public class JailTile extends Tile {
	JPanel prison;
	
	/**
	 * A tile to represent the Jail
	 *
	 * @param	n	The name used for this tile
	 * @param	s	The JailSquare associated with this tile
	 */
	public JailTile(int n, Square s){
		super(n, s);
		button.setToolTipText("Your mouse is currently in jail");
		
		prison = new JPanel();
		prison.setBackground(Color.white);
		this.add(prison);
		
	}
	
	/**
	 * Updates the jail and puts people in if they belong there.
	 */
	public void update(){
		for(Player p : OaklandOligarchy.playerList){
			if(p.isInJail()){
				prison.add(p.getToken());
			}
			else{
				prison.remove(p.getToken());
				if(p.getPosition()==OaklandOligarchy.JAIL_POS){
					this.add(p.getToken());
				}
			}
		}
	}
}
