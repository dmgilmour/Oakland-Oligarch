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
	private Property[] properties;
	private static Board board;
	private static Window window;
	private static int num_players;
	private static Player[] playerList;
	
	public Game(Player[] _playerList) {
		
		playerList = _playerList;
		properties = new Property[36];
		num_players = playerList.length;
		playerTurn = 0;
		rollTaken = false;
		for(int i = 0; i<36; i++){
			properties[i]=new Property("Property "+i, i, i);
		}
		board = new Board(playerList, properties);
		window = new Window(playerList, properties, board);		
	}
	
	/**
	 * Runs the game phase during which players roll and move
	 */
	public static void movePhase() {
		int roll = roll(System.currentTimeMillis());		
		board.movePlayer(playerList[playerTurn], roll);
		window.update();
		actionPhase();
		endPhase();
	}
	
	/**
	 * Runs the game phase that ends each players turn
	 */
	public static void endPhase() {
		playerTurn = (playerTurn + 1) % num_players;	//Increment to the next player's turn
		rollTaken = false;								//Enable the "roll" button again
		window.update();
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
		Tile tile = board.getTile(player.getPosition());
		if(tile == null)									//Check to ensure that a tile was retrived properly from the board
			return;
		if(tile instanceof PropertyTile) {					//If the tile retrived is a property:
			Property property = ((PropertyTile)tile).getProperty(); 
			Player owner = property.getOwner();
			if(owner != null) {								//If this property is owned:
				player.payRent(property);					//Pay rent on the property and alert the player
				JOptionPane.showMessageDialog(null, player.getName()+ " pays $" + property.getRent() + "  to " + owner.getName());
			}
			else {		//The property is unowned
				int choice = JOptionPane.showConfirmDialog(null, "Would you like to buy " + property.getName() + "?", "Buy property?", JOptionPane.YES_NO_OPTION);
				if(choice == JOptionPane.YES_OPTION) {		//Ask of the player would like to purchase this property
					player.buy(property);
				}
			}
		}
		window.update();
	}
}
