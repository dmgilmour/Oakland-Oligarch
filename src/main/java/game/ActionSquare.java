package game;

/**
 * @author Woodrow Fulmer
 */
public class ActionSquare extends Square {
	
	public ActionSquare(String n) {
		name = n;
	}
	
	public boolean act(Player player) {
		return true;
	}
}