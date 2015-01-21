package com.thecodewarrior.guides.gui;

import java.util.ArrayList;
import java.util.List;

public class MultiRect extends Rect {
	
	protected List<Rect> rects = new ArrayList<Rect>();
	
	public MultiRect() {
		this(new ArrayList<Rect>());
	}
	
	public MultiRect(List<Rect> rectList) {
		super(0,0,0,0);
		this.rects = rectList;
	}
	
	public void add(Rect rect) {
		rects.add(rect);
	}
	
	public boolean pointInside(int x, int y) {
		
		for(Rect rect : rects) {
			if(rect.pointInside(x, y)) {
				return true;
			}
		}
		
		return false;
	}
	
	public int getLeft() {
		double min = Double.POSITIVE_INFINITY;
		for(Rect rect : rects) {
			if(min > rect.getLeft()) {
				min = rect.getLeft();
			}
		}
		return (int) min;
	}
	
	public int getRight() {
		double max = Double.NEGATIVE_INFINITY;
		for(Rect rect : rects) {
			if(max < rect.getRight()) {
				max = rect.getRight();
			}
		}
		return (int) max;
	}
	
	public int getTop() {
		double min = Double.POSITIVE_INFINITY;
		for(Rect rect : rects) {
			if(min > rect.getTop()) {
				min = rect.getTop();
			}
		}
		return (int) min;
	}
	
	public int getBottom() {
		double max = Double.NEGATIVE_INFINITY;
		for(Rect rect : rects) {
			if(max < rect.getBottom()) {
				max = rect.getBottom();
			}
		}
		return (int) max;
	}

}
