package game;

import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.IOException;

/**
 * @author Dan
 *
 */
public class Game {

	private int playerTurn;
	private int num_players;
	private int active_players;


	private Board board;
	private Window window;
	private Player[] playerList;
	private ActionHandler actionHandler;

	/**
	 * Initializes the Game object
	 *
	 * @param	_playerList		The array of players in this game
	 * @param	squareList		The array of squares to be used in this game
	 * @param	w				The window this game is running in
	 */
	public Game(Player[] _playerList, Square[] squareList, Window w, Random random, int pt) {
		playerList = _playerList;
		board = new Board(squareList);
		window = w;
		actionHandler = new ActionHandler(board, playerList, random);
		playerTurn = pt;
		num_players = playerList.length;
		active_players = num_players;
		for (Player p : playerList) {
			if (p.getLoser()) {
				active_players--;
			}
		}
		winCheck();
	}

	/**
	 * Returns the value of playerTurn.
	 * @return 		playerTurn as an integer.
	 */
	public int getTurn() {
		return playerTurn;
	}

	/**
	 * Returns the number of players in the game.
	 * @return 		num_players as an integer.
	 */
	public int getNumPlayers(){
		return num_players;
	}

	/**
	 * Returns the list of Players.
	 * @return 		playerList as an array of Player objects.
	 */
	public Player[] getPlayers(){
		return playerList;
	}

	/**
	 * Returns the Player whose current turn it is.
	 * @return 		playerList[playerTurn] as a Player object.
	 */
	public Player getCurrentPlayer() {
		return playerList[playerTurn];
	}

	/**
	 * Runs the game phase for the start of a turn during which a player can click info
	 * buttons and roll the dice giant button
	 */
	public void startPhase() {
		window.update(this.getCurrentPlayer());
		window.disableEnd();
		window.disableBuy();
		window.enableRoll();
		window.enableSave();
		window.update(this.getCurrentPlayer());
	}


	/**
	 * Runs the game phase during which players roll and move
	 */
	public void movePhase() {
		window.disableSave();
		if (!this.getCurrentPlayer().isInJail()){	//if the player is not in jail take turn as normally
			int roll[] = roll(System.currentTimeMillis());
			boolean collectGoMoney;
			collectGoMoney = this.getCurrentPlayer().moveDistance(roll[0] + roll[1]);

			String squareName = board.getSquare(this.getCurrentPlayer().getPosition()).getName();
			String message = "You rolled a " + roll[0] + " and a " + roll[1] + " and landed on " + squareName;
			if (roll[0] == roll[1]) {
				message += "\nYou got doubles!";
				if (this.getCurrentPlayer().addToDoublesCounter() == 3){
					this.getCurrentPlayer().goToJail();
					window.update(this.getCurrentPlayer());
					message += "\nYou got 3 doubles in a row so you go to jail.";
					JOptionPane.showMessageDialog(null, message);
					endPhase();
					return;
				}
			}
			if (collectGoMoney) {
				message += "\nYou passed go and collected " + OaklandOligarchy.GO_PAYOUT;
			}
			window.update(this.getCurrentPlayer());
			JOptionPane.showMessageDialog(null, message);
			if (roll[0] != roll[1]) {
				window.disableRoll();
				window.enableEnd();
			} else {
				window.enableRoll();
				window.disableEnd();
			}
			actionPhase();
		} else {	//the player is currently in jail
			int roll[] = roll(System.currentTimeMillis());

			String message = "You rolled a " + roll[0] + " and a " + roll[1];
			if (roll[0] == roll[1]){
				this.getCurrentPlayer().leaveJail();
				boolean collectGoMoney;
				collectGoMoney = this.getCurrentPlayer().moveDistance(roll[0] + roll[1]);

				String squareName = board.getSquare(this.getCurrentPlayer().getPosition()).getName();
				message += "\nYou got doubles, left jail, and landed on" + squareName;
				if (collectGoMoney) {
					message += "\nYou passed go and collected " + OaklandOligarchy.GO_PAYOUT;
				}
				window.disableRoll();
				window.enableEnd();
				window.update(this.getCurrentPlayer());
				JOptionPane.showMessageDialog(null, message);
				actionPhase();
			} else {
				this.getCurrentPlayer().addToJailCounter();
				message += "\nYou've lost " + this.getCurrentPlayer().getJailCounter() + " turn(s) lost in jail.";
				JOptionPane.showMessageDialog(null, message);
				if(this.getCurrentPlayer().getJailCounter()==OaklandOligarchy.MAX_JAIL_TURNS){
					this.getCurrentPlayer().charge(OaklandOligarchy.JAIL_COST);
					this.getCurrentPlayer().leaveJail();
					window.disableRoll();
					window.enableEnd();
					window.update(this.getCurrentPlayer());
				}
				endPhase();
			}

		}
	}

