package com.pyraliron.pyralfishmod.init;

import java.util.ArrayList;
import java.util.List;

import com.pyraliron.pyralfishmod.block.BlockAdvancedNote;
import com.pyraliron.pyralfishmod.block.BlockBase;
import com.pyraliron.pyralfishmod.block.BlockDirtPyral;
import com.pyraliron.pyralfishmod.block.BlockGrassPyral;
import com.pyraliron.pyralfishmod.block.BlockSaplingPyral;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

public class ModBlocks {
	public static final List<Block> BLOCKS = new ArrayList<Block>();

	public static final Block GRASS_BLOCK_PYRAL = new BlockGrassPyral();
	public static final Block DIRT_BLOCK_PYRAL = new BlockDirtPyral();
	public static final Block SAPLING_PYRAL = new BlockSaplingPyral();
	public static final Block ADVANCED_NOTE_BLOCK_LOWEST = new BlockAdvancedNote(0,"advanced_note_block_lowest");
	public static final Block ADVANCED_NOTE_BLOCK_LOWER = new BlockAdvancedNote(1,"advanced_note_block_lower");
	public static final Block ADVANCED_NOTE_BLOCK_LOW = new BlockAdvancedNote(2,"advanced_note_block_low");
	public static final Block ADVANCED_NOTE_BLOCK_MID = new BlockAdvancedNote(3,"advanced_note_block_mid");
	public static final Block ADVANCED_NOTE_BLOCK_HIGH = new BlockAdvancedNote(4,"advanced_note_block_high");
	public static final Block ADVANCED_NOTE_BLOCK_HIGHER = new BlockAdvancedNote(5,"advanced_note_block_higher");
	
}
