package com.thecodewarrior.guides.guidepack.browse;

import com.thecodewarrior.guides.api.GuideRegistry;

public class BrowseItemGuide extends BrowseItem{
	
	String guideId;
	
	public BrowseItemGuide(String guideId) {
		this.guideId = guideId;
		this.text = GuideRegistry.getGuideName(guideId);
	}
	
	public String guideId() {
		return guideId;
	}
	
	public boolean doesMatchSearch(String search) {
		return this.text.contains(search);
	}

}
