package com.thecodewarrior.guides.views;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.StatCollector;

import com.thecodewarrior.guides.gui.GuiBookOfRevealing;

public class ViewNoMatch extends ViewError {
	public ViewNoMatch(int width, int height, GuiBookOfRevealing gui) {
		super(width, height, gui);
	}

	@Override
	public void init() {}

	@Override
	public String getTitleText() {
		return StatCollector.translateToLocal("guidemod.view.error.nomatch.title");
	}

	@Override
	public List<String> getDescLines() {
		ArrayList<String> l = new ArrayList<String>();
		String unLocName = "guidemod.view.error.nomatch.desc.";
		for(int i = 0; StatCollector.canTranslate(unLocName + i); i++) {
			l.add( StatCollector.translateToLocal(unLocName + i) );
		}
		return l;
	}

	
	
}
