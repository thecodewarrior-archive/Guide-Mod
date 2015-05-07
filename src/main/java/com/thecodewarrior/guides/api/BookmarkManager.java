package com.thecodewarrior.guides.api;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.config.Configuration;

public class BookmarkManager {

	Configuration bookmarkConfig;

	ArrayList<String> bookmarks;
	
	public BookmarkManager() {
		bookmarks = new ArrayList<String>();
	}
	
	public void loadConfig() {
		bookmarkConfig = new Configuration(new File(Minecraft.getMinecraft().mcDataDir, "config/guides_bookmarks.cfg"));
		bookmarkConfig.load();
		reloadConfigFile();
		bookmarkConfig.save();
	}
	
	public GuideGenerator getBookmarkGuide(int i) {
		return GuideRegistry.newBasicGuide(bookmarks.get(i));
	}
	
	void reloadConfigFile() {
		bookmarks = new ArrayList<String>(
				Arrays.asList(
						bookmarkConfig.getStringList("bookmarks", "bookmarks", new String[0], "The list of bookmarks")
						)
					);
	}
	
	void updateConfigFile() {
		bookmarkConfig.get("bookmarks", "bookmarks", new String[0]).set(bookmarks.toArray(new String[bookmarks.size()]));
		bookmarkConfig.save();
	}
	
	public void deleteBookmark(int i) {
		bookmarks.remove(i);
		updateConfigFile();
	}
	
	public void addBookmark(String s) {
		bookmarks.add(s);
		updateConfigFile();
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
