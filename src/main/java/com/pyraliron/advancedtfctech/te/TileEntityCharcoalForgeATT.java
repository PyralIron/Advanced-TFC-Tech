package com.pyraliron.advancedtfctech.te;

import net.dries007.tfc.objects.inventory.capability.IItemHandlerSidedCallback;
import net.dries007.tfc.objects.inventory.capability.ItemHandlerSidedWrapper;
import net.dries007.tfc.objects.te.TECharcoalForge;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nullable;

public class TileEntityCharcoalForgeATT extends TECharcoalForge implements IItemHandlerSidedCallback {
    public TileEntityCharcoalForgeATT() {
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
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? (T) new ItemHandlerSidedWrapper(this, this.inventory, facing) : super.getCapability(capability, facing);
    }
    public boolean canInsert(int slot, ItemStack stack, EnumFacing side) {
        return isItemValid(slot,stack);
    }

    public boolean canExtract(int slot, EnumFacing side) {
        return true;
    }

}
