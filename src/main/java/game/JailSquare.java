package game;

/**
 * @author Eddie
 *
 */
public class JailSquare extends Square {

	private static final int MAX_NUMBER_OF_PLAYERS = 4;
	
	private Player[] prisoners = new Player[MAX_NUMBER_OF_PLAYERS];
	
	/**
	 * Constructor for the Jail square.
	 * 
	 * @param n		The name of the square (should be "Jail")
	 */
	public JailSquare(String n) {
		name = n;
	}
	
	public boolean act(Player player) {
		return true;
	}
	
	public Player[] getPrisoners(){
		return prisoners;
	}
	
	public void addPrisoner(Player p){
		prisoners[p.getId()] = p;
	}
	
	public void removePrisoner(Player p){
		prisoners[p.getId()] = null;
	}
	

}
