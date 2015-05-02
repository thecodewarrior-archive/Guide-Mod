package com.thecodewarrior.guides;

import java.util.List;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;

import com.thecodewarrior.guides.api.GuideRegistry;
import com.thecodewarrior.guides.guides.elements.GuideElement;
import com.thecodewarrior.guides.guides.elements.GuideElementFormat;
import com.thecodewarrior.guides.guides.elements.GuideElementImage;
import com.thecodewarrior.guides.guides.elements.GuideElementIndent;
import com.thecodewarrior.guides.guides.elements.GuideElementText;
import com.thecodewarrior.guides.guides.elements.GuideElementTextLink;
import com.thecodewarrior.guides.guides.tags.Tag;
import com.thecodewarrior.guides.proxy.CommonProxy;

import cpw.mods.fml.common.FMLLog;
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

	public static final String loggerName = "In-Game Guides";
	
	@Instance(Reference.MODID)
	public static GuideMod instance;
	
	public static Logger l;
	
	public static Item bookOfRevealing;
	
	@SidedProxy(clientSide="com.thecodewarrior.guides.proxy.ClientProxy", serverSide="com.thecodewarrior.guides.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	Configuration config;
	
	public static void updateEnabled(boolean value) {
		instance.config.get("guideserver", "enabled", false, "Is Guide Server enabled?").set(value);
		instance.config.save();
	}
	
	public static Logger logChild(String name) {
		Logger log = LogManager.getLogger(loggerName + "] [" + name);
		return log;
	}
	
	@EventHandler 
	public void preInit(FMLPreInitializationEvent event) {
		l = LogManager.getLogger(loggerName);
		config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();
		GuideServerInterface.enabled = config.getBoolean("enabled"    , "guideserver", GuideServerInterface.enabled,           "Is Guide Server enabled?");		
		GuideServerInterface.host    = config.getString ("host"       , "guideserver", GuideServerInterface.host   ,           "Guide Server Hostname");
		GuideServerInterface.port    = config.getInt    ("port"       , "guideserver", GuideServerInterface.port   , 0, 65536, "Guide Server Port");
		GuideServerInterface.dev     = config.getBoolean("development", "guideserver", GuideServerInterface.dev    ,           "Should mod re-download all guides every launch? (development only)");
		config.save();
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		
		bookOfRevealing = new BookOfRevealing();
		GameRegistry.registerItem(bookOfRevealing, bookOfRevealing.getUnlocalizedName().substring(5));

		GameRegistry.addShapelessRecipe(new ItemStack( bookOfRevealing ), new ItemStack( Items.book ) , new ItemStack( Items.spider_eye ));
		
		GuideRegistry.registerTag(new Tag("guide") {
			@Override
			public GuideElement getElement(int x, int y, int width, Node node, List<String> format) {
				return new GuideElementTextLink(x,y,width, node);
			}
		});
		
		GuideRegistry.registerTag(new Tag("image") {
			@Override
			public GuideElement getElement(int x, int y, int width, Node node, List<String> format) {
				return new GuideElementImage(x,y,width, node);
			}
		});
		
		GuideRegistry.registerTag(new Tag("indent") {
			@Override
			public GuideElement getElement(int x, int y, int width, Node node, List<String> format) {
				return new GuideElementIndent(x,y,width, node, format);
			}
		});
		
		GuideRegistry.registerTag(new Tag("b") {
			@Override
			public GuideElement getElement(int x, int y, int width, Node node, List<String> format) {
				return new GuideElementFormat(x, y, width, node, GuideElementText.BOLD, format);
			}
		});
		
		GuideRegistry.registerTag(new Tag("i") {
			@Override
			public GuideElement getElement(int x, int y, int width, Node node, List<String> format) {
				return new GuideElementFormat(x, y, width, node, GuideElementText.ITALIC, format);
			}
		});
		
		GuideRegistry.registerTag(new Tag("u") {
			@Override
			public GuideElement getElement(int x, int y, int width, Node node, List<String> format) {
				return new GuideElementFormat(x, y, width, node, GuideElementText.UNDERLINE, format);
			}
		});
		
		GuideRegistry.registerTag(new Tag("obf") {
			@Override
			public GuideElement getElement(int x, int y, int width, Node node, List<String> format) {
				return new GuideElementFormat(x, y, width, node, GuideElementText.OBFUSCATED, format);
			}
		});
		
		GuideRegistry.registerTag(new Tag("strike") {
			@Override
			public GuideElement getElement(int x, int y, int width, Node node, List<String> format) {
				return new GuideElementFormat(x, y, width, node, GuideElementText.STRIKETHROUGH, format);
			}
		});
		/*
		GuideRegistry.registerTag(new Tag("space") {
			@Override
			public GuideElement getElement(int x, int y, int width, Node node) {
				return new GuideElementSpace(x,y,width, node);
			}
		});
		
		GuideRegistry.registerTag(new Tag("bullet") {
			@Override
			public GuideElement getElement(int x, int y, int width, Node node) {
				return new GuideElementBullet(x,y,width, node);
			}
		});
		*/
	
		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.registerLoggers();
		proxy.registerProxies();
		proxy.registerEvents();
		
		proxy.downloadGuides();
		proxy.loadGuidePacks();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);
	}
}
