package com.thecodewarrior.guides;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

import org.lwjgl.input.Keyboard;

import com.thecodewarrior.guides.api.GuideRegistry;
import com.thecodewarrior.guides.api.GuideRegistry.GuideGeneratorBasic;
import com.thecodewarrior.guides.gui.GuiBookOfRevealing;
import com.thecodewarrior.guides.guides.GuideGenerator;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class EventHandlers {

	public static int ticksInGame = 0;
	public static float partialTicks = 0;
	public static KeyBinding openGuideKey;
	
	public static float getTotalTicks() {
		return ticksInGame + partialTicks;
	}
	
	public void init() {
		openGuideKey = new KeyBinding("key.guidemod.openGuideKey", Keyboard.KEY_G, "key.categories.misc");
		ClientRegistry.registerKeyBinding(openGuideKey);
	}

	/* Shamelessly "stolen" from Botania. Thanks Vazkii!
	 * https://github.com/Vazkii/Botania/blob/master/src/main/java/vazkii/botania/client/core/handler/ClientTickHandler.java#L48
	 */
	@SubscribeEvent
	public void clientTickEnd(ClientTickEvent event) {
		if(event.phase == Phase.END) {
			GuiScreen gui = Minecraft.getMinecraft().currentScreen;
			if(gui == null || !gui.doesGuiPauseGame()) {
				ticksInGame++;
			}
		}
	}
	
	@SubscribeEvent
    public void onKeyInput(InputEvent.KeyInputEvent event) {
        if(openGuideKey.isPressed()) {
        	EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        	MovingObjectPosition pos = rayTrace(player, Minecraft.getMinecraft().playerController.getBlockReachDistance(), 0, true);
			if(pos != null) {
//				String id = player.worldObj.getBlock(pos.blockX, pos.blockY, pos.blockZ).getUnlocalizedName();
				GuiHandler.x = pos.blockX;
				GuiHandler.y = pos.blockY;
				GuiHandler.z = pos.blockZ;
				GuiHandler.isPicking = true;
			}
    		if(!player.isSneaking()) {
        		player.openGui(GuideMod.instance, GuiBookOfRevealing.GUI_ID, player.worldObj, (int)player.posX, (int)player.posY, (int)player.posZ);
    		} else {
    			GuideGenerator g = GuideRegistry.findGuideFor(player.worldObj, GuiHandler.x, GuiHandler.y, GuiHandler.z);
    			if(g instanceof GuideGeneratorBasic) {
        			GuideMod.bookmarkManager.addBookmarkAtIndex(0, ((GuideGeneratorBasic) g).guideName);
    			}
    		}
        }
    }
	
	MovingObjectPosition rayTrace(EntityLivingBase entity, double par1, float par3, boolean liquid)
    {
        Vec3 vec3  = entity.getPosition(par3);
        Vec3 vec31 = entity.getLook(par3);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * par1, vec31.yCoord * par1, vec31.zCoord * par1);
        //func_147447_a, rayTraceBlocks
        return entity.worldObj.rayTraceBlocks(vec3, vec32, liquid);
    }
}
