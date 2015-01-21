package com.thecodewarrior.guides.guides.elements;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import scala.actors.threadpool.Arrays;

import com.thecodewarrior.guides.api.GuideRegistry;

public class GuideElementTextLink extends GuideElementText {

	protected List<GuideElementText> elements;
	protected int defaultColor;
	protected int hoverColor;
	protected String link;
	
	protected String protocol;
	protected String data;
	
	/**
	 * Create a new link element with the default colors
	 * @param elements a list of text elements that make up this link
	 * @param link link text
	 */
	public GuideElementTextLink(int x, int y, int width, String link) {
		this(x, y, width, link, 0x0000FF, 0xFF00FF);
	}
	
	/**
	 * Create a new link element
	 * @param elements a list of text elements that make up this link
	 * @param link link text
	 * @param color main text color
	 * @param hoverColor color when mouse is hovering
	 */
	public GuideElementTextLink(int x, int y, int width, String link, int color, int hoverColor) {
		super(x, y, width);
		
		String[] split = link.split(":", 2);
		
		this.protocol = split[0];
		this.data = split[1];
		
		parse(this.data.split("\\|", 2)[0]);
		
		this.defaultColor = color;
		this.hoverColor = hoverColor;
		// TODO Auto-generated constructor stub
	}
	
	public void clickInside(int mX, int mY, int button) {
		if(protocol.equalsIgnoreCase("GUIDE")) {
			this.gui.refreshGuide(GuideRegistry.newBasicGuide(data));
		}
	}

	private String subsetJoin(String[] arr, int start, String joinWith) {
		return subsetJoin(arr,start,arr.length, joinWith);
	}
	
	private String subsetJoin(String[] arr, int start, int end, String joinWith) {
		return StringUtils.join( Arrays.copyOfRange(arr, start, end) , joinWith);
	}
	
	public void draw(int mX, int mY) {
		
		boolean hovering = bounds.pointInside(mX, mY);
		
		this.drawString(hovering ? this.hoverColor : this.defaultColor);
	}

}
