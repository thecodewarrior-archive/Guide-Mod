package com.thecodewarrior.guides.guides.elements;

import java.util.ArrayList;
import java.util.List;

import com.thecodewarrior.guides.GuideMod;

public class GuideElementContainer extends GuideElement {

	public List<GuideElement> elements;
	private boolean inited = false;
	
	public GuideElementContainer(int x, int y, int width, List<GuideElement> elements) {
		super(x, y, width);
		init(elements);
	}
	
	public GuideElementContainer(int x, int y, int width) {
		super(x, y, width);
	}
	
	public void init(List<GuideElement> elements) {
		if(inited){
			return;
		} else {
			inited = true;
		}
		
		this.elements = elements;
		processGuideElements(elements);
		if(elements.size() == 0) return;
		
		this.newX = elements.get(elements.size() -1).newX;
		this.newY = elements.get(elements.size() -1).newY;
		
	}
	
	private void processGuideElements(List<GuideElement> elements) {
		for(GuideElement e: elements) {
			if(e instanceof GuideElementContainer) {
				processGuideElements(((GuideElementContainer) e).elements);
			} else {
				processGuideElement(e);
			}
		}
	}
	
	public void processGuideElement(GuideElement e) {
		
	}
	
	public int newX() {
		return this.newX;
	}
	
	public int newY() {
		return this.newY;
	}
	
	public int newWidth() {
		return this.width;
	}
	
	public void clickInside(int mX, int mY, int button) {
		if(!inited) {
			GuideMod.l.warn("Not drawing GuideElementContainer, hasn't been initialized.");
			return;
		}
		
		for(GuideElement element: elements) {
			if(element.bounds.pointInside(mX, mY)) {
				element.clickInside(
						mX-element.bounds.getLeft(),
						mY-element.bounds.getTop(),
						button);
			}
		}
	}
	
	public void draw(int mX, int mY) {
		if(!inited) {
			GuideMod.l.warn("Not drawing GuideElementContainer, hasn't been initialized.");
			return;
		}
		for(GuideElement element: elements) {
			element.draw(mX, mY);
		}
	}
	
	public ArrayList<Integer> getSearches(String search) {
		ArrayList<Integer> matches = new ArrayList<Integer>();
		if(elements != null) {
			for(GuideElement e : elements) {
				matches.addAll( e.getSearches(search) );
			}
		}
		return matches;
	}
	
}