	/**
	 * Runs the game phase that ends each players turn
	 */
	public void endPhase() {

		Square curSquare = board.getSquare(getCurrentPlayer().getPosition());
		if (curSquare instanceof Property) {
			if (((Property) curSquare).getOwner() == null) {
				auctionPhase();
			}
		}
		this.getCurrentPlayer().resetDoublesCounter();
		playerTurn = (playerTurn + 1) % num_players;	//Increment to the next player's turn
		if (playerList[playerTurn].getLoser() == false) {
			JOptionPane.showMessageDialog(null, this.getCurrentPlayer().getName() + "'s turn");
			startPhase();
		} else {
			endPhase();
		}
	}

	/**
	 * Runs the game phase where the player performs an action based on the tile they are on
	 */
	public void actionPhase() {

		Player player = this.getCurrentPlayer();
		Square square = board.getSquare(player.getPosition());
		if (square == null) {									//Check to ensure that a tile was retrieved properly from the board
			return;
		}
		// Either charges or prompts player to purchase depending on whether
		// it is owned or not
		boolean cannotBuy = square.act(player);
		if (!cannotBuy) {
			window.enableBuy();
		}
		if (square instanceof ActionSquare) {
			actionHandler.run(player);
		}
		for(Player p: playerList){
			if(p.isWinner()){
				window.endGame(p);
			}
		}
		window.update(player);
	}

	/**
	 * Runs the game phase where the player can purchase a property
	 */
	public void buyPhase() {
		Player player = this.getCurrentPlayer();
		Square square = board.getSquare(player.getPosition());
		if (square.act(player)) {
			window.disableBuy();
		}
		if(player.isWinner()){
			window.endGame(player);
		}
		window.update(player);
	}

	/**
	 * Runs the game phase where the player can trade properties with other players
	 *
	 * @param	tradee		the player the current player is trading with
	 * @returns			a boolean for whether the trade was successful
	 */
	public void tradePhase(Player tradee) {
		tradePhase(getCurrentPlayer(), tradee);
	}
		
