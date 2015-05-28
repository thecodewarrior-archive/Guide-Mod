package com.thecodewarrior.guides.gui.icon;

import net.minecraft.util.IIcon;

import com.thecodewarrior.guides.EventHandlers;
import com.thecodewarrior.guides.gui.TickCounter;

public class AnimatedIcon extends BasicIcon {

//	private float u;
//	private float v;
	private int w;
	private int h;
	private float texSize;
	private int uPx;
	private int vPx;
	private int frameCount;
	private int ticksPerFrame;
	private boolean animateHorizontally;
	
	public AnimatedIcon(int u, int v, int w, int h, int texSize, int frameCount) {
		this(u, v, w, h, texSize, frameCount, 1, false);
	}
	public AnimatedIcon(int u, int v, int w, int h, int texSize, int frameCount, boolean animateHorizontally) {
		this(u, v, w, h, texSize, frameCount, 1, animateHorizontally);
	}
	public AnimatedIcon(int u, int v, int w, int h, int texSize, int frameCount, int ticksPerFrame) {
		this(u, v, w, h, texSize, frameCount, ticksPerFrame, false);
	}
	public AnimatedIcon(int u, int v, int w, int h, int texSize, int frameCount, int ticksPerFrame, boolean animateHorizontally) {
		super(u, v, w, h, texSize);
		this.texSize = texSize;
//		this.u = u / this.texSize;
//		this.v = v / this.texSize;
		this.uPx = u;
		this.vPx = v;
		this.w = w;
		this.h = h;
		this.frameCount = frameCount;
		this.ticksPerFrame = ticksPerFrame;
		this.animateHorizontally = animateHorizontally;
	}
	
	public void setHorizontal(boolean horizontal) {
		this.animateHorizontally = horizontal;
	}
	
	public boolean getHoriztontal() {
		return this.animateHorizontally;
	}
	
	public void setTicksPerFrame(int t) {
		this.ticksPerFrame = t;
	}
	
	public int getTicksPerFrame() {
		return this.ticksPerFrame;
	}
	
	public void setFrameCount(int c) {
		this.frameCount = c;
	}
	
	public int getFrameCount() {
		return this.frameCount;
	}
	
	public int getMinPxU() {
		if(animateHorizontally)
			return uPx+( getIconWidth()*((EventHandlers.ticksInGame/ticksPerFrame)%frameCount));
		else
			return uPx;
	}
	
	public int getMinPxV() {
		if(animateHorizontally)
			return vPx;
		else
			return vPx+( getIconHeight()*((EventHandlers.ticksInGame/ticksPerFrame)%frameCount));
	}
}
