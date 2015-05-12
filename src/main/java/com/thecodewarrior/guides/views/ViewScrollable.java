package com.thecodewarrior.guides.views;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import com.thecodewarrior.guides.Reference;
import com.thecodewarrior.guides.gui.GuiBookOfRevealing;
import com.thecodewarrior.guides.gui.GuiButtonCustomTexture;
import com.thecodewarrior.guides.guides.Guide;

public class ViewScrollable extends View {
	
	public static ResourceLocation scrollTexture = new ResourceLocation(Reference.MODID, "textures/gui/view/view_scroll.png");
	
	public static double scrollMultiplier = 1;
	
	public static int scrollbarTop = 14;
	public static int buttonHeight = 13;
	public static int scrollbarHeight = 188-(2*buttonHeight);
	public static int scrollbarWidth = 11;
	public static int scrollTickSize = 120; // distance one "click" of mouse moves
	
	public int actualWidth;
	
	public ViewScrollable(Guide guide, int width, int height, GuiBookOfRevealing gui) {
		super(guide, width-scrollbarWidth, height, gui);
		this.actualWidth = width;
	}
	
	@Override
	public void init() {	
		this.buttonList.add(new GuiButtonCustomTexture(100,
				this.width-1, 1,
				11, 0,
				11, buttonHeight, scrollTexture));
		this.buttonList.add(new GuiButtonCustomTexture(101,
				this.width-1, 1+scrollbarHeight+buttonHeight,
				11, buttonHeight,
				11, buttonHeight, scrollTexture));
	}
	
	@Override
	public void draw(int mX, int mY) {
		mc.renderEngine.bindTexture(scrollTexture);
		drawTexturedModalRect(this.width-1, scrollbarTop, 0, 0, 11, scrollbarHeight);
		super.drawButtons(mX, mY);
		double frac = scrollBarFrac();
		drawTexturedModalRect(this.width, scrollbarTop + (int)(frac*(scrollbarHeight-5) ), 11, 26, 9, 5);
		
	}
	
	@Override
	public boolean onClick(int mX, int mY, int button) {
		return false;
	}
	
	int scrollAmount = 0;
	int contentHeight = 1;
	
	public int totalHeight() { return contentHeight; }
	public int currentScroll() { return scrollAmount; }
	public int scrollIncrement() { return mc.fontRenderer.FONT_HEIGHT; }
	public int getScrollPx() {
		return currentScroll();
//		int overflow = (totalHeight() - this.height);
//		int px = -(int)(currentScroll());
//		if(px > overflow && overflow > 0) {
//			return -overflow;
//		} else {
//			return -px;
//		}
	}
	public double scrollBarFrac() {
		int overflow = ( totalHeight() - this.height );
		int done = -this.getScrollPx();
		return (double)done / (double)overflow;
	}
	
	public void scrollBy(int scroll) {
		this.scrollAmount += scroll;
		int overflow = -(totalHeight()-this.height);
		if(this.scrollAmount < overflow) {
			this.scrollAmount = overflow;
		}
		if(this.scrollAmount > 0) {
			this.scrollAmount = 0;
		}
	}
	public void scrollTo(int scroll) {
		scrollBy( -scroll - currentScroll() );
	}
	
	public boolean scrollActionPerformed(GuiButton guibutton) {
		switch(guibutton.id) {
		case 100:
			scrollBy(scrollIncrement());
			return true;
		case 101:
			scrollBy(-scrollIncrement());
			return true;
		}
		return false;
	}
	
	public void scroll(int mX, int mY, int amt) {
		scrollBy(amt/120 * scrollIncrement());
	}
	
}
