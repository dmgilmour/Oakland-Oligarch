package game;

import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.util.ArrayList;
import java.io.IOException;
import java.lang.Throwable;

/**
 * A class that will handle the saving and loading of the game.
 *
 * @author Woodrow Fulmer
 */
public class FileHandler{
	private static final String DEFAULT_FILE = "defaultFile.txt";
	
	private ArrayList<Player> playerList;	
	private Time time;
	private Square[] squareList;
	private int GO_PAYOUT;
	private int playerTurn;
	private int JAIL_POS;
	private int activePlayers;
	
	public FileHandler(File f) {
		squareList = new Square[OaklandOligarchy.NUMBER_OF_TILES];
		playerList = new ArrayList<Player>(OaklandOligarchy.MAX_NUMBER_OF_PLAYERS);
		activePlayers = 0;
		load(f);
	}
	
	public FileHandler(String fn) {
		this(new File(fn));
	}
	
	public FileHandler() {
		this(DEFAULT_FILE);
	}
	
	public Time getTime() {return time;}
	public Square[] getBoard() {return squareList;}
	public Player[] getPlayerList() {return playerList.toArray(new Player[playerList.size()]);}
	public int getPayout() {return GO_PAYOUT;}
	public int getJailPosition() {return JAIL_POS;}
	public int getActivePlayers() {return activePlayers;}
	public int getPlayerTurn() {return playerTurn;}
	
	private void load(File f) {
		int[] ownerList = new int[OaklandOligarchy.NUMBER_OF_TILES];
		try{
			Scanner reader = new Scanner(f);
			while (reader.hasNextLine()) {
				String[] input = reader.nextLine().split("\t+");
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
		playerList.add(p);
	}
	
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
	
	private void loadPlayersIntoJail() {
		for(Player p: playerList) {
			if(p.isInJail()) {
				((JailSquare)squareList[JAIL_POS]).addPrisoner(p);
			}
		}
	}
	
	public void save(File file, Time time, Player[] players, Square[] squares, int playerTurn) {
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			bw.write("Time\t" + time.getTime());
			bw.newLine();
			bw.write("GoPayout\t" + GO_PAYOUT);
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
	
	private void savePlayer(BufferedWriter bw, Player p, int playerTurn) throws IOException {
		bw.write("Player\t");
		bw.write(p.getId()+"\t");
		bw.write(p.getName()+"\t");
		bw.write(p.getColor()+"\t");
		bw.write(p.getMoney() + "\t");
		bw.write(p.getPosition() + "\t");
		if(p.getId() == playerTurn) {
			bw.write("*\t");
		}
		else {
			bw.write("-\t");
		}
		if(p.isInJail()) {
			bw.write(p.getJailCounter());
		}
		else {
			bw.write("-1");
		}
		bw.newLine();
	}
	
	private void saveSquare(BufferedWriter bw, Square s, int index) throws IOException {
		if(s instanceof Property) {
			saveProperty(bw, (Property)s, index);
		}
		else if(s instanceof JailSquare) {
			bw.write("Jail\t" + index);
			bw.newLine();
		}
		else if(s instanceof GoSquare) {
			bw.write("Go\t" + index);
			bw.newLine();
		}
	}
	
	private void saveProperty(BufferedWriter bw, Property p, int index) throws IOException {
		bw.write("Property\t");
		bw.write(index + "\t");
		bw.write(p.getName() + "\t");
		bw.write(p.getPrice() + "\t");
		bw.write(p.getRent() + "\t");
		Player owner = p.getOwner();
		if(owner == null) {
			bw.write("-1\t");
		}
		else {
			bw.write(owner.getId() + "\t" );
		}
		if(p.getMortgaged()) {
			bw.write("m");
		}
		else {
			bw.write("u");
		}
		bw.newLine();
	}
}