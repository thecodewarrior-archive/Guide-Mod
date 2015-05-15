package com.thecodewarrior.guides.views;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.thecodewarrior.guides.GuideMod;
import com.thecodewarrior.guides.Reference;
import com.thecodewarrior.guides.api.GuideRegistry;
import com.thecodewarrior.guides.api.browse.BrowseItem;
import com.thecodewarrior.guides.api.browse.BrowseItemDirectory;
import com.thecodewarrior.guides.api.browse.BrowseItemGuide;
import com.thecodewarrior.guides.gui.Animation;
import com.thecodewarrior.guides.gui.BasicIcon;
import com.thecodewarrior.guides.gui.GuiBookOfRevealing;
import com.thecodewarrior.guides.gui.GuiUtils;

public class ViewBrowse extends ViewScrollable {

	ArrayList<String> path;
	ArrayList<BrowseItem> items;
	
	ArrayList<BrowseItem> fullSearchResults;
	ArrayList<BrowseItem> pathSearchResults;
	ArrayList<String> searchPath;
	String searchTerm;
	
	ResourceLocation texture = new ResourceLocation(Reference.MODID, "textures/gui/view/view_browse.png");
	
	public ViewBrowse(int width, int height, GuiBookOfRevealing gui) {
		this(width, height, gui, new ArrayList<String>());
	}
	
	public ViewBrowse(int width, int height, GuiBookOfRevealing gui, ArrayList<String> path) {
		super(null, width, height, gui);
		this.path = path;
		BrowseItem pathItem = GuideMod.browseManager.getForPath(path);
		if(pathItem instanceof BrowseItemDirectory) {
			items = ((BrowseItemDirectory) pathItem).getItems();
		}
	}
	
	public ViewBrowse(int width, int height, GuiBookOfRevealing gui, ArrayList<String> path, String search) {
		super(null, width, height, gui);
		searchTerm = search;
		this.searchPath = path;
		updateSearchResults(search);
	}

	@Override
	public void init() {
		super.init();
		rowHeight = mc.fontRenderer.FONT_HEIGHT;
		listTop = mc.fontRenderer.FONT_HEIGHT + 2 + 3;
		bgOpen = new Animation<Integer>(6, -1);
		bgClose = new Animation<Integer>(6, -1);
		entryBG = new BasicIcon(0,0,1,1, 256);
	}

	int rowHeight;
	int buffer = 2;
	int listTop;
	int left = 5;
	Animation<Integer> bgOpen;
	Animation<Integer> bgClose;
	GuiUtils u = new GuiUtils(this.zLevel);
	BasicIcon entryBG;
	
