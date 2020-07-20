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

public class StatusLabel extends JLabel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel title;
	private JLabel status;
	private JSeparator sep;
	private JComboBox<String> mapModeSelector;
	
	private String reinforcements;
	private String bonus;
	
	private List<StatusLabelListener> listeners;
	
	public StatusLabel(String[] mapModes, String reinforcements, String bonus) {
		if (reinforcements == null)
			reinforcements = "";
		if (bonus == null)
			bonus = "";
		this.reinforcements = reinforcements;
		this.bonus = bonus;
		
		if (mapModes == null)
			mapModes = new String[] {""};
		
		listeners = new ArrayList<>();
		
		this.setMaximumSize(new Dimension(9999999, 500));

		title = new JLabel("<html><div style='text-align: center;font-size: 16'>"
				+ "<b>Player Stats</b></div>", SwingConstants.CENTER);
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
		
		gl.setVerticalGroup(gl.createSequentialGroup()
				.addComponent(title)
				.addComponent(status)
				.addComponent(sep)
				.addComponent(mapModeSelector));
		
		gl.setHorizontalGroup(gl.createSequentialGroup()
				.addGroup(gl.createParallelGroup()
					.addComponent(title)
					.addComponent(status)
					.addComponent(sep)
					.addComponent(mapModeSelector)));
		
		this.setLayout(gl);
	}
	
	private void updateLabel() {
		status.setText("<html><head><style>td { padding: 0px; }</style></head><table>"
				+ "<tr><td>Reinforcements : </td><td><b>" + reinforcements + "</b></td></tr>"
				+ "<tr><td>Bonus : </td><td><b>" + bonus + "</b></td></tr>"
				+ "</table></html>");
	}
	
	public void setReinforcements(String reinforcements) {
		this.reinforcements = reinforcements;
		updateLabel();
	}
	
	public void setBonus(String bonus) {
		this.bonus = bonus;
		updateLabel();
	}
	
	public String getSelectedMapMode() {
		return (String) mapModeSelector.getSelectedItem();
	}
	
	public void addStatusLabelListener(StatusLabelListener listener) {
		listeners.add(listener);
	}
	
	public void removeStatusLabelListener(StatusLabelListener listener) {
		listeners.remove(listener);
	}
	
	public interface StatusLabelListener {
		
		public static final int TYPE_MAP_MODE_CHANGED = 0;
		
		public void statusActionPerformed(int tpye);
		
	}
}