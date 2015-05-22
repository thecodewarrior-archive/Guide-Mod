package com.thecodewarrior.guides.views;

import net.minecraft.util.ResourceLocation;

import com.thecodewarrior.guides.Reference;
import com.thecodewarrior.guides.gui.GuiBookOfRevealing;
import com.thecodewarrior.guides.guides.Guide;

public class ViewNull extends View {

	public ViewNull(int width, int height, GuiBookOfRevealing gui) {
		super(null, width, height, gui);
	}

	@Override
	public void init() {}

	@Override
	public void draw(int mX, int mY) {}

	@Override
	public boolean onClick(int mX, int mY, int button) {
		return false;
	}

}
