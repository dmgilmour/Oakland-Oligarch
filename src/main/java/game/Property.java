package game;

import javax.swing.*;

/**
 * @author David
 *
 */
public class Property extends Square{
    String name;
    int price;
    int rent;
    Player owner=null;

    public Property(String name, int price, int rent){
        this.name = name;
        this.price = price;
        this.rent = rent;
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
    
	public void act(Player player) {
		if(owner != null) {								//If this property is owned:
				player.payRent(this);					//Pay rent on the property and alert the player
				JOptionPane.showMessageDialog(null, player.getName()+ " pays $" + getRent() + "  to " + owner.getName());
		}
		else {		//The property is unowned
			int choice = JOptionPane.showConfirmDialog(null, "Would you like to buy " + getName() + "?", "Buy property?", JOptionPane.YES_NO_OPTION);
			if(choice == JOptionPane.YES_OPTION) {		//Ask of the player would like to purchase this property
				player.buy(this);
			}
		}
	}
}
