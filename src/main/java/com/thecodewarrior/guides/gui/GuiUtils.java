package com.thecodewarrior.guides.gui;

import java.nio.DoubleBuffer;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

import org.lwjgl.BufferUtils;

public class GuiUtils {

	protected float zLevel;
	
	public GuiUtils(float zLevel) {
		this.zLevel = zLevel;
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
