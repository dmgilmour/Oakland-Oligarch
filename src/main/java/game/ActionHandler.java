package game;

import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;

public class ActionHandler {
	private Player[] playerList;
	private Board board;
	private Random random;
	
	private final int COST_OF_WATER = 100;
	
	public ActionHandler(Board b, Player[] pll, Random r) {
		random = r;
		board = b;
		playerList = pll;
	}
	
	public void run(Player p) {
		switch(random.nextInt(OaklandOligarchy.NUMBER_OF_ACTIONS)){
			case 0:
				firstOfMonth(p);
				break;
			case 1:
				endOfTerm();
				break;
			case 2:
				beginTerm(p);
				break;
			case 3:
				badWeek(p);
				break;
			case 4:
				bacteria(p);
				break;
			default:
				System.err.println("Action Handler: default case");
				break;
		}
	}
	
	private void beginTerm(Player p) {
		Square randSquare = board.getSquare(random.nextInt(OaklandOligarchy.NUMBER_OF_TILES));
		while(!(randSquare instanceof Property) || !p.addProperty((Property)randSquare))
			randSquare = board.getSquare(random.nextInt(OaklandOligarchy.NUMBER_OF_TILES));
		JOptionPane.showMessageDialog(null, "Beginning of Term!\n" + p.getName() + " recieved property: " + ((Property)randSquare).getName());
	}
	
	private void endOfTerm() {
		String message = "End of Term!\n";
		for(Player player: playerList) {
			ArrayList<Property> properties = player.getProperties();
			int size = properties.size();
			if(size > 0) {
				int randProp = random.nextInt(size);
				Property prop = player.removeProperty(randProp);
				message += player.getName() + " lost " + prop.getName() + "\n";
			}
		}
		JOptionPane.showMessageDialog(null, message);
	}
	
	private void badWeek(Player p) {
		p.charge(p.getMoney());
		JOptionPane.showMessageDialog(null, "Have a bad week:\n" + p.getName() + " had Facebook hacked, got a bad haircut, lost Panther ID, laptop broke, and stress-ate\n" + "You are BROKE");
	}
	
	private void bacteria(Player p) {
		Square randSquare = board.getSquare(random.nextInt(OaklandOligarchy.NUMBER_OF_TILES));
		while(!(randSquare instanceof Property))
			randSquare = board.getSquare(random.nextInt(OaklandOligarchy.NUMBER_OF_TILES));
		Property prop = (Property)randSquare;
		JOptionPane.showMessageDialog(null, "Bacteria in the water!!!\n" + prop.getName() + " has safe water...");
		Player owner = prop.getOwner();
		if(owner == null) {
			int choice = JOptionPane.showConfirmDialog(null, "Would you like to buy " + prop.getName() + " for " + (prop.getPrice()*2) + "(twice the cost)?", "Buy property?", JOptionPane.YES_NO_OPTION);
			if(choice == JOptionPane.YES_OPTION) {
				p.buy(prop);
			}
		}
		else {
			String message = owner.getName() + " is selling water:\n";
			for(Player player: playerList) {
				if(player != owner) {
					player.charge(COST_OF_WATER);
					message += player.getName() + " paid $" + COST_OF_WATER + "\n";
				}
			}
			JOptionPane.showMessageDialog(null, message);
		}
	}
	
	private void firstOfMonth(Player p) {
		int total = 0;
		for(Property property: p.getProperties()) {
			total += property.getRent();
		}
		p.getPaid(total);
		JOptionPane.showMessageDialog(null, "First of the month!\nYou have been paid rent by all tenants\n" + p.getName() + " recieved $" + total);
	}
}