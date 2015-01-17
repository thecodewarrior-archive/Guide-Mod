package com.thecodewarrior.guides.gui;

import net.minecraft.inventory.IInventory;

import com.thecodewarrior.notmine.buildcraft.core.gui.slots.SlotPhantom;

public class SlotSinglePhantom extends SlotPhantom {

	public SlotSinglePhantom(IInventory iinventory, int slotIndex, int posX,
			int posY) {
		super(iinventory, slotIndex, posX, posY);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean canAdjust() {
		return true;
	}

}
