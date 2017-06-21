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
	
	public Game(Player[] playerList) {
		
		this.playerList = playerList;
		properties = new Property[36];
		num_players = playerList.length;
		playerTurn = 0;
		rollTaken = false;
		for(int i = 0; i<36; i++){
			properties[i]=new Property("Property "+i, i, i);
		}
		board = new Board(playerList, properties);
		window = new Window(playerList, properties, board);

		// Other random setup
		
	}

	// More things when we get to do non UI
	
	public static void movePhase() {
		int roll = roll(System.currentTimeMillis());		
		board.movePlayer(playerList[playerTurn], roll);
		window.update();
		actionPhase();
		endPhase();
	}
	
	public static void endPhase() {
		// These two lines need to go at the end of each turn, wherever that may be
		playerTurn = (playerTurn + 1) % num_players;
		rollTaken = false;
		window.update();
	}
	
	public static int roll(Long timeMillis) {
		if(!rollTaken)
		{
			Random rand = new Random(timeMillis);
			rollTaken = true;
			int roll = rand.nextInt(6) + rand.nextInt(6) + 2;
			return roll;
		}
		else
			return -1;
	}
	
	public static void actionPhase() {
		Player player = playerList[playerTurn];
		Tile tile = board.getTile(player.getPosition());
		if(tile == null)
			return;
		if(tile instanceof PropertyTile) {
			Property property = ((PropertyTile)tile).getProperty();
			Player owner = property.getOwner();
			if(owner != null) {
				player.payRent(owner, property);
				JOptionPane.showMessageDialog(null, player.getName()+ " pays $" + property.getRent() + "  to " + owner.getName());
			}
			else {
				int choice = JOptionPane.showConfirmDialog(null, "Would you like to buy " + property.getName() + "?", "Buy property?", JOptionPane.YES_NO_OPTION);
				if(choice == JOptionPane.YES_OPTION) {
					player.buy(property);
				}
			}
		}
		window.update();
	}
}
