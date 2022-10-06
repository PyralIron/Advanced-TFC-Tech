package com.pyraliron.advancedtfctech.blocks;

import com.pyraliron.advancedtfctech.te.TileEntityCharcoalForgeATT;
import net.dries007.tfc.objects.blocks.devices.BlockCharcoalForge;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockCharcoalForgeATT extends BlockCharcoalForge {
    public BlockCharcoalForgeATT() {
        super();
    }
    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityCharcoalForgeATT();
    }
}
