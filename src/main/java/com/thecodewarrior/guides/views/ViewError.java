package com.thecodewarrior.guides.views;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import com.thecodewarrior.guides.gui.GuiBookOfRevealing;

public abstract class ViewError extends View {
	
	public ViewError(int width, int height, GuiBookOfRevealing gui) {
		super(null, width, height, gui);
	}

	public String getTitleText() { return ""; }
	public String getDescText() { return ""; }
	public List<String> getDescLines() {
		ArrayList<String> arr = new ArrayList<String>();
		arr.add(getDescText());
		return arr;
	}
	
	public static final int descMargin = 5;
	
	public int getTitleColor() { return 0xCC1F1F; }
	
	public int getDescColor() { return 0x404040; }
	
	@Override
	public void draw(int mX, int mY) {
		String titleText = getTitleText();
		int titleWidth = mc.fontRenderer.getStringWidth(titleText)*2;
		int titleX = ( this.width - titleWidth ) / 2;
		int titleY = 30;
		int titleBottom = titleY + 2 + (mc.fontRenderer.FONT_HEIGHT*2);
		
		List<String> descLines = getDescLines();
		
		GL11.glPushMatrix();
			GL11.glScalef(2, 2, 0);
			mc.fontRenderer.drawString(titleText, titleX/2, titleY/2, getTitleColor());
		GL11.glPopMatrix();
		
		int descLineHeight = mc.fontRenderer.FONT_HEIGHT + 1;
		int descY = titleBottom + (descLineHeight*2);
		
		for(String line: descLines) {
			int descWidth = mc.fontRenderer.getStringWidth(line);
			int descX = ( this.width - descWidth ) / 2;
			mc.fontRenderer.drawString(line, descX, descY, getDescColor());
			descY += descLineHeight;
		}
	}

	@Override
	public boolean onClick(int mX, int mY, int button) { return false; }

}
