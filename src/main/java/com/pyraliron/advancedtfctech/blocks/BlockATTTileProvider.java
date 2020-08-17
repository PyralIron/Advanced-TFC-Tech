/*
 * Large potions of this code were taken from Immersive Petroleum created by Flaxbeard
 * https://github.com/Flaxbeard/ImmersivePetroleum/
 * as well as from Immersive Engineering created by BluSunrize
 * https://github.com/BluSunrize/ImmersiveEngineering/
 */
package com.pyraliron.advancedtfctech.blocks;

import blusunrize.immersiveengineering.api.IEProperties;
import blusunrize.immersiveengineering.api.energy.wires.IImmersiveConnectable;
import blusunrize.immersiveengineering.api.energy.wires.ImmersiveNetHandler;
import blusunrize.immersiveengineering.api.energy.wires.TileEntityImmersiveConnectable;
import blusunrize.immersiveengineering.api.shader.CapabilityShader;
import blusunrize.immersiveengineering.client.models.IOBJModelCallback;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IDirectionalTile;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IHasObjProperty;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IPlayerInteraction;
import blusunrize.immersiveengineering.common.blocks.TileEntityIEBase;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import blusunrize.immersiveengineering.common.util.Utils;
import blusunrize.immersiveengineering.common.util.inventory.IEInventoryHandler;
import com.pyraliron.advancedtfctech.crafting.PowerLoomRecipe;
import com.pyraliron.advancedtfctech.proxy.CommonProxy;
import com.pyraliron.advancedtfctech.te.TileEntityPowerLoom;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;

public abstract class BlockATTTileProvider<E extends Enum<E> & BlockATTBase.IBlockEnum> extends BlockATTBase<E> implements ITileEntityProvider, IEBlockInterfaces.IColouredBlock {
    private boolean hasColours = false;

    public BlockATTTileProvider(String name, Material material, PropertyEnum<E> mainProperty, Class<? extends ItemBlockATTBase> itemBlock, Object... additionalProperties)
    {
        super(name, material, mainProperty, itemBlock, additionalProperties);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune)
    {
        return super.getDrops(world, pos, state, fortune);
    }

    @Override
    public void breakBlock(World world, BlockPos pos, IBlockState state)
    {

        TileEntity tile = world.getTileEntity(pos);
        if (tile != null && (!(tile instanceof IEBlockInterfaces.ITileDrop) || !((IEBlockInterfaces.ITileDrop) tile).preventInventoryDrop()) && tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null))
        {
            if (tile instanceof TileEntityPowerLoom) {
                //System.out.println(((TileEntityPowerLoom) tile).getStructureDimensions());
                //System.out.println("TILE ENTITY POS "+((TileEntityPowerLoom) tile));
               // System.out.println("TILE ENTITY POS "+((TileEntityPowerLoom) tile).getFakepos());

            }
            IItemHandler h = tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);

