package com.thecodewarrior.guides.gui;

import net.minecraft.client.Minecraft;

import com.thecodewarrior.guides.GuideMod;

import cpw.mods.fml.client.config.GuiButtonExt;

public class GuiButtonTransparent extends GuiButtonExt {

	public GuiButtonTransparent(int id, int xPos, int yPos, int width, int height) {
		super(id, xPos, yPos, width, height, "_");
		// TODO Auto-generated constructor stub
	}
	
	public void drawButton(Minecraft mc, int mouseX, int mouseY) {
//		if (this.visible)
//        {
//            this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
//        }
		updateHover(mouseX, mouseY);
	}
	
	public void updateHover(int mouseX, int mouseY) {
		if (this.visible)
        {
            this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
        } else {
        	this.field_146123_n = false;
        }
	}
	
	public boolean hovering() {
		return this.field_146123_n;
	}

}
