package com.thecodewarrior.guides.api;

import java.io.File;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;

public class BookmarkManager {

	Configuration bookmarkConfig;

	ArrayList<String> bookmarks;
	
	public BookmarkManager() {
		bookmarks = new ArrayList<String>();
		bookmarks.add("minecraft:planks");
		bookmarks.add("minecraft:cobblestone");
		GuideRegistry.addGuideName("minecraft:planks", "Planks");
		GuideRegistry.addGuideName("minecraft:cobblestone", "Cobblestone");
	}
	
	public void loadConfig() {
		bookmarkConfig = new Configuration(new File(Minecraft.getMinecraft().mcDataDir, "guides_bookmarks.cfg"));
	}
	
	public GuideGenerator getBookmarkGuide(int i) {
		return GuideRegistry.newBasicGuide(bookmarks.get(i));
	}
	
	void updateConfigFile() {
		
	}
	
	public void deleteBookmark(int i) {
		bookmarks.remove(i);
	}
	
	public void addBookmark(String s) {
		bookmarks.add(s);
	}
	
	public ArrayList<String> getBookmarks() {
		return bookmarks;
	}
	
	public String getBookmarkName(int i) {
		return GuideRegistry.getGuideName(bookmarks.get(i));
	}
	
	public int getBookmarkCount() {
		return bookmarks.size();
	}
}
