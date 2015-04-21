package com.thecodewarrior.guides.proxy;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.imageio.ImageIO;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.thecodewarrior.guides.GuideMod;
import com.thecodewarrior.guides.GuideServerInterface;
import com.thecodewarrior.guides.api.GuideRegistry;
import com.thecodewarrior.guides.guides.GuideText;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class ClientProxy extends CommonProxy{
	
	public static File guidePackPath = new File(Minecraft.getMinecraft().mcDataDir, "guidepacks");
	public static boolean didGuidePackPathLoad = false;
	private static List<File> packs = new ArrayList<File>();
	
	public static final String prefix = "[Client Proxy]";
	
	public static Logger l;
	
	@Override
	public void registerLoggers() {
		l = GuideMod.logChild("ClientProxy");
		l.debug("Registering loggers");
		GuideServerInterface.registerLogger();
	}
	
	@Override
	public void registerEvents() {
		GuideText.getSeperator(Minecraft.getMinecraft().fontRenderer);
	}

	@Override
	public FontRenderer getFontRenderer() {
		return Minecraft.getMinecraft().fontRenderer;
	}
	
	@Override
	public void registerProxies() {
		try{
			guidePackPath.mkdir();
	        didGuidePackPathLoad = true;
	    } catch(SecurityException se) {}
	}
	
	@Override
	public void downloadGuides() {
		if(!GuideServerInterface.enabled) {
			GuideMod.l.info(prefix + "Guide download disabled. skipping.");
		}
		List<ModContainer> mods = Loader.instance().getModList();
		
		String[] mcVersionSplit = Loader.instance().getMCVersionString().split(" ");
		
		GuideServerInterface.updateMod("minecraft", mcVersionSplit[mcVersionSplit.length-1] , new File(guidePackPath, "minecraft.zip"));
		
		for(ModContainer mod : mods) {
			File file = new File(guidePackPath, mod.getModId() + ".zip");
			GuideServerInterface.updateMod(mod.getModId(), mod.getVersion(), file);
		}
	}
	
	@Override
	public void loadGuidePacks() {
		
		if(didGuidePackPathLoad) {
			FileFilter directoryFilter = new FileFilter() {
				public boolean accept(File file) {
					return !file.isFile();
//					try {
//						if(!file.isFile()) { return false; }
//						ZipFile z = new ZipFile(file);
//						z.close();
//						return true;
//					} catch (ZipException e) {
//						return false;
//					} catch (IOException e) {
//						return false;
//					}
				}
			};
			
			File[] folders = guidePackPath.listFiles(directoryFilter);
			for(File folder : folders) {
				loadGuideFiles(folder);
			}
		}
		
	}
	
	@Override
	public void loadGuideFiles(File path) {
		try {
//			ZipFile zipFile = new ZipFile(path);
//
//			InputStream stream = zipFile.getInputStream(new ZipEntry("register.txt"));
//			BufferedReader r = new BufferedReader(new InputStreamReader(stream));
			File file = new File(path, "register.txt");
			BufferedReader r = new BufferedReader(new FileReader(file));
			
			String line;
			HashMap<String, String> blocks = new HashMap<String, String>();
			HashMap<String, String> items  = new HashMap<String, String>();
			boolean isInBlocks = false;
			boolean isInItems  = false;
			boolean error = false;
			
			while( ( line = r.readLine() ) != null) {
				if(line.equals("") || line.matches("^\\s+$")) {
					continue;
				}
				if(line.trim().equalsIgnoreCase("blocks:")) {
					isInItems  = false;
					isInBlocks = true;
					continue;
				}
				if(line.trim().equalsIgnoreCase("items:")) {
					isInBlocks = false;
					isInItems  = true;
					continue;
				}
				
				if(isInBlocks) {
					String[] parts = line.trim().split("=");
					if(parts.length < 2)
						continue;
					String key = parts[0].trim();
					String val = parts[1].trim();
					
					blocks.put(key, val);
					
				}
				if(isInItems) {
					String[] parts = line.trim().split("=");
					if(parts.length < 2)
						continue;
					String key = parts[0].trim();
					String val = parts[1].trim();
					
					blocks.put(key, val);
				}
			}
			
			r.close();
			
			for(Map.Entry<String, String> entry : blocks.entrySet()) {
				if(entry.getKey().matches(".+?:.+?\\.\\d+")) {
					String[] split = entry.getKey().split("\\.");
					GuideRegistry.registerBlockGuideByIDMeta(
								split[0], Integer.parseInt(split[1]),
								new GuideRegistry.GuideGeneratorBasic(entry.getValue())
							);
				} else {
					GuideRegistry.registerBlockGuideByID(
							entry.getKey(),
							new GuideRegistry.GuideGeneratorBasic(entry.getValue())
						);
				}
			}
			
			for(Map.Entry<String, String> entry : items.entrySet()) {
				if(entry.getKey().matches(".+?:.+?\\.\\d+")) {
					String[] split = entry.getKey().split("\\.");
					GuideRegistry.registerItemGuideByIDMeta(
								split[0], Integer.parseInt(split[1]),
								new GuideRegistry.GuideGeneratorBasic(entry.getValue())
							);
				} else {
					GuideRegistry.registerItemGuideByID(
							entry.getKey(),
							new GuideRegistry.GuideGeneratorBasic(entry.getValue())
						);
				}
			}

//	    zipFile.close();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	@Override
	public String getLang() {
		return Minecraft.getMinecraft().gameSettings.language;
	}
	
	@Override
	public boolean isClient() {
		return true;
	}
	
	protected ZipFile getZipFile(String modid) {
		File guideZip = new File(guidePackPath, modid + ".zip");
		try {
			ZipFile zipFile = new ZipFile(guideZip);
			return zipFile;
			
		} catch (ZipException e) {
		} catch (IOException e) {
		}
		return null;
	}
	
	protected String getGuidePackText(String modid, String guideName/*, File packPath*/) {
		try {
			
			String lang = getLang();
			
//			ZipFile zipFile = getZipFile(modid);
							
//				InputStream stream = zipFile.getInputStream(new ZipEntry(lang + "/" + guideName + ".txt"));
			File file = new File(guidePackPath, modid + "/" + lang + "/" + guideName + ".txt");
			InputStream stream = new FileInputStream(file);
//			if(stream == null) {
//				zipFile.close();
//				return null;
//			}
			String str = IOUtils.toString(stream);
//			zipFile.close();
			stream.close();
			return str;
			
		} catch (IOException e1) {}
		return null;
	}
	
	@Override
	public String getGuideText(String modid, String guideName) {
		String lang = getLang();
		try {
			InputStream stream = Minecraft.getMinecraft().getResourceManager().getResource(
					new ResourceLocation(modid, "guides/" + lang + "/" + guideName + ".txt")
					).getInputStream();
			return IOUtils.toString(stream, "UTF-8");
		} catch (IOException e) {
			String str = null;
			String errorText = modid + ":" + guideName + " not found. searched for\n";
			errorText += "resource pack: " + modid + "/guides/" + lang + "/" + guideName + ".txt\n";
			errorText += "guide pack: " + modid + "/" + lang + "/" + guideName + ".txt";
			
			if(didGuidePackPathLoad) {
				str = getGuidePackText(modid, guideName/*, guidePackPath*/);
				GuideMod.l.info("text: " + str);
			}
			if(str != null) {
				return str;
			}
			
			return errorText;
		}
	}
	
	@Override
	public String getFileText(ResourceLocation loc) {
		try {
			InputStream stream = Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream();
			return IOUtils.toString(stream, "UTF-8");
		} catch (IOException e) {
			
			return loc.getResourceDomain() + ":" + loc.getResourcePath();
		}
	}
	
	private static HashMap<String, ResourceLocation> textureLocations   = new HashMap<String, ResourceLocation>();
	private static HashMap<String, DynamicTexture>   dynamicTextures    = new HashMap<String, DynamicTexture>();
	private static HashMap<String, Integer> imageWidths  = new HashMap<String, Integer>();
	private static HashMap<String, Integer> imageHeights = new HashMap<String, Integer>();
	
	@Override
	public int imageWidth(String modid, String name) {
		return imageWidths.get(modid + ":" + name);
	}
	
	@Override
	public int imageHeight(String modid, String name) {
		return imageHeights.get(modid + ":" + name);
	}
	
	/**
	 * Attempts to load the image.
	 */
	public void loadGuideImage(String modid, String name)
	{
		try {
			String key = modid + ":" + name;
			if(!textureLocations.containsKey(modid + ":" + name)) {
//	        	ZipFile file = getZipFile(modid);
//	    	    InputStream input = file.getInputStream(new ZipEntry("images/" + name + ".png"));
				File file = new File(guidePackPath, modid + "/images/" + name + ".png");
				InputStream input = new FileInputStream(file);
				BufferedImage buff = ImageIO.read(input);
	            DynamicTexture previewTexture = new DynamicTexture(buff);
	            ResourceLocation resourceLocation = Minecraft.getMinecraft().renderEngine.getDynamicTextureLocation(key, previewTexture);
	            
	             imageWidths.put(key, buff.getWidth() );
	            imageHeights.put(key, buff.getHeight());
	            dynamicTextures.put(modid + ":" + name, previewTexture);
	            textureLocations.put(modid + ":" + name, resourceLocation);
	            input.close();
	        }
			
			
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
	}
	
	@Override
	public void bindGuideImage(String modid, String name) {
		String key = modid + ":" + name;
        loadGuideImage(modid, name);
        
        dynamicTextures.get(key).updateDynamicTexture();
		Minecraft.getMinecraft().renderEngine.bindTexture(textureLocations.get(key));
	}
	
	public static int loadTexture(BufferedImage image) {
		
		int BYTES_PER_PIXEL = 4;
		
		int[] pixels = new int[image.getWidth() * image.getHeight()]; // Makes an array of pixels for modification
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth()); // Sets the RGB values to 0

		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL); // 4 for RGBA, 3 for RGB

		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int pixel = pixels[y * image.getWidth() + x];
				buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
				buffer.put((byte) ((pixel >> 8) & 0xFF)); // Green component
				buffer.put((byte) (pixel & 0xFF)); // Blue component
				buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha component.
			}
		}

		buffer.flip(); // FOR THE LOVE OF GOD DO NOT FORGET THIS

		// You now have a ByteBuffer filled with the color data of each pixel.
		// Now just create a texture ID and bind it. Then you can load it using
		// whatever OpenGL method you want

		int textureID = GL11.glGenTextures(); // Generate texture ID
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID); // Bind texture ID

		// Setup wrap mode
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);

		// Setup texture scaling filtering
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);

		// Send texel data to OpenGL
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, image.getWidth(), image.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

		// Return the texture ID so we can bind it later again
		return textureID;
	}
	
	public Minecraft getMC() {
		return Minecraft.getMinecraft();
	}
}
