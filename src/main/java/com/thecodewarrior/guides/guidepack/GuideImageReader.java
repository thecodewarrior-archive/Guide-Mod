package com.thecodewarrior.guides.guidepack;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;

public class GuideImageReader {

	private static HashMap<String, ResourceLocation> textureLocations   = new HashMap<String, ResourceLocation>();
	private static HashMap<String, DynamicTexture>   dynamicTextures    = new HashMap<String, DynamicTexture>();
	private static HashMap<String, Integer> imageWidths  = new HashMap<String, Integer>();
	private static HashMap<String, Integer> imageHeights = new HashMap<String, Integer>();
	
	public GuideImageReader() {
		// TODO Auto-generated constructor stub
	}
	
	public static void bindGuideImage(String modid, String name) {
		String key = modid + ":" + name;
        loadGuideImage(modid, name);
        
        dynamicTextures.get(key).updateDynamicTexture();
		Minecraft.getMinecraft().renderEngine.bindTexture(textureLocations.get(key));
	}
	
	public static void loadGuideImage(String modid, String name)
	{
		try {
			String key = modid + ":" + name;
			if(!textureLocations.containsKey(modid + ":" + name)) {
//	        	ZipFile file = getZipFile(modid);
//	    	    InputStream input = file.getInputStream(new ZipEntry("images/" + name + ".png"));
//				File file = new File(guidePackPath, modid + "/images/" + name + ".png");
				InputStream input = GuidePackReader.getInputStream(modid + "/images/" + name + ".png");//new FileInputStream(file);
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
	
	// I didn't make this, I have nowhere near the expertise to create something this awesome
	// and I still only vaguely understand it
	// the problem is, I forgot where I got this code
	public static int loadTexture(BufferedImage image) {
		
		int BYTES_PER_PIXEL = 4;
		
		int[] pixels = new int[image.getWidth() * image.getHeight()]; // Makes an array of pixels for modification
		image.getRGB(0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth()); // Sets the RGB values to 0

		ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * BYTES_PER_PIXEL); // 4 for RGBA, 3 for RGB

		for (int y = 0; y < image.getHeight(); y++) {
			for (int x = 0; x < image.getWidth(); x++) {
				int pixel = pixels[y * image.getWidth() + x];
				buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red component
				buffer.put((byte) ((pixel >> 8) & 0xFF));  // Green component
				buffer.put((byte) (pixel & 0xFF));         // Blue component
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

	public static int imageWidth(String modid, String name) {
		return imageWidths.get(modid + ":" + name);
	}
	
	public static int imageHeight(String modid, String name) {
		return imageHeights.get(modid + ":" + name);
	}
	
}
