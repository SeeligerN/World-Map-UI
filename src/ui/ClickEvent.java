package ui;

import ui.Map.Country;

/**
 * This class is the Event that is created when a click occurs on the Map. It
 * stores the Country below the cursor at that time. The Country will be null if
 * the Ocean was clicked.
 * 
 * @author Niklas S.
 *
 */
public class ClickEvent {

	private Country c;

	/**
	 * Constructor specifying the Country.
	 * 
	 * @param c the Country the click event was performed on.
	 */
	public ClickEvent(Country c) {
		this.c = c;
	}

	/**
	 * Getter for the specified Country.
	 * 
	 * @return the Country the click event was performed on.
	 */
	public Country getCountry() {
		return c;
	}
}
