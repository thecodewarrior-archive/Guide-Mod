package com.thecodewarrior.guides.gui;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import com.thecodewarrior.guides.GuideMod;
import com.thecodewarrior.guides.api.GuideRegistry;
import com.thecodewarrior.guides.api.IBlockGuideProvider;
import com.thecodewarrior.guides.api.IItemGuideProvider;
import com.thecodewarrior.guides.guides.GuideText;
import com.thecodewarrior.guides.views.View;
import com.thecodewarrior.guides.views.ViewGuide;
import com.thecodewarrior.guides.views.ViewMissing;
import com.thecodewarrior.guides.views.ViewNull;
import com.thecodewarrior.notmine.buildcraft.core.gui.slots.IPhantomSlot;
import com.thecodewarrior.notmine.buildcraft.core.gui.slots.SlotPhantom;
import com.thecodewarrior.notmine.codechicken.lib.inventory.InventorySimple;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class GuiContainerBookOfRevealing extends Container {

	public String guide;
	public int topLine = 0;
	public GuiBookOfRevealing gui;
	
	
	public GuiContainerBookOfRevealing(EntityPlayer player) {
		int left = 48;
		int top = 126;
		for (int i = 0; i < 3; i++)
		{
			for (int k = 0; k < 9; k++)
			{
				this.addSlotToContainer(new Slot(player.inventory, k + i * 9 + 9, left + k * 18, top + i * 18));
			}
		}

		for (int j = 0; j < 9; j++)
		{
			this.addSlotToContainer(new Slot(player.inventory, j, left + j * 18, top + 58));
		}
		InventorySimple inventory = new InventorySimple(1);
		this.addSlotToContainer(new SlotPhantom(inventory, 0, left-42, top) {
			public int getSlotStackLimit()
		    {
				return 1;
		    }
		});
		
	}
	
	@SideOnly(Side.CLIENT)
	public View getView(int width, int height) {		
		if(this.guide == "") {
			return null; //new ViewMissing(width, height, this.gui);
		} else if(this.guide == null) {
			return new ViewNull(width, height, this.gui);
		} else {
			return null; //new ViewGuide(new GuideText(this.guide), width, height, this.gui);
		}
		
	}

	private void refreshGuide(ItemStack stack) {
		if(this.gui == null) {
			return;
		}
		//this.gui.itemPlaced(stack);
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return true;
	}
	
	/**
	 * Called to transfer a stack from one inventory to the other eg. when shift
	 * clicking.
	 */
	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index){
		ItemStack stack = null;
		Slot slot = (Slot) this.inventorySlots.get(index);
		if(slot!=null&&slot.getHasStack()){
			ItemStack slotstack = slot.getStack();
			stack = slotstack.copy();
				if(index<8){
				if(!this.mergeItemStack(slotstack,8,this.inventorySlots.size(),false)) return null;
			}else if(!this.getSlot(0).isItemValid(slotstack)||!this.mergeItemStack(slotstack,0,4,false)) return null;
			if(slotstack.stackSize==0){
				slot.putStack((ItemStack) null);
			}else{
				slot.onSlotChanged();
			}
			if(slotstack.stackSize==stack.stackSize) return null;
			slot.onPickupFromSlot(player,slotstack);
		}
		return stack;
	}

	@Override
	public boolean mergeItemStack(ItemStack stack, int start, int end, boolean reverse){
		return super.mergeItemStack(stack,start,end,reverse);
	}

	/**
	 * Does the same as mergeItemStack with the same args, except does not
	 * actually merge— just returns the number of items that can be merged
	 * (usually either stack.stackSize or 0, but can be in between)
	 * @param stack
	 * @param start
	 * @param end
	 * @param reverse
	 * @return
	 */
	int dryMerge(ItemStack stack, int start, int end, boolean reverse){
		boolean flag1 = false;
		int i = start;
		if(reverse){
			i = end-1;
		}
		int quantity = stack.stackSize;
		Slot slot;
		ItemStack slotstack;
		if(stack.isStackable()){
			while(stack.stackSize>0&&(!reverse&&i<end||reverse&&i>=start)){
				slot = this.getSlot(i);
				slotstack = slot.getStack();
				if(slotstack!=null&&slotstack.getItem()==stack.getItem()&&(!stack.getHasSubtypes()||stack.getItemDamage()==slotstack.getItemDamage())&&ItemStack.areItemStackTagsEqual(stack,slotstack)){
					int l = slotstack.stackSize+stack.stackSize;
					if(l<=stack.getMaxStackSize()){
						quantity -= slotstack.stackSize;
					}else if(slotstack.stackSize<stack.getMaxStackSize()){
						quantity -= (stack.getMaxStackSize() - slotstack.stackSize);
					}
				}
				if(reverse) --i;
				else ++i;
			}
		}
		if(stack.stackSize>0){
			if(reverse){
				i = end-1;
			}else{
				i = start;
			}
			while(!reverse&&i<end||reverse&&i>=start){
				slot = (Slot) this.inventorySlots.get(i);
				slotstack = slot.getStack();
				if(slotstack==null){
					quantity = 0;
					break;
				}
				if(reverse){
					--i;
				}else{
					++i;
				}
			}
		}
		return stack.stackSize-quantity;
	}
	
	@Override
	public ItemStack slotClick(int slotNum, int mouseButton, int modifier, EntityPlayer player) {
		Slot slot = slotNum < 0 ? null : (Slot) this.inventorySlots.get(slotNum);
		if (slot instanceof IPhantomSlot) {
			return slotClickPhantom(slot, mouseButton, modifier, player);
		}
		return super.slotClick(slotNum, mouseButton, modifier, player);
	}

	private ItemStack slotClickPhantom(Slot slot, int mouseButton, int modifier, EntityPlayer player) {
		ItemStack stack = null;

		if (mouseButton == 2) {
			if (((IPhantomSlot) slot).canAdjust()) {
				slot.putStack(null);
			}
		}
		else if (mouseButton == 0 || mouseButton == 1) {
			InventoryPlayer playerInv = player.inventory;
			slot.onSlotChanged();
			ItemStack stackSlot = slot.getStack();
			ItemStack stackHeld = playerInv.getItemStack();

			if (stackSlot != null) {
				stack = stackSlot.copy();
			}

			if (stackSlot == null) {
				if (stackHeld != null && slot.isItemValid(stackHeld)) {
					fillPhantomSlot(slot, stackHeld, mouseButton, modifier);
				}
			} else if (stackHeld == null) {
				adjustPhantomSlot(slot, mouseButton, modifier);
				slot.onPickupFromSlot(player, playerInv.getItemStack());
			} else if (slot.isItemValid(stackHeld)) {
				if (canStacksMerge(stackSlot, stackHeld)) {
					adjustPhantomSlot(slot, mouseButton, modifier);
				} else {
					fillPhantomSlot(slot, stackHeld, mouseButton, modifier);
				}
			}
		}
		refreshGuide(slot.getStack());
		return stack;
	}

	public static boolean canStacksMerge(ItemStack stack1, ItemStack stack2) {
		if (stack1 == null || stack2 == null) {
			return false;
		}
		if (!stack1.isItemEqual(stack2)) {
			return false;
		}
		if (!ItemStack.areItemStackTagsEqual(stack1, stack2)) {
			return false;
		}
		return true;

	}
	
	protected void adjustPhantomSlot(Slot slot, int mouseButton, int modifier) {
		if (!((IPhantomSlot) slot).canAdjust()) {
			return;
		}
		ItemStack stackSlot = slot.getStack();
		int stackSize;
		if (modifier == 1) {
			stackSize = mouseButton == 0 ? (stackSlot.stackSize + 1) / 2 : stackSlot.stackSize * 2;
		} else {
			stackSize = mouseButton == 0 ? stackSlot.stackSize - 1 : stackSlot.stackSize + 1;
		}

		if (stackSize > slot.getSlotStackLimit()) {
			stackSize = slot.getSlotStackLimit();
		}

		stackSlot.stackSize = stackSize;

		if (stackSlot.stackSize <= 0) {
			slot.putStack((ItemStack) null);
		}
	}

	protected void fillPhantomSlot(Slot slot, ItemStack stackHeld, int mouseButton, int modifier) {
		if (!((IPhantomSlot) slot).canAdjust()) {
			return;
		}
		int stackSize = mouseButton == 0 ? stackHeld.stackSize : 1;
		if (stackSize > slot.getSlotStackLimit()) {
			stackSize = slot.getSlotStackLimit();
		}
		ItemStack phantomStack = stackHeld.copy();
		phantomStack.stackSize = 1;//stackSize;
		//refreshGuide(phantomStack);
		slot.putStack(phantomStack);
	}

}
