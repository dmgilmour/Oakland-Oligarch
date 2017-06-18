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

    public Property(String name, int price, int rent){
        this.name = name;
        this.price = price;
        this.rent = rent;
    }
    //getters for all the Property fields
    public String getPropertyName(){
        return name;
    }

    public int getPropertyPrice(){
        return price;
    }

    public int getPropertyRent(){
        return rent;
    }

    public Player getPropertyOwner(){
        return owner;
    }
    
    //setters for Property fields
    
    public void setPropertyOwner(Player p){
    	owner=p;
    }
    
}
