package com.thecodewarrior.guides.guides.elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.thecodewarrior.guides.GuideMod;
import com.thecodewarrior.guides.gui.MultiRect;
import com.thecodewarrior.guides.gui.Rect;
import com.thecodewarrior.guides.guides.GuideText;

public class GuideElementText extends GuideElement {

	public static final String OBFUSCATED    = "§k";
	public static final String BOLD          = "§l";
	public static final String STRIKETHROUGH = "§m";
	public static final String UNDERLINE     = "§n";
	public static final String ITALIC        = "§o";
	public static final String RESET         = "§r";
	
	public List<String> formats = new ArrayList<String>();
	
	public List<String> lines;
	public int color;
	
	public GuideElementText(int x, int y, int width) { // 1->2
		this(x, y, width, new ArrayList<String>());
	}
	
	public GuideElementText(int x, int y, int width, List<String> format) { // 2->4
		this(x, y, width, "", format);
	}
	
	public GuideElementText(int x, int y, int width, String string) { // 3->4
		this(x, y, width, string, 0x404040, new ArrayList<String>());
	}
	
	public GuideElementText(int x, int y, int width, String string, List<String> format) { // 4->6
		this(x, y, width, string, 0x404040, format);
	}
	
	public GuideElementText(int x, int y, int width, String string, int color) { //5->6
		this(x, y, width, string, color, new ArrayList<String>());
	}
	
	public GuideElementText(int x, int y, int width, String string, int color, List<String> format) { // 6->*
		super(x,y, width);
		if(fontRendererObj == null) { return; }
		
		parse(  StringUtils.join(format, "") + 
				string);
		
		this.color = color;
	}
	
	
	
	public void parse(String string) {
		if(string == "") {
			string = " ";
		}
		Matcher m = Pattern.compile("\n*\\z").matcher(string);
		int trailingLineCount = 0;
		if(m.find()) {
			trailingLineCount = m.group().length();
		}
		this.lines = new ArrayList<String>(
					fontRendererObj.listFormattedStringToWidth(
					StringUtils.repeat(GuideText.chrOneWidth, x) + string,
					width)
				);
		int curXcut = x;
		int i = 0;
		while(curXcut > 0 && curXcut >= this.lines.get(i).length()) {
			curXcut -= this.lines.get(i).length();
			this.lines.set(i, "");
			i += 1;
			if(i >= this.lines.size()) {
				i -= 1;
			}
		}
		if(this.lines.size() != 0) {
			this.lines.set(i, this.lines.get(i).substring(curXcut));
		}
		for(i = 0; i < trailingLineCount; i++) {
			this.lines.add("");
		}
		MultiRect rect = new MultiRect();
		boolean isFirstLine = true;
		int curY = y;
		for(String line : lines) {
			rect.add(new Rect(
						isFirstLine ? x : 0,
						curY,
						fontRendererObj.getStringWidth(line),
						fontRendererObj.FONT_HEIGHT+1));
			curY += fontRendererObj.FONT_HEIGHT+1;
			isFirstLine = false;
		}
		this.bounds = rect;
		
		if(this.lines.size() == 1) {
			this.newX = x + fontRendererObj.getStringWidth(this.lines.get(0));
		} else {
			this.newX = fontRendererObj.getStringWidth(this.lines.get(this.lines.size()-1));
		}
		this.newY = y + ((this.lines.size()-1) * (fontRendererObj.FONT_HEIGHT+1) );
	}
	
	protected void drawString(int _color) {
		String formatting = "";
		for(String format : formats) {
			formatting = formatting + format;
		}
		boolean isFirstLine = true;
		int curY = y;
		for(String line : lines) {
			int curX = isFirstLine ? x : 0;
			fontRendererObj.drawString(formatting + line,
					                   curX, curY, _color);
//			if(underline) {
//				this.drawRect(curX, curY+fontRendererObj.FONT_HEIGHT-1,
//					          curX + fontRendererObj.getStringWidth(line), curY+fontRendererObj.FONT_HEIGHT,
//					      0xFF000000 + _color);
//			}
			curY += fontRendererObj.FONT_HEIGHT+1;
			isFirstLine = false;
		}
	}
	
	@Override
	public void draw(int mX, int mY) {
		this.drawString(this.color);
	}

	public int getSearchMatchX(String str) {
		for(int i = 0; i < lines.size(); i++) {
			String line = lines.get(i);
			if(line.contains(str)) {
				return this.getY() + ( GuideMod.proxy.getFontRenderer().FONT_HEIGHT * i);
			}
		}
		return -1;
	}
}
