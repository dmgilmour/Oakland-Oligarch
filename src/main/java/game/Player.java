package game;

import javax.swing.*;
import java.util.ArrayList;

/**
 * @author Woody
 *
 */
public class Player {

	private int id;
	private int money;
	private String name;
	private ArrayList<Property> properties;
	private JLabel token; //this can be changed to whatever we decide player tokens should be
	private int position; //current space the player is on
	private int oldPos;
	private boolean hasMoved;
	private boolean loser;

	public Player (int id, int money, String name, Property[] properties) {
		this.id = id;
		this.money = money;
		this.name = name;
		token = new JLabel(name);
		if(properties == null)
			this.properties = new ArrayList<Property>();
		else
		{
			this.properties = new ArrayList<Property>(properties.length);
			for(int i = 0; i < properties.length; i++)
				this.properties.add(properties[i]);
		}
		position = 0;
		hasMoved = true;
		oldPos = 0;
		loser = false;
	}

	public boolean getLoser(){
		return loser;
	}

	public void setLoser(boolean b){
		loser = b;
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

	public int getMoney() {
		return money;
	}

	public ArrayList<Property> getProperties() {
		return properties;
	}

	/**
	 * Adds a given property to this player's propertyList
	 *
	 * @param	property		the property to be added
	 * @returns					the success of adding this property
	 */
	public boolean addProperty(Property property) {
		if(properties.contains(property))
			return false;
		property.setOwner(this);
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
	public boolean buy(Property property) {
		if(property.getOwner() == null)			//check that the property is owned
		{
			int cost = property.getPrice();
			if(charge(cost))					//If the charge to this player is successful:
			{
				property.setOwner(this);		//Change the ownership of the property
				properties.add(property);		//Add the property to the list of those owned by this players
				return true;
			}
			else
				return false;
		}
		else
			return false;
	}

	/**
	 * Charges a money value to the account of this player
	 *
	 * @param	cost		An integer value indicating the cost incurred
	 * @returns				A boolean indicating the success of the transaction
	 */
	public boolean charge(int cost) {
		money -= cost;
		return true;
		/*if(money >= cost){
			money -= cost;
			return true;
		}
		else{
			return false;
		}*/
	}

	/**
	 * This player pays the rent on a given property
	 *
	 * @param	property	A Property that this player should pay rent on
	 * @return 				A boolean indicating the sucesss of the payment
	 */
	public boolean payRent(Property property) {
		int cost = property.getRent();
		Player owner = property.getOwner();
		boolean success = charge(cost);			//Attempt to charge this player
		if(success && owner != null)			//If the charge is successful:
			owner.getPaid(cost);				//then pay the owner of the property
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
}
