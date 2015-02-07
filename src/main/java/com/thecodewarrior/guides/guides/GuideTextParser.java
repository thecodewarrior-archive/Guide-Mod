package com.thecodewarrior.guides.guides;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.thecodewarrior.guides.api.GuideRegistry;
import com.thecodewarrior.guides.gui.GuiBookOfRevealing;
import com.thecodewarrior.guides.guides.elements.GuideElement;
import com.thecodewarrior.guides.guides.elements.GuideElementText;
import com.thecodewarrior.guides.guides.tags.Tag;

public class GuideTextParser {
	
	
	public static List<GuideElement> parse(NodeList nodes, GuiBookOfRevealing gui, int width, int height, int x, int y) {
		return parse(nodes, gui, width, height, x, y, new ArrayList<String>());
	}
	
	public static List<GuideElement> parse(NodeList nodes, GuiBookOfRevealing gui, int width, int height, int x, int y, List<String> format) {
		List<GuideElement> elements = new ArrayList<GuideElement>();

		int curX = x;
		int curY = y;
		int curWidth = width;
		for(int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			GuideElement e = null;
			if(node.getNodeType() == Node.TEXT_NODE) {
				
				String text = node.getNodeValue();
				if(text.startsWith("\n")) {
					text = text.substring(1);
				}
				e = new GuideElementText(curX, curY, curWidth, text, format);
				
				
			} else {
				String protocol = node.getNodeName();
				Tag tag = GuideRegistry.getTag(protocol);
				if(tag != null) {
					e = tag.getElement(curX, curY, curWidth, node, new ArrayList<String>(format));
				}
			}
			if( e != null ) {
				e.gui = gui;
				curX = e.newX();
				curY = e.newY();
				curWidth = e.newWidth();
				
				elements.add(e);
			}
		}
		
		
		
		return elements;
	}
}
