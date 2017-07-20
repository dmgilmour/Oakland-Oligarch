package game;

/**
 * @author Eddie
 *
 */
public class JailSquare extends Square {
	
	private Player[] prisoners = new Player[OaklandOligarchy.MAX_NUMBER_OF_PLAYERS];
	
	/**
	 * Constructor for the Jail square.
	 * 
	 */
	public JailSquare(String n) {
		name = n;
	}
	
	/**
	 * No action occurs if a player lands on Jail
	 *	
	 */
	public boolean act(Player player) {
		return true;
	}
	
	/**
	 * Retrieves all the players currently incarcerated
	 *
	 */
	public Player[] getPrisoners(){
		return prisoners;
	}
	
	/**
	 * Adds the newest inmate to Jail
	 *
	 */
	public void addPrisoner(Player p){
		prisoners[p.getId()] = p;
	}
	
	/**
	 * Releases a specified inmate back into Oakland
	 *
	 */
	public void removePrisoner(Player p){
		prisoners[p.getId()] = null;
	}
}
