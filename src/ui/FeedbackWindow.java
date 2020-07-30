package ui;

import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

/**
 * This class provides a way to give Feedback to the User after the end of one
 * round by showing results of the dice and summing up who lost what and what is
 * happening to the conflicted Country.
 * 
 * @author Niklas S.
 *
 */
public class FeedbackWindow {

	private JDialog dialog;

	private JLabel titleLabel, action, dice, result;
	private JSeparator sep1, sep2;

	/**
	 * Constructor to create a Feedback window and show it immedeately.
	 * 
	 * @param parent              is the parent JFrame object above which the dialog
	 *                            will always appear.
	 * @param title               is the Window title on the Feedbackwindow.
	 * @param attacker            is the String representation of the attacker.
	 * @param defender            is the String representation of the defender.
	 * @param attackedCountry     is the String representation of the attacked
	 *                            country.
	 * @param originCountry       is the String representation of the country from
	 *                            which the attacked country is being attacked.
	 * @param diceResultsAttacker are the results of the dice throws of the attacker
	 *                            in an Integer array.
	 * @param diceResultsDefender are the results of the dice throws of the defender
	 *                            in an Integer array.
	 * @param lossesAttacker      are the Troops of the Attacker lost in the battle.
	 * @param lossesDefender      are the Troops of the defender lost in the battle.
	 * @param resultText          is the short summary of the fight that is
	 *                            displayed below the lower separator.
	 */
	public FeedbackWindow(JFrame parent, String title, String attacker, String defender, String attackedCountry,
			String originCountry, int[] diceResultsAttacker, int[] diceResultsDefender, int lossesAttacker,
			int lossesDefender, String resultText) { // TODO: add null checks
		dialog = new JDialog(parent);

		titleLabel = new JLabel(
				"<html><div style='text-align: center;font-size: 16'>" + "<b>" + title + "</b></div></html>",
				SwingConstants.CENTER);
		titleLabel.setFont(new Font("Dialog", Font.PLAIN, 12));

		action = new JLabel(
				"<html><div style='text-align:center;'>" + Language.get("feedback_text", attacker, defender, originCountry, attackedCountry) + "</div></html>",
				SwingConstants.CENTER);
		action.setFont(new Font("Dialog", Font.PLAIN, 12));

		String diceResultsAttackerS = diceResultsAttacker[0] + "";
		for (int i = 1; i < diceResultsAttacker.length; i++)
			diceResultsAttackerS += ", " + diceResultsAttacker[i];
		String diceResultsDefenderS = diceResultsDefender[0] + "";
		for (int i = 1; i < diceResultsDefender.length; i++)
			diceResultsDefenderS += ", " + diceResultsDefender[i];

		dice = new JLabel(
				"<html><table>" + "<tr><td></td><td>" + Language.get("feedback_attacker") + "</td><td>" + Language.get("feedback_defender") + "</td></tr>"
						+ "<tr><td>" + Language.get("feedback_dice") + "</td><td><b>" + diceResultsAttackerS + "</b></td><td><b>"
						+ diceResultsDefenderS + "</b></td></tr>" + "<tr><td>" + Language.get("feedback_losses") + "</td><td><b>" + lossesAttacker
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
