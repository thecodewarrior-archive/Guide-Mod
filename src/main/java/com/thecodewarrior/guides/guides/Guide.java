package com.thecodewarrior.guides.guides;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import com.thecodewarrior.guides.GuideMod;
import com.thecodewarrior.guides.gui.GuiBookOfRevealing;
import com.thecodewarrior.guides.guides.elements.GuideElement;

public abstract class Guide {
	
	/**
	 * Unparsed guide text, initialized in constructor
	 */
	protected String rawGuide;
	public FontRenderer fontRendererObj = GuideMod.proxy.getFontRenderer();
	public Minecraft mc = GuideMod.proxy.getMC();
	public GuiBookOfRevealing gui;
	public String guideName;
	
	public Guide(String guideText, String guideName) {
		rawGuide = guideText;
		this.guideName = guideName;
		parse();
	}
	
	/**
	 * Parse the guide text in {@link #rawGuide}
	 * @return whether the guide was parsed successfully
	 */
	public abstract boolean parse();
	
	public String viewName() {
		return "textGuide";
	}
	
	/**
	 * Create a list of all the elements with coords centered at top left
	 * @param width the width of the view
	 * @param height the height of the view
	 * @return a list of the guide elements
	 */
	public abstract List<GuideElement> getGuideElements(int width, int height);
}
