package com.thecodewarrior.guides.api;

import net.minecraft.block.Block;
import net.minecraft.world.World;

public abstract class IBlockMatcher {
	public abstract boolean matches(Block block);
	
	public boolean matches(World w, int x, int y, int z) {
		return this.matches(w.getBlock(x, y, z));
	}
}
