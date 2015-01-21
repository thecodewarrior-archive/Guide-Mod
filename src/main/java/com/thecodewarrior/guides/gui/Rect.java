package com.thecodewarrior.guides.gui;

public class Rect {
	int x1;
	int y1;

	int x2;
	int y2;
	
	public Rect(int x, int y, int width, int height) {
		this.x1 = x;
		this.y1 = y;
		this.x2 = x + width;
		this.y2 = y + height;
	}
	
	public boolean pointInside(int x, int y) {
		return 
				x > x1 && x < x2 &&
				y > y1 && y < y2
				;
	}
	
	public int getLeft() {
		return x1;
	}
	
	public int getRight() {
		return x2;
	}
	
	public int getTop() {
		return y1;
	}
	
	public int getBottom() {
		return y2;
	}
}
