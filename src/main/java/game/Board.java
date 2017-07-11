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
	 * @param	_sl		an array of squares that will makeup this board
	 */
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
	
	public void save(BufferedWriter bw) throws IOException{
		bw.newLine();
		for(int i = 0; i < squareList.length; i++) {
			if(squareList[i] instanceof Property) {
				Property p = (Property) squareList[i];
				bw.write(i + "\t");
				bw.write(p.getName());
				bw.write("\t\t\t" + p.getPrice());
				bw.write("\t" + p.getRent());
				if(p.getOwner() == null) {
					bw.write("\t-1");
				}
				else {
					bw.write("\t" + p.getOwner().getId());
				}
				if(p.getMortgaged()) {
					bw.write("\tm");
				}
				else {
					bw.write("\tu");
				}
				bw.newLine();
			}
		}
	}
}