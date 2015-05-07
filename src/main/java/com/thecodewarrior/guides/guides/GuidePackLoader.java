package com.thecodewarrior.guides.guides;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Iterator;
import java.util.Map.Entry;

import org.apache.logging.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.thecodewarrior.guides.GuideMod;
import com.thecodewarrior.guides.api.GuideMatcher;
import com.thecodewarrior.guides.api.GuideRegistry;
import com.thecodewarrior.guides.proxy.ClientProxy;

public class GuidePackLoader {
	
	public static final Logger l = GuideMod.logChild("GuidePackLoader");
	
	public static void loadPack(File packPath) {
		String packID = packPath.getName();
		File file = new File(packPath, "pack.json");
		BufferedReader r;
		try {
			r = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
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
		
		JsonObject namesMapObj = rootObj.getAsJsonObject("names");
		if(namesMapObj != null) {
			JsonObject namesMapLangObj = namesMapObj.getAsJsonObject(GuideMod.proxy.getLang());
			if(namesMapObj != null) {
				for(Iterator<Entry<String, JsonElement>> iter = namesMapLangObj.entrySet().iterator(); iter.hasNext(); ) {
					Entry<String, JsonElement> entry = iter.next();
					String guideId = entry.getKey();
					String guideName = entry.getValue().getAsString();
					
					GuideRegistry.addGuideName(guideId, guideName);
				}
			}
		}
		
		
		
		/*
		 * 
JsonObject locObj = rootObj.getAsJsonObject("result").getAsJsonObject("geometry").getAsJsonObject("location");

String status = rootObj.get("status").getAsString();
String lat = locObj.get("lat").getAsString();
String lng = locObj.get("lng").getAsString();

System.out.printf("Status: %s, Latitude: %s, Longitude: %s\n", status, lat, lng);
		 * */
	}
}
