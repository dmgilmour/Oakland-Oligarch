package game;

/**
 * The base class for all squares. Squares are interactable locations within the game board.
 *
 * @author Woodrow Fulmer
 */
public abstract class Square{
	protected String name;
	
	public String getName() {
		return name;
	}
	
	public abstract boolean act(Player player);
}