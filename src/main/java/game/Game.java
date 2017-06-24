package game;

import java.util.Random;
import javax.swing.*;

/**
 * @author Dan
 *
 */
public class Game {

	private static boolean rollTaken;
	private static int playerTurn;
	private static int num_players;

	public static  Board board;
	private static Property[] properties;
	private static Window window;
	private static Player[] playerList;
	
	public Game(Property[] propertyList) {
		properties = propertyList;
		board = new Board(properties);

		playerTurn = 0;
		rollTaken = false;
	}

	public int getTurn() {
		return playerTurn;
	}
	
	public void setWindow(Window _window) {
		window = _window;
	}

	public void setPlayers(Player[] _playerList) {
		playerList = _playerList;
		num_players = playerList.length;
		board.setPlayers(playerList);
	}


	/**
	 * Runs the game phase during which players roll and move
	 */
	public static void movePhase() {
		int roll = roll(System.currentTimeMillis());		
		board.movePlayer(playerList[playerTurn], roll);
		window.update(playerList[playerTurn]);
		actionPhase();
		endPhase();
	}
	
	/**
	 * Runs the game phase that ends each players turn
	 */
	public static void endPhase() {
		playerTurn = (playerTurn + 1) % num_players;	//Increment to the next player's turn
		rollTaken = false;								//Enable the "roll" button again
		window.update(playerList[playerTurn]);
	}
	
	/**
	 *	A psuedo-random roll that simulates 2 six-sided dice
	 *
	 * @param	timeMillis		A long integer used to the seed the random roll
	 * @returns					An integer value between 2-12 that is the result of rolling 2 six-sided dice
	 */
	public static int roll(Long timeMillis) {
		if(!rollTaken)
		{
			Random rand = new Random(timeMillis);
			rollTaken = true;
			int roll = rand.nextInt(6) + rand.nextInt(6) + 2;	//Simulates two dice rolls by retriving integers from 0-5 and adding 2
			return roll;
		}
		else
			return -1;
	}
	
	/**
	 * Runs the game phase where the player performs an action based on the tile they are on
	 */
	public static void actionPhase() {
		Player player = playerList[playerTurn];
		Square square = board.getSquare(player.getPosition());
		if(square == null) {									//Check to ensure that a tile was retrived properly from the board
			return;
		}
		else {												//If the tile retrived is a property:
			square.act(player);			
		}
		window.update(playerList[playerTurn]);
	}
}
