package com.thecodewarrior.guides.api;

import com.thecodewarrior.guides.gui.GuiBookOfRevealing;
import com.thecodewarrior.guides.views.View;

public abstract class GuideGenerator {
	public GuideGenerator() {}
	
	public abstract View generate(int width, int height, GuiBookOfRevealing gui);
}
