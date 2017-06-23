package game;

import java.util.Random;
import javax.swing.*;

/**
 * @author Dan
 *
 */
public class Game {

	private static final int NUM_PROPERTIES = 36;

	// Tracking primative variables
	private static boolean rollTaken;
	private static int playerTurn;
	private static int num_players;

	// Tracking objects
	public static Player curPlayer;
	public static Tile curTile;

	// Overall objects
	public static  Board board;
	private static Property[] properties;
	private static Window window;
	private static Player[] playerList;
	
	public Game() {
		properties = generateProperties();
		board = new Board(properties);

		playerTurn = 0;
		rollTaken = false;
	}

	public void setWindow(Window _window) {
		window = _window;
	}
	
	public Property[] generateProperties() { 
		Property[] properties = new Property[NUM_PROPERTIES];
		for (int i = 0; i < NUM_PROPERTIES; i++){
			properties[i] = new Property("Property "+i, i, i);
		}
		return properties;
	}

	public void setPlayers(Player[] _playerList) {
		playerList = _playerList;
		num_players = playerList.length;
		board.setPlayers(playerList);
	}


	/**
	 * Runs the game phase for the start of a turn during which a player can click info
	 * buttons and roll the dice giant button
	 */
	public static void startPhase() {
		rollTaken = false;			
		curPlayer = playerList[playerTurn];
		window.disableEnd();
		window.enableRoll();
		window.update();
	}


	/**
	 * Runs the game phase during which players roll and move
	 */
	public static void movePhase() {
		int roll = roll(System.currentTimeMillis());		
		board.movePlayer(curPlayer, roll);
		curTile = board.getTile(curPlayer.getPosition());

		doTheTilesThing();

		window.update();
		actionPhase();
	}


	/**
	 * Runs the game phase where the player performs an action based on the tile they are on
	 */
	public static void actionPhase() {
		// Check to ensure that a tile was retrived properly from the board
		window.disableRoll();
		window.enableEnd();
		window.update();
	}
	
	/**
	 * Runs the game phase that ends each players turn
	 */
	public static void endPhase() {
		playerTurn = (playerTurn + 1) % num_players;	//Increment to the next player's turn
		startPhase();
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

	public static void buyProperty() {
		Property property = ((PropertyTile)curTile).getProperty(); 
		curPlayer.buy(property);
		window.disableBuy();
		window.update();
	}

	public static void doTheTilesThing() {

		if (curTile == null) { 
			throw new IllegalArgumentException("Unable to fetch Tile");	
		}

		// If the tile retrived is a property:
		if (curTile instanceof PropertyTile) { 
			Property property = ((PropertyTile)curTile).getProperty(); 
			Player owner = property.getOwner();
			if(owner != null) {								//If this property is owned:
				curPlayer.payRent(property);					//Pay rent on the property and alert the player
				JOptionPane.showMessageDialog(null, curPlayer.getName()+ " pays $" + property.getRent() + "  to " + owner.getName());
			} else { // The property is unowned
				int choice = JOptionPane.showConfirmDialog(null, "Would you like to buy " + property.getName() + "?", "Buy property?", JOptionPane.YES_NO_OPTION);
				if(choice == JOptionPane.YES_OPTION) {		//Ask of the player would like to purchase this property
					curPlayer.buy(property);
				}
			}

			if (property.getOwner() == null) {
				window.enableBuy();
			} else {
				window.disableBuy();
			}
				
		}
	}


}
