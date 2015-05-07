package com.thecodewarrior.guides.guides.elements;

import java.util.HashMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;
import org.w3c.dom.Node;

import com.thecodewarrior.guides.GuideMod;
import com.thecodewarrior.guides.gui.Rect;

public class GuideElementImage extends GuideElement {

	protected Alignment align = Alignment.LEFT;
	protected String srcMod;
	protected String srcName;
	protected int forceWidth;
	protected int forceHeight;
	protected double scale;
	private int imgWidth;
	private int imgHeight;
	
	public GuideElementImage(int x, int y, int windowWidth, Node node) {
		super(x, y, windowWidth);
		
		HashMap<String, String> args = null;//Tag.parseArgs(data);
		String srcString = node.getAttributes().getNamedItem("src").getNodeValue();
		String[] src = srcString.split(":", 2);
		if(src.length < 2) {
			srcMod  = "minecraft";
			if(GuideMod.dev) {
				srcName = "MISSING_DEV";
			} else {
				srcName = "MISSING";
			}
		} else {
			srcMod  = src[0];
			srcName = src[1];
		}
		
		
		String alignStr = "left";
		if(node.getAttributes().getNamedItem("align") != null) {
			alignStr = node.getAttributes().getNamedItem("align").getNodeValue();
		}
		
		if(alignStr.equalsIgnoreCase("left")) {
			align = Alignment.LEFT;
		} else
		if(alignStr.equalsIgnoreCase("right")) {
			align = Alignment.RIGHT;
		} else
		if(alignStr.equalsIgnoreCase("center")) {
			align = Alignment.CENTER;
		}
		
		GuideMod.proxy.loadGuideImage(srcMod, srcName);
		
		forceWidth  = this.width-5;
		forceHeight = -1;
		Node widthNode = node.getAttributes().getNamedItem("width");
		if(widthNode != null) {
			forceWidth = Integer.parseInt( widthNode.getNodeValue() );
		}
		
		Node heightNode = node.getAttributes().getNamedItem("height");
		if(heightNode != null) {
			forceHeight = Integer.parseInt(heightNode.getNodeValue());
		}
		
		this.imgWidth  = GuideMod.proxy.imageWidth (srcMod, srcName);
		this.imgHeight = GuideMod.proxy.imageHeight(srcMod, srcName);
		
		double forceWidthScale  = -1;
		double forceHeightScale = -1;
		
		//if(forceWidth  != -1) {
		forceWidthScale = (double)(forceWidth)/(double)imgWidth;
		//}
		if(forceHeight != -1) {
			forceHeightScale = (double)(forceHeight)/(double)imgHeight;
		}
		
		double scale = forceWidthScale;
		if(forceHeightScale != -1) {
			scale = Math.min(scale, forceHeightScale);
		}
		
		setScale(scale);
		
		//this.scale = scale;
		//this.bounds = new Rect(0, y, (int)( imgWidth*scale ), (int)( imgHeight*scale )+Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT);
		
		// TODO Auto-generated constructor stub
	}
	
	public void setScale(double scale) {
		this.scale = scale;
		this.bounds = new Rect(x, y, (int)( imgWidth*scale ), (int)( imgHeight*scale ));//+Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT);
	}
	
	public double scale() {
		return this.scale;
	}
	
	public int newX() {
		return this.x + (int)(imgWidth*scale);
	}
	
	public int newY() {
		return this.y + (int)( imgHeight*scale );// + Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
	}
	
	public void clickInside(int mX, int mY, int button) {
		
	}
	
	public void draw(int mX, int mY) {		
		//Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(Reference.MODID, "textures/gui/book_of_revealing_gui.png"));
		
		GuideMod.proxy.bindGuideImage(srcMod, srcName);
		
//		int width  = GuideMod.proxy.imageWidth(srcMod, srcName);
//		int height = GuideMod.proxy.imageHeight(srcMod, srcName);

//		double scale = (double)(this.width-5)/(double)width;
		
//		int fh = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT;
		
		
		
//		if(forceWidth  != -1) {
//			double targetScale = (double)this.forceWidth/(double)width;
//			if(targetScale < scale) {
//				scale = targetScale;
//			}
//		}
//		if(forceHeight != -1) {
//			double targetScale = (double)this.forceWidth/(double)width;
//			if(targetScale < scale) {
//				scale = targetScale;
//			}
//		}
		
		double drawOrigX  = x;
		double drawOrigY  = y;//+fh;
		double drawHeight = (imgHeight*scale);
		double drawWidth  = (imgWidth *scale);
		
		
		
		if(this.align == Alignment.LEFT){
			// do nothing
		} else if (this.align == Alignment.RIGHT) {
			drawOrigX = this.width - drawWidth;
		} else if (this.align == Alignment.CENTER) {
			drawOrigX = (this.width-drawWidth)/2;
		}
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

		Tessellator t = Tessellator.instance;
		
		t.startDrawingQuads();
		
		t.addVertexWithUV(drawOrigX          , drawOrigY           , 0,
				0, 0);
		t.addVertexWithUV(drawOrigX          , drawOrigY+drawHeight, 0,
				0, 1);
		t.addVertexWithUV(drawOrigX+drawWidth, drawOrigY+drawHeight, 0,
				1, 1);
		t.addVertexWithUV(drawOrigX+drawWidth, drawOrigY           , 0,
				1, 0);
		
		t.draw();
	}
	
	public enum Alignment {
		LEFT, CENTER, RIGHT
	}
	
}
