package com.pyraliron.pyralfishmod.block;

import com.pyraliron.pyralfishmod.Main;
import com.pyraliron.pyralfishmod.init.ModBlocks;
import com.pyraliron.pyralfishmod.init.ModItems;
import com.pyraliron.pyralfishmod.util.IHasModel;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class BlockBase extends Block implements IHasModel {

	public BlockBase(Material material, String name, CreativeTabs tab) {
		super(material);
		setUnlocalizedName(name);
		setRegistryName(name);
		setCreativeTab(tab);
		ModBlocks.BLOCKS.add(this);
		ModItems.ITEMS.add(new ItemBlockPyral(this).setRegistryName(this.getRegistryName()));
	}

	@Override
	public void registerModel() {
		Main.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}
	
	public String getUnlocalizedNameFromMetaData(int meta) {
		return getUnlocalizedName();
	}
	
}
