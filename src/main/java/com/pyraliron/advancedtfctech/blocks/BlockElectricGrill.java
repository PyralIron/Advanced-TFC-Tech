package com.pyraliron.advancedtfctech.blocks;

import com.pyraliron.advancedtfctech.te.TileEntityElectricGrill;
import net.dries007.tfc.objects.blocks.devices.BlockFirePit;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BlockElectricGrill extends BlockFirePit {
    public BlockElectricGrill() {
        super();
    }
    @Nullable
    @Override
    public TileEntity createTileEntity(World world, IBlockState state)
    {
        return new TileEntityElectricGrill();
    }
}
