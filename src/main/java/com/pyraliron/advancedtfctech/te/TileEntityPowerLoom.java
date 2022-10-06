/*
 * Large potions of this code were taken from Immersive Petroleum created by Flaxbeard
 * https://github.com/Flaxbeard/ImmersivePetroleum/
 * as well as from Immersive Engineering created by BluSunrize
 * https://github.com/BluSunrize/ImmersiveEngineering/
 */
package com.pyraliron.advancedtfctech.te;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.common.Config;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedCollisionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IAdvancedSelectionBounds;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces.IGuiTile;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import com.google.common.collect.Lists;
import com.pyraliron.advancedtfctech.client.gui.ContainerPowerLoom;
import com.pyraliron.advancedtfctech.crafting.PowerLoomRecipe;
import com.pyraliron.advancedtfctech.multiblocks.MultiblockPowerLoom;
import com.pyraliron.advancedtfctech.util.Reference;
import com.pyraliron.advancedtfctech.util.inventory.ATTInventoryHandler;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.*;
import java.util.stream.IntStream;

import static java.lang.Math.min;

public class TileEntityPowerLoom extends TileEntityMultiblockMetal<TileEntityPowerLoom, IMultiblockRecipe> implements IAdvancedSelectionBounds, IAdvancedCollisionBounds, IGuiTile
{

    int fakepos;
    /* uh yeah just for clientside rendering shit */
    public int tick;
    public int maxTicks;
    public boolean isTicking;
    public int processType;

    // Visual
    public int pirnRotation;

    final int[] capabilityList = {1,16,3,6,9};
    public ContainerPowerLoom container;
    public NonNullList<ItemStack> inventory = NonNullList.withSize(17, ItemStack.EMPTY);


    public static class TileEntityPowerLoomParent extends TileEntityPowerLoom
    {
        @SideOnly(Side.CLIENT)
        @Override
        public AxisAlignedBB getRenderBoundingBox()
        {
            //System.out.println(this.getPos());
            BlockPos nullPos = this.getPos();

            //System.out.println(getPos());
            if (facing == EnumFacing.NORTH) {
                return new AxisAlignedBB(getPos().getX()-2,getPos().getY()-2,getPos().getZ()-5,getPos().getX()+2,getPos().getY()+2,getPos().getZ()+1);
            } else if (facing == EnumFacing.SOUTH) {
                return new AxisAlignedBB(getPos().getX()-2,getPos().getY()-2,getPos().getZ()-1,getPos().getX()+2,getPos().getY()+2,getPos().getZ()+5);
            } else if (facing == EnumFacing.WEST) {
                return new AxisAlignedBB(getPos().getX()-5,getPos().getY()-2,getPos().getZ()-2,getPos().getX()+1,getPos().getY()+2,getPos().getZ()+2);
            } else {
                return new AxisAlignedBB(getPos().getX()-1,getPos().getY()-2,getPos().getZ()-2,getPos().getX()+5,getPos().getY()+2,getPos().getZ()+2);
            }
            //System.out.println(facing+" "+aabb);
            //return new AxisAlignedBB(nullPos.offset(facing, -2).offset(mirrored ? facing.rotateYCCW() : facing.rotateY(), -1).down(1), nullPos.offset(facing, 5).offset(mirrored ? facing.rotateYCCW() : facing.rotateY(), 2).up(3));
        }

        @Override
        public boolean isDummy()
        {
            return false;
        }

        @Override
        @SideOnly(Side.CLIENT)
        public double getMaxRenderDistanceSquared()
        {
            return super.getMaxRenderDistanceSquared() * Config.IEConfig.increasedTileRenderdistance;
        }
    }

    public TileEntityPowerLoom()
    {
        super(MultiblockPowerLoom.instance, new int[]{3, 5, 3}, 16000, true);

    }
    public int[] getStructureDimensions() {
        return this.structureDimensions;
    }
    public void setPos(long lng) {/*this.pos.fromLong(lng);*/this.fakepos = (int)lng;}

    public boolean wasActive = false;
    public int activeTicks = 0;
    public IBlockState state = null;


    public boolean canInteractWith(EntityPlayer playerIn) {
        // If we are too far away from this tile entity you cannot use it

        return !isInvalid() && playerIn.getDistanceSq(this.getPos().add(0.5D, 0.5D, 0.5D)) <= 64D;
    }

    public String getUniqueName() {
        return "ATT:PowerLoom";
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing)
    {
        if((IntStream.of(capabilityList).anyMatch(x -> x == pos)) && capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
            return master()!=null;
        return super.hasCapability(capability, facing);
    }

    /*@Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound nbtTag = new NBTTagCompound();
        this.writeCustomNBT();
        int[] inv = new int[17];
        int[] invTypes = new int[17];

        for (int i = 0; i < 17; i++) {
            inv[i] = this.inventory.get(i).getCount();
            if (i < 8) {
                if (this.inventory.get(i).getItem().equals(ModItems.FIBER_WINDED_PIRN)) {invTypes[i] = 0;}
                else if (this.inventory.get(i).getItem().equals(ModItems.WOOL_WINDED_PIRN)) {invTypes[i] = 1;}
                else {invTypes[i] = 2;}
            } else if (i < 11) {
                if (this.inventory.get(i).getItem().equals(ItemsTFC.BURLAP_CLOTH)) {invTypes[i] = 0;}
                else if (this.inventory.get(i).getItem().equals(ItemsTFC.WOOL_CLOTH)) {invTypes[i] = 1;}
                else {invTypes[i] = 2;}
            } else if (i < 13) {
                invTypes[i] = 0;
            } else if (i < 16) {
                if (this.inventory.get(i).getItem().equals(ItemsTFC.JUTE_FIBER || )) {invTypes[i] = 0;}
                else if (this.inventory.get(i).getItem().equals(ItemsTFC.WOOL_CLOTH)) {invTypes[i] = 1;}
                else {invTypes[i] = 2;}
            } else {
                invTypes[i] = 0;
            }
        }
        //System.out.println(" INITIAL INV "+inv+" "+inv[0]);
        nbtTag.setIntArray("inventory",inv);
        nbtTag.setIntArray("inventory_types",invTypes);
        nbtTag.setInteger("tick",this.processQueue.size() > 0 ? this.processQueue.get(0).processTick : 0);
        nbtTag.setBoolean("isTicking",this.processQueue.size() > 0);
        return new SPacketUpdateTileEntity(getPos(),-1,nbtTag);
    }
    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        NBTTagCompound tag = pkt.getNbtCompound();
        int[] inv = tag.getIntArray("inventory");
        //System.out.println(" PACKET INV "+inv.toString()+" "+inv[0]);
        int[] invTypes = tag.getIntArray("inventory_types");
        for (int i = 0; i < 17; i++) {
            if (i < 8) {
                this.inventory.set(i,new ItemStack(invTypes[i] == 0 ? ModItems.FIBER_WINDED_PIRN : invTypes[i] == 1 ? ModItems.WOOL_WINDED_PIRN : ModItems.SILK_WINDED_PIRN,inv[i]));
            }
        }
    }*/

    @Override
    public void readCustomNBT(NBTTagCompound nbt, boolean descPacket)
    {

        super.readCustomNBT(nbt, descPacket);
        this.wasActive = nbt.getBoolean("wasActive");
        this.activeTicks = nbt.getInteger("activeTicks");
        this.tick = nbt.getInteger("tick");
        this.isTicking = nbt.getBoolean("isTicking2");
        this.maxTicks = nbt.getInteger("maxTicks");
        this.processType = nbt.getInteger("processType");
        this.pirnRotation = nbt.getInteger("pirnRotation");
        boolean lastActive = this.wasActive;
        if (!this.wasActive && lastActive) {
            ++this.activeTicks;
        }
        //if(!descPacket)
        inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 17);
        //System.out.println(inventory);
    }

