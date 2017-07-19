package game;

import javax.swing.*;
/**
 * @author Woodrow Fulmer
 */
public class GoSquare extends Square {
	
	/**
	 * The constructor for a GoSquare
	 */
	public GoSquare() {
		name = "Go";
	}
	
	public boolean act(Player player) {
		player.getPaid(200);
		JOptionPane.showMessageDialog(null, "Earn an extra $200 for landing on Go!");
		return true;
	}
}