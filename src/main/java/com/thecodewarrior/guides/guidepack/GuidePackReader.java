package com.thecodewarrior.guides.guidepack;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;

import com.thecodewarrior.guides.ConfigOptions;
import com.thecodewarrior.guides.GuideMod;

public class GuidePackReader {

	public static final Logger l = GuideMod.logChild("GuidePackReader");
		
	public static InputStream getInputStream(String fn) {
		try {
			return new FileInputStream( new File(GuidePackManager.getGuidePackDir(), fn) );
		} catch (FileNotFoundException e) {
			l.error("Guide pack file not found: " + fn);
			return null;
		}
	}
	
	public static String getGuidePackText(String modid, String guideName) {
		try {
			String lang = ConfigOptions.lang;
			File file = new File(GuidePackManager.getGuidePackDir(modid), lang + "/" + guideName + ".txt");
			InputStream stream = new FileInputStream(file);
			String str = IOUtils.toString(stream);
			stream.close();
			return str;
			
		} catch (IOException e1) {}
		return null;
	}
	
	public static ResourceLocation getGuideResourceLocation(String modid, String guideName) {
		return new ResourceLocation(modid, "guides/" + ConfigOptions.lang + "/" + guideName + ".txt");
	}
	
	public static String getResourceText(ResourceLocation loc) {
		try {
			InputStream stream = Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream();
			return IOUtils.toString(stream, "UTF-8");
		} catch (IOException e) {
			
			return null;
		}
	}
	
	public static String getGuideText(String modid, String guideName) {
		
		String lang = ConfigOptions.lang;
		String text = getResourceText(getGuideResourceLocation(modid, guideName));
		if(text == null) {
			text = getGuidePackText(modid, guideName);
		}
		if(text == null) {
			text = StatCollector.translateToLocal("guidemod.guideReader.internalError");
		}
		return text;
	}
	
	

}
