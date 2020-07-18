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

public class MoveLabel extends JLabel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JLabel title;
	private JSlider troopCount;
	private JButton moveButton;
	
	private String moveFrom;
	private String moveTo;
	
	private List<MoveLabelListener> listeners;
	
	public MoveLabel(String moveFrom, String moveTo, int minTroops, int maxTroops) {
		listeners = new ArrayList<>();
		
		if (moveFrom == null)
			moveFrom = "";
		if (moveTo == null)
			moveTo = "";
		this.moveFrom = moveFrom;
		this.moveTo = moveTo;
		
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
				for (MoveLabelListener dl : listeners)
					dl.moveActionPerformed(MoveLabelListener.TYPE_TROOP_COUNT_CHANGED);
		});

		moveButton = new JButton("Move");
		
		GroupLayout gl = new GroupLayout(this);
		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);
		
		gl.setVerticalGroup(gl.createSequentialGroup()
				.addComponent(title)
				.addComponent(troopCount)
				.addGap(0, 50, 9999)
				.addComponent(moveButton));
		
		gl.setHorizontalGroup(gl.createSequentialGroup()
				.addGroup(gl.createParallelGroup()
					.addComponent(title)
					.addComponent(troopCount)
					.addComponent(moveButton, GroupLayout.Alignment.TRAILING)));
		
		this.setLayout(gl);
	}
	
	private void updateLabel() {
		title.setText("<html><div style='text-align: center;'>"
				+ "<a style='font-size: 16'><b>Move</b></a><br>"
				+ "<b>" + moveFrom + "</b> moves to <b>" + moveTo + "</b>"
				+ "</div></html>");
	}
	
	public void setMoveFrom(String moveFrom) {
		this.moveFrom = moveFrom;
		updateLabel();
	}
	
	public void setMoveTo(String moveTo) {
		this.moveTo = moveTo;
		updateLabel();
	}
	
	public int getTroopCount() {
		return troopCount.getValue();
	}
	
	public void addMoveLabelListener(MoveLabelListener listener) {
		listeners.add(listener);
	}
	
	public void removeMoveLabelListener(MoveLabelListener listener) {
		listeners.remove(listener);
	}
	
	public interface MoveLabelListener {
		
		public static final int TYPE_MOVE = 0;
		public static final int TYPE_TROOP_COUNT_CHANGED = 2;
		
		public void moveActionPerformed(int type);
		
	}
}
