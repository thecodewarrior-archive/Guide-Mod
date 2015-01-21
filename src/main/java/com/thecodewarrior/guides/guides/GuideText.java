package com.thecodewarrior.guides.guides;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;

import org.apache.commons.lang3.StringUtils;

import scala.actors.threadpool.Arrays;

import com.thecodewarrior.guides.gui.IndexedRect;
import com.thecodewarrior.guides.guides.elements.GuideElement;
import com.thecodewarrior.guides.guides.elements.GuideElementText;
import com.thecodewarrior.guides.guides.elements.GuideElementTextLink;

public class GuideText extends Guide {

	protected List<String> parts;
		
	public int textMargin = 1;
	
	public static String chrOneWidth = "\u258F"; // a one pixel width char
	
	/**
	 * Gets characters with specific widths for specific purposes. Doesn't include NULL as that would end a string
	 * @param fr FontRenderer to use for getting char widths
	 */
	public static void getSeperator(FontRenderer fr) {
		for(int c=1; c < 65536-1; c++) {
			char chr = (char)c;
			if(fr.getCharWidth(chr) == 1) {
				chrOneWidth = Character.toString((char)c);
				return;
			}
		}
	}
	
	protected int lineCount = 0;
	protected int topLine   = 0;
	
	public GuideText(String guideText) {
		super(guideText);
	}

	private String subsetJoin(String[] arr, int start, String joinWith) {
		return subsetJoin(arr,start,arr.length, joinWith);
	}
	
	private String subsetJoin(String[] arr, int start, int end, String joinWith) {
		return StringUtils.join( Arrays.copyOfRange(arr, start, end) , joinWith);
	}
	
	
	@Override
	public boolean parse() {
				
		if(this.rawGuide == null) {
			this.parts = new ArrayList<String>();
			return false;
		}
		
		parts = Arrays.asList( rawGuide.split("<<|>>") );
		
//		
//		String[] guideParts = rawGuide.split("<<|>>");
//		this.links = new String[(int) Math.floor(guideParts.length/2.0)];
//		
//		int linkNum = 0;
//		boolean isLink = false;
//		
//		StringBuilder builder = new StringBuilder();
//		
//		for(String part : guideParts) {
//			if(isLink) {
//				if(part.matches(".+?\\|.+?:.+?")) { // link to another guide
//					String linkText = part.split("\\|")[0];
//					
//					builder.append( seperator+ linkText +seperator );
//					
//					this.links[linkNum] = "guide:" + subsetJoin( part.split("\\|"), 1, "|");
//				}
//			} else {
//				builder.append( part );
//			}
//			isLink = !isLink;
//		}
//		
//		guide = builder.toString();
		return true;
	}
	
	@Override
	public List<GuideElement> getGuideElements(int width, int height) {
		List<GuideElement> elements = new ArrayList<GuideElement>();
		
		int curX = 0;
		int curY = 0;
		boolean isTag = false;
		for(String part : parts) {
			GuideElement element = null;
			if(isTag) {
				element = new GuideElementTextLink(curX, curY, width, part);
			} else {
				element = new GuideElementText(curX, curY, width, part);
			}
			if(element != null) {
				curX = element.newX();
				curY = element.newY();
				element.gui = this.gui;
				elements.add(element);
			}
			isTag = !isTag;
		}
		
//		
//		if(fontRendererObj == null)
//			return elements;
//		if(guide == null) {
//			return elements;
//		}
//		List<String> lines = fontRendererObj.listFormattedStringToWidth(guide, width);
//		
//		
//		int lineNum = 0;
//		int toSkip = topLine;
//		int linkNum = 0;
//		int curY = textMargin;
//		
//		int fontHeight = fontRendererObj.FONT_HEIGHT;
//		boolean isLink = false;
//		List<GuideElementText> currentLink = null;
//		for(String line: lines) {
//			String[] lineParts = line.split(seperator);
//			int curX=textMargin;
//			//isLink = false;
//			boolean linkLast = false;
//			for(String linePart: lineParts) {
//				linkLast = false;
//				int len = fontRendererObj.getStringWidth(linePart);
//				if(isLink) {
//					if(currentLink == null) {
//						currentLink = new ArrayList<GuideElementText>();
//					}
//					currentLink.add(new GuideElementText(curX, curY, linePart));
//				} else {
//					if(currentLink != null) {
//						elements.add(new GuideElementTextLink(currentLink, this.links[linkNum], 0x200040, 0x500050));
//						currentLink = null;
//						linkNum++;
//					}
//					elements.add(new GuideElementText(curX,curY,linePart));
//				}
//				curX += len;
//				isLink = !isLink;
//			}
//			isLink = linkLast;
//			curY += fontHeight;
//			lineNum++;
//		}
//		
//		
//		int textLeft = textMargin;
//		int textTop  = textMargin;
//		
		return elements;
	}

}
