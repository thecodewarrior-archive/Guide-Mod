package com.thecodewarrior.guides.views;

import net.minecraft.util.ResourceLocation;

import com.thecodewarrior.guides.Reference;
import com.thecodewarrior.guides.gui.GuiBookOfRevealing;
import com.thecodewarrior.guides.guides.Guide;

public class ViewNull extends View {
	public static final ResourceLocation tex = new ResourceLocation(Reference.MODID, "textures/gui/view/view_null.png");


	public ViewNull(int width, int height, GuiBookOfRevealing gui) {
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
	}

	@Override
	public boolean onClick(int mX, int mY, int button) {
		// TODO Auto-generated method stub
		return false;
	}

}
