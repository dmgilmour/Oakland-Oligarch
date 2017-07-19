package game;

import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;

/**
 * @author Woodrow Fulmer
 */
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
	private final int OWNS_PROPERTY_ACTIONS = 2;
	
	/**
	 * The constructor for the ActionHandler
	 *
	 * @param	b		The board this game is played on
	 * @param	pll		The list of players in this game
	 * @param	r		A psuedo-random number generator used in this game
	 */
	public ActionHandler(Board b, Player[] pll, Random r) {
		random = r;
		board = b;
		playerList = pll;
	}
	
	/**
	 * Performs a random action from the list upon the give player
	 *
	 * @param	p		The player this action should effect
	 */
	public void run(Player p) {
		boolean ownsProperty = p.getProperties().size() > 0;
		int randInt = random.nextInt(OaklandOligarchy.NUMBER_OF_ACTIONS);
		if(!ownsProperty && randInt >= OaklandOligarchy.NUMBER_OF_ACTIONS - OWNS_PROPERTY_ACTIONS) {
			randInt = random.nextInt(OaklandOligarchy.NUMBER_OF_ACTIONS - OWNS_PROPERTY_ACTIONS);
		}
		switch(randInt){
			case 0:
				construction();
				break;
			case 1:
				oweek(p);
				break;
			case 2:
				summer();
				break;
			case 3:
				badWeek(p);
				break;
			case 4:
				bacteria(p);
				break;
			case 5:
				rain();
				break;
			case 6:
				tapingo(p);
				break;
			case 7:
				sublet(p);
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
				significantOther(p);
				break;
			//Properties involving the loss of p's property should be at end of switch-case
			//This solves the issue of these actions doing nothing when the player does not own anything
			case 12:
				firstOfMonth(p);
				break;
			case 13:
				studAct(p);
				break;
			default:
				System.err.println("Action Handler: default case");
				break;
		}
	}
	
	/**
	 * Gives a random property (owned or unowned) to the given player
	 *
	 * @param	p		The player this action should effect
	 */
	private void oweek(Player p) {
		Square randSquare = board.getSquare(random.nextInt(OaklandOligarchy.NUMBER_OF_TILES));
		while(!(randSquare instanceof Property) || !p.addProperty((Property)randSquare)) {
			randSquare = board.getSquare(random.nextInt(OaklandOligarchy.NUMBER_OF_TILES));
		}
		JOptionPane.showMessageDialog(null, "Orientation Week!\nFreshmen move in and " + p.getName() + "\ngains the property: " + ((Property)randSquare).getName());
	}
	
	/**
	 * Removes a random property from all players
	 */
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
			else {
				message += player.getName() + " did not have a property to lose\n";
			}
		}
		JOptionPane.showMessageDialog(null, message);
	}
	
	/**
	 * Removes all money from a given player
	 *
	 * @param	p		The player this action should effect
	 */
	private void badWeek(Player p) {
		p.charge(p.getMoney());
		JOptionPane.showMessageDialog(null, "Have a bad week:\n" + p.getName() + " had Facebook hacked, got a bad haircut, lost Panther ID, laptop broke, and stress-ate\n" + "You are BROKE");
	}
	
	/**
	 * Selects a random property. If it is owned, all players pay the owner. If it is unowned, allows the given player to buy it for twice the cost.
	 *
	 * @param	p		The player this action should effect
	 */
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
					if(player.charge(COST_OF_WATER)) {
						message += player.getName() + " paid $" + COST_OF_WATER + "\n";
					}
				}
			}
			JOptionPane.showMessageDialog(null, message);
		}
	}
	
	/**
	 * Gives the given player a sum of money equal to the rent of all properties owned
	 *
	 * @param	p		The given player this action should effect
	 */
	private void firstOfMonth(Player p) {
		int total = 0;
		for(Property property: p.getProperties()) {
			total += property.getRent();
		}
		p.getPaid(total);
		JOptionPane.showMessageDialog(null, "First of the month!\nYou have been paid rent by all tenants\n" + p.getName() + " recieved $" + total);
	}
	
	/**
	 * All players pay the given player
	 *
	 * @param	p		The given player this action should effect
	 */
	private void tapingo(Player p) {
		for(Player player: playerList) {
			if(player != p) {
				if(player.charge(TAPINGO_FEE)) {
					p.getPaid(TAPINGO_FEE);
				}
			}
		}
		JOptionPane.showMessageDialog(null, "Work for Tapingo delivery:\nAll players pay " + p.getName() + " $" + TAPINGO_FEE);
	}
	
	/**
	 * The given player gets a random property and loses all money
	 *
	 * @param	p		The player this action should effect
	 */
	private void significantOther(Player p) {
		Square randSquare = board.getSquare(random.nextInt(OaklandOligarchy.NUMBER_OF_TILES));
		while(!(randSquare instanceof Property) || !p.addProperty((Property)randSquare)) {
			randSquare = board.getSquare(random.nextInt(OaklandOligarchy.NUMBER_OF_TILES));
		}
		p.charge(p.getMoney());
		JOptionPane.showMessageDialog(null, "You found a significant other!\n" + p.getName() + " gained " + ((Property)randSquare).getName() + ",\nbut lost all money");
	}
	
	/**
	 * Given player pays an amount and all other players are paid a fraction of that amount
	 *
	 * @param	p		The player this action should effect
	 */
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
	
	/**
	 * Given player pays an amount and takes an additional turn
	 *
	 * @param	p		The player this action should effect
	 */
	private void uber(Player p) {
		int choice = JOptionPane.showConfirmDialog(null, "Order an \"autonomous\" Uber:\nWould you like to pay " + UBER_COST + "\nand take another turn?");
		if(choice == JOptionPane.YES_OPTION) {
			p.charge(UBER_COST);
			OaklandOligarchy.switchPhase(OaklandOligarchy.GamePhase.START, null);
		}
	}
	
	/**
	 * Chooses a random action for a given player from:
	 *  -Lose a random property
	 *  -Gain a random property
	 *  -Pay a set amount
	 *  -All players pay you a set amount
	 *  -Go to Jail
	 *
	 * @param	p		The player this action should effect
	 */
	private void frat(Player p) {
		JOptionPane.showMessageDialog(null, "You rush a Fraternity...");
		ArrayList<Property> properties = p.getProperties();
		int size = properties.size();
		int rand = random.nextInt(5);
		if(size <= 0 && rand >= 4)
			rand = random.nextInt(4);
		switch(rand) {
			case 0:
				JOptionPane.showMessageDialog(null, "...And spend a night in Jail\n(choose your favorite misdemeanor)");
				p.goToJail();
				break;
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
				int randProp = random.nextInt(size);
				Property prop = p.removeProperty(randProp);
				JOptionPane.showMessageDialog(null, "...And the police come to your house party\n" + p.getName() + " loses the property: " + prop.getName());
				break;
			default:
				System.err.println("Frat Method: default case");
				break;
		}
	}
	
	/**
	 * Given player collects an amount for each property he/she owns
	 *
	 * @param	p		The player this action should effect
	 */
	private void studAct(Player p) {
		int totalGain = 0;
		for(Property prop: p.getProperties()) {
			totalGain += STUDENT_ACTIVITY_FEE;
		}
		p.getPaid(totalGain);
		JOptionPane.showMessageDialog(null, "Student Activities Fee:\nCollect $" + STUDENT_ACTIVITY_FEE + " for each property you own\n" + p.getName() + " gains $" + totalGain);
	}
	
	/**
	 * All players pay twice the rent of the tile they currently occupy (regardles of owner) plus an amount
	 */
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
	
	/**
	 * A random 3 consectutive tiles become unowned
	 */
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
			randInt = (randInt + 1) % OaklandOligarchy.NUMBER_OF_TILES;
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
	
	/**
	 * A given player moves to a random property that they do not own and pays rent on that property
	 *
	 * @param	p		The player this action should effect
	 */
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
		p.moveDistance(moveAmount);
		Property randProp = (Property)randSquare;
		p.payRent(randProp);
		JOptionPane.showMessageDialog(null, "Become a subletter:\n" + p.getName() + " moves to " + randProp.getName() + "\nand pays $" + randProp.getRent());
	}
}
