package game;

import javax.swing.*;

/**
 * @author David
 *
 */
public class Property extends Square{
	private String name;
	private int price;
	private int rent;
	private Player owner;
	private boolean mortgaged;

	public Property(String n, int p, int r){
		name = n;
		price = p;
		rent = r;
		owner = null;
    mortgaged = false;
	}

	public String getName(){
		return name;
	}

	public int getPrice(){
		return price;
	}

	public int getRent(){
		return rent;
	}

	public Player getOwner(){
		return owner;
	}

	public void setOwner(Player p){
		owner=p;
	}

	/**
	 * Performs the action on a property square
	 *
	 * @param	player		The player that this property is interacting with
	 * @return				A boolean determining if the "buy" button should be disabled or not
	 */
	public boolean act(Player player) {
		if(owner != null) {								//If this property is owned:
				player.payRent(this);					//Pay rent on the property and alert the player
				JOptionPane.showMessageDialog(null, player.getName()+ " pays $" + getRent() + "  to " + owner.getName());
				return true;
		}
		else {		//The property is unowned
			int choice = JOptionPane.showConfirmDialog(null, "Would you like to buy " + getName() + "?", "Buy property?", JOptionPane.YES_NO_OPTION);
			if(choice == JOptionPane.YES_OPTION) {		//Ask of the player would like to purchase this property
				player.buy(this);
				return true;
			}
			return false;
		}
	}

	public void setMortgaged(boolean _mortgaged) {
    		mortgaged = _mortgaged;
	}

	public boolean getMortgaged() {
		return mortgaged;
	}
}
