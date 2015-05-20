package com.thecodewarrior.guides.guidepack;

import java.io.File;
import java.io.FileFilter;

import com.thecodewarrior.guides.api.GuideRegistry;

import net.minecraft.client.Minecraft;

public class GuidePackManager {

	static File guidePackDir = new File(Minecraft.getMinecraft().mcDataDir, "guidepacks");
	
	public static void init() {
		guidePackDir.mkdir();
	}
	
	public static File getGuidePackDir() {
		return guidePackDir;
	}
	
	public static File getGuidePackDir(String packID) {
		return new File( guidePackDir, packID );
	}
	
	public static void unloadGuidePacks() {
		GuideRegistry.wipeGuideRegistry();
	}
	
	public static void loadGuidePacks() {
		
		FileFilter directoryFilter = new FileFilter() {
			public boolean accept(File file) {
				return !file.isFile();
			}
		};
		
		File[] folders = guidePackDir.listFiles(directoryFilter);
		for(File folder : folders) {
			GuidePackLoader.loadPack(folder);
		}
		
	}

}
