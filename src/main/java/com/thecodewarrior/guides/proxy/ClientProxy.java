package com.thecodewarrior.guides.proxy;

import java.io.IOException;
import java.io.InputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.io.IOUtils;

import com.thecodewarrior.guides.guides.GuideText;

public class ClientProxy extends CommonProxy{
	@Override
	public void registerEvents() {
		GuideText.getSeperator(Minecraft.getMinecraft().fontRenderer);
	}
	
	@Override
	public FontRenderer getFontRenderer() {
		return Minecraft.getMinecraft().fontRenderer;
	}
	
	@Override
	public void registerProxies() {}
	
	@Override
	public String getLang() {
		return Minecraft.getMinecraft().gameSettings.language;
	}
	
	@Override
	public boolean isClient() {
		return true;
	}
	
	@Override
	public String getFileText(ResourceLocation loc) {
		try {
			InputStream stream = Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream();
			return IOUtils.toString(stream, "UTF-8");
		} catch (IOException e) {
			return loc.getResourcePath();
		}
	}
	
	public Minecraft getMC() {
		return Minecraft.getMinecraft();
	}
}
