import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class StatusPanel extends JPanel {
	
	public StatusPanel() {
		setBackground(Color.BLACK);
		setOpaque(true);
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.ipadx = 100;
		c.ipady = 40;

		JButton button = new JButton("status baby");

		add(button, c);
	}
}
