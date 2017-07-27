package game;

import javax.swing.*;
import java.util.ArrayList;

/**
 * @author Woodrow Fulmer
 */
public class Player {

	private int id;
	private int money;
	private String name;
	private ArrayList<Property> properties;
	private JLabel token;
	private int position;
	private int oldPos;
	private boolean hasMoved;
	private boolean loser;
	private int color;
	private boolean inJail;
	private int jailCounter;		//how many turns a player has been in jail
	private int doublesCounter;		//how many times a player has rolled doubles


	/**
	 * The constructor for Players
	 *
	 * @param	int		the unique ID number for this player
	 * @param	int		the starting money for this player
	 * @param	String	the name of this player
	 */
	public Player (int id, int money, String name) {
		this.id = id;
		this.money = money;
		this.name = name;
		token = new JLabel(name);
		properties = new ArrayList<Property>();
		position = 0;
		hasMoved = true;
		oldPos = 0;
		loser = false;
		jailCounter=0;
		inJail = false;
	}

	public boolean getLoser(){
		return loser;
	}

	public void setLoser(boolean b){
		loser = b;
		money = -1;
	}

	public int getOldPos() {
		return oldPos;
	}

	public void setMoved(boolean b) {
		hasMoved = b;
	}

	public boolean hasMoved() {
		return hasMoved;
	}

	public int getId() {
		return id;
	}
	public String getName() {
		return name;
	}

	public void setName(String n) {
		name = n;
		token = new JLabel(name);
	}

	public JLabel getToken(){
		return token;
	}

	public int getPosition(){
		return position;
	}

	public void setPosition(int p){
		oldPos = position;
		position = p;
		hasMoved = true;
	}

	public boolean moveDistance(int dist) {
		if (position + dist >= OaklandOligarchy.NUMBER_OF_TILES) {
			this.getPaid(OaklandOligarchy.GO_PAYOUT);
			this.setPosition((position + dist) % OaklandOligarchy.NUMBER_OF_TILES);
			return true;
		} else {
			this.setPosition(position + dist);
			return false;
		}
	}

	public void setMoney(int value){
		money = value;
	}

	public int getMoney() {
		return money;
	}

	public ArrayList<Property> getProperties() {
		return properties;
	}

	/**
	 * Adds a given property to this player's propertyList
	 *
	 * @param	property	the property to be added
	 * @return				the success of adding this property
	 */
	public boolean addProperty(Property property) {
		if(properties.contains(property)) {
			return false;
		}
		Player owner = property.getOwner();
		if(owner != null) {
			owner.removeProperty(property);
		}
		properties.add(property);
		property.setOwner(this);
		return true;
	}

	/**
	 * Removes a property from the Player's propertyList at a given index. Also sets the owner of property to this player.
	 *
	 * @param	index		The index to remove the property from
	 * @return				The property which has been removed, returns null if index was invalid
	 */
	public Property removeProperty(int index) {
		if(index >= properties.size()) {
			return null;
		}
		Property prop = properties.remove(index);
		prop.setOwner(null);
		return prop;
	}

	/**
	 * Removes a given property from the Player's propertyList. Also sets the owner of property to this player.
	 *
	 * @param	prop		The property that should be removed
	 * @return				The property which has been removed, returns null if index was invalid
	 */
	public Property removeProperty(Property prop) {
		if(!properties.contains(prop)) {
			return null;
		}
		prop.setOwner(null);
		properties.remove(prop);
		return prop;
	}

	/**
	 * Purchases a property for this player
	 *
	 * @param	property	A property that this player is buying
	 * @return 				A boolean indicating success of the purchase
	 */
	public boolean buy (Property property) {
		if (property.getOwner() == null && this.money >= property.getPrice())	{
			int cost = property.getPrice();
			if (canAfford(cost)) {
				charge(cost);
				return this.addProperty(property);
			}
		}
		return false;
	}

	public boolean canAfford(int cost) {
		return money >= cost;
	}

	/**
	 * Charges a money value to the account of this player
	 *
	 * @param	cost		An integer value indicating the cost incurred
	 * @returns				A boolean indicating the success of the transaction
	 */
	public int charge(int cost) {

		money -= cost;

		if (money < 0) {

			System.out.println("Cannot afford");
			System.out.println("Has: " + money);

			OaklandOligarchy.game.lose(this);
			return cost + money;

		} else {

			System.out.println("Can afford");
			System.out.println("Has: " + money);
			return cost;

		}
	}
			
			
	/**
	 * This player pays the rent on a given property, or as much as possible.
	 *
	 * @param	property	A Property that this player should pay rent on
	 * @return 				A boolean indicating the success of the payment
	 */
	public boolean payRent(Property property) {
		int cost = property.getRent();
		boolean success = canAfford(cost);

		Player owner = property.getOwner();
		owner.getPaid(charge(cost));
		
		return success;
	}

	/**
	 *	Deposits funds into this player's account
	 *
	 * @param	payment		An integer value that the player should receive
	 */
	public void getPaid(int payment) {
		if(payment > 0)
			money += payment;
	}

	public void setColor(int c) {
		color = c;
	}

	public int getColor() {
		return color;
	}

	public void goToJail(){
		inJail = true;
		this.setPosition(OaklandOligarchy.JAIL_POS);
	}

	public void leaveJail(){
		inJail = false;
		jailCounter = 0;
	}

	public boolean isInJail(){
		return inJail;
	}

	public void addToJailCounter(){
		jailCounter++;
	}

	public int getJailCounter(){
		return jailCounter;
	}

	public void resetJailCounter(){
		jailCounter=0;
	}

	/**
	 * @return	returns the doubles counter after adding 1
	 */
	public int addToDoublesCounter(){
		return ++doublesCounter;
	}

	public void resetDoublesCounter(){
		doublesCounter = 0;
	}
}
