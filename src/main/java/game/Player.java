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
	
	public String getName() {
		return name;
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
		if(prop.getOwner() == null)
		{
			int cost = prop.getPrice();
			if(charge(cost))
			{
				prop.setOwner(this);
				properties.add(prop);
				return true;
			}
			else
				return false;
		}
		else
			return false;
	}
	
	private boolean charge(int cost) {
		if(money >= cost)
		{
			money -= cost;
			return true;
		}
		else
			return false;
	}
	
	public boolean payRent(Property property) {
		int cost = property.getRent();
		Player owner = property.getOwner();
		boolean success = charge(cost);
		if(success)
			owner.getPaid(cost);
		return success;
	}
	
	private void getPaid(int payment) {
		if(payment > 0)
			money += payment;
	}
}