            if (h instanceof IEInventoryHandler)
               // System.out.println("handler "+h.getSlots());
                for (int i = 0; i < h.getSlots(); i++)
                {
                    if (h.getStackInSlot(i) != null)
                    {
                        spawnAsEntity(world, pos, h.getStackInSlot(i));
                        //System.out.println("INV HANDLER "+h+" "+i);
                        ((IEInventoryHandler) h).setStackInSlot(i, ItemStack.EMPTY);
                    }
                }
        }
        if (tile instanceof IEBlockInterfaces.IHasDummyBlocks)
        {
            //System.out.println("Has dummy blocks");
            ((IEBlockInterfaces.IHasDummyBlocks) tile).breakDummies(pos, state);
        }
        if (tile instanceof IImmersiveConnectable)
            //System.out.println("Connectable");
            if (!world.isRemote || !Minecraft.getMinecraft().isSingleplayer())
                ImmersiveNetHandler.INSTANCE.clearAllConnectionsFor(Utils.toCC(tile), world, !world.isRemote && world.getGameRules().getBoolean("doTileDrops"));
        super.breakBlock(world, pos, state);
        world.removeTileEntity(pos);
    }

    @Override
    public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity tile, ItemStack stack)
    {
        if (tile instanceof IEBlockInterfaces.ITileDrop)
        {
            ItemStack s = ((IEBlockInterfaces.ITileDrop) tile).getTileDrop(player, state);
            if (!s.isEmpty())
            {
                spawnAsEntity(world, pos, s);
                return;
            }
        }
        if (tile instanceof IEBlockInterfaces.IAdditionalDrops)
        {
            Collection<ItemStack> stacks = ((IEBlockInterfaces.IAdditionalDrops) tile).getExtraDrops(player, state);
            if (stacks != null && !stacks.isEmpty())
                for (ItemStack s : stacks)
                {
                    if (!s.isEmpty())
                        spawnAsEntity(world, pos, s);
                }
        }
        super.harvestBlock(world, player, pos, state, tile, stack);
    }

    @Override
    public boolean canEntityDestroy(IBlockState state, IBlockAccess world, BlockPos pos, Entity entity)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IEBlockInterfaces.IEntityProof)
            return ((IEBlockInterfaces.IEntityProof) tile).canEntityDestroy(entity);
        return super.canEntityDestroy(state, world, pos, entity);
    }

    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IEBlockInterfaces.ITileDrop)
        {
            ItemStack s = ((IEBlockInterfaces.ITileDrop) tile).getTileDrop(player, world.getBlockState(pos));
            if (!s.isEmpty())
                return s;
        }
        Item item = Item.getItemFromBlock(this);
        return item == Items.AIR ? ItemStack.EMPTY : new ItemStack(item, 1, this.damageDropped(world.getBlockState(pos)));
    }


    @Override
    public boolean eventReceived(IBlockState state, World worldIn, BlockPos pos, int eventID, int eventParam)
    {
        super.eventReceived(state, worldIn, pos, eventID, eventParam);
        TileEntity tileentity = worldIn.getTileEntity(pos);
        return tileentity != null && tileentity.receiveClientEvent(eventID, eventParam);
    }

    protected EnumFacing getDefaultFacing()
    {
        return EnumFacing.NORTH;
    }

    @Override
    public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        state = super.getActualState(state, world, pos);
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof IEBlockInterfaces.IAttachedIntegerProperies)
        {
            for (String s : ((IEBlockInterfaces.IAttachedIntegerProperies) tile).getIntPropertyNames())
            {
                state = applyProperty(state, ((IEBlockInterfaces.IAttachedIntegerProperies) tile).getIntProperty(s), ((IEBlockInterfaces.IAttachedIntegerProperies) tile).getIntPropertyValue(s));
            }
        }

        if (tile instanceof IDirectionalTile && (state.getPropertyKeys().contains(IEProperties.FACING_ALL) || state.getPropertyKeys().contains(IEProperties.FACING_HORIZONTAL)))
        {
            PropertyDirection prop = state.getPropertyKeys().contains(IEProperties.FACING_HORIZONTAL) ? IEProperties.FACING_HORIZONTAL : IEProperties.FACING_ALL;
            state = applyProperty(state, prop, ((IDirectionalTile) tile).getFacing());
        }
        else if (state.getPropertyKeys().contains(IEProperties.FACING_HORIZONTAL))
            state = state.withProperty(IEProperties.FACING_HORIZONTAL, getDefaultFacing());
        else if (state.getPropertyKeys().contains(IEProperties.FACING_ALL))
            state = state.withProperty(IEProperties.FACING_ALL, getDefaultFacing());

        if (tile instanceof IEBlockInterfaces.IActiveState)
        {
            IProperty boolProp = ((IEBlockInterfaces.IActiveState) tile).getBoolProperty(IEBlockInterfaces.IActiveState.class);
            if (state.getPropertyKeys().contains(boolProp))
                state = applyProperty(state, boolProp, ((IEBlockInterfaces.IActiveState) tile).getIsActive());
        }

        if (tile instanceof IEBlockInterfaces.IDualState)
        {
            IProperty boolProp = ((IEBlockInterfaces.IDualState) tile).getBoolProperty(IEBlockInterfaces.IDualState.class);
            if (state.getPropertyKeys().contains(boolProp))
                state = applyProperty(state, boolProp, ((IEBlockInterfaces.IDualState) tile).getIsSecondState());
        }

        if (tile instanceof TileEntityMultiblockPart)
            state = applyProperty(state, IEProperties.MULTIBLOCKSLAVE, ((TileEntityMultiblockPart) tile).isDummy());
        else if (tile instanceof IEBlockInterfaces.IHasDummyBlocks)
            state = applyProperty(state, IEProperties.MULTIBLOCKSLAVE, ((IEBlockInterfaces.IHasDummyBlocks) tile).isDummy());

        if (tile instanceof IEBlockInterfaces.IMirrorAble)
            state = applyProperty(state, ((IEBlockInterfaces.IMirrorAble) tile).getBoolProperty(IEBlockInterfaces.IMirrorAble.class), ((IEBlockInterfaces.IMirrorAble) tile).getIsMirrored());

        return state;
    }

    @Override
    public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IDirectionalTile)
        {
            if (!((IDirectionalTile) tile).canRotate(axis))
                return false;
            IBlockState state = world.getBlockState(pos);
            if (state.getPropertyKeys().contains(IEProperties.FACING_ALL) || state.getPropertyKeys().contains(IEProperties.FACING_HORIZONTAL))
            {
                PropertyDirection prop = state.getPropertyKeys().contains(IEProperties.FACING_HORIZONTAL) ? IEProperties.FACING_HORIZONTAL : IEProperties.FACING_ALL;
                EnumFacing f = ((IDirectionalTile) tile).getFacing();
                int limit = ((IDirectionalTile) tile).getFacingLimitation();

                if (limit == 0)
                    f = EnumFacing.VALUES[(f.ordinal() + 1) % EnumFacing.VALUES.length];
                else if (limit == 1)
                    f = axis.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? f.rotateAround(axis.getAxis()).getOpposite() : f.rotateAround(axis.getAxis());
                else if (limit == 2 || limit == 5)
                    f = axis.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? f.rotateY() : f.rotateYCCW();
                if (f != ((IDirectionalTile) tile).getFacing())
                {
                    EnumFacing old = ((IDirectionalTile) tile).getFacing();
                    ((IDirectionalTile) tile).setFacing(f);
                    ((IDirectionalTile) tile).afterRotation(old, f);
                    state = applyProperty(state, prop, ((IDirectionalTile) tile).getFacing());
                    world.setBlockState(pos, state.cycleProperty(prop));
                }
            }
        }
        return false;
    }

    @Override
    public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        state = super.getExtendedState(state, world, pos);
        if (state instanceof IExtendedBlockState)
        {
            IExtendedBlockState extended = (IExtendedBlockState) state;
            TileEntity te = world.getTileEntity(pos);
            if (te != null)
            {
                if (te instanceof IEBlockInterfaces.IConfigurableSides)
                    for (int i = 0; i < 6; i++)
                    {
                        if (extended.getUnlistedNames().contains(IEProperties.SIDECONFIG[i]))
                            extended = extended.withProperty(IEProperties.SIDECONFIG[i], ((IEBlockInterfaces.IConfigurableSides) te).getSideConfig(i));
                    }
                if (te instanceof IEBlockInterfaces.IAdvancedHasObjProperty)
                    extended = extended.withProperty(Properties.AnimationProperty, ((IEBlockInterfaces.IAdvancedHasObjProperty) te).getOBJState());
                else if (te instanceof IHasObjProperty)
                    extended = extended.withProperty(Properties.AnimationProperty, new OBJModel.OBJState(((IHasObjProperty) te).compileDisplayList(), true));
                if (te instanceof IEBlockInterfaces.IDynamicTexture)
                    extended = extended.withProperty(IEProperties.OBJ_TEXTURE_REMAP, ((IEBlockInterfaces.IDynamicTexture) te).getTextureReplacements());
                if (te instanceof IOBJModelCallback)
                    extended = extended.withProperty(IOBJModelCallback.PROPERTY, (IOBJModelCallback) te);
                if (te.hasCapability(CapabilityShader.SHADER_CAPABILITY, null))
                    extended = extended.withProperty(CapabilityShader.BLOCKSTATE_PROPERTY, te.getCapability(CapabilityShader.SHADER_CAPABILITY, null));
                if (te instanceof IEBlockInterfaces.IPropertyPassthrough && ((IExtendedBlockState) state).getUnlistedNames().contains(IEProperties.TILEENTITY_PASSTHROUGH))
                    extended = extended.withProperty(IEProperties.TILEENTITY_PASSTHROUGH, te);
                if (te instanceof TileEntityImmersiveConnectable && ((IExtendedBlockState) state).getUnlistedNames().contains(IEProperties.CONNECTIONS))
                    extended = extended.withProperty(IEProperties.CONNECTIONS, ((TileEntityImmersiveConnectable) te).genConnBlockstate());
            }
            state = extended;
        }

        return state;
    }

    @Override
    public void onIEBlockPlacedBy(World world, BlockPos pos, IBlockState state, EnumFacing side, float hitX, float hitY, float hitZ, EntityLivingBase placer, ItemStack stack)
    {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof IDirectionalTile)
        {
            EnumFacing f = ((IDirectionalTile) tile).getFacingForPlacement(placer, pos, side, hitX, hitY, hitZ);
            ((IDirectionalTile) tile).setFacing(f);
            if (tile instanceof IEBlockInterfaces.IAdvancedDirectionalTile)
                ((IEBlockInterfaces.IAdvancedDirectionalTile) tile).onDirectionalPlacement(side, hitX, hitY, hitZ, placer);
        }
        if (tile instanceof IEBlockInterfaces.IHasDummyBlocks)
        {
            ((IEBlockInterfaces.IHasDummyBlocks) tile).placeDummies(pos, state, side, hitX, hitY, hitZ);
        }
        if (tile instanceof IEBlockInterfaces.ITileDrop)
        {
            ((IEBlockInterfaces.ITileDrop) tile).readOnPlacement(placer, stack);
        }
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ)
    {
        ItemStack heldItem = player.getHeldItem(hand);
        TileEntity tile = world.getTileEntity(pos);
        //System.out.println("onBlockActivated RUN");
        if (tile instanceof IEBlockInterfaces.IConfigurableSides && Utils.isHammer(heldItem) && !world.isRemote)
        {
            int iSide = player.isSneaking() ? side.getOpposite().ordinal() : side.ordinal();
            if (((IEBlockInterfaces.IConfigurableSides) tile).toggleSide(iSide, player))
                return true;
        }
        if (tile instanceof IDirectionalTile && Utils.isHammer(heldItem) && ((IDirectionalTile) tile).canHammerRotate(side, hitX, hitY, hitZ, player) && !world.isRemote)
        {

            EnumFacing f = ((IDirectionalTile) tile).getFacing();
            int limit = ((IDirectionalTile) tile).getFacingLimitation();

            if (limit == 0)
                f = EnumFacing.VALUES[(f.ordinal() + 1) % EnumFacing.VALUES.length];
            else if (limit == 1)
                f = player.isSneaking() ? f.rotateAround(side.getAxis()).getOpposite() : f.rotateAround(side.getAxis());
            else if (limit == 2 || limit == 5)
                f = player.isSneaking() ? f.rotateYCCW() : f.rotateY();
            ((IDirectionalTile) tile).setFacing(f);
            tile.markDirty();
            world.notifyBlockUpdate(pos, state, state, 3);
            world.addBlockEvent(tile.getPos(), tile.getBlockType(), 255, 0);
            return true;
        }
        if (tile instanceof IEBlockInterfaces.IHammerInteraction && Utils.isHammer(heldItem) && !world.isRemote)
        {
            boolean b = ((IEBlockInterfaces.IHammerInteraction) tile).hammerUseSide(side, player, hitX, hitY, hitZ);
            if (b)
                return b;
        }
        //System.out.println("TILE IS: "+tile);
        if (tile instanceof IPlayerInteraction)
        {
            //System.out.println("TILE IS PLAYER INTERACTION");
            boolean b = ((IPlayerInteraction) tile).interact(side, player, hand, heldItem, hitX, hitY, hitZ);
            if (b)
                return b;
        }
        if (tile instanceof IEBlockInterfaces.IGuiTile && hand == EnumHand.MAIN_HAND && !player.isSneaking())
        {
            //System.out.println("TILE IS BLOCK INTERFACE");

            TileEntity master = ((IEBlockInterfaces.IGuiTile) tile).getGuiMaster();
            //System.out.println("GUI MASTER"+master);
            //System.out.println("WHY U NO CAN OPEN GUI"+((IEBlockInterfaces.IGuiTile) tile).canOpenGui(player));
            if (((IEBlockInterfaces.IGuiTile) tile).canOpenGui(player))
            {
                //System.out.println("TRY OPEN GUI FIXED SHOULD BE");
                if (!world.isRemote && master != null)
                    CommonProxy.openGuiForTile(player, (TileEntity & IEBlockInterfaces.IGuiTile) master);
                return true;
            }
        }
        //System.out.println("IS SHIFT KEY DOWN "+ GuiScreen.isShiftKeyDown()+" "+!player.getHeldItem(hand).isEmpty()+" "+hand+" "+player.getHeldItem(hand));

        if (Side == Side.Client && GuiScreen.isShiftKeyDown() && !player.getHeldItem(hand).isEmpty() && hand == EnumHand.MAIN_HAND) {

            TileEntity te = world.getTileEntity(pos);
            //System.out.println("TILE ENTITY "+te);
            if (te instanceof TileEntityPowerLoom) {
                TileEntityPowerLoom tem = ((TileEntityPowerLoom) te).master();
                ItemStack shiftCount = ItemStack.EMPTY;
                int j = -1;
                //System.out.println("TE POS "+((TileEntityPowerLoom) te).getFakepos());
                if (((TileEntityPowerLoom) te).getFakepos() == 15+1) {
                    for (int i = 0; i < 8; i++) {
                        if (tem.inputHandler.getStackInSlot(i).getCount() < 1) {
                            shiftCount = player.getHeldItem(hand);
                            j = i;
                        }
                    }
                }
                else if (((TileEntityPowerLoom) te).getFakepos() == 3 || ((TileEntityPowerLoom) te).getFakepos() == 6 || ((TileEntityPowerLoom) te).getFakepos() == 9) {
                    for (int i = 13; i < 16; i++) {
                        if (tem.inputHandler.getStackInSlot(i).getCount() < 64) {
                            shiftCount = player.getHeldItem(hand);
                            j = i;
                            break;
                        }
                    }
                }
                else if (((TileEntityPowerLoom) te).getFakepos() == 15+4 || ((TileEntityPowerLoom) te).getFakepos() == 15+7 || ((TileEntityPowerLoom) te).getFakepos() == 15+10) {
                    for (int i = 16; i < 17; i++) {
                        //System.out.println("wtf item count "+tem.inputHandler.getStackInSlot(i).getCount());
                        if (tem.inputHandler.getStackInSlot(i).getCount() < 16) {
                            shiftCount = player.getHeldItem(hand);
                            j = i;
                            break;
                        }
                    }
                }
                //System.out.println("SHIFT COUNT "+shiftCount +" "+j);
                if (!shiftCount.isEmpty() && PowerLoomRecipe.isValidRecipeInput(shiftCount)) {
                    ItemStack result = tem.inputHandler.insertItem(j,new ItemStack(player.getHeldItem(hand).getItem()),false);
                    player.getHeldItem(hand).shrink(1-result.getCount());
                    return true;
                } else if (!shiftCount.isEmpty() && PowerLoomRecipe.isValidRecipeSecondary(new ItemStack(shiftCount.getItem(),64,shiftCount.getMetadata()))) {
                    //System.out.println("actually works");
                    ItemStack result = tem.inputHandler.insertItem(j,player.getHeldItem(hand),false);
                    //System.out.println("RESULT "+result.getCount());
                    if (j == 16) {
                        player.getHeldItem(hand).shrink((Math.min(16,player.getHeldItem(hand).getCount())-result.getCount()));
                    }
                    else {
                        player.getHeldItem(hand).shrink(player.getHeldItem(hand).getCount()-result.getCount());
                    }
                    return true;




                }

            }
            return false;
        } else if (!GuiScreen.isShiftKeyDown() && player.getHeldItem(hand).isEmpty() && hand == EnumHand.MAIN_HAND) {

            TileEntity te = world.getTileEntity(pos);
            if (te instanceof TileEntityPowerLoom && !((TileEntityPowerLoom) te).master().isTicking) {
                //System.out.println("te fake pos "+((TileEntityPowerLoom) te).getFakepos());
                TileEntityPowerLoom tem = ((TileEntityPowerLoom) te).master();
                ItemStack shiftCount = ItemStack.EMPTY;
                if (((TileEntityPowerLoom) te).getFakepos() == 15+1) {
                    for (int i = 0; i < 8; i++) {
                        if (tem.inputHandler.getStackInSlot(i).getCount() == 1) {
                            player.setHeldItem(hand,new ItemStack(tem.inventory.get(i).getItem(),1));
                            tem.inventory.get(i).shrink(1);
                            break;
                        }
                    }
                }
                else if (((TileEntityPowerLoom) te).getFakepos() == 3 || ((TileEntityPowerLoom) te).getFakepos() == 6 || ((TileEntityPowerLoom) te).getFakepos() == 9) {
                    for (int i = 13; i < 16; i++) {
                        if (tem.inputHandler.getStackInSlot(i).getCount() > 0) {
                            int count;
                            if (player.getHeldItem(hand).isEmpty()) {
                                player.setHeldItem(hand,new ItemStack(tem.inventory.get(i).getItem(),tem.inventory.get(i).getCount(),tem.inventory.get(i).getMetadata()));
                                tem.inventory.get(i).shrink(tem.inventory.get(i).getCount());
                            }
                            if (!tem.inventory.get(i).getItem().equals(player.getHeldItem(hand).getItem()) || tem.inventory.get(i).getMetadata() != player.getHeldItem(hand).getMetadata()) { continue; }
                            //TODO: Change this to care about stack size
                            count = Math.min(64,tem.inventory.get(i).getCount()+player.getHeldItem(hand).getCount());
                            player.getHeldItem(hand).grow(count-player.getHeldItem(hand).getCount());
                            tem.inventory.get(i).shrink(count);
                            break;
                        }
                    }
                }
                else if (((TileEntityPowerLoom) te).getFakepos() == 15+4 || ((TileEntityPowerLoom) te).getFakepos() == 15+7 || ((TileEntityPowerLoom) te).getFakepos() == 15+10) {
                    for (int i = 16; i < 17; i++) {
                        if (tem.inputHandler.getStackInSlot(i).getCount() > 0) {
                            player.setHeldItem(hand,new ItemStack(tem.inventory.get(i).getItem(),tem.inventory.get(i).getCount(),tem.inventory.get(i).getMetadata()));
                            tem.inventory.get(i).shrink( tem.inventory.get(i).getCount());
                            break;
                        }
                    }
                } else if (((TileEntityPowerLoom) te).getFakepos() == 5 || ((TileEntityPowerLoom) te).getFakepos() == 8 || ((TileEntityPowerLoom) te).getFakepos() == 11) {
                    for (int i = 8; i < 11; i++) {
                        if (tem.inventory.get(i).getCount() > 0) {
                            int count;
                            if (player.getHeldItem(hand).isEmpty()) {
                                player.setHeldItem(hand,new ItemStack(tem.inventory.get(i).getItem(),tem.inventory.get(i).getCount()));
                                tem.inventory.get(i).shrink(tem.inventory.get(i).getCount());
                            }
                            if (!tem.inventory.get(i).getItem().equals(player.getHeldItem(hand).getItem()) || tem.inventory.get(i).getMetadata() != player.getHeldItem(hand).getMetadata()) { continue; }

                            count = Math.min(64,tem.inventory.get(i).getCount()+player.getHeldItem(hand).getCount());
                            player.getHeldItem(hand).grow(count-player.getHeldItem(hand).getCount());
                            tem.inventory.get(i).shrink(count);
                            break;
                        }
                    }
                } else if (((TileEntityPowerLoom) te).getFakepos() == 0) {
                    for (int i = 11; i < 13; i++) {
                        if (tem.inventory.get(i).getCount() > 0) {
                            int count;
                            if (player.getHeldItem(hand).isEmpty()) {
                                player.setHeldItem(hand,new ItemStack(tem.inventory.get(i).getItem(),tem.inventory.get(i).getCount()));
                                tem.inventory.get(i).shrink(tem.inventory.get(i).getCount());
                            }
                            if (!tem.inventory.get(i).getItem().equals(player.getHeldItem(hand).getItem()) || tem.inventory.get(i).getMetadata() != player.getHeldItem(hand).getMetadata()) { continue; }

                            count = Math.min(64,tem.inventory.get(i).getCount()+player.getHeldItem(hand).getCount());
                            player.getHeldItem(hand).grow(count-player.getHeldItem(hand).getCount());
                            tem.inventory.get(i).shrink(count);
                            break;
                        }
                    }
                }

            }
        }
        return false;

    }

    @Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
    {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof IEBlockInterfaces.INeighbourChangeTile && !tile.getWorld().isRemote)
            ((IEBlockInterfaces.INeighbourChangeTile) tile).onNeighborBlockChange(pos);
    }

    @Override
    public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IEBlockInterfaces.ILightValue)
            return ((IEBlockInterfaces.ILightValue) te).getLightValue();
        return 0;
    }

    public BlockATTTileProvider setHasColours()
    {
        this.hasColours = true;
        return this;
    }

    @Override
    public boolean hasCustomBlockColours()
    {
        return hasColours;
    }

    @Override
    public int getRenderColour(IBlockState state, @Nullable IBlockAccess worldIn, @Nullable BlockPos pos, int tintIndex)
    {
        if (worldIn != null && pos != null)
        {
            TileEntity tile = worldIn.getTileEntity(pos);
            if (tile instanceof IEBlockInterfaces.IColouredTile)
                return ((IEBlockInterfaces.IColouredTile) tile).getRenderColour(tintIndex);
        }
        return 0xffffff;
    }

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess world, BlockPos pos)
    {
        if (world.getBlockState(pos).getBlock() != this)
            return FULL_BLOCK_AABB;
        else
        {
            TileEntity te = world.getTileEntity(pos);
            if (te instanceof IEBlockInterfaces.IBlockBounds)
            {
                float[] bounds = ((IEBlockInterfaces.IBlockBounds) te).getBlockBounds();
                if (bounds != null)
                    return new AxisAlignedBB(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
            }
        }
        return super.getBoundingBox(state, world, pos);
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World world, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list, @Nullable Entity ent, boolean p_185477_7_)
    {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IEBlockInterfaces.IAdvancedCollisionBounds)
        {
            List<AxisAlignedBB> bounds = ((IEBlockInterfaces.IAdvancedCollisionBounds) te).getAdvancedColisionBounds();
            if (bounds != null && !bounds.isEmpty())
            {
                for (AxisAlignedBB aabb : bounds)
                {
                    if (aabb != null && mask.intersects(aabb))
                        list.add(aabb);
                }
                return;
            }
        }
        super.addCollisionBoxToList(state, world, pos, mask, list, ent, p_185477_7_);
    }

    @Override
    public RayTraceResult collisionRayTrace(IBlockState state, World world, BlockPos pos, Vec3d start, Vec3d end)
    {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IEBlockInterfaces.IAdvancedSelectionBounds)
        {
            List<AxisAlignedBB> list = ((IEBlockInterfaces.IAdvancedSelectionBounds) te).getAdvancedSelectionBounds();
            if (list != null && !list.isEmpty())
            {
                for (AxisAlignedBB aabb : list)
                {
                    RayTraceResult mop = this.rayTrace(pos, start, end, aabb.offset(-pos.getX(), -pos.getY(), -pos.getZ()));
                    if (mop != null)
                        return mop;
                }
                return null;
            }
        }
        return super.collisionRayTrace(state, world, pos, start, end);
    }
