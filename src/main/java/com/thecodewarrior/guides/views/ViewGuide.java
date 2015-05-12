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

public class ViewGuide extends View {

	public static final Logger l = GuideMod.logChild("ViewGuide");
	
	public static final ResourceLocation tex = new ResourceLocation(Reference.MODID, "textures/gui/view/view_guide.png");
	
	public int scroll=0;
	
	public ViewGuide(Guide guide, int width, int height, GuiBookOfRevealing gui) {
		super(guide, width, height, gui);
	}

	public String guideName() {
		return this.guide.guideName;
	}
	
	private List<GuideElement> elements;

	public int contentHeight;
	
	public static double scrollMultiplier = 1;
	
	public static int scrollbarTop = 14;
//	public static int scrollbarLeft = 230;
	public static int buttonHeight = 13;
	public static int scrollbarHeight = 188-(2*buttonHeight);
	public static int scrollbarWidth = 11;
	public static int scrollTickSize = 120; // distance one "click" of mouse moves
	
	@Override
	public void init() {
		int scrollBarWidth = 16;
		elements = guide.getGuideElements(width-scrollBarWidth, height);
		GuideMod.l.info("length is" + elements.size());
		this.contentHeight = 0;
		for(GuideElement elem: elements) {
			if(elem.bounds.getBottom() > this.contentHeight) {
				this.contentHeight = elem.bounds.getBottom();
			}
		}
		this.buttonList.add(new GuiButtonCustomTexture(1,
							this.width-scrollbarWidth-1, 1,
							11, 0,
							11, buttonHeight, tex));
		this.buttonList.add(new GuiButtonCustomTexture(2,
							this.width-scrollbarWidth-1, 1+scrollbarHeight+buttonHeight,
							11, buttonHeight,
							11, buttonHeight, tex));
	}
	
	public void scroll(int mX, int mY, int amt) {
		tryScroll(amt/120 * mc.fontRenderer.FONT_HEIGHT);
	}
	
	public double getScrollFraction() {
		int overflow = ( this.contentHeight - this.height );
		int done = -this.getScrollPx();
		return (double)done / (double)overflow;
	}

	protected void tryScroll(int amt) {
		if(amt > 1) {
			if(this.scroll != 0) {
				this.scroll += amt;
			}
			if(this.scroll > 0) {
				this.scroll = 0;
			}
		} else if(amt < 1){
			if(( ( this.contentHeight - this.height ) + this.getScrollPx() ) > 0) { // scrollPx is negative
				this.scroll += amt; // amt is negative
			}
		}
	}
	
	public int getScrollPx() {
		if(mc == null) {
			return 0;
		}
		int overflow = (this.contentHeight - this.height);
		int px = -(int)(this.scroll);
		if(px > overflow && overflow > 0) {
			return -overflow;
		} else {
			return -px;
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
		switch(guibutton.id) {
		case 1:
			tryScroll(mc.fontRenderer.FONT_HEIGHT);
			break;
		case 2:
			tryScroll(-mc.fontRenderer.FONT_HEIGHT);
			break;
		}
	}
	
	public void draw(int mX, int mY) {
		if(mc == null)
			return;
		
		mc.renderEngine.bindTexture(tex);
		drawTexturedModalRect(this.width-scrollbarWidth-1, scrollbarTop, 0, 0, 11, scrollbarHeight);
		super.drawButtons(mX, mY);
		double frac = getScrollFraction();
		drawTexturedModalRect(this.width-scrollbarWidth, scrollbarTop + (int)(frac*(scrollbarHeight-5) ), 11, 26, 9, 5);
		
		int scrollPx = getScrollPx();
		
		GL11.glTranslated(0, scrollPx, 0);
		if(elements != null) {
			for(GuideElement element: elements) {
				element.draw(mX, mY-scrollPx);
			}
		}
		GL11.glTranslated(0, -scrollPx, 0);
	}
	
	public void scrollTo(int x) {
		tryScroll( -x - scroll );
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
