package com.thecodewarrior.guides.gui;

import java.nio.DoubleBuffer;
import java.util.Stack;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.thecodewarrior.guides.GuideMod;
import com.thecodewarrior.guides.Reference;
import com.thecodewarrior.guides.api.GuideGenerator;
import com.thecodewarrior.guides.api.GuideRegistry;
import com.thecodewarrior.guides.views.View;
import com.thecodewarrior.guides.views.ViewSettings;

import cpw.mods.fml.client.config.GuiButtonExt;

public class GuiBookOfRevealing extends GuiScreen {
	public static final Logger l = GuideMod.logChild("GUI");
	
	public static final int GUI_ID = 100;

	public static final ResourceLocation texture = new ResourceLocation(Reference.MODID, "textures/gui/book_of_revealing_gui.png");
	//public GuiContainerBookOfRevealing container;
	
	public static final String seperator = "\u0380";// some random unused code point with size=0 in glyph_sizes.bin
	
	public static final int guiWidth  = 254;
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
	
	private GuiButtonExt backButton;
	private GuiButtonExt reloadButton;
	
	private GuiButtonTransparent detailsButton;
	private GuiButtonTransparent browseButton;
	private GuiButtonTransparent settingsButton;
	
	private GuiTextField searchBar;
	
	private GuideGenerator guideGen;

	private int viewIndex;
	
