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
	public boolean customColor = false;
	public ResourceLocation texture;
	
	public GuiButtonCustomTexture(int id, int xPos, int yPos, int u, int v, int width,
			int height,  ResourceLocation loc) {
		super(id, xPos, yPos, width, height, "_");
		textureU = u;
		textureV = v;
		texture = loc;
		// TODO Auto-generated constructor stub
	}
	
     /**
      * Draws this button to the screen.
      */
     public void drawButton(Minecraft mc, int mouseX, int mouseY)
     {
         if (this.visible)
         {
        	 boolean flag = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
             if(!customColor)
            	 GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
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
