package com.pyraliron.pyralfishmod.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockPyral extends ItemBlock {

	public ItemBlockPyral(Block block) {
		super(block);
		
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
    {
		//System.out.println("f2");
		String b = ((BlockBase) (Block.getBlockFromItem(stack.getItem()))).getUnlocalizedName();//.getUnlocalizedNameFromMetaData(0);//stack.getMetadata());
		return b;
        
    }

}
