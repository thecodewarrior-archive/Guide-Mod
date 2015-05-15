package com.thecodewarrior.guides.api.browse;

import java.util.ArrayList;

public class BrowseItemDirectory extends BrowseItem {

	ArrayList<BrowseItem> items;
	
	public BrowseItemDirectory(String name) {
		items = new ArrayList<BrowseItem>();
		this.text = name;
	}
	
	public void addChild(BrowseItem item) {
		items.add(item);
	}
	
	public ArrayList<BrowseItem> getItems() {
		return items;
	}
	
	public BrowseItem getByName(String name) {
		for(BrowseItem item : items) {
			if(item.getText().equals(name)) {
				return item;
			}
		}
		return null;
	}
	
	public ArrayList<BrowseItem> getMatching(String search) {
		ArrayList<BrowseItem> matches = new ArrayList<BrowseItem>();
		
		for(BrowseItem item : items) {
			if(item instanceof BrowseItemDirectory) {
				matches.addAll(( (BrowseItemDirectory)item ).getMatching(search));
			} else if(item instanceof BrowseItemGuide && ( (BrowseItemGuide)item ).doesMatchSearch(search) ) {
				matches.add(item);
			}
		}
		
		return matches;
	}

}
