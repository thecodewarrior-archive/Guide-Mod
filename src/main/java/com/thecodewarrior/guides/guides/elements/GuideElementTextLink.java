package com.thecodewarrior.guides.guides.elements;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import scala.actors.threadpool.Arrays;

import com.thecodewarrior.guides.GuideMod;
import com.thecodewarrior.guides.api.GuideRegistry;

public class GuideElementTextLink extends GuideElementText {

	protected List<GuideElementText> elements;
	protected int defaultColor;
	protected int hoverColor;
	protected String link;
	
	protected String data;
	
	/**
	 * Create a new link element with the default colors
	 * @param elements a list of text elements that make up this link
	 * @param node link node
	 */
	public GuideElementTextLink(int x, int y, int width, Node node) {
		this(x, y, width, node, 0x0000FF, 0x990099);
	}
	
	/**
	 * Create a new link element
	 * @param elements a list of text elements that make up this link
	 * @param node link node
	 * @param color main text color
	 * @param hoverColor color when mouse is hovering
	 */
	public GuideElementTextLink(int x, int y, int width, Node node, int color, int hoverColor) {
		super(x, y, width);
		String text = node.getFirstChild().getNodeValue();
		parse(UNDERLINE + text);
		NamedNodeMap map = node.getAttributes();
		Node attrNode = map.getNamedItem("href");
		this.link = attrNode.getNodeValue();//.getAttributes().getNamedItem("href").getNodeValue();//this.data.split("\\|", 2)[1];
		GuideMod.l.info(text + "=" + this.link);
		this.defaultColor = color;
		this.hoverColor = hoverColor;
		// TODO Auto-generated constructor stub
	}
	
	public void clickInside(int mX, int mY, int button) {
		this.gui.refreshGuide(GuideRegistry.newBasicGuide(this.link));
	}

	private String subsetJoin(String[] arr, int start, String joinWith) {
		return subsetJoin(arr,start,arr.length, joinWith);
	}
	
	private String subsetJoin(String[] arr, int start, int end, String joinWith) {
		return StringUtils.join( Arrays.copyOfRange(arr, start, end) , joinWith);
	}
	
	public void draw(int mX, int mY) {
		
		boolean hovering = bounds.pointInside(mX, mY);
		
		int color = (hovering ? this.hoverColor : this.defaultColor);
		
		this.drawString(color);
		
		
//		this.drawRect(this.x     , this.y     +fontRendererObj.FONT_HEIGHT-1,
//				      this.newX(), this.newY()+fontRendererObj.FONT_HEIGHT,
//				      0xFF000000 + color);
	}

}
