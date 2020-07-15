package ui.controlBar;

import java.awt.Dimension;
import java.awt.Font;

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

	@SuppressWarnings("unused")
	private String instigator;
	@SuppressWarnings("unused")
	private String defender;
	
	AttackLabel() {
		this.setMaximumSize(new Dimension(9999999, 500));
		
		title = new JLabel("<html><div style='text-align: center;'>"
				+ "<a style='font-size: 16'><b>Attack</b></a><br>"
				+ "<b>Greenland</b> attacks <b>Quebec</b>"
				+ "</div></html>", SwingConstants.CENTER);
		title.setFont(new Font("Dialog", Font.PLAIN, 12));
		
		troopCount = new JSlider(1, 4, 1);
		troopCount.setMajorTickSpacing(1);
		troopCount.setPaintLabels(true);
		troopCount.setOpaque(false);

		attackButton = new JButton("Attack!");
		
		dismissButton = new JButton("Dismiss");
		
		GroupLayout gl = new GroupLayout(this);
		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);
		
		gl.setVerticalGroup(gl.createSequentialGroup()
				.addComponent(title)
				.addComponent(troopCount)
				.addGap(0, 50, 9999)
				.addGroup(gl.createParallelGroup(GroupLayout.Alignment.TRAILING)
						.addComponent(dismissButton)
						.addComponent(attackButton)));
		
		gl.setHorizontalGroup(gl.createSequentialGroup()
				.addGroup(gl.createParallelGroup()
					.addComponent(title)
					.addComponent(troopCount)
					.addComponent(dismissButton)
					.addComponent(attackButton, GroupLayout.Alignment.TRAILING)));
		
		this.setLayout(gl);
	}
}
