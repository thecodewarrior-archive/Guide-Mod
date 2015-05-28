package com.thecodewarrior.guides.gui.icon;

import net.minecraft.util.ResourceLocation;

public class IconFactory {

	public int texSize;
	public ResourceLocation loc;
	
	public IconFactory(int texSize, ResourceLocation loc) {
		this.texSize = texSize;
		this.loc = loc;
	}
	
	public BasicIcon create(int u, int v, int w, int h) {
		return new BasicIcon(u, v, w, h, texSize);
	}
	
	public AnimatedIcon createAnimated(int u, int v, int w, int h, int frameCount) {
		return new AnimatedIcon(u, v, w, h, texSize, frameCount);
	}
	public AnimatedIcon createAnimated(int u, int v, int w, int h, int frameCount, int ticksPerFrame) {
		return new AnimatedIcon(u, v, w, h, texSize, frameCount, ticksPerFrame);
	}
	public AnimatedIcon createAnimated(int u, int v, int w, int h, int frameCount, boolean animateHorizontally) {
		return new AnimatedIcon(u, v, w, h, texSize, frameCount, animateHorizontally);
	}
	public AnimatedIcon createAnimated(int u, int v, int w, int h, int frameCount, int ticksPerFrame, boolean animateHorizontally) {
		return new AnimatedIcon(u, v, w, h, texSize, frameCount, ticksPerFrame, animateHorizontally);
	}
	
	public ManuallyAnimatedIcon createManuallyAnimated(int u, int v, int w, int h, boolean animateHorizontally) {
		return new ManuallyAnimatedIcon(u, v, w, h, texSize, animateHorizontally);
	}
	
	public ManuallyAnimatedIcon createManuallyAnimated(int u, int v, int w, int h) {
		return new ManuallyAnimatedIcon(u, v, w, h, texSize);
	}

}
