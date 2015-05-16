package com.thecodewarrior.guides.gui;

import com.thecodewarrior.guides.EventHandlers;

public class TickCounter<T> {

	float ticksElapsed;
	float lastTime;
	public T par;
	boolean stopped = false;
	
	public TickCounter() {
	}
	
	public void tick() {
		if(lastTime == 0) {
			lastTime = EventHandlers.getTotalTicks();
		}
		if(!stopped) {
			ticksElapsed += EventHandlers.getTotalTicks() - lastTime;
		}
		lastTime = EventHandlers.getTotalTicks();
	}
	
	public float ticks() {
		return ticksElapsed;
	}
	
	public void setTicks(float ticks) {
		ticksElapsed = ticks;
	}
	
	public void reset() {
		setTicks(0);
	}

	public void stop() {
		this.stopped = true;
	}
	
	public void start() {
		this.stopped = false;
	}
	
	public boolean stopped() {
		return this.stopped;
	}
	
}
