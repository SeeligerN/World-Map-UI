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
 * fortifying one of his countries. The class shows relevant information and
 * listens for events in the label which are then passed to all added listeners.
 * 
 * @author Niklas S.
 *
 */
public class FortifyingLabel extends JLabel {

	private static final long serialVersionUID = 1L;

	private JLabel title;
	private JSlider troopCount;
	private JButton fortifyButton;

	private String fortifyingCountry;

	private List<FortifyLabelListener> listeners;

	/**
	 * The constructor creates a new FortifyingLabel without attaching it to any
	 * Window. The user can select with how many troops they want to fortify a given
	 * country.
	 * 
	 * @param fortifyingCountry is the country that the user is currently
	 *                          fortifying. Should fortifyingCountry be null it will
	 *                          be replaced by an empty String and the space for the
	 *                          country will remain empty.
	 * @param minTroops         is the minimum amount of troops that are selectable
	 *                          by the slider on the label.
	 * @param maxTroops         is the maximum amount of troops that are selectable
	 *                          by the slider on the label. Should maxTroops be
	 *                          smaller than minTroops it will be set to minTroops.
	 */
	public FortifyingLabel(String fortifyingCountry, int minTroops, int maxTroops) {

		listeners = new ArrayList<>();

		if (fortifyingCountry == null)
			fortifyingCountry = "";
		this.fortifyingCountry = fortifyingCountry;

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
				for (FortifyLabelListener dl : listeners)
					dl.fortifyActionPerformed(FortifyLabelListener.TYPE_TROOP_COUNT_CHANGED);
		});

		fortifyButton = new JButton(Language.get("fortify_confirm"));
		fortifyButton.addActionListener(ae -> {
			for (FortifyLabelListener dl : listeners)
				dl.fortifyActionPerformed(FortifyLabelListener.TYPE_FORTIFY);
		});

		GroupLayout gl = new GroupLayout(this);
		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);

		gl.setVerticalGroup(gl.createSequentialGroup().addComponent(title).addComponent(troopCount).addGap(0, 50, 9999)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.TRAILING).addComponent(fortifyButton)));

		gl.setHorizontalGroup(gl.createSequentialGroup().addGroup(gl.createParallelGroup().addComponent(title)
				.addComponent(troopCount).addComponent(fortifyButton, GroupLayout.Alignment.TRAILING)));

		this.setLayout(gl);
	}

	/**
	 * This method updates the fortifying country, displayed below the label title,
	 * with a new one.
	 * 
	 * @param attackedCountry is the new country that is being fortified. Should
	 *                        fortifyingCountry be null the space will be empty.
	 */
	public void setFortifyingCountry(String attackedCountry) {
		if (attackedCountry == null)
			attackedCountry = "";

		this.fortifyingCountry = attackedCountry;
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
	 * This method adds a new {@link FortifyLabelListener} to the list of listeners
	 * and it's method will be called whenever a fortifyEvent was performed.
	 * 
	 * @param listener is the listener that is to be added.
	 */
	public void addFortifyLabelListener(FortifyLabelListener listener) {
		listeners.add(listener);
	}

	/**
	 * This method removes a {@link FortifyLabelListener} from the list of
	 * listeners. This means that it's method will no longer be called.
	 * 
	 * @param listener is the listener that is to be removed.
	 */
	public void removeFortifyLabelListener(FortifyLabelListener listener) {
		listeners.remove(listener);
	}

	/**
	 * This method updates the title label with all the stored information in case
	 * it has changed.
	 */
	private void updateLabel() {
		title.setText("<html><div style='text-align: center;'>" + Language.get("fortify_title") + "<br>"
				+ Language.get("fortify_text", fortifyingCountry) + "</div></html>");
	}

	/**
	 * This primitive interface facilitates the method that is to be called when a
	 * fortifyingAction was performed.
	 * 
	 * @author Niklas S.
	 *
	 */
	public interface FortifyLabelListener {

		/**
		 * This value will be passed whenever the fortifying button was pressed.
		 */
		public static final int TYPE_FORTIFY = 0;
		/**
		 * This value will be passed whenever the value of the troop slider was changed.
		 * The method will not be called while the user is currently editing the slider
		 * but after the mouse releases it will always be called even if the value is
		 * the same that is was previously.
		 */
		public static final int TYPE_TROOP_COUNT_CHANGED = 2;

		/**
		 * This method is called whenever a fortifyingAction was performed.
		 * 
		 * @param type is the type of action that was performed. Possible values are
		 *             FortifyLabelListener.TYPE_FORTIFY and
		 *             FortifyLabelListener.TYPE_TROOP_COUNT_CHANGED.
		 */
		public void fortifyActionPerformed(int type);

	}
}
