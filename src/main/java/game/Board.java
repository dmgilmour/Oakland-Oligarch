package game;

/**
 * @author Woody
 *
 */
public class Board {
	Square[] squareList;
	
	public Board(Square[] _sl) {
		squareList = _sl;
	}
	
	public void setPlayers(Player[] playerList) {
		for(Player p: playerList) {
			squareList[0].add(p);
		}
	}
	
	/**
	 * Changes the position of the player
	 *
	 * @param 	player		Player object that is the player to be moved
	 * @param	roll		Integer value of a roll that is how far the player should move
	 */
	public void movePlayer(Player player, int roll)
	{
		int oldPosition = player.getPosition();
		int newPosition = (oldPosition + roll) % OaklandOligarchy.NUMBER_OF_TILES;
		squareList[oldPosition].remove(player);
		squareList[newPosition].add(player);
		player.setPosition(newPosition);
	}
	
	/**
	 * Retrival function to get a particular tile from the board
	 * 
	 * @param 	num			The integer value identifying which square to get
	 * @return				The Square in the board at location num
	 */
	public Square getSquare(int num) {
		if(num < OaklandOligarchy.NUMBER_OF_TILES)
			return squareList[num];
		return null;
	}
}