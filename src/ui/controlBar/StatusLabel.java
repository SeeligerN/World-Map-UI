package ui.controlBar;

import java.awt.Dimension;
import java.awt.Font;

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
	
	public StatusLabel() {
		this.setMaximumSize(new Dimension(9999999, 500));

		title = new JLabel("<html><div style='text-align: center;font-size: 16'>"
				+ "<b>Player Stats</b></div>", SwingConstants.CENTER);
		title.setFont(new Font("Dialog", Font.PLAIN, 12));
		
		status = new JLabel("<html>Reinforcements : 3 <br>Bonus : 2</html>");
		status.setFont(new Font("Dialog", Font.PLAIN, 12));
		
		sep = new JSeparator(SwingConstants.HORIZONTAL);
		
		mapModeSelector = new JComboBox<>(new String[] {"geographical", "political"});
		mapModeSelector.setMaximumSize(new Dimension(99999999, 20));
		mapModeSelector.setFont(new Font("Dialog", Font.PLAIN, 12));
		
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
}