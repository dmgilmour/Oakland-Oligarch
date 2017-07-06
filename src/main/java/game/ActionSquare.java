package game;

/**
 * @author Woodrow Fulmer
 */
public class ActionSquare extends Square {
	
	/**
	 * The constructor for an ActionSquare
	 *
	 * @param	n		The name of this ActionSquare
	 */
	public ActionSquare(String n) {
		name = n;
	}
	
	public boolean act(Player player) {
		return true;
	}
}