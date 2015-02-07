package com.thecodewarrior.guides.guides.elements;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import com.thecodewarrior.guides.GuideMod;
import com.thecodewarrior.guides.Reference;
import com.thecodewarrior.guides.guides.tags.Tag;
import com.thecodewarrior.notmine.codechicken.lib.colour.ColourRGBA;

public class GuideElementBullet extends GuideElement{

	ResourceLocation tex = new ResourceLocation(Reference.MODID, "textures/gui/icons.png");
	
	BulletType type = BulletType.ROUND;
	
	int color = 0x777777;
	
	public GuideElementBullet(int x, int y, int width, String str) {
		super(x, y, width);
		
		HashMap<String, String> args = null;// Tag.parseArgs(str);
		
		if(args.containsKey("0")) {
			String arg = args.get("0");
			if(arg.equalsIgnoreCase("round")) {
				type = BulletType.ROUND;
			}
			if(arg.equalsIgnoreCase("round_open")) {
				type = BulletType.EMPTY_ROUND;
			}
			if(arg.equalsIgnoreCase("diamond")) {
				type = BulletType.DIAMOND;
			}
			if(arg.equalsIgnoreCase("diamond_open")) {
				type = BulletType.EMPTY_DIAMOND;
			}
		}
		if(args.containsKey("color")) {
			String arg = args.get("color");
			if(arg.startsWith("0x")) {
				arg = arg.substring(2);
			}
			try {
				this.color = Integer.parseUnsignedInt(arg, 16);
			} catch(NumberFormatException e) {
				GuideMod.l.warn("[Bullet element]Unable to parse color: " + args.get("color"));
			}
		}
	}
	
	public void draw(int mX, int mY) {
		int texW = 8;
		int texH = 8;
		int texU = 0;
		int texV = 0;
		switch(this.type) {
		case ROUND:
			texU = 0;
			texV = 0;
			break;
		case EMPTY_ROUND:
			texU = texW;
			texV = 0;
			break;
		case DIAMOND:
			texU = 0;
			texV = texH;
			break;
		case EMPTY_DIAMOND:
			texU = texW;
			texV = texH;
			break;
		}
		
		ColourRGBA c = new ColourRGBA(this.color);
		
		Minecraft.getMinecraft().renderEngine.bindTexture(tex);
		GL11.glColor4b(c.r, c.g, c.b, c.a);
		
		this.drawTexturedModalRect(this.x-texW-2, this.y, texU, texV, texW, texH);
		
		
	}

	public enum BulletType {
		ROUND, EMPTY_ROUND, DIAMOND, EMPTY_DIAMOND
	}
	
}