//	public RayTraceResult doRaytrace(World world, BlockPos pos, Vec3d start, Vec3d end)
//	{
//		start = start.addVector((double)(-pos.getX()), (double)(-pos.getY()), (double)(-pos.getZ()));
//		end = end.addVector((double)(-pos.getX()), (double)(-pos.getY()), (double)(-pos.getZ()));
//		Vec3d vec3 = start.getIntermediateWithXValue(end, this.minX);
//		Vec3d vec31 = start.getIntermediateWithXValue(end, this.maxX);
//		Vec3d vec32 = start.getIntermediateWithYValue(end, this.minY);
//		Vec3d vec33 = start.getIntermediateWithYValue(end, this.maxY);
//		Vec3d vec34 = start.getIntermediateWithZValue(end, this.minZ);
//		Vec3d vec35 = start.getIntermediateWithZValue(end, this.maxZ);
//
//		if(!this.isVecInsideYZBounds(vec3))
//			vec3 = null;
//		if(!this.isVecInsideYZBounds(vec31))
//			vec31 = null;
//		if(!this.isVecInsideXZBounds(vec32))
//			vec32 = null;
//		if(!this.isVecInsideXZBounds(vec33))
//			vec33 = null;
//		if(!this.isVecInsideXYBounds(vec34))
//			vec34 = null;
//		if(!this.isVecInsideXYBounds(vec35))
//			vec35 = null;
//
//		Vec3d vec36 = null;
//
//		if(vec3 != null && (vec36 == null || start.squareDistanceTo(vec3) < start.squareDistanceTo(vec36)))
//			vec36 = vec3;
//		if(vec31 != null && (vec36 == null || start.squareDistanceTo(vec31) < start.squareDistanceTo(vec36)))
//			vec36 = vec31;
//		if(vec32 != null && (vec36 == null || start.squareDistanceTo(vec32) < start.squareDistanceTo(vec36)))
//			vec36 = vec32;
//		if(vec33 != null && (vec36 == null || start.squareDistanceTo(vec33) < start.squareDistanceTo(vec36)))
//			vec36 = vec33;
//		if(vec34 != null && (vec36 == null || start.squareDistanceTo(vec34) < start.squareDistanceTo(vec36)))
//			vec36 = vec34;
//		if(vec35 != null && (vec36 == null || start.squareDistanceTo(vec35) < start.squareDistanceTo(vec36)))
//			vec36 = vec35;
//
//		if (vec36 == null)
//			return null;
//		else
//		{
//			EnumFacing enumfacing = null;
//			if(vec36 == vec3)
//				enumfacing = EnumFacing.WEST;
//			if(vec36 == vec31)
//				enumfacing = EnumFacing.EAST;
//			if(vec36 == vec32)
//				enumfacing = EnumFacing.DOWN;
//			if(vec36 == vec33)
//				enumfacing = EnumFacing.UP;
//			if(vec36 == vec34)
//				enumfacing = EnumFacing.NORTH;
//			if(vec36 == vec35)
//				enumfacing = EnumFacing.SOUTH;
//			return new RayTraceResult(vec36.addVector((double)pos.getX(), (double)pos.getY(), (double)pos.getZ()), enumfacing, pos);
//		}
//	}
//	protected boolean isVecInsideYZBounds(Vec3d point)
//	{
//		return point != null && (point.yCoord >= this.minY && point.yCoord <= this.maxY && point.zCoord >= this.minZ && point.zCoord <= this.maxZ);
//	}
//	protected boolean isVecInsideXZBounds(Vec3d point)
//	{
//		return point != null && (point.xCoord >= this.minX && point.xCoord <= this.maxX && point.zCoord >= this.minZ && point.zCoord <= this.maxZ);
//	}
//	protected boolean isVecInsideXYBounds(Vec3d point)
//	{
//		return point != null && (point.xCoord >= this.minX && point.xCoord <= this.maxX && point.yCoord >= this.minY && point.yCoord <= this.maxY);
//	}


    @Override
    public boolean hasComparatorInputOverride(IBlockState state)
    {
        return true;
    }

    @Override
    public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos)
    {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IEBlockInterfaces.IComparatorOverride)
            return ((IEBlockInterfaces.IComparatorOverride) te).getComparatorInputOverride();
        return 0;
    }


    @Override
    public int getWeakPower(IBlockState blockState, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IEBlockInterfaces.IRedstoneOutput)
            return ((IEBlockInterfaces.IRedstoneOutput) te).getWeakRSOutput(blockState, side);
        return 0;
    }

    @Override
    public int getStrongPower(IBlockState blockState, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IEBlockInterfaces.IRedstoneOutput)
            return ((IEBlockInterfaces.IRedstoneOutput) te).getStrongRSOutput(blockState, side);
        return 0;
    }

    @Override
    public boolean canProvidePower(IBlockState state)
    {
        return true;
    }

    @Override
    public boolean canConnectRedstone(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side)
    {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof IEBlockInterfaces.IRedstoneOutput)
            return ((IEBlockInterfaces.IRedstoneOutput) te).canConnectRedstone(state, side);
        return false;
    }

    //@Override
    public void onEntityCollision(World world, BlockPos pos, IBlockState state, Entity entity)
    {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityIEBase)
            ((TileEntityIEBase) te).onEntityCollision(world, entity);
    }
}
