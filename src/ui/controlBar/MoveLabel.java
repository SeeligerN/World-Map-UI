package ui.controlBar;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

import ui.Language;

/**
 * This class is the label that is to be visible whenever a player is currently
 * moving some troops from one country to another. The class shows relevant
 * information and listens for events in the label which are then passed to all
 * added listeners.
 * 
 * @author Niklas S.
 *
 */
public class MoveLabel extends JLabel {

	private static final long serialVersionUID = 1L;

	private JLabel title;
	private JSlider troopCount;
	private JButton moveButton;

	private String moveFrom;
	private String moveTo;

	private List<MoveLabelListener> listeners;

	/**
	 * The constructor creates a new FortifyingLabel without attaching it to any
	 * Window. The user can select how many troops they want to move from and to a
	 * given country.
	 * 
	 * @param moveFrom  is the country that the user wants to move troops from.
	 *                  Should moveFrom be null it will be replaced with an empty
	 *                  String and the space will remain empty.
	 * @param moveTo    is the country that the user wants to move troops to. Should
	 *                  moveTo be null it will be replaced with an empty String and
	 *                  the space will remain empty.
	 * @param minTroops is the minimum amount of troops that are selectable by the
	 *                  slider on the label.
	 * @param maxTroops is the maximum amount of troops that are selectable by the
	 *                  slider on the label. Should maxTroops be smaller than
	 *                  minTroops it will be set to minTroops.
	 */
	public MoveLabel(String moveFrom, String moveTo, int minTroops, int maxTroops) {
		listeners = new ArrayList<>();

		if (moveFrom == null)
			moveFrom = "";
		if (moveTo == null)
			moveTo = "";
		this.moveFrom = moveFrom;
		this.moveTo = moveTo;

		this.setMaximumSize(new Dimension(9999999, 500));

		title = new JLabel("", SwingConstants.CENTER);
		title.setFont(new Font("Dialog", Font.PLAIN, 12));
		updateLabel();

		if (maxTroops < minTroops)
			maxTroops = minTroops;

		troopCount = new JSlider(minTroops, maxTroops, minTroops);
		troopCount.setMajorTickSpacing(1);
		troopCount.setPaintLabels(true);
		troopCount.setOpaque(false);
		troopCount.addChangeListener(e -> {
			if (!troopCount.getValueIsAdjusting())
				for (MoveLabelListener dl : listeners)
					dl.moveActionPerformed(MoveLabelListener.TYPE_TROOP_COUNT_CHANGED);
		});

		moveButton = new JButton(Language.get("move_confirm"));
		moveButton.addActionListener(a -> {
			for (MoveLabelListener ml : listeners)
				ml.moveActionPerformed(MoveLabelListener.TYPE_MOVE);
		});

		GroupLayout gl = new GroupLayout(this);
		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);

		gl.setVerticalGroup(gl.createSequentialGroup().addComponent(title).addComponent(troopCount).addGap(0, 50, 9999)
				.addComponent(moveButton));

		gl.setHorizontalGroup(gl.createSequentialGroup().addGroup(gl.createParallelGroup().addComponent(title)
				.addComponent(troopCount).addComponent(moveButton, GroupLayout.Alignment.TRAILING)));

		this.setLayout(gl);
	}

	/**
	 * Thie method updates the title label with all the stored information in case
	 * it has changed.
	 */
	private void updateLabel() {
		title.setText("<html><div style='text-align: center;'>" + Language.get("move_title") + "<br>"
				+ Language.get("move_text", moveFrom, moveTo) + "</div></html>");
	}

	/**
	 * This method updates the country the player wants to move troops from,
	 * displayed below the label title, with a new one.
	 * 
	 * @param moveFrom is the new country that the player wants to move troops from.
	 *                 Should moveFrom be null the space will be empty.
	 */
	public void setMoveFrom(String moveFrom) {
		if (moveFrom == null)
			moveFrom = "";

		this.moveFrom = moveFrom;
		updateLabel();
	}

	/**
	 * This method updates the country the player wants to move troops to, displayed
	 * below the label title, with a new one.
	 * 
	 * @param moveTo is the new country that the player wants to move troops to.
	 *               Should moveTo be null the space will be empty.
	 * 
	 */
	public void setMoveTo(String moveTo) {
		if (moveTo == null)
			moveTo = "";

		this.moveTo = moveTo;
		updateLabel();
	}

	/**
	 * Getter for the troop amount selected by the slider on the label.
	 * 
	 * @return the amount of troops currently selected.
	 */
	public int getTroopCount() {
		return troopCount.getValue();
	}

	/**
	 * This method adds a new {@link MoveLabelListener} to the list of listeners and
	 * it's method will be called whenever a moveEvent was performed.
	 * 
	 * @param listener is the listener that is to be added.
	 */
	public void addMoveLabelListener(MoveLabelListener listener) {
		listeners.add(listener);
	}

	/**
	 * This method removes a {@link MoveLabelListener} from the list of listeners.
	 * This means that it's method will no longer be called.
	 * 
	 * @param listener is the listener that is to be removed.
	 */
	public void removeMoveLabelListener(MoveLabelListener listener) {
		listeners.remove(listener);
	}

	/**
	 * This primitive interface facilitates the method that is to be called when a
	 * moveAction was performed.
	 * 
	 * @author Niklas S.
	 *
	 */
	public interface MoveLabelListener {

		/**
		 * This value will be passed whenever the move button was pressed.
		 */
		public static final int TYPE_MOVE = 0;
		/**
		 * This value will be passed whenever the value of the troop slider was changed.
		 * The method will not be called while the user is currently editing the slider
		 * but after the mouse releases it will always be called even if the value is
		 * the same that is was previously.
		 */
		public static final int TYPE_TROOP_COUNT_CHANGED = 2;

		/**
		 * This method is called whenever a moveAction was performed.
		 * 
		 * @param type is the type of action that was performed. Possible values are
		 *             MoveLabelListener.TYPE_MOVE and
		 *             MoveLabelListener.TYPE_TROOP_COUNT_CHANGED.
		 */
		public void moveActionPerformed(int type);

	}
}
