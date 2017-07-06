package game;

import java.util.Random;
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

	private Player getCurrentPlayer() {
		return playerList[playerTurn];
	}

	/**
	 * Runs the game phase for the start of a turn during which a player can click info
	 * buttons and roll the dice giant button
	 */
	public void startPhase() {
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
