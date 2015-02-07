package com.thecodewarrior.guides.guides;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.FontRenderer;

import com.thecodewarrior.guides.GuideMod;
import com.thecodewarrior.guides.gui.GuiBookOfRevealing;
import com.thecodewarrior.guides.gui.Rect;
import com.thecodewarrior.guides.guides.elements.GuideElement;

public class GuideElementScale extends GuideElement {

	public GuideElement element;
	public double scale;
	
	public GuideElementScale(GuideElement element, double scale) {
		this(element.getX(), element.getY(), element.getScreenWidth(), element, scale);
	}
	
	public GuideElementScale(int x, int y, int width, GuideElement element, double scale) {
		super(x, y, width);
		
		//this.bounds  = new RectScale(element.bounds, scale);
		this.scale   = scale;
		this.element = element;
	}
	
	public int getX() {
		return element.getX();
	}
	
	public int getY() {
		return element.getY();
	}
	
	public int getScreenWidth() {
		return element.getScreenWidth();
	}
	
	public int newX() {
		return element.newX() + (int)(( element.newX()-element.getX() )*scale);
	}
	
	public int newY() {
		return element.newY() + (int)(( element.newY()-element.getY() )*scale);
	}
	
	public int newWidth() {
		return (int)(element.newWidth()*scale);
	}
	
	public void clickInside(int mX, int mY, int button) {
		element.clickInside((int)( mX*scale ), (int)( mY*scale ), button);
	}
	
	public void draw(int mX, int mY) {
		GL11.glPushMatrix();
			GL11.glTranslated( getX(),  getY(), 0);
			GL11.glScaled(scale, scale, 1);
			GL11.glTranslated(-getX(), -getY(), 0);
			element.draw((int)(mX*scale), (int)(mY*scale));
		GL11.glPopMatrix();
	}

}
