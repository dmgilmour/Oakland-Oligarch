package game;

import java.util.Random;
import java.awt.*;
import java.awt.event.*; 
import javax.swing.*;

public class OaklandOligarchy {

	static Player[] playerList;
	private static boolean rollTaken;
	private static int playerTurn;
	private static int num_players;

	public static void main(String[] args) {
		
		// Get input for number of players
		num_players = promptNumPlayers();
		playerList = generatePlayers(num_players);
		promptPlayerNames(playerList);
		playerTurn = 0;
		rollTaken = false;
		Game game = new Game(playerList);

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

			if (num_players > 1 && num_players < 7) {
				valid_input = true;
			}
		}
		return num_players;
	}

	public static Player[] generatePlayers(int num_players) {

		Player[] playerList = new Player[num_players];

		for (int i = 0; i < num_players; i++) {
			playerList[i] = new Player(i, 2000, null, null);
		}

		return playerList;
	}


	private static void promptPlayerNames(Player[] playerList) {
		for (Player player : playerList) {
			player.name = JOptionPane.showInputDialog("Input Name for Player " + (player.id + 1));	
			if (player.name == null) {
				System.exit(0);
			}
		}
	}

	public static void movePhase() {
		rollTaken = true;
		Random rand = new Random(System.currentTimeMillis());
		int roll = rand.nextInt(5) + rand.nextInt(5) + 2;
		/***	Insert the moving portion of the turn 
				aka. the part having to do with the interface ***/
		System.out.println(playerList[playerTurn].name + " rolled a " + roll);	
		
		
		// These two lines need to go at the end of each turn, wherever that may be
		playerTurn = (playerTurn + 1) % num_players;
		rollTaken = false;
	}
}
