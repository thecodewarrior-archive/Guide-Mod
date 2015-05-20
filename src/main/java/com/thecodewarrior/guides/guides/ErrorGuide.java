package com.thecodewarrior.guides.guides;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.util.StatCollector;

import com.thecodewarrior.guides.guides.elements.GuideElement;
import com.thecodewarrior.guides.guides.elements.GuideElementLine;
import com.thecodewarrior.guides.guides.elements.GuideElementText;

public class ErrorGuide extends Guide {

	public String name;
	public String error;
	
	public ErrorGuide(String name, String error) {
		super(null, null);
		this.name = name;
		this.error = error;
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean parse() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public String viewName() {
		return "ERROR";
	}

	@Override
	public List<GuideElement> getGuideElements(int width, int height) {
		List<GuideElement> elements = new ArrayList<GuideElement>();
		
		elements.add(new GuideElementText(1, 1, width, StatCollector.translateToLocal(name), 0xCC1F1F));
		elements.add(new GuideElementLine(10, 10, 20, 10, 1.0F, 0x000000));
		elements.add(new GuideElementText(10,10,width, error));
		
		return elements;
	}

}
