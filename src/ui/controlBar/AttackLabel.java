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

public class AttackLabel extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel title;
	private JSlider troopCount;
	private JButton attackButton, dismissButton;

	private String attacker;
	private String defender;

	private List<AttackLabelListener> listeners;

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

		attackButton = new JButton("Attack!");
		attackButton.addActionListener(ae -> {
			for (AttackLabelListener al : listeners)
				al.attackActionPerformed(AttackLabelListener.TYPE_ATTACK);
		});

		dismissButton = new JButton("Dismiss");
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

	private void updateLabel() {
		title.setText("<html><div style='text-align: center;'>" + "<a style='font-size: 16'><b>Attack</b></a><br>"
				+ "<b>" + attacker + "</b> attacks <b>" + defender + "</b></div></html>");
	}

	public int getTroopcount() {
		return troopCount.getValue();
	}

	public void setAttacker(String attacker) {
		if (attacker == null)
			attacker = "";
		
		this.attacker = attacker;
		updateLabel();
	}

	public void setDefender(String defender) {
		if (defender == null)
			defender = "";
		
		this.defender = defender;
		updateLabel();
	}
	
	public void addAttackLabelListener(AttackLabelListener listener) {
		listeners.add(listener);
	}
	
	public void removeAttackLabelListener(AttackLabelListener listener) {
		listeners.add(listener);
	}

	public interface AttackLabelListener {

		public static final int TYPE_ATTACK = 0;
		public static final int TYPE_DISMISS = 1;
		public static final int TYPE_TROOP_COUNT_CHANGED = 2;

		public void attackActionPerformed(int type);
	}
}
