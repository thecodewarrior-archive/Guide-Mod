package com.thecodewarrior.guides.guides.elements;


import java.util.List;

import org.lwjgl.opengl.GL11;
import org.w3c.dom.Node;

import com.thecodewarrior.guides.guides.GuideTextParser;

public class GuideElementIndent extends GuideElementContainer {
	
	public static int defaultDepth = 10;
	public int depth;
	
	public GuideElementIndent(int x, int y, int width, Node node, List<String> format) {
		super(x, y, width);
		depth = defaultDepth;
		Node amt = node.getAttributes().getNamedItem("amt");
		if(amt != null) {
			depth = Integer.parseInt( amt.getNodeValue() );
		}
		
		init( GuideTextParser.parse(node.getChildNodes(), gui, width-depth, 10000, x, y, format) );
		
	}
	
//	public int newWidth() {
//		return this.width - depth;
//	}
	
	public void draw(int mX, int mY) {
		GL11.glTranslated( depth, 0, 0);
			super.draw(mX, mY);
		GL11.glTranslated(-depth, 0, 0);
	}

}
