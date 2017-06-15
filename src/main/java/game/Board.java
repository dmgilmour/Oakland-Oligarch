package game;

import java.awt.*;
import java.awt.event.*; 
import javax.swing.*;

public class Board extends JPanel {

	Tile[] tiles;
	
	public Board(){
		//The buttons are just place holders for tiles right now. The layout itself is complicated 
		//current button size is 60X60 just for easy math
		this.setLayout(new GridBagLayout());
		tiles = new Tile[36];
		
		for(int i = 0; i < 36; i++){
			tiles[i] = new Tile(i);
			//associate action listeners here
			tiles[i].setPreferredSize(new Dimension(60, 60));
		}

		int i=0;
		GridBagConstraints constraint = new GridBagConstraints();

		/*
		JButton centerSquare = new JButton("Center");
		constraint.gridx = 1;
		constraint.gridy = 1;
		constraint.gridheight = 8;
		constraint.gridwidth = 8;
		constraint.weightx = 0.8;
		constraint.weighty = 0.8;
		add(centerSquare, constraint);
		*/
		
		//r = button ROW
		//c = button COLLUMN
		//i = button number

		constraint.weightx = 0.1;
		constraint.weighty = 0.1;
		constraint.fill = GridBagConstraints.BOTH;
		for(int r = 0; r < 10; r++){
			if(r == 0||r == 9){
				for(int c = 0; c < 10; c++){
					constraint.gridx = c;
					constraint.gridy = r;
					this.add(tiles[i],constraint);
					i++;
						
				}
			}
			else{
				for(int c = 0; c < 2; c++){
					constraint.gridy = r;
					if(c==0){
						constraint.gridx=0;
					}
					else{
						constraint.gridx = 9;
					}
					this.add(tiles[i],constraint);
					i++;
				}
			}
		}
	}
	
	public void addPlayer(JLabel p){
		tiles[0].addPlayer(p);
	}
}
