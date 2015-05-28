package com.thecodewarrior.guides.gui;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreenBook;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiUtils;

public class GuiButtonCustomTexture extends GuiButtonExt {

	public int textureU;
	public int textureV;
	public double r = 1;
	public double g = 1;
	public double b = 1;
	public double a = 1;
	public ResourceLocation texture;
	
	public GuiButtonCustomTexture(int id, int xPos, int yPos, int u, int v, int width,
			int height,  ResourceLocation loc) {
		super(id, xPos, yPos, width, height, "_");
		textureU = u;
		textureV = v;
		texture = loc;
		// TODO Auto-generated constructor stub
	}
	
	public void setColor(double r, double g, double b, double a) {
		this.r = r;
		this.g = g;
		this.b = b;
		this.a = a;
	}
	
     /**
      * Draws this button to the screen.
      */
     public void drawButton(Minecraft mc, int mouseX, int mouseY)
     {
         if (this.visible)
         {
        	 boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
        	 GL11.glColor4d(r, g, b, a);
            	 
             mc.getTextureManager().bindTexture(texture);
             int u = textureU;
             int v = textureV;

             if (flag)
             {
                 u += width;
             }

             this.drawTexturedModalRect(this.xPosition, this.yPosition, u, v, width, height);
         }
    }

}
