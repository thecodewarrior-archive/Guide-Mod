package com.thecodewarrior.guides.views;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.thecodewarrior.guides.ConfigOptions;
import com.thecodewarrior.guides.Reference;
import com.thecodewarrior.guides.gui.GuiBookOfRevealing;
import com.thecodewarrior.guides.gui.GuiButtonTransparent;
import com.thecodewarrior.guides.gui.GuiUtils;
import com.thecodewarrior.guides.gui.icon.BasicIcon;
import com.thecodewarrior.guides.gui.icon.IconFactory;
import com.thecodewarrior.guides.guidepack.GuidePackManager;
import com.thecodewarrior.guides.guidepack.GuidePackUpdater;

import cpw.mods.fml.client.config.GuiButtonExt;

public class ViewSettings extends View {

	public static final ResourceLocation tex = new ResourceLocation(Reference.MODID, "textures/gui/view/view_settings.png");
	GuiButton shouldDownload;
	GuiButtonExt reloadPacks;
	GuiButtonExt updatePacks;
	GuiUtils gu;
	
	String lastReloadTimeText;
	
	public ViewSettings(int width, int height, GuiBookOfRevealing gui) {
		super(null, width, height, gui);
	}

	@Override
	public void init() {
		this.gu = new GuiUtils(this.zLevel);
		this.shouldDownload = new GuiButtonTransparent(1, 0, 3, 10, 10);//new GuiCheckBox(1, 0, 0, "Automatically download guide packs", GuideServerInterface.enabled);
		this.buttonList.add(this.shouldDownload);
		this.updatePacks = new GuiButtonExt(3, 0, 18, StatCollector.translateToLocal("guidemod.view.settings.updatePacksButton"));
		this.reloadPacks = new GuiButtonExt(2, 0, 38, StatCollector.translateToLocal("guidemod.view.settings.reloadPacksButton"));
		this.buttonList.add(this.reloadPacks);
		this.buttonList.add(this.updatePacks);
		this.updateLastReloadTimeText();
	}
	
	void updateLastReloadTimeText() {
		int seconds = GuidePackManager.lastLoadSeconds;
		int minutes = (int)Math.floor( ( (float)GuidePackManager.lastLoadSeconds)/60.0 );
		seconds -= minutes*60;
		if(minutes > 0) {
			lastReloadTimeText = String.format( StatCollector.translateToLocal("guidemod.view.settings.estimatedReloadTime.ms") , minutes, seconds);
		} else {
			lastReloadTimeText = String.format( StatCollector.translateToLocal("guidemod.view.settings.estimatedReloadTime.s") , seconds);
		}
	}

	@Override
	public void actionPerformed(GuiButton guibutton) {
		switch(guibutton.id) {
		case 1:
			ConfigOptions.autoDownload = !ConfigOptions.autoDownload;
			ConfigOptions.updateConfig();
			break;
		case 2:
			GuidePackManager.unloadGuidePacks();
			GuidePackManager.loadGuidePacks();
			this.updateLastReloadTimeText();
	    	break;
		case 3:
			GuidePackManager.unloadGuidePacks();
			GuidePackUpdater.manualUpdate = true;
			GuidePackUpdater.downloadPacksForMods();
			GuidePackManager.loadGuidePacks();
			break;
		}
	};
	
	static IconFactory f = new IconFactory(256, tex);
	
	static BasicIcon checkOff = f.create(0,  0, 10, 10);
	static BasicIcon checkOn  = f.create(0, 10, 10, 10);
	
	@Override
	public void draw(int mX, int mY) {
		mc.fontRenderer.drawSplitString(StatCollector.translateToLocal("guidemod.view.settings.guideServerEnable"), 12, 0, 200, 0x000000);
		mc.renderEngine.bindTexture(tex);
		GL11.glColor4f(1, 1, 1, 1);
		if(ConfigOptions.autoDownload) {
			gu.drawIcon(0, 3, checkOn);
		} else {
			gu.drawIcon(0, 3, checkOff);
		}
		
		if(ConfigOptions.dev) {
			mc.renderEngine.bindTexture(gui.texture);
			GL11.glScaled(0.5, 0.5, 0.5);
				drawTexturedModalRect(12*2, 30*2, 0, 0, 256, 256);
			GL11.glScaled(2  ,   2,   2);
		}
		
		this.reloadPacks.drawButton(mc, mX, mY);
		this.updatePacks.drawButton(mc, mX, mY);
		
		mc.fontRenderer.drawSplitString(lastReloadTimeText, 1, reloadPacks.yPosition + reloadPacks.height + 2, width-10, 0x00);
	}

	@Override
	public void keyTyped(char par1, int par2) {
	}
	
	@Override
	public boolean onClick(int mX, int mY, int button) {
		return false;
	}

}