	@Override
	public void draw(int mX, int mY) {
		super.draw(mX, mY);
		int hover = hoverIndex(mX, mY);
		drawBreadcrumb(mX, mY);
		if(hover != bgOpen.param && bgOpen.param != -1) { // hover exit
				bgClose.param = bgOpen.param; // move over the id
				bgClose.setProgress(bgOpen.getLength() - bgOpen.getProgress());
				// set the progress to however much is left on the open animation
				// (the close animation is reversed, it's the empty space on the right of the box)
		} if(hover != bgOpen.param && hover != -1) { // hover enter
			if(bgClose.param == hover) { // entering into one that's closing
				bgOpen.param = bgClose.param; // move over the id
				bgOpen.setProgress(bgClose.getLength() - bgClose.getProgress());
			} else {
				bgOpen.reset();
			}
		}
		bgOpen.param = hover;

		bgOpen.frame();
		bgClose.frame();
		
		GL11.glClipPlane(GL11.GL_CLIP_PLANE2, GuiUtils.clipEqMinY(listTop-1));
		GL11.glEnable(GL11.GL_CLIP_PLANE2);
		GL11.glPushMatrix();
			GL11.glTranslated(left, listTop+getScrollPx(), 0);
			for(int i = 0; i < items.size(); i++) {
				int boxPx = 0;
				if(bgOpen.param == i) {
					boxPx = (int)( (width-left) * bgOpen.fracDone() );
				} else if(bgClose.param == i) {
					boxPx = (int)( (width-left) * bgClose.fracLeft() );
				}
				
				if(boxPx != 0) {
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.25F);
					mc.renderEngine.bindTexture(texture);
					u.drawIconWH(0, -1, entryBG, boxPx, mc.fontRenderer.FONT_HEIGHT);
					GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				}
				items.get(i).draw(false/*hover == i*/);
				GL11.glTranslated(0, rowHeight + buffer, 0);
			}
		GL11.glPopMatrix();
		GL11.glDisable(GL11.GL_CLIP_PLANE2);
	}
	
	public void drawBreadcrumb(int mX, int mY) {
		//int hover = breadcrumbHoverIndex(mX, mY);
		int curX = left-2;
		int y = 2;
		int color = 0x00;
		int hoverColor = 0xFF0000;
		int hover = breadcrumbHoverIndex(mX, mY);
		int joinLength = mc.fontRenderer.getStringWidth(" > ");
		for(int i = 0; i < path.size(); i++) {
			mc.fontRenderer.drawString(path.get(i), curX, y, hover == i ? hoverColor : color);
			curX += mc.fontRenderer.getStringWidth(path.get(i));
			if(i != path.size()-1) {
				mc.fontRenderer.drawString(" > ", curX, y, color);
			}
			curX += joinLength;
		}
		//mc.fontRenderer.drawString(String.join(" > ", path), left-2, 2, 0x00);
	}

	@Override
	public boolean onClick(int mX, int mY, int button) {
		int i = hoverIndex(mX, mY);
		if(i != -1) {
			BrowseItem item = items.get(i);
			if(item instanceof BrowseItemDirectory) {
				ArrayList<String> list = new ArrayList<String>(path);
				list.add(item.getText());
				bgClose.param = -1;
				bgOpen.param = -1;
				this.gui.goToView(new ViewBrowse(this.actualWidth, this.height, this.gui, list));
				return true;
			}
			if(item instanceof BrowseItemGuide) {
				bgClose.param = -1;
				bgOpen.param = -1;
				this.gui.refreshGuide(GuideRegistry.newBasicGuide( ((BrowseItemGuide)item).guideId() ));
				return true;
			}
		}
		i = breadcrumbHoverIndex(mX, mY);
		if(i != -1 && i != path.size()-1) {
			ArrayList<String> list = new ArrayList<String>( path.subList(0, i+1) );
			bgClose.param = -1;
			bgOpen.param = -1;
			this.gui.goToView(new ViewBrowse(this.actualWidth, this.height, this.gui, list));
			return true;
		}
		return false;
	}
	
	public int breadcrumbHoverIndex(int mX, int mY) {
		int curX = left-2;
		int top = 2;
		int bottom = top+mc.fontRenderer.FONT_HEIGHT;
		int joinLength = mc.fontRenderer.getStringWidth(" > ");
		for(int i = 0; i < path.size(); i++) {
			int len = mc.fontRenderer.getStringWidth(path.get(i));
			if(
				mX > curX &&
				mX < curX+len &&
				mY > top &&
				mY < bottom) {
				return i;
			}
			curX += len + joinLength;
		}
		return -1;
	}
	
	public int hoverIndex(int mX, int mY) {
		if(!(
			mX > left &&
			mX < width-left &&
			mY > listTop &&
			mY < height
			)) {
			return -1;
		}
		mY += -getScrollPx();
		for(int i = 0; i < items.size(); i++) {
			if(mX > left &&
			   mX < width-left &&
			   mY > ( listTop + ( i*(rowHeight+buffer) ) ) &&
			   mY < ( listTop + ( i*(rowHeight+buffer) ) + rowHeight ) ) {
				return i;
			}
		}
		return -1;
	}
	
	public int totalHeight() {
		return listTop + (items.size()*(rowHeight+buffer)) + rowHeight;
	}
	
	public void actionPerformed(GuiButton guibutton) {
		if(scrollActionPerformed(guibutton))
			return;
	}
	
	public void updateSearch(String search, int occurance) {
		if(searchTerm == null) {
			bgClose.param = -1;
			bgOpen.param = -1;
			this.gui.goToView(new ViewBrowse(this.actualWidth, this.height, this.gui, path, search));
		} else if(searchTerm.equals(search)) {
			updateSelectedResults(occurance);
		} else {
			updateSearchResults(search);
		}
	}
	
	public void updateSearchResults(String search) {
		fullSearchResults = GuideMod.browseManager.getForSearch(search);
		BrowseItem pathItem = GuideMod.browseManager.getForPath(searchPath);
		if(pathItem instanceof BrowseItemDirectory) {
			pathSearchResults = ((BrowseItemDirectory) pathItem).getMatching(search);
		}
		updateSelectedResults(0);
	}
	
	public void updateSelectedResults(int occurance) {
		boolean searchingAll = (occurance % 2) == 0;
		if(searchingAll) {
			items = fullSearchResults;
			path = new ArrayList<String>();
			path.add("All");
		} else {
			if(pathSearchResults == null) {
				items = new ArrayList<BrowseItem>();
				path = searchPath;
			} else {
				items = pathSearchResults;
				path = searchPath;
			}
		}
	}

}
