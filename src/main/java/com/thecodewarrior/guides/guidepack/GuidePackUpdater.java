package com.thecodewarrior.guides.guidepack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.util.StatCollector;

import org.apache.logging.log4j.Logger;

import com.thecodewarrior.guides.ConfigOptions;
import com.thecodewarrior.guides.GuideMod;
import com.thecodewarrior.guides.Reference;
import com.thecodewarrior.guides.gui.GuiBookOfRevealing;
import com.thecodewarrior.guides.gui.ticker.Ticker;
import com.thecodewarrior.guides.gui.ticker.TickerButton;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class GuidePackUpdater {
	
	public static String  protocol = "http";
	
	public static final Logger l = GuideMod.logChild("GuidePackUpdater");
	
	static int totalUpdates;
	static int modsWithUpdates;
	
	public static boolean manualUpdate = false;
	
	public static void downloadPacksForMods() {
		
		totalUpdates = 0;
		modsWithUpdates = 0;
		
		boolean dl = ConfigOptions.autoDownload || manualUpdate;
		
		List<ModContainer> mods = Loader.instance().getModList();
		
		String[] mcVersionSplit = Loader.instance().getMCVersionString().split(" ");
		
		GuidePackUpdater.updatePack(GuidePackManager.getGuidePackDir("minecraft"), "minecraft", mcVersionSplit[mcVersionSplit.length-1], dl);
		
		updatePack(GuidePackManager.getGuidePackDir(Reference.MODID), Reference.MODID, Reference.VERSION, true);
		
		for(ModContainer mod : mods) {
			if(mod.getModId().equals(Reference.MODID))
				continue;
			File file = GuidePackManager.getGuidePackDir(mod.getModId());
			int amt = updatePack(file, mod.getModId(), mod.getVersion(), dl);
			totalUpdates += amt;
			if(amt != 0) {
				modsWithUpdates++;
			}
		}
		
		for(String pack : ConfigOptions.supplementaryGuidePacks) {
			String[] split = pack.split(":", 2);
			if(split.length < 2) {
				l.info("ERROR: Additional guide pack " + pack + " doesn't match format packid:version!");
				continue;
			}
			String id = split[0];
			String version = split[1];
			File file = GuidePackManager.getGuidePackDir(id);
			int amt = updatePack(file, id, version, dl);
			totalUpdates += amt;
			if(amt != 0) {
				modsWithUpdates++;
			}
		}
		
		if(!ConfigOptions.autoDownload)
			addUpdatedTicker();
		
		manualUpdate = false;
	}
	
	public static void addUpdatedTicker() {
		Ticker t = new Ticker(
				0xFF0000,
				String.format(StatCollector.translateToLocal("guidemod.updateNotification.text"), totalUpdates, modsWithUpdates)
				);
		
		t.addButton(new TickerButton( StatCollector.translateToLocal("guidemod.updateNotification.enableAutoUpdate") ){
					@Override
					public void click(GuiBookOfRevealing gui) {
						ConfigOptions.autoDownload = true;
						ConfigOptions.updateConfig();
					}
		});
		t.setTimeout(40);
		GuiBookOfRevealing.pendingTickers.add(t);
	}
	
	public static int updatePack(File packPath, String modid, String version, boolean apply) {
		
		l.info("Checking for updates to guide pack '" + modid + "'");
		
		if(!packPath.isDirectory() && packPath.exists()) {
			l.info("  Pack path isn't a directory... why is this?");
			return 0;
		}
		
		File manifest = new File(packPath, "manifest.txt");
		
		Map<String, Integer> currentVersions = new HashMap<String, Integer>();
		Map<String, Integer> serverVersions = null;
		
		try {
			BufferedReader br = null;
			try{
				br = new BufferedReader(new FileReader(manifest));
				currentVersions = parseFileVersions(br, packPath);
			} catch (FileNotFoundException e) {
				//l.error("  Manifest file not found " + modid );
			}
			br = new BufferedReader( new InputStreamReader( new URL(protocol, ConfigOptions.serverHost, ConfigOptions.serverPort,
																	"/api/"+modid+"/"+version+"/manifest.txt").openStream()) );
			serverVersions = parseFileVersions(br, packPath);
			
		} catch (MalformedURLException e) {
			l.error("  Malformed url when downloading manifest file. URL:" + 
					protocol + "://" + ConfigOptions.serverHost + ":" + ConfigOptions.serverPort + "/api/" + modid + "/" + version + "/manifest.txt"
					);
		} catch (IOException e) {
			l.error("  IOException when opening stream to server");
		}
		
		int updated = 0;
		if(serverVersions != null) {
			if(!packPath.exists() && apply) {
				l.info("  So it does exist on the server, I'll create a directory then.");
				packPath.mkdir();
			}
			for(String i : currentVersions.keySet()) {
				int cv = currentVersions.get(i);
				if( serverVersions.containsKey(i)) {
					int sv = serverVersions.get(i);
					if(cv < sv) {
						if(apply)
							downloadGuide(modid, version, new File(packPath, i), i);
						updated++;
					}
					serverVersions.remove(i);
				} else {
					if(apply)
						removeGuide(new File(packPath, i));
					updated++;
				}
			}
		
			for(String i : serverVersions.keySet()) {
				if(apply)
					downloadGuide(modid, version, new File(packPath, i), i);
				updated++;
			}
		}
			
		
		if(apply)
			downloadGuide(modid, version, new File(packPath, "manifest.txt"), "manifest.txt");
		if(updated > 0) {
			l.info(String.format("  Guide pack has %d updates, auto update is %s, and the guides %s updated.",
					updated, ConfigOptions.autoDownload ? "on" : "off", apply ? "were" : "were not"));
		} else {
			l.info("  No updates found for guide pack");
		}
		return updated;
	}
	
	private static void removeGuide(File file) {
		file.delete();
	}
	
	private static void downloadGuide(String modid, String version, File localfile, String path) {
		try {
			URL remote = new URL(protocol, ConfigOptions.serverHost, ConfigOptions.serverPort, "/api/"+modid+"/"+version+"/" + path);
			ReadableByteChannel rbc = Channels.newChannel(remote.openStream());
			localfile.getParentFile().mkdirs();
			FileOutputStream fos = new FileOutputStream(localfile);
			fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
			fos.close();
		} catch (MalformedURLException e) {
			//l.error("Malformed URL trying to download update:" + e.toString());
			//l.catching(e);
		} catch (IOException e) {
			//l.error("IOException trying to download update: " + e.toString());
			//l.catching(e);
		}
		
	}
	
	private static Map<String, Integer> parseFileVersions(BufferedReader br, File packPath) {
		Map<String, Integer> versions = new HashMap<String, Integer>();
		try {
			String line;
			while ((line = br.readLine()) != null) {
				String[] parts = line.split(":");
				String path = parts[0];
				int version = Integer.parseInt(parts[1]);
				
				versions.put(path, version);
			}
			br.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return versions;
	}
	
	public static class GuidePackFileVersion {
		public File file;
		public int version;
		
		public GuidePackFileVersion(File file, int version) {
			this.file = file;
			this.version = version;
		}
		
		public boolean newer(GuidePackFileVersion other) {
			return this.version > other.version;
		}
	}
}
