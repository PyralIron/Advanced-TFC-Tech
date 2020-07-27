package com.pyraliron.pyralfishmod.init;

import com.pyraliron.pyralfishmod.Main;
import com.pyraliron.pyralfishmod.util.Reference;
import com.pyraliron.pyralfishmod.world.WorldTypePyral;
import com.pyraliron.pyralfishmod.world.biome.BiomeForestPyral;
import com.pyraliron.pyralfishmod.world.biome.BiomeShrubland;
import com.pyraliron.pyralfishmod.world.gen.feature.WorldGenTreePyral;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockOldLeaf;
import net.minecraft.block.BlockOldLog;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.WorldGenBlockBlob;
import net.minecraft.world.gen.feature.WorldGenSavannaTree;
import net.minecraft.world.gen.feature.WorldGenShrub;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.BiomeType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBiomes {
	
	public static WorldTypePyral worldTypePyral;
	public static Biome shrubland;
	public static Biome forest;
	
	public static void init()
    {
		worldTypePyral = new WorldTypePyral();
		shrubland = new BiomeShrubland();
		forest = new BiomeForestPyral();
		
		RegisterBiomes.registerOverworldBiomes(shrubland, BiomeType.WARM, true, 6);
		RegisterBiomes.registerOverworldBiomes(forest, BiomeType.WARM, true, 6);
		MinecraftForge.EVENT_BUS.register(new RegisterBiomes());
    }
	
	
	
	
	public static abstract class BiomeBlocks {
        public static final IBlockState OAK_LOG = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.OAK);
        public static final IBlockState OAK_LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.OAK).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));

        public static final IBlockState BIRCH_LOG = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.BIRCH);
        public static final IBlockState BIRCH_LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.BIRCH).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));

        public static final IBlockState SPRUCE_LOG = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.SPRUCE);
        public static final IBlockState SPRUCE_LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.SPRUCE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));

        public static final IBlockState JUNGLE_LOG = Blocks.LOG.getDefaultState().withProperty(BlockOldLog.VARIANT, BlockPlanks.EnumType.JUNGLE);
        public static final IBlockState JUNGLE_LEAF = Blocks.LEAVES.getDefaultState().withProperty(BlockOldLeaf.VARIANT, BlockPlanks.EnumType.JUNGLE).withProperty(BlockLeaves.CHECK_DECAY, Boolean.valueOf(false));
    }

    public static abstract class Features {
        public static final WorldGenShrub OAK_SHRUB = new WorldGenShrub(BiomeBlocks.OAK_LOG, BiomeBlocks.OAK_LEAF);
        public static final WorldGenShrub JUNGLE_SHRUB = new WorldGenShrub(BiomeBlocks.JUNGLE_LOG, BiomeBlocks.JUNGLE_LEAF);
        public static final WorldGenShrub TUNDRA_SHRUB = new WorldGenShrub(BiomeBlocks.OAK_LOG, BiomeBlocks.SPRUCE_LEAF);
        
        public static final WorldGenTreePyral PYRAL_TREE = new WorldGenTreePyral(false);

        public static final WorldGenSavannaTree SAVANNA_TREE = new WorldGenSavannaTree(false);

        public static final WorldGenBlockBlob BOULDER_COBBLE = new WorldGenBlockBlob(Blocks.COBBLESTONE, 0);
    }
}
