package com.thecodewarrior.guides.gui.ticker;

import com.thecodewarrior.guides.api.Link;
import com.thecodewarrior.guides.gui.GuiBookOfRevealing;

public class TickerButtonLink extends TickerButton {

	Link link;

	public TickerButtonLink(String text, String link) {
		super(text);
		this.link = new Link(link);
	}
	
	public Link getLink() {
		return link;
	}
	
	public void click(GuiBookOfRevealing gui) {
		link.go(gui);
	}


}