package ui.controlBar;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class PlayerListLabel extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel title;
	
	private JLabel[] playerNames;
	
	PlayerListLabel() {
		this.setMaximumSize(new Dimension(9999999, 500));
		
		title = new JLabel("<html><div style='text-align: center;'>"
				+ "<a style='font-size: 16'><b>Players</b></a>", SwingConstants.CENTER);
		title.setFont(new Font("Dialog", Font.PLAIN, 12));
		
		playerNames = new JLabel[6];
		for (int i = 0; i < playerNames.length; i++)
			playerNames[i] = new JLabel("<html><i>player " + (i + 1) + " (" + ("" + i).hashCode() % 10 + " troops)</i></html>");
		
		GroupLayout gl = new GroupLayout(this);
		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);
		
		gl.setVerticalGroup(gl.createSequentialGroup()
				.addComponent(title)
				.addGroup(gl.createParallelGroup()
						.addComponent(playerNames[0])
						.addComponent(playerNames[1]))
				.addGroup(gl.createParallelGroup()
						.addComponent(playerNames[2])
						.addComponent(playerNames[3]))
				.addGroup(gl.createParallelGroup()
						.addComponent(playerNames[4])
						.addComponent(playerNames[5])));
		
		gl.setHorizontalGroup(gl.createParallelGroup()
				.addComponent(title)
				.addGroup(gl.createSequentialGroup()
					.addGroup(gl.createParallelGroup()
							.addComponent(playerNames[0])
							.addComponent(playerNames[2])
							.addComponent(playerNames[4]))
					.addGroup(gl.createParallelGroup()
							.addComponent(playerNames[1])
							.addComponent(playerNames[3])
							.addComponent(playerNames[5]))));
		
		this.setLayout(gl);
	}
}
