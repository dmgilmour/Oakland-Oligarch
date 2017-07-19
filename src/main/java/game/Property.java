package game;

import javax.swing.*;

/**
 * @author David Haskell
 */
public class Property extends Square{
	private int price;
	private int rent;
	private Player owner;
	private boolean mortgaged;
	private JLabel ownerToken;

	/**
	 * Constructor for property object. Initializes name, pice, rent, owner,
	 * mortgaged, and ownerToken.
	 * name, price, and rent are parameters passed when the constructor is called.
	 * owner, mortgaged, and ownerToken are Initialized to null, false, and null.
	 * @param n the name of property
	 * @param p the price of property
	 * @param r the rent of property
	 */
	public Property(String n, int p, int r){
		name = n;
		price = p;
		rent = r;
		owner = null;
		mortgaged = false;
		ownerToken = null;
	}

	/**
	 * Returns the name of a property as a String.
	 * @return 		name of property.
	 */
	public String getName(){
		return name;
	}

	/**
	 * Returns the price of a property as an integer.
	 * @return 		price of property.
	 */
	public int getPrice(){
		return price;
	}

	/**
	 * Returns the rent of a property as an integer.
	 * @return 		rent of property.
	 */
	public int getRent(){
		return rent;
	}

	/**
	 * Returns the owner of a property as a Player object.
	 * @return 		owner of property.
	 */
	public Player getOwner(){
		return owner;
	}

	/**
	 * Returns the ownerToken of the property as a JLabel.
	 * @return 		ownerToken of property.
	 */
	public JLabel getOwnerToken() {
		return ownerToken;
	}

	/**
	 * Sets the owner and ownerToken of a property.
	 * @param p Player to be set as owner of property.
	 */
	public void setOwner(Player p){
		owner=p;
		if(owner == null) {
			ownerToken = null;
		}
		else {
			ownerToken = new JLabel("P"+owner.getId());
		}
	}

	/**
	 * Performs the action on a property square. If the property is owned, this makes the player pay rent
	 * on the property and is alerted. If the property is unowned, the player is prompted to purchase this property.
	 *
	 * @param	player		The player that this property is interacting with
	 * @return				Returns true if the "buy" button should be disabled
	 */
	public boolean act(Player player) {
		if(owner != null) {
			if(owner == player) {
				return true;
			}
			if (this.mortgaged) {
				JOptionPane.showMessageDialog(null, "Mortgaged property");
			} else {
				player.payRent(this);
				JOptionPane.showMessageDialog(null, player.getName()+ " pays $" + getRent() + " to " + owner.getName());
			}
			return true;
		} else {
			// If too poor, do not prompt to buy and disable button
			if (player.getMoney() < price) {
				return true;
			}

			int choice = JOptionPane.showConfirmDialog(null, "Would you like to buy " + getName() + "?", "Buy property?", JOptionPane.YES_NO_OPTION);
			if (choice == JOptionPane.YES_OPTION) {
				player.buy(this);
				return true;
			}
			return false;
		}
	}

  
	/**
	 * Will set the property to unmortgaged and charge player half the price
	 *
	 * @param 	property	the property the player is attempting to unmortgage
	 */
  	 public void unmortgage() {
		if (mortgaged) {
			if (owner.getMoney() >= price) {
				mortgaged = false;
				owner.charge(price);
			}
		}
	}
	

	/**
	 * Will set the property to mortgaged and give the player have the price
	 *
	 * @param 	property	the property the player is attempting to mortgage
	 */
	public void mortgage() {
		if (!mortgaged) {
			mortgaged = true;
			owner.getPaid(price / 2);
		}
	}

	/**
	 * Returns mortgaged boolean of property.
	 * @return 		returns if the property is mortgaged(true) or not(false).
	 */
	public boolean getMortgaged() {
		return mortgaged;
	}

	public void setMortgaged(boolean m) {
		mortgaged = m;
	}
}
