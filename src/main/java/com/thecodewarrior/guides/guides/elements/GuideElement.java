package com.thecodewarrior.guides.guides.elements;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;

import com.thecodewarrior.guides.GuideMod;
import com.thecodewarrior.guides.gui.GuiBookOfRevealing;
import com.thecodewarrior.guides.gui.Rect;

public class GuideElement extends Gui {
	
	public Rect bounds = new Rect(0,0,0,0);
	public FontRenderer fontRendererObj = GuideMod.proxy.getFontRenderer();
	public GuiBookOfRevealing gui;
	
	protected int x;
	protected int y;
	protected int width;
	
	protected int newX;
	protected int newY;
	
	public GuideElement(int x, int y, int width) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.newX = x;
		this.newY = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getScreenWidth() {
		return width;
	}
	
	public int newX() {
		return this.newX;
	}
	
	public int newY() {
		return this.newY;
	}
	
	public int newWidth() {
		return this.width;
	}
	
	public void clickInside(int mX, int mY, int button) {
		
	}
	
	public void draw(int mX, int mY) {
		
	}
}
