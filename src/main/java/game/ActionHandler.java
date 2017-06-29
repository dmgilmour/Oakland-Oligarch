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
	private final int UBER_COST = 50;
	private final int FRAT_DUES = 40;
	private final int STUDENT_ACTIVITY_FEE = 25;
	private final int SMELL_COMPLAINT = 32;
	
	public ActionHandler(Board b, Player[] pll, Random r) {
		random = r;
		board = b;
		playerList = pll;
	}
	
	public void run(Player p) {
		switch(random.nextInt(OaklandOligarchy.NUMBER_OF_ACTIONS)){
			case 0:
				sublet(p);
				break;
			case 1:
				summer();
				break;
			case 2:
				oweek(p);
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
			case 8:
				qdoba(p);
				break;
			case 9:
				uber(p);
				break;
			case 10:
				frat(p);
				break;
			case 11:
				studAct(p);
				break;
			case 12:
				rain();
				break;
			case 13:
				construction();
				break;
			default:
				System.err.println("Action Handler: default case");
				break;
		}
	}
	
	private void oweek(Player p) {
		Square randSquare = board.getSquare(random.nextInt(OaklandOligarchy.NUMBER_OF_TILES));
		while(!(randSquare instanceof Property) || !p.addProperty((Property)randSquare)) {
			randSquare = board.getSquare(random.nextInt(OaklandOligarchy.NUMBER_OF_TILES));
		}
		JOptionPane.showMessageDialog(null, "Orientation Week!\nFreshmen move in and " + p.getName() + "\ngains the property: " + ((Property)randSquare).getName());
	}
	
	private void summer() {
		String message = "Summer Break!!! (woo)\nStudent tenants move out and:\n";
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
		while(!(randSquare instanceof Property)) {
			randSquare = board.getSquare(random.nextInt(OaklandOligarchy.NUMBER_OF_TILES));
		}
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
		while(!(randSquare instanceof Property) || !p.addProperty((Property)randSquare)) {
			randSquare = board.getSquare(random.nextInt(OaklandOligarchy.NUMBER_OF_TILES));
		}
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
	
	private void uber(Player p) {
		p.charge(UBER_COST);
		JOptionPane.showMessageDialog(null, "Order an \"autonomous\" Uber\nYou pay $" + UBER_COST + "\nand take another turn!");
		OaklandOligarchy.switchPhase(OaklandOligarchy.GamePhase.START);
	}
	
	private void frat(Player p) {
		JOptionPane.showMessageDialog(null, "You rush a Fraternity...");
		switch(random.nextInt(5)) {
			case 0:
				ArrayList<Property> properties = p.getProperties();
				int size = properties.size();
				if(size > 0) {
					int randProp = random.nextInt(size);
					Property prop = p.removeProperty(randProp);
					JOptionPane.showMessageDialog(null, "...And the police come to your house party\n" + p.getName() + " loses the property: " + prop.getName());
					break;
				}
			case 1:
				Square randSquare = board.getSquare(random.nextInt(OaklandOligarchy.NUMBER_OF_TILES));
				while(!(randSquare instanceof Property) || !p.addProperty((Property)randSquare)) {
					randSquare = board.getSquare(random.nextInt(OaklandOligarchy.NUMBER_OF_TILES));
				}
				JOptionPane.showMessageDialog(null, "And move into the frat house!\n" + p.getName() + " gains the property: " + ((Property)randSquare).getName());
				break;
			case 2:
				p.charge(FRAT_DUES);
				JOptionPane.showMessageDialog(null, "...And need to pay your dues\n" + p.getName() + " pays $" + FRAT_DUES);
				break;
			case 3:
				String message = "...And all members pay you dues\n";
				for(Player player: playerList) {
					if(player != p) {
						if(player.charge(FRAT_DUES)) {
							p.getPaid(FRAT_DUES);
							message += player.getName() + " pays $" + FRAT_DUES;
						}
					}
				}
				JOptionPane.showMessageDialog(null, message);
				break;
			case 4:
				JOptionPane.showMessageDialog(null, "...And spend a night in Jail\n(choose your favorite misdemeanor)");
				break;
			default:
				System.err.println("Frat Method: default case");
				break;
		}
	}
	
	private void studAct(Player p) {
		int totalGain = 0;
		for(Property prop: p.getProperties()) {
			totalGain += STUDENT_ACTIVITY_FEE;
		}
		p.getPaid(totalGain);
		JOptionPane.showMessageDialog(null, "Student Activities Fee:\nCollect $" + STUDENT_ACTIVITY_FEE + " for each property you own\n" + p.getName() + " gains $" + totalGain);
	}
	
	private void rain() {
		JOptionPane.showMessageDialog(null, "Major Rainstorm!\nThe drainage system overflows into the river and tenants complain about the smell...");
		String message = "Pay twice the rent of your current tile plus $" + SMELL_COMPLAINT + "\n";
		for(Player player: playerList) {
			int charges = 0;
			Square s = board.getSquare(player.getPosition());
			if(s instanceof Property) {
				charges += ((Property)s).getRent() * 2;
			}
			charges += SMELL_COMPLAINT;
			if(player.charge(charges)){
				message += player.getName() + " pays $" + charges + "\n";
			}
		}
		JOptionPane.showMessageDialog(null, message);
	}
	
	private void construction() {
		int randInt = random.nextInt(OaklandOligarchy.NUMBER_OF_TILES);
		Square randSquare = board.getSquare(randInt);
		while(!(randSquare instanceof Property)) {
			randInt = random.nextInt(OaklandOligarchy.NUMBER_OF_TILES);
			randSquare = board.getSquare(randInt);
		}
		Property midProp = (Property)randSquare;
		int randInt2 = (randInt - 1) % OaklandOligarchy.NUMBER_OF_TILES;
		Square s = board.getSquare(randInt2);
		while(!(s instanceof Property)) {
			randInt2 = (randInt2 - 1) % OaklandOligarchy.NUMBER_OF_TILES;
			s = board.getSquare(randInt2);
		}
		Property prevProp = (Property)s;
		randInt = (randInt + 1) % OaklandOligarchy.NUMBER_OF_TILES;
		s = board.getSquare(randInt);
		while(!(s instanceof Property)) {
			randInt = (randInt2 + 1) % OaklandOligarchy.NUMBER_OF_TILES;
			s = board.getSquare(randInt);
		}
		Property nextProp = (Property)s;
		Player owner = prevProp.getOwner();
		if(owner != null) {
			owner.removeProperty(prevProp);
		}
		owner = midProp.getOwner();
		if(owner != null) {
			owner.removeProperty(midProp);
		}
		owner = nextProp.getOwner();
		if(owner != null) {
			owner.removeProperty(nextProp);
		}
		JOptionPane.showMessageDialog(null, "INDEFINITE Construction:\n" + prevProp.getName() + " becomes unowned\n" + midProp.getName() + " becomes unowned\n" + nextProp.getName() + " becomes unowned\n");
	}
	
	private void sublet(Player p) {
		int moveTo = random.nextInt(OaklandOligarchy.NUMBER_OF_TILES);
		Square randSquare = board.getSquare(moveTo);
		while(!(randSquare instanceof Property) || ((Property)randSquare).getOwner() == p) {
			moveTo = random.nextInt(OaklandOligarchy.NUMBER_OF_TILES);
			randSquare = board.getSquare(moveTo);
		}
		int moveAmount = moveTo - p.getPosition();
		if(moveAmount < 0) {
			moveAmount += OaklandOligarchy.NUMBER_OF_TILES;
		}
		board.movePlayer(p, moveAmount);
		Property randProp = (Property)randSquare;
		p.payRent(randProp);
		JOptionPane.showMessageDialog(null, "Become a subletter:\n" + p.getName() + " moves to " + randProp.getName() + "\nand pays $" + randProp.getRent());
	}
}