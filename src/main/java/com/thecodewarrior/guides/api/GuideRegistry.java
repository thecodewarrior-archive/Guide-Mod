package com.thecodewarrior.guides.api;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.thecodewarrior.guides.GuideMod;
import com.thecodewarrior.guides.gui.GuiBookOfRevealing;
import com.thecodewarrior.guides.guidepack.GuidePackReader;
import com.thecodewarrior.guides.guides.ErrorGuide;
import com.thecodewarrior.guides.guides.Guide;
import com.thecodewarrior.guides.guides.GuideGenerator;
import com.thecodewarrior.guides.guides.GuideText;
import com.thecodewarrior.guides.guides.tags.Tag;
import com.thecodewarrior.guides.views.View;
import com.thecodewarrior.guides.views.ViewGuide;
import com.thecodewarrior.guides.views.ViewMissing;
import com.thecodewarrior.guides.views.ViewNoMatch;
import com.thecodewarrior.guides.views.ViewNull;

import cpw.mods.fml.common.registry.GameRegistry;


public class GuideRegistry {
	
	private static HashMap<String, Tag> tags = new HashMap<String, Tag>();
	
	private static List<File> guidePacks = new ArrayList<File>();
		
	private static HashMap<String, List<GuideMatcher>> matchers = new HashMap<String, List<GuideMatcher>>();
	
	private static List<String> disabledPacks = new ArrayList<String>();
	
	private static HashMap<String, String> guideNames = new HashMap<String, String>();
	
	private static List<GuideMatcher> getOrCreateGuidePack(String packID) {
		if(!matchers.containsKey(packID)) {
			matchers.put(packID, new ArrayList<GuideMatcher>());
		}
		return matchers.get(packID);
	}
	
	public static void registerMatcher(String packID, GuideMatcher mat) {
		List<GuideMatcher> matchList = getOrCreateGuidePack(packID);
		matchList.add(mat);
	}
	
	public static void wipeGuideRegistry() {
		matchers = new HashMap<String, List<GuideMatcher>>();
		guideNames = new HashMap<String, String>();
	}
	
	public static void addGuideName(String guide, String name) {
		guideNames.put(guide, name);
	}
	public static String getGuideName(String guide) {
		if(guideNames.containsKey(guide)) {
			return guideNames.get(guide);
		} else {
			return guide;
		}
	}
	public static boolean guideNameExists(String guide) {
		return guideNames.containsKey(guide);
	}
	
	public static GuideGenerator guideFromName(String name) {
		return new GuideGeneratorBasic(name);
	}
	
	public static GuideGenerator findGuideFor(World w, int x, int y, int z) {
		
		GameRegistry.UniqueIdentifier idObj = GameRegistry.findUniqueIdentifierFor(w.getBlock(x,y,z));
		String id = idObj.modId + ":" + idObj.name;
		int meta  = w.getBlockMetadata(x, y, z);
		
		GuideMatcher.Match bestMatch = new GuideMatcher.Match();
		for (List<GuideMatcher> matcherArray : matchers.values()) {
			for(GuideMatcher matcher : matcherArray) {
		    	GuideMatcher.Match match = matcher.match(id, meta);
		    	if(
		    			(match.hasMatch) &&
		    			(match.typeSpecifity == bestMatch.typeSpecifity && match.itemSpecifity > bestMatch.itemSpecifity) ||
		    			(match.typeSpecifity >  bestMatch.typeSpecifity)
		    		) {
		    		bestMatch = match;
		    	}
		    }
		}
		if(bestMatch.hasMatch) {
			return guideFromName(bestMatch.guideName);
		} else {
			return NO_MATCH_GUIDE;
		}
	}
	
