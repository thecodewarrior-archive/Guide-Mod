package com.thecodewarrior.guides.gui;

import net.minecraft.util.ResourceLocation;

public class BasicIconFactory {

	public int texSize;
	public ResourceLocation loc;
	
	public BasicIconFactory(int texSize, ResourceLocation loc) {
		this.texSize = texSize;
		this.loc = loc;
	}
	
	public BasicIcon create(int u, int v, int w, int h) {
		return new BasicIcon(u, v, w, h, texSize);
	}

}
