package game;
public class Game {

	public Game(Player[] playerList) {

		Window window = new Window(playerList);
		
		for(Player p: playerList){
			window.addPlayer(p.getToken());
		}

		// Other random setup
		
	}

	// More things when we get to do non UI
	
}
