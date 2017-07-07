package game;

import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.*;

/**
 * @author Dan
 *
 */
public class Game {

	private boolean rollTaken;
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
	public Game(Player[] _playerList, Square[] squareList, Window w, Random random) {
		playerList = _playerList;
		board = new Board(squareList);
		window = w;
		actionHandler = new ActionHandler(board, playerList, random);
		playerTurn = 0;
		rollTaken = false;
    num_players = playerList.length;
		active_players = num_players;
	}

	public int getTurn() {
		return playerTurn;
	}

	public int getNumPlayers(){
		return num_players;
	}

	public Player[] getPlayers(){
		return playerList;
	}

	public Player getCurrentPlayer() {
		return playerList[playerTurn];
	}

	/**
	 * Runs the game phase for the start of a turn during which a player can click info
	 * buttons and roll the dice giant button
	 */
	public void startPhase() {
		window.update(this.getCurrentPlayer());
		rollTaken = false;			

		window.disableEnd();
		window.disableBuy();
		window.enableRoll();
		window.update(this.getCurrentPlayer());
	}


	/**
	 * Runs the game phase during which players roll and move
	 */
	public void movePhase() {
		int roll = roll(System.currentTimeMillis());		
		board.movePlayer(this.getCurrentPlayer(), roll);
		window.update(this.getCurrentPlayer());
		window.disableRoll();
		actionPhase();
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
		playerTurn = (playerTurn + 1) % num_players;	//Increment to the next player's turn
		if(playerList[playerTurn].getLoser() == false){
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
	private int roll(Long timeMillis) {
		if(!rollTaken) {
			Random rand = new Random(timeMillis);
			rollTaken = true;
			int roll = rand.nextInt(6) + rand.nextInt(6) + 2;	//Simulates two dice rolls by retriving integers from 0-5 and adding 2
			return roll;
		}
		else {
			return -1;
		}
	}

	/**
	 * Runs the game phase where the player performs an action based on the tile they are on
	 */
	public void actionPhase() {
		Player player = this.getCurrentPlayer();
		Square square = board.getSquare(player.getPosition());
		if(square == null) {									//Check to ensure that a tile was retrived properly from the board
			return;
		}
		boolean cannotBuy = square.act(player);
		if(!cannotBuy) {
			window.enableBuy();
		}
		loserCheck();
		window.enableEnd();
		if(square instanceof ActionSquare) {
			actionHandler.run(player);
		}
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
	public boolean tradePhase(Player tradee) {
		Player trader = this.getCurrentPlayer();
		if (trader == tradee) {
			return false;
		}
		Property[] traderProperties = tradePrompt(trader);
		if (traderProperties == null) return false;
		Property[] tradeeProperties = tradePrompt(tradee);
		if (tradeeProperties == null) return false;
		boolean validTrade = false;
		int traderProfit = 0;
		while (!validTrade) {
			String traderProfitString = JOptionPane.showInputDialog("Amount requested");
			if (traderProfitString == null) return false;

			try {
				traderProfit = Integer.parseInt(traderProfitString);
			} catch (NumberFormatException e) {
				continue;
			}

			if (traderProfit > 0) {
				validTrade = (tradee.getMoney() > traderProfit);
			} else {
				validTrade = (trader.getMoney() > traderProfit * -1);
			}
		}

//		if (JOptionPane.showMessageDialog(null, tradee.getName() + ": Do you want this trade?", "wat", JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) return false;

		trade(tradee, traderProperties, tradeeProperties, traderProfit);
		window.update(trader);
		return true;
	}	

	/**
	 * Runs the game phase where the property is auctioned to the other players 
	 */
	public void auctionPhase() {
		ArrayList<Player> remainingPlayers = new ArrayList<Player>(Arrays.asList(playerList));
		int i = 0;
		boolean goneAround = false;
		remainingPlayers.remove(getCurrentPlayer());
		Property prop = (Property) board.getSquare(getCurrentPlayer().getPosition());
		int topAmount = prop.getPrice();
		while (remainingPlayers.size() > 1) {
			if (i >= remainingPlayers.size()) {
				goneAround = true;
				i %= remainingPlayers.size();
			}
			boolean invalidInput = true;
			while (invalidInput) {
				String amountString = JOptionPane.showInputDialog(remainingPlayers.get(i).getName() + ": Input a bid above $" + topAmount + " or cancel");
				if (amountString == null) {
					remainingPlayers.remove(i);
					invalidInput = false;
				} else {
					try {
						int amount = Integer.parseInt(amountString);
						if (amount < topAmount) {
							invalidInput = true;
						} else {
							topAmount = amount;
							i++;
							invalidInput = false;
						}
					} catch (NumberFormatException e) {
						invalidInput = true;

					}
				}
			}
		}

    if (!goneAround) {
			if (JOptionPane.showConfirmDialog(null, remainingPlayers.get(0).getName() + ": Would you like to buy this property for $" + topAmount + "?") != JOptionPane.YES_OPTION) {
				return;
			}
		}

		remainingPlayers.get(0).addProperty(prop);
		remainingPlayers.get(0).charge(topAmount);
	}


	/**
	 * Prompts which properties want to be traded for a given player
	 *
	 * @param 	player 		the player whose properties are being selected for trade
	 * @return 			the list of properties the players want to trade
	 */
	public Property[] tradePrompt(Player player) {
		ArrayList<Property> playerProperties = player.getProperties();
		String[] propList = new String[playerProperties.size()];
		for (int i = 0; i < playerProperties.size(); i++) {
			propList[i] = playerProperties.get(i).getName();
		}
		JList list = new JList(propList); 

		JOptionPane.showMessageDialog(null, list, player.getName(), JOptionPane.PLAIN_MESSAGE);
		int[] tradeProperties = list.getSelectedIndices();
		Property[] toReturn = new Property[tradeProperties.length];
		for (int i = 0; i < toReturn.length; i++) {
			toReturn[i] = playerProperties.get(tradeProperties[i]); 
		}
		return toReturn;
	}
		

	/**
	 * Will set the property to mortgaged and give the player have the price
	 *
	 * @param 	property	the property the player is attempting to mortgage
	 */
	public void mortgage(Property property) {
		if (!property.getMortgaged()) {
			int mortgageValue = property.getPrice() / 2;
			property.setMortgaged(true);
			this.getCurrentPlayer().getPaid(mortgageValue);
		}
	}

	/**
	 * Will set the property to unmortgaged and take half the price
	 *
	 * @param 	property	the property the player is attempting to unmortgage
	 */
	public void unmortgage(Property property) {
		if (property.getMortgaged()) {
			Player player = this.getCurrentPlayer();
			int price = property.getPrice();
			if (player.getMoney() > price) {
				property.setMortgaged(false);
				player.charge(price);
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
	public void trade(Player tradee, Property[] traderProps, Property[] tradeeProps, int traderProfit) {
		Player trader = this.getCurrentPlayer();
		for (Property prop : traderProps) {
			tradee.addProperty(trader.removeProperty(prop));
		}
		for (Property prop : tradeeProps) {
			trader.addProperty(tradee.removeProperty(prop));
		}
		if (traderProfit > 0) {
			trader.getPaid(traderProfit);
			tradee.charge(traderProfit);
		} else {
			tradee.getPaid(traderProfit);
			trader.charge(traderProfit);
		}
	}
				
	/**
	 * checks to see if a player has lost. If a loser is found, that player is marked as
	 * a loser and the number of active_players is decremented. If the number of active_players
	 * reaches 1, there is a winner. The winner is passed to window.endGame(). GG
	 */
	private void loserCheck(){
		for(int i = 0; i < playerList.length; i++){
			if(playerList[i].getMoney() < 0 && playerList[i].getLoser() == false){
				playerList[i].setLoser(true);
				window.printLoser(playerList[i]);
				active_players --;
				if(active_players > 1){
					loserCleanUp(playerList[i]);
				}
				else{
					//winner
					for(int j = 0; j < playerList.length; j++){
						if(playerList[j].getLoser() == false){
							window.endGame(playerList[j]);
						}
          }
				}
			}
		}
	}

	/**
	 * cleans up the properties and board if there a loser was knocked out of the game
	 * @param player player that has just lost the game
	 */
	private void loserCleanUp(Player player){
		for(int i = 0; i < player.getProperties().size(); i++){
			Property pReset = player.getProperties().get(i);
			pReset.setOwner(null);
			pReset.setMortgaged(false);
		}
	}
}
