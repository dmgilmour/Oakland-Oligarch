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
    
    //setters for Property fields
    
    public void setOwner(Player p){
    	owner=p;
    }
    
}
