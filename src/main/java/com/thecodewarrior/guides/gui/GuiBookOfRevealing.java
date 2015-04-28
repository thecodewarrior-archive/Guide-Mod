package com.thecodewarrior.guides.gui;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
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
import com.thecodewarrior.guides.api.GuideRegistry.GuideGeneratorBasic;
import com.thecodewarrior.guides.views.View;

import cpw.mods.fml.client.config.GuiButtonExt;

public class GuiBookOfRevealing extends GuiContainer {
	public static final Logger l = GuideMod.logChild("GUI");
	
	public static final int GUI_ID = 100;

	public static final ResourceLocation texture = new ResourceLocation(Reference.MODID, "textures/gui/book_of_revealing_gui.png");
	public GuiContainerBookOfRevealing container;
	
	public static final String seperator = "\u0380";// some random unused code point with size=0 in glyph_sizes.bin
	
	public List<View> viewHistory = new ArrayList<View>();

	private boolean needsRefresh;
	
	private float mouseX;

	private float mouseY;

	private View view;

	private int viewWidth;
	private int viewHeight;
	
	private GuiButtonExt backButton;
	private GuiButtonExt reloadButton;
	
	private GuideGenerator guideGen;

	private int viewIndex;
	
	public GuiBookOfRevealing(EntityPlayer player, ItemStack stack, World w, int x, int y, int z) {
		super(new GuiContainerBookOfRevealing(player));
		this.init();
		this.refreshGuide(w,x,y,z);
		this.refreshView();
		//refreshGuideReal();
	}

	public GuiBookOfRevealing(EntityPlayer player) {
		super(new GuiContainerBookOfRevealing(player));
		this.init();
		this.refreshView();
	}
	
	protected void init() {
		this.container = (GuiContainerBookOfRevealing) this.inventorySlots;
		this.container.gui = this;
		
		this.xSize = 256;
		this.ySize = 208;
		
		this.viewWidth = 246;
		this.viewHeight = 115;
		
		this.refreshGuide(GuideRegistry.NULL_GUIDE);
	}
	
	public void itemPlaced(ItemStack stack) {
		GuideGenerator guide = null;
		if(stack == null) {
			refreshGuide(GuideRegistry.NULL_GUIDE);
			return;
		}
		guide = GuideRegistry.findGuideFor(stack);
//		if(stack.getItem() instanceof ItemBlock) {
//			guide = GuideRegistry.findBlockGuide(stack);
//		} else {
//			guide = GuideRegistry.findItemGuide(stack);
//		}
		refreshGuide(guide);
	}

	
	public void back() {
		this.viewIndex--;
		this.view = this.viewHistory.get(this.viewIndex);
		for(int i=this.viewHistory.size()-1; i >= this.viewIndex; i--) {
			this.viewHistory.remove(i);
		}
		if(this.viewIndex == 0 && this.backButton != null) {
			this.backButton.enabled = false;
		}
	}
	
