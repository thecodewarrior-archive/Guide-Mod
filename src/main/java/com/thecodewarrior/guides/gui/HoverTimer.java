package com.thecodewarrior.guides.gui;

import com.thecodewarrior.guides.EventHandlers;

public class HoverTimer {

	float hoverTicks;
	float lastTime;
	
	int mX;
	int mY;
	
	public HoverTimer() {
		// TODO Auto-generated constructor stub
	}
	
	public void tick(int mX, int mY) {
		if(lastTime == 0) {
			lastTime = EventHandlers.getTotalTicks();
		}
		hoverTicks += EventHandlers.getTotalTicks() - lastTime;
		lastTime = EventHandlers.getTotalTicks();
		if(mX != this.mX || mY != this.mY) {
			hoverTicks = 0;
		}
		this.mX = mX;
		this.mY = mY;
	}
	
	public boolean hasHoveredFor(int ticks) {
		return hoverTicks > ticks;
	}
	
	public float getTicks() {
		return hoverTicks;
	}

}
