package game;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 * @author Woodrow Fulmer
 */
public class Board {
	private Square[] squareList;
	
	/**
	 * The constructor for the game Board
	 *
	 * @param	_sl		An array of squares that will makeup this board
	 */
	public Board(Square[] _sl) {
		squareList = _sl;
	}
	
	/**
	 * Retrieves the square at a given index
	 *
	 * @param	num		The index to retrieve from		
	 * @return 			The square at the give index
	 */
	public Square getSquare(int num) {
		if(num < OaklandOligarchy.NUMBER_OF_TILES)
			return squareList[num];
		return null;
	}
}
