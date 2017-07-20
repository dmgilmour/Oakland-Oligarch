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
	
	/**
	 * A player who lands on Go recieves an additional $200,
	 *		plus the $200 recieved for passing Go.
	 *
	 * @param	player	The player who landed on Go
	 * @return			TRUE. Go cannot be bought
	 */
	public boolean act(Player player) {
		player.getPaid(200);
		JOptionPane.showMessageDialog(null, "Earn an extra $200 when you land on Go!");
		return true;
	}
}