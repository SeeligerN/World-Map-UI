package ui.controlBar;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

import ui.Language;
import ui.controlBar.MoveLabel.MoveLabelListener;

/**
 * This class is the label that is to be visible whenever a player is initially
 * preparing for the game. The label shows a specified country as well as
 * provide the user to choose fast mode.
 * 
 * @author Niklas S.
 *
 */
public class PreparationLabel extends JLabel {

	private static final long serialVersionUID = 1L;

	private JLabel title;
	private JCheckBox fastMode;
	private JButton deployButton;

	private int troops;
	private String country;

	private List<PreparationLabelListener> listeners;

	/**
	 * The constructor creates a new PreparationLabel without attaching it to any
	 * Window. The user can set whether or not to use fast mode.
	 * 
	 * @param troops  is how many troops are to be deployed.
	 * @param country is the country the troops are being deployed to. Should
	 *                country be null it will be replaced with an empty String and
	 *                the space remains empty.
	 */
	public PreparationLabel(int troops, String country) {
		if (country == null)
			this.country = country;
		this.troops = troops;
		this.country = country;

		listeners = new ArrayList<>();

		this.setMaximumSize(new Dimension(9999999, 500));

		title = new JLabel("", SwingConstants.CENTER);
		title.setFont(new Font("Dialog", Font.PLAIN, 12));
		updateLabel();

		fastMode = new JCheckBox(Language.get("preparation_fast"));
		fastMode.setFont(new Font("Dialog", Font.PLAIN, 12));
		fastMode.setOpaque(false);
		fastMode.addChangeListener(e -> {
			for (PreparationLabelListener dl : listeners)
				dl.preparationActionPerformed(PreparationLabelListener.TYPE_FAST_MODE_CHANGED);
		});

		deployButton = new JButton(Language.get("preparation_confirm"));
		deployButton.addActionListener(a -> {
			for (PreparationLabelListener dl : listeners)
				dl.preparationActionPerformed(PreparationLabelListener.TYPE_DEPLOY);
		});

		GroupLayout gl = new GroupLayout(this);
		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);

		gl.setVerticalGroup(gl.createSequentialGroup().addComponent(title).addComponent(fastMode).addGap(0, 50, 9999)
				.addComponent(deployButton));

		gl.setHorizontalGroup(gl.createSequentialGroup()
				.addGroup(gl.createParallelGroup().addComponent(title)
						.addComponent(fastMode, GroupLayout.Alignment.CENTER)
						.addComponent(deployButton, GroupLayout.Alignment.TRAILING)));

		this.setLayout(gl);
	}

	/**
	 * This method updates the title label with the relevant information after it
	 * might have changed. Automatically deciding whether the text should be
	 * singular or plural.
	 */
	private void updateLabel() {
		title.setText("<html><div style='text-align: center;'>" + Language.get("preparation_title") + "<br>"
				+ Language.get("preparation_text_" + (troops == 1 || troops == -1 ? "s" : "m"), troops, country)
				+ "</div></html>");
	}

	/**
	 * Getter for whether or not fast mode is selected.
	 * 
	 * @return true if the fast mode is selected false otherwise.
	 */
	public boolean getFastMood() {
		return fastMode.isSelected();
	}

	/**
	 * This method updates the troop amount with a new specified amount.
	 * 
	 * @param troops is the new troop amount to be displayed.
	 */
	public void setTroops(int troops) {
		this.troops = troops;
		updateLabel();
	}

	/**
	 * This method updates the country that troops are being deployed to.
	 * 
	 * @param country the new country that is to be deployed. Should country be null
	 *                the space will be empty.
	 */
	public void setCountry(String country) {
		if (country == null)
			country = "";

		this.country = country;
		updateLabel();
	}

	/**
	 * This method adds a new {@link PreparationLabelListener} to the list of
	 * listeners and it's method will be called whenever a preparationAction was
	 * performed.
	 * 
	 * @param listener is the listener that is to be added.
	 */
	public void addPreparationLabelListener(PreparationLabelListener listener) {
		listeners.add(listener);
	}

	/**
	 * This method removes a {@link MoveLabelListener} from the list of listeners.
	 * This means that it's method will no longer be called.
	 * 
	 * @param listener is the listener that is to be removed.
	 */
	public void removePreparationLabelListener(PreparationLabelListener listener) {
		listeners.remove(listener);
	}

	/**
	 * This primitive interface facilitates the method that is to be called when a
	 * preparationAction was performed.
	 * 
	 * @author Niklas S.
	 *
	 */
	public interface PreparationLabelListener {

		/**
		 * This value will be passed whenever the deploy button was pressed.
		 */
		public static final int TYPE_DEPLOY = 0;
		/**
		 * This value will be passed whenever the fast mode was activated or
		 * deactivated.
		 */
		public static final int TYPE_FAST_MODE_CHANGED = 2;

		/**
		 * This method is called whenev a preparationAction was performed.
		 * 
		 * @param type is the type of action that was performed. Possible values are
		 *             PreparationLabelListener.TYPE_DEPLOY and
		 *             PreparationLabelListener.TYPE_FAST_MODE_CHANGED.
		 */
		public void preparationActionPerformed(int type);

	}
}
