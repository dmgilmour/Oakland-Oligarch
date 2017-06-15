package game;

public class Property{
    String name;
    int price;
    int rent;
    int owner;

    public Property(String name, int price, int rent, int owner){
        this.name = name;
        this.price = price;
        this.rent = rent;
        this.owner = owner;
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

    public int getPropertyOwner(){
        return owner;
    }
}
