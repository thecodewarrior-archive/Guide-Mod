package com.thecodewarrior.guides.views;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;

import com.thecodewarrior.guides.GuideMod;
import com.thecodewarrior.guides.gui.GuiBookOfRevealing;
import com.thecodewarrior.guides.guides.Guide;

public abstract class View extends Gui {
	
	/**
	 * The guide that this view is displaying, should never change during the lifetime of the view
	 */
	protected Guide guide;
	
	public int width;
	public int height;
	
	public Minecraft mc = GuideMod.proxy.getMC();
	public GuiBookOfRevealing gui;

	public GuiButton selectedButton;

	public List<GuiButton> buttonList;
	
	public View(Guide guide, int width, int height, GuiBookOfRevealing gui) {
		this.guide  = guide;
		this.width  = width;
		this.height = height;
		this.gui = gui;
		if(this.guide != null) {
			this.guide.gui = gui;
		}
		this.buttonList = new ArrayList<GuiButton>();
		this.init();
	}
	
	/**
	 * initialize the view
	 */
	public abstract void init();
	
	/**
	 * Draw the buttons in the {@link #buttonList}
	 * @param mX mouse X position relative to view left
	 * @param mY mouse Y position relative to view top
	 */
	public void drawButtons(int mX, int mY) {
		int k;

        for (k = 0; k < this.buttonList.size(); ++k)
        {
            ((GuiButton)this.buttonList.get(k)).drawButton(this.mc, mX, mY);
        }
	}
	
	/**
	 * Called to draw the view, draw relative to top left of the view
	 * @param mX mouse X position relative to view left
	 * @param mY mouse Y position relative to view top
	 */
	public abstract void draw(int mX, int mY);
	
	/**
	 * Called when the mouse is clicked inside the view, relative to the top-left corner of the view
	 * 
	 * @param mX mouse X position
	 * @param mY mouse Y position
	 * @param button mouse button press
	 * 
	 * @return whether an action was performed and click event should stop propagating
	 */
	public abstract boolean onClick(int mX, int mY, int button);

	public void actionPerformed(GuiButton guibutton) {
		
	}
}
