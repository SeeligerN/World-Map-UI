package ui;

import ui.Map.Country;

public class ClickEvent {
	
	private Country c;
	
	public ClickEvent(Country c) {
		this.c = c;
	}
	
	public Country getCountry() {
		return c;
	}
}
