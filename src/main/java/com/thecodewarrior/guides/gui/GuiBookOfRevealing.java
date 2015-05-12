package com.thecodewarrior.guides.gui;

import java.nio.DoubleBuffer;
import java.util.Stack;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.thecodewarrior.guides.GuideMod;
import com.thecodewarrior.guides.Reference;
import com.thecodewarrior.guides.api.GuideGenerator;
import com.thecodewarrior.guides.api.GuideRegistry;
import com.thecodewarrior.guides.views.View;
import com.thecodewarrior.guides.views.ViewBrowse;
import com.thecodewarrior.guides.views.ViewGuide;
import com.thecodewarrior.guides.views.ViewScrollable;
import com.thecodewarrior.guides.views.ViewSettings;

import cpw.mods.fml.client.config.GuiButtonExt;

public class GuiBookOfRevealing extends GuiScreen {
	public static final Logger l = GuideMod.logChild("GUI");
	
	public static final int GUI_ID = 100;

	public static final ResourceLocation texture = new ResourceLocation(Reference.MODID, "textures/gui/book_of_revealing_gui.png");
	//public GuiContainerBookOfRevealing container;
	
	public static final String seperator = "\u0380";// some random unused code point with size=0 in glyph_sizes.bin
	
	public static final int guiWidth  = 252;
	public static final int guiHeight = 214;
	
	public static final int viewWidth = 250;
	public static final int viewHeight = 190;
	
	public static final int viewTopOffset  = 12;
	public static final int viewLeftOffset = 2;
	
	public Stack<View> viewHistory = new Stack<View>();

	public int top;
	public int left;
	public int viewTop;
	public int viewLeft;
	
	private boolean needsRefresh;
	
	private int mouseX;

	private int mouseY;

	private View view;
	
	private GuiButtonExt reloadButton;

	private GuiButtonTransparent backButton;
	private GuiButtonTransparent detailsButton;
	private GuiButtonTransparent browseButton;
	private GuiButtonTransparent settingsButton;
	
	private GuiButtonTransparent newBookmarkButton;
	private GuiButtonCustomTexture bookmarkScrollDownButton;
	private GuiButtonCustomTexture bookmarkScrollUpButton;
	private GuiButtonCustomTexture helpButton;
	
	private GuiTextField searchBar;
	
	private GuideGenerator guideGen;

	private GuiUtils gu;
	
	private int viewIndex;
	
	private ViewSettings settingsView;
	
	public boolean	isClickingOnBookmark;
	public int		bookmarkClickStartX;
	public int		bookmarkClickStartY;
	public boolean	isDraggingBookmark;
	public int		bookmarkIndex;
	public int		dragStartThreshold = 5;
	public int		bookmarkDragAmount;
	public double	bookmarkDragVelocity;
	
	public GuiBookOfRevealing(EntityPlayer player, World w, int x, int y, int z) {
		super();
		this.init();
		this.refreshGuide(w,x,y,z);
		this.refreshView();
	}
	
	public GuiBookOfRevealing(EntityPlayer player, ItemStack stack) {
		super();
		this.init();
		this.refreshGuide(stack);
	}

	public GuiBookOfRevealing(EntityPlayer player) {
		super();
		this.init();
		this.refreshView();
	}
	
	protected void init() {
		this.refreshGuide(GuideRegistry.NULL_GUIDE);
	}
	
	public void refreshTopLeft() {
		left = (width - guiWidth)  / 2;
		top  = (height - guiHeight) / 2;
		viewTop  = top  + viewTopOffset;
		viewLeft = left + viewLeftOffset;
	}
	
	public void back() {
		this.view = this.viewHistory.pop();
		if(this.viewHistory.size() == 0) {
			this.backButton.enabled = false;
		}
	}
	
	public void refreshView() {
		if(this.needsRefresh) {
			this.goToView(this.guideGen.generate(viewWidth, viewHeight, this));
			this.needsRefresh = false;
		}
	}
	
