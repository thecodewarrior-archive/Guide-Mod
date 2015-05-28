package com.thecodewarrior.guides.gui;

import java.nio.DoubleBuffer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class GuiUtils extends cpw.mods.fml.client.config.GuiUtils {

	protected float zLevel;
	
	public GuiUtils(float zLevel) {
		this.zLevel = zLevel;
	}
	
	//{{ Ints
	public void drawIconScaleWH(int x, int y, IIcon i, double scaleW, double scaleH) {
		drawIconWH(x, y, i, (int)(i.getIconWidth()*scaleW), (int)(i.getIconHeight()*scaleH));
	}
	
	public void drawIconScaleW(int x, int y, IIcon i, double scaleW) {
		drawIconScaleWH(x, y, i, scaleW, 1.0);
	}
	
	public void drawIconScaleH(int x, int y, IIcon i, double scaleH) {
		drawIconScaleWH(x, y, i, 1.0, scaleH);
	}
	
	public void drawIconScale(int x, int y, IIcon i, double scale) {
		drawIconScaleWH(x, y, i, scale, scale);
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
	
	public void drawTexturedModelRectFromIcon(int p_94065_1_, int p_94065_2_, IIcon p_94065_3_, int p_94065_4_, int p_94065_5_)
    {
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(p_94065_1_ + 0), (double)(p_94065_2_ + p_94065_5_), (double)this.zLevel, (double)p_94065_3_.getMinU(), (double)p_94065_3_.getMaxV());
        tessellator.addVertexWithUV((double)(p_94065_1_ + p_94065_4_), (double)(p_94065_2_ + p_94065_5_), (double)this.zLevel, (double)p_94065_3_.getMaxU(), (double)p_94065_3_.getMaxV());
        tessellator.addVertexWithUV((double)(p_94065_1_ + p_94065_4_), (double)(p_94065_2_ + 0), (double)this.zLevel, (double)p_94065_3_.getMaxU(), (double)p_94065_3_.getMinV());
        tessellator.addVertexWithUV((double)(p_94065_1_ + 0), (double)(p_94065_2_ + 0), (double)this.zLevel, (double)p_94065_3_.getMinU(), (double)p_94065_3_.getMinV());
        tessellator.draw();
    }
	
	//}}
	
	//{{ Doubles
	public void drawIconScaleWH(double x, double y, IIcon i, double scaleW, double scaleH) {
		drawIconWH(x, y, i, (double)(i.getIconWidth()*scaleW), (double)(i.getIconHeight()*scaleH));
	}
	
	public void drawIconScaleW(double x, double y, IIcon i, double scaleW) {
		drawIconScaleWH(x, y, i, scaleW, 1.0);
	}
	
	public void drawIconScaleH(double x, double y, IIcon i, double scaleH) {
		drawIconScaleWH(x, y, i, 1.0, scaleH);
	}
	
	public void drawIconScale(double x, double y, IIcon i, double scale) {
		drawIconScaleWH(x, y, i, scale, scale);
	}
	
	public void drawIcon(double x, double y, IIcon i)
    {
		drawIconWH(x, y, i, i.getIconWidth(), i.getIconHeight());
    }
	
	public void drawIconW(double x, double y, IIcon i, double w)
    {
		drawIconWH(x, y, i, w, i.getIconHeight());
    }
	public void drawIconH(double x, double y, IIcon i, double h)
    {
		drawIconWH(x, y, i, i.getIconWidth(), h);
    }
	
	// just for ease of use
	public void drawIconWH(double x, double y, IIcon i, double w, double h)
    {
		drawTexturedModelRectFromIcon(x, y, i, w, h);
    }
	
	public void drawTexturedModelRectFromIcon(double p_94065_1_, double p_94065_2_, IIcon p_94065_3_, double p_94065_4_, double p_94065_5_)
    {
		Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV((double)(p_94065_1_ + 0), (double)(p_94065_2_ + p_94065_5_), (double)this.zLevel, (double)p_94065_3_.getMinU(), (double)p_94065_3_.getMaxV());
        tessellator.addVertexWithUV((double)(p_94065_1_ + p_94065_4_), (double)(p_94065_2_ + p_94065_5_), (double)this.zLevel, (double)p_94065_3_.getMaxU(), (double)p_94065_3_.getMaxV());
        tessellator.addVertexWithUV((double)(p_94065_1_ + p_94065_4_), (double)(p_94065_2_ + 0), (double)this.zLevel, (double)p_94065_3_.getMaxU(), (double)p_94065_3_.getMinV());
        tessellator.addVertexWithUV((double)(p_94065_1_ + 0), (double)(p_94065_2_ + 0), (double)this.zLevel, (double)p_94065_3_.getMinU(), (double)p_94065_3_.getMinV());
        tessellator.draw();
    }
	//}}
	
	//{{ textured box with color
	/**
     * Draws a textured box of any size (smallest size is borderSize * 2 square) based on a fixed size textured box with continuous borders
     * and filler. It is assumed that the desired texture ResourceLocation object has been bound using
     * Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation).
     * 
     * @param x x axis offset
     * @param y y axis offset
     * @param u bound resource location image x offset
     * @param v bound resource location image y offset
     * @param width the desired box width
     * @param height the desired box height
     * @param textureWidth the width of the box texture in the resource location image
     * @param textureHeight the height of the box texture in the resource location image
     * @param borderSize the size of the box's borders
     * @param zLevel the zLevel to draw at
     */
    public static void drawContinuousTexturedBoxWithColor(int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight,
            int borderSize, float zLevel)
    {
        drawContinuousTexturedBoxWithColor(x, y, u, v, width, height, textureWidth, textureHeight, borderSize, borderSize, borderSize, borderSize, zLevel);
    }
    
    /**
     * Draws a textured box of any size (smallest size is borderSize * 2 square) based on a fixed size textured box with continuous borders
     * and filler. The provided ResourceLocation object will be bound using
     * Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation).
     * 
     * @param res the ResourceLocation object that contains the desired image
     * @param x x axis offset
     * @param y y axis offset
     * @param u bound resource location image x offset
     * @param v bound resource location image y offset
     * @param width the desired box width
     * @param height the desired box height
     * @param textureWidth the width of the box texture in the resource location image
     * @param textureHeight the height of the box texture in the resource location image
     * @param borderSize the size of the box's borders
     * @param zLevel the zLevel to draw at
     */
    public static void drawContinuousTexturedBoxWithColor(ResourceLocation res, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight,
            int borderSize, float zLevel)
    {
        drawContinuousTexturedBoxWithColor(res, x, y, u, v, width, height, textureWidth, textureHeight, borderSize, borderSize, borderSize, borderSize, zLevel);
    }
    
    /**
     * Draws a textured box of any size (smallest size is borderSize * 2 square) based on a fixed size textured box with continuous borders
     * and filler. The provided ResourceLocation object will be bound using
     * Minecraft.getMinecraft().getTextureManager().bindTexture(resourceLocation).
     * 
     * @param res the ResourceLocation object that contains the desired image
     * @param x x axis offset
     * @param y y axis offset
     * @param u bound resource location image x offset
     * @param v bound resource location image y offset
     * @param width the desired box width
     * @param height the desired box height
     * @param textureWidth the width of the box texture in the resource location image
     * @param textureHeight the height of the box texture in the resource location image
     * @param topBorder the size of the box's top border
     * @param bottomBorder the size of the box's bottom border
     * @param leftBorder the size of the box's left border
     * @param rightBorder the size of the box's right border
     * @param zLevel the zLevel to draw at
     */
    public static void drawContinuousTexturedBoxWithColor(ResourceLocation res, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight,
            int topBorder, int bottomBorder, int leftBorder, int rightBorder, float zLevel)
    {
        Minecraft.getMinecraft().getTextureManager().bindTexture(res);
        drawContinuousTexturedBoxWithColor(x, y, u, v, width, height, textureWidth, textureHeight, topBorder, bottomBorder, leftBorder, rightBorder, zLevel);
    }
	
	public static void drawContinuousTexturedBoxWithColor(int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight,
            int topBorder, int bottomBorder, int leftBorder, int rightBorder, float zLevel)
    {
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        
        int fillerWidth = textureWidth - leftBorder - rightBorder;
        int fillerHeight = textureHeight - topBorder - bottomBorder;
        int canvasWidth = width - leftBorder - rightBorder;
        int canvasHeight = height - topBorder - bottomBorder;
        int xPasses = canvasWidth / fillerWidth;
        int remainderWidth = canvasWidth % fillerWidth;
        int yPasses = canvasHeight / fillerHeight;
        int remainderHeight = canvasHeight % fillerHeight;
        
        // Draw Border
        // Top Left
        drawTexturedModalRect(x, y, u, v, leftBorder, topBorder, zLevel);
        // Top Right
        drawTexturedModalRect(x + leftBorder + canvasWidth, y, u + leftBorder + fillerWidth, v, rightBorder, topBorder, zLevel);
        // Bottom Left
        drawTexturedModalRect(x, y + topBorder + canvasHeight, u, v + topBorder + fillerHeight, leftBorder, bottomBorder, zLevel);
        // Bottom Right
        drawTexturedModalRect(x + leftBorder + canvasWidth, y + topBorder + canvasHeight, u + leftBorder + fillerWidth, v + topBorder + fillerHeight, rightBorder, bottomBorder, zLevel);
        
        for (int i = 0; i < xPasses + (remainderWidth > 0 ? 1 : 0); i++)
        {
            // Top Border
            drawTexturedModalRect(x + leftBorder + (i * fillerWidth), y, u + leftBorder, v, (i == xPasses ? remainderWidth : fillerWidth), topBorder, zLevel);
            // Bottom Border
            drawTexturedModalRect(x + leftBorder + (i * fillerWidth), y + topBorder + canvasHeight, u + leftBorder, v + topBorder + fillerHeight, (i == xPasses ? remainderWidth : fillerWidth), bottomBorder, zLevel);
            
            // Throw in some filler for good measure
            for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++)
                drawTexturedModalRect(x + leftBorder + (i * fillerWidth), y + topBorder + (j * fillerHeight), u + leftBorder, v + topBorder, (i == xPasses ? remainderWidth : fillerWidth), (j == yPasses ? remainderHeight : fillerHeight), zLevel);
        }
        
        // Side Borders
        for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++)
        {
            // Left Border
            drawTexturedModalRect(x, y + topBorder + (j * fillerHeight), u, v + topBorder, leftBorder, (j == yPasses ? remainderHeight : fillerHeight), zLevel);
            // Right Border
            drawTexturedModalRect(x + leftBorder + canvasWidth, y + topBorder + (j * fillerHeight), u + leftBorder + fillerWidth, v + topBorder, rightBorder, (j == yPasses ? remainderHeight : fillerHeight), zLevel);
        }
    }
	//}}
	
	
	public static DoubleBuffer clipEqMinY(int val) {
		double[] topMask = new double[4];
		topMask[1] = 1; // it's masking everything not in the +Y direction
		topMask[3] = -val;
		DoubleBuffer buf = BufferUtils.createDoubleBuffer(4);
		buf.put(topMask);
		buf.flip();
		return buf;
	}
	public static DoubleBuffer clipEqMaxY(int val) {
		double[] bottomMask = new double[4];
		bottomMask[1] = -1; // it's masking everything not in the -Y direction
		bottomMask[3] = val; // 
		DoubleBuffer buf = BufferUtils.createDoubleBuffer(4);
		buf.put(bottomMask);
		buf.flip();
		return buf;
	}
	
	public static DoubleBuffer clipEqMinX(int val) {
		double[] bottomMask = new double[4];
		bottomMask[0] = 1; // it's masking everything not in the +X direction
		bottomMask[3] = -val;
		DoubleBuffer buf = BufferUtils.createDoubleBuffer(4);
		buf.put(bottomMask);
		buf.flip();
		return buf;
	}
	public static DoubleBuffer clipEqMaxX(int val) {
		double[] bottomMask = new double[4];
		bottomMask[0] = -1; // it's masking everything not in the -X direction
		bottomMask[3] = val;
		DoubleBuffer buf = BufferUtils.createDoubleBuffer(4);
		buf.put(bottomMask);
		buf.flip();
		return buf;
	}

}
