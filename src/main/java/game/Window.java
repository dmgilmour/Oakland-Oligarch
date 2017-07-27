package game;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;

/**
 * @author Dan Gilmour
 */
public class Window extends JFrame {

	private TopPanel topPanel;
	private StatusPanel statusPanel;
	private ActionPanel actionPanel;
	private BoardPanel boardPanel;

	/**
	 * Constructor of the UI window
	 *
	 * @param	squareList			The properties and other spaces making up the board
	 * @param	random				Randomizes the color scheme of the UI
	 * @param	bl					Buy button trigger
	 * @param	ml					Roll button trigger
	 * @param	el					End button trigger
	 * @param	time				Elapsed game time
	 * @param	ll					Load button trigger
	 * @param	sl					Save button trigger
	 * @param	mortgageListener	Mortgage button trigger
	 * @param	pl					Property info button trigger
	 * @param	jl					Pay button trigger for paying to escape jail
	 */

	public Window(Square[] squareList, Random random, ActionListener bl, ActionListener ml, ActionListener el, Time time, ActionListener ll, ActionListener sl,  ActionListener mortgageListener, ActionListener pl, ActionListener jl, ActionListener il, ActionListener asl) {

		boardPanel = new BoardPanel(squareList, pl);

		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		this.setUndecorated(true);

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		topPanel = new TopPanel(random, time, ll, sl, il, asl);
		c.gridwidth = 2;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0.1;
		c.anchor = GridBagConstraints.PAGE_START;
		this.add(topPanel, c);

		statusPanel = new StatusPanel(random, mortgageListener);
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0.4;
		c.weighty = 0.45;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		this.add(statusPanel, c);

		actionPanel = new ActionPanel(random, bl, ml, el, jl);
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0.4;
		c.weighty = 0.45;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		this.add(actionPanel, c);

		c.gridwidth = 1;
		c.gridheight = 3;
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 0.9;
		c.anchor = GridBagConstraints.CENTER;
		this.add(boardPanel, c);

		this.setVisible(true);
	}

	/**
	 * Initializes the status panel with the players and sets up trading
	 *
	 * @param	playerList		The players in this game
	 * @param	tradeListeners	Player button trigger, starts a trade between players
	 */
	public void setPlayers(Player[] playerList, ActionListener[] tradeListeners) {
		statusPanel.setPlayers(playerList, tradeListeners);
		for(Player p: playerList) {
			this.update(p);
		}
	}

	/**
	 * Refreshes the UI
	 *
	 * @param	p	Who's turn it is
	 */
	public void update(Player p) {
		statusPanel.update(p);
		boardPanel.update(p);

		if(p.isInJail()){
			this.enablePay();
		}
		else{
			this.hidePay();
		}

		setVisible(true);
	}


	/**
	 * Shows the winner of the game. Exits the game when "ok" is clicked.
	 *
	 * @param player	The winner of the game
	 */
	 public void endGame(Player player){
		 String winner = player.getName();
		 JOptionPane.showMessageDialog(null, winner + " has won the game.");
		 System.exit(0);
	 }

	 /**
	  * Shows a pop up of a loser of the game, to let all players know someone lost.
	  *
	  * @param player	The recent loser
	  */
	public void printLoser(Player player){
		String loser = player.getName();
		JOptionPane.showMessageDialog(null, loser + " has lost the game.");
	}

	/**
	 * Shows a pop-up JPane of how much a player owes.
	 *
	 * @param	player	The debtor
	 * @param	cost	The amount of money being charged
	 */
	public void printMortgage(Player player, int cost){
		JOptionPane.showMessageDialog(null, player.getName() + " needs $" + cost + " more.");
	}

	public void enableRoll() {
		actionPanel.rollButton.setEnabled(true);
	}

	public void disableRoll() {
		actionPanel.rollButton.setEnabled(false);
	}

	public void enableEnd() {
		actionPanel.endButton.setEnabled(true);
	}

	public void disableEnd() {
		actionPanel.endButton.setEnabled(false);
	}

	public void enableBuy() {
		actionPanel.buyButton.setEnabled(true);
	}

	public void disableBuy() {
		actionPanel.buyButton.setEnabled(false);
	}

	public void hidePay(){
		actionPanel.payButton.setEnabled(false);
		actionPanel.payButton.setVisible(false);
	}

	public void enablePay(){
		actionPanel.payButton.setEnabled(true);
		actionPanel.payButton.setVisible(true);
	}

	public void enableSave() {
		topPanel.saveButton.setEnabled(true);
	}

	public void disableSave() {
		topPanel.saveButton.setEnabled(false);
	}
}
