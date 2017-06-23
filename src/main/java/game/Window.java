package game;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author Dan
 *
 */
public class Window {

	private JFrame frame = new JFrame("Oakland Oligarchy");
	
	private TopPanel topPanel;
	private StatusPanel statusPanel;
	private ActionPanel actionPanel;
	private Board boardPanel;

	/**
	 * Initializes the UI window 
	 *
	 * @param	playerList		The list of Players in the game
	 * @param	properties		The list of Properties to be used for this match
	 * @param	boardPanel		The board that will be displayed in the window
	 */
	public Window(Player[] playerList, Property[] properties, Board boardPanel) {
		frame.setSize(1000, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;

		
		topPanel = new TopPanel();
		c.gridwidth = 2; // Span left panel and board
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 0.1;
		c.anchor = GridBagConstraints.PAGE_START;
		frame.add(topPanel, c);

		statusPanel = new StatusPanel(playerList);
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 1;
		c.weightx = 0.4;
		c.weighty = 0.45;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		frame.add(statusPanel, c);

		actionPanel = new ActionPanel();
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridx = 0;
		c.gridy = 2;
		c.weightx = 0.4;
		c.weighty = 0.45;
		c.anchor = GridBagConstraints.LAST_LINE_START;
		frame.add(actionPanel, c);

		c.gridwidth = 1;
		c.gridheight = 3;
		c.gridx = 1;
		c.gridy = 1;
		c.weightx = 1;
		c.weighty = 0.9;
		c.anchor = GridBagConstraints.CENTER;
		frame.add(boardPanel, c);

		frame.setVisible(true);
	}
	
	/**
	 * Refreshes the UI
	 */	
	public void update() {
		statusPanel.update();
		frame.setVisible(true);
	}
}
