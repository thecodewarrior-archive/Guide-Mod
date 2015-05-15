package com.thecodewarrior.guides;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class ClientTickHandler {

	public static int ticksInGame = 0;
	public static float partialTicks = 0;
	
	public static float getTotalTicks() {
		return ticksInGame + partialTicks;
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
}