	private ViewSettings settingsView;
	
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
		this.settingsView = new ViewSettings(guiWidth, guiHeight, this);
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
			//this.backButton.enabled = false;
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
			this.view.scroll(wheel);
		}
	}
	
	protected void mouseClicked(int x, int y, int button)
    {
		super.mouseClicked(x,y,button);
		refreshTopLeft();
		
		if(this.view == null) { return; }
		this.view.onClick(x-(left+5), y-(top+5), button);
		
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
			//this.backButton.enabled = true;
		}
		this.view = v;
	}
	
	public void initGui() {
		super.initGui();
		refreshTopLeft();
		
		this.detailsButton = new GuiButtonTransparent(3,
				left-detailsRibbon.getIconWidth(), top+10,
				detailsRibbon.getIconWidth(), detailsRibbon.getIconHeight());
		this.browseButton = new GuiButtonTransparent(4,
				left-browseRibbon.getIconWidth(), top+22,
				browseRibbon.getIconWidth(), browseRibbon.getIconHeight());
		this.settingsButton = new GuiButtonTransparent(5,
				left-settingsRibbon.getIconWidth(), top+34,
				settingsRibbon.getIconWidth(), settingsRibbon.getIconHeight());
		
		this.buttonList.add(this.detailsButton);
		this.buttonList.add(this.browseButton);
		this.buttonList.add(this.settingsButton);
		
		this.searchBar = new GuiTextField(mc.fontRenderer, left+8, 207, guiWidth-7, 9);
		this.searchBar.setCanLoseFocus(true);
		this.searchBar.setEnableBackgroundDrawing(false);
		this.searchBar.setFocused(false);
		
		//this.backButton = new GuiButtonExt(1, left+215, top+124, 40, 20, "Back");
		//this.backButton.enabled = false;
		//this.buttonList.add(this.backButton);
		
		//this.reloadButton = new GuiButtonExt(1, left+215, top+144, 40, 20, "Reload");
		//this.buttonList.add(this.reloadButton);
	}
	
	protected void keyTyped(char par1, int par2)
    {
        super.keyTyped(par1, par2);
        this.searchBar.textboxKeyTyped(par1, par2);
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
        case 1:
        	this.back();
        case 2:
        	GuideRegistry.wipeGuideRegistry();
        	GuideMod.proxy.loadGuidePacks();
        case 5:
        	this.goToView(this.settingsView);
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
	
	public void drawIcon(int x, int y, IIcon i)
    {
		drawIconWH(x, y, i, i.getIconWidth(), i.getIconHeight());
    }
	
	public void drawIconW(int x, int y, IIcon i, int w)
    {
		drawIconWH(x, y, i, w, i.getIconHeight());
    }
	public void drawIconH(int x, int y, IIcon i, int h)
    {
		drawIconWH(x, y, i, i.getIconWidth(), h);
    }
	
	// just for ease of use
	public void drawIconWH(int x, int y, IIcon i, int w, int h)
    {
		drawTexturedModelRectFromIcon(x, y, i, w, h);
    }

	static final int rollHeight = 10;
	static final int ribbonHeight = 11;
	
	static final BasicIconFactory f = new BasicIconFactory(256, null);
	
	static final BasicIcon	page				= f.create(0, 0, guiWidth, guiHeight);

	static final BasicIcon	rollTop				= f.create(0, guiHeight, guiWidth + 2, rollHeight);
	static final BasicIcon	rollBottom			= f.create(0, guiHeight + rollHeight, guiWidth + 2, rollHeight);

	static final BasicIcon	detailsRibbon		= f.create(0, guiHeight + (rollHeight*2), 26, ribbonHeight);
	static final BasicIcon	browseRibbon		= f.create(26, guiHeight + (rollHeight*2), 26, ribbonHeight);
	static final BasicIcon	settingsRibbon		= f.create(52, guiHeight + (rollHeight*2), 26, ribbonHeight);

	static final BasicIcon	addBookmark			= f.create(0, 0, guiWidth, guiHeight);
	static final BasicIcon	bookmarkFadeTop		= f.create(0, 0, guiWidth, guiHeight);
	static final BasicIcon	bookmarkFadeBottom	= f.create(0, 0, guiWidth, guiHeight);
	static final BasicIcon	bookmark			= f.create(0, 0, guiWidth, guiHeight);
	static final BasicIcon	bookmarkScroll		= f.create(0, 0, guiWidth, guiHeight);

	static final BasicIcon	searchLeft			= f.create(205, guiHeight+28, 9, 14);
	static final BasicIcon	searchMiddle		= f.create(214, guiHeight+31, 1, 11);
	static final BasicIcon	searchRight			= f.create(215, guiHeight+31, 6, 11);
	
	public void drawScreen(int mX, int mY, float par3)
	{
		super.drawScreen(mX, mY, par3);
		this.drawButtons(mX, mY);
		
		refreshTopLeft();

		this.mouseX = mX;
		this.mouseY = mY;
		
		if(this.needsRefresh) {
			this.refreshView();
		}
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(texture);
		
		drawLeftSideButtons();

		drawIcon(left, top, page); /* main page */
		drawIcon(left, top, rollTop); /* top wrap */
		drawIcon(left-2, top+guiHeight-rollHeight, rollBottom); /* bottom wrap */
		
		
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
			this.view.draw(mouseX-viewLeft, mouseY-viewTop);
			GL11.glTranslated(-viewLeft, -viewTop, 0);

			// disable the clip to draw anything else.
			GL11.glDisable(GL11.GL_CLIP_PLANE0);
			GL11.glDisable(GL11.GL_CLIP_PLANE1);
			
		}
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		// re-bind the texture, as the view will likely have bound a different one
		mc.renderEngine.bindTexture(texture);

		drawSearchBar();
		
	}

	private void drawLeftSideButtons() {
		int detailsHoverOffset = 1;
		if(detailsButton.hovering()) {
			detailsHoverOffset = 0;
		}
		drawIcon(left-detailsRibbon.getIconWidth()+detailsHoverOffset, top+10, detailsRibbon);
		
		int browseHoverOffset = 1;
		if(browseButton.hovering()) {
			browseHoverOffset = 0;
		}
		drawIcon(left-browseRibbon.getIconWidth()+browseHoverOffset, top+22, browseRibbon);
		
		int settingsHoverOffset = 1;
		if(settingsButton.hovering()) {
			settingsHoverOffset = 0;
		}
		drawIcon(left-settingsRibbon.getIconWidth()+settingsHoverOffset, top+34, settingsRibbon);
		
	}
	
	private void drawSearchBar() {
		drawIcon(left-2, top+189, searchLeft);
		
		int textWidth = 6+ mc.fontRenderer.getStringWidth(searchBar.getText());
		
		drawIconW(left+7, top+192, searchMiddle, textWidth);
		
		drawIcon(left+7+textWidth, top+192, searchRight);
		
		if(!searchBar.isFocused()) {
			
			mc.fontRenderer.drawStringWithShadow("_", this.searchBar.xPosition+textWidth-6, this.searchBar.yPosition, 14737632);
			
			//mc.fontRenderer.drawStringWithShadow("_", left+2+textWidth, top+192, 14737632);
		}
		
		searchBar.drawTextBox();
		
	}
	
}
