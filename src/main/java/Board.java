import java.awt.*;
import java.awt.event.*; 
import javax.swing.*;

public class Board extends JPanel {

		public Board(){
			//The buttons are just place holders for tiles right now. The layout itself is complicated 
			//current button size is 60X60 just for easy math
			this.setLayout(new GridBagLayout());
			JButton[] tiles = new JButton[36];
			
			for(int i=0; i<36; i++){
				tiles[i]=new JButton("Propert");
				//associate action listeners here
			}

			GridBagConstraints constraint = new GridBagConstraints();
			for(int r=0; r<10; r++){
				if(r==0||r==9){
					for(int c=0; c<10; c++){
						constraint.gridx=c;
						constraint.gridy=r;
						this.add(tiles[r+c],constraint);
						
					}
				}
				else{
					for(int c=0; c<2; c++){
						constraint.gridx=c;
						if(r==1){
							constraint.gridx=9;
						}
						else{
							constraint.gridx=0;
						}
						this.add(tiles[r+c],constraint);
					}
				}
			}
		}
}
