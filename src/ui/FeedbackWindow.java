package ui;

import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class FeedbackWindow {

	private JFrame frame;
	
	private JLabel title, action, dice, result;
	private JSeparator sep1, sep2;
	
	public FeedbackWindow() {
		frame = new JFrame("Feedback");
		
		title = new JLabel("<html><div style='text-align: center;font-size: 16'>"
				+ "<b>Feedback</b></div></html>", SwingConstants.CENTER);
		title.setFont(new Font("Dialog", Font.PLAIN, 12));
		
		action = new JLabel("<html><div style='text-align:center;'><b>player 1</b> attacked <b>player 3</b><br>in <b>Greenland</b> from <b>Quebec</b></div></html>", SwingConstants.CENTER);
		action.setFont(new Font("Dialog", Font.PLAIN, 12));
		
		dice = new JLabel("<html><table>"
				+ "<tr><td></td><td>Attacker</td><td>Defender</td></tr>"
				+ "<tr><td>Dice results</td><td><b>4, 8 ,6</b></td><td><b>1, 2</b></td></tr>"
				+ "<tr><td>Losses</td><td><b>3</b></td><td><b>5</b></td></tr>"
				+ "</table</html>", SwingConstants.CENTER);
		dice.setFont(new Font("Dialog", Font.PLAIN, 12));
		
		result = new JLabel("<html><div style='text-align:center;'><b>Greenland</b> has been captured by <b>player 1</b><br>"
				+ "and is now occupied with <b>3</b> troops</div></html>", SwingConstants.CENTER);
		result.setFont(new Font("Dialog", Font.PLAIN, 12));
		
		sep1 = new JSeparator();
		sep2 = new JSeparator();
		
		GroupLayout gl = new GroupLayout(frame.getContentPane());
		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);
		
		
		gl.setVerticalGroup(gl.createSequentialGroup()
				.addComponent(title)
				.addComponent(action)
				.addComponent(sep1)
				.addComponent(dice)
				.addComponent(sep2)
				.addComponent(result));
		
		gl.setHorizontalGroup(gl.createParallelGroup()
				.addComponent(title)
				.addComponent(action)
				.addComponent(sep1)
				.addComponent(dice)
				.addComponent(sep2)
				.addComponent(result));
		
		frame.setLayout(gl);
		
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setResizable(false);
		frame.setVisible(true);
	}
}
