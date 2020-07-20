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

public class FortifyingLabel extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel title;
	private JSlider troopCount;
	private JButton fortifyButton;

	private String fortifyingCountry;

	private List<FortifyLabelListener> listeners;

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

		fortifyButton = new JButton("Fortify");
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

	public void setFortifyingCountry(String attackedCountry) {
		if (attackedCountry == null)
			attackedCountry = "";
		
		this.fortifyingCountry = attackedCountry;
		updateLabel();
	}

	public int getTroopCount() {
		return troopCount.getValue();
	}

	public void addFortifyLabelListener(FortifyLabelListener listener) {
		listeners.add(listener);
	}

	public void removeFortifyLabelListener(FortifyLabelListener listener) {
		listeners.remove(listener);
	}

	private void updateLabel() {
		title.setText("<html><div style='text-align: center;'>" + "<a style='font-size: 16'><b>Fortify</b></a><br>"
				+ "fortify <b>" + fortifyingCountry + "</b>" + "</div></html>");
	}

	public interface FortifyLabelListener {

		public static final int TYPE_FORTIFY = 0;
		public static final int TYPE_TROOP_COUNT_CHANGED = 2;

		public void fortifyActionPerformed(int type);

	}
}
