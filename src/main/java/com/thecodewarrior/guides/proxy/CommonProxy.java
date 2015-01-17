package com.thecodewarrior.guides.proxy;

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
		if ( ID == GuiBookOfRevealing.GUI_ID )
            return new GuiContainerBookOfRevealing(player, player.getHeldItem());
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
				return new GuiBookOfRevealing(player, player.getHeldItem(), world, clickX, clickY, clickZ);
			} else {
				return new GuiBookOfRevealing(player, player.getHeldItem());
			}
		}
		
		return null;
	}
	
	public String getLang() {
		return "";
	}
	
	public boolean isClient() {
		return false;
	}
	public String getFileText(ResourceLocation guideLoc) {
		// TODO Auto-generated method stub
		return null;
	}
}
