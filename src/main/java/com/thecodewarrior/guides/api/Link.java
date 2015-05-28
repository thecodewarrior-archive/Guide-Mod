package com.thecodewarrior.guides.api;

import com.thecodewarrior.guides.gui.GuiBookOfRevealing;

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
			gui.refreshGuide(GuideRegistry.newBasicGuide(this.linkString));
			break;
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
