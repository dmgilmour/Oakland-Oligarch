import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class TopPanel extends JPanel {
	
	public TopPanel() {
		setBackground(Color.BLACK);
		setOpaque(true);
		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();

		JButton button = new JButton("o fuk");

		add(button, c);
	}
}
