package game;

import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author Dan
 *
 */
public class OaklandOligarchy {

	public enum GamePhase {MOVE, ACTION, END, START, BUY};

	public static final int NUMBER_OF_TILES = 36;
	public static final int NUMBER_OF_PROPERTIES = 28;
	public static final int MAX_NUMBER_OF_PLAYERS = 4;
	public static final int PLAYER_STARTING_MONEY = 200;
	public static final int NUMBER_OF_ACTIONS = 14;
	
	private static Game game;

	public static void main(String[] args) {
		Random random = new Random(System.currentTimeMillis());
		Square[] squareList = generateSquares();
		
		PhaseListener buyListener = new PhaseListener(GamePhase.BUY);
		PhaseListener moveListener = new PhaseListener(GamePhase.MOVE);
		PhaseListener endListener = new PhaseListener(GamePhase.END);
		Window window = new Window(squareList, random, buyListener, moveListener, endListener);
		
		int num_players = promptNumPlayers();
		Player[] playerList = generatePlayers(num_players);
	
		game = new Game(playerList, squareList, window, random);
		window.setPlayers(playerList);
		game.startPhase();
	}

	/**
	 * Changes which phase the game is in currently. Is called by various ActionListeners
	 *
	 * @param	gamePhase		Which phase the game should be set to
	 */
	public static void switchPhase(GamePhase gamePhase) {
		switch(gamePhase) {
			case MOVE:
				game.movePhase();
				break;
			case ACTION:
				game.actionPhase();
				break;
			case END:
				game.endPhase();
				break;
			case BUY:
				game.buyPhase();
				break;
			case START:
				game.startPhase();
				break;
			default:
				break;
		}
	}
	
	/**
	 *	Generates an array of Squares (properties and actions) that will act as the game board
	 */
	private static Square[] generateSquares() {
		Square[] squareList = new Square[OaklandOligarchy.NUMBER_OF_TILES];
		for (int i = 0; i < squareList.length; i++){
			if(i == 4 || i == 5 || i == 13 || i == 14 || i == 22 || i == 23 || i == 31 || i == 32) {
				squareList[i] = new ActionSquare("Action "+i);
			}
			else {
				squareList[i] = new Property("Property "+i, i, i);
			}
		}
		return squareList;
	}

	/**
	 *	Prompts the user using a JPane to input the number of players > 1 and < 5
	 */
	private static int promptNumPlayers() {

		boolean valid_input = false;
		int num_players = 0;

		while (!valid_input) {
			String numPlayers = JOptionPane.showInputDialog("Number of Players");
			if (numPlayers == null) System.exit(0);

			try {
				num_players = Integer.parseInt(numPlayers);
			} catch (NumberFormatException e) {
				continue;
			}

			if (num_players > 1 && num_players < 5) {
				valid_input = true;
			}
		}
		return num_players;
	}

	/**
	 * Creates an array of players with their starting money and names
	 *
	 * @param	num_players		The number of players in this game
	 */
	private static Player[] generatePlayers(int num_players) {

		Player[] playerList = new Player[num_players];

		for (int i = 0; i < num_players; i++) {
			System.err.println(i);
			String playerName = promptName(i);
			playerList[i] = new Player(i, PLAYER_STARTING_MONEY, playerName, null);
		}

		return playerList;
	}
	
	/**
	 * Prompts the user for their name via a JPane
	 *
	 * @param	playerID		the ID number of the player we are prompting for his/her name
	 */
	private static String promptName(int playerID) {
		String toReturn;
		toReturn = JOptionPane.showInputDialog("Input Name for Player " + (playerID + 1));
		if (toReturn == null) {
			System.exit(0);
		}
		return toReturn;
	}	
	
	/**
	 *	An ActionListener which upon being triggered, will move the game into the assigned phase.
	 */
	private static class PhaseListener implements ActionListener {
		GamePhase gamePhase;
		
		PhaseListener(GamePhase gp) {
			gamePhase = gp;
		}
		public void actionPerformed(ActionEvent e) {
			OaklandOligarchy.switchPhase(gamePhase);
		}
	}
}
