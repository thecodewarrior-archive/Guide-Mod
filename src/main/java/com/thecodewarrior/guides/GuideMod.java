package com.thecodewarrior.guides;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Node;

import com.thecodewarrior.guides.api.BookmarkManager;
import com.thecodewarrior.guides.api.GuideRegistry;
import com.thecodewarrior.guides.api.browse.BrowseStructureManager;
import com.thecodewarrior.guides.guidepack.GuidePackManager;
import com.thecodewarrior.guides.guidepack.GuidePackUpdater;
import com.thecodewarrior.guides.guides.GuideText;
import com.thecodewarrior.guides.guides.elements.GuideElement;
import com.thecodewarrior.guides.guides.elements.GuideElementFormat;
import com.thecodewarrior.guides.guides.elements.GuideElementImage;
import com.thecodewarrior.guides.guides.elements.GuideElementIndent;
import com.thecodewarrior.guides.guides.elements.GuideElementText;
import com.thecodewarrior.guides.guides.elements.GuideElementTextLink;
import com.thecodewarrior.guides.guides.tags.Tag;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;

@Mod(modid=Reference.MODID, version=Reference.VERSION)
public class GuideMod {

	public static final String loggerName = "In-Game Guides";
	
	@Instance(Reference.MODID)
	public static GuideMod instance;
	
	public static Logger l;
	
	public static GuiHandler guiHandler;
	public static EventHandlers eventHandlers;
	Configuration config;
	public static BookmarkManager bookmarkManager;
	public static BrowseStructureManager browseManager;
	
	public static Logger logChild(String name) {
		Logger log = LogManager.getLogger(loggerName + "] [" + name);
		return log;
	}
	
	@EventHandler 
	public void preInit(FMLPreInitializationEvent event) {
		l = LogManager.getLogger(loggerName);

		bookmarkManager = new BookmarkManager();
		bookmarkManager.loadConfig();
		browseManager = new BrowseStructureManager();
		guiHandler = new GuiHandler();
		
		ConfigOptions.init(event.getSuggestedConfigurationFile());
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event) {
		eventHandlers = new EventHandlers();
		eventHandlers.init();
		FMLCommonHandler.instance().bus().register(eventHandlers);
		
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
		GuideText.getSeperator(Minecraft.getMinecraft().fontRenderer); // find a 1px wide glyph
		
		GuidePackUpdater.downloadPacksForMods();
		GuidePackManager.loadGuidePacks();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, guiHandler);
	}
}
