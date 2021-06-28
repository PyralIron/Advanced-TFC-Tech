/*
 * Large potions of this code were taken from Immersive Petroleum created by Flaxbeard
 * https://github.com/Flaxbeard/ImmersivePetroleum/
 * as well as from Immersive Engineering created by BluSunrize
 * https://github.com/BluSunrize/ImmersiveEngineering/
 */
package com.pyraliron.advancedtfctech.multiblocks;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import com.pyraliron.advancedtfctech.blocks.BlockATTBase;
import com.pyraliron.advancedtfctech.blocks.BlockATTTileProvider;
import com.pyraliron.advancedtfctech.blocks.ItemBlockATTBase;
import flaxbeard.immersivepetroleum.common.blocks.BlockIPBase;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
public abstract class BlockATTMultiblock<E extends Enum<E> & BlockIPBase.IBlockEnum & BlockATTBase.IBlockEnum> extends BlockATTTileProvider<E>
{
    public BlockATTMultiblock(String name, Material material, PropertyEnum<E> mainProperty, Class<? extends ItemBlockATTBase> itemBlock, Object... additionalProperties)
    {
        super(name, material, mainProperty, itemBlock, combineProperties(additionalProperties, IEProperties.FACING_HORIZONTAL, IEProperties.MULTIBLOCKSLAVE));
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        state = super.getActualState(state, world, pos);
        return state;
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityMultiblockPart && world.getGameRules().getBoolean("doTileDrops"))
        {
            TileEntityMultiblockPart tile = (TileEntityMultiblockPart) tileEntity;
            if (!tile.formed && tile.getPos().toLong() == -1 && tile.getOriginalBlock() != null)
                world.spawnEntity(new EntityItem(world, pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5, tile.getOriginalBlock().copy()));

            if (tileEntity instanceof IInventory)
                InventoryHelper.dropInventoryItems(world, pos, (IInventory) tile);
        }
        //System.out.println("TRY TO DISASSEMBLE");
        if (tileEntity instanceof TileEntityMultiblockPart) {
            //System.out.println("DISASSEMBLE");
            BlockPos masterPos = (tileEntity).getPos().add(-((TileEntityMultiblockPart) tileEntity).offset[0], -((TileEntityMultiblockPart) tileEntity).offset[1], -((TileEntityMultiblockPart) tileEntity).offset[2]);
            //System.out.println(((TileEntityMultiblockPart) tileEntity).getOrigin()+ " "+masterPos+" "+state);
            //if (state.getProperties().get("type") == "powerloom_parent") {
            //}
            //System.out.println(state.getPropertyKeys());
            //System.out.println("dis prev");
            ((TileEntityMultiblockPart) tileEntity).disassemble();
            //System.out.println("dis next");


        }

        super.breakBlock(world, pos, state);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        return new ArrayList<ItemStack>();
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        ItemStack stack = getOriginalBlock(world, pos);
        if (!stack.isEmpty())
            return stack;
        return super.getPickBlock(state, target, world, pos, player);
    }

    public ItemStack getOriginalBlock(World world, BlockPos pos)
    {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityMultiblockPart)
            return ((TileEntityMultiblockPart) te).getOriginalBlock();
        return ItemStack.EMPTY;
    }
}
