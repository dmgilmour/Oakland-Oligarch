package game;
import java.util.Random;
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
		System.out.println(playerList[playerTurn].name + " rolled a " + roll);
		
		board.movePlayer(playerList[playerTurn], roll);
		window.setVisible(true);
		endPhase();
	}
	
	public static void endPhase() {
		// These two lines need to go at the end of each turn, wherever that may be
		playerTurn = (playerTurn + 1) % num_players;
		rollTaken = false;
	}
	
	public static int roll(Long timeMillis) {
		if(!rollTaken)
		{
			Random rand = new Random(timeMillis);
			rollTaken = true;
			int roll = rand.nextInt(5) + rand.nextInt(5) + 2;
			return roll;
		}
		else
			return -1;
	}
}
