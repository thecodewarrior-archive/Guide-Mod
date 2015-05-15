package com.thecodewarrior.guides.gui;

import com.thecodewarrior.guides.EventHandlers;

public class Animation<T> {

	float length;
	float progressTicks;
	float lastTime;
	public T param;
	
	public Animation(float length, T param) {
		this.length = length;
		this.param  = param;
	}
	
	public void start() {
		lastTime = EventHandlers.getTotalTicks();
	}
	
	public void tick() {
		if(!isDone()) {
			progressTicks += EventHandlers.getTotalTicks() - lastTime;
			if(progressTicks > length) {
				progressTicks = length;
			}
			lastTime = EventHandlers.getTotalTicks();
		}
	}
	
	public double fracDone() {
		return progressTicks/(double)length;
	}
	
	public double fracLeft() {
		if(progressTicks >= length) {
			return 0;
		} else {
			return 1.0 - fracDone();
		}
	}
	
	public void reset() {
		progressTicks = 0;
		start();
	}
	
	public void setProgress(float i) {
		if(i > length) {
			progressTicks = length;
		} else {
			progressTicks = i;
		}
	}
	
	public float getProgress() {
		return progressTicks;
	}
	
	public float progressTicksMaxed() {
		if(progressTicks > length) {
			return length;
		} else {
			return progressTicks;
		}
	}
	
	public float getLength() {
		return length;
	}
	public boolean isDone() {
		return progressTicks >= length;
	}
}
