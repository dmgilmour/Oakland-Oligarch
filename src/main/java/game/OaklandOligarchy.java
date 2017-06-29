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
	public static final int NUMBER_OF_PROPERTIES = 33;
	public static final int MAX_NUMBER_OF_PLAYERS = 4;
	public static final int NUMBER_OF_ACTIONS = 1;
	
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
	
	private static Square[] generateSquares() {
		Square[] squareList = new Square[OaklandOligarchy.NUMBER_OF_TILES];
		for (int i = 0; i < squareList.length; i++){
			if(i > 5 && i < 9) {
				squareList[i] = new ActionSquare("Action "+i);
			}
			else {
				squareList[i] = new Property("Property "+i, i, i);
			}
		}
		return squareList;
	}

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

	private static Player[] generatePlayers(int num_players) {

		Player[] playerList = new Player[num_players];

		for (int i = 0; i < num_players; i++) {
			System.err.println(i);
			String playerName = promptName(i);
			playerList[i] = new Player(i, 2000, playerName, null);
		}

		return playerList;
	}

	private static String promptName(int playerID) {
		String toReturn;
		toReturn = JOptionPane.showInputDialog("Input Name for Player " + (playerID + 1));	
		if (toReturn == null) {
			System.exit(0);
		}
		return toReturn;
	}	
	
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
