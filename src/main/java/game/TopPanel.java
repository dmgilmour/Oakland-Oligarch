package game;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * @author Dan
 *
 */
public class TopPanel extends JPanel {
	

	JLabel clock = new JLabel();
	int time = 0; // this will eventually take a value during save/load
	
	public TopPanel(Random random) {
		setBackground(new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()));
		setOpaque(true);
		GridBagConstraints constraints = new GridBagConstraints();

		constraints.weightx = 0.25;

		constraints.gridx = 0;
		JButton saveButton = new JButton("Save Game");
		add(saveButton, constraints);

		constraints.gridx = 1;
		JButton loadButton = new JButton("Load Game");
		add(loadButton, constraints);

		constraints.gridx = 2;
		JButton optionsButton = new JButton("Options");
		add(optionsButton, constraints);

		constraints.gridx = 3;
		JButton instructionsButton = new JButton("Instructions");
		add(instructionsButton, constraints);
		
		constraints.gridx = 4;
		Thread clockThread = new Thread(() -> {
			while(true){
				clock.setText("Time Played: " + time);
				try{
					Thread.sleep(1000);
				}
				catch(InterruptedException e){
					System.out.println(e);
				}
				time++;
			}
		});
		clockThread.start();
		add(clock, constraints);
		

	}
}