	public void handleMouseInput() {
		super.handleMouseInput();
		
		int wheel = Mouse.getDWheel();
		
		if(this.view != null) {
			this.view.scroll(mouseX, mouseY, wheel);
		}
	}
	
	protected void mouseClicked(int x, int y, int button)
    {
		super.mouseClicked(x,y,button);
		refreshTopLeft();
		
		l.info("mouse clicked");
		
		if(this.view == null) { return; }
		this.view.onClick(x-(viewLeft), y-(viewTop), button);
		
		/* minecraft button code */
		if (button == 0)
        {
            for (int l = 0; l < this.view.buttonList.size(); ++l)
            {
                GuiButton guibutton = (GuiButton)this.view.buttonList.get(l);

                if (guibutton.mousePressed(this.mc, x-viewLeft, y-viewTop))
                {
                    this.view.selectedButton = guibutton;
                    guibutton.func_146113_a(this.mc.getSoundHandler());
                    this.view.actionPerformed(guibutton);
                }
            }
        }
		
		if(x < this.searchBar.xPosition + mc.fontRenderer.getStringWidth(this.searchBar.getText()) + 6) {
			this.searchBar.mouseClicked(x, y, button);
		} else {
			this.searchBar.setFocused(false);
		}
		
		int hoverIndex = this.bookmarkHoverIndex(x, y);
		
		if(hoverIndex != -1) {
			this.isClickingOnBookmark = true;
			this.bookmarkIndex = hoverIndex;
			this.bookmarkClickStartX = x;
			this.bookmarkClickStartY = y;
		}
	}

	protected void mouseClickMove(int x, int y, int lastButton, long timeSinceClick) {
		if (this.isClickingOnBookmark) {
			boolean i = Math.abs(x - this.bookmarkClickStartX) > dragStartThreshold;
			if(this.isDraggingBookmark) {
				this.bookmarkDragAmount = x - this.bookmarkClickStartX;
				this.bookmarkDragVelocity = bookmarkDragAmount / (double)timeSinceClick;
			} else if(i && !this.isDraggingBookmark) {
				this.isDraggingBookmark = true;
			} 
		}
	}
	
	protected void mouseMovedOrUp(int x, int y, int button) {
		super.mouseMovedOrUp(x, y, button);
		refreshTopLeft();
		
		if(this.view == null) { return; }
		
		if (this.view.selectedButton != null && button == 0)
        {
            this.view.selectedButton.mouseReleased(x-viewLeft, y-viewTop);
            this.view.selectedButton = null;
        }
		
		if(button != -1 && this.isClickingOnBookmark) {
			if(this.isDraggingBookmark) {
				this.isDraggingBookmark = false;
				if(this.bookmarkDragAmount > (0.75 * left) || this.bookmarkDragVelocity > 0.2) {
					this.startDelete(this.bookmarkIndex);;
				}
				l.info(bookmarkDragVelocity); 
				this.bookmarkDragAmount = 0;
			} else {
				this.goToBookmark(this.bookmarkIndex);
			}
			this.isClickingOnBookmark = false;
		}
		
	}
	
	public void refreshGuide() {
		this.needsRefresh = true;
	}
	
	private void refreshGuide(World w, int x, int y, int z) {
		GuideGenerator guide = GuideRegistry.findBlockGuide(w, x, y, z);
		GuideGenerator otherGuide = GuideRegistry.findGuideFor(w, x, y, z);
		refreshGuide(otherGuide);
	}
	
	public void refreshGuide(ItemStack stack) {
		GuideGenerator guide = null;
		if(stack == null) {
			refreshGuide(GuideRegistry.NULL_GUIDE);
			return;
		}
		guide = GuideRegistry.findGuideFor(stack);
		refreshGuide(guide);
	}
	
	public void refreshGuide(GuideGenerator gen) {
		this.guideGen = gen;
		refreshGuide();
	}
	
	
	
	public void goToView(View v) {
		if(this.view != null) {
			this.viewHistory.add(this.view);
			this.backButton.enabled = true;
		}
		this.view = v;
	}
	
