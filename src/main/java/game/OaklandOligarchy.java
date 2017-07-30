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
import javax.sound.sampled.*;


/**
 * @author Eddie Hartman
 * @author Woodrow Fulmer
 * @author David Haskell
 * @author Dan Gilmour
 */
public class OaklandOligarchy {

	public enum GamePhase {MOVE, ACTION, END, START, BUY, TRADE};

	private static final String[] ICON_FILE_NAMES = {"Helmet", "Insignia", "Mascot", "Script"};
	public static final int NUMBER_OF_TILES = 36;
	public static final int MAX_NUMBER_OF_PLAYERS = 4;
	public static final int NUMBER_OF_ACTIONS = 14;
	public static final int JAIL_COST = 50;
	public static final int MAX_JAIL_TURNS = 3;	//the maximum amount of turns a player can spend in jail
	public static int JAIL_POS;	//what tile jail is
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
	private static InstructionListener instructionListener;
	private static ActionSquareListener actionSquareListener;
	private static AudioPlayer player;

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
		instructionListener = new InstructionListener();
		actionSquareListener = new ActionSquareListener();
		window = new Window(squareList, random, buyListener, moveListener, endListener, time, loadListener, saveListener, mortgageListener, propertyListener, payListener, instructionListener, actionSquareListener);
		player = new AudioPlayer();

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
				window = new Window(squareList, random, buyListener, moveListener, endListener, time, loadListener, saveListener, mortgageListener, propertyListener, payListener, instructionListener, actionSquareListener);
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
		
		ArrayList<String> icons = new ArrayList<String>();
		for(String name: ICON_FILE_NAMES) {
			icons.add(name);
		}
		
		for (int i = 0; i < num_players; i++) {
			tradeListeners[i] = new PhaseListener(GamePhase.TRADE, playerList[i]);
			if(playerList[i].getName().equals("null")) {
				String name = promptName(i);
				playerList[i].setName(name);
			}
			if(playerList[i].getToken().getIcon() == null) {
				promptTokens(icons, playerList[i]);
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
		nameCheck(toReturn);
		return toReturn;
	}
	
	private static void promptTokens(ArrayList<String> icons, Player p) {
		String selected = (String)(JOptionPane.showInputDialog(null, p.getName() + "\nSelect a token:", "Token Selection", JOptionPane.PLAIN_MESSAGE, null, icons.toArray(), (Object)icons.get(0)));
		ImageIcon image = new ImageIcon(selected + ".png");

		image = new ImageIcon(image.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH), selected);
		JLabel label = new JLabel();
		label.setIcon(image);
		
		p.setToken(label);
		icons.remove(selected);
	}

