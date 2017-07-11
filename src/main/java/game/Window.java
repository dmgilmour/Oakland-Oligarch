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

	private final int height = 980;
	private final int width = 1820;

	private TopPanel topPanel;
	private StatusPanel statusPanel;
	private ActionPanel actionPanel;
	private BoardPanel boardPanel;

	/**
	 * Constructor of the UI window
	 *
	 * @param	squareList		The list of squares to be used in this game
	 * @param	random			A seeded psuedo-random number generator used to stylize the UI
	 * @param	bl				An ActionListener for the Buy phase of a turn
	 * @param	ml				An ActionListener for the Move phase of a turn
	 * @param	el				An ActionListener for the End phase of a turn
	 */
	public Window(Square[] squareList, Random random, ActionListener bl, ActionListener ml, ActionListener el, Time time, ActionListener ll, ActionListener sl) {

		boardPanel = new BoardPanel(squareList);

		this.setSize(width, height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		topPanel = new TopPanel(random, time, ll, sl);
		c.gridwidth = 2;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0.1;
		c.anchor = GridBagConstraints.PAGE_START;
		this.add(topPanel, c);

		statusPanel = new StatusPanel(random);
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0.4;
		c.weighty = 0.45;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		this.add(statusPanel, c);

		actionPanel = new ActionPanel(random, bl, ml, el);
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

	public void setPlayers(Player[] playerList, ActionListener[] tradeListeners) {
		statusPanel.setPlayers(playerList, tradeListeners);
		for(Player p: playerList) {
			this.update(p);
		}
	}

	/**
	 * Refreshes the UI
	 */
	public void update(Player p) {
		OaklandOligarchy.setStatusProperties(p);
		statusPanel.update();
		boardPanel.update(p);
		setVisible(true);
	}

	/**
	 * Shows the winner of the game. Exits the game when "ok" is clicked.
	 * @param player the winning Player of the game to be printed out as the winner.
	 */
	 public void endGame(Player player){
		 String winner = player.getName();
		 JOptionPane.showMessageDialog(null, winner + " has won the game.");
		 System.exit(0);
	 }

	 /**
	  * Shows a pop up of a loser of the game, to let all players know someone lost.
	  * @param player loser of the game to be announced.
	  */
	public void printLoser(Player player){
		String loser = player.getName();
		JOptionPane.showMessageDialog(null, loser + " has lost the game.");
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

	public void updateStatusProperties(ArrayList<Property> properties, ActionListener[] mortgageListeners) {
		statusPanel.updateStatusProperties(properties, mortgageListeners);
	}

}