	public void initGui() {
		super.initGui();
		refreshTopLeft();
		
		this.settingsView = new ViewSettings(guiWidth, guiHeight, this);
		
		this.gu = new GuiUtils(this.zLevel);
		
		this.backButton = new GuiButtonTransparent(1,
				left-backRibbon.getIconWidth(), top+10,
				backRibbon.getIconWidth(), backRibbon.getIconHeight());
		this.backButton.enabled = false;

		this.detailsButton = new GuiButtonTransparent(3,
				left-detailsRibbon.getIconWidth(), top+24,
				detailsRibbon.getIconWidth(), detailsRibbon.getIconHeight());
		this.browseButton = new GuiButtonTransparent(4,
				left-browseRibbon.getIconWidth(), top+36,
				browseRibbon.getIconWidth(), browseRibbon.getIconHeight());
		this.settingsButton = new GuiButtonTransparent(5,
				left-settingsRibbon.getIconWidth(), top+48,
				settingsRibbon.getIconWidth(), settingsRibbon.getIconHeight());
		
		this.newBookmarkButton = new GuiButtonTransparent(6,
				left+guiWidth, top+10,
				addBookmark.getIconWidth(), addBookmark.getIconHeight());
		this.bookmarkScrollUpButton = new GuiButtonCustomTexture(7,
				left+guiWidth+2, top+ribbonHeight+( (ribbonHeight+1)*bookmarkRowCount ) + 24,
				133, guiHeight+20, 9, 9, texture);
		this.bookmarkScrollDownButton = new GuiButtonCustomTexture(8,
				left+guiWidth+12, top+ribbonHeight+( (ribbonHeight+1)*bookmarkRowCount ) + 24,
				151, guiHeight+20, 9, 9, texture);
		
		this.helpButton = new GuiButtonCustomTexture(9,
				left+guiWidth-5, top+1,
				169, guiHeight+20, 5, 8, texture);
		
		this.buttonList.add(this.detailsButton);
		this.buttonList.add(this.browseButton);
		this.buttonList.add(this.settingsButton);
		this.buttonList.add(this.backButton);
		this.buttonList.add(this.newBookmarkButton);
		this.buttonList.add(this.bookmarkScrollUpButton);
		this.buttonList.add(this.bookmarkScrollDownButton);
		this.buttonList.add(this.helpButton);
		
		this.searchBar = new GuiTextField(mc.fontRenderer, left+9, top+194, guiWidth-7, 9);
		this.searchBar.setCanLoseFocus(true);
		this.searchBar.setEnableBackgroundDrawing(false);
		this.searchBar.setFocused(false);
		
		this.deletingBookmark = new Animation<Integer>(3, -1);
		
		
		//this.reloadButton = new GuiButtonExt(1, left+215, top+144, 40, 20, "Reload");
		//this.buttonList.add(this.reloadButton);
	}
	
	public int searchOccurance = 0;
	
	protected void keyTyped(char par1, int par2)
    {
        super.keyTyped(par1, par2);
        String oldstr = this.searchBar.getText();
        this.searchBar.textboxKeyTyped(par1, par2);
        if(this.searchBar.isFocused()) {
        	if(par2 == Keyboard.KEY_RETURN) {
        		this.view.updateSearch(this.searchBar.getText(), this.searchOccurance++);
        	} else {
        		this.searchOccurance = 0;
        	}
        }
        this.view.keyTyped(par1, par2);
    }
	
