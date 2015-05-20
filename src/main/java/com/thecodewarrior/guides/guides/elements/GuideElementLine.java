package com.thecodewarrior.guides.guides.elements;

import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Color;
import org.lwjgl.util.ReadableColor;

public class GuideElementLine extends GuideElement {

	protected int x1;
	protected int y1;
	protected int x2;
	protected int y2;
	protected int color;
	protected float thickness;
	public GuideElementLine(int x1, int y1, int x2, int y2, float thickness, int color) {
		super(x1, y1, x2-x1);
		this.newX = x;
		this.newY = y;
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.thickness = thickness;
		this.color = color;
		// TODO Auto-generated constructor stub
	}
	
	public void draw(int mX, int mY) {
		GL11.glPushMatrix();
		{
			// This is all MC code from the FontRenderer
			if ((this.color & -67108864) == 0)
	        {
	            this.color |= -16777216;
	        }
	
			float red = (float)(this.color >> 16 & 255) / 255.0F;
	        float blue = (float)(this.color >> 8 & 255) / 255.0F;
	        float green = (float)(this.color & 255) / 255.0F;
	        float alpha = (float)(this.color >> 24 & 255) / 255.0F;
	        GL11.glColor4f(red, blue, green, alpha);
	        
	        // here is my code
		    GL11.glLineWidth(this.thickness);
		    GL11.glDepthMask(false);
	
		    Tessellator tessellator = Tessellator.instance;
		    tessellator.startDrawing(GL11.GL_LINES);
		    tessellator.addVertex(x1, y1, 0);
		    tessellator.addVertex(x2, y2, 0);
		    tessellator.draw();
		}
	    GL11.glPopMatrix();
	}
	
}
