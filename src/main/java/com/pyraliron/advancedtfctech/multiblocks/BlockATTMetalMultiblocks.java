package com.pyraliron.advancedtfctech.multiblocks;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import com.pyraliron.advancedtfctech.blocks.ItemBlockATTBase;
import com.pyraliron.advancedtfctech.te.TileEntityPowerLoom;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.Properties;

import javax.annotation.Nullable;

public class BlockATTMetalMultiblocks extends BlockATTMultiblock<BlockTypes_ATTMetalMultiblock>
{
    public BlockATTMetalMultiblocks()
    {
        super("metal_multiblock_att", Material.IRON, PropertyEnum.create("type", BlockTypes_ATTMetalMultiblock.class),ItemBlockATTBase.class, IEProperties.DYNAMICRENDER, IEProperties.BOOLEANS[0], Properties.AnimationProperty, IEProperties.OBJ_TEXTURE_REMAP);
        setHardness(3.0F);
        setResistance(15.0F);
        this.setAllNotNormalBlock();
        lightOpacity = 0;
    }

    @Override
    public boolean useCustomStateMapper()
    {
        return true;
    }

    @Override
    public String getCustomStateMapping(int meta, boolean itemBlock)
    {
        if (BlockTypes_ATTMetalMultiblock.values()[meta].needsCustomState())
            return BlockTypes_ATTMetalMultiblock.values()[meta].getCustomState();
        return null;
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        switch (BlockTypes_ATTMetalMultiblock.values()[meta])
        {
            case POWERLOOM:
                return new TileEntityPowerLoom();
            case POWERLOOM_PARENT:
                return new TileEntityPowerLoom.TileEntityPowerLoomParent();
        }
        return null;
    }



    @Override
    public boolean isSideSolid(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityMultiblockPart)
        {
            TileEntityMultiblockPart tile = (TileEntityMultiblockPart) te;
            if (tile instanceof TileEntityMultiblockMetal && ((TileEntityMultiblockMetal) tile).isRedstonePos())
                return true;


            else if (te instanceof TileEntityPowerLoom)
            {
                return tile.getPos().toLong() == 2;
            }

        }
        return super.isSideSolid(state, world, pos, side);
    }


    @Override
    public boolean allowHammerHarvest(IBlockState state)
    {
        return true;
    }

    //@Override
    public void onEntityCollision(World worldIn, BlockPos pos, IBlockState state, Entity entityIn)
    {
        //super.onEntityCollision(worldIn, pos, state, entityIn);
        if (entityIn instanceof EntityLivingBase && !((EntityLivingBase) entityIn).isOnLadder() && isLadder(state, worldIn, pos, (EntityLivingBase) entityIn))
        {
            float f5 = 0.15F;
            if (entityIn.motionX < -f5)
                entityIn.motionX = -f5;
            if (entityIn.motionX > f5)
                entityIn.motionX = f5;
            if (entityIn.motionZ < -f5)
                entityIn.motionZ = -f5;
            if (entityIn.motionZ > f5)
                entityIn.motionZ = f5;

            entityIn.fallDistance = 0.0F;
            if (entityIn.motionY < -0.15D)
                entityIn.motionY = -0.15D;

            if (entityIn.motionY < 0 && entityIn instanceof EntityPlayer && entityIn.isSneaking())
            {
                entityIn.motionY = .05;
                return;
            }
            if (entityIn.collidedHorizontally)
                entityIn.motionY = .2;
        }
    }

    @Override
    public boolean isLadder(IBlockState state, IBlockAccess world, BlockPos pos, EntityLivingBase entity)
    {
        TileEntity te = world.getTileEntity(pos);
        /*if (te instanceof TileEntityDistillationTower)
        {
            return ((TileEntityDistillationTower) te).isLadder();
        }*/
        return false;
    }

    @Deprecated
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
    }

}