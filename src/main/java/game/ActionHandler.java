package game;

import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;

public class ActionHandler {
	private Player[] playerList;
	private Board board;
	private Random random;
	
	private final int COST_OF_WATER = 100;
	private final int TAPINGO_FEE = 100;
	private final int QDOBA_DAMAGES = 500;
	
	public ActionHandler(Board b, Player[] pll, Random r) {
		random = r;
		board = b;
		playerList = pll;
	}
	
	public void run(Player p) {
		switch(random.nextInt(OaklandOligarchy.NUMBER_OF_ACTIONS)){
			case 0:
				qdoba(p);
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
			case 5:
				firstOfMonth(p);
				break;
			case 6:
				tapingo(p);
				break;
			case 7:
				significantOther(p);
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
	
	private void tapingo(Player p) {
		for(Player player: playerList) {
			if(player != p) {
				if(player.charge(TAPINGO_FEE)) {
					p.getPaid(TAPINGO_FEE);
				}
			}
		}
		JOptionPane.showMessageDialog(null, "Work for Tapingo delievery:\nAll players pay " + p.getName() + " $" + TAPINGO_FEE);
	}
	
	private void significantOther(Player p) {
		Square randSquare = board.getSquare(random.nextInt(OaklandOligarchy.NUMBER_OF_TILES));
		while(!(randSquare instanceof Property) || !p.addProperty((Property)randSquare))
			randSquare = board.getSquare(random.nextInt(OaklandOligarchy.NUMBER_OF_TILES));
		p.charge(p.getMoney());
		JOptionPane.showMessageDialog(null, "You found a significant other!\n" + p.getName() + " gained " + ((Property)randSquare).getName() + ",\nbut lost all money");
	}
	
	private void qdoba(Player p) {
		p.charge(QDOBA_DAMAGES);
		for(Player player: playerList) {
			if(player != p) {
				player.getPaid(QDOBA_DAMAGES / 10);
			}
		}
		JOptionPane.showMessageDialog(null, "To impress a girl\nyou try to jump the gap\nin the roof of Qdoba...\nand fail!");
		JOptionPane.showMessageDialog(null, p.getName() + " pays $" + QDOBA_DAMAGES + "\nAll other plays recieve a $" + (QDOBA_DAMAGES / 10) + " gift card\nwhen it reopens");
	}
}