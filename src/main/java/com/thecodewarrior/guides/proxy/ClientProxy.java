package com.thecodewarrior.guides.proxy;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class ClientProxy extends CommonProxy{
	public void registerEvents() {}
	public void registerProxies() {}
	
	public String getLang() {
		return Minecraft.getMinecraft().gameSettings.language;
	}
	
	public boolean isClient() {
		return true;
	}
	
	public String getFileText(ResourceLocation loc) {
		// TODO Auto-generated method stub
		try {
			InputStream stream = Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream();
			return IOUtils.toString(stream, "UTF-8");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return null;
		}
	}
}
