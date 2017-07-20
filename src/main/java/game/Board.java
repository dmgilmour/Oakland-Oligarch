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
	 */
	public Board(Square[] _sl) {
		squareList = _sl;
	}
	
	/**
	 * Retrieves the square at a given index
	 *
	 */
	public Square getSquare(int num) {
		if(num < OaklandOligarchy.NUMBER_OF_TILES)
			return squareList[num];
		return null;
	}
}
