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

public class CommonProxy implements IGuiHandler{
	public void registerEvents() {}
	public void registerProxies() {}
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
//		if ( ID == GuiBookOfRevealing.GUI_ID )
//            return new GuiContainerBookOfRevealing(player);
		return null;
	}
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world,
			int x, int y, int z) {
		if ( ID == GuiBookOfRevealing.GUI_ID ){
			
			NBTTagCompound tag = player.getEntityData();
			
			int clickX = tag.getInteger("guide_click_x");
			int clickY = tag.getInteger("guide_click_y");
			int clickZ = tag.getInteger("guide_click_z");
			if(tag.getBoolean("guide_is_picking")) {
				return new GuiBookOfRevealing(player, world, clickX, clickY, clickZ);
			} else {
				return new GuiBookOfRevealing(player, player.getHeldItem());
			}
		}
		
		return null;
	}
	
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
