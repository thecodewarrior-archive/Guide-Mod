package com.thecodewarrior.guides.guidepack.browse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BrowseStructureManager {

	Map<String, BrowseItemDirectoryMod> modItems = new HashMap<String, BrowseItemDirectoryMod>();
	BrowseItemDirectoryRoot root = new BrowseItemDirectoryRoot("/");
	
	public BrowseStructureManager() {
	}
	
	public void wipeBrowseStructure() {
		modItems = new HashMap<String, BrowseItemDirectoryMod>();
		root = new BrowseItemDirectoryRoot("/");
	}
	
	public BrowseItemDirectoryMod getMod(String modid, String name) {
		BrowseItemDirectoryMod m = null;
		if(!modItems.containsKey(modid)) {
			m = new BrowseItemDirectoryMod(modid, name);
			modItems.put(modid, m);
			root.addChild(m);
		} else {
			m = modItems.get(modid);
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
