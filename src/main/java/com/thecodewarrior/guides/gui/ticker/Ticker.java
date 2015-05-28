package com.thecodewarrior.guides.gui.ticker;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import org.apache.logging.log4j.Logger;
import org.lwjgl.opengl.GL11;

import com.thecodewarrior.guides.ConfigOptions;
import com.thecodewarrior.guides.GuideMod;
import com.thecodewarrior.guides.Reference;
import com.thecodewarrior.guides.gui.GuiBookOfRevealing;
import com.thecodewarrior.guides.gui.GuiButtonCustomTexture;
import com.thecodewarrior.guides.gui.GuiUtils;
import com.thecodewarrior.guides.gui.TickCounter;
import com.thecodewarrior.guides.gui.icon.BasicIcon;
import com.thecodewarrior.guides.gui.icon.IconFactory;
import com.thecodewarrior.guides.gui.icon.ManuallyAnimatedIcon;

public class Ticker extends Gui {

	public static final ResourceLocation texture = new ResourceLocation(Reference.MODID, "textures/gui/ticker.png");
	
	public static final Logger l = GuideMod.logChild("Ticker");
	
	public Minecraft mc = Minecraft.getMinecraft();
	
	int color;
	String text;
	public List<GuiButton> buttonList;
	public List<TickerButton> tickerButtonList;
	GuiButton closeButton;
	int height;
	double r;
	double g;
	double b;
	double a;
	int textColor;
	int timeout = -1;
	int tickerButtonHeight = Minecraft.getMinecraft().fontRenderer.FONT_HEIGHT+2;
	TickCounter timer;
	
	public Ticker(int color, String text) {
		this.color = color;
		this.text = text;
		this.buttonList = new ArrayList<GuiButton>();
		this.tickerButtonList = new ArrayList<TickerButton>();
		this.closeButton = new GuiButtonCustomTexture(1,
				222, 1, 230, 0, 5, 5, texture);
		this.buttonList.add(this.closeButton);
		r = ((color >> 24) & 0xFF)/256.0;
		g = ((color >> 16) & 0xFF)/256.0;
		b = ((color >> 8 ) & 0xFF)/256.0;
		a = ( color        & 0xFF)/256.0;
		if(a == 0) a = 1;
		((GuiButtonCustomTexture)this.closeButton).setColor(r, g, b, a);
		textColor = (/* RGB text color */0x000000 << 8 /* move over to make space for alpha */) | ( /* alpha */ color & 0xFF );
		timer = new TickCounter();
		timer.stop();
		this.timeout = ConfigOptions.defaultTickerTimeout;
	}
	
	public void setTimeout(int ticks) {
		timeout = ticks;
	}
	
	public void addButton(TickerButton b) {
		tickerButtonList.add(b);
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
            int x = 4;
    		int y = height-tickerButtonHeight;
    		int innerWidth = 230-5;
    		int tickerButtonMargin = 2;
    		int tickerButtonWidth = (innerWidth/tickerButtonList.size())-tickerButtonMargin;
    		
    		for(TickerButton t : tickerButtonList) {
    			boolean hovering = (mX > x && mX < x+tickerButtonWidth && mY > y && mY < y+tickerButtonHeight);
        		if(hovering && GuiBookOfRevealing.currentGui != null) {
        			t.click(GuiBookOfRevealing.currentGui);
        		}
    			x += tickerButtonWidth + tickerButtonMargin;
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
	
	public void resetTimer() {
		timer.reset();
	}
	
	public boolean closed() { return closed; }
	
	static final GuiUtils u = new GuiUtils(0);
	
	static final IconFactory f = new IconFactory(256, null);
	
	static final BasicIcon	buttonLeft   = f.create(0,  100, 2,   15);
	static final BasicIcon	buttonMiddle = f.create(2,  100, 10,  15);
	static final BasicIcon	buttonRight  = f.create(12, 100, 2,   15);
	static final BasicIcon	background	 = f.create(0, 	0,   230, 50);
	static final ManuallyAnimatedIcon closeProgress = f.createManuallyAnimated(0, 50, 7, 7, true);
	
	boolean wasHoveringLastFrame = false;
	
	public void draw(int mX, int mY) {
		if(timer.stopped())
			timer.start();
		if(timer.ticks() > timeout && timeout > 0)
			close();
		
		height = 0;
		List<String> lines = mc.fontRenderer.listFormattedStringToWidth(text, 200);
		height += mc.fontRenderer.FONT_HEIGHT*lines.size();
		
		if(tickerButtonList.size() > 0) {
			height += tickerButtonHeight+2;
		}
		
		int bottomMargin = 3;
		int offset = height+bottomMargin;
		int bgX = -background.getIconHeight()+offset;
		boolean hovering = false;
		if( mX > 0 && mX < background.getIconWidth() &&
			mY > 0 && mY < offset) {
			hovering = true;
		}
		if(hovering && !wasHoveringLastFrame) { // mouse enter
			timer.reset();
			timer.stop();
		}
		if(!hovering && wasHoveringLastFrame){ // mouse exit
			timer.start();
		}
		
		timer.tick();
		GL11.glColor4d(r, g, b, a);
		mc.renderEngine.bindTexture(texture);
		u.drawIcon(0, bgX, background);
		int progressFrames = 24;
		if(timer.ticks() == 0)
			closeProgress.setFrame(0);
		else
			closeProgress.setFrame( (int)((timer.ticks()/timeout)*progressFrames ) );
		u.drawIcon(closeButton.xPosition-1, closeButton.yPosition-1, closeProgress);
		if(tickerButtonList.size() > 0) {
			drawTickerButtons(mX, mY);
		}
		GL11.glColor4d(r, g, b, a);
		
		int curX = 5;
		int curY = 2;
		for(String line : lines) {
			mc.fontRenderer.drawString(line, curX, curY, textColor);
			curY += mc.fontRenderer.FONT_HEIGHT;
		}
		
		GL11.glColor4d(r, g, b, a);
		drawButtons(mX, mY);
		GL11.glColor4f(1,1,1,1);
		
	}
	
	public void drawTickerButtons(int mX, int mY) {
		int x = 4;
		int y = height-tickerButtonHeight;
		int innerWidth = 230-5;
		int tickerButtonMargin = 2;
		int tickerButtonWidth = (innerWidth/tickerButtonList.size())-tickerButtonMargin;
		
		for(TickerButton t : tickerButtonList) {
			String s = t.getText();
			int stringWidth = mc.fontRenderer.getStringWidth(s);
			boolean hovering = (mX > x && mX < x+tickerButtonWidth && mY > y && mY < y+tickerButtonHeight);
            GuiUtils.drawContinuousTexturedBoxWithColor(texture, x, y, 0, 100+(hovering ? 15 : 0), tickerButtonWidth, tickerButtonHeight, 13, 13, 2, 2, 2, 2, this.zLevel);
            int stringLeft = (tickerButtonWidth - stringWidth) / 2;
            mc.fontRenderer.drawString(s, x+stringLeft, y+2, textColor);
    		GL11.glColor4d(r, g, b, a);
    		x += tickerButtonWidth + tickerButtonMargin;
		}
	}
	
	public void drawButtons(int mX, int mY) {
		int k;

        for (k = 0; k < this.buttonList.size(); ++k)
        {
            ((GuiButton)this.buttonList.get(k)).drawButton(this.mc, mX, mY);
        }
	}

}
