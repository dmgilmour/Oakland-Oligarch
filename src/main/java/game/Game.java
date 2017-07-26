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
	public Game(Player[] _playerList, Square[] squareList, Window w, Random random, int pt, int ap) {
		playerList = _playerList;
		board = new Board(squareList);
		window = w;
		actionHandler = new ActionHandler(board, playerList, random);
		playerTurn = pt;
		num_players = playerList.length;
		active_players = ap-1;
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
		if(!this.getCurrentPlayer().isInJail()){	//if the player is not in jail take turn as normally
			int roll[] = roll(System.currentTimeMillis());
			boolean collectGoMoney;
			collectGoMoney = this.getCurrentPlayer().moveDistance(roll[0] + roll[1]);

			String squareName = board.getSquare(this.getCurrentPlayer().getPosition()).getName();
			String message = "You rolled a " + roll[0] + " and a " + roll[1] + " and landed on " + squareName;
			if (roll[0] == roll[1]) {
				message += "\nYou got doubles!";
				if(this.getCurrentPlayer().addToDoublesCounter()==3){
					this.getCurrentPlayer().goToJail();
					message += "\nYou got 3 doubles in a row so you go to jail.";
					JOptionPane.showMessageDialog(null, message);
					window.update(this.getCurrentPlayer());
					endPhase();
					return;
				}
			}
			if (collectGoMoney) {
				message += "\nYou passed go and collected " + OaklandOligarchy.GO_PAYOUT;
			}
			JOptionPane.showMessageDialog(null, message);
			window.update(this.getCurrentPlayer());
			if (roll[0] != roll[1]) {
				window.disableRoll();
				window.enableEnd();
			} else {
				window.enableRoll();
				window.disableEnd();
			}
			actionPhase();
		}
		else{	//the player is currently in jail
			int roll[] = roll(System.currentTimeMillis());

			String message = "You rolled a " + roll[0] + " and a " + roll[1];
			if(roll[0] == roll[1]){
				this.getCurrentPlayer().leaveJail();
				boolean collectGoMoney;
				collectGoMoney = this.getCurrentPlayer().moveDistance(roll[0] + roll[1]);

				String squareName = board.getSquare(this.getCurrentPlayer().getPosition()).getName();
				message += "\nYou got doubles, left jail, and landed on" + squareName;
				if (collectGoMoney) {
					message += "\nYou passed go and collected " + OaklandOligarchy.GO_PAYOUT;
				}
				JOptionPane.showMessageDialog(null, message);

				window.disableRoll();
				window.enableEnd();
				window.update(this.getCurrentPlayer());
				actionPhase();
			}
			else{
				this.getCurrentPlayer().addToJailCounter();
				message += "\nThis is your " + this.getCurrentPlayer().getJailCounter() + " turn lost in jail.";
				JOptionPane.showMessageDialog(null, message);
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
		//System.out.println("in end game: " + this.getCurrentPlayer().getName() + ": money: " + this.getCurrentPlayer().getMoney() + " worth: " + this.getCurrentPlayer().getWorth() + " loser: " + this.getCurrentPlayer().getLoser());
		//loserCheck();
		this.getCurrentPlayer().resetDoublesCounter();
		playerTurn = (playerTurn + 1) % num_players;	//Increment to the next player's turn
		if (playerList[playerTurn].getLoser() == false){
			JOptionPane.showMessageDialog(null, this.getCurrentPlayer().getName() + "'s turn");
			startPhase();
		}
		else{
			endPhase();
		}
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

	/**
	 * Runs the game phase where the player performs an action based on the tile they are on
	 */
	public void actionPhase() {
		Player player = this.getCurrentPlayer();
		Square square = board.getSquare(player.getPosition());
		if(square == null) {									//Check to ensure that a tile was retrieved properly from the board
			return;
		}
		// Either charges or prompts player to purchase depending on whether
		// it is owned or not
		boolean cannotBuy = square.act(player);
		if(!cannotBuy) {
			window.enableBuy();
		}
		if (square instanceof ActionSquare) {
			actionHandler.run(player);
		}
		loserCheck();
		window.update(player);
	}

	/**
	 * Runs the game phase where the player can purchase a property
	 */
	public void buyPhase() {
		Player player = this.getCurrentPlayer();
		Square square = board.getSquare(player.getPosition());
		if(square.act(player))
		{
			window.disableBuy();
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

		JFrame tradeFrame = new JFrame("Trade between " + trader.getName() + " and " + tradee.getName());
		tradeFrame.setSize(400, 500);

		tradeFrame.setLayout(new GridBagLayout());
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



		SpinnerModel traderSpinnerModel = new SpinnerNumberModel(0, 0, trader.getMoney(), 1);
		JSpinner traderMoneySpinner = new JSpinner(traderSpinnerModel);
		SpinnerModel tradeeSpinnerModel = new SpinnerNumberModel(0, 0, tradee.getMoney(), 1);
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

		constraints.gridx = 1;
		constraints.gridy = 0;
		tradeFrame.add(tradePanel, constraints);

		JButton submit = new JButton("Submit Trade");
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.gridwidth = 2;
		tradeFrame.add(submit, constraints);

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
					tradeFrame.dispose();
					window.update(getCurrentPlayer());
				}
			}
		});

		tradeFrame.setVisible(true);
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
	 * Runs the game phase where the property is auctioned to the other players
	 */
	public void auctionPhase() {
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
			//tradee.addWorth(prop.getPrice() / 2);
			//trader.removeWorth(prop.getPrice() / 2);
		}
		for (Property prop : tradeeProps) {
			trader.addProperty(tradee.removeProperty(prop));
			//trader.addWorth(prop.getPrice() / 2);
			//tradee.removeWorth(prop.getPrice() / 2);
		}
		if (traderProfit > 0) {
			trader.getPaid(traderProfit);
			tradee.charge(traderProfit);
		} else {
			tradee.getPaid(traderProfit * -1);
			trader.charge(traderProfit * -1);
		}
		updateBuyButton();
	}

	/**
	 * checks to see if a player has lost. If a loser is found, that player is marked as
	 * a loser and the number of active_players is decremented. If the number of active_players
	 * reaches 1, there is a winner. The winner is passed to window.endGame(). GG
	 */
	private void loserCheck(){
		for(int i = 0; i < playerList.length; i++){
			if(playerList[i].getMoney() < 0 && playerList[i].getWorth() < 0 && !playerList[i].getLoser()){
				//System.out.println(playerList[i].getName() + ": money: " + playerList[i].getMoney() + " worth: " + playerList[i].getWorth() + " Loser: " + playerList[i].getLoser());
				window.printLoser(playerList[i]);
				playerList[i].setLoser(true);
				//System.out.println(playerList[i].getName() + ": money: " + playerList[i].getMoney() + " worth: " + playerList[i].getWorth() + " Loser: " + playerList[i].getLoser());
				//System.out.println("active players: " + active_players);
				active_players --;
				if(active_players > 1){
					loserCleanUp(playerList[i]);
				}
				else{
					//winner
					for(int j = 0; j < playerList.length; j++){
						if(!playerList[j].getLoser()){
							window.endGame(playerList[j]);
						}
          }
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
		if(player.getLoser()){
			for(int i = 0; i < player.getProperties().size(); i++){
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

	/**
	 * Starts the mortgagePrompt for a player that owes money and must mortgage properties to pay cost.
	 * @param player	The player that needs to mortgage properties.
	 * @param cost		The amount of money the player owes.
	 */
	public void mortgagePhase(Player player, int cost){
		while(player.getMoney() < cost && player != null){
			window.printMortgage(player, cost);
			mortgage(player, mortgagePrompt(player));
		}
	}

	/**
	 * Prompts a Player they need to mortgage properties, followed by a list of properties to choose from.
	 * @param player the Player that needs morgage properties to prevent losing.
	 * @return 		returns an array of properties to mortgage.
	 */
	public Property[] mortgagePrompt(Player player){
		ArrayList<Property> playerProperties = player.getProperties();
		String[] propList = new String[playerProperties.size()];
		for (int i = 0; i < playerProperties.size(); i++){
			if(!playerProperties.get(i).getMortgaged()){
				propList[i] = playerProperties.get(i).getName();
			}
		}
		JList list = new JList(propList);
		JOptionPane.showMessageDialog(null, player.getName() + " choose properties to mortgage.");
		JOptionPane.showMessageDialog(null, list, player.getName() + " mortgaging.", JOptionPane.PLAIN_MESSAGE);
		int[] mortgageProperties = list.getSelectedIndices();
		Property[] toReturn = new Property[mortgageProperties.length];
		for (int i = 0; i < toReturn.length; i++){
			toReturn[i] = playerProperties.get(mortgageProperties[i]);
		}
		return toReturn;
	}

	/**
	 * Mortgages an array of properties specified by mortgagePrompt.
	 * @param mortgager	Player that is mortgaging properties.
	 * @param props		Array of properties to be mortgaged.
	 */
	public void mortgage(Player mortgager, Property[] props){
		for(Property prop : props){
			prop.mortgage();
		}
	}

	/**
	 * Loop through players properties morgaging them. then setting worth and money to -1.
	 * @param player	The player that is morgaging properties.
	 */
	public void loserPhase(Player player){
		//loop through all properties and morgage them.
		/*for(Property prop : player.getProperties()){
			prop.mortgage();
		}*/
		//player.setMoney(-1);
		//player.setWorth(-1);
		//charging all of players money is handeled in Player.payRent() or ActionHandler
		//there was a loser go to loserCheck to have it handle them.
		loserCheck();
		//now clean up the loser.
		loserCleanUp(player);
	}
}