    @Override
    public void writeCustomNBT(NBTTagCompound nbt, boolean descPacket)
    {
        super.writeCustomNBT(nbt, descPacket);
        nbt.setBoolean("wasActive", wasActive);
        nbt.setInteger("activeTicks",activeTicks);
        nbt.setInteger("tick",this.processQueue.size() > 0 ? this.processQueue.get(0).processTick : 0);
        nbt.setInteger("maxTicks",this.processQueue.size() > 0 ? this.processQueue.get(0).maxTicks : 100);
        nbt.setBoolean("isTicking2",this.processQueue.size() > 0);
        nbt.setInteger("pirnRotation",this.pirnRotation);
        this.isTicking = this.processQueue.size() > 0;
        nbt.setInteger("processType",this.processQueue.size() > 0 ? this.processQueue.get(0).recipe instanceof PowerLoomRecipe ? ((PowerLoomRecipe) this.processQueue.get(0).recipe).processType.ordinal(): 0: 0);
        //if(!descPacket)


        nbt.setTag("inventory", Utils.writeInventory(inventory));
    }

    @Override
    public void update()
    {
        ApiUtils.checkForNeedlessTicking(this);
        tickedProcesses = 0;
        if(world.isRemote || isDummy() || isRSDisabled())
            return;

        int max = getMaxProcessPerTick();
        int ii = 0;
        Iterator<MultiblockProcess<IMultiblockRecipe>> processIterator = processQueue.iterator();
        tickedProcesses = 0;
        //System.out.println("PROCESS QUEUE "+this.processQueue+" "+this.processQueue.size());
        this.markContainingBlockForUpdate(null);
        while(processIterator.hasNext() && ii++<max)
        {
            MultiblockProcess<IMultiblockRecipe> process = processIterator.next();
            /*System.out.println("PROCESS"+process+" "+process.canProcess(this));
            System.out.println(process.recipe.getItemInputs().toArray());
            System.out.println(this.energyStorage.extractEnergy(process.energyPerTick, true)+" "+process.energyPerTick);
            System.out.println(process.recipe.getItemOutputs().toArray());
            System.out.println(this.getOutputSlots());*/
            int[] outputSlots = this.getOutputSlots();
            List<ItemStack> outputs = process.recipe.getItemOutputs();
            for(ItemStack output : outputs)
                if(!output.isEmpty())
                {
                    boolean canOutput = false;
                    if(outputSlots==null)
                        canOutput = true;
                    else
                    {
                        for(int iOutputSlot : outputSlots)
                        {
                            ItemStack s = this.getInventory().get(iOutputSlot);
                            if(s.isEmpty() || (ItemHandlerHelper.canItemStacksStack(s, output) && s.getCount() + output.getCount() <= this.getSlotLimit(iOutputSlot)))
                            {
                                canOutput = true;
                                break;
                            }
                        }
                    }
                    //if(!canOutput)
                        //System.out.println("CAN'T OUTPUT");
                }
                //System.out.println("extra check "+this.additionalCanProcessCheck(process));

            if(process.canProcess(this))
            {
                //this.markContainingBlockForUpdate(null);



                process.doProcessTick(this);
                tickedProcesses++;
                updateMasterBlock(null, true);

                this.markDirty();

                /* magic to make the world go 'round ie. make the client-side and server-side match */
                world.notifyBlockUpdate(this.getPos(), world.getBlockState(this.getPos()), world.getBlockState(this.getPos()), 3);
                world.addBlockEvent(this.getPos(), this.getBlockType(), 255, 0);
            }
            if(process.clearProcess)
                processIterator.remove();
        }

        if (isDummy())
            return;

        else if (!isRSDisabled() && energyStorage.getEnergyStored() > 0)
        {

            /*if (this.tickedProcesses > 0)
                for (int i = 23; i < 26; i++)
                    if (this.inventory.get(i).attemptDamageItem(1, Utils.RAND, null))
                    {
                        this.inventory.set(i, ItemStack.EMPTY);
                        //						updateClient = true;
                        //						update = true;
                    }
                    */
            this.wasActive = false;
            if (this.processQueue.size() < this.getProcessQueueMaxLength())
            {
                this.wasActive = true;
                this.activeTicks++;
                //System.out.println("PROCESS QUEUE"+this.processQueue+" "+this.tickedProcesses);
                Set<Integer> usedInvSlots = new HashSet<Integer>();
                //			final int[] usedInvSlots = new int[8];
                for (MultiblockProcess<IMultiblockRecipe> process : processQueue)
                    if (process instanceof MultiblockProcessInMachine)
                        for (int i : ((MultiblockProcessInMachine<IMultiblockRecipe>) process).getInputSlots()) {
                            usedInvSlots.add(i);
                        }

                //			Integer[] preferredSlots = new Integer[]{0,1,2,3,4,5,6,7};
                //			Arrays.sort(preferredSlots, 0,8, new Comparator<Integer>(){
                //				@Override
                //				public int compare(Integer arg0, Integer arg1)
                //				{
                //					return Integer.compare(usedInvSlots[arg0],usedInvSlots[arg1]);
                //				}});
                ItemStack secondary = this.getInventory().get(16);
                for (int slot = 0; slot < 8; slot++)
                    if (!usedInvSlots.contains(slot))
                    {
                        ItemStack stack = this.getInventory().get(slot);
                        //				if(stack!=null)
                        //				{
                        //					stack = stack.copy();
                        ////					stack.stackSize-=usedInvSlots[slot];
                        //				}
                        if (!stack.isEmpty() && !secondary.isEmpty() && stack.getCount() > 0 && secondary.getCount() >= 16)
                        {
                            PowerLoomRecipe recipe = PowerLoomRecipe.findRecipe(stack,secondary);

                            if (recipe != null)
                            {
                                MultiblockProcessPowerLoom process = new MultiblockProcessPowerLoom(recipe, slot);


                                if (this.addProcessToQueue((MultiblockProcess) process, true) && this.additionalCanProcessCheck((MultiblockProcess)process))
                                {
                                    this.addProcessToQueue((MultiblockProcess) process, false);
                                    usedInvSlots.add(slot);
                                    //							update = true;
                                }
                            }
                        }
                    }
            }

            if (world.getTotalWorldTime() % 8 == 0)
            {
                BlockPos outputPos = this.getBlockPosForPos(8).offset(facing.rotateY(), 1);
                if (this.mirrored) {outputPos = this.getBlockPosForPos(8).offset(facing.rotateY(), -1);}
                TileEntity outputTile = Utils.getExistingTileEntity(world, outputPos);
                if (outputTile != null)
                    for (int j = 8; j < 11; j++)
                        if (!inventory.get(j).isEmpty())
                        {
                            ItemStack stack = Utils.copyStackWithAmount(inventory.get(j), 1);
                            stack = Utils.insertStackIntoInventory(outputTile, stack, facing.getOpposite());
                            if (stack.isEmpty())
                            {
                                this.inventory.get(j).shrink(1);
                                if (this.inventory.get(j).getCount() <= 0)
                                    this.inventory.set(j, ItemStack.EMPTY);
                            }
                        }
                outputPos = this.getBlockPosForPos(0).offset(facing.rotateY(),-1);
                if (this.mirrored) {outputPos = this.getBlockPosForPos(0).offset(facing.rotateY(), 1);}
                outputTile = Utils.getExistingTileEntity(world, outputPos);
                if (outputTile != null)
                    for (int j = 11; j < 13; j++)
                        if (!inventory.get(j).isEmpty())
                        {
                            ItemStack stack = Utils.copyStackWithAmount(inventory.get(j), 1);
                            stack = Utils.insertStackIntoInventory(outputTile, stack, facing.getOpposite());
                            if (stack.isEmpty())
                            {
                                this.inventory.get(j).shrink(1);
                                if (this.inventory.get(j).getCount() <= 0)
                                    this.inventory.set(j, ItemStack.EMPTY);
                            }
                        }
            }
        }
    }