	public void refreshView() {
		if(this.needsRefresh) {
			if(this.view != null) {
				this.viewHistory.add(this.view);
				this.viewIndex = this.viewHistory.size();
				if(this.viewIndex >= 0 && this.backButton != null) {
					this.backButton.enabled = true;
				}
			}
			this.view = this.guideGen.generate(viewWidth, viewHeight, this);
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

		int left = (width - xSize)  / 2;
		int top  = (height - ySize) / 2;
		if(this.view == null) { return; }
		this.view.onClick(x-(left+5), y-(top+5), button);
		
		/* minecraft button code */
		if (button == 0)
        {
            for (int l = 0; l < this.view.buttonList.size(); ++l)
            {
                GuiButton guibutton = (GuiButton)this.view.buttonList.get(l);

                if (guibutton.mousePressed(this.mc, x-(left+5), y-(top+5)))
                {
                    this.view.selectedButton = guibutton;
                    guibutton.func_146113_a(this.mc.getSoundHandler());
                    this.view.actionPerformed(guibutton);
                }
            }
        }
	}

	protected void mouseMovedOrUp(int x, int y, int button) {
		super.mouseMovedOrUp(x, y, button);
		
		if(this.view == null) {
			return;
		}
		
		int left = (width - xSize)  / 2;
		int top  = (height - ySize) / 2;
		if (this.view.selectedButton != null && button == 0)
        {
            this.view.selectedButton.mouseReleased(x-(top+5), y-(top+5));
            this.view.selectedButton = null;
        }
	}
	
	public void refreshGuide() {
		this.needsRefresh = true;
		//refreshView();
	}
	
	private void refreshGuide(World w, int x, int y, int z) {
		GuideGenerator guide = GuideRegistry.findBlockGuide(w, x, y, z);
		GuideGenerator otherGuide = GuideRegistry.findGuideFor(w, x, y, z);
		refreshGuide(otherGuide);
	}
	
	public void refreshGuide(GuideGenerator gen) {
		this.guideGen = gen;
		refreshGuide();
	}
	
	public void initGui() {
		super.initGui();
		int left = ((width - xSize) / 2);
		int top = (height - ySize) / 2;
		//id, x, y, u, v, width, height, texture 224
		

		
		this.backButton = new GuiButtonExt(1, left+215, top+124, 40, 20, "Back");
		this.backButton.enabled = false;
		this.buttonList.add(this.backButton);
		
		this.reloadButton = new GuiButtonExt(1, left+215, top+144, 40, 20, "Reload");
		this.buttonList.add(this.reloadButton);
	}
	
	
	protected void actionPerformed(GuiButton guibutton) {
        //id is the id you give your button
		GuiContainerBookOfRevealing container = ((GuiContainerBookOfRevealing) this.inventorySlots);
        switch(guibutton.id) {
        case 1:
        	this.back();
        case 2:
        	GuideRegistry.wipeGuideRegistry();
        	GuideMod.proxy.loadGuidePacks();
        }
        //Packet code here
        //PacketDispatcher.sendPacketToServer(packet); //send packet
	}
	
	double[] planeEquation( float x1, float y1, float z1, float x2, float y2, float z2, float x3, float y3, float z3)
	{
		double[] eq = new double[4];
		eq[0] = (y1*(z2 - z3)) + (y2*(z3 - z1)) + (y3*(z1 - z2));
		eq[1] = (z1*(x2 - x3)) + (z2*(x3 - x1)) + (z3*(x1 - x2));
		eq[2] = (x1*(y2 - y3)) + (x2*(y3 - y1)) + (x3*(y1 - y2));
		eq[3] = -((x1*((y2*z3) - (y3*z2))) + (x2*((y3*z1) - (y1*z3))) + (x3*((y1*z2) - (y2*z1))));
		return eq;
	}
	
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float par3, int mX, int mY) {
		
		
		
		//refreshGuideReal();
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(texture);
		int left = (width - xSize)  / 2;
		int top  = (height - ySize) / 2;
//		j = j - 1 + 1;
		drawTexturedModalRect(left, top, 0, 0, xSize, ySize);
		
		if(this.view != null) {
			// Draw the background

			double[] topMask = new double[4];
			topMask[1] = 1; // it's masking outside the +Y axis 
			topMask[3] = -(top+5);
			DoubleBuffer buf = BufferUtils.createDoubleBuffer(4);
			buf.put(topMask);
			buf.flip();
			GL11.glClipPlane(GL11.GL_CLIP_PLANE0, buf);
			GL11.glEnable(GL11.GL_CLIP_PLANE0);

			double[] bottomMask = new double[4];
			bottomMask[1] = -1; // it's masking outside the -Y axis
			bottomMask[3] = top+5+this.viewHeight; // 
			buf = BufferUtils.createDoubleBuffer(4);
			buf.put(bottomMask);
			buf.flip();
			GL11.glClipPlane(GL11.GL_CLIP_PLANE1, buf);
			GL11.glEnable(GL11.GL_CLIP_PLANE1);
			
			
			GL11.glTranslated(   left+5,      top+5,   0);
			this.view.draw(mX-(left+5), mY-(top+5));
			GL11.glTranslated(-( left+5 ), -( top+5 ), 0);

			// disable the clip to draw anything else.
			GL11.glDisable(GL11.GL_CLIP_PLANE0);
			GL11.glDisable(GL11.GL_CLIP_PLANE1);
			
		}
		
	}
	
	protected void drawButtons(int mX, int mY) {
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
	
	public void drawScreen(int mX, int mY, float par3)
	{
		this.mouseX = (float)mX;
		this.mouseY = (float)mY;
		
		this.drawButtons(mX, mY);
		
		if(this.needsRefresh) {
			this.refreshView();
		}
		
		super.drawScreen(mX, mY, par3);
		
	}
	
}
