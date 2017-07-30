package game;

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.io.IOException;
import java.lang.Throwable;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.lang.StringBuilder;


/**
 * A class that will handle the saving and loading of the game.
 *
 * @author Woodrow Fulmer
 */
public class FileHandler{
	private static final String DEFAULT_FILE_NAME = "default_file_encrypt.txt";
	private static final int CIPHER = 3;
	
	private ArrayList<Player> playerList;	
	private Time time;
	private Square[] squareList;
	private int GO_PAYOUT;
	private int playerTurn;
	private int JAIL_POS;
	private int activePlayers;
	
	/**
	 * Creates a FileHandler to save and load from any files.
	 *
	 * @param	File	The file to load from
	 */
	public FileHandler(File f) {
		squareList = new Square[OaklandOligarchy.NUMBER_OF_TILES];
		playerList = new ArrayList<Player>(OaklandOligarchy.MAX_NUMBER_OF_PLAYERS);
		activePlayers = 0;
		load(f);
	}
	
	/**
	 * Creates a FileHandler to save a load from any files.
	 *
	 * @param	String	The name of the file to load
	 */
	public FileHandler(String fn) {
		this(new File(fn));
	}
	
	/**
	 * Creates a FileHandler to save and load from any files.
	 */
	public FileHandler() {
		this(DEFAULT_FILE_NAME);
	}
	
	public Time getTime() {return time;}
	public Square[] getBoard() {return squareList;}
	public Player[] getPlayerList() {return playerList.toArray(new Player[playerList.size()]);}
	public int getPayout() {return GO_PAYOUT;}
	public int getJailPosition() {return JAIL_POS;}
	public int getActivePlayers() {return activePlayers;}
	public int getPlayerTurn() {return playerTurn;}
	
	/**
	 * Reads through a given file and parses out all the information into appropriate structures
	 *
	 * @param	file	File to be loaded from
	 */
	private void load(File f) {
		int[] ownerList = new int[OaklandOligarchy.NUMBER_OF_TILES];
		try{
			Scanner reader = new Scanner(f);
			while (reader.hasNextLine()) {
				String decrypted = decrypt(reader.nextLine());
				String[] input = decrypted.split("\t+");
				if(input.length > 1) {
					loadLine(input, ownerList);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}
		setSquareOwners(ownerList);
		loadPlayersIntoJail();
	} 
	
	/**
	 * Loads all the appropriate information from a single line of text
	 *		which has been split at each tab.
	 *
	 * @param	input		A single line from the file, split by tabs
	 * @param	ownerList	A list which matches property indices to the ID of their owner
	 */
	private void loadLine(String[] input, int[] ownerList) {
		if(input[0].equals("Time")) {
			time = new Time(Integer.parseInt(input[1]));
		}
		else if(input[0].equals("GoPayout")) {
			GO_PAYOUT = Integer.parseInt(input[1]);
		}
		else if(input[0].equals("Player")) {
			loadPlayer(input);
		}
		else {
			loadSquare(input, ownerList);
		}
	}
	
	/**
	 * Loads player info from a line that is designated as a player.
	 *
	 * @param	input	A single line from the file, split by tabs
	 */
	private void loadPlayer(String[] input) {
		Player p = new Player(Integer.parseInt(input[1]), Integer.parseInt(input[4]), input[2]);
		p.setPosition(Integer.parseInt(input[5]));
		if(p.getMoney() < 0) {
			p.setLoser(true);
		}
		else {
			activePlayers++;
		}
		p.setColor(Integer.decode(input[3]));
		if(input[6].equals("*")){
			playerTurn = p.getId();
		}
		int jail = Integer.parseInt(input[7]);
		
		if(jail >= 0) {
			p.goToJail();
			for(int i = 0; i < jail; i++) {
				p.addToJailCounter();
			}
		}
		
		JLabel label = new JLabel();
		label.setSize(40,40);
		ImageIcon image = new ImageIcon("", "null");
		if(!input[8].equals("null")) {
			image = new ImageIcon(input[8] + ".png", input[8]);
		}
		image = new ImageIcon(image.getImage().getScaledInstance(40, 40, java.awt.Image.SCALE_SMOOTH), image.toString());
		label.setIcon(image);
		p.setToken(label);
		
		playerList.add(p);
	}
	
	/**
	 * Parses a single line from a file for information pertaining to a Square.
	 *
	 * @param	input		A single line from the file, split by tabs
	 * @param	ownerList	A list which matches property indices to the ID of their owner
	 */
	private void loadSquare(String[] input, int[] ownerList) {
		int current = Integer.parseInt(input[1]);
		if(input[0].equals("Property")) {
			squareList[current] = new Property(input[2], Integer.parseInt(input[3]), Integer.parseInt(input[4]));
			ownerList[current] = Integer.parseInt(input[5]);
			if(input[5].equals("m")) {
				((Property)squareList[current]).setMortgaged(true);
			}
		}
		else if(input[0].equals("Jail")) {
			JAIL_POS = current;
			squareList[current] = new JailSquare("Jail");
			ownerList[current] = -1;
		}
		else if(input[0].equals("Go")) {
			squareList[Integer.parseInt(input[1])] = new GoSquare();
			ownerList[current] = -1;
		}
	}
	
	/**
	 * Assigns unmarked squares as actions. Sets the owner of each property if it has one.
	 *
	 * @param	ownerList	A list which matches property indices to the ID of their owner
	 */
	private void setSquareOwners(int[] ownerList) {
		for (int i = 0; i < squareList.length; i++) {
			if (squareList[i] == null) {
				squareList[i] = new ActionSquare("Action");
			}
			else if(ownerList[i] > -1 && ownerList[i] < playerList.size()) {
				Player player = playerList.get(ownerList[i]);
				Property property = (Property)squareList[i];
				if(!player.getLoser()) {
					player.addProperty(property);
				}
			}
		}
	}
	
	/**
	 * Puts a player in jail if they are incarcerated.
	 */
	private void loadPlayersIntoJail() {
		for(Player p: playerList) {
			if(p.isInJail()) {
				((JailSquare)squareList[JAIL_POS]).addPrisoner(p);
			}
		}
	}
	
	/**
	 * Saves the time, the go payout, all square info, and all player info to a given file.
	 *
	 * @param	file		The file to save to
	 * @param	time		The elapsed game time
	 * @param	players		The full list of players
	 * @param	squares		All the squares on the board
	 * @param	playerTurn	The ID of the player whose turn it is
	 */
	public void save(File file, Time time, Player[] players, Square[] squares, int playerTurn) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			String nextLine = encrypt("Time\t" + time.getTime());
			bw.write(nextLine);
			bw.newLine();
			nextLine = encrypt("GoPayout\t" + GO_PAYOUT);
			bw.write(nextLine);
			bw.newLine();
			for(Player p: players) {
				savePlayer(bw, p, playerTurn);
			}
			bw.newLine();
			for(int i = 0; i < squares.length; i++) {
				saveSquare(bw, squares[i], i);
			}
			bw.close();
		} catch (IOException except) {}
	}
	
