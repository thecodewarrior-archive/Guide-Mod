package com.thecodewarrior.guides.views;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.thecodewarrior.guides.GuideMod;
import com.thecodewarrior.guides.Reference;
import com.thecodewarrior.guides.gui.GuiBookOfRevealing;
import com.thecodewarrior.guides.gui.GuiButtonCustomTexture;
import com.thecodewarrior.guides.guides.Guide;
import com.thecodewarrior.guides.guides.elements.GuideElement;

public class ViewGuide extends View {

	public static final ResourceLocation tex = new ResourceLocation(Reference.MODID, "textures/gui/view/view_guide.png");
	
	public int scroll=0;
	
	public ViewGuide(Guide guide, int width, int height, GuiBookOfRevealing gui) {
		super(guide, width, height, gui);
	}

	private List<GuideElement> elements;

	public int contentHeight;
	
	public static double scrollMultiplier = 1;
	
	public static int scrollbarTop = 16;
	public static int scrollbarLeft = 230;
	public static int scrollbarHeight = 83;
	public static int scrollbarWidth = 16;
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
		this.buttonList.add(new GuiButtonCustomTexture(1, scrollbarLeft, 0 , 0, 224, 16, 16, tex));
		this.buttonList.add(new GuiButtonCustomTexture(2, scrollbarLeft, 99, 0, 240, 16, 16, tex));
	}
	
	public void scroll(int amt) {
		tryScroll(amt);
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
		int px = -(int)(this.scroll/(double)scrollTickSize)*mc.fontRenderer.FONT_HEIGHT;
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
			tryScroll( this.scrollTickSize);
			break;
		case 2:
			tryScroll(-this.scrollTickSize);
			break;
		}
	}
	
	public void draw(int mX, int mY) {
		if(mc == null)
			return;
		
		mc.renderEngine.bindTexture(tex);
		drawTexturedModalRect(0, 0, 0, 0, width, height);
		super.drawButtons(mX, mY);
		double frac = getScrollFraction();
		drawTexturedModalRect(scrollbarLeft, scrollbarTop + (int)(frac*(scrollbarHeight-7) ), 0, 217, 16, 7);
		
		int scrollPx = getScrollPx();
		
		GL11.glTranslated(0, scrollPx, 0);
		if(elements != null) {
			for(GuideElement element: elements) {
				element.draw(mX, mY-scrollPx);
			}
		}
		GL11.glTranslated(0, -scrollPx, 0);
	}
}
