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
	public static final int NUMBER_OF_PROPERTIES = 36;
	public static final int MAX_NUMBER_OF_PLAYERS = 4;
	public static final int PLAYER_STARTING_MONEY = 200;

	private static Player[] playerList;
	private static Property[] propertyList;
	private static Game game;

	private static int num_players;

	public static void main(String[] args) {
		Random random = new Random(System.currentTimeMillis());
		propertyList = generateProperties();
		// Initialize the window to display basic screen when prompting
		// player information. Window and Game won't have any player info yet
		game = new Game(propertyList);
		Window window = new Window(propertyList, random);
		game.setWindow(window);

		// Prompt the number of players, then generate the playerlist
		// and prompt their names
		num_players = promptNumPlayers();
		playerList = generatePlayers(num_players);

		// Set the playerlists in Game and Window
		game.setPlayers(playerList);
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
			default:
				break;
		}
	}

	public static Property[] generateProperties() {
		Property[] properties = new Property[OaklandOligarchy.NUMBER_OF_PROPERTIES];
		for (int i = 0; i < OaklandOligarchy.NUMBER_OF_PROPERTIES; i++){
			properties[i] = new Property("Property "+i, i, i);
		}
		return properties;
	}

	private static int promptNumPlayers() {

		boolean valid_input = false;
		num_players = 0;

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

	public static Player[] generatePlayers(int num_players) {

		Player[] playerList = new Player[num_players];

		for (int i = 0; i < num_players; i++) {
			System.err.println(i);
			String playerName = promptName(i);
			playerList[i] = new Player(i, PLAYER_STARTING_MONEY, playerName, null);
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
}
