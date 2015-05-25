package com.thecodewarrior.guides.gui;

public class MetaRect<T> extends Rect {

	private T meta;
	
	public MetaRect(int x, int y, int width, int height, T meta) {
		super(x, y, width, height);
		this.meta = meta;
	}
	
	public T getMeta() {
		return meta;
	}
	
	public void setMeta(T newMeta) {
		meta = newMeta;
	}

}
