package com.thecodewarrior.guides.guides.elements;

import java.util.HashMap;

import com.thecodewarrior.guides.guides.tags.Tag;

public class GuideElementSpace extends GuideElement {

	protected boolean horizontal = false;
	protected int distance = 0;
	
	public GuideElementSpace(int x, int y, int width, String data, boolean horizontal) {
		super(x, y, width);
		
		distance = Integer.parseInt(data);
		
		// TODO Auto-generated constructor stub
	}
	
	public int newX() {
		if(horizontal) {
			return this.x + distance;
		} else {
			return this.x;
		}
	}
	
	public int newY() {
		if(horizontal) {
			return this.y;
		} else {
			return this.y + distance;
		}
	}

}