	/**
	 * Method checking if the name entered is an "easter egg," and if something
	 * special should happen
	 *
	 * @param	name	String entered as a name for a player.
	 */
	private static void nameCheck(String name){
		if(name.equals("Morty")){
			String audioFilePath = "./sounds/my_name_is_morty_smith.wav";
			player.play(audioFilePath);
		}
		if(name.equals("Mr.Meeseeks")){
			String audioFilePath = "./sounds/Hi_I'm_mr_meeseeks_look_at_me.wav";
			player.play(audioFilePath);
		}
		if(name.equals("Tiny Rick")){
			String audioFilePath = "./sounds/rick.wav";
			player.play(audioFilePath);
		}
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
			if (!game.getCurrentPlayer().getLoser()) {
				game.getCurrentPlayer().leaveJail();
				window.update(game.getCurrentPlayer());
				JOptionPane.showMessageDialog(null, "You paid $" + JAIL_COST + " to leave jail.");
			}
		}
	}

	private static class InstructionListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			String audioFilePath = "./sounds/Oooo_yeah__caaan_doo!.wav";
			player.play(audioFilePath);
			JEditorPane textArea = new JEditorPane("text/html", "");
			String text = "<h1>Oakland Oligarchy</h1>\nThe Object of Oakland Oligarchy game is to become the wealthiest player through buying, renting, and selling property (easier said than done). There are two methods to achieve victory.\n<ol><li>Acquire all four (4) transit properties, for a transit monopoly victory.</li><li>Outlast your opponents by being the last player with any money.</li></ol>To play, move tokens around the board according to each roll of the dice. When your token lands on a space that is not already owned by another player, you may purchase that property. If you do not want to buy it (or can not afford to buy it), the property will be auctioned off to the highest bidder. If the property your token lands on is owned by another player, you pay rent for staying there. When landing on an action space, players must obey the action selected.\n\nWelcome to the wonderful world of Oakland Oligarchy!\n\n<h2>Playing the Game</h2>\nEach player starts from the Go space (top left corner of the board).\nTo begin, roll the dice by clicking the roll giant dice button. Your token will be moved the number of spaces indicated by the dice, clockwise around the board. Once your token lands on a space, you may be entitled to one of the following, depending on that space.\n<ul><li>Buy property</li><li>Pay rent (if the property is already owned)</li><li>Perform an action</li><li>Go to Jail</li><li>Collect $200 or $400</li></ul>Doubles:\nIf you roll doubles, your token will move as usual. You are subject to any privileges or penalties of the space you land on. Then roll the dice again as if you turn had started over. If you roll three doubles in a row, you are sent to Jail immediately after the third roll of doubles, ending your turn.\n\nPassing Go:\nYou are paid $200 every time your token passes Go, and an additional $200 for landing directly on Go.\n\nLanding on Unowned Property:\nWhen you land on unowned property you may buy that property by paying the shown price. If you choose to buy, you pay the Bank for the property and the property is added to your inventory and shown on the left player panel during your turn. If you do not wish to buy the property, it will be auctioned off to the highest bidder.\n\nPaying Rent:\nWhen you land on a property that is already owned by another player, the owner collects rent from you, as indicated by the property. If the property is mortgaged, the player who owns the property does not collect rent.\n\nLanding on an Acton Space:\nWhen you land on one of these spaces, you are given a random action that will either benefit or penalise you and perhaps other players. Good luck! For a list of possible actions see the list of possible actions by clicking the Actions button in the top panel.\n\n<h2>Jail</h2>\nYou go to Jail if:<ul><li>You land on an Action space and are sent to Jail.</li><li>You roll doubles three times in a row.</li></ul>When you are sent to Jail, your turn ends there. You do not pass Go or collect the $200 for doing so, you move directly to Jail. If you are not sent to Jail by land on Jail by chance, you are not assessed any penalty. Continue play as if you landed on a free space.\n\nYou get out of Jail if:<ul><li>You roll doubles on any of your next three turns after being sent to Jail.</li><li>Paying a $50 fine.</li></ul>If you do not roll doubles by your third turn in Jail you must pay the $50 fine.\n\n<h2>Mortgaging Property</h2>\nYou may mortgage properties you own in order to increase the amount of money you have. The mortgage value of a property is half its price to purchase. The mortgage value is also indicated on the property buttons in the top left player panel. Properties that are mortgaged do not collect rent from visiting players. To unmortgage a property you must buy it back at full cost of the property.\n\n<h2>Losing the Game</h2>\nYou are knocked out of and lose the game if you owe more money than you can pay to another player or the bank. If you owe that money to another player you must liquidate all of the properties you own in an attempt to pay as much of the debt as possible. Properties of a loser are unmortgaged and reverted to being unowned, free for other players to purchase when landed on.\n\n<h2>Winning the Game</h2>\nTo win the game you must either:<ul><li>Acquire all four (4) transit properties, for a transit monopoly victory</li><li>Outlast your opponents by being the last player with any money.</li></ul>";
			textArea.setText(text);
			JScrollPane scrollPane = new JScrollPane(textArea);
			scrollPane.setPreferredSize(new Dimension(1000, 500));
			JOptionPane.showMessageDialog(null, scrollPane, "Instructions", JOptionPane.PLAIN_MESSAGE);
		}
	}

	private static class ActionSquareListener implements ActionListener{
		public void actionPerformed(ActionEvent e){
			String audioFilePath = "./sounds/Oooo_yeah__caaan_doo!.wav";
			player.play(audioFilePath);
			JEditorPane textArea = new JEditorPane("text/html", "");
			String text = "<h2>Student Activities Fee</h2>\nGain $25 for each property  you own.\n<h2>Order an Autonomous Uber</h2>\nPay $50, and roll again (does not count as doubles).\n<h2>Work at Tapingo Delivery!</h2>\nEach player pays you $100.\n<h2>Rain Storm</h2>\nAll players pay 2 times the rent of the tile they are currently on plus $32.\n<h2>First of the Month</h2>\nCollect money from the bank equal to the rent of all properties you own.\n<h2>Summer Break!!!</h2>\nAll players lose one property at random (lose nothing if you do not own any property).\n<h2>Orientation Week</h2>\nGain one random property.\n<h2>Arrange Sublet</h2>\nMove directly to a random property square you do not own, and pay rent to the owner, if owned. Do not pass Go.\n<h2>Had a Bad Week</h2>\nLose all your money.\n<h2>Bacteria in the Water</h2>\nIf random property is unowned, you can purchase it for twice the price. Otherwise, all players pay owner $100.\n<h2>Rush a Fraternity</h2>\nGain a random property -OR- go directly to jail (do not pass Go) -OR- all other players pay you $40.\n<h2>Find a Significant Other</h2>\nGain a random property, but lose all your money.\n<h2>Indefinite Construction</h2>\n3 random consecutive properties become unowned.\n<h2>Attempt to Jump the Gap Over Qdoba</h2>\nLose $500 and all other players gain $50.\n";
			textArea.setText(text);
			JScrollPane scrollPane = new JScrollPane(textArea);
			scrollPane.setPreferredSize(new Dimension(1000, 500));
			JOptionPane.showMessageDialog(null, scrollPane, "Actions", JOptionPane.PLAIN_MESSAGE);
		}
	}

	public static class AudioPlayer implements LineListener{
		boolean playCompleted;

		/**
		 * Tries to play the sound file with path equal to audioFilePath.
		 *
		 * @param	audioFilePath	String version of the path for a sound to be played.
		 */
		public void play(String audioFilePath){
			File audioFile = new File(audioFilePath);
			try{
				AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
				AudioFormat format = audioStream.getFormat();
				DataLine.Info info = new DataLine.Info(Clip.class, format);
				Clip audioClip = (Clip) AudioSystem.getLine(info);
				audioClip.addLineListener(this);
				audioClip.open(audioStream);
				audioClip.start();
				/*while(!playCompleted){
					try{
						Thread.sleep(100);
					}catch(InterruptedException ie){
						ie.printStackTrace();
					}
				}*/
			} catch(UnsupportedAudioFileException uafe){
				System.out.println("The specified audio file is not supported.");
				uafe.printStackTrace();
			}catch(LineUnavailableException lua){
				System.out.println("Audio line for playing back is unavailable.");
				lua.printStackTrace();
			}catch(IOException ioe){
				System.out.println("Error playing the audio file.");
				ioe.printStackTrace();
			}
		}

		public void update(LineEvent event){
			LineEvent.Type type = event.getType();
			if(type == LineEvent.Type.START){
				//System.out.println("Playback started.");
			}
			else if(type == LineEvent.Type.STOP){
				playCompleted = true;
				//System.out.println("Playback completed.");
			}
		}
	}
}
