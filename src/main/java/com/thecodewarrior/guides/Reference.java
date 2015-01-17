package com.thecodewarrior.guides;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class Reference {
	public static final String MODID = "guides";
	public static final String VERSION = "0.1";
	
	public static int bookOfRevealingGuiID;
	
	public static final CreativeTabs tab = new CreativeTabs(MODID + ".tab") {
		@Override
		public Item getTabIconItem() {
			// TODO Auto-generated method stub
			return GuideMod.bookOfRevealing;
		}
	};
}
