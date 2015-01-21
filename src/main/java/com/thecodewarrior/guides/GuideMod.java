package com.thecodewarrior.guides;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDynamicLiquid;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockStaticLiquid;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import org.apache.logging.log4j.Logger;

import com.thecodewarrior.guides.api.GuideGenerator;
import com.thecodewarrior.guides.api.GuideProvider;
import com.thecodewarrior.guides.api.GuideRegistry;
import com.thecodewarrior.guides.api.IBlockMatcher;
import com.thecodewarrior.guides.proxy.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid=Reference.MODID, version=Reference.VERSION)
public class GuideMod {
	
	public static final String MISSING_GUIDE_NAME = "guides:missing";

	@Instance(Reference.MODID)
	public static GuideMod instance;
	
	public static Logger logger;
	
	public static Item bookOfRevealing;
	
	@SidedProxy(clientSide="com.thecodewarrior.guides.proxy.ClientProxy", serverSide="com.thecodewarrior.guides.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler 
	public void preInit(FMLPreInitializationEvent event) {
		logger = event.getModLog();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
		bookOfRevealing = new BookOfRevealing();
		GameRegistry.registerItem(bookOfRevealing, bookOfRevealing.getUnlocalizedName().substring(5));
		
		HashMap<String,String> blockByID = new HashMap<String,String>();
		
		blockByID.put("minecraft:grass", "minecraft:grass");
		blockByID.put("minecraft:stone", "minecraft:stone");
		blockByID.put("minecraft:lava", "miencraft:lava");
		blockByID.put("minecraft:water", "minecraft:water");
		
		for(Map.Entry<String, String> entry : blockByID.entrySet()) {
			GuideRegistry.registerBlockGuideByID(entry.getKey(),
					new GuideRegistry.GuideGeneratorBasic(entry.getValue()));
		}
		
		HashMap<String,String> itemByID = new HashMap<String,String>();
		
		itemByID.put("minecraft:wooden_sword", "minecraft:swords");
		itemByID.put("minecraft:stone_sword", "minecraft:swords");
		itemByID.put("minecraft:iron_sword", "minecraft:swords");
		itemByID.put("minecraft:golden_sword", "minecraft:swords");
		itemByID.put("minecraft:diamond_sword", "minecraft:swords");

		for(Map.Entry<String, String> entry : itemByID.entrySet()) {
			GuideRegistry.registerItemGuideByID(entry.getKey(),
					new GuideRegistry.GuideGeneratorBasic(entry.getValue()));
		}
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.registerProxies();
		proxy.registerEvents();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
	}
}
