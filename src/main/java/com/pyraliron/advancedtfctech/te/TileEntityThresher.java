package com.pyraliron.advancedtfctech.te;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.api.crafting.IngredientStack;
import blusunrize.immersiveengineering.common.Config;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.blocks.TileEntityMultiblockPart;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import com.google.common.collect.Lists;
import com.pyraliron.advancedtfctech.crafting.ThresherRecipe;
import com.pyraliron.advancedtfctech.multiblocks.MultiblockThresher;
import com.pyraliron.advancedtfctech.util.Reference;
import com.pyraliron.advancedtfctech.util.inventory.ATTInventoryHandler;
import net.dries007.tfc.api.capability.food.CapabilityFood;
import net.dries007.tfc.api.capability.food.IFood;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Math.min;
import static net.dries007.tfc.api.capability.food.CapabilityFood.updateFoodFromPrevious;

public class TileEntityThresher extends TileEntityMultiblockMetal<TileEntityThresher, IMultiblockRecipe> implements IEBlockInterfaces.IAdvancedSelectionBounds, IEBlockInterfaces.IAdvancedCollisionBounds,IEBlockInterfaces.IGuiTile {
    int fakepos;
    /* uh yeah just for clientside rendering shit */
    public int tick;
    public int maxTicks;
    public boolean isTicking;

    final int[] capabilityList = {7,22};//{10,16};
    public NonNullList<ItemStack> inventory = NonNullList.withSize(12, ItemStack.EMPTY);


    public static class TileEntityThresherParent extends TileEntityThresher
    {
        //AxisAlignedBB aabb;
        @SideOnly(Side.CLIENT)
        @Override
        public AxisAlignedBB getRenderBoundingBox()
        {
                if (facing == EnumFacing.NORTH) {
                    return new AxisAlignedBB(getPos().getX()-2,getPos().getY()-2,getPos().getZ()-3,getPos().getX()+2,getPos().getY()+2,getPos().getZ()+1);
                } else if (facing == EnumFacing.SOUTH) {
                    return new AxisAlignedBB(getPos().getX()-2,getPos().getY()-2,getPos().getZ()-1,getPos().getX()+2,getPos().getY()+2,getPos().getZ()+3);
                } else if (facing == EnumFacing.WEST) {
                    return new AxisAlignedBB(getPos().getX()-3,getPos().getY()-2,getPos().getZ()-2,getPos().getX()+1,getPos().getY()+2,getPos().getZ()+2);
                } else {
                    return new AxisAlignedBB(getPos().getX()-5,getPos().getY()-5,getPos().getZ()-5,getPos().getX()+5,getPos().getY()+5,getPos().getZ()+5);
                }
                //System.out.println(getPos()+" "+facing);
                //return new AxisAlignedBB(getPos().getX()-(facing.getAxis()== EnumFacing.Axis.Z?2:1),getPos().getY(),getPos().getZ()-(facing.getAxis()== EnumFacing.Axis.X?2:1), getPos().getX()+(facing.getAxis()== EnumFacing.Axis.Z?3:2),getPos().getY()+3,getPos().getZ()+(facing.getAxis()== EnumFacing.Axis.X?3:2));
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
            this.getOrigin();
            return super.getMaxRenderDistanceSquared() * Config.IEConfig.increasedTileRenderdistance;
        }
    }