	public void tradePhase(Player trader, Player tradee) {

		JDialog tradeDialog = new JDialog(window, "Trade", true);

		GridBagConstraints constraints = new GridBagConstraints();

		JPanel tradePanel = new JPanel();
		tradePanel.setLayout(new GridBagLayout());

		JLabel traderName = new JLabel(trader.getName());
		JLabel tradeeName = new JLabel(tradee.getName());

		traderName.setHorizontalAlignment(JLabel.CENTER);
		tradeeName.setHorizontalAlignment(JLabel.CENTER);

		traderName.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
		tradeeName.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));


		String[] traderProperties = new String[trader.getProperties().size()];
		for (int i = 0; i < trader.getProperties().size(); i++) {
			traderProperties[i] = trader.getProperties().get(i).getName();
		}

		JList traderList = new JList(traderProperties);

		String[] tradeeProperties = new String[tradee.getProperties().size()];
		for (int i = 0; i < tradee.getProperties().size(); i++) {
			tradeeProperties[i] = tradee.getProperties().get(i).getName();
		}
		JList tradeeList = new JList(tradeeProperties);



		int max = 0;
		if (trader.getMoney() > 0) {
			max = trader.getMoney();
		} else {
			max = 0;
		}
		SpinnerModel traderSpinnerModel = new SpinnerNumberModel(0, 0, max, 1);
		JSpinner traderMoneySpinner = new JSpinner(traderSpinnerModel);
		if (tradee.getMoney() > 0) {
			max = tradee.getMoney();
		} else {
			max = 0;
		}
		SpinnerModel tradeeSpinnerModel = new SpinnerNumberModel(0, 0, max, 1);
		JSpinner tradeeMoneySpinner = new JSpinner(tradeeSpinnerModel);


		constraints.insets = new Insets(10,30,10,30);

		constraints.gridx = 0;
		constraints.gridy = 0;
		tradePanel.add(traderName, constraints);
		constraints.gridx = 1;
		constraints.gridy = 0;
		tradePanel.add(tradeeName, constraints);

		constraints.gridx = 0;
		constraints.gridy = 1;
		tradePanel.add(traderList, constraints);
		constraints.gridx = 1;
		constraints.gridy = 1;
		tradePanel.add(tradeeList, constraints);

		constraints.gridx = 0;
		constraints.gridy = 2;
		tradePanel.add(traderMoneySpinner, constraints);
		constraints.gridx = 1;
		constraints.gridy = 2;
		tradePanel.add(tradeeMoneySpinner, constraints);


		JButton submit = new JButton("Submit Trade");
		constraints.gridx = 0;
		constraints.gridy = 3;
		constraints.gridwidth = 2;
		tradePanel.add(submit, constraints);

		tradeDialog.getContentPane().add(tradePanel);
		tradeDialog.pack();

		submit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int dialogResult = JOptionPane.showConfirmDialog (null, tradee.getName() + ": Do you accept this trade?" , "Confirm Trade", JOptionPane.YES_NO_OPTION);
				if (dialogResult == JOptionPane.YES_OPTION) {

					int traderProfit = (Integer) tradeeMoneySpinner.getValue() - (Integer) traderMoneySpinner.getValue();



					int[] temp = traderList.getSelectedIndices();
					Property[] traderProperties = new Property[temp.length];
					for (int i = 0; i < traderProperties.length; i++) {
						traderProperties[i] = trader.getProperties().get(temp[i]);
					}

					temp = tradeeList.getSelectedIndices();
					Property[] tradeeProperties = new Property[temp.length];
					for (int i = 0; i < tradeeProperties.length; i++) {
						tradeeProperties[i] = tradee.getProperties().get(temp[i]);
					}

					trade(trader, tradee, traderProperties, tradeeProperties, traderProfit);
					tradeDialog.dispose();
					window.update(getCurrentPlayer());
				}
			}
		});

		tradeDialog.setVisible(true);
	}


		


	public void toggleMortgage(int propIndex) {
		Property prop = this.getCurrentPlayer().getProperties().get(propIndex);
		if (prop.getMortgaged()) {
			prop.unmortgage();
		} else {
			prop.mortgage();
		}
		updateBuyButton();
	}

	/**
	 *	A psuedo-random roll that simulates 2 six-sided dice
	 *
	 * @param	timeMillis		A long integer used to the seed the random roll
	 * @returns					An integer value between 2-12 that is the result of rolling 2 six-sided dice
	 */
	private int[] roll(Long timeMillis) {
		Random rand = new Random(timeMillis);
		int[] roll = new int[2];
		roll[0] = rand.nextInt(6) + 1;
		roll[1] = rand.nextInt(6) + 1;
		return roll;
	}

	/*

	private void auctionPhase(Property prop) {

		int highestBid = prop.getPrice();

		Player[] participatingPlayers = new Player[active_players];

		int index = 0;
		for (int i = 0; i < num_players; i++) {
			if (!playerList[i].getLoser()) {
				participatingPlayers[index] = playerList[i];	
				index++;
			}
		}

		JDialog auctionDialog = new JDialog(window, "Auction", true);

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(10,30,10,30);

		JPanel auctionPanel = new JPanel();
		auctionPanel.setLayout(new GridBagLayout());

		JLabel header = new JLabel("Auction for " + prop.getName());
		JLabel bid = new JLabel("Bidding starts at " + prop.getPrice());

		auctionPanel.add(header, constraints);
		constraints.gridy++;
		auctionPanel.add(bid, constraints);


		JPanel[] playerPanel = new JPanel[active_players];
		JLabel[] playerLabel = new JLabel[active_players];
		JButton[] submitButton = new JButton[active_players];
		JButton[] endButton = new JButton[active_players];
		JSpinner[] playerSpinner = new JSpinner[active_players];
		SpinnerModel spinner; 
		for (int i = 0; i < active_players; i++) {
			playerPanel[i] = new JPanel();
			playerPanel[i].setLayout(new GridBagLayout());

			Player player = participatingPlayers[i];

			playerLabel[i] = new JLabel(participatingPlayers[i].getName());

			if (player.getMoney() <= highestBid) {
				spinner = new SpinnerNumberModel(0, 0, 0, 1);
			} else {
				spinner = new SpinnerNumberModel(highestBid, 0, player.getMoney(), 1);
			}
			playerSpinner[i] = new JSpinner(spinner);

			submitButton[i] = new JButton("Submit bid");
			submitButton[i].setActionCommand(Integer.toString(i));
			submitButton.addActionListener(new ActionListener() {

				public void ActionPerformed(ActionEvent e) {
					
					int index = Integer.parseInt(e.getActionCommand());
					
					int bid = (Integer) playerSpinner[index].getValue();
					if (bid > highestBid) {
						highestBid = bid;
						
						// Next bidder		
					} else {
						return;
					}
				}
			});
			endButton[i] = new JButton("Withdraw");
			endButton[i].addActionCommand(Integer.toString(i));
			// endButton.addActionListener(new ActionListener(

			constraints.gridx = 0;
			constraints.anchor = GridBagConstraints.WEST;
			playerPanel[i].add(playerLabel[i], constraints);

			constraints.gridx++;
			constraints.anchor = GridBagConstraints.EAST;
			playerPanel[i].add(playerSpinner[i], constraints);

			constraints.gridx++;
			playerPanel[i].add(submitButton[i], constraints);
			
			constraints.gridx++;
			playerPanel[i].add(endButton[i], constraints);
			

			constraints.gridx = 0;
			constraints.gridy++;

			auctionPanel.add(playerPanel[i], constraints);

		}
			
		auctionDialog.getContentPane().add(auctionPanel);
		auctionDialog.pack();

		auctionDialog.setVisible(true);

		/*
		int finalPrice = auction(participatingPlayers);

		if (participatingPlayers.size() == 1) {
			Player winningBidder = participatingPlayers.get(0);
			winningBidder.charge(finalPrice);
			winningBidder.addProperty(prop);
		}
	}
		*/



	/**
	 * Runs the game phase where the property is auctioned to the other players
	 */
	public void auctionPhase() {

//		auctionPhase((Property) board.getSquare(getCurrentPlayer().getPosition()));
		ArrayList<Player> remainingPlayers = new ArrayList<Player>(Arrays.asList(playerList));
		int i = 0;
		remainingPlayers.remove(getCurrentPlayer());
		Player highestBidder = null;
		Property prop = (Property) board.getSquare(getCurrentPlayer().getPosition());
		int topAmount = prop.getPrice() - 1;
		while ((remainingPlayers.size() > 1 || highestBidder == null) && remainingPlayers.size() > 0) {
			i %= remainingPlayers.size();
			if (remainingPlayers.get(i).getMoney() <= topAmount) {
				JOptionPane.showMessageDialog(null, "Cannot match bid");
				remainingPlayers.remove(i);
				continue;
			}
			while (true) {
				String amountString = JOptionPane.showInputDialog(remainingPlayers.get(i).getName() + ": Input a bid above $" + topAmount + " or cancel");
				if (amountString == null) {
					remainingPlayers.remove(i);
					break;
				} else {
					try {
						int amount = Integer.parseInt(amountString);
						if (amount <= topAmount || amount > remainingPlayers.get(i).getMoney()) {
							continue;
						} else {
							topAmount = amount;
							highestBidder = remainingPlayers.get(i);
							i++;
							break;
						}
					} catch (NumberFormatException e) {
						continue;
					}
				}
			}
		}

		if (highestBidder != null) {
			JOptionPane.showMessageDialog(null, highestBidder.getName() + " wins the auction for " + prop.getName() + " for $" + topAmount);
			highestBidder.addProperty(prop);
			highestBidder.charge(topAmount);
			if(highestBidder.isWinner()){
				window.endGame(highestBidder);
			}
		}
	}

	/**
	 * Will complete a trade between players
	 *
	 * @param 	tradee			The player the current player is trading with
	 * @param 	traderProps		The properties that the trader is giving in the trade
	 * @param 	tradeeProps		The properties that the tradee is giving in the trade
	 * @param 	traderProfit		The amount of money the player will be gaining in the trade
	 */
	public void trade(Player trader, Player tradee, Property[] traderProps, Property[] tradeeProps, int traderProfit) {
		for (Property prop : traderProps) {
			tradee.addProperty(trader.removeProperty(prop));
			if(tradee.isWinner()){
				window.endGame(tradee);
			}
			//tradee.addWorth(prop.getPrice() / 2);
			//trader.removeWorth(prop.getPrice() / 2);
		}
		for (Property prop : tradeeProps) {
			trader.addProperty(tradee.removeProperty(prop));
			if(trader.isWinner()){
				window.endGame(trader);
			}
			//trader.addWorth(prop.getPrice() / 2);
			//tradee.removeWorth(prop.getPrice() / 2);
		}
		if (traderProfit == 0) {
			return;
		} else if (traderProfit > 0) {
			trader.getPaid(traderProfit);
			tradee.charge(traderProfit);
		} else {
			tradee.getPaid(traderProfit * -1);
			trader.charge(traderProfit * -1);
		}
		updateBuyButton();
	}

	/**
	 * checks to see if a player has won. If all players are losers, the remaining player is marked as
	 * a winner
	 */
	private void winCheck() {
		if (active_players == 1) {
			for (Player p : playerList) {
				if (!p.getLoser()) {
					window.endGame(p);
				}
			}
		}
					
	}

	/**
	 * Cleans up the properties and board if there a loser was knocked out of the game.
	 * This method is only called when there is a loser being removed from the game.
	 * @param player player that has just lost the game
	 */
	private void loserCleanUp(Player player){
		if (player.getLoser()){
			for (int i = 0; i < player.getProperties().size(); i++){
				Property pReset = player.getProperties().get(i);
				pReset.setOwner(null);
				pReset.setMortgaged(false);
			}
		}
	}

	public void updateBuyButton() {
		Square currentSq = board.getSquare(this.getCurrentPlayer().getPosition());
		if (currentSq instanceof Property) {
			if (((Property) currentSq).getOwner() == null) {
				if (getCurrentPlayer().getMoney() >= ((Property) currentSq).getPrice()) {
					window.enableBuy();
				} else {
					window.disableBuy();
				}
			} else {
				window.disableBuy();
			}
		} else {
			window.disableBuy();
		}
	}

	public void debtCollection(Player debtor) {

		if (debtor.getMoney() >= 0) {
			return;
		}

		JDialog debtDialog = new JDialog(window, "Debt Collection", true);
		debtDialog.setSize(600, 600);

		JPanel debtPanel = new JPanel();
		debtPanel.setLayout(new GridBagLayout());

		GridBagConstraints constraints = new GridBagConstraints();
		constraints.insets = new Insets(10,30,10,30);

		JLabel infoLabel = new JLabel(debtor.getName() + " is in debt for $" + debtor.getMoney() * -1);
		JLabel tradeWithPlayersLabel = new JLabel("Trade with the following players");
		constraints.gridwidth = active_players - 1;
		constraints.gridy = 0;
		debtPanel.add(infoLabel, constraints);
		constraints.gridy++;
		debtPanel.add(tradeWithPlayersLabel, constraints);

		constraints.gridy++;	
		constraints.gridwidth = 1;
		for (Player p : playerList) {
			if (p != debtor && !p.getLoser()) {
				constraints.gridx++;
				JButton temp = new JButton(p.getName());
				temp.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						tradePhase(debtor, p);
						debtDialog.dispose();
						window.update(getCurrentPlayer());
						debtCollection(debtor);
					}
				});
				debtPanel.add(temp, constraints);
			}
		}

		constraints.gridx = 0;
		constraints.gridwidth = active_players - 1;

		JLabel temp = new JLabel(" ");
		constraints.gridy++;
		debtPanel.add(temp, constraints);


		ArrayList<Property> playerProperties = new ArrayList<Property>(debtor.getProperties());
		for (int i = 0; i < playerProperties.size(); i++) {
			Property p = playerProperties.get(i);
			if (p.getMortgaged()) {
				playerProperties.remove(p);
				i--;
			}
		}
		if (playerProperties.size() > 0) {
			String[] propList = new String[playerProperties.size()];
			for (int i = 0; i < playerProperties.size(); i++){
				propList[i] = playerProperties.get(i).getName();
			}
			JList list = new JList(propList);

			constraints.gridwidth = active_players - 1;
			constraints.gridy++;
			constraints.gridx = 0;

			debtPanel.add(list, constraints);

			JButton submitMortgage = new JButton("Mortgage Selected");
			submitMortgage.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int[] mortgagePropertiesIndices = list.getSelectedIndices();
					Property[] mortgageProperties = new Property[mortgagePropertiesIndices.length];
					for (int i = 0; i < mortgageProperties.length; i++){
						mortgageProperties[i] = playerProperties.get(mortgagePropertiesIndices[i]);
					}
					mortgage(debtor, mortgageProperties);
					debtDialog.dispose();
					window.update(getCurrentPlayer());
					debtCollection(debtor);
				}
			});

			constraints.gridy++;
			debtPanel.add(submitMortgage, constraints);
		}

		temp = new JLabel(" ");
		constraints.gridy++;
		debtPanel.add(temp, constraints);

		JButton exit = new JButton();
		if (debtor.getMoney() >= 0) {
			exit.setText("Return to Game");
		} else {
			exit.setText("Accept Your Fate");
		}
		exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				debtDialog.dispose();
			}
		});

		constraints.gridy++;
		debtPanel.add(exit, constraints);		



		debtDialog.getContentPane().add(debtPanel);
		debtDialog.pack();

		debtDialog.setVisible(true);

	}

	/**
	 * Mortgages an array of properties specified by mortgagePrompt.
	 * @param mortgager	Player that is mortgaging properties.
	 * @param props		Array of properties to be mortgaged.
	 */
	public void mortgage(Player mortgager, Property[] props){
		for (Property prop : props) {
			prop.mortgage();
		}
	}

	public void lose(Player player) {

		if (player.getMoney() < 0) {

			window.printLoser(player);
			player.setLoser(true);
			active_players--;
			
			loserCleanUp(player);

			winCheck();

		}
	}
}
