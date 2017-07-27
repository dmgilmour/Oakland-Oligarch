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
	private boolean transport;

	/**
	 * Constructor for property object. Initializes name, price, rent, owner,
	 * 		mortgaged, and ownerToken. Owner, Mortgaged, and OwnerToken are 
	 *		initialized to null, false, and null, respectively.
	 *
	 * @param n	 	the name of property
	 * @param p		the price of property
	 * @param r		the rent of property
	 */
	public Property(String n, int p, int r){
		name = n;
		price = p;
		rent = r;
		owner = null;
		mortgaged = false;
		ownerToken = null;
		transport = false;
	}

	/**
	 * Returns the name of a property.
	 *
	 * @return		The name of this property
	 */
	public String getName(){
		return name;
	}

	/**
	 * Returns the price of a property.
	 *
	 * @return		The price of this property
	 */
	public int getPrice(){
		return price;
	}

	/**
	 * Returns the rent of a property.
	 *
	 * @return		The rent of this property
	 */
	public int getRent(){
		return rent;
	}

	/**
	 * Returns the owner of a property as a Player object.
	 * @return		The owner of this property
	 */
	public Player getOwner(){
		return owner;
	}

	/**
	 * Returns the token for the owner of this property.
	 * @return		Token for the owner of this property
	 */
	public JLabel getOwnerToken() {
		return ownerToken;
	}

	/**
	 * Sets the owner and token for this property.
	 *
	 * @param	p 	The new owner of this property
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
	 * @param	player	The player that this property is interacting with
	 * @return			Returns true if the "buy" button should be disabled
	 */
	public boolean act(Player player) {
		if(owner != null && !owner.equals(player)) {
			if (this.mortgaged) {
				JOptionPane.showMessageDialog(null, "Mortgaged property");
			} else {
				player.payRent(this);
				JOptionPane.showMessageDialog(null, player.getName()+ " pays $" + getRent() + " to " + owner.getName());
			}
			return true;
		}
		else {
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
	 * Will set the property to unmortgaged and charge player for the price
	 */
  	 public void unmortgage() {
		if (mortgaged) {
			if (owner.getMoney() >= price) {
				mortgaged = false;
				owner.charge(price);
				owner.addWorth(price / 2);
			}
		}
	}


	/**
	 * Will set the property to mortgaged and give the player half the price
	 */
	public void mortgage() {
		if (!mortgaged) {
			mortgaged = true;
			owner.gainMortgageValue(price / 2);
		}
	}

	/**
	 * Returns mortgaged boolean of property.
	 *
	 * @return	TRUE if the property is mortgaged, else false
	 */
	public boolean getMortgaged() {
		return mortgaged;
	}

	/**
	 * Sets the mortgaged value for this property
	 *
	 * @param	The truth value to set the mortgage state to
	 */
	public void setMortgaged(boolean m) {
		mortgaged = m;
	}
	
	/**
	 * Returns whether or not this is a transport property.
	 * @return	True if it is a transport property.
	 */
	public boolean isTransport(){
		return transport;
	}
}
