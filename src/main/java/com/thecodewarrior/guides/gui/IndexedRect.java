package com.thecodewarrior.guides.gui;

public class IndexedRect extends Rect {
	public int id;
	
	public IndexedRect(int id, int x, int y, int width, int height) {
		super(x,y,width,height);
		this.id = id;
	}
	
}
