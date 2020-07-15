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
		setHeight(130);
		
		leftLabel = new StatusLabel();
		
		centerLabel = new StatusLabel();
		
		rightLabel = new StatusLabel();
		
		
		GroupLayout gl = new GroupLayout(this);
		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);
		
		gl.setHorizontalGroup(gl.createSequentialGroup()
				.addComponent(leftLabel)
				.addComponent(centerLabel)
				.addComponent(rightLabel));
		
		gl.setVerticalGroup(gl.createParallelGroup(GroupLayout.Alignment.CENTER)
				.addComponent(leftLabel)
				.addComponent(centerLabel)
				.addComponent(rightLabel));
		
		this.setLayout(gl);
	}
	
	public void setLabel(int labelLoc, JLabel labelToSet) {
		if (labelToSet == null)
			labelToSet = new JLabel(); // if null was given a new empty JLabel will be created in its place. The location will be empty. 
		
		if (labelLoc == Window.LEFT_LABEL)
			leftLabel = labelToSet;
		if (labelLoc == Window.CENTER_LABEL)
			centerLabel = labelToSet;
		if (labelLoc == Window.RIGHT_LABEL)
			rightLabel = labelToSet;
	
	}
	
	public void setHeight(int height) {
		this.setMaximumSize(new Dimension(99999999, height));
	}
}
