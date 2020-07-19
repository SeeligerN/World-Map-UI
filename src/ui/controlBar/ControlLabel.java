package ui.controlBar;

import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.JLabel;

import ui.Window;

public class ControlLabel extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel leftLabel, centerLabel, rightLabel;
	
	public ControlLabel() {
		setHeight(0);
		
		leftLabel = new JLabel();
		centerLabel = new JLabel();
		rightLabel = new JLabel();
		
		updateLayout();
	}
	
	private void updateLayout() {
		GroupLayout gl = new GroupLayout(this);
		gl.setAutoCreateContainerGaps(true);
		
		gl.setHorizontalGroup(gl.createSequentialGroup()
				.addComponent(leftLabel)
				.addComponent(centerLabel)
				.addComponent(rightLabel));
		
		gl.setVerticalGroup(gl.createParallelGroup()
				.addComponent(leftLabel, GroupLayout.Alignment.LEADING)
				.addComponent(centerLabel, GroupLayout.Alignment.CENTER)
				.addComponent(rightLabel, GroupLayout.Alignment.TRAILING));
		
		this.setLayout(gl);
	}
	
	public void setLabel(int labelLoc, JLabel labelToSet) {
		
		if (labelToSet == null)
			labelToSet = new JLabel(); // if null was given a new empty JLabel will be created in its place. The location will be empty. 
		
		if (labelLoc == Window.LEFT_LABEL) {
			this.remove(leftLabel);
			leftLabel = labelToSet;
		}
		if (labelLoc == Window.CENTER_LABEL) {
			this.remove(centerLabel);
			centerLabel = labelToSet;
		}
		if (labelLoc == Window.RIGHT_LABEL) {
			this.remove(rightLabel);
			rightLabel = labelToSet;
		}
		
		updateLayout();
	}
	
	public void setHeight(int height) {
		this.setMaximumSize(new Dimension(99999999, height));
	}
}
