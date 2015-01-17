package com.thecodewarrior.guides.gui;

import net.minecraft.client.Minecraft;
import cpw.mods.fml.client.config.GuiButtonExt;
import cpw.mods.fml.client.config.GuiUtils;

public class GuiButtonLink extends GuiButtonExt {

	public GuiButtonLink(int id, int xPos, int yPos, String displayString) {
		super(id, xPos, yPos, displayString);
		// TODO Auto-generated constructor stub
	}
	
	@Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            this.field_146123_n = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
            int k = this.getHoverState(this.field_146123_n);
            //GuiUtils.drawContinuousTexturedBox(buttonTextures, this.xPosition, this.yPosition, 0, 46 + k * 20, this.width, this.height, 200, 20, 2, 3, 2, 2, this.zLevel);
            this.mouseDragged(mc, mouseX, mouseY);
            int color = 14737632;
            
//            if (packedFGColour != 0)
//            {
//                color = packedFGColour;
//            }
//            else 
//            if (!this.enabled)
//            {
                color = 0x0000FF;
//            }
//            else 
            	if (this.field_146123_n)
            {
                color = 0x800080;
            }
            
            String buttonText = this.displayString;
            int strWidth = mc.fontRenderer.getStringWidth(buttonText);
            int ellipsisWidth = mc.fontRenderer.getStringWidth("...");
            
            if (strWidth > width - 6 && strWidth > ellipsisWidth)
                buttonText = mc.fontRenderer.trimStringToWidth(buttonText, width - 6 - ellipsisWidth).trim() + "...";

//            this.drawRect(this.xPosition, this.yPosition+this.height+2, this.xPosition+this.width, this.yPosition+this.height+5, color);
            this.drawCenteredString(mc.fontRenderer, buttonText, this.xPosition + this.width / 2, this.yPosition + (this.height - 8) / 2, color);

        }
    }

}
