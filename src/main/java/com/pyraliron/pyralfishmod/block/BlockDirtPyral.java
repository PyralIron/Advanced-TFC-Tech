package com.pyraliron.pyralfishmod.block;

import java.util.Random;

import com.pyraliron.pyralfishmod.init.ModBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockDirtPyral extends BlockBase {
	
	
	public static final PropertyBool SNOWY = PropertyBool.create("snowy");
	public BlockDirtPyral(String name) {
		super(Material.GROUND, ("dirt_block_pyral_"+name), CreativeTabs.BUILDING_BLOCKS);
		setHardness(0.5F);
		this.setDefaultState(this.blockState.getBaseState().withProperty(SNOWY, Boolean.valueOf(false)));
		
		
	}
	public BlockDirtPyral() {
		super(Material.GROUND, ("dirt_block_pyral"), CreativeTabs.BUILDING_BLOCKS);
		 
		this.setDefaultState(this.blockState.getBaseState().withProperty(SNOWY, Boolean.valueOf(false)));
		
		
	}
	

    /**
     * Get the actual Block state of this Block at the given position. This applies properties not visible in the
     * metadata, such as fence connections.
     */
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos)
    {
        Block block = worldIn.getBlockState(pos.up()).getBlock();
        //TODO replace snow/snow_layer with own blocks
        return state.withProperty(SNOWY, Boolean.valueOf(block == Blocks.SNOW || block == Blocks.SNOW_LAYER));
    }
    /**
     * returns a list of blocks with the same ID, but different meta (eg: wood returns 4 blocks)
     */
    /*@SideOnly(Side.CLIENT)
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items)
    {
        items.add(new ItemStack(this, 1, BlockDirtPyral.DirtType.DIRT.getMetadata()));
        items.add(new ItemStack(this, 1, BlockDirtPyral.DirtType.COARSE_DIRT.getMetadata()));
        items.add(new ItemStack(this, 1, BlockDirtPyral.DirtType.PODZOL.getMetadata()));
    }*/

    /*
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state)
    {
        return new ItemStack(this, 1, ((BlockDirtPyral.DirtType)state.getValue(VARIANT)).getMetadata());
    }*/
    
    /**
     * Convert the given metadata into a BlockState for this Block
     */
    /*
    public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(VARIANT, BlockDirtPyral.DirtType.byMetadata(meta));
    }*/

    
    /**
     * Convert the BlockState into the correct metadata value
     */
    
    public int getMetaFromState(IBlockState state)
    {
        return 0;
    }
    
    protected BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, new IProperty[] {SNOWY});
    }

    /**
     * Gets the metadata of the item this Block can drop. This method is called when the block gets destroyed. It
     * returns the metadata of the dropped item based on the old metadata of the block.
     */
    /*
    @Override
    public int damageDropped(IBlockState state)
    {
    	//metadata test
        BlockDirtPyral.DirtType blockdirt$dirttype = (BlockDirtPyral.DirtType)state.getValue(VARIANT);
        System.out.println(blockdirt$dirttype.getMetadata());
        
        if (blockdirt$dirttype == BlockDirtPyral.DirtType.PODZOL)
        {
        	System.out.println("Podzol");
            blockdirt$dirttype = BlockDirtPyral.DirtType.DIRT;
        } 
        else if (blockdirt$dirttype == BlockDirtPyral.DirtType.DIRT) 
        {
        	System.out.println("Dirt");
        	blockdirt$dirttype = BlockDirtPyral.DirtType.COARSE_DIRT;
        } 
        else 
        {
        	System.out.println("Coarse Dirt");
        	blockdirt$dirttype = BlockDirtPyral.DirtType.PODZOL;
        }
        System.out.println(blockdirt$dirttype.getMetadata());
        
        return blockdirt$dirttype.getMetadata();
    }
    public String getUnlocalizedNameFromMetaData(int meta) {
		return (getUnlocalizedName()+"_"+BlockDirtPyral.DirtType.byMetadata(meta).getUnlocalizedName());
	}*/
    

    public static enum DirtType implements IStringSerializable
    {
        DIRT(0, "dirt", "default", MapColor.DIRT),
        COARSE_DIRT(1, "coarse_dirt", "coarse", MapColor.DIRT),
        PODZOL(2, "podzol", MapColor.OBSIDIAN);

        private static final BlockDirtPyral.DirtType[] METADATA_LOOKUP = new BlockDirtPyral.DirtType[values().length];
        private final int metadata;
        private final String name;
        private final String unlocalizedName;
        private final MapColor color;

        private DirtType(int metadataIn, String nameIn, MapColor color)
        {
        	
            this(metadataIn, nameIn, nameIn, color);
        }

        private DirtType(int metadataIn, String nameIn, String unlocalizedNameIn, MapColor color)
        {
            this.metadata = metadataIn;
            this.name = nameIn;
            this.unlocalizedName = unlocalizedNameIn;
            this.color = color;
        }
        
        

        public int getMetadata()
        {
            return this.metadata;
        }

        public String getUnlocalizedName()
        {
            return this.unlocalizedName;
        }

        public MapColor getColor()
        {
            return this.color;
        }

        public String toString()
        {
            return this.name;
        }

        public static BlockDirtPyral.DirtType byMetadata(int metadata)
        {
            if (metadata < 0 || metadata >= METADATA_LOOKUP.length)
            {
                metadata = 0;
            }

            return METADATA_LOOKUP[metadata];
        }

        public String getName()
        {
            return this.name;
        }

        static
        {
            for (BlockDirtPyral.DirtType blockdirt$dirttype : values())
            {
                METADATA_LOOKUP[blockdirt$dirttype.getMetadata()] = blockdirt$dirttype;
            }
        }
    }
}
