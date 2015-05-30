package com.thecodewarrior.guides;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import net.minecraftforge.common.config.Configuration;

public class ConfigOptions {

	static Configuration config;
	
	public static boolean dev = false;
	public static boolean autoDownload = true;
	
	public static String serverHost = "localhost";
	public static int    serverPort = 80;
	
	public static int defaultTickerTimeout = 5*20;
	public static String lang = "en_US";
	
	public static List<String> supplementaryGuidePacks;
	
	public static void init(File configFile) {
		config = new Configuration(configFile);
		reloadConfig();
	}
	
	public static void reloadConfig() {
		config.load();
		autoDownload = config.getBoolean("autodownload", "guideserver", autoDownload,
						"Automatically download guides?");		
		serverHost   = config.getString ("host"        , "guideserver", serverHost,
						"Guide Server Hostname");
		serverPort   = config.getInt    ("port"        , "guideserver", serverPort, 0, 65536,
						"Guide Server Port");
		dev          = config.getBoolean("development" , "guideserver", dev,
						"Enable development mode");
		supplementaryGuidePacks = Arrays.asList(config.getStringList("supplementaryGuidePacks", "guideserver", new String[] {},
				"Guide packs to download in addition to the ones from installed mods"));
		config.save();
	}
	
	public static void updateConfig() {
		
		config.get("guideserver", "autodownload", false, "Automatically download guides?").set(autoDownload);
		
		config.save();
	}
	
	public static Configuration getConfig() {
		return config;
	}

}
