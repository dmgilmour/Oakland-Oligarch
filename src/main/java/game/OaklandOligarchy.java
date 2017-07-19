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
	public static int JAIL_POS;	//what tile jail is
	public static int PLAYER_STARTING_MONEY;
	public static int GO_PAYOUT;

	public static Player[] playerList;
<<<<<<< HEAD
	private static Game game;
=======

	private static final String FILENAME = "defaultFile.txt";
	private static Scanner reader;

	protected static Game game;

<<<<<<< HEAD
>>>>>>> first workings for a proper win/lose. mortgagePhase and loserPhase added to Game. Cost calls both phases. Shouldn't pay yourself rent anymore. Need to add worth functionality and clean up loserCheck / loserCleanup.
=======
>>>>>>> post Dan's test branch
	private static Window window;
	private static Square[] squareList;
	private static Random random;
	private static Time time;
<<<<<<< HEAD
<<<<<<< HEAD
	private static Scanner reader;
	private static FileHandler fh;
=======

>>>>>>> first workings for a proper win/lose. mortgagePhase and loserPhase added to Game. Cost calls both phases. Shouldn't pay yourself rent anymore. Need to add worth functionality and clean up loserCheck / loserCleanup.
=======

>>>>>>> post Dan's test branch

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
<<<<<<< HEAD
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
		
=======
		File file = new File(FILENAME);
		int[] ownersList = initializeBoard(file);

		int num_players = 0;
>>>>>>> first workings for a proper win/lose. mortgagePhase and loserPhase added to Game. Cost calls both phases. Shouldn't pay yourself rent anymore. Need to add worth functionality and clean up loserCheck / loserCleanup.
		int wantToLoad = JOptionPane.showConfirmDialog(null, "Would you like to LOAD a game?", "Load Game", JOptionPane.YES_NO_OPTION);
		boolean load = false;
		if(wantToLoad == JOptionPane.YES_OPTION) {
			if(load(true, 0)) {
				load = true;
			}
		}
		if(!load){
			int num_players = promptNumPlayers();
			load(false, num_players);
		}
	}

<<<<<<< HEAD
<<<<<<< HEAD
	public static boolean load(boolean loadNewFile, int num_players) {
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
			playerList = Arrays.copyOfRange(fh.getPlayerList(), 0, num_players);
		}
		int playerTurn = fh.getPlayerTurn();
		int activePlayers = fh.getActivePlayers();
		GO_PAYOUT = fh.getPayout();
		JAIL_POS = fh.getJailPosition();
		game = new Game(playerList, squareList, window, random, playerTurn, activePlayers);
