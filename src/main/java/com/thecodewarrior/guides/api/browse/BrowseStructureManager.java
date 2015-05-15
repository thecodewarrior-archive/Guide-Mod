package com.thecodewarrior.guides.api.browse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BrowseStructureManager {

	Map<String, BrowseItemDirectoryMod> modItems = new HashMap<String, BrowseItemDirectoryMod>();
	BrowseItemDirectoryRoot root = new BrowseItemDirectoryRoot("/");
	
	public BrowseStructureManager() {
//		BrowseItemDirectoryMod mc = getMod("minecraft", "Vanilla minecraft");
//		mc.addChild(new BrowseItemGuide("minecraft:planks"));
//		mc.addChild(new BrowseItemGuide("minecraft:lava"));
//		root.addChild(mc);
	}
	
	public BrowseItemDirectoryMod getMod(String modid, String name) {
		BrowseItemDirectoryMod m = null;
		if(!modItems.containsKey(modid)) {
			m = new BrowseItemDirectoryMod(modid, name);
			modItems.put(modid, m);
			root.addChild(m);
		}
		return m;
	}
	
	public BrowseItemDirectoryRoot getRoot() {
		return root;
	}
	
	public BrowseItem getForPath(ArrayList<String> path) {
		BrowseItemDirectory dir = root;
		for(String pathItem : path) {
			BrowseItem tmp = dir.getByName(pathItem);
			if(tmp instanceof BrowseItemDirectory) {
				dir = (BrowseItemDirectory) tmp;
			} else {
				break;
			}
		}
		
		return dir;
	}
	
	public ArrayList<BrowseItem> getForSearch(String search) {
		return root.getMatching(search);
	}

}
