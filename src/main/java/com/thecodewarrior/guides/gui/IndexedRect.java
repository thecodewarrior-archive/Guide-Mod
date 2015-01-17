package com.thecodewarrior.guides.gui;

public class IndexedRect {
	int x1;
	int y1;

	int x2;
	int y2;
	
	int id;
	
	public IndexedRect(int id, int x, int y, int width, int height) {
		this.x1 = x;
		this.y1 = y;
		this.x2 = x + width;
		this.y2 = y + height;
		this.id = id;
	}
	
	public boolean pointInside(int x, int y) {
		return 
				x > x1 && x < x2 &&
				y > y1 && y < y2
				;
	}
	
}
