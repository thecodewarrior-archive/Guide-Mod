package com.thecodewarrior.guides;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

import com.thecodewarrior.guides.gui.GuiBookOfRevealing;

public class BookOfRevealing extends Item {
	
	public BookOfRevealing() {
		setCreativeTab(Reference.tab);
		setTextureName(Reference.MODID + ":book_of_revealing");
		setUnlocalizedName("revealingBook");
		setMaxStackSize(1);
	}
	
	public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
		NBTTagCompound tag = player.getEntityData();
		if(!player.isSneaking()) {
			
			MovingObjectPosition pos = rayTrace(player, 5.0F, 0, true);
			if(pos != null) {
				String id = world.getBlock(pos.blockX, pos.blockY, pos.blockZ).getUnlocalizedName();
				tag.setInteger("guide_click_x", pos.blockX);
				tag.setInteger("guide_click_y", pos.blockY);
				tag.setInteger("guide_click_z", pos.blockZ);
				tag.setBoolean("guide_is_picking", true);
			}
		} else {
			tag.setBoolean("guide_is_picking", false);
		}
		player.openGui(GuideMod.instance, GuiBookOfRevealing.GUI_ID, world, (int)player.posX, (int)player.posY, (int)player.posZ);
		return item;
	}
	
	public MovingObjectPosition rayTrace(EntityLivingBase entity, double par1, float par3, boolean liquid)
    {
        Vec3 vec3  = entity.getPosition(par3);
        Vec3 vec31 = entity.getLook(par3);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * par1, vec31.yCoord * par1, vec31.zCoord * par1);
        //func_147447_a, rayTraceBlocks
        return entity.worldObj.rayTraceBlocks(vec3, vec32, liquid);
    }	
	
}