    @Override
    public float[] getBlockBounds()
    {
		/*if(pos==19)
			return new float[]{facing==EnumFacing.WEST?.5f:0,0,facing==EnumFacing.NORTH?.5f:0, facing==EnumFacing.EAST?.5f:1,1,facing==EnumFacing.SOUTH?.5f:1};
		if(pos==17)
			return new float[]{.0625f,0,.0625f, .9375f,1,.9375f};*/

        return new float[]{0, 0, 0, 0, 0, 0};
    }

    @Override
    public List<AxisAlignedBB> getAdvancedSelectionBounds()
    {
        EnumFacing fl = facing;
        EnumFacing fw = facing.rotateY();
        if (mirrored)
            fw = fw.getOpposite();
        float s = 0.0625F;
        //System.out.println("xyz of tile: "+x+" "+y+" "+z+" "+this.fakepos);
        if (this.pos < 2 || this.pos == 10 || this.pos == 13 || this.pos == 28 || this.pos == 4 || this.pos == 7) {
            return Lists.newArrayList(new AxisAlignedBB(0, 0, 0, 1, 1, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
        } else if (this.pos == 5)
        {

            List list = Lists.newArrayList(new AxisAlignedBB(0, 0, 0, 1, .25f, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            if (this.mirrored) {
                float minX = fl == EnumFacing.NORTH ? 8*s : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 1 - 5 * s : s;
                float maxX = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 8*s : fl == EnumFacing.WEST ? 1 - s : 5 * s;
                float minZ = fl == EnumFacing.NORTH ? 1 - 5 * s : fl == EnumFacing.SOUTH ? s : fl == EnumFacing.WEST ? 8*s : 0;
                float maxZ = fl == EnumFacing.NORTH ? 1 - s : fl == EnumFacing.SOUTH ? 5 * s : fl == EnumFacing.WEST ? 1 : 8*s;
                list.add(new AxisAlignedBB(minX, 4 * s, minZ, maxX, 1, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));

                minX = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 8*s : fl == EnumFacing.WEST ? 1 - 6 * s : 0;
                maxX = fl == EnumFacing.NORTH ? 8*s : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 1 : 6 * s;
                minZ = fl == EnumFacing.NORTH ? 1 - 6 * s : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 0 : 8*s;
                maxZ = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 6 * s : fl == EnumFacing.WEST ? 8*s : 1;
                list.add(new AxisAlignedBB(minX, 4 * s, minZ, maxX, 1, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            } else {
                float minX = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 8 * s : fl == EnumFacing.WEST ? 1 - 5 * s : s;
                float maxX = fl == EnumFacing.NORTH ? 8 * s : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 1 - s : 5 * s;
                float minZ = fl == EnumFacing.NORTH ? 1 - 5 * s : fl == EnumFacing.SOUTH ? s : fl == EnumFacing.WEST ? 0 : 8 * s;
                float maxZ = fl == EnumFacing.NORTH ? 1 - s : fl == EnumFacing.SOUTH ? 5 * s : fl == EnumFacing.WEST ? 8 * s : 1;
                list.add(new AxisAlignedBB(minX, 4 * s, minZ, maxX, 1, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));

                minX = fl == EnumFacing.NORTH ? 8 * s : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 1 - 6 * s : 0;
                maxX = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 8 * s : fl == EnumFacing.WEST ? 1 : 6 * s;
                minZ = fl == EnumFacing.NORTH ? 1 - 6 * s : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 8 * s : 0;
                maxZ = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 6 * s : fl == EnumFacing.WEST ? 1 : 8 * s;
                list.add(new AxisAlignedBB(minX, 4 * s, minZ, maxX, 1, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            }
            return list;
        } else if (this.pos == 5+15)
        {
            //fl = this.mirrored ? facing.getOpposite() : facing;
            List list = Lists.newArrayList();
            if (!mirrored) {
                float minX = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 6 * s : fl == EnumFacing.WEST ? 1 - 5 * s : s;
                float maxX = fl == EnumFacing.NORTH ? 10 * s : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 1 - s : 5 * s;
                float minZ = fl == EnumFacing.NORTH ? 1 - 5 * s : fl == EnumFacing.SOUTH ? s : fl == EnumFacing.WEST ? 0 : 6 * s;
                float maxZ = fl == EnumFacing.NORTH ? 1 - s : fl == EnumFacing.SOUTH ? 5 * s : fl == EnumFacing.WEST ? 10 * s : 1;
                list.add(new AxisAlignedBB(minX, 0, minZ, maxX, 12 * s, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));

                minX = fl == EnumFacing.NORTH ? 8 * s : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 1 - 6 * s : 0;
                maxX = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 8 * s : fl == EnumFacing.WEST ? 1 : 6 * s;
                minZ = fl == EnumFacing.NORTH ? 1 - 6 * s : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 8 * s : 0;
                maxZ = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 6 * s : fl == EnumFacing.WEST ? 1 : 8 * s;
                list.add(new AxisAlignedBB(minX, 0, minZ, maxX, 4 * s, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            } else {
                float minX = fl == EnumFacing.NORTH ? 6*s : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 1 - 5 * s : s;
                float maxX = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 10*s : fl == EnumFacing.WEST ? 1 - s : 5 * s;
                float minZ = fl == EnumFacing.NORTH ? 1 - 5 * s : fl == EnumFacing.SOUTH ? s : fl == EnumFacing.WEST ? 6*s  : 0;
                float maxZ = fl == EnumFacing.NORTH ? 1 - s : fl == EnumFacing.SOUTH ? 5 * s : fl == EnumFacing.WEST ? 1 : 10*s;
                list.add(new AxisAlignedBB(minX, 0, minZ, maxX, 12 * s, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));

                minX = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 8*s : fl == EnumFacing.WEST ? 1 - 6 * s : 0;
                maxX = fl == EnumFacing.NORTH ? 8*s : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 1 : 6 * s;
                minZ = fl == EnumFacing.NORTH ? 1 - 6 * s : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 0 : 8*s;
                maxZ = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 6 * s : fl == EnumFacing.WEST ? 8*s : 1;
                list.add(new AxisAlignedBB(minX, 0, minZ, maxX, 4 * s, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            }
            return list;
        } else if (this.pos == 11) {
            List list = Lists.newArrayList(new AxisAlignedBB(0, 0, 0, 1, .25f, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            if (mirrored) {
                float minX = fl == EnumFacing.NORTH ? 8*s : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? s : 1 - 5 * s;
                float maxX = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 8*s : fl == EnumFacing.WEST ? 5 * s : 1 - s;
                float minZ = fl == EnumFacing.NORTH ? s : fl == EnumFacing.SOUTH ? 1 - 5 * s : fl == EnumFacing.WEST ? 8*s : 0;
                float maxZ = fl == EnumFacing.NORTH ? 5 * s : fl == EnumFacing.SOUTH ? 1 - s : fl == EnumFacing.WEST ? 1 : 8*s;
                list.add(new AxisAlignedBB(minX, 4 * s, minZ, maxX, 1, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));

                minX = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 8*s : fl == EnumFacing.WEST ? 0 : 1 - 6 * s;
                maxX = fl == EnumFacing.NORTH ? 8*s : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 6 * s : 1;
                minZ = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 1 - 6 * s : fl == EnumFacing.WEST ? 0 : 8*s;
                maxZ = fl == EnumFacing.NORTH ? 6 * s : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 8*s : 1;
                list.add(new AxisAlignedBB(minX, 4 * s, minZ, maxX, 1, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            } else {
                float minX = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 8*s : fl == EnumFacing.WEST ? s : 1 - 5 * s;
                float maxX = fl == EnumFacing.NORTH ? 8*s : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 5 * s : 1 - s;
                float minZ = fl == EnumFacing.NORTH ? s : fl == EnumFacing.SOUTH ? 1 - 5 * s : fl == EnumFacing.WEST ? 0 : 8 * s;
                float maxZ = fl == EnumFacing.NORTH ? 5 * s : fl == EnumFacing.SOUTH ? 1 - s : fl == EnumFacing.WEST ? 8 * s : 1;
                list.add(new AxisAlignedBB(minX, 4 * s, minZ, maxX, 1, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));

                minX = fl == EnumFacing.NORTH ? 8 * s : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 0 : 1 - 6 * s;
                maxX = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 8 * s : fl == EnumFacing.WEST ? 6 * s : 1;
                minZ = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 1 - 6 * s : fl == EnumFacing.WEST ? 8 * s : 0;
                maxZ = fl == EnumFacing.NORTH ? 6 * s : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 1 : 8 * s;
                list.add(new AxisAlignedBB(minX, 4 * s, minZ, maxX, 1, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            }
            return list;
        } else if (this.pos == 11+15) {
            List list = Lists.newArrayList();
            float minX = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 6*s : fl == EnumFacing.WEST ? s : 1-5*s;
            float maxX = fl == EnumFacing.NORTH ? 10*s : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 5*s : 1-s;
            float minZ = fl == EnumFacing.NORTH ? s : fl == EnumFacing.SOUTH ? 1-5*s : fl == EnumFacing.WEST ? 0 : 6*s;
            float maxZ = fl == EnumFacing.NORTH ? 5*s : fl == EnumFacing.SOUTH ? 1-s : fl == EnumFacing.WEST ? 10*s : 1;
            list.add(new AxisAlignedBB(minX, 0, minZ, maxX, 12*s, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));

            minX = fl == EnumFacing.NORTH ? 8*s : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 0 : 1-6*s;
            maxX = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 8*s : fl == EnumFacing.WEST ? 6*s : 1;
            minZ = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 1-6*s : fl == EnumFacing.WEST ? 8*s : 0;
            maxZ = fl == EnumFacing.NORTH ? 6*s : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 1 : 8*s;
            list.add(new AxisAlignedBB(minX, 0, minZ, maxX, 4*s, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;
        }else if (this.pos == 16) {
            List list = Lists.newArrayList();
            if (mirrored) {
                float minX = fl == EnumFacing.NORTH ? s : fl == EnumFacing.SOUTH ? 3*s : fl == EnumFacing.WEST ? 0F : 7 * s;
                float maxX = fl == EnumFacing.NORTH ? 13*s : fl == EnumFacing.SOUTH ? 1-s : fl == EnumFacing.WEST ? 9 * s : 1;
                float minZ = fl == EnumFacing.NORTH ? 0F : fl == EnumFacing.SOUTH ? 7 * s : fl == EnumFacing.WEST ? s : 3*s;
                float maxZ = fl == EnumFacing.NORTH ? 9 * s : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 13*s : 1-s;
                list.add(new AxisAlignedBB(minX, 0, minZ, maxX, 1, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            }
            else {
                float minX = fl == EnumFacing.NORTH ? 3 * s : fl == EnumFacing.SOUTH ? s : fl == EnumFacing.WEST ? 0F : 7 * s;
                float maxX = fl == EnumFacing.NORTH ? 1 - s : fl == EnumFacing.SOUTH ? 13 * s : fl == EnumFacing.WEST ? 9 * s : 1;
                float minZ = fl == EnumFacing.NORTH ? 0F : fl == EnumFacing.SOUTH ? 7 * s : fl == EnumFacing.WEST ? 3 * s : s;
                float maxZ = fl == EnumFacing.NORTH ? 9 * s : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 1 - s : 13 * s;
                list.add(new AxisAlignedBB(minX, 0, minZ, maxX, 1, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            }
            return list;
        } else if (this.pos == 19+15 || this.pos == 22+15 || this.pos == 25+15) {
            fl = this.mirrored ? facing.getOpposite() : facing;
            List list = Lists.newArrayList();
            float minX = fl == EnumFacing.NORTH ? 7.5F*s : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 0F : 0F;
            float maxX = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 8.5F*s : fl == EnumFacing.WEST ? 1 : 1;
            float minZ = fl == EnumFacing.NORTH ? 0F : fl == EnumFacing.SOUTH ? 0F : fl == EnumFacing.WEST ? 7.5F*s : 0;
            float maxZ = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 1 : 8.5F*s;
            list.add(new AxisAlignedBB(minX, 0, minZ, maxX, 1-s, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;
        } else if (this.pos == 19 || this.pos == 22 || this.pos == 25) {
            fl = this.mirrored ? facing.getOpposite() : facing;
            List list = Lists.newArrayList(new AxisAlignedBB(0, 0, 0, 1, .25f, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            float minX = fl == EnumFacing.NORTH ? 7.5F*s : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 0F : 0F;
            float maxX = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 8.5F*s : fl == EnumFacing.WEST ? 1 : 1;
            float minZ = fl == EnumFacing.NORTH ? 0F : fl == EnumFacing.SOUTH ? 0F : fl == EnumFacing.WEST ? 7.5F*s : 0;
            float maxZ = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 1 : 8.5F*s;
            list.add(new AxisAlignedBB(minX, 0, minZ, maxX, 1-s, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;
        } else if (this.pos == 19+14 || this.pos == 22+14 || this.pos == 25+14) {
            fl = this.mirrored ? facing.getOpposite() : facing;
            List list = Lists.newArrayList();
            float minX = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 10.5F*s : fl == EnumFacing.WEST ? 0F : 0F;
            float maxX = fl == EnumFacing.NORTH ? 5.5F*s : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 1 : 1;
            float minZ = fl == EnumFacing.NORTH ? 0F : fl == EnumFacing.SOUTH ? 0F : fl == EnumFacing.WEST ? 0 : 10.5F*s;
            float maxZ = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 5.5F*s : 1;
            list.add(new AxisAlignedBB(minX, 0, minZ, maxX, 1-s, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;
        } else {
            return Lists.newArrayList(new AxisAlignedBB(0, 0, 0, 1, 0.25F, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
        }
        /*if ((pos >= 3 && pos <= 14 && pos != 10 && pos != 13 && pos != 11 && pos != 9) || pos == 1)
        {
            return Lists.newArrayList(new AxisAlignedBB(0, 0, 0, 1, .5f, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
        }
        else if (pos == 13)
        {
            float minX = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? 0F : .3125F;
            float maxX = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? 1F : .685F;
            float minZ = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? 0F : .3125F;
            float maxZ = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? 1F : .685F;
            List list = Lists.newArrayList(new AxisAlignedBB(minX, 0.5, minZ, maxX, .5 + 3 / 8F, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            list.add(new AxisAlignedBB(0, 0, 0, 1, .5f, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;

        }
        else if (pos == 10)
        {
            float minX = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? .3125F : ((fl == EnumFacing.EAST) ? 11F / 16F : 0F / 16F);
            float maxX = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? .685F : ((fl == EnumFacing.EAST) ? 16F / 16F : 5F / 16F);
            float minZ = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? .3125F : ((fl == EnumFacing.SOUTH) ? 11F / 16F : 0F / 16F);
            float maxZ = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? .685F : ((fl == EnumFacing.SOUTH) ? 16F / 16F : 5F / 16F);
            List list = Lists.newArrayList(new AxisAlignedBB(minX, 0.5, minZ, maxX, .5 + 3 / 8F, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));

            minX = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? 0F : 5F / 16F;
            maxX = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? 1F : 11F / 16F;
            minZ = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? 0F : 5F / 16F;
            maxZ = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? 1F : 11F / 16F;
            list.add(new AxisAlignedBB(minX, 0.5, minZ, maxX, .5 + 3 / 8F, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));

            list.add(new AxisAlignedBB(0, 0, 0, 1, .5f, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;

        }
        else if (pos == 16)
        {
            return Lists.newArrayList(new AxisAlignedBB(3 / 16F, 0, 3 / 16F, 13 / 16F, 1, 13 / 16F).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
        }
        else if (pos == 22)
        {
            float minX = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? 0F : .25F;
            float maxX = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? 1F : .75F;
            float minZ = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? 0F : .25F;
            float maxZ = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? 1F : .75F;
            return Lists.newArrayList(new AxisAlignedBB(minX, -0.75, minZ, maxX, 1, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
        }
        else if (pos == 40)
        {
            float minX = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? 0F : .25F;
            float maxX = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? 1F : .75F;
            float minZ = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? 0F : .25F;
            float maxZ = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? 1F : .75F;
            return Lists.newArrayList(new AxisAlignedBB(minX, 0, minZ, maxX, 0.25, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
        }
        else if (pos == 27)
        {
            fl = this.mirrored ? facing.getOpposite() : facing;
            float minX = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? 0.7F : ((fl == EnumFacing.NORTH) ? 0.6F : -.1F);
            float maxX = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? 1.4F : ((fl == EnumFacing.NORTH) ? 1.1F : 0.4F);
            float minZ = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? 0.7F : ((fl == EnumFacing.EAST) ? .6F : -.1F);
            float maxZ = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? 1.4F : ((fl == EnumFacing.EAST) ? 1.1F : 0.4F);
            List list = Lists.newArrayList(new AxisAlignedBB(minX, -0.5F, minZ, maxX, 1, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));

            minX = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? -.4F : ((fl == EnumFacing.NORTH) ? 0.6F : -.1F);
            maxX = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? .3F : ((fl == EnumFacing.NORTH) ? 1.1F : 0.4F);
            minZ = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? -.4F : ((fl == EnumFacing.EAST) ? .6F : -.1F);
            maxZ = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? .3F : ((fl == EnumFacing.EAST) ? 1.1F : 0.4F);
            list.add(new AxisAlignedBB(minX, -0.5F, minZ, maxX, 1, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;
        }
        else if (pos == 29)
        {
            fl = this.mirrored ? facing.getOpposite() : facing;
            float minX = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? 0.7F : ((fl == EnumFacing.SOUTH) ? 0.6F : -.1F);
            float maxX = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? 1.4F : ((fl == EnumFacing.SOUTH) ? 1.1F : 0.4F);
            float minZ = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? 0.7F : ((fl == EnumFacing.WEST) ? .6F : -.1F);
            float maxZ = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? 1.4F : ((fl == EnumFacing.WEST) ? 1.1F : 0.4F);
            List list = Lists.newArrayList(new AxisAlignedBB(minX, -0.5F, minZ, maxX, 1, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));

            minX = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? -.4F : ((fl == EnumFacing.SOUTH) ? 0.6F : -.1F);
            maxX = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? .3F : ((fl == EnumFacing.SOUTH) ? 1.1F : 0.4F);
            minZ = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? -.4F : ((fl == EnumFacing.WEST) ? .6F : -.1F);
            maxZ = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? .3F : ((fl == EnumFacing.WEST) ? 1.1F : 0.4F);
            list.add(new AxisAlignedBB(minX, -0.5F, minZ, maxX, 1, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;
        }
        else if (pos == 45)
        {
            fl = this.mirrored ? facing.getOpposite() : facing;
            float minX = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? 0F : ((fl == EnumFacing.NORTH) ? 0.8F : -0.2F);
            float maxX = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? 1F : ((fl == EnumFacing.NORTH) ? 1.2F : 0.2F);
            float minZ = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? 0F : ((fl == EnumFacing.EAST) ? 0.8F : -0.2F);
            float maxZ = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? 1 : ((fl == EnumFacing.EAST) ? 1.2F : 0.2F);
            return Lists.newArrayList(new AxisAlignedBB(minX, 0F, minZ, maxX, 1, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
        }
        else if (pos == 47)
        {
            fl = this.mirrored ? facing.getOpposite() : facing;
            float minX = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? 0F : ((fl == EnumFacing.NORTH) ? -0.2F : 0.8F);
            float maxX = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? 1F : ((fl == EnumFacing.NORTH) ? 0.2F : 1.2F);
            float minZ = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? 0F : ((fl == EnumFacing.EAST) ? -0.2F : 0.8F);
            float maxZ = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? 1 : ((fl == EnumFacing.EAST) ? 0.2F : 1.2F);
            return Lists.newArrayList(new AxisAlignedBB(minX, 0F, minZ, maxX, 1, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
        }
        else if (pos == 63 || pos == 65)
        {
            return new ArrayList();
        }
        else if (pos == 58 || pos == 61 || pos == 64 || pos == 67)
        {
            float minX = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? 0F : 0.25F;
            float maxX = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? 1F : 0.75F;
            float minZ = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? 0F : 0.25F;
            float maxZ = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? 1 : 0.75F;
            return Lists.newArrayList(new AxisAlignedBB(minX, -.25F, minZ, maxX, 0.75F, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
        }
        else if (pos == 70)
        {
            float minX = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? 0F : 0.125F;
            float maxX = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? 1F : 0.875F;
            float minZ = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? 0F : 0.125F;
            float maxZ = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? 1 : 0.875F;
            return Lists.newArrayList(new AxisAlignedBB(minX, 0, minZ, maxX, 1.25F, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
        }
        else if (pos == 52)
        {
            float minX = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? 0F : 0.125F;
            float maxX = (fl == EnumFacing.EAST || fl == EnumFacing.WEST) ? 1F : 0.875F;
            float minZ = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? 0F : 0.125F;
            float maxZ = (fl == EnumFacing.NORTH || fl == EnumFacing.SOUTH) ? 1 : 0.875F;
            return Lists.newArrayList(new AxisAlignedBB(minX, .25F, minZ, maxX, 1F, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
        }e

        List list = new ArrayList<AxisAlignedBB>();
        list.add(new AxisAlignedBB(0, 0, 0, 1, 1, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
        return list;*/
    }

    @Override
    public boolean isOverrideBox(AxisAlignedBB box, EntityPlayer player, RayTraceResult mop, ArrayList<AxisAlignedBB> list)
    {
        return false;
    }

    @Override
    public List<AxisAlignedBB> getAdvancedColisionBounds()
    {
        List list = new ArrayList<AxisAlignedBB>();
        return getAdvancedSelectionBounds();
    }

    @Override
    public int[] getEnergyPos()
    {
        return new int[]{15+3*4+1};
    }

    @Override
    public int[] getRedstonePos()
    {
        return new int[]{1};
    }

    @Override
    public boolean isInWorldProcessingMachine()
    {
        return false;
    }

    @Override
    public void doProcessOutput(ItemStack output)
    {
        //System.out.println("POWER LOOM PROCESS OUTPUT "+this.tickedProcesses);
    }

    @Override
    public void doProcessFluidOutput(FluidStack output)
    {
    }

    @Override
    public void onProcessFinish(MultiblockProcess<IMultiblockRecipe> process)
    {
        //System.out.println("POWER LOOM PROCESS FINISH");
        this.activeTicks = 0;
        this.pirnRotation = (this.pirnRotation+1)%8;
        if(process.recipe instanceof PowerLoomRecipe && !((PowerLoomRecipe)process.recipe).secondaryOutput.isEmpty())
        {
            //System.out.println("CAN SECONDARY STACK "+ItemHandlerHelper.canItemStacksStack(this.inventory.get(11), ((PowerLoomRecipe)process.recipe).secondaryOutput));
            if(this.inventory.get(11).isEmpty())
                this.inventory.set(11, ((PowerLoomRecipe)process.recipe).secondaryOutput.copy());
            else if(ItemHandlerHelper.canItemStacksStack(this.inventory.get(11), ((PowerLoomRecipe)process.recipe).secondaryOutput) && !(inventory.get(11).getCount() + ((PowerLoomRecipe)process.recipe).secondaryOutput.getCount() >getSlotLimit(11)))
                this.inventory.get(11).grow(((PowerLoomRecipe)process.recipe).secondaryOutput.getCount());
            else if(this.inventory.get(12).isEmpty())
                this.inventory.set(12, ((PowerLoomRecipe)process.recipe).secondaryOutput.copy());
            else if(ItemHandlerHelper.canItemStacksStack(this.inventory.get(12), ((PowerLoomRecipe)process.recipe).secondaryOutput) && !(inventory.get(12).getCount() + ((PowerLoomRecipe)process.recipe).secondaryOutput.getCount() >getSlotLimit(12)))
                this.inventory.get(12).grow(((PowerLoomRecipe)process.recipe).secondaryOutput.getCount());
            else {
                Utils.dropStackAtPos(world, getPos(), ((PowerLoomRecipe)process.recipe).secondaryOutput, facing);
            }
        }
        if(process.recipe instanceof PowerLoomRecipe) {
            if (((PowerLoomRecipe) process.recipe).secondaryInput.matches(this.inventory.get(13))) {this.inventory.get(13).shrink(((PowerLoomRecipe) process.recipe).secondaryInput.inputSize);}
            else if (((PowerLoomRecipe) process.recipe).secondaryInput.matches(this.inventory.get(14))) {this.inventory.get(14).shrink(((PowerLoomRecipe) process.recipe).secondaryInput.inputSize);}
            else if (((PowerLoomRecipe) process.recipe).secondaryInput.matches(this.inventory.get(15))) {this.inventory.get(15).shrink(((PowerLoomRecipe) process.recipe).secondaryInput.inputSize);}
        }
        world.addBlockEvent(this.getPos(), this.getBlockType(), 255, 0);

    }

    @Override
    public int getMaxProcessPerTick()
    {
        return 1;
    }

    @Override
    public int getProcessQueueMaxLength()
    {
        return 1;
    }

    @Override
    public float getMinProcessDistance(MultiblockProcess<IMultiblockRecipe> process)
    {
        return 0;
    }

    @Override
    public boolean isStackValid(int slot, ItemStack stack)
    {
        return true;
    }

    @Override
    public int getSlotLimit(int slot)
    {
        return 64;
    }

    static int[] outputSlots = {8,9,10};
    @Override
    public int[] getOutputSlots()
    {
        return outputSlots;
    }

    @Override
    public int[] getOutputTanks()
    {
        return null;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing)
    {
        if(capability==CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
        {
            TileEntityPowerLoom master = master();
            if (master==null)
                return null;
            if(pos==1)
                return (T)master.outputHandler;
            else if(pos==16)//pos==(mirrored?6:8))
                return (T)master.inputHandler;
            else if (pos==3||pos==6||pos==9)
                return (T)master.secondaryInputHandler;
        }
        return super.getCapability(capability, facing);
    }

    public int getFakepos() {
        return this.pos;
    }
    @Override
    public void doGraphicalUpdates(int slot)
    {
        this.markDirty();
        this.markContainingBlockForUpdate(null);
    }

    public ATTInventoryHandler inputHandler = new ATTInventoryHandler(8, this, 0, true, false,1)
    {
        //ignore the given slot and spread it out
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
        {
            //System.out.println("ITEM STACK "+stack.getCount()+" "+stack);
            //System.out.println("SLOT "+slot);
            if (stack.isEmpty())
                return stack;
            if (slot == 16) {
                stack = new ItemStack(stack.getItem(),min(stack.getCount(),16),stack.getMetadata());
            }
            else {
                stack = stack.copy();
            }
            List<Integer> possibleSlots = new ArrayList<>(8);
            if (slot < 8) {
                for (int i = 0; i < 8; i++) {
                    ItemStack here = inventory.get(i);
                    for (int j = 0; j < 8; j++) {
                        if (!inventory.get(j).isEmpty() && !inventory.get(j).getItem().equals(stack.getItem())) {
                            return stack;
                        }
                    }
                    for (int j = 13; j < 17; j++) {
                        if (!PowerLoomRecipe.inputMatchesSecondary(stack,inventory.get(j),true)) {
                            return stack;
                        }
                    }
                    if (here.isEmpty()) {
                        if (!simulate)
                            inventory.set(i, stack);
                        return ItemStack.EMPTY;
                    } else if (ItemHandlerHelper.canItemStacksStack(stack, here) && here.getCount() < 1) {
                        possibleSlots.add(i);
                    }
                }
            }
            else if (slot >= 13 && slot < 16) {
                for (int i = 13; i < 16; i++) {
                    ItemStack here = inventory.get(i);
                    for (int j = 0; j < 8; j++) {
                        if (!PowerLoomRecipe.inputMatchesSecondary(inventory.get(j),stack,true)) {
                            return stack;
                        }
                    }
                    for (int j = 13; j < 17; j++) {
                        if (!inventory.get(j).isEmpty() && (!inventory.get(j).getItem().equals(stack.getItem()) || inventory.get(j).getMetadata() != stack.getMetadata() )) {
                            return stack;
                        }
                    }
                    if (here.isEmpty()) {
                        if (!simulate)
                            inventory.set(i, stack);
                        return ItemStack.EMPTY;
                    } else if (ItemHandlerHelper.canItemStacksStack(stack, here) && here.getCount() < 64) {
                        possibleSlots.add(i);
                    }
                }
            }
            else if (slot >= 16 && slot < 17) {
                for (int i = 16; i < 17; i++) {
                    ItemStack here = inventory.get(i);
                    for (int j = 0; j < 8; j++) {
                        if (!PowerLoomRecipe.inputMatchesSecondary(inventory.get(j),stack,true)) {
                            //System.out.println("fail 0-8");
                            return stack;
                        }
                    }
                    for (int j = 13; j < 17; j++) {
                        //System.out.println("inv slot "+j+" "+inventory.get(j)+" "+inventory.get(j).isEmpty()+" "+inventory.get(j).getCount()+" meta: "+inventory.get(j).getMetadata()+" "+stack.getMetadata());
                        if (!inventory.get(j).isEmpty() && (!inventory.get(j).getItem().equals(stack.getItem()) || inventory.get(j).getMetadata() != stack.getMetadata())) {
                            //System.out.println("fail 13-17 "+j);
                            return stack;
                        }
                    }
                    if (here.isEmpty()) {
                        if (!simulate)
                            inventory.set(i, stack);
                        return ItemStack.EMPTY;
                    } else if (ItemHandlerHelper.canItemStacksStack(stack, here) && here.getCount() < 16) {
                        possibleSlots.add(i);
                    }
                }
            }


            //System.out.println("POSSIBLE SLOTS "+possibleSlots.toArray());
            Collections.sort(possibleSlots, (a, b) -> Integer.compare(inventory.get(a).getCount(), inventory.get(b).getCount()));
            for (int i : possibleSlots)
            {
                ItemStack here = inventory.get(i);

                int fillCount = min(here.getMaxStackSize() - here.getCount(), stack.getCount());
                if (!simulate)
                    here.grow(fillCount);
                stack.shrink(fillCount);
                //System.out.println("STACK SHIFT "+i);
                if (stack.isEmpty())
                    return ItemStack.EMPTY;
            }
            return stack;
        }
    };

    ATTInventoryHandler outputHandler = new ATTInventoryHandler(5, this, 8, false,true,64);
    ATTInventoryHandler secondaryInputHandler = new ATTInventoryHandler(4, this, 13, true,false,64) {
        //ignore the given slot and spread it out
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
        {
            slot += 13;
            //System.out.println("ITEM STACK "+stack.getCount()+" "+stack);
            //System.out.println("SLOT "+slot);
            if (stack.isEmpty())
                return stack;
            if (slot == 16) {
                stack = new ItemStack(stack.getItem(),min(stack.getCount(),16),stack.getMetadata());
            }
            else {
                stack = stack.copy();
            }
            List<Integer> possibleSlots = new ArrayList<>(8);
            if (slot >= 13 && slot < 16) {
                for (int i = 13; i < 16; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (!PowerLoomRecipe.inputMatchesSecondary(inventory.get(j),stack,true)) {
                            return stack;
                        }
                    }
                    ItemStack here = inventory.get(i);
                    for (int j = 13; j < 17; j++) {
                        if (!inventory.get(j).isEmpty() && (!inventory.get(j).getItem().equals(stack.getItem()) || inventory.get(j).getMetadata() != stack.getMetadata())) {
                            return stack;
                        }
                    }
                    if (here.isEmpty()) {
                        if (!simulate)
                            inventory.set(i, stack);
                        return ItemStack.EMPTY;
                    } else if (ItemHandlerHelper.canItemStacksStack(stack, here) && here.getCount() < 64) {
                        possibleSlots.add(i);
                    }
                }
            }
            else if (slot >= 16 && slot < 17) {
                for (int i = 16; i < 17; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (!PowerLoomRecipe.inputMatchesSecondary(inventory.get(j),stack,true)) {
                            //System.out.println("fail");
                            return stack;
                        }
                    }
                    ItemStack here = inventory.get(i);
                    for (int j = 13; j < 17; j++) {
                        if (!inventory.get(j).isEmpty() && (!inventory.get(j).getItem().equals(stack.getItem()) || inventory.get(j).getMetadata() != stack.getMetadata())) {
                            return stack;
                        }
                    }
                    if (here.isEmpty()) {
                        if (!simulate)
                            inventory.set(i, stack);
                        return ItemStack.EMPTY;
                    } else if (ItemHandlerHelper.canItemStacksStack(stack, here) && here.getCount() < 16) {
                        possibleSlots.add(i);
                    }
                }
            }


            //System.out.println("POSSIBLE SLOTS "+possibleSlots.toArray());
            Collections.sort(possibleSlots, (a, b) -> Integer.compare(inventory.get(a).getCount(), inventory.get(b).getCount()));
            for (int i : possibleSlots)
            {
                ItemStack here = inventory.get(i);

                int fillCount = min(here.getMaxStackSize() - here.getCount(), stack.getCount());
                if (!simulate)
                    here.grow(fillCount);
                stack.shrink(fillCount);
                //System.out.println("STACK SHIFT "+i);
                if (stack.isEmpty())
                    return ItemStack.EMPTY;
            }
            return stack;
        }
    };

    @Override
    public IMultiblockRecipe findRecipeForInsertion(ItemStack inserting)
    {
        return null;
    }

    @Override
    protected IMultiblockRecipe readRecipeFromNBT(NBTTagCompound tag)
    {

        return PowerLoomRecipe.loadFromNBT(tag);
    }
    @Override
    protected MultiblockProcess loadProcessFromNBT(NBTTagCompound tag)
    {
        IMultiblockRecipe recipe = readRecipeFromNBT(tag);
        if(recipe!=null && recipe instanceof PowerLoomRecipe)
            return new MultiblockProcessPowerLoom((PowerLoomRecipe)recipe, tag.getIntArray("process_inputSlots"));
        return null;
    }

    @Override
    public boolean canOpenGui()
    {
        /* you can uncomment this to be able to see inside the loom for debugging purposes, maybe. but the gui is ugly. */
        //return this.formed && this.pos == 6;
        return false;
    }

    @Override
    public int getGuiID()
    {
        return Reference.GUIID_PowerLoom;
    }

    @Override
    public TileEntity getGuiMaster()
    {
        return master();
    }

    @Override
    public NonNullList<ItemStack> getInventory()
    {
        return this.inventory;
    }

    @Override
    public IFluidTank[] getInternalTanks()
    {
        return null;
    }

    @Override
    public boolean additionalCanProcessCheck(MultiblockProcess<IMultiblockRecipe> process)
    {
        //System.out.println("ADDITIONAL CHECK "+process.recipe);
        /*if(process.recipe!=null)
        {
            if(this.inventory.get(9).getCount()+this.inventory.get(8).getCount()+this.inventory.get(10).getCount() > 64*3)
                return true;
            return false;
        }*/
        /*if (process.recipe instanceof PowerLoomRecipe) {
            System.out.println("primer match "+((PowerLoomRecipe) process.recipe).secondaryInput.matches(new ItemStack(this.inventory.get(16).getItem(),64,this.inventory.get(16).getMetadata())));
            System.out.println("count match 16 "+(this.inventory.get(16).getCount() >= 16));
            System.out.println("inv 13 14 15 "+this.inventory.get(13)+" "+this.inventory.get(14)+" "+this.inventory.get(15)+" "+((PowerLoomRecipe) process.recipe).secondaryInput.getExampleStack());
            System.out.println("rest match "+(((PowerLoomRecipe) process.recipe).secondaryInput.matches(this.inventory.get(13)) ||
                    ((PowerLoomRecipe) process.recipe).secondaryInput.matches(this.inventory.get(14)) ||
                    ((PowerLoomRecipe) process.recipe).secondaryInput.matches(this.inventory.get(15))));
        }*/
        if (process.recipe instanceof PowerLoomRecipe && ((PowerLoomRecipe) process.recipe).secondaryInput.matches(new ItemStack(this.inventory.get(16).getItem(),64,this.inventory.get(16).getMetadata())) &&
                this.inventory.get(16).getCount() >= 16 &&
                (((PowerLoomRecipe) process.recipe).secondaryInput.matches(this.inventory.get(13)) ||
                ((PowerLoomRecipe) process.recipe).secondaryInput.matches(this.inventory.get(14)) ||
                ((PowerLoomRecipe) process.recipe).secondaryInput.matches(this.inventory.get(15)))) {
            //System.out.println("SUCCESS ADDITIONAL CHECK");
            return true;
        }
        return false;
    }

    @Override
    protected IFluidTank[] getAccessibleFluidTanks(EnumFacing side)
    {
        TileEntityPowerLoom master = this.master();


        if (master != null)
        {
            if (pos == 9 && (side == null || side == facing.rotateY() || side == facing.getOpposite().rotateY()))
            {
                return new FluidTank[0];
                //return new FluidTank[]{fakeTank};
            }
            else if (pos == 11 && (side == null || side == facing.rotateY() || side == facing.getOpposite().rotateY()))
            {
                return new FluidTank[0];
                //return new FluidTank[]{fakeTank};
            }
            else if (pos == 16 /*&& IPConfig.Extraction.req_pipes*/ && (side == null || side == EnumFacing.DOWN))
            {
                return new FluidTank[0];
                //return new FluidTank[]{fakeTank};
            }
        }

        return new FluidTank[0];
    }

    @Override
    protected boolean canFillTankFrom(int iTank, EnumFacing side, FluidStack resource)
    {
        return false;
    }

    @Override
    protected boolean canDrainTankFrom(int iTank, EnumFacing side)
    {
        return false;
    }

    @Override
    public boolean isDummy()
    {
        return true;
    }

    @Override
    public TileEntityPowerLoom master()
    {

        if (offset[0] == 0 && offset[1] == 0 && offset[2] == 0)
        {
            return this;
        }
        TileEntity te = world.getTileEntity(getPos().add(-offset[0], -offset[1], -offset[2]));

        return this.getClass().isInstance(te) ? (TileEntityPowerLoom) te : null;
    }

    @Override
    public TileEntityPowerLoom getTileForPos(int targetPos)
    {
        BlockPos target = getBlockPosForPos(targetPos);
        TileEntity tile = world.getTileEntity(target);
        return tile instanceof TileEntityPowerLoom ? (TileEntityPowerLoom) tile : null;
    }
    public static class MultiblockProcessPowerLoom extends MultiblockProcessInMachine<PowerLoomRecipe>
    {
        public MultiblockProcessPowerLoom(PowerLoomRecipe recipe, int... inputSlots)
        {
            super(recipe, inputSlots);
        }

        @Override
        protected NonNullList<ItemStack> getRecipeItemOutputs(TileEntityMultiblockMetal multiblock)
        {
            ItemStack input = multiblock.getInventory().get(this.inputSlots[0]);

            return recipe.getOutputs(input);
        }

        @Override
        protected void writeExtraDataToNBT(NBTTagCompound nbt)
        {
            super.writeExtraDataToNBT(nbt);
        }

        @Override
        protected void processFinish(TileEntityMultiblockMetal te)
        {
            super.processFinish(te);
            te.getWorld().addBlockEvent(te.getPos(), te.getBlockType(), 0,40);
        }
    }

}