    public TileEntityThresher()
    {
        super(MultiblockThresher.instance, new int[]{3, 3, 3}, 16000, true);

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

        return !isInvalid() && playerIn.getDistanceSq(getPos().add(0.5D, 0.5D, 0.5D)) <= 64D;
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
        //this.processType = nbt.getInteger("processType");
        boolean lastActive = this.wasActive;
        if (!this.wasActive && lastActive) {
            ++this.activeTicks;
        }
        //if(!descPacket)
        inventory = Utils.readInventory(nbt.getTagList("inventory", 10), 12);
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
        this.isTicking = this.processQueue.size() > 0;
        //nbt.setInteger("processType",this.processQueue.size() > 0 ? this.processQueue.get(0).recipe instanceof ThresherRecipe ? ((ThresherRecipe) this.processQueue.get(0).recipe).processType.ordinal(): 0: 0);
        //if(!descPacket)


        nbt.setTag("inventory", Utils.writeInventory(inventory));
    }
    @Override
    public void disassemble()
    {
        if(formed&&!world.isRemote)
        {
            //System.out.println("ENTER");
            BlockPos startPos = getOrigin();
            BlockPos masterPos = getPos().add(-offset[0], -offset[1], -offset[2]);
            long time = world.getTotalWorldTime();
            for(int yy = 0; yy < structureDimensions[0]; yy++)
                for(int ll = 0; ll < structureDimensions[1]; ll++)
                    for(int ww = 0; ww < structureDimensions[2]; ww++)
                    {
                        int w = mirrored?-ww: ww;
                        BlockPos pos = startPos.offset(facing, ll).offset(facing.rotateY(), w).add(0, yy, 0);
                        ItemStack s = ItemStack.EMPTY;
                        //System.out.println(pos);

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
    @Override
    public void update()
    {
        ApiUtils.checkForNeedlessTicking(this);
        tickedProcesses = 0;
        //System.out.println("update ??");
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

                }
            //System.out.println("extra check "+this.additionalCanProcessCheck(process));

            if(process.canProcess(this))
            {
                //this.markContainingBlockForUpdate(null);
                //System.out.println("can process "+process);


                process.doProcessTick(this);
                tickedProcesses++;
                updateMasterBlock(null, true);

                this.markDirty();

                /* magic to make the world go 'round ie. make the client-side and server-side match */
                world.notifyBlockUpdate(getPos(), world.getBlockState(getPos()), world.getBlockState(getPos()), 3);
                world.addBlockEvent(this.getPos(), this.getBlockType(), 255, 0);
            }
            if(process.clearProcess)
                processIterator.remove();
        }

        //System.out.println("is dummy "+this.isDummy()+" rs "+isRSDisabled()+" stored energy "+energyStorage.getEnergyStored());
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
            //System.out.println("process queue size "+this.processQueue.size()+" max "+this.getProcessQueueMaxLength());
            if (this.processQueue.size() < this.getProcessQueueMaxLength())
            {
                if (this.processQueue.size() > 0) {
                    this.wasActive = true;
                    this.activeTicks++;
                }
                //System.out.println("PROCESS QUEUE"+this.processQueue+" "+this.tickedProcesses);
                final int[] usedInvSlots = new int[6];
                //			final int[] usedInvSlots = new int[8];
                for (MultiblockProcess<IMultiblockRecipe> process : processQueue)
                    if (process instanceof MultiblockProcessInMachine)
                        for (int i : ((MultiblockProcessInMachine<IMultiblockRecipe>) process).getInputSlots()) {
                            usedInvSlots[i]++;
                        }

                //			Integer[] preferredSlots = new Integer[]{0,1,2,3,4,5,6,7};
                //			Arrays.sort(preferredSlots, 0,8, new Comparator<Integer>(){
                //				@Override
                //				public int compare(Integer arg0, Integer arg1)
                //				{
                //					return Integer.compare(usedInvSlots[arg0],usedInvSlots[arg1]);
                //				}});
                for (int slot = 0; slot < 6; slot++) {
                    ItemStack stack = this.getInventory().get(slot);
                    if(!stack.isEmpty())
                    {
                        stack = stack.copy();
                        stack.shrink(usedInvSlots[slot]);
                    }

                    //System.out.println("stack "+stack);
                    //				if(stack!=null)
                    //				{
                    //					stack = stack.copy();
                    ////					stack.stackSize-=usedInvSlots[slot];
                    //				}
                    if (!stack.isEmpty() && stack.getCount() > 0 && (!stack.hasCapability(CapabilityFood.CAPABILITY,null) || !stack.getCapability(CapabilityFood.CAPABILITY,null).isRotten())) {
                        ThresherRecipe recipe = ThresherRecipe.findRecipe(stack);

                        if (recipe != null) {
                            TileEntityThresher.MultiblockProcessThresher process = new TileEntityThresher.MultiblockProcessThresher(recipe, slot);


                            if (this.addProcessToQueue((MultiblockProcess) process, true) && this.additionalCanProcessCheck((MultiblockProcess) process)) {

                                this.addProcessToQueue((MultiblockProcess) process, false);
                                //usedInvSlots.add(slot);
                                //							update = true;
                            }
                        }
                    }

                }
            }

            if (world.getTotalWorldTime() % 8 == 0)
            {
                BlockPos outputPos = this.getBlockPosForPos(7).offset(facing, 1);
                if (this.mirrored) {outputPos = this.getBlockPosForPos(7).offset(facing, 1);}
                TileEntity outputTile = Utils.getExistingTileEntity(world, outputPos);
                if (outputTile != null)
                    for (int j = 6; j < 12; j++)
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
        //return new float[]{0, 0, 0, 1, 1, 1};
    }

    @Override
    public List<AxisAlignedBB> getAdvancedSelectionBounds()
    {
        EnumFacing fl = facing;
        EnumFacing fw = facing.rotateY();
        if (mirrored)
            fw = fw.getOpposite();
        float s = 0.0625F;
        int npos = mirrored ? this.pos : this.pos % 3 == 1 ? this.pos : this.pos % 3 == 0 ? this.pos+2 : this.pos-2;
        //System.out.println("xyz of tile: "+x+" "+y+" "+z+" "+this.fakepos);
        if (npos == 7 || npos == 12 || npos == 14 || npos == 22 || npos == 13) {
            return Lists.newArrayList(new AxisAlignedBB(0, 0, 0, 1, 1, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
        } else if (npos < 9) {
            return Lists.newArrayList(new AxisAlignedBB(0, 0, 0, 1, 0.5F, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
        } else if (npos  == 10) {
            List list = Lists.newArrayList();
            float minX = fl == EnumFacing.NORTH ? 2*s : fl == EnumFacing.SOUTH ? 2*s : fl == EnumFacing.WEST ? 0 : 0;
            float maxX = fl == EnumFacing.NORTH ? 14*s : fl == EnumFacing.SOUTH ? 14*s : fl == EnumFacing.WEST ? 1 : 1;
            float minZ = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 2*s : 2*s;
            float maxZ = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 14*s : 14*s;
            list.add(new AxisAlignedBB(minX, 2*s, minZ, maxX, 14*s, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;
        } else if (npos == 9) {
            List list = Lists.newArrayList();
            float minZ = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 9*s : fl == EnumFacing.WEST ? 8*s : 0;
            float maxZ = fl == EnumFacing.NORTH ? 7*s : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 1 : 8*s;
            float minX = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 8*s : fl == EnumFacing.WEST ? 0 : 9*s;
            float maxX = fl == EnumFacing.NORTH ? 8*s : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 7*s : 1;
            list.add(new AxisAlignedBB(minX, 0, minZ, maxX, 14*s, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;
        } else if (npos == 11) {
            List list = Lists.newArrayList();
            float minZ = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 9*s : fl == EnumFacing.WEST ? 0 : 8*s;
            float maxZ = fl == EnumFacing.NORTH ? 7*s : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 8*s : 1;
            float minX = fl == EnumFacing.NORTH ? 8*s : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 0 : 9*s;
            float maxX = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 8*s : fl == EnumFacing.WEST ? 7*s : 1;
            list.add(new AxisAlignedBB(minX, 0, minZ, maxX, 14*s, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;
        } else if (npos == 9+6) {
            List list = Lists.newArrayList();
            float minZ = fl == EnumFacing.NORTH ? 9*s : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 8*s : 0;
            float maxZ = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 7*s : fl == EnumFacing.WEST ? 1 : 8*s;
            float minX = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 8*s : fl == EnumFacing.WEST ? 9*s : 0;
            float maxX = fl == EnumFacing.NORTH ? 8*s : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 1 : 7*s;
            list.add(new AxisAlignedBB(minX, 0, minZ, maxX, 14*s, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;
        } else if (npos == 11+6) {
            List list = Lists.newArrayList();
            float minZ = fl == EnumFacing.NORTH ? 9*s : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 0 : 8*s;
            float maxZ = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 8*s : fl == EnumFacing.WEST ? 8*s : 1;
            float minX = fl == EnumFacing.NORTH ? 8*s : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 9*s : 0;
            float maxX = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 8*s : fl == EnumFacing.WEST ? 1 : 7*s;
            list.add(new AxisAlignedBB(minX, 0, minZ, maxX, 14*s, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;
        } else if (npos == 23) {
            List list = Lists.newArrayList();
            float minX = fl == EnumFacing.NORTH ? 8*s : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 2*s : 2*s;
            float maxX = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 8*s : fl == EnumFacing.WEST ? 14*s : 14*s;
            float minZ = fl == EnumFacing.NORTH ? 2*s : fl == EnumFacing.SOUTH ? 2*s : fl == EnumFacing.WEST ? 0 : 8*s;
            float maxZ = fl == EnumFacing.NORTH ? 14*s : fl == EnumFacing.SOUTH ? 14*s : fl == EnumFacing.WEST ? 8*s : 1;
            list.add(new AxisAlignedBB(minX, 0, minZ, maxX, 7*s, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;
        } else if (npos == 21) {
            List list = Lists.newArrayList();
            float minX = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 8*s : fl == EnumFacing.WEST ? 2*s : 2*s;
            float maxX = fl == EnumFacing.NORTH ? 8*s : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 14*s : 14*s;
            float minZ = fl == EnumFacing.NORTH ? 2*s : fl == EnumFacing.SOUTH ? 2*s : fl == EnumFacing.WEST ? 8*s : 0;
            float maxZ = fl == EnumFacing.NORTH ? 14*s : fl == EnumFacing.SOUTH ? 14*s : fl == EnumFacing.WEST ? 1 : 8*s;
            list.add(new AxisAlignedBB(minX, 0, minZ, maxX, 7*s, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;
        } else if (npos == 16) {
            List list = Lists.newArrayList();
            float minX = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 9*s : 0;
            float maxX = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 1 : 7*s;
            float minZ = fl == EnumFacing.NORTH ? 9*s : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 0 : 0;
            float maxZ = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 7*s : fl == EnumFacing.WEST ? 1 : 1;
            list.add(new AxisAlignedBB(minX, 2*s, minZ, maxX, 14*s, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;
        } else if (npos == 18 || npos == 17 || npos == 19) {
            List list = Lists.newArrayList();
            float minX = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 0 : 8*s;
            float maxX = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 8*s : 1;
            float minZ = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 8*s : fl == EnumFacing.WEST ? 0 : 0;
            float maxZ = fl == EnumFacing.NORTH ? 8*s : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 1 : 1;
            list.add(new AxisAlignedBB(minX, 0, minZ, maxX, 1, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;
        } else if (npos == 18+15 || npos == 17+15) {
            List list = Lists.newArrayList();
            float minX = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 0 : 8*s;
            float maxX = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 8*s : 1;
            float minZ = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 8*s : fl == EnumFacing.WEST ? 0 : 0;
            float maxZ = fl == EnumFacing.NORTH ? 8*s : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 1 : 1;
            list.add(new AxisAlignedBB(minX, 0, minZ, maxX, 8*s, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;
        } else if (npos == 18+10 || npos == 17+10 || npos == 19+10) {
            List list = Lists.newArrayList();
            if (npos == 27) {list.add(new AxisAlignedBB(0, 0, 0, 1, 0.5f, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));}
            float minX = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 8*s : 0;
            float maxX = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 1 : 8*s;
            float minZ = fl == EnumFacing.NORTH ? 8*s : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 0 : 0;
            float maxZ = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 8*s : fl == EnumFacing.WEST ? 1 : 1;
            list.add(new AxisAlignedBB(minX, 0, minZ, maxX, 1, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;
        } else if (npos == 18+15+10 || npos == 17+15+10) {
            List list = Lists.newArrayList();
            float minX = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 8*s : 0;
            float maxX = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 1 : 8*s;
            float minZ = fl == EnumFacing.NORTH ? 8*s : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 0 : 0;
            float maxZ = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 8*s : fl == EnumFacing.WEST ? 1 : 1;
            list.add(new AxisAlignedBB(minX, 0, minZ, maxX, 8*s, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;
        } else if (npos == 15) {
            List list = Lists.newArrayList();
            float minX = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 8*s : fl == EnumFacing.WEST ? 0 : 0;
            float maxX = fl == EnumFacing.NORTH ? 8*s : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 1 : 1;
            float minZ = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 0 : 8*s;
            float maxZ = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 8*s : 1;
            list.add(new AxisAlignedBB(minX, 0, minZ, maxX, 1, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;
        } else if (npos == 1) {
            List list = Lists.newArrayList();
            list.add(new AxisAlignedBB(0, 0, 0, 1, 0.5f, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            float minX = fl == EnumFacing.NORTH ? s : fl == EnumFacing.SOUTH ? s : fl == EnumFacing.WEST ? -2*s : 12*s;
            float maxX = fl == EnumFacing.NORTH ? 15*s : fl == EnumFacing.SOUTH ? 15*s : fl == EnumFacing.WEST ? 4*s : 18*s;
            float minZ = fl == EnumFacing.NORTH ? -2*s : fl == EnumFacing.SOUTH ? 12*s : fl == EnumFacing.WEST ? s : s;
            float maxZ = fl == EnumFacing.NORTH ? 4*s : fl == EnumFacing.SOUTH ? 18*s : fl == EnumFacing.WEST ? 15*s : 15*s;
            list.add(new AxisAlignedBB(minX, 8*s, minZ, maxX, 1, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;
        } else if (npos == 16) {
            List list = Lists.newArrayList();
            float minX = fl == EnumFacing.NORTH ? s : fl == EnumFacing.SOUTH ? s : fl == EnumFacing.WEST ? -2*s : 12*s;
            float maxX = fl == EnumFacing.NORTH ? 15*s : fl == EnumFacing.SOUTH ? 15*s : fl == EnumFacing.WEST ? 4*s : 18*s;
            float minZ = fl == EnumFacing.NORTH ? -2*s : fl == EnumFacing.SOUTH ? 12*s : fl == EnumFacing.WEST ? s : s;
            float maxZ = fl == EnumFacing.NORTH ? 4*s : fl == EnumFacing.SOUTH ? 18*s : fl == EnumFacing.WEST ? 15*s : 15*s;
            list.add(new AxisAlignedBB(minX, 0, minZ, maxX, 1, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;
        } else if (npos == 31) {
            List list = Lists.newArrayList();
            float minX = fl == EnumFacing.NORTH ? s : fl == EnumFacing.SOUTH ? s : fl == EnumFacing.WEST ? -2*s : 12*s;
            float maxX = fl == EnumFacing.NORTH ? 15*s : fl == EnumFacing.SOUTH ? 15*s : fl == EnumFacing.WEST ? 4*s : 18*s;
            float minZ = fl == EnumFacing.NORTH ? -2*s : fl == EnumFacing.SOUTH ? 12*s : fl == EnumFacing.WEST ? s : s;
            float maxZ = fl == EnumFacing.NORTH ? 4*s : fl == EnumFacing.SOUTH ? 18*s : fl == EnumFacing.WEST ? 15*s : 15*s;
            list.add(new AxisAlignedBB(minX, 0, minZ, maxX, 15*s, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;
        }else if (npos == 11) {
            List list = Lists.newArrayList();
            list.add(new AxisAlignedBB(0, 0, 0, 1, 0.5f, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            float minX = fl == EnumFacing.NORTH ? s : fl == EnumFacing.SOUTH ? s : fl == EnumFacing.WEST ? 12*s : -2*s;
            float maxX = fl == EnumFacing.NORTH ? 15*s : fl == EnumFacing.SOUTH ? 15*s : fl == EnumFacing.WEST ? 18*s : 4*s;
            float minZ = fl == EnumFacing.NORTH ? 12*s : fl == EnumFacing.SOUTH ? -2*s : fl == EnumFacing.WEST ? s : s;
            float maxZ = fl == EnumFacing.NORTH ? 18*s : fl == EnumFacing.SOUTH ? 4*s : fl == EnumFacing.WEST ? 15*s : 15*s;
            list.add(new AxisAlignedBB(minX, 8*s, minZ, maxX, 1, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;
        } else if (npos == 26) {
            List list = Lists.newArrayList();
            float minX = fl == EnumFacing.NORTH ? s : fl == EnumFacing.SOUTH ? s : fl == EnumFacing.WEST ? 12*s : -2*s;
            float maxX = fl == EnumFacing.NORTH ? 15*s : fl == EnumFacing.SOUTH ? 15*s : fl == EnumFacing.WEST ? 18*s : 4*s;
            float minZ = fl == EnumFacing.NORTH ? 12*s : fl == EnumFacing.SOUTH ? -2*s : fl == EnumFacing.WEST ? s : s;
            float maxZ = fl == EnumFacing.NORTH ? 18*s : fl == EnumFacing.SOUTH ? 4*s : fl == EnumFacing.WEST ? 15*s : 15*s;
            list.add(new AxisAlignedBB(minX, 0, minZ, maxX, 1, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;
        } else if (npos == 41) {
            List list = Lists.newArrayList();
            float minX = fl == EnumFacing.NORTH ? s : fl == EnumFacing.SOUTH ? s : fl == EnumFacing.WEST ? 12*s : -2*s;
            float maxX = fl == EnumFacing.NORTH ? 15*s : fl == EnumFacing.SOUTH ? 15*s : fl == EnumFacing.WEST ? 18*s : 4*s;
            float minZ = fl == EnumFacing.NORTH ? 12*s : fl == EnumFacing.SOUTH ? -2*s : fl == EnumFacing.WEST ? s : s;
            float maxZ = fl == EnumFacing.NORTH ? 18*s : fl == EnumFacing.SOUTH ? 4*s : fl == EnumFacing.WEST ? 15*s : 15*s;
            list.add(new AxisAlignedBB(minX, 0, minZ, maxX, 15*s, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;
        } else if (npos == 36 || npos == 35) {
            List list = Lists.newArrayList();
            float minX = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 2*s : 2*s;
            float maxX = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 14*s : 14*s;
            float minZ = fl == EnumFacing.NORTH ? 2*s : fl == EnumFacing.SOUTH ? 2*s : fl == EnumFacing.WEST ? 0 : 0;
            float maxZ = fl == EnumFacing.NORTH ? 14*s : fl == EnumFacing.SOUTH ? 14*s : fl == EnumFacing.WEST ? 1 : 1;
            list.add(new AxisAlignedBB(minX, 2*s, minZ, maxX, 14*s, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
            return list;
        } else {
            return Lists.newArrayList(new AxisAlignedBB(0, 0, 0, 1, 0.5F, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
        }
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
        return new int[]{12};
    }

    @Override
    public int[] getRedstonePos()
    {
        return new int[]{14};
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

        this.activeTicks = 0;
        if(process.recipe instanceof ThresherRecipe) //&& !((ThresherRecipe)process.recipe).secondaryOutput.isEmpty())
        {

            //System.out.println("CAN SECONDARY STACK "+ItemHandlerHelper.canItemStacksStack(this.inventory.get(11), ((PowerLoomRecipe)process.recipe).secondaryOutput));
            EnumFacing outputDir = facing.rotateY().rotateY();
            BlockPos outputPos = this.getBlockPosForPos(10).offset(facing, -1);
            if (this.mirrored) {
                //System.out.println("mirror");
                outputPos = this.getBlockPosForPos(10).offset(facing, -1);}
            TileEntity outputTile = Utils.getExistingTileEntity(world, outputPos);
            if (outputTile != null) {
                ItemStack stack = ((ThresherRecipe) process.recipe).secondaryOutput.copy();
                stack = Utils.insertStackIntoInventory(outputTile, stack, facing.getOpposite());
                if (stack.getCount() > 0) {
                    Utils.dropStackAtPos(world, outputPos.add(0,0,0), stack, outputDir);
                }
            } else {
                Utils.dropStackAtPos(world, outputPos.add(0,0,0), ((ThresherRecipe)process.recipe).secondaryOutput, outputDir);
            }

            /*if(this.inventory.get(11).isEmpty())
                this.inventory.set(11, ((PowerLoomRecipe)process.recipe).secondaryOutput.copy());
            else if(ItemHandlerHelper.canItemStacksStack(this.inventory.get(11), ((PowerLoomRecipe)process.recipe).secondaryOutput) && !(inventory.get(11).getCount() + ((PowerLoomRecipe)process.recipe).secondaryOutput.getCount() >getSlotLimit(11)))
                this.inventory.get(11).grow(((PowerLoomRecipe)process.recipe).secondaryOutput.getCount());
            else if(this.inventory.get(12).isEmpty())
                this.inventory.set(12, ((PowerLoomRecipe)process.recipe).secondaryOutput.copy());
            else if(ItemHandlerHelper.canItemStacksStack(this.inventory.get(12), ((PowerLoomRecipe)process.recipe).secondaryOutput) && !(inventory.get(12).getCount() + ((PowerLoomRecipe)process.recipe).secondaryOutput.getCount() >getSlotLimit(12)))
                this.inventory.get(12).grow(((PowerLoomRecipe)process.recipe).secondaryOutput.getCount());
            else {
                Utils.dropStackAtPos(world, pos, ((PowerLoomRecipe)process.recipe).secondaryOutput, facing);
            }*/
        }
        /*if(process.recipe instanceof PowerLoomRecipe) {
            if (((PowerLoomRecipe) process.recipe).secondaryInput.matches(this.inventory.get(13))) {this.inventory.get(13).shrink(((PowerLoomRecipe) process.recipe).secondaryInput.inputSize);}
            else if (((PowerLoomRecipe) process.recipe).secondaryInput.matches(this.inventory.get(14))) {this.inventory.get(14).shrink(((PowerLoomRecipe) process.recipe).secondaryInput.inputSize);}
            else if (((PowerLoomRecipe) process.recipe).secondaryInput.matches(this.inventory.get(15))) {this.inventory.get(15).shrink(((PowerLoomRecipe) process.recipe).secondaryInput.inputSize);}
        }*/
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

    static int[] outputSlots = {6,7,8,9,10,11};
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
            TileEntityThresher master = master();
            if (master==null)
                return null;
            if(pos==7)
                return (T)master.outputHandler;
            else if(pos==22)//pos==(mirrored?6:8))
                return (T)master.inputHandler;
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

    public ATTInventoryHandler inputHandler = new ATTInventoryHandler(6, this, 0, true, false,64)
    {
        //ignore the given slot and spread it out
        @Override
        public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
        {
            //System.out.println("ITEM STACK "+stack.getCount()+" "+stack);
            //System.out.println("SLOT "+slot);
            if (stack.isEmpty())
                return stack;
            else {
                stack = stack.copy();
            }
            List<Integer> possibleSlots = new ArrayList<>(6);
            if (slot < 6) {
                for (int i = 0; i < 6; i++) {
                    ItemStack here = inventory.get(i);

                    if (here.isEmpty()) {
                        if (!simulate)
                            inventory.set(i, stack);
                        return ItemStack.EMPTY;
                    } else if (ItemHandlerHelper.canItemStacksStack(stack, here) && here.getCount() < 1) {
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

    ATTInventoryHandler outputHandler = new ATTInventoryHandler(6, this, 6, false,true,64);


    @Override
    public IMultiblockRecipe findRecipeForInsertion(ItemStack inserting)
    {
        return null;
    }

    @Override
    protected IMultiblockRecipe readRecipeFromNBT(NBTTagCompound tag)
    {

        return ThresherRecipe.loadFromNBT(tag);
    }
    @Override
    protected MultiblockProcess loadProcessFromNBT(NBTTagCompound tag)
    {
        IMultiblockRecipe recipe = readRecipeFromNBT(tag);
        if(recipe!=null && recipe instanceof ThresherRecipe)
            return new TileEntityThresher.MultiblockProcessThresher((ThresherRecipe) recipe, tag.getIntArray("process_inputSlots"));
        return null;
    }

    @Override
    public boolean canOpenGui()
    {
        //System.out.println("is formed to open "+this.formed+" pos "+this.pos);
        //return false;
        return this.formed;// && this.pos == 6;
        //return false;
    }

    @Override
    public int getGuiID()
    {
        return Reference.GUIID_Thresher;
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
        return true;
    }

    @Override
    protected IFluidTank[] getAccessibleFluidTanks(EnumFacing side)
    {
        TileEntityThresher master = this.master();


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
    public TileEntityThresher master()
    {

        if (offset[0] == 0 && offset[1] == 0 && offset[2] == 0)
        {
            return this;
        }
        TileEntity te = world.getTileEntity(getPos().add(-offset[0], -offset[1], -offset[2]));
        //System.out.println("this.getClass().isInstance(te) ? (TileEntityThresher) te : null "+(this.getClass().isInstance(te) ? (TileEntityThresher) te : null));
        return this.getClass().isInstance(te) ? (TileEntityThresher) te : null;
    }

    @Override
    public TileEntityThresher getTileForPos(int targetPos)
    {
        BlockPos target = getBlockPosForPos(targetPos);
        TileEntity tile = world.getTileEntity(target);
        return tile instanceof TileEntityThresher ? (TileEntityThresher) tile : null;
    }
    public static class MultiblockProcessThresher extends MultiblockProcessInMachine<ThresherRecipe>
    {
        public MultiblockProcessThresher(ThresherRecipe recipe, int... inputSlots)
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
            //System.out.println("PROCESS FINISH");
            List<ItemStack> outputs = getRecipeItemOutputs(te);
            if(outputs!=null && !outputs.isEmpty())
            {
                int[] outputSlots = te.getOutputSlots();
                for(ItemStack output : outputs)
                    if(!output.isEmpty())
                        if(outputSlots==null || te.getInventory()==null)
                            te.doProcessOutput(output.copy());
                        else
                        {
                            for(int iOutputSlot:outputSlots)
                            {
                                ItemStack s = te.getInventory().get(iOutputSlot);
                                ItemStack src = te.getInventory().get(this.inputSlots[0]);
                                ItemStack out = output.copy();
                                if(s.isEmpty())
                                {
                                    if (out.hasCapability(CapabilityFood.CAPABILITY,null) && src.hasCapability(CapabilityFood.CAPABILITY,null)) {
                                        //IFood outcap = out.getCapability(CapabilityFood.CAPABILITY,null);
                                        //IFood srccap = src.getCapability(CapabilityFood.CAPABILITY,null);
                                        //outcap.setCreationDate(srccap.getCreationDate());
                                        updateFoodFromPrevious(src,out);
                                    }
                                    te.getInventory().set(iOutputSlot, out);
                                    break;
                                }
                                //System.out.println((s.getCount() + output.getCount())+" "+te.getSlotLimit(iOutputSlot)+" "+te.getInventory().get(iOutputSlot).getItem()+" "+output.getItem());
                                if (s.getCount() + output.getCount() <= te.getSlotLimit(iOutputSlot) && te.getInventory().get(iOutputSlot).getItem() == output.getItem() &&
                                        s.hasCapability(CapabilityFood.CAPABILITY,null) && src.hasCapability(CapabilityFood.CAPABILITY,null)
                                        && out.hasCapability(CapabilityFood.CAPABILITY,null) && CapabilityFood.areStacksStackableExceptCreationDate(s,out)) {
                                    updateFoodFromPrevious(src,out);
                                    IFood destcap = s.getCapability(CapabilityFood.CAPABILITY,null);
                                    //IFood srccap = src.getCapability(CapabilityFood.CAPABILITY,null);
                                    IFood outcap = out.getCapability(CapabilityFood.CAPABILITY,null);

                                    //System.out.println(destcap.getCreationDate()+" "+outcap.getCreationDate());
                                    if (Math.abs(destcap.getCreationDate() - outcap.getCreationDate()) < 24000) {
                                        te.getInventory().get(iOutputSlot).grow(output.getCount());
                                        break;
                                    }
                                } else if (s.getCount() + output.getCount() <= te.getSlotLimit(iOutputSlot) && te.getInventory().get(iOutputSlot).getItem() == output.getItem()) {
                                    te.getInventory().get(iOutputSlot).grow(output.getCount());
                                    break;
                                }
                                /*if(ItemHandlerHelper.canItemStacksStack(s, output) && s.getCount() + output.getCount() <= te.getSlotLimit(iOutputSlot))
                                {
                                    te.getInventory().get(iOutputSlot).grow(output.getCount());
                                    break;
                                }*/
                            }
                        }
            }
            NonNullList<ItemStack> inv = te.getInventory();
            List<IngredientStack> itemInputList = this.getRecipeItemInputs(te);
            if(inv != null && this.inputSlots != null && itemInputList != null)
            {
                Iterator<IngredientStack> iterator = new ArrayList(itemInputList).iterator();
                while(iterator.hasNext())
                {
                    IngredientStack ingr = iterator.next();
                    int ingrSize = ingr.inputSize;
                    for(int slot : this.inputSlots)
                        if(!inv.get(slot).isEmpty() && ingr.matchesItemStackIgnoringSize(inv.get(slot)))
                        {
                            int taken = Math.min(inv.get(slot).getCount(), ingrSize);
                            inv.get(slot).shrink(taken);
                            if(inv.get(slot).getCount() <= 0)
                                inv.set(slot, ItemStack.EMPTY);
                            if((ingrSize -= taken) <= 0)
                                break;
                        }
                }
            }
            te.onProcessFinish(this);
            this.clearProcess = true;
            //TODO: Figure out what this event means
            te.getWorld().addBlockEvent(te.getPos(), te.getBlockType(), 0,40);
        }
    }
}
