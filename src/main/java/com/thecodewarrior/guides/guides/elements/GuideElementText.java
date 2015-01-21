package com.thecodewarrior.guides.guides.elements;

import java.util.List;

import net.minecraft.client.Minecraft;

import org.apache.commons.lang3.StringUtils;

import com.thecodewarrior.guides.gui.MultiRect;
import com.thecodewarrior.guides.gui.Rect;
import com.thecodewarrior.guides.guides.GuideText;

public class GuideElementText extends GuideElement {

	public List<String> lines;
	public int color;
	
	public GuideElementText(int x, int y, int width) {
		this(x, y, width, "");
	}
	
	public GuideElementText(int x, int y, int width, String string) {
		this(x, y, width, string, 0x404040);
	}
	
	public GuideElementText(int x, int y, int width, String string, int color) {
		super(x,y, width);
		if(fontRendererObj == null) { return; }
		
		parse(string);
		
		this.color = color;
	}
	
	public void parse(String string) {
		this.lines = fontRendererObj.listFormattedStringToWidth(
				StringUtils.repeat(GuideText.chrOneWidth, x) + string,
				width);
		
		this.lines.set(0, this.lines.get(0).substring(x));
		
		MultiRect rect = new MultiRect();
		boolean isFirstLine = true;
		int curY = y;
		for(String line : lines) {
			rect.add(new Rect(
						isFirstLine ? x : 0,
						curY,
						fontRendererObj.getStringWidth(line),
						fontRendererObj.FONT_HEIGHT));
			curY += fontRendererObj.FONT_HEIGHT;
			isFirstLine = false;
		}
		this.bounds = rect;
		
		if(this.lines.size() == 1) {
			this.newX = x + fontRendererObj.getStringWidth(this.lines.get(0));
		} else {
			this.newX = fontRendererObj.getStringWidth(this.lines.get(this.lines.size()-1));
		}
		this.newY = y + ( (this.lines.size()-1) * fontRendererObj.FONT_HEIGHT );
	}
	
	protected void drawString(int _color) {
		boolean isFirstLine = true;
		int curY = y;
		for(String line : lines) {
			fontRendererObj.drawString(line,
					isFirstLine ? x : 0, curY, _color);
			curY += fontRendererObj.FONT_HEIGHT;
			isFirstLine = false;
		}
	}
	
	@Override
	public void draw(int mX, int mY) {
		this.drawString(this.color);
	}

}