	/**
	 * Writes all the information from a given player to the specified output buffer.
	 *
	 * @param	bw			The output buffer to write to
	 * @param	p			The player to be saved to file
	 * @param	playerTurn	The ID of the player whose turn it is
	 */
	private void savePlayer(BufferedWriter bw, Player p, int playerTurn) throws IOException {
		StringBuilder sb = new StringBuilder("Player\t");
		sb.append(p.getId()+"\t");
		sb.append(p.getName()+"\t");
		sb.append(p.getColor()+"\t");
		sb.append(p.getMoney() + "\t");
		sb.append(p.getPosition() + "\t");
		if(p.getId() == playerTurn) {
			sb.append("*\t");
		}
		else {
			sb.append("-\t");
		}
		if(p.isInJail()) {
			sb.append(p.getJailCounter() + "\t");
		}
		else {
			sb.append("-1\t");
		}
  	sb.append(((ImageIcon)(p.getToken().getIcon())).getDescription());
    
		String encrypted = encrypt(sb.toString());
		bw.write(encrypted);
		bw.newLine();
	}
	
	/**
	 * Writes all the information from a given square to the specified output buffer.
	 *
	 * @param	bw		The output buffer to write to
	 * @param	s		The square to be saved to file
	 * @param	index	The index of the given square within the board
	 */	
	private void saveSquare(BufferedWriter bw, Square s, int index) throws IOException {
		if(s instanceof Property) {
			saveProperty(bw, (Property)s, index);
		}
		else if(s instanceof JailSquare) {
			String encrypted = encrypt("Jail\t" + index);
			bw.write(encrypted);
			bw.newLine();
		}
		else if(s instanceof GoSquare) {
			String encrypted = encrypt("Go\t" + index);
			bw.write(encrypted);
			bw.newLine();
		}
	}
	
	/**
	 * Writes all the information from a given property to the specified output buffer.
	 *
	 * @param	bw		The output buffer to write to
	 * @param	p		The property to be saved to file
	 * @param	index	The index of the given property within the board
	 */	
	private void saveProperty(BufferedWriter bw, Property p, int index) throws IOException {
		StringBuilder sb = new StringBuilder("Property\t");
		sb.append(index + "\t");
		sb.append(p.getName() + "\t");
		sb.append(p.getPrice() + "\t");
		sb.append(p.getRent() + "\t");
		Player owner = p.getOwner();
		if(owner == null) {
			sb.append("-1\t");
		}
		else {
			sb.append(owner.getId() + "\t" );
		}
		if(p.getMortgaged()) {
			sb.append("m");
		}
		else {
			sb.append("u");
		}
		String encrypted = encrypt(sb.toString());
		bw.write(encrypted);
		bw.newLine();
	}
	
	private String decrypt(String input) {
		char [] chars = input.toCharArray();
		for(int i = 0; i < chars.length; i++) {
			chars[i] = (char)(chars[i] + CIPHER);
		}
		return new String(chars);
	}
	
	private String encrypt(String input) {
		char [] chars = input.toCharArray();
		for(int i = 0; i < chars.length; i++) {
			chars[i] = (char)(chars[i] - CIPHER);
		}
		return new String(chars);
	}
}