package com.thecodewarrior.guides.guides.tags;

import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Node;

import com.thecodewarrior.guides.guides.elements.GuideElement;

public abstract class Tag {
	protected String protocol;
	
	public Tag(String protocol) {
		this.protocol = protocol;
	}
	
	public String getProtocol() {
		return this.protocol;
	}
	
	public GuideElement getElement(int x, int y, int width, Node node) {
		return getElement(x, y, width, node, new ArrayList<String>());
	}
	
	public abstract GuideElement getElement(int x, int y, int width, Node node, List<String> format);
}
