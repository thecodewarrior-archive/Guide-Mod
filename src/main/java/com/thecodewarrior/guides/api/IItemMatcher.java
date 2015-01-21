package com.thecodewarrior.guides.api;

import net.minecraft.item.ItemStack;

import com.thecodewarrior.guides.guides.Guide;

public interface IItemMatcher {
	public boolean matches(ItemStack stack);
}
