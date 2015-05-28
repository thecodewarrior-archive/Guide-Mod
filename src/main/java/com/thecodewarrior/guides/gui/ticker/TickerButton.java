package com.thecodewarrior.guides.gui.ticker;

import com.thecodewarrior.guides.gui.GuiBookOfRevealing;

public abstract class TickerButton {

	String text;
	
	public TickerButton(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}
	
	public abstract void click(GuiBookOfRevealing gui);

}