	public void updateScreen()
    {
        super.updateScreen();
        this.searchBar.updateCursorCounter();
    }

	
	protected void actionPerformed(GuiButton guibutton) {
        //id is the id you give your button
        switch(guibutton.id) {
        case 1: // back
        	this.back();
        	break;
        case 2: // reload
        	GuideRegistry.wipeGuideRegistry();
        	GuideMod.proxy.loadGuidePacks();
        	break;
        case 4: // browse
        	if(!( this.view instanceof ViewBrowse ) ) {
        		this.goToView(new ViewBrowse(viewWidth, viewHeight, this));
        	}
        	break;
        case 5: // settings
        	if(this.view != this.settingsView) {
        		this.goToView(this.settingsView);
        	}
        	break;
        case 6: // add bookmark
        	if(this.view instanceof ViewGuide) {
        		ViewGuide v = (ViewGuide)this.view;
        		GuideMod.bookmarkManager.addBookmark(v.guideName());
        	}
        	break;
        case 7: // bookmark scroll (bookmarks move down)
        	if(this.bookmarkScrollAmount > 0) {
        		this.bookmarkScrollAmount--;
        	}
        	break;
        case 8: // bookmark scroll (bookmarks move up)
        	int c = GuideMod.bookmarkManager.getBookmarkCount();
        	if(this.bookmarkScrollAmount+this.bookmarkRowCount < c) {
        		this.bookmarkScrollAmount++;
        	}
        	break;
    	default:
    		l.info("unknown action: " + guibutton.id);
        }
	}
	
//	double[] planeEquation( float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3)
//	{
//		double[] eq = new double[4];
//		eq[0] = (y1*(z2 - z3)) + (y2*(z3 - z1)) + (y3*(z1 - z2));
//		eq[1] = (z1*(x2 - x3)) + (z2*(x3 - x1)) + (z3*(x1 - x2));
//		eq[2] = (x1*(y2 - y3)) + (x2*(y3 - y1)) + (x3*(y1 - y2));
//		eq[3] = -((x1*((y2*z3) - (y3*z2))) + (x2*((y3*z1) - (y1*z3))) + (x3*((y1*z2) - (y2*z1))));
//		return eq;
//	}
	
	
//	@Override
//	public void drawBackground(int p_146278_1_) {
//		
//		
//	}
	
//********************************DRAWING CODE**********************************
	
	
	
	protected void drawButtons(int mX, int mY) {
//		int k;
//		
//		for (k = 0; k < this.buttonList.size(); ++k)
//        {
//            if(this.buttonList.get(k) instanceof GuiButtonTransparent) {
//            	((GuiButtonTransparent)this.buttonList.get(k)).updateHover(mX, mY);
//            }
//        }
		
		int k;

        for (k = 0; k < this.buttonList.size(); ++k)
        {
            ((GuiButton)this.buttonList.get(k)).drawButton(this.mc, mX, mY);
        }

        for (k = 0; k < this.labelList.size(); ++k)
        {
            ((GuiLabel)this.labelList.get(k)).func_146159_a(this.mc, mX, mY);
        }
	}
	
	static final int rollHeight = 10;
	static final int ribbonHeight = 11;
	
	static final BasicIconFactory f = new BasicIconFactory(256, null);
	
	static final BasicIcon	page				= f.create(0, 0, guiWidth, guiHeight);

	static final BasicIcon	rollTop				= f.create(0, guiHeight, 256, rollHeight);
	static final BasicIcon	rollBottom			= f.create(0, guiHeight + rollHeight, 256, rollHeight);

	static final int guiRollBottom = guiHeight + (rollHeight*2);
	
	static final BasicIcon	backRibbon			= f.create(227, guiRollBottom, 29, ribbonHeight);
	static final BasicIcon	backRibbonDisabled	= f.create(227, guiRollBottom + rollHeight + 1, 29, ribbonHeight);
	
	static final BasicIcon	detailsRibbon		= f.create(0,   guiRollBottom, 26, ribbonHeight);
	static final BasicIcon	browseRibbon		= f.create(26,  guiRollBottom, 26, ribbonHeight);
	static final BasicIcon	settingsRibbon		= f.create(52,  guiRollBottom, 26, ribbonHeight);

	static final BasicIcon	addBookmark			= f.create(113, guiRollBottom, 20, ribbonHeight);
	static final BasicIcon	bookmarkFadeTop		= f.create(78, guiRollBottom, 35, 4);
	static final BasicIcon	bookmarkFadeBottom	= f.create(78, guiRollBottom+7, 35, 4);
	
