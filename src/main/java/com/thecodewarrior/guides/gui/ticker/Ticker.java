package com.thecodewarrior.guides.gui.ticker;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import com.thecodewarrior.guides.GuideMod;
import com.thecodewarrior.guides.Reference;
import com.thecodewarrior.guides.gui.GuiButtonCustomTexture;

public class Ticker extends Gui {

	public static final ResourceLocation texture = new ResourceLocation(Reference.MODID, "textures/gui/ticker.png");
	
	public static final Logger l = GuideMod.logChild("Ticker");
	
	public Minecraft mc = Minecraft.getMinecraft();
	
	int color;
	String text;
	public List<GuiButton> buttonList;
	GuiButton closeButton;
	
	public Ticker(int color, String text) {
		this.color = color;
		this.text = text;
		this.buttonList = new ArrayList<GuiButton>();
		this.closeButton = new GuiButtonCustomTexture(1,
				222, 1, 230, 0, 5, 5, texture);
		((GuiButtonCustomTexture)this.closeButton).customColor = true;
		this.buttonList.add(this.closeButton);
	}
	
	public void click(int mX, int mY, int button) {
		if (button == 0)
        {
            for (int l = 0; l < this.buttonList.size(); ++l)
            {
                GuiButton guibutton = (GuiButton)this.buttonList.get(l);

                if (guibutton.mousePressed(this.mc, mX, mY))
                {
                    this.actionPerformed(guibutton);
                    return;
                }
            }
        }
	}
	
	public void actionPerformed(GuiButton button) {
		switch(button.id) {
		case 1:
			close();
		}
	}
	
	boolean closed;
	
	public void close() {
		closed = true;
	}
	public boolean closed() { return closed; }
	
	public void draw(int mX, int mY) {
		float r = (color >> 24) & 0xFF;
		float g = (color >> 16) & 0xFF;
		float b = (color >> 8 ) & 0xFF;
		float a =  color        & 0xFF;
		
		int height = 0;
		height += mc.fontRenderer.FONT_HEIGHT;
		
		GL11.glColor4d(r/256.0, g/256.0, b/256.0, 1.0);
		mc.renderEngine.bindTexture(texture);
		drawTexturedModalRect(0, -(46-height), 0, 0, 230, 50);
		drawButtons(mX, mY);
		mc.fontRenderer.drawString(text, 5, 2, 0x0);
		GL11.glColor4f(1,1,1,1);
		
	}
	
	public void drawButtons(int mX, int mY) {
		int k;

        for (k = 0; k < this.buttonList.size(); ++k)
        {
            ((GuiButton)this.buttonList.get(k)).drawButton(this.mc, mX, mY);
        }
	}

}
