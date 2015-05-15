package com.thecodewarrior.guides.proxy;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.thecodewarrior.guides.gui.GuiBookOfRevealing;
import com.thecodewarrior.guides.gui.GuiContainerBookOfRevealing;

import cpw.mods.fml.common.network.IGuiHandler;

public class CommonProxy{
	public void registerEvents() {}
	public void registerProxies() {}
	
	
	public String getLang() { return ""; }
	public boolean isClient() { return false; }
	public String getFileText(ResourceLocation guideLoc) { return null; }
	public FontRenderer getFontRenderer() { return null; }
	public Minecraft getMC() { return null; }
	public String getGuideText(String modid, String guideName) { return null; }
	public void loadGuidePacks() {}
	public void loadGuideFiles(File path) {}
	public void registerPack(File f) {}
	public void downloadGuides() {}
	public void bindGuideImage(String modid, String name) {}
	public void loadGuideImage(String modid, String name) {}
	public int imageWidth(String modid, String name) { return 0; }
	public int imageHeight(String modid, String name) { return 0; }
	public void registerLoggers() { }
}
