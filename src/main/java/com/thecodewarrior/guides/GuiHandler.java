package com.thecodewarrior.guides;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import com.thecodewarrior.guides.gui.GuiBookOfRevealing;

import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {

	public GuiHandler() {
		// TODO Auto-generated constructor stub
	}

	public static int x;
	public static int y;
	public static int z;
	public static boolean isPicking;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) { return null; }
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if ( ID == GuiBookOfRevealing.GUI_ID ){
			
//			NBTTagCompound tag = player.getEntityData();
			
//			int clickX = tag.getInteger("guide_click_x");
//			int clickY = tag.getInteger("guide_click_y");
//			int clickZ = tag.getInteger("guide_click_z");
			if(isPicking) { //tag.getBoolean("guide_is_picking")
				return new GuiBookOfRevealing(player, world, this.x, this.y, this.z);
			} else {
				return new GuiBookOfRevealing(player, player.getHeldItem());
			}
		}
		
		return null;
	}
	
}
