package game;

/**
 * @author Woody
 *
 */
public class Board {
	private Square[] squareList;
	
	public Board(Square[] _sl) {
		squareList = _sl;
	}
	
	/**
	 * Changes the position of the player
	 *
	 * @param 	player		Player object that is the player to be moved
	 * @param	roll		Integer value of a roll that is how far the player should move
	 */
	public void movePlayer(Player player, int roll)
	{
		player.setPosition((player.getPosition() + roll) % OaklandOligarchy.NUMBER_OF_TILES);
	}
	
	public Square getSquare(int num) {
		if(num < OaklandOligarchy.NUMBER_OF_TILES)
			return squareList[num];
		return null;
	}
}