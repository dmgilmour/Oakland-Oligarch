package game;

import javax.swing.*;
import java.util.ArrayList;

/**
 * @author Woody
 *
 */
public class Player {

	int id;
	int money;
	String name;
	ArrayList<Property> properties;
	JLabel token; //this can be changed to whatever we decide player tokens should be
	int position; //current space the player is on

	public Player (int id, int money, String name, Property[] properties) {
		this.id = id;
		this.money = money;
		this.name = name;
		if(properties == null)
			this.properties = new ArrayList<Property>();
		else
		{
			this.properties = new ArrayList<Property>(properties.length);
			for(int i = 0; i < properties.length; i++)
				this.properties.add(properties[i]);
		}
		position=0;
	}
	
	public void setName(String n){
		name=n;
		token = new JLabel(name);
	}
	
	public JLabel getToken(){
		return token;
	}
	
	public int getPosition(){
		return position;
	}
	
	public void setPosition(int p){
		position=p;
	}
	
	public int getMoney() {
		return money;
	}
	
	public ArrayList<Property> getProperties() {
		return properties;
	}
	
	public boolean buy(Property prop) {
		if(prop.getPropertyOwner() == null)
		{
			int cost = prop.getPropertyPrice();
			if(charge(cost))
			{
				prop.setPropertyOwner(this);
				properties.add(prop);
				return true;
			}
			else
				return false;
		}
		else
			return false;
	}
	
	public boolean charge(int cost) {
		if(money >= cost)
		{
			money -= cost;
			return true;
		}
		else
			return false;
	}
}
