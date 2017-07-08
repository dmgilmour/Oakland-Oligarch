package game;

import java.util.Random;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author Eddie Hartman
 * @author Woodrow Fulmer
 * @author David Haskell
 * @author Dan Gilmour
 */
public class OaklandOligarchy {
	
	public enum GamePhase {MOVE, ACTION, END, START, BUY, TRADE};
	
	public static final int NUMBER_OF_TILES = 36;
	public static final int NUMBER_OF_PROPERTIES = 28;
	public static final int MAX_NUMBER_OF_PLAYERS = 4;
	public static final int PLAYER_STARTING_MONEY = 200;
	public static final int NUMBER_OF_ACTIONS = 14;
	
	private static Game game;
	private static Window window;
	private static Square[] squareList;

	public static void main(String[] args) {
		Random random = new Random(System.currentTimeMillis());
		squareList = generateSquares();
		
		PhaseListener buyListener = new PhaseListener(GamePhase.BUY, null);
		PhaseListener moveListener = new PhaseListener(GamePhase.MOVE, null);
		PhaseListener endListener = new PhaseListener(GamePhase.END, null);
		window = new Window(squareList, random, buyListener, moveListener, endListener, new MortgageListener(), new PropertyListener());
		
		int num_players = promptNumPlayers();
		Player[] playerList = generatePlayers(num_players);
	
		game = new Game(playerList, squareList, window, random);
		PhaseListener[] tradeListeners = new PhaseListener[num_players];
		for (int i = 0; i < num_players; i++) {
			tradeListeners[i] = new PhaseListener(GamePhase.TRADE, playerList[i]);
		}
		window.setPlayers(playerList, tradeListeners);
		game.startPhase();
	}

	/**
	 * Changes which phase the game is in currently. Is called by various ActionListeners
	 *
	 * @param	gamePhase		Which phase the game should be set to
	 */
	public static void switchPhase(GamePhase gamePhase, Player player) {
		switch(gamePhase) {
			case START:
				game.startPhase();
				break;
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
			case TRADE:
				game.tradePhase(player);
				break;
			default:
				break;
		}
	}
	
	/**
	 * Generates an array of Squares (properties and actions) that will act as the game board
	 *
	 * @return					the array of squares to be used as a game board
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
	 * Prompts the user using a JPane to input the number of players > 1 and < 5
	 *
	 * @return			the integer number of players for this game 
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
	 * @return					The array of players in this game
	 */
	private static Player[] generatePlayers(int num_players) {

		Player[] playerList = new Player[num_players];

		for (int i = 0; i < num_players; i++) {
			String playerName = promptName(i);
			playerList[i] = new Player(i, PLAYER_STARTING_MONEY, playerName, null);
			switch(i) {
				case 0:
					playerList[i].setColor(0xFFA9A9);
					;
					break;
				case 1:
					playerList[i].setColor(0xA9BDFF);
					break;
				case 2:
					playerList[i].setColor(0xFAFFA9);
					break;
				case 3:
					playerList[i].setColor(0xA9FFB5);
					break;
			}
		}

		return playerList;
	}
	
	/**
	 * Prompts the user for their name via a JPane
	 *
	 * @param	playerID		the ID number of the player we are prompting for his/her name
	 * @return					the String defining the player's name
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
	 * Creates the method used by mortgage listeners that will toggle
	 * whether a property is mortgaged or not
	 *
	 * @param	propIndex	The index of the property to toggle
	 */
	private static void toggleMortgage(int propIndex) {
		Player player = game.getCurrentPlayer();
		Property prop = player.getProperty(propIndex);
		if (prop.getMortgaged()) {
			game.unmortgage(prop);
		} else {
			game.mortgage(prop);
		}
		window.update(player);
	}

	private static void displayProperty(int propIndex) {
		Property prop = (Property) squareList[propIndex];
		String message = prop.getName();
		if (prop.getOwner() == null) {
			message += "\nUnowned";
		} else {
			message += "\nOwned by: " + prop.getOwner().getName();
		}
		message += "\nPrice to buy: $" + prop.getPrice();
		message += "\nRent: $" + prop.getRent();
		JOptionPane.showMessageDialog(null, message);
	}


	/**
	 * Creates actionlisteners for the status panel 
	 *
	 * @param	player		the player for which we should display their properties
	 */
	
	/**
	 *	An ActionListener which upon being triggered, will move the game into the assigned phase.
	 */
	private static class PhaseListener implements ActionListener {
		GamePhase gamePhase;
		Player player;
		
		PhaseListener(GamePhase gp, Player optionalPlayer) {
			gamePhase = gp;
			player = optionalPlayer;
		}
		public void actionPerformed(ActionEvent e) {
			OaklandOligarchy.switchPhase(gamePhase, player);
		}
	}


	private static class PropertyListener implements ActionListener {
		
		public void actionPerformed(ActionEvent e) {
			int propertyIndex = Integer.parseInt(e.getActionCommand());
			displayProperty(propertyIndex);
		}
	}

	/**
	 * Creates mortgagelisteners for each property 
	 *
	 * @param	prop		the property the actionlistener is trying to control
	 */
	private static class MortgageListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			int propertyIndex = Integer.parseInt(e.getActionCommand());
			toggleMortgage(propertyIndex);
		}
	}
}
