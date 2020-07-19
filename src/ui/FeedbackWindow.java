package ui;

import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

public class FeedbackWindow {

	private JDialog dialog;

	private JLabel titleLabel, action, dice, result;
	private JSeparator sep1, sep2;

	public FeedbackWindow(JFrame parent, String title, String attacker, String defender, String attackedCountry,
			String originCountry, int[] diceResultsAttacker, int[] diceResultsDefender, int lossesAttacker,
			int lossesDefender, String resultText) {
		dialog = new JDialog(parent);

		titleLabel = new JLabel(
				"<html><div style='text-align: center;font-size: 16'>" + "<b>" + title + "</b></div></html>",
				SwingConstants.CENTER);
		titleLabel.setFont(new Font("Dialog", Font.PLAIN, 12));

		action = new JLabel(
				"<html><div style='text-align:center;'><b>" + attacker + "</b> attacked <b>" + defender
						+ "</b><br>in <b>" + attackedCountry + "</b> from <b>" + originCountry + "</b></div></html>",
				SwingConstants.CENTER);
		action.setFont(new Font("Dialog", Font.PLAIN, 12));

		String diceResultsAttackerS = diceResultsAttacker[0] + "";
		for (int i = 1; i < diceResultsAttacker.length; i++)
			diceResultsAttackerS += ", " + diceResultsAttacker[i];
		String diceResultsDefenderS = diceResultsDefender[0] + "";
		for (int i = 1; i < diceResultsDefender.length; i++)
			diceResultsDefenderS += ", " + diceResultsDefender[i];

		dice = new JLabel(
				"<html><table>" + "<tr><td></td><td>Attacker</td><td>Defender</td></tr>"
						+ "<tr><td>Dice results</td><td><b>" + diceResultsAttackerS + "</b></td><td><b>"
						+ diceResultsDefenderS + "</b></td></tr>" + "<tr><td>Losses</td><td><b>" + lossesAttacker
						+ "</b></td><td><b>" + lossesDefender + "</b></td></tr>" + "</table></html>",
				SwingConstants.CENTER);
		dice.setFont(new Font("Dialog", Font.PLAIN, 12));

		result = new JLabel("<html><div style='text-align:center;'><b>" + resultText + "</b></div></html>",
				SwingConstants.CENTER);
		result.setFont(new Font("Dialog", Font.PLAIN, 12));

		sep1 = new JSeparator();
		sep2 = new JSeparator();

		GroupLayout gl = new GroupLayout(dialog.getContentPane());
		gl.setAutoCreateContainerGaps(true);
		gl.setAutoCreateGaps(true);

		gl.setVerticalGroup(gl.createSequentialGroup().addComponent(titleLabel).addComponent(action).addComponent(sep1)
				.addComponent(dice).addComponent(sep2).addComponent(result));

		gl.setHorizontalGroup(gl.createParallelGroup().addComponent(titleLabel).addComponent(action).addComponent(sep1)
				.addComponent(dice).addComponent(sep2).addComponent(result));

		dialog.setLayout(gl);

		dialog.setTitle("Success");
		dialog.pack();
		dialog.setLocationRelativeTo(null);
		dialog.setResizable(false);
		dialog.setVisible(true);
	}
}