	static final BasicIcon  bookmarkMiddle		= f.create(16, guiRollBottom+ribbonHeight, 8, ribbonHeight);
	static       BasicIcon  bookmarkMiddleFrac	= f.create(16, guiRollBottom+ribbonHeight, 8, ribbonHeight);
	static final BasicIcon	bookmarkEnd			= f.create(24, guiRollBottom+ribbonHeight, 6, ribbonHeight);
	static final BasicIcon	bookmarkTear		= f.create(0,  guiRollBottom+ribbonHeight, 16, ribbonHeight);
	
	static final BasicIcon	bookmarkScroll		= f.create(134, guiRollBottom+9, 29, 13);

	static final BasicIcon	searchLeft			= f.create(198, guiHeight+28, 9, 14);
	static final BasicIcon	searchMiddle		= f.create(207, guiHeight+31, 8, 11);
	static       BasicIcon	searchMiddleFrac	= f.create(207, guiHeight+31, 8, 11);
	static final BasicIcon	searchRight			= f.create(215, guiHeight+31, 6, 11);
	
	public float lastTime     = 0F;
	public float partialTicks = 0F;
	public float timeDelta    = 0F;
	
	public void drawScreen(int mX, int mY, float partialTicks)
	{
		
		super.drawScreen(mX, mY, partialTicks);
		
		refreshTopLeft();

		this.mouseX = mX;
		this.mouseY = mY;
		
		if(this.needsRefresh) {
			this.refreshView();
		}
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(texture);
		
		drawLeftSideButtons();
		drawBookmarks();
		
		gu.drawIcon(left, top, page); /* main page */
		gu.drawIcon(left-2, top, rollTop); /* top wrap */
		gu.drawIcon(left-2, top+guiHeight-rollHeight, rollBottom); /* bottom wrap */
		
		
		if(this.view != null) {
			// Draw the background

			int topClip = viewTop;
			int bottomClip = viewTop+viewHeight;
			
			double[] topMask = new double[4];
			topMask[1] = 1; // it's masking outside the +Y axis 
			topMask[3] = -topClip;
			DoubleBuffer buf = BufferUtils.createDoubleBuffer(4);
			buf.put(topMask);
			buf.flip();
			GL11.glClipPlane(GL11.GL_CLIP_PLANE0, buf);
			GL11.glEnable(GL11.GL_CLIP_PLANE0);

			double[] bottomMask = new double[4];
			bottomMask[1] = -1; // it's masking outside the -Y axis
			bottomMask[3] = bottomClip; // 
			buf = BufferUtils.createDoubleBuffer(4);
			buf.put(bottomMask);
			buf.flip();
			GL11.glClipPlane(GL11.GL_CLIP_PLANE1, buf);
			GL11.glEnable(GL11.GL_CLIP_PLANE1);
			
			
			GL11.glTranslated(viewLeft, viewTop, 0);
			GL11.glPushMatrix();
				this.view.draw(mouseX-viewLeft, mouseY-viewTop);
			GL11.glPopMatrix();
			GL11.glTranslated(-viewLeft, -viewTop, 0);

			// disable the clip to draw anything else.
			GL11.glDisable(GL11.GL_CLIP_PLANE0);
			GL11.glDisable(GL11.GL_CLIP_PLANE1);
			
		}
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		// re-bind the texture, as the view will likely have bound a different one
		mc.renderEngine.bindTexture(texture);
		this.drawButtons(mX, mY);

		drawSearchBar();
		
	}

