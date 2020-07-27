package com.pyraliron.pyralfishmod.items;

import com.pyraliron.pyralfishmod.Main;
import com.pyraliron.pyralfishmod.init.ModItems;
import com.pyraliron.pyralfishmod.util.IHasModel;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ItemBase extends Item implements IHasModel {

	public ItemBase(String name, CreativeTabs tab) {
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(tab);
		ModItems.ITEMS.add(this);
	}
	@Override
	public void registerModel() {
		Main.proxy.registerItemRenderer(this, 0, "inventory");
	}

}
