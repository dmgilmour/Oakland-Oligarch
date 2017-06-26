package game;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

/**
 * @author Dan
 *
 */
public class Window extends JFrame {

	private final int height = 980;
	private final int width = 1820;
	
	private Game game;
	private Player[] playerList;

	private TopPanel topPanel;
	private StatusPanel statusPanel;
	private ActionPanel actionPanel;
	private BoardPanel boardPanel;

	/**
	 * Initializes the UI window 
	 *
	 * @param	playerList		The list of Players in the game
	 * @param	properties		The list of Properties to be used for this match
	 * @param	boardPanel		The board that will be displayed in the window
	 */

	public Window(Property[] propertyList, Random random) {

		boardPanel = new BoardPanel(propertyList);


		this.setSize(width, height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		
		topPanel = new TopPanel(random);
		c.gridwidth = 2; // Span left panel and board
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

		actionPanel = new ActionPanel(random);
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

	public void setPlayers(Player[] _playerList) {
		playerList = _playerList;
		statusPanel.setPlayers(playerList);
		this.update();
	}
	
	/**
	 * Refreshes the UI
	 */	
	public void update() {
		statusPanel.update();
		for(Player p: playerList) {
			boardPanel.update(p);
		}
		setVisible(true);
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

}
