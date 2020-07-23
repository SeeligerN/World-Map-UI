package ui;

/**
 * This primitive interface is designed to handle Listener Events.
 * 
 * @author Niklas S.
 *
 */
public interface ClickListener {

	/**
	 * This method is to be called when a ClickEvent occurs.
	 * 
	 * @param e the ClickEvent housing all relevant information of the click.
	 */
	public void handleClick(ClickEvent e);
}