	private void drawLeftSideButtons() {
		
		if(backButton.enabled) {
			int backHoverOffset = 1;
			if(backButton.hovering()) {
				backHoverOffset = 0;
			}
			gu.drawIcon(left-backRibbon.getIconWidth()+backHoverOffset, top+10, backRibbon);
		} else {
			gu.drawIcon(left-backRibbonDisabled.getIconWidth()+1, top+10, backRibbonDisabled);
		}
		int detailsHoverOffset = 1;
		if(detailsButton.hovering()) {
			detailsHoverOffset = 0;
		}
		gu.drawIcon(left-detailsRibbon.getIconWidth()+detailsHoverOffset, top+24, detailsRibbon);
		
		int browseHoverOffset = 1;
		if(browseButton.hovering()) {
			browseHoverOffset = 0;
		}
		gu.drawIcon(left-browseRibbon.getIconWidth()+browseHoverOffset, top+36, browseRibbon);
		
		int settingsHoverOffset = 1;
		if(settingsButton.hovering() || this.view == this.settingsView) {
			settingsHoverOffset = 0;
		}
		gu.drawIcon(left-settingsRibbon.getIconWidth()+settingsHoverOffset, top+48, settingsRibbon);
		
	}
	
	private void drawSearchBar() {
		gu.drawIcon(left-2, top+189, searchLeft);
		
		int textWidth = 6+ mc.fontRenderer.getStringWidth(searchBar.getText()) + (this.searchBar.isFocused() ? mc.fontRenderer.getStringWidth("_") : 0);
		
		double numOfTiles = (double)textWidth/(double)searchMiddle.getIconWidth();
		int numOfWholeTiles = (int) Math.floor(numOfTiles);
		int fracTileWidth = (int)( searchMiddle.getIconWidth() * ( numOfTiles - numOfWholeTiles ) );
		
		int curX = 7;
		
		if(searchMiddleFrac.getIconWidth() != fracTileWidth) {
			searchMiddleFrac = f.create(searchMiddle.getMinPxU(), searchMiddle.getMinPxV(), fracTileWidth, searchMiddle.getIconHeight());
		}
		
		gu.drawIcon(left+curX, top+192, searchMiddleFrac);
		curX += fracTileWidth;
		
		for(int i = 0; i < numOfWholeTiles; i++) {
			gu.drawIcon(left+curX, top+192, searchMiddle);
			curX+=searchMiddle.getIconWidth();
		}
		
		gu.drawIcon(left+curX, top+192, searchRight);
		
		if(!searchBar.isFocused()) {
			
			mc.fontRenderer.drawStringWithShadow("_", this.searchBar.xPosition+textWidth-6, this.searchBar.yPosition, 14737632);
			
			//mc.fontRenderer.drawStringWithShadow("_", left+2+textWidth, top+192, 14737632);
		}
		
		searchBar.drawTextBox();
		
	}
	
	private int bookmarkScrollAmount=0;
	private int bookmarkRowCount = 13;
	private int bookmarkY;
	
	private void drawBookmarks() {
		
		bookmarkY = top+10 + ribbonHeight + 2 + 4/*fading scroll indicator*/ + 1;
		
		if(newBookmarkButton.hovering()) {
			gu.drawIcon(left+guiWidth, top+10, addBookmark);
		} else {
			gu.drawIcon(left+guiWidth-1, top+10, addBookmark);
		}
		
		if(bookmarkScrollAmount > 0) {
			gu.drawIcon(left+guiWidth, top+10+ribbonHeight+1, bookmarkFadeTop);
		}
		
		int max = GuideMod.bookmarkManager.getBookmarkCount()-bookmarkScrollAmount;
		if(bookmarkRowCount < max) {
			max = bookmarkRowCount;
			gu.drawIcon(left+guiWidth, bookmarkY+ ( (ribbonHeight+1)*bookmarkRowCount ), bookmarkFadeBottom);
		}
		
		gu.drawIcon(left+guiWidth, bookmarkY+ ( (ribbonHeight+1)*bookmarkRowCount ) +5, bookmarkScroll);
		
		int curY = bookmarkY;
		
		int hoverIndex = -1;
		
		if(!this.isClickingOnBookmark) {
			hoverIndex = bookmarkHoverIndex(this.mouseX, this.mouseY);
		}
		
		int deleteQueueIndex = -1;
		
		for(int i = bookmarkScrollAmount; i < bookmarkScrollAmount + max; i++) {
			if(this.deletingBookmark.param == i) {
				curY += (ribbonHeight+1)* this.deletingBookmark.amtLeft();
				this.deletingBookmark.frame();
				if(this.deletingBookmark.isDone()) {
					this.deletingBookmark.param = -1;
					deleteQueueIndex = i;
				}
			} else {
				int offset = 0;
				if(bookmarkIndex == i && bookmarkDragAmount != 0) {
					offset = bookmarkDragAmount;
				} else if (i == hoverIndex) {
					offset = 1;
				}
				drawBookmark(i, curY, offset);
				mc.renderEngine.bindTexture(texture);
				GL11.glColor4f(1,1,1,1);
				curY += ribbonHeight + 1;
			}
		}
		if(deleteQueueIndex != -1) {
			GuideMod.bookmarkManager.deleteBookmark(deleteQueueIndex);
		}
		
	}

