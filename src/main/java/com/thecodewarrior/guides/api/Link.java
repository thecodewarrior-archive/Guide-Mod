package com.thecodewarrior.guides.api;

import java.util.ArrayList;
import java.util.Arrays;

import com.thecodewarrior.guides.GuideMod;
import com.thecodewarrior.guides.gui.GuiBookOfRevealing;
import com.thecodewarrior.guides.guidepack.browse.BrowseItem;
import com.thecodewarrior.guides.guidepack.browse.BrowseItemDirectory;
import com.thecodewarrior.guides.guidepack.browse.BrowseItemDirectoryMod;
import com.thecodewarrior.guides.guidepack.browse.BrowseItemGuide;
import com.thecodewarrior.guides.views.ViewBrowse;

public class Link {

	int type;
	String linkString;
	public static final String[] types = {"guide", "browse"};
	
	public Link(String link) {
		String[] split = link.split("~", 2);
		if(split.length < 2) {
			type = 0;
			linkString = link;
		} else {
			type = parseType(split[0]);
			linkString = split[1];
		}
	}
	
	public void go(GuiBookOfRevealing gui) {
		switch(type) {
		case 0:
			guideClick(gui);
			break;
		case 1:
			browseClick(gui);
			break;
		}
	}
	
	public void guideClick(GuiBookOfRevealing gui) {
		gui.refreshGuide(GuideRegistry.newBasicGuide(this.linkString));
	}
	
	public void browseClick(GuiBookOfRevealing gui) {
		String[] split = linkString.split(":", 2);
		if(split.length < 2)
			return;
		BrowseItemDirectoryMod mod = GuideMod.browseManager.getMod(split[0]);
		if(mod == null)
			return;
		String modText = mod.getText();
		ArrayList<String> path = new ArrayList<String>();
		path.add(modText);
		path.addAll(Arrays.asList(split[1].split("/")));
		path.removeAll(Arrays.asList("", null));
		BrowseItem pathItem = GuideMod.browseManager.getForPath(path);
		if(pathItem instanceof BrowseItemDirectory) {
			gui.goToView(new ViewBrowse(gui.viewWidth, gui.viewHeight, gui, path));
		}
		if(pathItem instanceof BrowseItemGuide) {
			gui.refreshGuide(GuideRegistry.newBasicGuide( ((BrowseItemGuide)pathItem).guideId() ));
		}
	}
	
	public int parseType(String typeString) {
		for(int i = 0; i < types.length; i++) {
			if(types[i].equalsIgnoreCase(typeString)) {
				return i;
			}
		}
		return 0;
	}
	
}
