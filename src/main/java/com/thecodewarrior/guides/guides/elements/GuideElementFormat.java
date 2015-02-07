package com.thecodewarrior.guides.guides.elements;

import java.util.List;

import org.w3c.dom.Node;

import com.thecodewarrior.guides.guides.GuideTextParser;

public class GuideElementFormat extends GuideElementContainer {

	public GuideElementFormat(int x, int y, int width, Node node, String formatAdd, List<String> format) {
		super(x, y, width);
		format.add(formatAdd);
		init(GuideTextParser.parse(node.getChildNodes(), gui, width, 10000, x, y, format));
		
	}
	
	public void processGuideElement(GuideElement e) {
//		if(e instanceof GuideElementText) {
//			((GuideElementText) e).formats.add(GuideElementText.BOLD);
//		}
	}

}
