import java.awt.*;
import java.awt.event.*; 
import javax.swing.*;

public class Board extends JPanel {

		public Board(){
			//The buttons are just placeholders for tiles right now. The layout itself is complicated 
			//current button size is 60X60 just for easy math
			JButton[] tiles = new JButton[36];
			
			for(int i=0; i<36; i++){
				tiles[i]=new JButton("Propert");
				//associate action listeners here
			}

			GridBagConstraints constraint = new GridBagConstraints();
			for(int c=0; c<10; c++){
				if(c==0||c==9){
					for(int r=0; r<10; r++){
						constraint.gridx=c;
						constraint.gridy=r;
						this.add(tiles[c+r],constraint);
						
					}
				}
				else{
					for(int r=0; r<2; r++){
						
					}
				}
			}
		}
}
