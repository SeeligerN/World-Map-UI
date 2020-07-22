package ui.controlBar;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import ui.Language;

public class PlayerListLabel extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel title;

	private JLabel[] playerNames;

	public PlayerListLabel() {
		this(null);
	}

	public PlayerListLabel(String[] names) {
		this.setMaximumSize(new Dimension(9999999, 500));

		title = new JLabel("<html><div style='text-align: center;'>" + Language.get("players_title") + "</div></html>",
				SwingConstants.CENTER);
		title.setFont(new Font("Dialog", Font.PLAIN, 12));

		playerNames = new JLabel[6];
		for (int i = 0; i < playerNames.length; i++)
			playerNames[i] = new JLabel("<html></html>");

		if (names != null)
			for (int i = 0; i < 6 && i < names.length; i++)
				setPlayer(i, names[i]);

		GroupLayout gl = new GroupLayout(this);
		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);

		gl.setVerticalGroup(gl.createSequentialGroup().addComponent(title)
				.addGroup(gl.createParallelGroup().addComponent(playerNames[0]).addComponent(playerNames[1]))
				.addGroup(gl.createParallelGroup().addComponent(playerNames[2]).addComponent(playerNames[3]))
				.addGroup(gl.createParallelGroup().addComponent(playerNames[4]).addComponent(playerNames[5])));

		gl.setHorizontalGroup(gl.createParallelGroup().addComponent(title)
				.addGroup(gl.createSequentialGroup()
						.addGroup(gl.createParallelGroup().addComponent(playerNames[0]).addComponent(playerNames[2])
								.addComponent(playerNames[4]))
						.addGroup(gl.createParallelGroup().addComponent(playerNames[1]).addComponent(playerNames[3])
								.addComponent(playerNames[5]))));

		this.setLayout(gl);
	}

	public void setPlayer(int playerNum, String player) {
		if (playerNum < 0 || playerNum > 5)
			return;

		if (player == null)
			playerNames[playerNum].setText("");
		else
			playerNames[playerNum].setText("<html><b>" + player + "</b></html>");
	}
}