	public static GuideGenerator findGuideFor(ItemStack stack) {
		GameRegistry.UniqueIdentifier idObj = GameRegistry.findUniqueIdentifierFor(stack.getItem());
		String id = idObj.modId + ":" + idObj.name;
		int meta  = stack.getItemDamage();
		
		GuideMatcher.Match bestMatch = new GuideMatcher.Match();
		for (List<GuideMatcher> matcherArray : matchers.values()) {
			for(GuideMatcher matcher : matcherArray) {
		    	GuideMatcher.Match match = matcher.match(id, meta);
		    	if(
		    			(match.hasMatch) &&
		    			(match.typeSpecifity == bestMatch.typeSpecifity && match.itemSpecifity > bestMatch.itemSpecifity) ||
		    			(match.typeSpecifity >  bestMatch.typeSpecifity)
		    		) {
		    		bestMatch = match;
		    	}
		    }
		}
		if(bestMatch.hasMatch) {
			return guideFromName(bestMatch.guideName);
		} else {
			return NO_MATCH_GUIDE;
		}
	}
	
	//{{ Tags
	public static void registerTag(Tag tag) {
		GuideRegistry.tags.put(tag.getProtocol().toLowerCase(), tag);
	}
	
	public static Tag getTag(String protocol) {
		return GuideRegistry.tags.get(protocol.toLowerCase());
	}
	//}}
	
	// Default Guide Generators
	
	public static GuideGenerator newBasicGuide(String name) {
		return new GuideGeneratorBasic(name);
	}
	
	public static GuideGenerator NULL_GUIDE = new GuideGenerator() {

		@Override
		public View generate(int width, int height, GuiBookOfRevealing gui) {
			return new ViewNull(width,height,gui);
		}
		
	};
	
	public static GuideGenerator NO_MATCH_GUIDE = new GuideGenerator() {

		@Override
		public View generate(int width, int height, GuiBookOfRevealing gui) {
			return new ViewNoMatch(width,height,gui);
		}
		
	};
	
	public static GuideGenerator NOTFOUND_GUIDE = new GuideGeneratorError("guide.text.error.notfound", "Guide not found");
	public static GuideGenerator NOTBLOCK_GUIDE = new GuideGeneratorError("guide.text.error.notblock", "Not a block");
	
	public static ResourceLocation guideLoc(String name) {
		return GuidePackReader.getGuideResourceLocation(name.split(":")[0], name.split(":")[1]);
		//new ResourceLocation( name.split(":")[0], "guides/" + GuideMod.proxy.getLang() + "/" + name.split(":")[1] + ".txt" );
	}
	// GuideGenerator classes
		public static class GuideGeneratorError extends GuideGenerator {
	
			public String name;
			public String text;
			
			public GuideGeneratorError(String name, String text) {
				this.name = name;
				this.text = text;
			}
			
			@Override
			public View generate(int width, int height, GuiBookOfRevealing gui) {
				return new ViewGuide(new ErrorGuide(this.name, this.text), width, height, gui);
			}
			
		}
		
		public static class GuideGeneratorBasic extends GuideGenerator {
	
			public String guideName;
			
			public GuideGeneratorBasic(String guideName) {
				this.guideName = guideName;
			}
			
			@Override
			public View generate(int width, int height, GuiBookOfRevealing gui) {
				String text = GuidePackReader.getGuideText(guideName.split(":")[0], guideName.split(":")[1]);
				if(text == null) {
					return new ViewMissing(guideName, width, height, gui);
				} else {
					return new ViewGuide(new GuideText(text, guideName), width, height, gui);
				}
							
			}
			
		}
		
		public static abstract class GuideGeneratorView extends GuideGenerator {
	
			protected Guide guide;
			
			public GuideGeneratorView(Guide guide) {
				this.guide = guide;
			}
			
			@Override
			public abstract View generate(int width, int height, GuiBookOfRevealing gui);
			
		}
		
		public static class GuideGeneratorViewGuide extends GuideGeneratorView {
	
			public GuideGeneratorViewGuide(Guide guide) {
				super(guide);
			}
	
			@Override
			public View generate(int width, int height, GuiBookOfRevealing gui) {
				return new ViewGuide(this.guide, width, height, gui);
			}
			
		}
	// end GuideGenerator Classes
}
