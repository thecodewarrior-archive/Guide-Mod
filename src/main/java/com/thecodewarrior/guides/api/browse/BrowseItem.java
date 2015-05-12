package com.thecodewarrior.guides.api.browse;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class BrowseItem {
	
	String text;
	ResourceLocation icon;
	Minecraft mc;
	
	public BrowseItem() {
		this.mc = Minecraft.getMinecraft();
	}
	
	public String getText() {
		return text;
	}
	
	public void draw(boolean hovering) {
		mc.fontRenderer.drawString(text, hovering ? 2 : 0, 0, 0x00);
	}

}
