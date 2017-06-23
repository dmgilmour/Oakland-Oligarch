package game;


import java.awt.*;
import java.awt.event.*; 
import javax.swing.*;

/**
 * @author Dan
 *
 */
public class OaklandOligarchy {

	static Player[] playerList;
	
	private static int num_players;

	public static void main(String[] args) {
		
		// Initialize the window to display basic screen when prompting
		// player information. Window and Game won't have any player info yet
		Game game = new Game();
		Window window = new Window(game);
		game.setWindow(window);

		// Prompt the number of players, then generate the playerlist
		// and prompt their names
		num_players = promptNumPlayers();
		playerList = generatePlayers(num_players);

		// Set the playerlists in Game and Window
		game.setPlayers(playerList);
		window.setPlayers(playerList);

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
}
