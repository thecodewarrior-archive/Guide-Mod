package com.thecodewarrior.guides.views;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import com.thecodewarrior.guides.GuideMod;
import com.thecodewarrior.guides.Reference;
import com.thecodewarrior.guides.gui.GuiBookOfRevealing;
import com.thecodewarrior.guides.gui.GuiButtonCustomTexture;
import com.thecodewarrior.guides.guides.Guide;
import com.thecodewarrior.guides.guides.elements.GuideElement;

public class ViewGuide extends ViewScrollable {

	public static final Logger l = GuideMod.logChild("ViewGuide");
	
	public static final ResourceLocation tex = new ResourceLocation(Reference.MODID, "textures/gui/view/view_guide.png");
	
	public ViewGuide(Guide guide, int width, int height, GuiBookOfRevealing gui) {
		super(guide, width, height, gui);
	}

	public String guideName() {
		return this.guide.guideName;
	}
	
	private List<GuideElement> elements;
	
	@Override
	public void init() {
		super.init();
		elements = guide.getGuideElements(width, height);
		this.contentHeight = 0;
		for(GuideElement elem: elements) {
			if(elem.bounds.getBottom() > this.contentHeight) {
				this.contentHeight = elem.bounds.getBottom();
			}
		}
	}
	
	@Override
	public boolean onClick(int mX, int mY, int button) {
		if(elements != null) {
			int scrollPx = getScrollPx();
			for(GuideElement element: elements) {
				if(element.bounds.pointInside(mX, mY-scrollPx)) {
					element.clickInside(
							mX-element.bounds.getLeft(),
							mY-scrollPx-element.bounds.getTop(),
							button);
				}
			}
		}
		
		return false;
	}
	
	public void actionPerformed(GuiButton guibutton) {
		if(scrollActionPerformed(guibutton))
			return;
	}
	
	public void draw(int mX, int mY) {
		if(mc == null)
			return;
		
		super.draw(mX, mY);
		
		mc.renderEngine.bindTexture(tex);
		
		int scrollPx = getScrollPx();
		
		GL11.glTranslated(0, scrollPx, 0);
		if(elements != null) {
			for(GuideElement element: elements) {
				element.draw(mX, mY-scrollPx);
			}
		}
		GL11.glTranslated(0, -scrollPx, 0);
	}
	
	private ArrayList<Integer> searchResults = null;
	private String searchResultQuery = "";
	
	public void updateSearchResults(String search) {
		searchResults = new ArrayList<Integer>();
		for(GuideElement e : elements) {
			searchResults.addAll(e.getSearches(search));
		}
	}
	
	public void updateSearch(String search, int occurance) {
		if(elements != null) {
//			if(occurance == 0)
//				occurance = 1;
			
			if(!search.equals(searchResultQuery)) {
				updateSearchResults(search);
				searchResultQuery = search;
			}
			l.info("occurance = " + occurance);
			l.info("length = " + searchResults.size());
			int xValue = -1;
			if(searchResults.size() > 0) {
				xValue = searchResults.get(occurance % searchResults.size());
			}
			
			if(xValue != -1) {
//				xValue += mc.fontRenderer.FONT_HEIGHT*1.4;
				scrollTo(xValue);
			}
		}
	}
}
