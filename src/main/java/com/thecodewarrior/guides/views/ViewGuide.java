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
	
	@Override
	public void init() {
		int scrollBarWidth = 16;
		elements = guide.getGuideElements(width-scrollBarWidth, height);
		GuideMod.logger.info("length is" + elements.size());
		this.contentHeight = 0;
		for(GuideElement elem: elements) {
			if(elem.bounds.getBottom() > this.contentHeight) {
				this.contentHeight = elem.bounds.getBottom();
			}
		}
		this.buttonList.add(new GuiButtonCustomTexture(1, scrollbarLeft, 0 , 0, 224, 16, 16, tex));
		this.buttonList.add(new GuiButtonCustomTexture(2, scrollbarLeft, 99, 0, 240, 16, 16, tex));
	}
	
	public double getScrollFraction() {
		return ((double)-scroll)/contentHeight;
	}

	public int getScrollPx() {
		return this.scroll;
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
			if(this.scroll != 0) {
				this.scroll += 1;
			}
			break;
		case 2:
			if(this.getScrollFraction() != 1.0D) {
				this.scroll -= 1;
			}
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