=======
=======
>>>>>>> post Dan's test branch
	public static boolean load() {
		JFileChooser chooser = new JFileChooser();
		int choice = chooser.showOpenDialog(null);
		int[] ownersList;
		int num_players;
		if(choice != JFileChooser.APPROVE_OPTION) {
			return false;
		}
		File file = chooser.getSelectedFile();
		window.dispose();
		ownersList = initializeBoard(file);
		try {
			reader = new Scanner(file);
		} catch (Exception e) {
			System.exit(1);
		}

		//need to increment to correct line
		reader.nextInt();
		reader.nextInt();

		num_players = reader.nextInt();
		initializeGame(num_players, ownersList);
		return true;
	}

	private static int[] initializeBoard(File file) {
		try {
			reader = new Scanner(file);
		} catch (Exception e) {
			System.exit(1);
		}
		time = new Time(reader.nextInt());
		GO_PAYOUT = reader.nextInt();
		int[] ownersList = generateSquares();

		PhaseListener buyListener = new PhaseListener(GamePhase.BUY, null);
		PhaseListener moveListener = new PhaseListener(GamePhase.MOVE, null);
		PhaseListener endListener = new PhaseListener(GamePhase.END, null);
		LoadListener loadListener = new LoadListener();
		SaveListener saveListener = new SaveListener();
		PayListener payListener = new PayListener();
		window = new Window(squareList, random, buyListener, moveListener, endListener, time, loadListener, saveListener, new MortgageListener(), new PropertyListener(), payListener);

		//Reset the reader to the beginning of the file
		try {
			reader = new Scanner(file);
		} catch (Exception e) {
			System.exit(1);
		}

		return ownersList;
	}

	private static void initializeGame(int num_players, int[] ownersList) {
		int[] playerInfo = generatePlayers(num_players, ownersList);

		game = new Game(playerList, squareList, window, random, playerInfo[0], playerInfo[1]);
>>>>>>> first workings for a proper win/lose. mortgagePhase and loserPhase added to Game. Cost calls both phases. Shouldn't pay yourself rent anymore. Need to add worth functionality and clean up loserCheck / loserCleanup.
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
	 * Changes which phase the game is in currently. Is called by various ActionListeners
	 *
	 * @param	gamePhase		Which phase the game should be set to
	 * @param	player			Player to trade with if switching to tradePhase
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
<<<<<<< HEAD
=======
	 * Generates an array of Squares (properties and actions) that will act as the game board
	 *
	 * @return					an int[] of who owns which properties
	 */
	private static int[] generateSquares() {
		squareList = new Square[OaklandOligarchy.NUMBER_OF_TILES];
		int[] resultList = new int[NUMBER_OF_TILES];

		while (reader.hasNextLine()) {
			String[] input = reader.nextLine().split("\t+");
			if (input.length == 6){
				try {
					int current = Integer.parseInt(input[0]);
					squareList[current] = new Property(input[1], Integer.parseInt(input[2]), Integer.parseInt(input[3]));
					resultList[current] = Integer.parseInt(input[4]);
					if(input[5].equals("m")) {
						((Property)squareList[current]).setMortgaged(true);
					}
				} catch (NumberFormatException e) {
					//just CONTINUE
				}
			}
			else if (input.length == 2){
				try {
					int current = Integer.parseInt(input[0]);
					JAIL_POS = current;
					squareList[current] = new JailSquare("Jail"); //could be hard coded or take input from defaultFile.txt idc
				} catch (NumberFormatException e) {
					//just CONTINUE
				}
			}
		}

		for (int i = 0; i < squareList.length; i++) {
			if (squareList[i] == null) {
				squareList[i] = new ActionSquare("Action");
				resultList[i] = -1;
			}
		}
		return resultList;


		/*
		for (int i = 0; i < squareList.length; i++){
			if(i == 4 || i == 5 || i == 13 || i == 14 || i == 22 || i == 23 || i == 31 || i == 32) {
				squareList[i] = new ActionSquare("Action "+i);
			}
			else if(i == 9){
				squareList[i] = new JailSquare("Jail");
			}
			else {
				squareList[i] = new Property("Property "+i, i, i);
			}
		}
		return squareList;
		*/
	}

	/**
>>>>>>> first workings for a proper win/lose. mortgagePhase and loserPhase added to Game. Cost calls both phases. Shouldn't pay yourself rent anymore. Need to add worth functionality and clean up loserCheck / loserCleanup.
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
<<<<<<< HEAD
	
=======

	/**
	 * Creates an array of players with their starting money and names
	 *
	 * @param	num_players		The number of players in this game
	 * @param	ownersList		An array indicating who owns what property
	 * @return					The array of players in this game
	 */
	private static int[] generatePlayers(int num_players, int[] ownersList) {
		playerList = new Player[num_players];
		int playersAdded = 0;
		int playerTurn = -1;
		int activePlayers = num_players;
		while (reader.hasNextLine() && playersAdded < num_players) {
			String[] input = reader.nextLine().split("\t+");
			if (input.length != 5) continue;
			String playerName = input[0];
			if(playerName.equals("null")) {
				playerName = promptName(playersAdded);
			}
			if(input[4].equals("*")) {
				playerTurn = playersAdded;
			}
			try {
				int currentMoney = Integer.parseInt(input[2]);
				playerList[playersAdded] = new Player(playersAdded, currentMoney, playerName);
				playerList[playersAdded].setPosition(Integer.parseInt(input[3])); //TODO may need to change for save/load with Jail
				if(currentMoney < 0) {
					activePlayers--;
					playerList[playersAdded].setLoser(true);
				}
				playerList[playersAdded].setColor(Integer.decode(input[1]));
			} catch (NumberFormatException e) {
				continue;
			}
			playersAdded++;
		}

		for(int i = 0; i < ownersList.length; i++) {
			int owner_id = ownersList[i];
			if(owner_id > -1 && owner_id < num_players && !playerList[owner_id].getLoser() && !(squareList[i] instanceof JailSquare)) {
				playerList[owner_id].addProperty((Property)squareList[i]);
			}
		}
		if(playerTurn < 0) {
			playerTurn = 0;
		}
		int[] res = {playerTurn, activePlayers};
		return res;
	}

<<<<<<< HEAD
>>>>>>> first workings for a proper win/lose. mortgagePhase and loserPhase added to Game. Cost calls both phases. Shouldn't pay yourself rent anymore. Need to add worth functionality and clean up loserCheck / loserCleanup.
=======
>>>>>>> post Dan's test branch
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
	 * Method used by mortgage listeners that will toggle
	 * whether a property is mortgaged or not
	 *
	 * @param	propIndex	The index of the property to toggle
	 */
	private static void toggleMortgage(int propIndex) {
		game.toggleMortgage(propIndex);
		window.update(game.getCurrentPlayer());
<<<<<<< HEAD
=======
		Player player = game.getCurrentPlayer();

		Property prop = player.getProperties().get(propIndex);
		if (prop.getMortgaged()) {
			game.unmortgage(prop);
		} else {
			game.mortgage(prop);
		}
		window.update(player);
>>>>>>> first workings for a proper win/lose. mortgagePhase and loserPhase added to Game. Cost calls both phases. Shouldn't pay yourself rent anymore. Need to add worth functionality and clean up loserCheck / loserCleanup.
=======
>>>>>>> pre-last PR from dan
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

	private static class LoadListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			OaklandOligarchy.load(true, 0);
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
			game.getCurrentPlayer().charge(JAIL_COST);	//TODO when charge returns a bool check to see if they could pay
			game.getCurrentPlayer().leaveJail();
			window.update(game.getCurrentPlayer());
			JOptionPane.showMessageDialog(null, "You paid $" + JAIL_COST + " to leave jail.");
		}
	}
}
