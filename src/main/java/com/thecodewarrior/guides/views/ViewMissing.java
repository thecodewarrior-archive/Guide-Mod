package com.thecodewarrior.guides.views;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.StatCollector;

import com.thecodewarrior.guides.api.GuideRegistry;
import com.thecodewarrior.guides.gui.GuiBookOfRevealing;

public class ViewMissing extends ViewError {

	public String name;
	
	public ViewMissing(String name, int width, int height, GuiBookOfRevealing gui) {
		super(width, height, gui);
		this.name = name;
	}

	@Override
	public void init() {}
	
	@Override
	public String getTitleText() {
		return StatCollector.translateToLocal("guidemod.view.error.missing.title");
	}

	@Override
	public List<String> getDescLines() {
		ArrayList<String> l = new ArrayList<String>();
		String unLocName = "";
		if(name.equals(GuideRegistry.getGuideName(name))) {
			unLocName = "guidemod.view.error.missing.descNoName.";
		} else {
			unLocName = "guidemod.view.error.missing.descNameID.";
		}
		for(int i = 0; StatCollector.canTranslate(unLocName + i); i++) {
			l.add( String.format(StatCollector.translateToLocal(unLocName + i), name, GuideRegistry.getGuideName(name)) );
		}
		return l;
		
	}

}
