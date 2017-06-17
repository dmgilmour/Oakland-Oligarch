package game;
public class Game {

	public Game(Player[] playerList) {
		
		Property[] properties = new Property[36];
		for(int i = 0; i<36; i++){
			properties[i]=new Property("Property "+i, i, i);
		}

		Window window = new Window(playerList, properties);

		// Other random setup
		
	}

	// More things when we get to do non UI
	
}
