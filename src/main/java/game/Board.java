package game;

/**
 * @author Woodrow Fulmer
 */
public class Board {
	private Square[] squareList;
	
	/**
	 * The constructor for the game Board
	 *
	 * @param	_sl		an array of squares that will makeup this board
	 */
	public Board(Square[] _sl) {
		squareList = _sl;
	}
	
	public Square getSquare(int num) {
		if(num < OaklandOligarchy.NUMBER_OF_TILES)
			return squareList[num];
		return null;
	}
}
