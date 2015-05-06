package com.thecodewarrior.guides.guides;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;

import net.minecraft.client.gui.FontRenderer;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import com.thecodewarrior.guides.GuideMod;
import com.thecodewarrior.guides.guides.elements.GuideElement;
import com.thecodewarrior.guides.guides.elements.GuideElementImage;
import com.thecodewarrior.guides.guides.elements.GuideElementText;

public class GuideText extends Guide {

	public static final Logger l = GuideMod.logChild("GuideText");
	
	public static DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
	
	public static XPathFactory xPathfactory = XPathFactory.newInstance();
	public static XPath xpath = xPathfactory.newXPath();
	
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
	
	public GuideText(String guideText, String guideName) {
		super(guideText, guideName);
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
		return true;
	}
	
	@Override
	public List<GuideElement> getGuideElements(int width, int height) {
		
		List<GuideElement> elements = new ArrayList<GuideElement>();

		
		DocumentBuilder builder = null;
		try {
		    builder = builderFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
		    e.printStackTrace();  
		}
		
		if(builder == null) { return elements; }
		
		try {
		    //l.info(rawGuide);
		    Document document = builder.parse( new ByteArrayInputStream(
		    			( 
		    				"<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" + 
		    				"<root>\n" +
		    						rawGuide +
		    				"\n</root>"
		    			).getBytes( "UTF-8" )
		    		) );
		    
		    
		    int top = 0;
		    
		    
		    Element toolnode    = (Element) document.getElementsByTagName("tool").item(0);
		    if(toolnode != null) {
		    	Element img = (Element) toolnode.getElementsByTagName("image").item(0);
		    	if(img != null) {
			    	img.setAttribute("height", "16");
			    	img.setAttribute("align", "right");
		    		GuideElement imgElement = new GuideElementImage(0, 0, width, img);
		    		imgElement.gui = gui;
		    		elements.add(imgElement);
		    		top = imgElement.bounds.getBottom();
		    	}
		    }
		    
		    Node titlenode   = document.getElementsByTagName("title").item(0);
		    if(titlenode != null) {
		    	double scale = 1.4D;
		    	GuideElement titleElement = new GuideElementText(0, 0, width, GuideElementText.BOLD + titlenode.getTextContent().replace("\n", "").replace("\r", ""));
		    	GuideElement scaleElement = new GuideElementScale(titleElement, scale);
		    	int titleHeight = (int)( scaleElement.fontRendererObj.FONT_HEIGHT * scale );
		    	if(titleHeight > top) {
		    		top = titleHeight;
		    	}
		    	titleElement.gui = gui;
		    	scaleElement.gui = gui;
		    	elements.add(scaleElement);
		    }
		    
		    Node contentnode = document.getElementsByTagName("content").item(0);
		    if(contentnode != null) {
		    	elements.addAll( GuideTextParser.parse(contentnode.getChildNodes(), this.gui, width, height, 0, top) );
		    }

		} catch (IOException e) {
		    e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		
		return elements;
		
//		List<GuideElement> elements = new ArrayList<GuideElement>();
//		
//		int curX = 0;
//		int curY = 0;
//		int curWidth = width;
//		boolean isTag = false;
//		for(String part : parts) {
//			GuideElement element = null;
//			if(isTag) {
//				String[] tagParts = part.split(":", 2);
//				String protocol = tagParts[0];
//				String data     = "";
//				if(tagParts.length > 1) {
//					data = tagParts[1];
//				}
//				
//				Tag tag = GuideRegistry.getTag(protocol);
//				if(tag != null) {
//					element = tag.getElement(curX, curY, curWidth, data);
//				}
//			} else {
//				element = new GuideElementText(curX, curY, curWidth, part);
//			}
//			if(element != null) {
//				curX = element.newX();
//				curY = element.newY();
//				curWidth = element.newWidth();
//				element.gui = this.gui;
//				elements.add(element);
//			}
//			isTag = !isTag;
//		}
		
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
//		return elements;
	}

}
