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
	 * @param n		The name of Jail
	 */
	public JailSquare(String n) {
		name = n;
	}
	
	/**
	 * No action occurs if a player lands on Jail
	 *	
	 * @returns		TRUE, Jail cannot be bought
	 */
	public boolean act(Player player) {
		return true;
	}
	
	/**
	 * Retrieves all the players currently incarcerated
	 *
	 * @returns		The prisoners currently in Jail
	 */
	public Player[] getPrisoners(){
		return prisoners;
	}
	
	/**
	 * Adds the newest inmate to Jail
	 *
	 * @param	p	The player being imprisoned
	 */
	public void addPrisoner(Player p){
		prisoners[p.getId()] = p;
	}
	
	/**
	 * Releases a specified inmate back into Oakland
	 *
	 * @param	p	The current resident being released
	 */
	public void removePrisoner(Player p){
		prisoners[p.getId()] = null;
	}
}
