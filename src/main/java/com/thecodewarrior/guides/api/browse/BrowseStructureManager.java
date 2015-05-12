package com.thecodewarrior.guides.api.browse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BrowseStructureManager {

	Map<String, BrowseItemDirectoryMod> modItems = new HashMap<String, BrowseItemDirectoryMod>();
	BrowseItemDirectoryRoot root = new BrowseItemDirectoryRoot("/");
	
	public BrowseStructureManager() {
		BrowseItemDirectoryMod mc = getMod("minecraft", "Vanilla minecraft");
		mc.addChild(new BrowseItemGuide("minecraft:planks"));
		mc.addChild(new BrowseItemGuide("minecraft:lava"));
		root.addChild(mc);
	}
	
	public BrowseItemDirectoryMod getMod(String modid, String name) {
		if(!modItems.containsKey(modid)) {
			modItems.put(modid, new BrowseItemDirectoryMod(modid, name));
		}
		return modItems.get(modid);
	}
	
	public BrowseItemDirectoryRoot getRoot() {
		return root;
	}
	
	public ArrayList<BrowseItem> getForPath(ArrayList<String> path) {
		BrowseItemDirectory dir = root;
		for(String pathItem : path) {
			BrowseItem tmp = dir.getByName(pathItem);
			if(tmp instanceof BrowseItemDirectory) {
				dir = (BrowseItemDirectory) tmp;
			} else {
				break;
			}
		}
		
		return dir.getItems();
	}

}
