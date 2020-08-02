package ui.controlBar;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import ui.Language;

/**
 * This class is the label that is shows status information such as the
 * reinforcements and bonuses of the current player. In addition this label also
 * houses the map mode selector.
 * <p>
 * The label listens for events in the label which are then passed to all added
 * listeners.
 * 
 * @author Niklas S.
 *
 */
public class StatusLabel extends JLabel {

	private static final long serialVersionUID = 1L;

	private JLabel title;
	private JLabel status;
	private JSeparator sep;
	private JComboBox<String> mapModeSelector;

	private String reinforcements;
	private String bonus;

	private List<StatusLabelListener> listeners;

	/**
	 * The constructor creates a new StatusLabel without attaching it to any Window.
	 * The user sees reinforcement amounts and bonuses and is able to select the
	 * current map mode.
	 * 
	 * @param mapModes       are all possible map modes that will be selectable by
	 *                       the user. Should mapModes be null there will be no
	 *                       mapModes selectable. Should an element be empty or null
	 *                       it will be omitted from the the mapModes.
	 * @param reinforcements is the reinforcements String that is to be displayed
	 *                       next to the reinforcements label. Should reinforcements
	 *                       be null the space will remain empty.
	 * @param bonus          is the bonus String that is to be displayed next to the
	 *                       bonus label. Should bonus be null the space will remain
	 *                       empty.
	 */
	public StatusLabel(String[] mapModes, String reinforcements, String bonus) {
		if (reinforcements == null)
			reinforcements = "";
		if (bonus == null)
			bonus = "";
		this.reinforcements = reinforcements;
		this.bonus = bonus;

		if (mapModes == null)
			mapModes = new String[] {};

		List<String> newMapModes = new ArrayList<>();
		for (String s : mapModes)
			if (s != null && !s.isEmpty())
				newMapModes.add(s);

		listeners = new ArrayList<>();

		this.setMaximumSize(new Dimension(9999999, 500));

		title = new JLabel("<html><div style='text-align: center;'>" + Language.get("status_title") + "</div>",
				SwingConstants.CENTER);
		title.setFont(new Font("Dialog", Font.PLAIN, 12));

		status = new JLabel("");
		status.setFont(new Font("Dialog", Font.PLAIN, 12));
		updateLabel();

		sep = new JSeparator(SwingConstants.HORIZONTAL);

		mapModeSelector = new JComboBox<>(mapModes);
		mapModeSelector.setMaximumSize(new Dimension(99999999, 20));
		mapModeSelector.setFont(new Font("Dialog", Font.PLAIN, 12));
		mapModeSelector.addActionListener(ae -> {
			for (StatusLabelListener sl : listeners)
				sl.statusActionPerformed(StatusLabelListener.TYPE_MAP_MODE_CHANGED);
		});

		GroupLayout gl = new GroupLayout(this);
		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);

		gl.setVerticalGroup(gl.createSequentialGroup().addComponent(title).addComponent(status).addComponent(sep)
				.addComponent(mapModeSelector));

		gl.setHorizontalGroup(gl.createSequentialGroup().addGroup(gl.createParallelGroup().addComponent(title)
				.addComponent(status).addComponent(sep).addComponent(mapModeSelector)));

		this.setLayout(gl);
	}

	/**
	 * This method updates the label table with all information that may have been
	 * changed.
	 */
	private void updateLabel() {
		status.setText("<html><head><style>td { padding: 0px; }</style></head><table>" + "<tr><td>"
				+ Language.get("status_reinf") + "</td><td><b>" + reinforcements + "</b></td></tr>" + "<tr><td>"
				+ Language.get("status_bonus") + "</td><td><b>" + bonus + "</b></td></tr>" + "</table></html>");
	}

	/**
	 * This method updates the reinforcement String that is displayed with a new
	 * String.
	 * 
	 * @param reinforcements the new reinforcements String. Should reinforcements be
	 *                       null the space will turn empty.
	 */
	public void setReinforcements(String reinforcements) {
		if (reinforcements == null)
			reinforcements = "";
		this.reinforcements = reinforcements;
		updateLabel();
	}

	/**
	 * This method updates the bonus String that is displayed with a new String.
	 * 
	 * @param bonus is the new bonus String. Should bonus be null the space will
	 *              turn empty.
	 */
	public void setBonus(String bonus) {
		if (bonus == null)
			bonus = "";
		this.bonus = bonus;
		updateLabel();
	}

	/**
	 * Getter for the currently selected map mode.
	 * 
	 * @return the object initially passed in the array that was now selected.
	 */
	public String getSelectedMapMode() {
		return (String) mapModeSelector.getSelectedItem();
	}

	/**
	 * This method adds a new {@link StatusLabelListener} to the list of listeners
	 * and it's method will be called whenever a statusAction was performed.
	 * 
	 * @param listener is the listener that is to be added.
	 */
	public void addStatusLabelListener(StatusLabelListener listener) {
		listeners.add(listener);
	}

	/**
	 * This method removes a {@link StatusLabelListener} from the list of listeners.
	 * This means that it's method will no longer be called.
	 * 
	 * @param listener is the listener that is to be removed.
	 */
	public void removeStatusLabelListener(StatusLabelListener listener) {
		listeners.remove(listener);
	}

	/**
	 * This primitive interface facilitates the method that is to be called when a
	 * moveAction was performed.
	 * 
	 * @author Niklas S.
	 *
	 */
	public interface StatusLabelListener {

		/**
		 * This value will be passed whenever the mapmode was updated. It will also be
		 * called if it didn't change and it was only selected again.
		 * <p>
		 * Currently this if the only value that can be passed.
		 */
		public static final int TYPE_MAP_MODE_CHANGED = 0;

		/**
		 * This method is called whenever a statusAction was performed.
		 * 
		 * @param tpye is the type of action that was performed. The only possible value
		 *             that can be passed is StatusLabelListener.TYPE_MAP_MODE_CHANGED.
		 */
		public void statusActionPerformed(int tpye);

	}
}