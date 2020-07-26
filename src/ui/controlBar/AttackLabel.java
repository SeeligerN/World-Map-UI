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
 * This class is the Label that is to be set whenever a player is currently
 * planning an attack. The class shows the attack details and listens for
 * changes in the label which are then passed to all added listeners.
 * 
 * @author Niklas S.
 *
 */
public class AttackLabel extends JLabel {

	private static final long serialVersionUID = 1L;

	private JLabel title;
	private JSlider troopCount;
	private JButton attackButton, dismissButton;

	private String attacker;
	private String defender;

	private List<AttackLabelListener> listeners;

	/**
	 * The constructor creates a new AttackLabel without attaching it to any Window.
	 * It is meant for the user to be able to specify with how many troops they want
	 * to attack which country.
	 * 
	 * @param attacker  is the attacker that is to be displayed below the title.
	 *                  Should attacker be null the space for the attacker will be
	 *                  empty.
	 * @param defender  is the defender that is to be displayed below the title.
	 *                  Should defender be null the space for the defender will be
	 *                  empty.
	 * @param minTroops is the minimum amount of troops that are selectable by the
	 *                  slider on the label.
	 * @param maxTroops is the maximum amount of troops that are selectable by the
	 *                  slider on the label. Should maxTroops be smaller than
	 *                  minTroops it will be set to minTroops.
	 */
	public AttackLabel(String attacker, String defender, int minTroops, int maxTroops) {
		this.setMaximumSize(new Dimension(9999999, 500));

		listeners = new ArrayList<>();

		if (attacker == null)
			attacker = "";
		if (defender == null)
			defender = "";

		this.attacker = attacker;
		this.defender = defender;

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
				for (AttackLabelListener al : listeners)
					al.attackActionPerformed(AttackLabelListener.TYPE_TROOP_COUNT_CHANGED);
		});

		attackButton = new JButton(Language.get("attack_confirm"));
		attackButton.addActionListener(ae -> {
			for (AttackLabelListener al : listeners)
				al.attackActionPerformed(AttackLabelListener.TYPE_ATTACK);
		});

		dismissButton = new JButton(Language.get("attack_dismiss"));
		dismissButton.addActionListener(ae -> {
			for (AttackLabelListener al : listeners)
				al.attackActionPerformed(AttackLabelListener.TYPE_DISMISS);
		});

		GroupLayout gl = new GroupLayout(this);
		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);

		gl.setVerticalGroup(gl.createSequentialGroup().addComponent(title).addComponent(troopCount).addGap(0, 50, 9999)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(dismissButton)
						.addComponent(attackButton)));

		gl.setHorizontalGroup(gl.createSequentialGroup()
				.addGroup(gl.createParallelGroup().addComponent(title).addComponent(troopCount)
						.addComponent(dismissButton).addComponent(attackButton, GroupLayout.Alignment.TRAILING)));

		this.setLayout(gl);
	}

	/**
	 * This updates the text of the title label with all information that may have
	 * been changed.
	 */
	private void updateLabel() {
		title.setText("<html><div style='text-align: center;'>" + Language.get("attack_title") + "<br>"
				+ Language.get("attack_text", attacker, defender) + "</div></html>");
	}

	/**
	 * Getter for the troop amount selected by the slider on the label.
	 * 
	 * @return the amount of troops currently selected.
	 */
	public int getTroopcount() {
		return troopCount.getValue();
	}

	/**
	 * Sets the attacker that is to be displayed below the title.
	 * 
	 * @param attacker is the String that is to be put into the attacker space.
	 *                 Should attacker be null an empty String will replace it and
	 *                 the space will be empty.
	 */
	public void setAttacker(String attacker) {
		if (attacker == null)
			attacker = "";

		this.attacker = attacker;
		updateLabel();
	}

	/**
	 * Sets the defender that is to be displayed below the title.
	 * 
	 * @param defender is the String that is to be put into the defender space.
	 *                 Should defender be null an empty String will replace it an
	 *                 the space will be empty.
	 */
	public void setDefender(String defender) {
		if (defender == null)
			defender = "";

		this.defender = defender;
		updateLabel();
	}

	/**
	 * This method adds a new {@link AttackLabelListener} to the list of listeners
	 * and it's method will be called whenever an attackAction was performed.
	 * 
	 * @param listener is the listener that is to be added.
	 */
	public void addAttackLabelListener(AttackLabelListener listener) {
		listeners.add(listener);
	}

	/**
	 * This method removes an {@link AttackLabelListener} from the list of
	 * listeners. This means that it's method will no longer be called.
	 * 
	 * @param listener is the {@link Listener} that is to be removed.
	 */
	public void removeAttackLabelListener(AttackLabelListener listener) {
		listeners.add(listener);
	}

	/**
	 * This primitive interface facilitates the method that is to be called when an
	 * AttackAction was performed.
	 * 
	 * @author Niklas S.
	 *
	 */
	public interface AttackLabelListener {

		/**
		 * This value will be passed whenever the attack button was pressed.
		 */
		public static final int TYPE_ATTACK = 0;
		/**
		 * This value will be passed whenever the dismiss button was pressed.
		 */
		public static final int TYPE_DISMISS = 1;
		/**
		 * This value will be passed whenever the value of the troop slider was changed.
		 * The method will not be called while the user is currently editing the slider
		 * but after the mouse releases it will always be called even when the value is
		 * the same that is was before.
		 */
		public static final int TYPE_TROOP_COUNT_CHANGED = 2;

		/**
		 * This method is called whenever an attackAction was performed.
		 * 
		 * @param type is the type of action that was performed. Possible Values are
		 *             AttackLabelListener.TYPE_ATTACK, AttackLabelListener.TYPE_DISMISS
		 *             and AttackLabelListener.TYPE_TROOP_COUNT_CHANGED.
		 */
		public void attackActionPerformed(int type);
	}
}
