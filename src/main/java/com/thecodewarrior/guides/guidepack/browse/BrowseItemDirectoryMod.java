package com.thecodewarrior.guides.guidepack.browse;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class BrowseItemDirectoryMod extends BrowseItemDirectory{

	ModContainer mod;
	String modid;
	
	public BrowseItemDirectoryMod(String modid, String name) {
		super(name); // get the human readable name of the mod
		// the automatic version had a NPE. => Loader.instance().getIndexedModList().get(modid).getName()
		this.modid = modid;
		this.mod = Loader.instance().getIndexedModList().get(modid);
	}
	
	public String getModId() {
		return modid;
	}

}
