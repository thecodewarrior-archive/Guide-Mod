package com.thecodewarrior.guides.views;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.thecodewarrior.guides.GuideMod;
import com.thecodewarrior.guides.api.GuideRegistry;
import com.thecodewarrior.guides.api.browse.BrowseItem;
import com.thecodewarrior.guides.api.browse.BrowseItemDirectory;
import com.thecodewarrior.guides.api.browse.BrowseItemGuide;
import com.thecodewarrior.guides.gui.GuiBookOfRevealing;

public class ViewBrowse extends View {

	ArrayList<String> path;
	ArrayList<BrowseItem> items;
	
	public ViewBrowse(int width, int height, GuiBookOfRevealing gui) {
		this(width, height, gui, new ArrayList<String>());
	}
	
	public ViewBrowse(int width, int height, GuiBookOfRevealing gui, ArrayList<String> path) {
		super(null, width, height, gui);
		this.path = path;
		items = GuideMod.browseManager.getForPath(path);
	}

	@Override
	public void init() {
		rowHeight = mc.fontRenderer.FONT_HEIGHT;
	}

	int rowHeight;
	int buffer = 2;
	int top = 5;
	int left = 5;
	
	@Override
	public void draw(int mX, int mY) {
		int hover = hoverIndex(mX, mY);
		GL11.glPushMatrix();
			GL11.glTranslated(left, top, 0);
			for(int i = 0; i < items.size(); i++) {
				items.get(i).draw(hover == i);
				GL11.glTranslated(0, rowHeight + buffer, 0);
			}
		GL11.glPopMatrix();
	}

	@Override
	public boolean onClick(int mX, int mY, int button) {
		int i = hoverIndex(mX, mY);
		if(i != -1) {
			BrowseItem item = items.get(i);
			if(item instanceof BrowseItemDirectory) {
				ArrayList<String> list = new ArrayList<String>(path);
				list.add(item.getText());
				this.gui.goToView(new ViewBrowse(this.width, this.height, this.gui, list));
				return true;
			}
			if(item instanceof BrowseItemGuide) {
				this.gui.refreshGuide(GuideRegistry.newBasicGuide( ((BrowseItemGuide)item).guideId() ));
			}
		}
		return false;
	}
	
	public int hoverIndex(int mX, int mY) {
		for(int i = 0; i < items.size(); i++) {
			if(mX > left &&
			   mY > ( top + ( i*(rowHeight+buffer) ) ) &&
			   mY < ( top + ( i*(rowHeight+buffer) ) + rowHeight ) ) {
				return i;
			}
		}
		return -1;
	}

}
