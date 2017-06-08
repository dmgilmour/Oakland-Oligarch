import java.awt.*;
import java.awt.event.*; 
import javax.swing.*;

public class Tile extends JPanel{

		int number; // what number this tile is, identifier for the board
		
		Label player1 = new Label("1");
		Label player2 = new Label("2");
		Label player3 = new Label("3");
		Label player4 = new Label("4");
		JPanel players;
		
		public Tile(int n){
			number=n;
			this.setLayout(new GridBagLayout());
			
			players = new JPanel();
			players.setLayout(new GridLayout(1, 4));
			
			JButton button = new JButton("Property " + n);
			button.setPreferredSize(new Dimension(60, 30));
			
			GridBagConstraints constraint = new GridBagConstraints();
			
			constraint.weightx = 0.1; //This can be adjusted accordingly.
			constraint.weighty = 0.05;
			constraint.fill = GridBagConstraints.BOTH;
			
			constraint.gridx = 0;
			constraint.gridy = 0;
			
			this.add(button, constraint);
			
			constraint.gridy = 1;
			
			this.add(players, constraint);
			
		}
		
		//Put a player onto this tile. Takes the player number. 1-4
		public void addPlayer(int p){
			switch (p) {
            case 1:  players.add(player1);
                     break;
            case 2:  players.add(player2);
                     break;
            case 3:  players.add(player3);
                     break;
            case 4:  players.add(player4);
                     break;
            default: System.out.println("Invalid player number to add to tile. Tile.java");
                     break;
			}
		}
		
		//Remove a player from this tile. Takes the player number. 1-4
		public void removePlayer(int p){
			switch (p) {
            case 1:  players.remove(player1);
                     break;
            case 2:  players.remove(player2);
                     break;
            case 3:  players.remove(player3);
                     break;
            case 4:  players.remove(player4);
                     break;
            default: System.out.println("Invalid player number to remove from tile. Tile.java");
                     break;
			}
		}
}
