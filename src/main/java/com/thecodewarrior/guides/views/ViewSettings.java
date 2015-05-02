package com.thecodewarrior.guides.views;

import scala.swing.event.KeyTyped;

import com.thecodewarrior.guides.gui.GuiBookOfRevealing;
import com.thecodewarrior.guides.guides.Guide;

public class ViewSettings extends View {

	public ViewSettings(int width, int height, GuiBookOfRevealing gui) {
		super(null, width, height, gui);
	}

	@Override
	public void init() {
		
	}

	@Override
	public void draw(int mX, int mY) {
		mc.fontRenderer.drawString("hello", 0, 0, 0x000000, false);
	}

	@Override
	public void keyTyped(char par1, int par2) {
	}
	
	@Override
	public boolean onClick(int mX, int mY, int button) {
		
		return false;
	}

}
