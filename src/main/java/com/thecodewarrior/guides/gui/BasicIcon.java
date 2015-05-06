package com.thecodewarrior.guides.gui;

import net.minecraft.util.IIcon;

public class BasicIcon implements IIcon {

	private float u;
	private float v;
	private int w;
	private int h;
	private float texSize;
	private int uPx;
	private int vPx;
	
	public BasicIcon(int u, int v, int w, int h, int texSize) {
		this.texSize = texSize;
		this.u = u / this.texSize;
		this.v = v / this.texSize;
		this.uPx = u;
		this.vPx = v;
		this.w = w;
		this.h = h;
	}
	
	public int getMinPxU() {
		return uPx;
	}
	
	public int getMinPxV() {
		return vPx;
	}
	
	@Override
	public int getIconWidth() {
		return w;
	}

	@Override
	public int getIconHeight() {
		return h;
	}

	@Override
	public float getMinU() {
		return u;
	}

	@Override
	public float getMaxU() {
		return u+(w/texSize);
	}

	@Override
	public float getInterpolatedU(double arg) {
		return (float) ( (w*arg/texSize) + u );
	}

	@Override
	public float getMinV() {
		return v;
	}

	@Override
	public float getMaxV() {
		return v+(h/texSize);
	}

	@Override
	public float getInterpolatedV(double arg) {
		return (float)( (h*arg/texSize) + v);
	}

	@Override
	public String getIconName() {
		return "";
	}

}
