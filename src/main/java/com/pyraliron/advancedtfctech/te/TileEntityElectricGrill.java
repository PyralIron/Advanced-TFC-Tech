package com.pyraliron.advancedtfctech.te;

import net.dries007.tfc.objects.te.TEFirePit;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class TileEntityElectricGrill extends TEFirePit {
    public TileEntityElectricGrill() {
        super();
//        super.super(null);
//        this.inventory = null;
//        this.get
    }
    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing)
    {
        if (this.getCapability(capability,facing) != null) {
            return true;
        }
        return super.hasCapability(capability, facing);
    }

//    @Override
//    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
//        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T)new ItemHandlerSidedWrapper(this, this.inventory, facing) : super.getCapability(capability, facing);
//    }
//
    @Override
    public boolean canInsert(int slot, ItemStack stack, EnumFacing side) {
        return isItemValid(slot,stack);
    }
    @Override
    public boolean canExtract(int slot, EnumFacing side) {
        return slot == this.SLOT_OUTPUT_1 || slot == this.SLOT_OUTPUT_2;
    }

}
