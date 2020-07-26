package ui.controlBar;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import ui.Language;

/**
 * This class is the label showing the players in the game. The label only shows
 * information and is cannot be interacted with.
 * 
 * @author Niklas S.
 *
 */
public class PlayerListLabel extends JLabel {

	private static final long serialVersionUID = 1L;

	private JLabel title;

	private JLabel[] playerNames;

	/**
	 * This constructor creates a new PlayerListLabel that is completely empty. The
	 * Label will not be attached to any Window.
	 */
	public PlayerListLabel() {
		this(null);
	}

	/**
	 * This constructor creates a new PlayerListLabel with the specified player
	 * names in their slots.
	 * 
	 * @param names is a String[] that contains the names of all the players in the
	 *              game up to a maximum of 6 players. If names is null a
	 *              PlayerListLabel is created where no players are listed. If a
	 *              player in the names array is null that slot is skipped and will
	 *              remain empty.
	 */
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

	/**
	 * This method updates the player at the specified spot the the specified
	 * String.
	 * 
	 * @param playerNum the player slot to be updated. If playerNum is outside of
	 *                  the range 0-5 nothing will happen. The slots are ordered
	 *                  left to right then top to bottom.
	 * @param player    is the String that is to be inserted into the player slot.
	 */
	public void setPlayer(int playerNum, String player) {
		if (playerNum < 0 || playerNum > 5)
			return;

		if (player == null)
			playerNames[playerNum].setText("");
		else
			playerNames[playerNum].setText("<html><b>" + player + "</b></html>");
	}
}
