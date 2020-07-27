package com.pyraliron.advancedtfctech.te;

import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import blusunrize.immersiveengineering.common.util.Utils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileEntityMultiblockPartATT extends TileEntityMultiblockPart<TileEntityMultiblockPartATT> {
    public TileEntityMultiblockPartATT (int[] size) {
        super(size);
    }

    @Nonnull
    @Override
    protected IFluidTank[] getAccessibleFluidTanks(@Nullable EnumFacing enumFacing) {
        return new IFluidTank[0];
    }

    @Override
    protected boolean canFillTankFrom(int i, EnumFacing enumFacing, FluidStack fluidStack) {
        return false;
    }

    @Override
    protected boolean canDrainTankFrom(int i, EnumFacing enumFacing) {
        return false;
    }

    @Override
    public ItemStack getOriginalBlock() {
        return null;
    }

    @Override
    public float[] getBlockBounds() {
        return new float[0];
    }

    @Override
    public void update() {

    }
    @Override
    public void disassemble()
    {
        if(formed&&!world.isRemote)
        {
            BlockPos startPos = getOrigin();
            BlockPos masterPos = getPos().add(-offset[0], -offset[1], -offset[2]);
            long time = world.getTotalWorldTime();
            for(int yy = 0; yy < structureDimensions[0]; yy++)
                for(int ll = 0; ll < structureDimensions[1]; ll++)
                    for(int ww = 0; ww < structureDimensions[2]; ww++)
                    {
                        int w = mirrored?-ww: ww;
                        BlockPos pos = startPos.offset(facing, ll).offset(facing.rotateY(), w).add(0, yy, 0);
                        System.out.println(pos+" "+startPos+" "+offset+" "+masterPos);
                        ItemStack s = ItemStack.EMPTY;

                        TileEntity te = world.getTileEntity(pos);
                        if(te instanceof TileEntityMultiblockPart)
                        {
                            TileEntityMultiblockPart part = (TileEntityMultiblockPart)te;
                            Vec3i diff = pos.subtract(masterPos);
                            if(part.offset[0]!=diff.getX()||part.offset[1]!=diff.getY()||part.offset[2]!=diff.getZ())
                                continue;
                            else if(time!=part.onlyLocalDissassembly)
                            {
                                s = part.getOriginalBlock();
                                part.formed = false;
                            }
                        }
                        if(pos.equals(getPos()))
                            s = this.getOriginalBlock();
                        IBlockState state = Utils.getStateFromItemStack(s);
                        if(state!=null)
                        {
                            if(pos.equals(getPos()))
                                world.spawnEntity(new EntityItem(world, pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5, s));
                            else
                                replaceStructureBlock(pos, state, s, yy, ll, ww);
                        }
                    }
        }
    }
}