	private int bookmarkHoverIndex(int mX, int mY) {
		int max = Math.min(GuideMod.bookmarkManager.getBookmarkCount()-bookmarkScrollAmount, bookmarkRowCount);
		
		int curY = bookmarkY ;
		
		for(int i = bookmarkScrollAmount; i < bookmarkScrollAmount + max; i++) {
			String text = GuideMod.bookmarkManager.getBookmarkName(i);
			int rightEdge = left+guiWidth;
			int textWidth = 3+mc.fontRenderer.getStringWidth(text);
			int drag = (this.bookmarkIndex == i) ? bookmarkDragAmount : 0;
			if(mX > rightEdge + drag &&
			   mX < rightEdge + textWidth + bookmarkEnd.getIconWidth() + drag &&
			   mY > curY &&
			   mY < curY + ribbonHeight) {
				return i;
			}
			if(this.deletingBookmark.param == i) {
				curY += (ribbonHeight+1)* this.deletingBookmark.amtLeft();
			} else {
				curY += ribbonHeight + 1;
			}
		}
		
		return -1;
	}
	
	private void drawBookmark(int num, int y, int xOffset) {
		String text = GuideMod.bookmarkManager.getBookmarkName(num);
		int rightEdge = left+guiWidth + xOffset;
		
		int textWidth = 3+ mc.fontRenderer.getStringWidth(text);
		
		double numOfTiles = (double)textWidth/(double)bookmarkMiddle.getIconWidth();
		int numOfWholeTiles = (int) Math.floor(numOfTiles);
		int fracTileWidth = (int)( bookmarkMiddle.getIconWidth() * ( numOfTiles - numOfWholeTiles ) );
		
		int curX = rightEdge;
		
		if(bookmarkMiddleFrac.getIconWidth() != fracTileWidth) {
			bookmarkMiddleFrac = f.create(bookmarkMiddle.getMinPxU(), bookmarkMiddle.getMinPxV(), fracTileWidth, bookmarkMiddle.getIconHeight());
		}
		
		gu.drawIcon(curX, y, bookmarkMiddleFrac);
		curX += fracTileWidth;
		
		for(int i = 0; i < numOfWholeTiles; i++) {
			gu.drawIcon(curX, y, bookmarkMiddle);
			curX+=bookmarkMiddle.getIconWidth();
		}
		
		gu.drawIcon(curX, y, bookmarkEnd);
		gu.drawIcon(rightEdge-bookmarkTear.getIconWidth(), y, bookmarkTear);
		
		mc.fontRenderer.drawString(text, rightEdge+1, y+2, 14737632);
	}
	
	private void goToBookmark(int i) {
		this.refreshGuide(GuideMod.bookmarkManager.getBookmarkGuide(i));
	}
	
	Animation<Integer> deletingBookmark;
	
	private void startDelete(int i) {
		this.deletingBookmark.param = i;
		this.deletingBookmark.reset();
		
	}
	
}
