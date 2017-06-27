package game;

import java.util.Random;
import javax.swing.*;

/**
 * @author Dan
 *
 */
public class Game {

	private boolean rollTaken;
	private int playerTurn;

	private Board board;
	private Window window;
	private Player[] playerList;
	
	/**
	 * Initializes the Game object
	 *
	 * @param	_playerList		The array of players in this game
	 * @param	squareList		The array of squares to be used in this game
	 * @param	w				The window this game is running in
	 */
	public Game(Player[] _playerList, Square[] squareList, Window w) {
		playerList = _playerList;
		board = new Board(squareList);
		window = w;
		playerTurn = 0;
		rollTaken = false;
	}

	private Player getCurrentPlayer() {
		return playerList[playerTurn];
	}

	/**
	 * Runs the game phase for the start of a turn during which a player can click info
	 * buttons and roll the dice giant button
	 */
	public void startPhase() {
		rollTaken = false;			
		window.disableEnd();
		window.disableBuy();
		window.enableRoll();
		window.update(this.getCurrentPlayer());
	}


	/**
	 * Runs the game phase during which players roll and move
	 */
	public void movePhase() {
		int roll = roll(System.currentTimeMillis());		
		board.movePlayer(playerList[playerTurn], roll);
		window.update(this.getCurrentPlayer());
		window.disableRoll();
		actionPhase();
	}
	
	/**
	 * Runs the game phase that ends each players turn
	 */
	public void endPhase() {
		playerTurn = (playerTurn + 1) % playerList.length;	//Increment to the next player's turn
		startPhase();
	}
	
	/**
	 *	A psuedo-random roll that simulates 2 six-sided dice
	 *
	 * @param	timeMillis		A long integer used to the seed the random roll
	 * @returns					An integer value between 2-12 that is the result of rolling 2 six-sided dice
	 */
	private int roll(Long timeMillis) {
		if(!rollTaken) {
			Random rand = new Random(timeMillis);
			rollTaken = true;
			int roll = rand.nextInt(6) + rand.nextInt(6) + 2;	//Simulates two dice rolls by retriving integers from 0-5 and adding 2
			return roll;
		}
		else {
			return -1;
		}
	}

	/**
	 * Runs the game phase where the player performs an action based on the tile they are on
	 */
	public void actionPhase() {
		Player player = playerList[playerTurn];
		Square square = board.getSquare(player.getPosition());
		if(square == null) {									//Check to ensure that a tile was retrived properly from the board
			return;
		}
		else {												//If the tile retrived is a property:
			boolean cannotBuy = square.act(player);
			if(!cannotBuy)
			{
				window.enableBuy();
			}
		}
		window.enableEnd();
		window.update(this.getCurrentPlayer());
	}

	/**
	 * Runs the game phase where the player can purchase a property
	 */
	public void buyPhase() {
		Player player = playerList[playerTurn]; 
		Square square = board.getSquare(player.getPosition());
		if(square.act(player))
		{
			window.disableBuy();
		}
		window.update(this.getCurrentPlayer());
	}
}
