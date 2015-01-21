package com.thecodewarrior.guides.views;

import net.minecraft.util.ResourceLocation;

import com.thecodewarrior.guides.Reference;
import com.thecodewarrior.guides.gui.GuiBookOfRevealing;
import com.thecodewarrior.guides.guides.Guide;

public class ViewMissing extends View {

	public static final ResourceLocation tex = new ResourceLocation(Reference.MODID, "textures/gui/view/view_missing.png");

	
	public ViewMissing(int width, int height, GuiBookOfRevealing gui) {
		super(null, width, height, gui);
	}

	@Override
	public void init() {

	}

	@Override
	public void draw(int mX, int mY) {
		if(mc == null)
			return;
		
		mc.renderEngine.bindTexture(tex);
		drawTexturedModalRect(0, 0, 0, 0, width, height);
		
		String missingText = "MISSING";
		int missingColor = 0xCC1F1F;
		
		int missingWidth = mc.fontRenderer.getStringWidth(missingText);
		int missingX = ( this.width - missingWidth ) / 2;
		int missingY = 30;
		
		mc.fontRenderer.drawStringWithShadow("MISSING", missingX, missingY, missingColor);
	}

	@Override
	public boolean onClick(int mX, int mY, int button) {
		
		return false;
	}

}
