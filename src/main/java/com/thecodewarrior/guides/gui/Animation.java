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
	
	public double fracDone() {
		return progressFrames/(double)length;
	}
	
	public double fracLeft() {
		if(progressFrames == length) {
			return 0;
		} else {
			return 1.0 - fracDone();
		}
	}
	
	public void reset() {
		progressFrames = 0;
	}
	
	public void setProgress(int i) {
		if(i > length) {
			progressFrames = length;
		} else {
			progressFrames = i;
		}
	}
	
	public int getProgress() {
		return progressFrames;
	}
	
	public int getLength() {
		return length;
	}
	public boolean isDone() {
		return progressFrames == length;
	}
}
