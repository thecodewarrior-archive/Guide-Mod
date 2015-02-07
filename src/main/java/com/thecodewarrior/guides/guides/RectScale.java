package com.thecodewarrior.guides.guides;

import com.thecodewarrior.guides.gui.Rect;

public class RectScale extends Rect {

	public Rect   rect;
	public double scale;
	
	public RectScale(Rect rect, double scale) {
		super(0,0,0,0);
		this.rect  = rect;
		this.scale = scale;
	}
	
	public boolean pointInside(int x, int y) {
		return rect.pointInside(
				rect.getLeft() + (int)(( x-rect.getLeft() )*scale),
				rect.getTop()  + (int)(( y-rect.getTop()  )*scale)
				);
	}
	
	public int getLeft() {
		return rect.getLeft();
	}
	
	public int getRight() {
		return rect.getLeft() + (int)(( rect.getRight()-rect.getLeft() )*scale);
	}
	
	public int getTop() {
		return rect.getTop();
	}
	
	public int getBottom() {
		return rect.getTop() + (int)(( rect.getBottom()-rect.getTop() )*scale);
	}

}
