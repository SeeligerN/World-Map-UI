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
 * This class is the label that is to be set whenever a player is currently
 * defending an attack. The class shows relevant information and listens for
 * events in the label which are then passed to all added listeners.
 * 
 * @author Niklas S.
 *
 */
public class DefendLabel extends JLabel {

	private static final long serialVersionUID = 1L;

	private JLabel title;
	private JSlider troopCount;
	private JButton defendButton;

	private String attackedCountry;

	private List<DefendLabelListener> listeners;

	/**
	 * The constructor creates a new DefendLabel without attaching it to any Window.
	 * The user can select with how many troops they want to defend an attack.
	 * 
	 * @param attackedCountry is the attacked country that is to be defended. Should
	 *                        attackedCountry be null the space for the attacked
	 *                        country will remain empty.
	 * @param minTroops       is the minimum amount of troops that are selectable by
	 *                        the slider on the label.
	 * @param maxTroops       is the maximum amount of troops that are selectable by
	 *                        the slider on the label. Should maxTroops be smaller
	 *                        than minTroops it will be set to minTroops.
	 */
	public DefendLabel(String attackedCountry, int minTroops, int maxTroops) {

		listeners = new ArrayList<>();

		this.attackedCountry = attackedCountry;
		if (attackedCountry == null)
			this.attackedCountry = "";

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
				for (DefendLabelListener dl : listeners)
					dl.defendActionPerformed(DefendLabelListener.TYPE_TROOP_COUNT_CHANGED);
		});

		defendButton = new JButton(Language.get("defend_confirm"));
		defendButton.addActionListener(ae -> {
			for (DefendLabelListener dl : listeners)
				dl.defendActionPerformed(DefendLabelListener.TYPE_DEFEND);
		});

		GroupLayout gl = new GroupLayout(this);
		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);

		gl.setVerticalGroup(gl.createSequentialGroup().addComponent(title).addComponent(troopCount).addGap(0, 50, 9999)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(defendButton)));

		gl.setHorizontalGroup(gl.createSequentialGroup().addGroup(gl.createParallelGroup().addComponent(title)
				.addComponent(troopCount).addComponent(defendButton, GroupLayout.Alignment.TRAILING)));

		this.setLayout(gl);
	}

	/**
	 * This method updates the attacked country, displayed below the title, with a
	 * new one.
	 * 
	 * @param attackedCountry is the new country that is being attacked. Should
	 *                        attackedCountry be null the space will be empty.
	 */
	public void setAttackedCountry(String attackedCountry) {
		if (attackedCountry == null)
			attackedCountry = "";

		this.attackedCountry = attackedCountry;
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
	 * This method adds a new {@link DefendLabelListener} to the list of listeners
	 * and it's method will be called whenever a defendEvent was performed.
	 * 
	 * @param listener is the listener that is to be added.
	 */
	public void addDefendLabelListener(DefendLabelListener listener) {
		listeners.add(listener);
	}

	/**
	 * This method removes a {@link DefendLabelListener} from the list of listeners.
	 * This means that it's method will no longer be called.
	 * 
	 * @param listener is the listener that is to be removed.
	 */
	public void removeDefendLabelListener(DefendLabelListener listener) {
		listeners.remove(listener);
	}

	/**
	 * This method updates the title label with all the stored information in case
	 * it has changed.
	 */
	private void updateLabel() {
		title.setText("<html><div style='text-align: center;'>" + Language.get("defend_title") + "<br>"
				+ Language.get("defend_text", attackedCountry) + "</div></html>");
	}

	/**
	 * This primitive interface facilitates the method that is to be called when a
	 * DefendAction was performed.
	 * 
	 * @author Niklas S.
	 *
	 */
	public interface DefendLabelListener {

		/**
		 * This value will be passed whenever the defend button was pressed.
		 */
		public static final int TYPE_DEFEND = 0;
		/**
		 * This value will be passed whenever the value of the troop slider was changed.
		 * The method will not be called while the user is currently editing the slider
		 * but after the mouse releases it will always be called even when the value is
		 * the same that is was before.
		 */
		public static final int TYPE_TROOP_COUNT_CHANGED = 2;

		/**
		 * This method is called whenever a defendAction was performed.
		 * 
		 * @param type is the type of action that was performed. Possible values are
		 *             DefendLabelListener.TYPE_DEFEND and
		 *             DefendLabelListener.TYPE_TROOP_COUNT_CHANGED.
		 */
		public void defendActionPerformed(int type);

	}
}
