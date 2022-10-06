package com.pyraliron.advancedtfctech.te;

import blusunrize.immersiveengineering.api.ApiUtils;
import blusunrize.immersiveengineering.api.crafting.IMultiblockRecipe;
import blusunrize.immersiveengineering.common.Config;
import blusunrize.immersiveengineering.common.blocks.IEBlockInterfaces;
import blusunrize.immersiveengineering.common.blocks.metal.TileEntityMultiblockMetal;
import blusunrize.immersiveengineering.common.util.Utils;
import com.google.common.collect.Lists;
import com.pyraliron.advancedtfctech.crafting.DoughMixerRecipe;
import com.pyraliron.advancedtfctech.multiblocks.MultiblockDoughMixer;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Math.min;

public class TileEntityDoughMixer extends TileEntityMultiblockMetal<TileEntityDoughMixer, IMultiblockRecipe> implements IEBlockInterfaces.IAdvancedSelectionBounds, IEBlockInterfaces.IAdvancedCollisionBounds,IEBlockInterfaces.IGuiTile {
    /* uh yeah just for clientside rendering shit */
    public int tick;
    public int maxTicks;
    public boolean isTicking;

    final int[] capabilityList = {};
    public FluidTank tank = new FluidTank(12000);
    public NonNullList<ItemStack> inventory = NonNullList.withSize(12, ItemStack.EMPTY);

    public static class TileEntityDoughMixerParent extends TileEntityDoughMixer
    {
        @SideOnly(Side.CLIENT)
        @Override
        public AxisAlignedBB getRenderBoundingBox()
        {
            return new AxisAlignedBB(getPos().getX()-(facing.getAxis()== EnumFacing.Axis.Z?2:1),getPos().getY(),getPos().getZ()-(facing.getAxis()== EnumFacing.Axis.X?2:1), getPos().getX()+(facing.getAxis()== EnumFacing.Axis.Z?3:2),getPos().getY()+3,getPos().getZ()+(facing.getAxis()== EnumFacing.Axis.X?3:2));
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

    public TileEntityDoughMixer()
    {
        super(MultiblockDoughMixer.instance, new int[]{3, 2, 3}, 16000, true);

    }
    public int[] getStructureDimensions() {
        return this.structureDimensions;
    }

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
        tank.readFromNBT(nbt.getCompoundTag("tank"));
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


        nbt.setTag("inventory", Utils.writeInventory(inventory));
        NBTTagCompound tankTag = tank.writeToNBT(new NBTTagCompound());
        nbt.setTag("tank", tankTag);
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
                this.wasActive = true;
                this.activeTicks++;
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
                    if (!stack.isEmpty() && stack.getCount() > 0) {
                        DoughMixerRecipe recipe = DoughMixerRecipe.findRecipe(stack, tank.getFluid());

                        if (recipe != null) {
                            TileEntityDoughMixer.MultiblockProcessDoughMixer process = new TileEntityDoughMixer.MultiblockProcessDoughMixer(recipe, slot);


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
                BlockPos outputPos = this.getBlockPosForPos(2).offset(facing,-1);
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
        if (this.pos == 2 || this.pos == 7 || this.pos == 18 || this.pos == 4 || this.pos == 30 || this.pos == 22) {
            return Lists.newArrayList(new AxisAlignedBB(0, 0, 0, 1, 1, 1).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
        } else if (this.pos == 14) {
            List list = Lists.newArrayList();
            float minX = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 0 : fl == EnumFacing.WEST ? 0 : 8*s;
            float maxX = fl == EnumFacing.NORTH ? 1 : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 8*s : 1;
            float minZ = fl == EnumFacing.NORTH ? 0 : fl == EnumFacing.SOUTH ? 8*s : fl == EnumFacing.WEST ? 0 : 0;
            float maxZ = fl == EnumFacing.NORTH ? 8*s : fl == EnumFacing.SOUTH ? 1 : fl == EnumFacing.WEST ? 1 : 1;
            list.add(new AxisAlignedBB(minX, 0, minZ, maxX, 1, maxZ).offset(getPos().getX(), getPos().getY(), getPos().getZ()));
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
        return new int[]{13};
    }

    @Override
    public int[] getRedstonePos()
    {
        return new int[]{7};
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


    }

    @Override
    public int getMaxProcessPerTick()
    {
        return 6;
    }

    @Override
    public int getProcessQueueMaxLength()
    {
        return 6;
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
            TileEntityDoughMixer master = master();
            if (master==null)
                return null;
            if(pos==2)
                return (T)master.outputHandler;
            else if(pos==30)//pos==(mirrored?6:8))
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

        return DoughMixerRecipe.loadFromNBT(tag);
    }
    @Override
    protected MultiblockProcess loadProcessFromNBT(NBTTagCompound tag)
    {
        IMultiblockRecipe recipe = readRecipeFromNBT(tag);
        if(recipe!=null && recipe instanceof DoughMixerRecipe)
            return new TileEntityDoughMixer.MultiblockProcessDoughMixer((DoughMixerRecipe) recipe, tag.getIntArray("process_inputSlots"));
        return null;
    }

    @Override
    public boolean canOpenGui()
    {
        //System.out.println("is formed to open "+this.formed+" pos "+this.pos);
        return this.formed;// && this.pos == 6;
        //return false;
    }

    @Override
    public int getGuiID()
    {
        return Reference.GUIID_DoughMixer;
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
        return new IFluidTank[]{tank};
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
        TileEntityDoughMixer master = this.master();


        if (master != null)
        {
            if (pos == 4 && (side == facing.rotateY().rotateY()))
            {
                return new FluidTank[]{tank};
            }
        }

        return new FluidTank[0];
    }

    @Override
    protected boolean canFillTankFrom(int iTank, EnumFacing side, FluidStack resource)
    {
        System.out.println("filling tank from "+side+" "+iTank+" "+resource);
        return false;
    }

    @Override
    protected boolean canDrainTankFrom(int iTank, EnumFacing side)
    {
        System.out.println("draining tank from "+side+" "+iTank);
        return false;
    }

    @Override
    public boolean isDummy()
    {
        return true;
    }

    @Override
    public TileEntityDoughMixer master()
    {

        if (offset[0] == 0 && offset[1] == 0 && offset[2] == 0)
        {
            return this;
        }
        TileEntity te = world.getTileEntity(getPos().add(-offset[0], -offset[1], -offset[2]));
        return this.getClass().isInstance(te) ? (TileEntityDoughMixer) te : null;
    }

    @Override
    public TileEntityDoughMixer getTileForPos(int targetPos)
    {
        BlockPos target = getBlockPosForPos(targetPos);
        TileEntity tile = world.getTileEntity(target);
        return tile instanceof TileEntityDoughMixer ? (TileEntityDoughMixer) tile : null;
    }
    public static class MultiblockProcessDoughMixer extends MultiblockProcessInMachine<DoughMixerRecipe>
    {
        public MultiblockProcessDoughMixer(DoughMixerRecipe recipe, int... inputSlots)
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
