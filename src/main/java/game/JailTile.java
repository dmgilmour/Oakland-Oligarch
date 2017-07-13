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
	
	public JailTile(int n, Square s){
		super(n, s);
		
		prison = new JPanel();
		prison.setBackground(Color.BLACK);
		prison.setPreferredSize(new Dimension(20,30));
		this.add(prison);
		
	}
	
	/**
	 * Updates the jail and puts people in if they belong there.
	 */
	public void update(){
		for(Player p : ((JailSquare)square).getPrisoners()){
			if(p.isInJail()){
				prison.add(p.getToken());
			}
		}
	}
}
