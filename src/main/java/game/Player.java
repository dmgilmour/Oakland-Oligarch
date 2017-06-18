package game;

import javax.swing.*;

/**
 * @author Woody
 *
 */
public class Player {

	int id;
	int money;
	String name;
	String[] properties;
	JLabel token; //this can be changed to whatever we decide player tokens should be
	int position; //current space the player is on

	public Player (int id, int money, String name, String[] properties) {
		this.id = id;
		this.money = money;
		this.name = name;
		this.properties = properties;
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
}
