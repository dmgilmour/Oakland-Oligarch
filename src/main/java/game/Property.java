package game;

/**
 * @author David
 *
 */
public class Property{
	String name;
	int price;
	int rent;
	Player owner=null;
	boolean mortgaged;

	public Property(String _name, int _price, int _rent){
		name = _name;
		price = _price;
		rent = _rent;
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

	public void setMortgaged(boolean _mortgaged) {
    		mortgaged = _mortgaged;
	}

	public boolean getMortgaged() {
		return mortgaged;
	}

    
}
