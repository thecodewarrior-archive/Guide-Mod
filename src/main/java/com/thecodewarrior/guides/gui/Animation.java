package com.thecodewarrior.guides.gui;

public class Animation<T> {

	int length;
	int progressFrames;
	public T param;
	
	public Animation(int length, T param) {
		this.length = length;
		this.param  = param;
	}
	
	public void frame() {
		if(!isDone()) {
			progressFrames++;
		}
	}
	
	public double progress() {
		return progressFrames/(double)length;
	}
	
	public double amtLeft() {
		if(progressFrames == length) {
			return 0;
		} else {
			return 1.0 - progress();
		}
	}
	
	public void reset() {
		progressFrames = 0;
	}
	
	public boolean isDone() {
		return progressFrames == length;
	}
}
