package com.thecodewarrior.guides.guidepack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.client.Minecraft;

import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.thecodewarrior.guides.GuideMod;
import com.thecodewarrior.guides.api.GuideMatcher;
import com.thecodewarrior.guides.api.GuideRegistry;
import com.thecodewarrior.guides.guidepack.browse.BrowseItemDirectory;
import com.thecodewarrior.guides.guidepack.browse.BrowseItemGuide;

public class GuidePackLoader {
	
	public static final Logger l = GuideMod.logChild("GuidePackLoader");
	
	public static void loadPack(File packPath) {
		String packID = packPath.getName();
		File file = new File(packPath, "pack.json");
		BufferedReader r;
		try {
			r = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		JsonParser parser = new JsonParser();
		JsonObject rootObj = null;
		try{
			rootObj = parser.parse(r).getAsJsonObject();
		} catch (JsonSyntaxException e) {
			l.info("JSON Syntax error in pack: " + packID + " on line " + e.getLocalizedMessage());
		}
		if(rootObj == null)
			return;
		
		loadIdMaps(rootObj, packID);
		
		loadNameMap(rootObj);
		
		loadBrowse(rootObj, packID, Minecraft.getMinecraft().gameSettings.language);
		
		/*
		 * 
JsonObject locObj = rootObj.getAsJsonObject("result").getAsJsonObject("geometry").getAsJsonObject("location");

String status = rootObj.get("status").getAsString();
String lat = locObj.get("lat").getAsString();
String lng = locObj.get("lng").getAsString();

System.out.printf("Status: %s, Latitude: %s, Longitude: %s\n", status, lat, lng);
		 * */
	}
	
	static void loadIdMaps(JsonObject rootObj, String packID) {
		JsonObject idmapsObj = rootObj.getAsJsonObject("idmaps");
		
		for(Iterator<Entry<String, JsonElement>> iter = idmapsObj.entrySet().iterator(); iter.hasNext(); ) {
			Entry<String, JsonElement> guideElement = iter.next();
			String guideName = guideElement.getKey();
			JsonArray arr = (JsonArray) guideElement.getValue();
			
			for(Iterator<JsonElement> mapIter = arr.iterator(); mapIter.hasNext(); ) {
				JsonObject map = (JsonObject) mapIter.next();
				String id = map.get("id").getAsString();
				String meta = "";
				if(map.has("meta")) {
					meta = map.get("meta").getAsString();
				}
				GuideRegistry.registerMatcher(packID, new GuideMatcher(id, meta, guideName));
			}
		}
	}

	static void loadNameMap(JsonObject rootObj) {
		JsonObject namesMapObj = rootObj.getAsJsonObject("names");
		if(namesMapObj != null) {
			JsonObject namesMapLangObj = namesMapObj.getAsJsonObject(Minecraft.getMinecraft().gameSettings.language);
			if(namesMapObj != null) {
				for(Iterator<Entry<String, JsonElement>> iter = namesMapLangObj.entrySet().iterator(); iter.hasNext(); ) {
					Entry<String, JsonElement> entry = iter.next();
					String guideId = entry.getKey();
					String guideName = entry.getValue().getAsString();
					
					GuideRegistry.addGuideName(guideId, guideName);
				}
			}
		}
	}

	static void loadBrowse(JsonObject rootObj, String packID, String lang) {
		JsonObject browse = (JsonObject) rootObj.get("browse");
		JsonArray  mod    = (JsonArray) browse.get(lang);
		
		BrowseItemDirectory m = GuideMod.browseManager.getMod(packID, mod.get(0).getAsString());
		
		loadBrowseIntoDirectory(mod, m, true);
	}
	
	static void loadBrowseDirectory(JsonArray json, BrowseItemDirectory parent) {
		BrowseItemDirectory dir = new BrowseItemDirectory(json.get(0).getAsString());
		loadBrowseIntoDirectory(json, dir, true);
		parent.addChild(dir);
	}
	
	static void loadBrowseIntoDirectory(JsonArray json, BrowseItemDirectory dir, boolean ignoreZeroth) {
		for(int i = (ignoreZeroth ? 1 : 0); i < json.size(); i++) {
			JsonElement e = json.get(i);
			if(e instanceof JsonArray) {
				loadBrowseDirectory((JsonArray)e, dir);
			} else if (e instanceof JsonPrimitive) {
				dir.addChild(new BrowseItemGuide(e.getAsString()));
			} else {
				l.warn("ERROR: non primitive, non array element in browse map");
			}
		}
	}
}
