package com.thecodewarrior.guides.views;

import org.lwjgl.opengl.GL11;

import net.minecraft.util.ResourceLocation;

import com.thecodewarrior.guides.Reference;
import com.thecodewarrior.guides.gui.GuiBookOfRevealing;
import com.thecodewarrior.guides.guides.elements.GuideElementText;

public class ViewNoMatch extends View {
	public static final ResourceLocation tex = new ResourceLocation(Reference.MODID, "textures/gui/view/view_no_match.png");


	public ViewNoMatch(int width, int height, GuiBookOfRevealing gui) {
		super(null, width, height, gui);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void draw(int mX, int mY) {
		// TODO Auto-generated method stub
		if(mc == null)
			return;
		
		mc.renderEngine.bindTexture(tex);
		drawTexturedModalRect(0, 0, 0, 0, width, height);
		
		
		String titleText = "No match found";
		int titleColor = 0xCC1F1F;
		int titleWidth = mc.fontRenderer.getStringWidth(titleText)*2;
		int titleX = ( this.width - titleWidth ) / 2;
		int titleY = 30;
		
		String descText = "No guide exists for this";
		int descColor = 0x404040;
		int descWidth = mc.fontRenderer.getStringWidth(descText);
		int descX = ( this.width - descWidth ) / 2;
		int descY = titleY + (mc.fontRenderer.FONT_HEIGHT*2) + 2;
		
		GL11.glPushMatrix();
			GL11.glScalef(2, 2, 0);
			mc.fontRenderer.drawString(titleText, titleX/2, titleY/2, titleColor);
		GL11.glPopMatrix();
		mc.fontRenderer.drawString(descText, descX, descY, descColor);
	}

	@Override
	public boolean onClick(int mX, int mY, int button) {
		// TODO Auto-generated method stub
		return false;
	}
	
}
