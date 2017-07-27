package game;

import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author Eddie Hartman
 * @author Woodrow Fulmer
 * @author David Haskell
 * @author Dan Gilmour
 */
public class OaklandOligarchy {

	public enum GamePhase {MOVE, ACTION, END, START, BUY, TRADE};

	public static final int NUMBER_OF_TILES = 36;
	public static final int MAX_NUMBER_OF_PLAYERS = 4;
	public static final int NUMBER_OF_ACTIONS = 14;
	public static final int JAIL_COST = 50;
	public static int JAIL_POS;
	public static int GO_PAYOUT;

	public static Player[] playerList;
	protected static Game game;
	private static Window window;
	private static Square[] squareList;
	private static Random random;
	private static Time time;
	private static FileHandler fh;

	private static PhaseListener buyListener;
	private static PhaseListener moveListener;
	private static PhaseListener endListener;
	private static LoadListener loadListener;
	private static SaveListener saveListener;
	private static PayListener payListener;
	private static MortgageListener mortgageListener;
	private static PropertyListener propertyListener;

	public static void main(String[] args) {
		random = new Random(System.currentTimeMillis());
		fh = new FileHandler();
		squareList = fh.getBoard();
		time = fh.getTime();

		buyListener = new PhaseListener(GamePhase.BUY, null);
		moveListener = new PhaseListener(GamePhase.MOVE, null);
		endListener = new PhaseListener(GamePhase.END, null);
		loadListener = new LoadListener();
		saveListener = new SaveListener();
		payListener = new PayListener();
		mortgageListener = new MortgageListener();
		propertyListener = new PropertyListener();
		window = new Window(squareList, random, buyListener, moveListener, endListener, time, loadListener, saveListener, mortgageListener, propertyListener, payListener);

		int wantToLoad = JOptionPane.showConfirmDialog(null, "Would you like to LOAD a game?", "Load Game", JOptionPane.YES_NO_OPTION);
		if(wantToLoad == JOptionPane.YES_OPTION) {
			if(load(true)) {
				return;
			}
		}
		load(false);
	}

	/**
	 * Loads game info from a selected file or sets up the board if a new game is selected.
	 *
	 * @param	loadNewFile		True if a file is to be loaded, else creates a new game
	 * @returns				Returns false if no file is selected. Else returns true.
	 */
	public static boolean load(boolean loadNewFile) {
		int num_players;
		if(loadNewFile) {
			JFileChooser chooser = new JFileChooser();
			int choice = chooser.showOpenDialog(null);
			if(choice == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				fh = new FileHandler(file);
				squareList = fh.getBoard();
				time = fh.getTime();
				window.dispose();
				window = new Window(squareList, random, buyListener, moveListener, endListener, time, loadListener, saveListener, mortgageListener, propertyListener, payListener);
				playerList = fh.getPlayerList();
				num_players = playerList.length;
			}
			else {
				return false;
			}
		}
		else {
			num_players = promptNumPlayers();
			playerList = Arrays.copyOfRange(fh.getPlayerList(), 0, num_players);
		}
		int playerTurn = fh.getPlayerTurn();
		GO_PAYOUT = fh.getPayout();
		JAIL_POS = fh.getJailPosition();
		game = new Game(playerList, squareList, window, random, playerTurn);
		PhaseListener[] tradeListeners = new PhaseListener[num_players];
		for (int i = 0; i < num_players; i++) {
			tradeListeners[i] = new PhaseListener(GamePhase.TRADE, playerList[i]);
			if(playerList[i].getName().equals("null")) {
				String name = promptName(i);
				playerList[i].setName(name);
			}
		}
		window.setPlayers(playerList, tradeListeners);
		game.startPhase();
		return true;
	}
	/**
	 * Changes which phase the game is in currently. Is called by PhaseListener.
	 *
	 * @param	gamePhase		Which phase the game should be set to
	 * @param	player			Player to trade with (null if not TradeListener)
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
	 * Prompts the user using a JPane to input the number of players > 1 and < 5
	 *
	 * @return		The number of players to be put into the game
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
	 * Prompts the user for their name via a JPane
	 *
	 * @param	playerID	the ID number of the player being prompted
	 * @return				The input player name
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
	 * Method used by mortgage listeners that will toggle
	 * 		whether a property is mortgaged or not
	 *
	 * @param	propIndex	The index of the property to toggle
	 */
	private static void toggleMortgage(int propIndex) {
		game.toggleMortgage(propIndex);
		window.update(game.getCurrentPlayer());
	}

	/**
	 * Displays the popup of information about a property. Used
	 *		by PropertyListener.
	 *
	 * @param	propIndex	The index of the property to display
	 */
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
	
	
	
	private static class PhaseListener implements ActionListener {
		GamePhase gamePhase;
		Player player;

		/**
		 *	An ActionListener which upon being triggered, will move the game into the assigned phase.
		 *
		 * @param	gp				An enumeration for which phase to trigger
		 * @param	optionalPlayer	Which player you are trading with (null if not TradeListener)
		 */
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

	private static class MortgageListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int propertyIndex = Integer.parseInt(e.getActionCommand());
			toggleMortgage(propertyIndex);
		}
	}

	private static class LoadListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			OaklandOligarchy.load(true);
		}
	}

	private static class SaveListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			JFileChooser chooser = new JFileChooser();
			if(chooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
				File file = chooser.getSelectedFile();
				fh.save(file, time, game.getPlayers(), squareList, game.getTurn());
			}
		}
	}

	private static class PayListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			game.getCurrentPlayer().charge(JAIL_COST);
			game.getCurrentPlayer().leaveJail();
			window.update(game.getCurrentPlayer());
			JOptionPane.showMessageDialog(null, "You paid $" + JAIL_COST + " to leave jail.");
		}
	}
}
