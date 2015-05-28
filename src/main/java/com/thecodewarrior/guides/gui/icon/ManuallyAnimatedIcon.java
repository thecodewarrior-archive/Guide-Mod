package com.thecodewarrior.guides.gui.icon;

import net.minecraft.util.IIcon;

public class ManuallyAnimatedIcon extends BasicIcon {
	
	public boolean animateHorizontally;
	public boolean doesLoop = false;
	public int maxFrame = Integer.MAX_VALUE;
	
	protected int frame;
	
	public ManuallyAnimatedIcon(int u, int v, int w, int h, int texSize) {
		this(u, v, w, h, texSize, false);
	}
	public ManuallyAnimatedIcon(int u, int v, int w, int h, int texSize, boolean horiztonal) {
		super(u, v, w, h, texSize);
	}
	
	public int getFrame() {
		if(doesLoop) {
			return frame%(maxFrame-1);
		} else {
			return frame;
		}
	}
	
	public void setFrame(int f) {
		frame = f;
	}
	
	public void nextFrame() {
		setFrame(frame+1);
	}
	
	public void prevFrame() {
		setFrame(frame-1);
	}
	
	public int getMinPxU() {
		if(animateHorizontally)
			return uPx;
		else
			return uPx+(getIconWidth()*getFrame());
	}
	
	public int getMinPxV() {
		if(animateHorizontally)
			return vPx+(getIconHeight()*getFrame());
		else
			return vPx;
	}

}
