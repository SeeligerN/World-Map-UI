package ui.controlBar;

import java.awt.Dimension;

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
	
	private JLabel status;
	private JSeparator sep;
	private JComboBox<String> mapModeSelector;
	
	public StatusLabel() {

		this.setMaximumSize(new Dimension(9999999, 500));
		
		status = new JLabel("<html>Reinforcements : 3 <br>Bonus : 2</html>");
		
		sep = new JSeparator(SwingConstants.HORIZONTAL);
		
		mapModeSelector = new JComboBox<>(new String[] {"geographical", "political"});
		mapModeSelector.setMaximumSize(new Dimension(99999999, 20));
		
		GroupLayout gl = new GroupLayout(this);
		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);
		
		gl.setVerticalGroup(gl.createSequentialGroup()
				.addComponent(status)
				.addComponent(sep)
				.addComponent(mapModeSelector));
		
		gl.setHorizontalGroup(gl.createParallelGroup()
				.addComponent(status)
				.addComponent(sep)
				.addComponent(mapModeSelector));
		
		this.setLayout(gl);
	}
}