package ui.controlBar;

import java.awt.Dimension;

import javax.swing.GroupLayout;
import javax.swing.JLabel;

import ui.Window;

/**
 * This class manages and houses the three slots in the controlBar.
 * 
 * @author Niklas S.
 *
 */
public class ControlLabel extends JLabel {

	private static final long serialVersionUID = 1L;

	private JLabel leftLabel, centerLabel, rightLabel;

	/**
	 * This constructor creates a new ControlLabel with all empty Labels.
	 */
	public ControlLabel() {
		setHeight(0);

		leftLabel = new JLabel();
		centerLabel = new JLabel();
		rightLabel = new JLabel();

		updateLayout();
	}

	/**
	 * This method updates the layout to include the currently saved labels.
	 */
	private void updateLayout() {
		GroupLayout gl = new GroupLayout(this);
		gl.setAutoCreateContainerGaps(true);

		gl.setHorizontalGroup(
				gl.createSequentialGroup().addComponent(leftLabel).addComponent(centerLabel).addComponent(rightLabel));

		gl.setVerticalGroup(gl.createParallelGroup().addComponent(leftLabel, GroupLayout.Alignment.LEADING)
				.addComponent(centerLabel, GroupLayout.Alignment.CENTER)
				.addComponent(rightLabel, GroupLayout.Alignment.TRAILING));

		this.setLayout(gl);
	}

	/**
	 * This method sets the specified label to be in the specified space. Should one
	 * or two label be empty the remaining labels will stretch to fill their space.
	 * 
	 * @param labelLoc   is the label location that is to be updated. Possible
	 *                   values are {@link Window}.LEFT_LABEL,
	 *                   {@link Window}.CENTER_LABEL or {@link Window}.RIGHT_LABEL.
	 *                   Should labelLoc be none of these values nothing will
	 *                   happen.
	 * @param labelToSet is the label that is to be put in the specified spot. Any
	 *                   JLabel can be placed in one of the slots. Should labelToSet
	 *                   be null an empty JLabel will replace it and the remaining
	 *                   labels will fill it's place.
	 */
	public void setLabel(int labelLoc, JLabel labelToSet) {

		if (labelToSet == null)
			labelToSet = new JLabel(); // if null was given a new empty JLabel will be created in its place. The
										// location will be empty.

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

	/**
	 * This method sets the height of the control bar. 
	 * 
	 * @param height is the new height that the control bar will occupy. 
	 */
	public void setHeight(int height) {
		this.setMaximumSize(new Dimension(99999999, height));
	}
}
