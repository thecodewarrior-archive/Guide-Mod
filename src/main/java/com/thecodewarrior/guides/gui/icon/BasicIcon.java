package com.thecodewarrior.guides.gui.icon;

import net.minecraft.util.IIcon;

public class BasicIcon implements IIcon {

	protected int w;
	protected int h;
	protected float texSize;
	protected int uPx;
	protected int vPx;
	
	public BasicIcon(int u, int v, int w, int h, int texSize) {
		this.texSize = texSize;
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
		return getMinPxU()/texSize;
	}

	@Override
	public float getMaxU() {
		return getMinU()+(w/texSize);
	}

	@Override
	public float getInterpolatedU(double arg) {
		return (float) ( (w*arg/texSize) + getMinU() );
	}

	@Override
	public float getMinV() {
		return getMinPxV()/texSize;
	}

	@Override
	public float getMaxV() {
		return getMinV()+(h/texSize);
	}

	@Override
	public float getInterpolatedV(double arg) {
		return (float)( (h*arg/texSize) + getMinV());
	}

	@Override
	public String getIconName() {
		return "";
	}

}
