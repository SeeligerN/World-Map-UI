package ui.controlBar;

import java.awt.Dimension;
import java.awt.Font;

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
	
	String fortifyCountry;
	
	FortifyingLabel() {
		this.setMaximumSize(new Dimension(9999999, 500));
		
		title = new JLabel("<html><div style='text-align: center;'>"
				+ "<a style='font-size: 16'><b>Fortify</b></a><br>"
				+ "fortify <b>Quebec</b>"
				+ "</div></html>", SwingConstants.CENTER);
		title.setFont(new Font("Dialog", Font.PLAIN, 12));
		
		troopCount = new JSlider(1, 4, 1);
		troopCount.setMajorTickSpacing(1);
		troopCount.setPaintLabels(true);
		troopCount.setOpaque(false);

		fortifyButton = new JButton("Fortify");
		
		GroupLayout gl = new GroupLayout(this);
		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);
		
		gl.setVerticalGroup(gl.createSequentialGroup()
				.addComponent(title)
				.addComponent(troopCount)
				.addGap(0, 50, 9999)
				.addComponent(fortifyButton));
		
		gl.setHorizontalGroup(gl.createSequentialGroup()
				.addGroup(gl.createParallelGroup()
					.addComponent(title)
					.addComponent(troopCount)
					.addComponent(fortifyButton, GroupLayout.Alignment.TRAILING)));
		
		this.setLayout(gl);
	}
}
